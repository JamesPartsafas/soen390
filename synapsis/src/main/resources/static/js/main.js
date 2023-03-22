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

function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.debug = () => {
    };
    stompClient.connect({}, onConnected, onError);
}

function onConnected() {
    stompClient.subscribe(`/user/queue/chat/${chatId}`, onMessageReceived);
    connectingElement.classList.add('hidden');
    messageForm.addEventListener('submit', sendMessage, true);
}

function onError(error) {
    connectingElement.classList.remove('hidden');
    connectingElement.textContent = `${error}`;
    setTimeout(() => connectingElement.classList.add('hidden'), 30 * 10e3);
}

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

function getFileName() {
    if (!fileInput || fileInput.files?.length === 0) {
        return null;
    }

    return fileInput.files[0].name;
}

function onMessageReceived(payload) {
    const message = JSON.parse(payload.body);

    if (message.type === 'TEXT') {
        sendRead(message.id);
    }

    showMessage(message);
}

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

function createMessageElement(message) {
    return `
        <li class="flex items-center">
            <div class="flex flex-col items-start">
                ${generateFileContainerElement(message)}
                <p>${message.content}</p>
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