{ // 수량 증/감 이벤트
    let $up = document.querySelector('.up');
    let $down = document.querySelector('.down');
    let $inputCnt = document.querySelector('#count');

    $up.addEventListener('click', () => {
        let cnt = parseInt($inputCnt.value);
        $inputCnt.value = ++cnt;
        modifyPrice(cnt);
    });

    $down.addEventListener('click', () => {
        let cnt = parseInt($inputCnt.value);
        if (cnt < 2) {
            alert("수량은 1개 이상을 선택해야합니다.");
            return;
        }
        $inputCnt.value = --cnt;
        modifyPrice(cnt);
    });
}

// 가격 수정 함수
function modifyPrice(count) {
    let $cartPrice = document.querySelector('.cart-price>span');
    let $totalPrice = document.querySelector('#total-price');
    let $price = document.querySelector('#result-price');

    let price = +$price.innerText.replaceAll(',', '');
    let priceWithComma = (price * count).toLocaleString();

    $cartPrice.innerText = priceWithComma;
    $totalPrice.innerText = priceWithComma;
}