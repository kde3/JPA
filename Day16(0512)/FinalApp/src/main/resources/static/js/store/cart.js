{ // 체크박스 전체 선택 이벤트

    let $checkAll = document.querySelector('#check-all');
    let $checkChildren = document.querySelectorAll('.check-box__children');

    $checkAll.addEventListener('change', function () {
        let isChecked = this.checked;

        if(isChecked){
            $checkChildren.forEach(ele => ele.checked=true );
        }else{
            $checkChildren.forEach(ele => ele.checked=false );
        }
    });
}


