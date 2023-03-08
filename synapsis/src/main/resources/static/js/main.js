const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const messageArea = document.querySelector('#messageArea');
const connectingElement = document.querySelector('.connecting');

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

function sendMessage(event) {
    event.preventDefault();
    const messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        const chatMessage = {
            id: 0,
            content: messageInput.value,
            type: 'TEXT',
            senderId,
            receiverId
        };

        try {
            stompClient.send(`/app/chat/${chatId}`, {}, JSON.stringify(chatMessage));
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
        messageArea.innerHTML += `<li>${message.content}</li>`;
    }
}

connect();