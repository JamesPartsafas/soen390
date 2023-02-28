//Insert common scripts to this file
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