var passwordInput = document.getElementById("password");
var openEye = document.getElementById("openEye");
var closedEye = document.getElementById("closedEye");

function toggle(){
    if(passwordInput.type === 'password'){
        passwordInput.type='text';
        closedEye.style.display = "none";
        openEye.style.display = "block";
    }
    else {
        passwordInput.type='password';
        openEye.style.display = "none";
        closedEye.style.display = "block";
    }
}