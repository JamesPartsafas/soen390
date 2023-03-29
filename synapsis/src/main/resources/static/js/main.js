const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const messageArea = document.querySelector('#messageArea');
const connectingElement = document.querySelector('.connecting');
const fileInput = document.querySelector('#file');
const csrfToken = document.querySelector('meta[name="_csrf"]').content;

let chatId = document.querySelector('#chat').value.trim();
let senderId = document.querySelector('#sender').value.trim();
let receiverId = document.querySelector('#receiver').value.trim();

let stompClient = null;

/**
 * This function creates a new instance of the SockJS WebSocket client, and passes it to StompJS. The resulting
 * StompJS client connects to the server with the specified URL ("/ws"), and calls the appropriate callback
 * function upon success or failure. Thus, connecting to the WebSocket server using SockJS and StompJS.
 */
function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.debug = () => {
    };
    stompClient.connect({}, onConnected, onError);
}

/**
 * Callback function called upon successful connection to the WebSocket server.
 * This function subscribes the StompJS client to the specified chat room, adds a listener to the message form
 * for sending messages, and hides the connecting element from the user interface.
 */
function onConnected() {
    stompClient.subscribe(`/user/queue/chat/${chatId}`, onMessageReceived);
    connectingElement.classList.add('hidden');
    messageForm.addEventListener('submit', sendMessage, true);
}

/**
 * Callback function called upon connection error to the WebSocket server including disconnections.
 * This function displays the error message in the connecting element and shows it to the user interface.
 * The connecting element is automatically hidden after 30 seconds.
 * @param error The error message received upon connection failure.
 */
function onError(error) {
    connectingElement.classList.remove('hidden');
    connectingElement.textContent = `${error}`;
    setTimeout(() => connectingElement.classList.add('hidden'), 30 * 10e3);
}

/**
 * Sends a chat message to the WebSocket server.
 * This function sends a chat message to the WebSocket server upon successful form submission. It performs
 * client-side validation of the message content and file attachment, and displays error messages to the user
 * if necessary. Upon successful message sending, it sends a notification to the receiver and displays the
 * message in the chat window.
 * @param event The submit event object of the message form.
 * @returns {Promise<void>} not used
 */
async function sendMessage(event) {
    event.preventDefault();
    const messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        try {
            const fileName = getFileName();

            if (fileName?.length > 50) {
                throw new Error('FileName is too long. Max length is 50.')
            }

            const content = messageInput.value;

            if (content.length > 255) {
                throw new Error('Message is too long. Max length is 255.')
            }

            const chatMessage = {
                id: 0,
                content,
                type: 'TEXT',
                senderId,
                receiverId,
                file: await getFileBase64(),
                fileName
            };

            const chatMessageStr = JSON.stringify(chatMessage);

            if (chatMessageStr.length > 65536) {
                throw new Error('Message size exceeds limit of 65KB.')
            }

            stompClient.send(`/app/chat/${chatId}`, {}, chatMessageStr);
            fileInput.value = '';

            sendNotification("You have a message.", `/chat/${chatId}`, receiverId, 'MESSAGE');
            showMessage(chatMessage);
        } catch (e) {
            onError(e);
        }

        messageInput.value = '';
    } else if (!stompClient) {
        onError("Client not available");
    }
}

/**
 * Sends a read receipt to the WebSocket server for a specific message.
 * This function sends a read receipt message to the WebSocket server for the specified message ID, indicating
 * that the message has been read by the user. If the StompJS client is not available, an error message is displayed
 * to the user.
 * @param messageId The ID of the message to send the read receipt for.
 */
function sendRead(messageId) {
    if (stompClient) {
        const readMessage = {
            id: messageId,
            content: '',
            type: 'READ',
            senderId,
            receiverId
        };

        try {
            stompClient.send(`/app/chat/${chatId}/read`, {}, JSON.stringify(readMessage))
        } catch (e) {
            onError(e);
        }
    } else if (!stompClient) {
        onError("Client not available");
    }
}

