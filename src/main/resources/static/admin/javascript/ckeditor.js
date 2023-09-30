const _ckeditor = document.querySelector('#ckeditor');
if (_ckeditor) {
    ClassicEditor
        .create(_ckeditor)
        .then(editor => {
            window.editor = editor;
        })
        .catch(err => {
            console.error(err.stack);
        });
}