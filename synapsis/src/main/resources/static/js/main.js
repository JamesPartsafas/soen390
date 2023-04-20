const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const messageArea = document.querySelector('#messageArea');
const connectingElement = document.querySelector('.connecting');
const fileInput = document.querySelector('#file');
const csrfToken = document.querySelector('meta[name="_csrf"]').content;

let chatId = document.querySelector('#chat').value.trim();
let senderId = document.querySelector('#sender').value.trim();
let receiverId = document.querySelector('#receiver').value.trim();
let chatUser = document.getElementById("chatUser").innerText;

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

    if (stompClient) {
        try {
            const fileName = getFileName();
            const content = messageInput.value.trim();

            if (fileName === null && content.length === 0) {
                return;
            }

            if (fileName?.length > 50) {
                throw new Error('FileName is too long. Max length is 50.')
            }

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

            sendNotification(chatUser, `/chat/${chatId}`, receiverId, 'MESSAGE');

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
        messageArea.insertAdjacentHTML('beforeend', createMessageElement(message));
        initPopovers();
        initModals();
    }
}

/**
 * Generates an HTML element to display a message.
 * @param message The message object to be displayed.
 * @returns {string} The HTML element representing the message.
 */
function createMessageElement(message) {
    if (message.senderId == senderId) {
        return `
        <li>
            <div class="p-2 px-4 m-2 ml-32 md:ml-56 lg:ml-84 xl:ml-96 bg-primary-variant rounded-lg text-right w-1/2 flex flex-row-reverse text-sm md:text-base">
                ${message.content}
                ${generateFileContainerElement(message)}
            </div>
        </li>
    `;
    }
    else {
        return `
        <li>
        <div class="p-2 px-2 m-2 bg-neutral-100 rounded-lg w-1/2 flex flex-row text-sm md:text-base">
            <i class="fa-solid fa-ellipsis-vertical fa-sm p-3 px-4" style="color: #c0c0c0;"
               data-popover-target="popover-click-${message.id}" data-popover-trigger="click" type="button"></i>
            ${message.content}
            ${generateFileContainerElement(message)}
        </div>
                        
            ${message.id !== 0 ?

            `<div data-popover id="popover-click-${message.id}" role="tooltip"
                 class="absolute z-10 invisible inline-block w-18 text-sm text-black-variant transition-opacity
                 duration-300 bg-primary-variant border border-light rounded-lg opacity-0">
                <div class="px-3 py-2 bg-primary-variant border-light rounded-t-lg" data-modal-target="popup-modal-${message.id}" data-modal-toggle="popup-modal-${message.id}">
                    <h3 class="langEN font-semibold text-black-variant">Report</h3>
                    <h3 class="langFR font-semibold text-black-variant">Signaler</h3>
                </div>
                <div data-popper-arrow></div>
            </div>
            <form action="${window.location.origin + '/chat/report'}" method="POST">
            <input type="hidden" name="_csrf" value="${csrfToken}"/>
            <input type="hidden" name="messageID" value="${message.id}" />
            <input type="hidden" name="chatID" value="${chatId}" />
            <div id="popup-modal-${message.id}" tabindex="-1" class="fixed top-0 left-0 right-0 z-50 hidden p-4 overflow-x-hidden overflow-y-auto md:inset-0 h-[calc(100%-1rem)] md:h-full">
                <div class="relative w-full h-full max-w-md md:h-auto">
                    <div class="relative bg-white rounded-lg">
                        <button type="button" class="absolute top-3 right-2.5 text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm p-1.5 ml-auto inline-flex items-center" data-modal-hide="popup-modal-${message.id}">
                            <svg aria-hidden="true" class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd"></path></svg>
                            <span class="sr-only">Close modal</span>
                        </button>
                        <div class="p-6 text-center">
                            <svg aria-hidden="true" class="mx-auto mb-4 text-gray-400 w-14 h-14 " fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                            <p class="langEN mb-5 text-lg font-normal text-gray-500 ">Please note that if you choose to report a message for harassment, up to 5 previous messages from the same conversation will
                                be reviewed by our administrative team in order to assess the situation fully. This is to ensure that appropriate action is taken and that any patterns of behavior are identified.
                                We take harassment very seriously and aim to create a safe and respectful environment for all of our users.
                                By reporting a message, you are helping us to achieve this goal.</p>
                            <p class="langFR mb-5 text-lg font-normal text-gray-500 ">Veuillez noter que si vous choisissez de signaler un message à cause d'un harcèlement, jusqu'à 5 messages précédents de la même 
                                conversation seront examiné par notre équipe administrative afin d'évaluer pleinement la situation. Il s'agit de s'assurer que des mesures appropriées sont prises et que tous les modèles
                                 de comportement sont identifiés. Nous prenons le harcèlement très sérieux et visons à créer un environnement sécurisé et respectueux pour tous nos utilisateurs. En signalant un message, 
                                 vous nous aidez à atteindre notre objectif.</p>
                            <div class="flex justify-center">
                                <button data-modal-hide="popup-modal-${message.id}" type="submit" class="langEN text-white bg-red-600 hover:bg-red-800 focus:ring-4 focus:outline-none focus:ring-red-300  font-medium rounded-lg text-sm inline-flex items-center px-5 py-2.5 text-center mr-2">I accept</button>
                                <button data-modal-hide="popup-modal-${message.id}" type="submit" class="langFR text-white bg-red-600 hover:bg-red-800 focus:ring-4 focus:outline-none focus:ring-red-300  font-medium rounded-lg text-sm inline-flex items-center px-5 py-2.5 text-center mr-2">J'accepte</button>
                                <button data-modal-hide="popup-modal-${message.id}" type="button" class="langEN text-gray-500 bg-white hover:bg-gray-100 focus:ring-4 focus:outline-none focus:ring-gray-200 rounded-lg border border-gray-200 text-sm font-medium px-5 py-2.5 hover:text-gray-900 focus:z-10">I decline</button>
                                <button data-modal-hide="popup-modal-${message.id}" type="button" class="langFR text-gray-500 bg-white hover:bg-gray-100 focus:ring-4 focus:outline-none focus:ring-gray-200 rounded-lg border border-gray-200 text-sm font-medium px-5 py-2.5 hover:text-gray-900 focus:z-10">Je refuse</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            </form>`
            : ''}
        </li>
    `;
    }
}

