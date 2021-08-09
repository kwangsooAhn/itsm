/**
 * [페이징 리스트] 타입의 화면에서 페이징 및 관련 정보를 컨트롤 한다.
 *
 * @author Jung Hee Chan
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
const DEFAULT_OPTIONS = {
    numOfPageNums : 5, // 출력되는 페이징 숫자의 개수
    activeArrowClass : 'active', // 유효한 화살표용 클래스 이름
    pageNumSelector : 'div.z-pagingNumbers a', // 페이지 번호 목록 셀렉터
    selectedPage : 'selectedPage' // 선택된 페이지 번호용 클래스 이름
}
class ZPaging {
    constructor(options) {
        this.options = Object.assign({}, DEFAULT_OPTIONS, options);
        // 리스트 목록에 옵져버 설정. 조회되어 리스트가 변하면 자동으로 페이징 처리
        const config = {childList: true, subtree: true};
        const observer = new MutationObserver(this.update.bind(this));
        observer.observe(document.querySelector('.list-content'), config);
    }

    /**
     * 목록이 변경되면 페이징에서 처리 부분은 크게 3가지.
     *   1) 관련 정보 : 검색건수, 전체 데이터 건수, 정렬 순서, 현재 페이지 번호 출력
     *   2) 페이지 번호 목록 : 기존 출력된 페이지 번호 목록은 지우고 새로 페이지 번호 표시
     *   3) 화살표 처리 : 페이지 번호 목록에 따라 페이징 화살표 링크를 다르게 수정.
     *
     * @return {boolean}
     */
    update() {
        // 1) 전체 검색건수, 저장된 데이터 전체 건수, 정렬 순서등을 출력
        document.getElementById('spanTotalCount').innerText = i18n.msg('common.label.count', document.getElementById('totalCount').value);
        document.getElementById('spanTotalCountWithoutCondition').innerText =
            i18n.msg(
                'common.label.totalCountWithoutCondition',
                i18n.msg(document.getElementById('orderType').value),
                document.getElementById('totalCountWithoutCondition').value
            );

        // 2) 페이지 번호 목록 처리
        // 2-1) 현재 출력되어 있는 페이징 번호 목록
        let currentPageList = [];
        document.querySelectorAll(this.options.pageNumSelector).forEach((element) => {
            currentPageList.push(element.innerText);
        });

        // 2-2) 현재 페이지 번호, 전체 페이지 수
        let currentPageNum = Number(document.getElementById('currentPageNum').value);
        let totalPageNum = Number(document.getElementById('totalPageNum').value);
        document.getElementById('spanCurrentPageNum').innerText = i18n.msg('common.label.totalPageNum', totalPageNum);

        // 2-3) 현재 페이지번호가 이미 있는 경우 -> class 적용만하고 끝냄.
        if (currentPageList.includes(currentPageNum)) {
            document.querySelectorAll(this.options.pageNumSelector).forEach((span) => {
                span.className = '';
                if (span.innerText === currentPageNum) {
                    span.classList.add(this.options.selectedPage);
                }
            })
            return true;
        }

        // 2-4) 현재 출력되어 있는 페이지 번호 목록 삭제.
        document.querySelectorAll(this.options.pageNumSelector).forEach((span) => {
            span.remove();
        });

        // 2-5) 페이지 번호 넣기.
        let startPageNum = (Math.floor((currentPageNum - 1) / this.options.numOfPageNums) * this.options.numOfPageNums) + 1;
        let endPageNum = (startPageNum + this.options.numOfPageNums - 1 > totalPageNum) ? totalPageNum : startPageNum + this.options.numOfPageNums - 1;
        for (let i = startPageNum; i <= endPageNum; i++) {
            // anchor tag
            let newAnchor = document.createElement('a');
            newAnchor.setAttribute('href', 'javascript:getList(' + i + ')')
            newAnchor.innerText = '' + i;
            document.querySelector('div.z-pagingNumbers').appendChild(newAnchor)
            // 현재 페이지면 클래스 추가
            if (currentPageNum === i) {
                newAnchor.classList.add(this.options.selectedPage);
            }
        }

        // 3) 화살표 처리
        // 3-1) Start 화살표 처리 (제일 앞에 있는 '페이지 목록' 이동)
        let pagingStartArrow = document.getElementById('pagingStartArrow');
        pagingStartArrow.classList.remove(this.options.activeArrowClass);
        pagingStartArrow.removeAttribute('href');
        if (endPageNum > this.options.numOfPageNums) {
            pagingStartArrow.classList.add(this.options.activeArrowClass);
            pagingStartArrow.setAttribute('href', 'javascript:getList(1)');
        }

        // 3-2) Prev 화살표 처리 (바로 앞에 있는 '페이지 목록' 이동)
        let pagingPrevArrow = document.getElementById('pagingPrevArrow');
        pagingPrevArrow.classList.remove(this.options.activeArrowClass);
        pagingPrevArrow.removeAttribute('href');
        if (endPageNum > this.options.numOfPageNums) {
            pagingPrevArrow.classList.add(this.options.activeArrowClass);
            pagingPrevArrow.setAttribute('href', 'javascript:getList(' + (startPageNum - 1) + ')');
        }

        // 3-3) End 화살표 처리 (제일 마지막 '페이지 목록' 이동)
        let pagingEndArrow = document.getElementById('pagingEndArrow');
        pagingEndArrow.classList.remove(this.options.activeArrowClass);
        pagingEndArrow.removeAttribute('href');
        if (totalPageNum > endPageNum) {
            pagingEndArrow.classList.add(this.options.activeArrowClass);
            pagingEndArrow.setAttribute('href', 'javascript:getList(' + (totalPageNum) + ')');
        }

        // 3-4) Next 화살표 처리 (바로 다음 '페이지 목록' 이동)
        let pagingNextArrow = document.getElementById('pagingNextArrow');
        pagingNextArrow.classList.remove(this.options.activeArrowClass);
        pagingNextArrow.removeAttribute('href');
        if (totalPageNum > endPageNum) {
            pagingNextArrow.classList.add(this.options.activeArrowClass);
            pagingNextArrow.setAttribute('href', 'javascript:getList(' + (endPageNum + 1) + ')');
        }
    }
}
