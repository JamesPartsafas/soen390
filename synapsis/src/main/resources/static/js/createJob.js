linkbox=document.getElementById("externalLink");
radioExternal = document.getElementById("external");
function EnableLinkBox(){
    if(radioExternal.checked){
        linkbox.disabled=false;
    }
    else{
        linkbox.disabled=true;
    }
}