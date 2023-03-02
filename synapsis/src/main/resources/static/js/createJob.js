linkbox=document.getElementById("linkbox");
radioExternal = document.getElementById("external");
function EnableLinkBox(){
    if(radioExternal.checked){
        linkbox.disabled=false;
    }
    else{
        linkbox.disabled=true;
    }
}