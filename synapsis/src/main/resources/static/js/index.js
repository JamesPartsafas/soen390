//Insert common scripts to this file


/**
 * Changes the language to the complement (e.g. EN to FR, FR to EN)
 * @param currentLang The language currently in use.
 */
//Changes the language when toggled via the language selector
function changeLang(currentLang) {
    const elements = document.getElementsByClassName(currentLang)
    if (currentLang == 'langEN') {
        const visibleElements = document.getElementsByClassName('langFR')
        for (const elem of elements) {
            elem.style.display = 'none'
        }
        for (const visibleElem of visibleElements) {
            visibleElem.style.display = 'block'
        }
    }
    if (currentLang == 'langFR') {
        const visibleElements = document.getElementsByClassName('langEN')
        for (const elem of elements) {
            elem.style.display = 'none'
        }
        for (const visibleElem of visibleElements) {
            visibleElem.style.display = 'block'
        }
    }
}

/**
 * Displays the profile picture.
 */
const convertBase64ToImage = () => {
    const base64Text = document.getElementById('base64Image').innerText
    const base64CoverText = document.getElementById('base64CoverImage').innerText
    if (base64Text != "") {
        let profilePictureHolder = document.getElementById('profilePictureHolder')
        profilePictureHolder.src = `data:image/png;base64,${base64Text}`
        document.getElementById('base64Image').innerText = null
    }
    if (base64CoverText != "") {
        let coverPictureHolder = document.getElementById('coverPictureHolder')
        coverPictureHolder.src = `data:image/png;base64,${base64CoverText}`
        document.getElementById('base64Image').innerText = null
    }
}


/**
 * Upon loading the window, retrieves the stored language and set that page to that language.
 * At the same time, creates an event listener for the language selector.
 */
window.onload = function () {
    const selector = document.querySelector('.langSelector')
    let currentLang = localStorage.getItem('localLang')
    if (currentLang == 'langFR') {
        changeLang('langEN')
    } else {
        changeLang('langFR')
    }
    if (selector) {
        selector.addEventListener('click', (event) => {
            if (currentLang == 'langEN') {
                currentLang = 'langFR'
                localStorage.setItem('localLang', 'langEN')
            } else {
                currentLang = 'langEN'
                localStorage.setItem('localLang', 'langFR')
            }
            changeLang(currentLang)
        })
    }
    convertBase64ToImage()
}