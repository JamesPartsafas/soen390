//Insert common scripts to this file

//Adds event listeners for the language selector
window.onload = function() {
    localStorage.setItem('localLang', 'EN')
    const selector = document.querySelector('.langSelector')
    let currentLang = localStorage.getItem('localLang')
    if (selector) {
        selector.addEventListener('change', () => {
            changeLang(currentLang)
        })
    }
}

//Changes the language when toggled via the language selector
function changeLang(currentLang) {
    const langCode = 'lang' + currentLang
    const elements = document.getElementsByClassName(langCode)
    if (currentLang == 'EN') {
        const newLangCode = 'langFR'
        const visibleElements = document.getElementsByClassName(newLangCode)
        for (const elem of elements) {
            elem.style.display = 'none'
        }
        for (const visibleElem of visibleElements) {
            visibleElem.style.display = 'inline'
        }
        localStorage.setItem('localLang', 'FR')
    }
    else {
        const newLangCode = 'langEN'
        const visibleElements = document.getElementsByClassName(newLangCode)
        for (const elem of elements) {
            elem.style.display = 'none'
        }
        for (const visibleElem of visibleElements) {
            visibleElem.style.display = 'inline'
        }
        localStorage.setItem('localLang', 'EN')
    }
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
