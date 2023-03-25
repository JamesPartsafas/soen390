//Insert common scripts to this file

//Adds event listeners for the language selector
window.onload = function() {
    const selector = document.querySelector('.langSelector')
    let currentLang = localStorage.getItem('localLang')
    if (currentLang == 'langFR') {
        changeLang('langEN')
    }
    else {
        changeLang('langFR')
    }
    if (selector) {
        selector.addEventListener('click', (event) => {
            if (currentLang == 'langEN') {
                currentLang = 'langFR'
                localStorage.setItem('localLang', 'langEN')
            }
            else {
                currentLang = 'langEN'
                localStorage.setItem('localLang', 'langFR')
            }
            changeLang(currentLang)
        })
    }
}

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

//Displays profile picture
const convertBase64ToImage = () => {
    const base64Text = document.getElementById('base64Image').innerText
    if (base64Text == "")
        return

    let image = new Image()
    image.src = `data:image/png;base64,${base64Text}`

    let profilePictureHolder = document.getElementById('profilePictureHolder')
    profilePictureHolder.innerHTML = null
    profilePictureHolder.appendChild(image)

    document.getElementById('base64Image').innerText = null
}

convertBase64ToImage()
