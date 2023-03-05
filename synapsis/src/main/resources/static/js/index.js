//Insert common scripts to this file

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

//gets language from footer
const selector = document.getElementById('langSelector');
selector.addEventListener('change', function (evt) {
    changeLang(this.value);
});

//set initial language to english
const lang = navigator.language || 'en-EN';
const startLang = Array.from(selector.options).map(opt => opt.value).find(val => lang.includes(val)) || 'EN';
changeLang(startLang);

selector.selectedIndex = Array.from(selector.options).map(opt => opt.value).indexOf(startLang)

//debugger
document.getElementById('browserLang').innerText = lang;
document.getElementById('startLang').innerText = startLang;