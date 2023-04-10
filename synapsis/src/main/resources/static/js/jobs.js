const filterBtn = document.getElementById("filterButton");
const filterMenu = document.getElementById("filterMenu");
const filterIcon = document.getElementById("filterIcon");

filterBtn.addEventListener("click", () => {
    filterMenu.classList.toggle("hidden");

    if (filterIcon.classList.contains("fa-chevron-down")) {
        filterIcon.classList.remove("fa-chevron-down");
        filterIcon.classList.add("fa-chevron-up");
    } else {
        filterIcon.classList.remove("fa-chevron-up");
        filterIcon.classList.add("fa-chevron-down")
    }
});