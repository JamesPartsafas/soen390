password=document.getElementById('password');
confirm_password=document.getElementById('confirmPassword');
let button = document.querySelector("button")
function check_password(){

    if(!(password.value.localeCompare(confirm_password.value)==0)){
        document.getElementById("mismatch_password").style.display="inline";
        button.disabled = true;
        return false;
    }
    else{
        button.disabled=false;
        document.getElementById("mismatch_password").style.display="none";
        return true;
    }

}