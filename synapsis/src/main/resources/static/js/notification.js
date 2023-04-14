let notificationStompClient = null;
let recipientId = document.querySelector('#userId').innerHTML;

function connectNotification() {
    const notificationSocket = new SockJS('/ws');
    notificationStompClient = Stomp.over(notificationSocket);
    notificationStompClient.debug = () => {
    };
    notificationStompClient.connect({}, onNotificationSocketConnected, onError);
}

function onNotificationSocketConnected() {
    notificationStompClient.subscribe(`/user/specific/notification/${recipientId}`, onNotificationReceived);
    updateNotifications();
}

function onError(error) {
    console.log(error);
}

function sendNotification(content, url, recipientId, type) {
    if (notificationStompClient) {
        const notification = {
            id: 0,
            recipientId: recipientId,
            type: type,
            text: content,
            url: url,
            seen: false,
            timestamp: ""
        }
        try {
            notificationStompClient.send(`/app/notification/${recipientId}`, {}, JSON.stringify(notification))
        } catch (e) {
            onError(e);
        }
    } else if (!notificationStompClient) {
        onError("Client not available");
    }
}

function sendSeen(notificationId) {
    if (notificationStompClient) {
        const notification = {
            id: notificationId,
            recipient_id: recipientId,
            text: '',
            url: '',
            seen: true
        }
        try {
            notificationStompClient.send(`/app/notification/${recipientId}/seen`, {}, JSON.stringify(notification))
        } catch (e) {
            onError(e);
        }
    } else if (!notificationStompClient) {
        onError("Client not available");
    }
}

function onNotificationReceived(payload) {
    updateNotifications();
}

function updateNotifications() {
    $.get('/updateNotifications/' + recipientId).done(function (fragment) {
        $("#notification_output").replaceWith(fragment);
        $("#mobile_notification_output").replaceWith(fragment);
    });

    setTimeout(showNotificationPresent, 200)
}

function showNotificationPresent() {
    if (document.querySelector('#unseen')) {
        document.querySelector('#notificationPresent').style.visibility = "visible";
        document.querySelector('#mobileNotificationPresent').style.visibility = "visible";
    }

    let currentLang = localStorage.getItem('localLang');

    if (currentLang == 'langFR') {
        changeNotificationsLang('langEN')
    } else {
        changeNotificationsLang('langFR')
    }
}

function changeNotificationsLang(currentLang) {
    let notifications = document.getElementById("notification_output");
    const elements = notifications.getElementsByClassName(currentLang);
    let visibleElements;
    if (currentLang == 'langEN') {
        visibleElements = notifications.getElementsByClassName('langFR');
    } else if (currentLang == 'langFR') {
        visibleElements = notifications.getElementsByClassName('langEN');
    }
    for (const elem of elements) {
        elem.style.display = 'none';
    }
    for (const visibleElem of visibleElements) {
        visibleElem.style.display = 'block';
    }

}

function callOnlyOnce(fn, context) {
    var result;
    return function () {
        if (fn) {
            result = fn.apply(context || this, arguments);
            fn = null;
        }
        return result;
    };
}

var connectNotifs = callOnlyOnce(connectNotification());

connectNotifs();