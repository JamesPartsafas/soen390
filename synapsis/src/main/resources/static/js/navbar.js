const btn = document.getElementById("mobileButton");
const menu = document.getElementById("hamburger");
const button= document.getElementById("notificationCompany");
const notifMenu = document.getElementById("notificationsDropdownCompany");
btn.addEventListener("click", () => {
    menu.classList.toggle("hidden");
});

button.addEventListener("click", () => {
    notifMenu.classList.toggle("hidden");
});