/**
 * Generates an HTML element for displaying a file attached to the message, if applicable.
 * If the file is an image, it will generate an img tag otherwise an anchor tag will be generated.
 * @param message The message object that may contain file information.
 * @returns {string}  An HTML string containing an image tag or download link for the file, or an empty string if no file is present in the message.
 */
function generateFileContainerElement(message) {
    if (!message.fileName || !message.file) {
        return '';
    }

    const imageExtensions = ['.jpg', '.jpeg', '.png', '.gif', '.bmp'];
    const fileExtension = message.fileName.slice(message.fileName.lastIndexOf('.')).toLowerCase();


    if (imageExtensions.includes(fileExtension)) {
        if (message.senderId == senderId) {
            return `<img src="${message.file}" alt="${message.fileName}}"
                class="p-2 px-4 m-2 ml-32 md:ml-56 lg:ml-84 xl:ml-96 bg-primary-variant rounded-lg text-right w-1/2 flex flex-row-reverse text-sm md:text-base"/>`
        }
        else {
            return `<img src="${message.file}" alt="${message.fileName}}"
                class="p-2 px-2 m-2 bg-neutral-100 rounded-lg w-1/2 flex flex-row text-sm md:text-base"/>`
        }

    } else {
        if (message.senderId == senderId) {
            return `<a href="${message.file}" download="${message.fileName}"
                class="p-2 px-4 m-2 ml-32 md:ml-56 lg:ml-84 xl:ml-96 bg-primary-variant rounded-lg text-right w-1/2 flex flex-row-reverse text-sm md:text-base">
                Download ${message.fileName}</a>`
        }
        else {
            return `<a href="${message.file}" download="${message.fileName}"
                class="p-2 px-2 m-2 bg-neutral-100 rounded-lg w-1/2 flex flex-row text-sm md:text-base">
                Download ${message.fileName}</a>`
        }

    }
}

connect();
