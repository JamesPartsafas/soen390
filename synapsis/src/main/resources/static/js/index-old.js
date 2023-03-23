//Insert common scripts to this file

window.onload = function() {
    //sets an event listener to the query selector
    const selector = document.querySelector('.langSelector');
    if (selector) {
        selector.addEventListener('change', () => {
            changeLang(this.value);
        });
    }

    //sets initial language to English
    const lang = navigator.language || 'en-EN';
    const startLang = Array.from(selector.options).map(opt => opt.value).find(val => lang.includes(val)) || 'EN';
    localStorage.setItem("sessionLang", "EN");
    changeLang(startLang);

    selector.selectedIndex = Array.from(selector.options).map(opt => opt.value).indexOf(startLang)

    //debugger
    document.getElementById('browserLang').innerText = lang;
    document.getElementById('startLang').innerText = startLang;
}

//changes language upon selection
function changeLang(langCode) {
    Array.from(document.getElementsByClassName('lang')).forEach(function (elem) {
        if (elem.classList.contains('lang' + langCode)) {
            elem.style.display = 'initial';
        }
        else {
            elem.style.display = 'none';
        }
    });
}




// const convertBase64ToImage = () => {
//     const base64Text = document.getElementById('base64Image').innerText
//     if (base64Text == "")
//         return
//
//     let image = new Image()
//     image.src = `data:image/png;base64,${base64Text}`
//
//     let profilePictureHolder = document.getElementById('profilePictureHolder')
//     profilePictureHolder.innerHTML = null
//     profilePictureHolder.appendChild(image)
//
//     document.getElementById('base64Image').innerText = null
// }
//
// convertBase64ToImage()
