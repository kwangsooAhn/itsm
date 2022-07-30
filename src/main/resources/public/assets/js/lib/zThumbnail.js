/**
 * @projectDescription 썸네일
 *
 * 공통 모달 라이브러리인 zModal.js 를 기반으로 만들어졌기 때문에 함께 import 한다.
 *
 * @param options 옵션
 *
 * title: '파일 선택'                      모달 제목
 * type: 'file'                          타입 : file, image, icon
 * doubleClickUse: false                 더블클릭으로 이미지 선택 기능 여부
 * isAbsolutePath: true                  전체경로 표시 여부
 * selected: ''                          선택된 경로가 존재할 경우
 *
 * @author Woo Da Jung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
(function(root, factory) {
    if (typeof define === 'function' && define.amd) {
        define([], factory);
    } else if (typeof exports === 'object') {
        module.exports = factory();
    } else {
        root.zThumbnail = factory();
    }
}(this, function() {
    'use strict';

    const defaultOptions = {
        title: 'resource.label.thumbnailTitle',
        type: 'file',
        pageType: 'modal',
        isAbsolutePath: false,
        doubleClickUse: false,
        selected: '',
    };
    const modalTemplate = `<div class="scroll-search">
            <form id="frmSearch" onsubmit="return false;">
                <input type="hidden" id="searchPath" name="searchPath" value=""/>
                <input class="input ic-search col-5 mr-2" type="text" id="searchValue" name="searchValue" 
                    maxlength="100" placeholder="${i18n.msg('resource.label.searchPlaceholder')}">
                <span id="spanTotalCount" class="search-count">/</span>
                <span class="search-count">/</span>
                <span id="spanTotalCountWithoutCondition" class="search-count"></span>
            </form>
            <ol class="breadcrumb flex-row col-pct-12 mt-4" id="breadcrumb"></ol>
        </div>
        <div class="flex-row scroll-list"></div>`.trim();

    return function(options) {
        this.options = Object.assign({}, defaultOptions, options);
        this.className = 'thumbnail__modal--' + this.options.type;
        this.basePath = '';
        this.fileSeparator = '/';
        this.selectedClassName = 'selected';

        // 기본 경로 조회
        this.getBasePath = function(type) {
            return aliceJs.fetchText('/rest/resources/basePath?type=' + type, {
                method: 'GET',
            }).then((response) => {
                this.basePath = response;
            });
        };

        // 파일 구분자 조회
        this.getFileSeparator = function() {
            return aliceJs.fetchText('/rest/resources/fileSeparator', {
                method: 'GET',
            }).then((response) => {
                this.fileSeparator = response;
            });
        };

        // 건수 표시
        this.setCountDisplay = function() {
            // 현재 건수
            const totalCount = this.modal.wrapper.querySelector('#totalCount');
            if (totalCount) {
                this.modal.wrapper.querySelector('#spanTotalCount').textContent =
                    i18n.msg('common.label.count', totalCount.value);
            }

            // 전체 건수
            const totalCountWithoutCondition = this.modal.wrapper.querySelector('#totalCountWithoutCondition');
            if (totalCountWithoutCondition) {
                this.modal.wrapper.querySelector('#spanTotalCountWithoutCondition').textContent =
                    i18n.msg('common.label.totalCountWithoutCondition', totalCountWithoutCondition.value);
            }
        };

        // 브레드 크럼
        this.setBreadCrumb = function(path) {
            const breadcrumb = this.modal.wrapper.querySelector('#breadcrumb');

            // 삭제
            while (breadcrumb.firstChild) {
                breadcrumb.firstChild.remove();
            }

            let breadcrumbTemplate = '';
            // 첫 번째 경로 추가
            breadcrumbTemplate += (
                `<li class="breadcrumb__item flex-row align-items-center">
                    <button type="button" class="btn__text--only" data-path="">
                        ${i18n.msg('resource.label.sharedFolder')}</button>
                    <span class="ic-arrow-right ml-1 mr-1"></span>
                 </li>`
            ).trim();

            const separator = this.fileSeparator;
            const pathArr = path.replace(this.basePath, '').split(separator);
            let startIndex = 1;
            let fullPath = this.basePath;
            // 5depth 이상일 경우 ... 표기
            if (pathArr.length > 4) {
                const shortBreadcrumbTemplate = pathArr.slice(startIndex, (pathArr.length - 2)).map((item) => {
                    fullPath += (separator + item);
                    return (
                        `<li class="dropdown__item" tabindex="-1">
                         <button type="button" class="btn__text--box" data-path="${fullPath}">${item}</button>
                        </li>`
                    ).trim();
                }).join('');

                breadcrumbTemplate += (
                    `<li class="breadcrumb__item flex-row align-items-center">
                        <div class="dropdown-menu">
                            <button type="button" class="btn__ic dropdown__toggle">
                                <span class="ic-meatballs"></span>
                            </button>
                            <ul class="dropdown__content--left">${shortBreadcrumbTemplate}</ul>
                        </div>
                        <span class="ic-arrow-right ml-1 mr-1"></span>
                    </li>`
                ).trim();

                startIndex = (pathArr.length - 2);
            }
            pathArr.slice(startIndex, pathArr.length).map((item) => {
                fullPath += (separator + item);
                breadcrumbTemplate += (
                    `<li class="breadcrumb__item flex-row align-items-center">
                        <button type="button" class="btn__text--only" data-path="${fullPath}"> ${item}</button>
                        <span class="ic-arrow-right ml-1 mr-1"></span>
                    </li>`
                ).trim();

            });
            breadcrumb.insertAdjacentHTML('beforeend', breadcrumbTemplate);

            // 이벤트 등록
            breadcrumb.querySelectorAll('.btn__text--only').forEach((btn) => {
                const path = btn.getAttribute('data-path');
                if (typeof path !== 'undefined' && path !== null) {
                    // 이동
                    btn.addEventListener('click',
                        this.goFolder.bind(this, encodeURIComponent(path)), false);
                }
            });

            const shortBreadCrumb = breadcrumb.querySelector('.dropdown__toggle');
            if (shortBreadCrumb) {
                // 이벤트 등록
                shortBreadCrumb.addEventListener('click', this.toggleContextMenu, false);
                
                shortBreadCrumb.parentNode.querySelectorAll('.btn__text--box').forEach((btn) => {
                    const path = btn.getAttribute('data-path');
                    if (typeof path !== 'undefined' && path !== null) {
                        // 이동
                        btn.addEventListener('click',
                            this.goFolder.bind(this, encodeURIComponent(path)), false);
                    }
                });
            }
        };

        // 이동
        this.goFolder = function(path) {
            const curPath = (path === '') ? this.basePath : decodeURIComponent(path);

            // 동일한 경로를 선택한 경우
            const searchPath = this.modal.wrapper.querySelector('#searchPath');
            if (searchPath.value === curPath) { return false; }

            // 브레드 크럼 경로 변경
            this.setBreadCrumb(curPath);

            // 조회
            searchPath.value = curPath;
            this.getList();
        };

        // 썸네일 선택
        this.selectedThumbnail = function(e) {
            const elem = aliceJs.clickInsideElement(e, 'thumbnail__image');
            if (elem === null) { return false; }

            const selectedElem = this.modal.wrapper.querySelector('.grid__thumbnail.' + this.selectedClassName);
            if (selectedElem) {
                selectedElem.classList.remove(this.selectedClassName);
            }
            elem.parentNode.classList.add(this.selectedClassName);
        };

        // 썸네일 저장
        this.saveThumbnail = function() {
            const selectedElem = this.modal.wrapper.querySelector('.grid__thumbnail.' + this.selectedClassName);
            if (selectedElem === null) {
                zAlert.warning(i18n.msg('validation.msg.fileSelect'));
                return false;
            }

            const target = document.getElementById(this.options.targetId);
            if (target !== null) {
                const fullPath = selectedElem.firstElementChild.getAttribute('data-path');
                this.options.selected = fullPath;
                const relativePath = this.options.isAbsolutePath ? fullPath :
                    fullPath.replace(this.basePath + this.fileSeparator, '');
                target.value = relativePath;

                target.dispatchEvent(new Event('focusout'));
            }
            aliceJs.inputButtonRemove();
            return true;
        };

        // ... 아이콘 클릭시 dropdown 메뉴 오픈
        this.toggleContextMenu = function(e) {
            e.preventDefault();

            if (e.target.classList.contains('on')) {
                e.target.classList.remove('on');
            } else {
                e.target.classList.add('on');
            }
        };

        // 검색시 브레드 크럼 비표시
        this.toggleBreadCrumb = function() {
            // 검색시, 브레드크럼 on off
            const breadcrumb = this.modal.wrapper.querySelector('#breadcrumb');
            if (this.modal.wrapper.querySelector('#searchValue').value === '') {
                breadcrumb.classList.remove('hidden');
                breadcrumb.classList.add('visible');
            } else {
                breadcrumb.classList.remove('visible');
                breadcrumb.classList.add('hidden');
            }
        };

        // 목록 조회
        this.getList = function(pageNum = 0) {
            const urlParam = aliceJs.serialize(document.getElementById('frmSearch'))
                + '&pageNum=' + pageNum
                + '&pageType=' + this.options.pageType
                + '&type=' + this.options.type;
            aliceJs.fetchText('/resources/thumbnail?' + urlParam, {
                method: 'GET',
                showProgressbar: true
            }).then((htmlData) => {
                const dialog = this.modal.wrapper.querySelector('.' + this.className);
                if (pageNum > 0) { // 스크롤
                    const scrollRow = dialog.querySelector('#resources');
                    scrollRow.insertAdjacentHTML('beforeend', htmlData);

                    // 스크롤 검색 결과 업데이트
                    const currentScrollNum = this.modal.wrapper.querySelector('#currentScrollNum');
                    currentScrollNum.value = pageNum;
                    const totalCount = this.modal.wrapper.querySelector('#totalCount');
                    totalCount.value = this.modal.wrapper.querySelectorAll('.grid__thumbnail').length;
                } else {
                    const scrollList = dialog.querySelector('.scroll-list');
                    scrollList.innerHTML = htmlData;

                    // 브레드크럼 생성
                    this.setBreadCrumb(dialog.querySelector('#searchPath').value);
                }
                // 건수 변경
                this.setCountDisplay();

                // 브레드크럼 토글
                this.toggleBreadCrumb();

                // 이벤트 등록
                dialog.querySelectorAll('.thumbnail__image[data-scrollNum="' + pageNum + '"]')
                    .forEach((thumbnail) => {
                        const name = thumbnail.getAttribute('data-name');
                        const path = thumbnail.getAttribute('data-path');
                        if (typeof name !== 'undefined' && name !== null) {
                            // 썸네일 선택
                            thumbnail.addEventListener('click', this.selectedThumbnail.bind(this), false);
                            // 더블 클릭 사용시,
                            if (this.options.doubleClickUse) {
                                thumbnail.addEventListener('dblclick', () => {
                                    this.modal.wrapper.querySelector('.thumbnail__action').click();
                                }, false);
                            }
                        } else {
                            // 폴더 이동
                            thumbnail.addEventListener('click',
                                this.goFolder.bind(this, encodeURIComponent(path)), false);
                        }
                    });

                // 선택된 경로 존재시
                if (this.options.selected !== '') {
                    const tempPath = this.options.selected.replace(this.basePath + this.fileSeparator, '');
                    // 브레드 크럼일치 여부 확인
                    const breadcrumb = dialog.querySelector('#breadcrumb');
                    const breadcrumbPath = breadcrumb.lastElementChild.children[0].getAttribute('data-path');
                    const breadcrumbTempPath = breadcrumbPath.replace(this.basePath + this.fileSeparator, '');
                    const breadcrumbTempPathArr = breadcrumbTempPath.split(this.fileSeparator);
                    const pathArr = tempPath.split(this.fileSeparator);
                    if (pathArr.length === 1 || JSON.stringify(breadcrumbTempPathArr) ===
                        JSON.stringify(pathArr.splice(0, pathArr.length - 1))) {
                        const selected = dialog.querySelector(
                            '.thumbnail__image[data-name="' + pathArr[pathArr.length - 1] + '"]');
                        if (selected && !selected.parentNode.classList.contains(this.selectedClassName)) {
                            selected.parentNode.classList.add(this.selectedClassName);
                        }
                    }
                }
            });
        };

        // 모달 오픈
        this.modal = new modal({
            title: i18n.msg(this.options.title),
            classes: this.className,
            body: modalTemplate,
            buttons: [{
                content: i18n.msg('common.btn.select'),
                classes: 'btn__text--box primary thumbnail__action',
                bindKey: false,
                callback: (modal) => {
                    if (this.saveThumbnail()) {
                        modal.hide();
                    }
                }
            }, {
                content: i18n.msg('common.btn.cancel'),
                classes: 'btn__text--box secondary',
                bindKey: false,
                callback: (modal) => {
                    modal.hide();
                }
            }],
            close: { closable: false },
            onCreate: (modal) => {
                const dialogBody = modal.wrapper.querySelector('.' + this.className + ' .modal__dialog__body');
                OverlayScrollbars(dialogBody, {
                    className: 'scrollbar',
                    callbacks: {
                        onScroll: (event) => {
                            const target = event.target || event;
                            const scrollHeight = target.scrollHeight;
                            const scrollTop = target.scrollTop;
                            const clientHeight = target.clientHeight;
                            const totalScrollNum = Number(dialogBody.querySelector('#totalScrollNum').value);
                            const currentScrollNum = Number(dialogBody.querySelector('#currentScrollNum').value);

                            // 데이터 가져오기
                            if (isScrollbarBottom(scrollHeight, scrollTop, clientHeight) &&
                                currentScrollNum < totalScrollNum) {
                                this.getList(currentScrollNum + 1);
                            }
                        }
                    }
                });

                const basePath = this.getBasePath(this.options.type);
                const fileSeparator = this.getFileSeparator();
                Promise.all([basePath, fileSeparator]).then(() => {
                    dialogBody.querySelector('#searchPath').value = this.basePath;
                    this.getList();
                });

                // 검색 이벤트 등록
                dialogBody.querySelector('#searchValue').addEventListener('keyup', (e) => {
                    if (e.keyCode === 13) {
                        e.preventDefault();
                        // 기존 경로 초기화
                        dialogBody.querySelector('#searchPath').value = this.basePath;

                        this.getList();
                    }
                }, false);
            },
        });
        this.modal.show();
    };
}));