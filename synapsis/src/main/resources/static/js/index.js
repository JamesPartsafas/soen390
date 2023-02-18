//Insert common scripts to this file
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

const selector = document.getElementById('langSelector');
selector.addEventListener('change', function (evt) {
    changeLanguage(this.value);
});

const lang = navigator.language || 'en-EN';
const startLang = Array.from(selector.options).map(opt => opt.value).find(val => lang.includes(val)) || 'en';
changeLanguage(startLang);

selector.selectedIndex = Array.from(selector.options).map(opt => opt.value).indexOf(startLang)

//debugger
document.getElementById('browserLang').innerText = lang;
document.getElementById('startLang').innerText = startLang;