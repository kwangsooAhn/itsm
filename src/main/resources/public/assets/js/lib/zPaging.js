/**

 *
 * @author Jung Hee Chan
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
const NUM_OF_PAGE_NUMS = 5 // 출력되는 페이징 숫자의 개수

class ZPaging {
    constructor() {
        // 리스트 목록에 옵져버 설정. 조회되어 리스트가 변하면 페이징 처리
        const config = {attribute: true, childList: true, subtree: true};
        const observer = new MutationObserver(this.update);
        observer.observe(document.querySelector('.notice-content'), config);
    }

    update() {
        let currentPageNum = document.querySelector('#currentPageNum').value;
        let totalPageNum = document.querySelector('#totalPageNum').value;

        document.querySelector('#spanTotalCount').innerText = i18n.msg('common.label.count', document.querySelector('#totalCount').value);
        document.querySelector('#spanTotalCountWithoutCondition').innerText =
            i18n.msg(
                'common.label.totalCountWithoutCondition',
                i18n.msg(document.querySelector('#orderType').value),
                document.querySelector('#totalCountWithoutCondition').value
            );

        // 현재 페이징 목록을 가져옴.
        let currentPageList = [];
        document.querySelectorAll('div.PagingNumber').forEach((span) => {
            currentPageList.push(span.innerHTML);
        })

        // 현재 페이지번호가 이미 있는 경우 -> class 입힘.
        if (currentPageList.includes(currentPageNum)) {
            // class add
            return true;
        }

        // 현재 페이지 번호들 삭제.
        document.querySelectorAll('div.PagingNumber span').forEach((span) => {
            span.remove();
        });

        // 페이지 번호 넣기.
        let startPageNum = (Math.floor(currentPageNum / NUM_OF_PAGE_NUMS) * NUM_OF_PAGE_NUMS) + 1
        let endPageNum = (startPageNum + NUM_OF_PAGE_NUMS - 1 > totalPageNum) ? totalPageNum : startPageNum + NUM_OF_PAGE_NUMS - 1;
        for (let i = startPageNum; i <= endPageNum; i++) {
            let newAnchor = document.createElement('a');
            newAnchor.setAttribute('href', 'javascript:getList(' + i + ')')
            newAnchor.innerText = '' + i;
            let newSpan = document.createElement('span');
            newSpan.appendChild(newAnchor);
            document.querySelector('div.PagingNumber').appendChild(newSpan)
        }

        // Start 화살표 처리
        let pagingStartArrow = document.querySelector('#pagingStartArrow');
        pagingStartArrow.className = '';
        pagingStartArrow.setAttribute('href', '');
        if (endPageNum > NUM_OF_PAGE_NUMS) {
            pagingStartArrow.classList.add('active');
            pagingStartArrow.setAttribute('href', 'javascript:getList(1)');
        }

        // Prev 화살표 처리
        let pagingPrevArrow = document.querySelector('#pagingPrevArrow');
        pagingPrevArrow.className = '';
        pagingPrevArrow.setAttribute('href', '');
        if (endPageNum > NUM_OF_PAGE_NUMS) {
            pagingPrevArrow.classList.add('active');
            pagingPrevArrow.setAttribute('href', 'javascript:getList(' + (startPageNum - 1) + ')');
        }

        // End 화살표 처리
        let pagingEndArrow = document.querySelector('#pagingEndArrow');
        pagingEndArrow.className = '';
        pagingEndArrow.setAttribute('href', '');
        if (totalPageNum > endPageNum) {
            pagingEndArrow.classList.add('active');
            pagingEndArrow.setAttribute('href', 'javascript:getList(' + (totalPageNum) + ')');
        }

        // Next 화살표 처리
        let pagingNextArrow = document.querySelector('#pagingNextArrow');
        pagingNextArrow.className = '';
        pagingNextArrow.setAttribute('href', '');
        if (totalPageNum > endPageNum) {
            pagingNextArrow.classList.add('active');
            pagingNextArrow.setAttribute('href', 'javascript:getList(' + (endPageNum + 1) + ')');
        }

        document.querySelector('#spanCurrentPageNum').innerText = i18n.msg('common.label.totalPageNum', totalPageNum);
    }
}

export const zPaging = new ZPaging();