/**
 * Reads a file from the file input and returns its Base64-encoded string representation.
 * This function reads a file from the file input and returns its Base64-encoded string representation.
 * If no file is selected, it resolves with a null value. It also performs client-side validation of the file size,
 * and rejects the promise if the file size exceeds the maximum limit.
 * @returns {Promise<null|FileReader|string>}
 */
function getFileBase64() {
    return new Promise((resolve, reject) => {
        if (!fileInput || fileInput.files?.length === 0) {
            resolve(null);
            return;
        }

        const file = fileInput.files[0];

        if (file.size > 48448) {
            reject('File size is larger than the limit of 48KB.');
            return;
        }

        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = error => reject(error);
    })
}

/**
 * Retrieves the name of the file selected in the file input.
 * This function retrieves the name of the file selected in the file input, or returns null if no file is selected.
 * @returns {null|string} The name of the file or null
 */
function getFileName() {
    if (!fileInput || fileInput.files?.length === 0) {
        return null;
    }

    return fileInput.files[0].name;
}

/**
 * Handles the receipt of a new message from the WebSocket server.
 * This function is called when a new message is received from the WebSocket server. It extracts the message
 * information from the payload, sends a read receipt for text messages, and displays the message on the UI.
 * @param payload The message payload received from the server.
 */
function onMessageReceived(payload) {
    const message = JSON.parse(payload.body);

    if (message.type === 'TEXT') {
        sendRead(message.id);
    }

    showMessage(message);
}

/**
 * Displays a message on the UI.
 * This function displays a message on the UI. If the message is a read receipt, it performs some frontend
 * operations to show the receipt on the UI, otherwise it creates an HTML element from the message object
 * and appends it to the message area. If there is an error, it will call the onError function.
 * @param message The message object to display.
 */
function showMessage(message) {
    if (message.type === 'READ') {
        // Do frontend magic here (the message contains an ID which will be the last read message)
        console.log(`READ MESSAGE: ${message.id}`);
    } else if (message.type === 'ERROR') {
        onError(`Could not send message: ${message.content}`);
    } else {
        messageArea.innerHTML += createMessageElement(message);
    }
}

/**
 * Generates an HTML element to display a message.
 * @param message The message object to be displayed.
 * @returns {string} The HTML element representing the message.
 */
function createMessageElement(message) {
    return `
        <li>
            <div class="p-2 px-4 m-2 ml-96 bg-neutral-100 rounded-lg text-right w-1/2 flex flex-row-reverse">
                ${message.content}
            </div>
            ${message.id !== 0 ? 
        
                `<form action="${window.location.origin + '/chat/report'}" method="POST">
                    <input type="hidden" name="_csrf" value="${csrfToken}"/>
                    <input type="hidden" name="messageID" value="${message.id}" />
                    <input type="hidden" name="chatID" value="${chatId}" />
                    <button class="btn bg-primary btn-primary font-sans text-sm w-20 h-9 p-0" type="submit">Report</button>
                </form>`
            : ''}
        </li>
    `;
}

/**
 * Generates an HTML element for displaying a file attached to the message, if applicable.
 * If the file is an image, it will generate an img tag otherwise an anchor tag will be generated.
 * @param message The message object that may contain file information.
 * @returns {string}  An HTML string containing an image tag or download link for the file, or an empty string if no file is present in the message.
 */
function generateFileContainerElement(message) {
    if (!message.fileName || !message.file){
        return '';
    }

    const imageExtensions = ['.jpg', '.jpeg', '.png', '.gif', '.bmp'];
    const fileExtension = message.fileName.slice(message.fileName.lastIndexOf('.')).toLowerCase();

    if (imageExtensions.includes(fileExtension)) {
        return `<img src="${message.file}" alt="${message.fileName}}"/>`
    } else {
        return `<a href="${message.file}" download="${message.fileName}">Download ${message.fileName}</a>`
    }
}

connect();