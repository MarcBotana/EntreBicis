document.addEventListener('DOMContentLoaded', function () {
    const imageInput = document.getElementById('imageFile');
    const previewImage = document.getElementById('previewImage');
    const defaultSrc = previewImage?.dataset.defaultSrc;

    if (imageInput && previewImage) {
        imageInput.addEventListener('change', function () {
            const file = this.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    previewImage.src = e.target.result;
                };
                reader.readAsDataURL(file);
            } else {
                previewImage.src = defaultSrc;
            }
        });
    }

    window.deleteImage = function () {
        imageInput.value = ''; 
        previewImage.src = defaultSrc;
    };
});

function deleteImage() {
    document.getElementById('deleteImage').value = 'true';   
    const imageInput = document.getElementById('imageFile');
    const previewImage = document.getElementById('previewImage');
    const defaultSrc = previewImage.dataset.defaultSrc;

    imageInput.value = '';  
    previewImage.src = defaultSrc; 
}