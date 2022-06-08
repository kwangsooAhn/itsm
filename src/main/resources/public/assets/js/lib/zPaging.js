/**
 * [페이징 리스트] 타입의 화면에서 페이징 및 관련 정보를 컨트롤 한다.
 *
 * @author Jung Hee Chan
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import { PAGING } from './zConstants.js';

const PAGING_DEFAULT_OPTIONS = {
    numOfPageNums : 5, // 출력되는 페이징 숫자의 개수
    activeArrowClass : 'active', // 유효한 화살표용 클래스 이름
    pageNumSelector : 'div.z-paging-numbers a', // 페이지 번호 목록 셀렉터
    selectedPage : 'selected-page' // 선택된 페이지 번호용 클래스 이름
};
export default class ZPaging {
    constructor(options) {
        this.options = Object.assign({}, PAGING_DEFAULT_OPTIONS, options);
        // 리스트 목록에 옵져버 설정. 조회되어 리스트가 변하면 자동으로 페이징 처리
        const config = {childList: true, subtree: false};
        const observer = new MutationObserver(this.update.bind(this));
        observer.observe(document.querySelector('.z-list-content'), config);
    }

    /**
     * 목록이 변경되면 페이징에서 처리 부분은 크게 3가지.
     *   1) 관련 정보 : 검색건수, 전체 데이터 건수, 정렬 순서, 현재 페이지 번호 출력
     *   2) 페이지 번호 목록 : 기존 출력된 페이지 번호 목록은 지우고 새로 페이지 번호 표시
     *   3) 화살표 처리 : 페이지 번호 목록에 따라 페이징 화살표 링크를 다르게 수정.
     *   4) 정렬 추가 : 상단 제목 선택시 데이터 재정렬.
     *
     * @return {boolean}
     */
    update() {
        // 전체 검색건수, 저장된 데이터 전체 건수, 정렬 순서등을 출력
        document.getElementById('spanTotalCount').innerText =
            i18n.msg('common.label.count', document.getElementById('totalCount').value);
        document.getElementById('spanTotalCountWithoutCondition').innerText =
            i18n.msg(
                'common.label.totalCountWithoutCondition',
                document.getElementById('totalCountWithoutCondition').value
            );

        // 정렬
        this.makeSortGridIcon();
        const gridHead = document.querySelector('.grid__head');
        if (gridHead) {
            gridHead.querySelectorAll('.grid__cell[data-grid-sortable="true"]').forEach((element) => {
                element.addEventListener('click', this.sortGrid.bind(this), false);
            });
        }

        // 스크롤바는 paging이 아니지만 옵져버가 새로 계산할 때마다 필요하기 때문에 임시로 이 곳에 작성.
        // 팀장님께서 다시 정리하신다고 하셨음.! by.mo
        OverlayScrollbars(document.querySelector('.z-table-body'), {className: 'scrollbar'});
        OverlayScrollbars(document.querySelector('.z-main'), {className: 'scrollbar'});
        OverlayScrollbars(document.querySelector('.grid__body'), {className: 'scrollbar'});

        // 전체 페이지 개수 확인 (데이터가 아예 없으면 전체 페이지가 0인 것도 감안)
        let totalPageNum = (document.getElementById('totalPageNum').value === '0') ? 1 : Number(document.getElementById('totalPageNum').value);

        // 전체 페이지가 1페이지이면 페이징 표시는 숨김.
        if (totalPageNum === 1) {
            document.querySelector('.z-page-paging').style.visibility = 'hidden';
        } else {
            document.querySelector('.z-page-paging').style.visibility = 'visible';
        }
        // 현재 출력되어 있는 페이징 번호 목록
        let currentPageList = [];
        document.querySelectorAll(this.options.pageNumSelector).forEach((element) => {
            currentPageList.push(element.innerText);
        });

        // 현재 페이지 번호
        let currentPageNum = Number(document.getElementById('currentPageNum').value);
        document.getElementById('spanCurrentPageNum').innerText = i18n.msg('common.label.totalPageNum', totalPageNum);

        // 현재 페이지번호가 이미 있는 경우 -> class 적용만하고 끝냄.
        if (currentPageList.includes(currentPageNum)) {
            document.querySelectorAll(this.options.pageNumSelector).forEach((span) => {
                span.className = '';
                if (span.innerText === currentPageNum) {
                    span.classList.add(this.options.selectedPage);
                }
            });
            return true;
        }

        // 현재 출력되어 있는 페이지 번호 목록 삭제.
        document.querySelectorAll(this.options.pageNumSelector).forEach((span) => {
            span.remove();
        });

        // 페이지 번호 넣기.
        let startPageNum = (Math.floor((currentPageNum - 1) / this.options.numOfPageNums) * this.options.numOfPageNums) + 1;
        let endPageNum = (startPageNum + this.options.numOfPageNums - 1 > totalPageNum) ? totalPageNum : startPageNum + this.options.numOfPageNums - 1;
        for (let i = startPageNum; i <= endPageNum; i++) {
            // anchor tag
            let newAnchor = document.createElement('a');
            newAnchor.setAttribute('href', 'javascript:getList(' + i + ')');
            newAnchor.innerText = '' + i;
            document.querySelector('div.z-paging-numbers').appendChild(newAnchor);
            // 현재 페이지면 클래스 추가
            if (currentPageNum === i) {
                newAnchor.classList.add(this.options.selectedPage);
            }
        }

        // 화살표 처리
        document.querySelectorAll('a.z-paging-arrow').forEach( arrow => {
            arrow.classList.remove(this.options.activeArrowClass);
            arrow.removeAttribute('href');
        });

        // 1) Start 화살표 처리 (제일 앞에 있는 '페이지 목록' 이동)
        if (endPageNum > this.options.numOfPageNums) {
            this.makePagingArrow('pagingStartArrow', 1);
        }

        // 2) Prev 화살표 처리 (바로 앞에 있는 '페이지 목록' 이동)
        if (endPageNum > this.options.numOfPageNums) {
            this.makePagingArrow('pagingPrevArrow', startPageNum - 1);
        }

        // 3) End 화살표 처리 (제일 마지막 '페이지 목록' 이동)
        if (totalPageNum > endPageNum) {
            this.makePagingArrow('pagingEndArrow', totalPageNum);
        }

        // 4) Next 화살표 처리 (바로 다음 '페이지 목록' 이동)
        if (totalPageNum > endPageNum) {
            this.makePagingArrow('pagingNextArrow', endPageNum + 1);
        }
    }

    makePagingArrow(elementId, pageNum) {
        let pagingArrow = document.getElementById(elementId);
        pagingArrow.classList.add(this.options.activeArrowClass);
        pagingArrow.setAttribute('href', 'javascript:getList(' + (pageNum) + ')');
    }

    // 정렬
    sortGrid(e) {
        if (!e.target.classList.contains('grid__cell')) { return false; }

        const sortColumn = e.target.getAttribute('data-grid-column');
        const orderColNameElem = document.getElementById('orderColName');
        const orderDirElem = document.getElementById('orderDir');

        if (!orderColNameElem || !orderDirElem) { return false; }

        if (sortColumn === orderColNameElem.value) { // 정렬 순서 변경
            if (orderDirElem.value === PAGING.ORDER_DIRECTION.ASC) {
                orderDirElem.value = PAGING.ORDER_DIRECTION.DESC;
            } else {
                orderDirElem.value = PAGING.ORDER_DIRECTION.ASC;
            }
        } else { // 정렬 대상 변경
            orderColNameElem.value = sortColumn;
            orderDirElem.value = PAGING.ORDER_DIRECTION.ASC;
        }

        // 데이터 재 호출
        const curPageLink = document.querySelector('.' + this.options.selectedPage);
        if (curPageLink) {
            curPageLink.click();
        }
    }
    // 정렬 아이콘 추가
    makeSortGridIcon() {
        const orderColNameElem = document.getElementById('orderColName');
        const orderDirElem = document.getElementById('orderDir');
        if (!orderColNameElem || !orderDirElem || orderColNameElem.value === '') { return false; }

        const sortIcon = document.querySelector('.grid__cell[data-grid-column="createDt"]');
        if (sortIcon) sortIcon.children[1].remove();

        const sortColElem = document.querySelector('.grid__cell[data-grid-column="' + orderColNameElem.value +'"]');
        if (sortColElem) {
            sortColElem.setAttribute('data-grid-sorting-type', orderDirElem.value);
            sortColElem.insertAdjacentHTML('beforeend', `<span class="z-icon i-sorting"></span>`);
        }
    }
}
