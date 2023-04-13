password=document.getElementById('password');
confirm_password=document.getElementById('confirmPassword');
let submitButton = document.getElementById("submit");
function check_password(){

    if(!(password.value.localeCompare(confirm_password.value)==0)){
        document.getElementById("mismatch_password").style.display="inline";
        submitButton.disabled = true;
        return false;
    }
    else{
        submitButton.disabled=false;
        document.getElementById("mismatch_password").style.display="none";
        return true;
    }

}