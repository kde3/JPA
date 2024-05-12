
{ // 서브 메뉴 드롭다운

    let $subBg = document.querySelector('.header__sub-menu-bg');
    let $subMenus = document.querySelectorAll('.header__sub-menu');

    document.querySelector('.header').addEventListener('mouseover', function (e) {
        // 검색창에서는 드롭다운 메뉴가 나오면 안됨
        if (e.target.closest('.header__search-top-menu')) { return; }

        $subBg.classList.remove('none');
        $subMenus.forEach(ele => ele.classList.remove('none'));
    });

    document.querySelector('.header').addEventListener('mouseout', function () {
        $subBg.classList.add('none');
        $subMenus.forEach(ele => ele.classList.add('none'));
    });
}

{
    // 검색 아이콘 클릭 이벤트
    document.querySelector(".search-icon-box").addEventListener('click', function () {
        toggleSearchMenu(true);
    });


//    검색 닫기(X) 버튼 클릭 이벤트
    document.querySelector(".header__x-btn").addEventListener('click', function () {
        toggleSearchMenu(false);
    });
}



function toggleSearchMenu(isTrue){
    let $searchMenu = document.querySelector('.header__search-top-menu');
    let $mainMenu = document.querySelector('.header__nav');

    if (isTrue) {
        $searchMenu.classList.remove('none');
        $mainMenu.classList.add('none');
    } else {
        $searchMenu.classList.add('none');
        $mainMenu.classList.remove('none');
    }
}