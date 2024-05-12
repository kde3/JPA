// 서머노트 (에디터 api)
$('#summernote').summernote({
    placeholder: '내용을 작성하세요.',
    tabsize: 2,
    height: 310,
    toolbar: [
        ['style', ['style']],
        ['font', ['bold', 'underline', 'clear']],
        ['color', ['color']],
        ['para', ['ul', 'ol', 'paragraph']],
        ['table', ['table']],
        ['insert', ['link', 'picture', 'video']],
        ['view', ['fullscreen', 'codeview', 'help']]
    ]
});

{   //대표 이미지
    let $fileInput = document.querySelector('#productFile');
    let $imgPreview = document.querySelector('.img-preview');

    $fileInput.addEventListener('change', function () {
        let files = this.files;
        let src = URL.createObjectURL(files[0]);
        $imgPreview.innerHTML = `<img src="${src}" alt=""/>`;
    })
}