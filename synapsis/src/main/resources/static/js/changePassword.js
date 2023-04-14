password=document.getElementById('password');
confirm_password=document.getElementById('confirmPassword');
passwordf=document.getElementById('passwordf');
confirm_passwordf=document.getElementById('confirmPasswordf');
let submitButton = document.getElementById("submit");
let submitButtonF = document.getElementById("submitf");
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

function check_passwordf(){

    if(!(passwordf.value.localeCompare(confirm_passwordf.value)==0)){
        document.getElementById("mismatch_passwordf").style.display="inline";
        submitButtonF.disabled = true;
        return false;
    }
    else{
        submitButtonF.disabled=false;
        document.getElementById("mismatch_passwordf").style.display="none";
        return true;
    }

}