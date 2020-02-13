/**
* @projectDescription Form Desigener Library
*
* @author woodajung
* @version 1.0
* @sdoc js/form/FormComponent.js
*/
(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
    typeof define === 'function' && define.amd ? define(['exports'], factory) :
    (factory((global.formEditor = global.formEditor || {})));
}(this, (function (exports) {
    'use strict';
    
    const context = (function () {
        const KEYCODE = { ARROW_UP: 38, ARROW_DOWN: 40, ENTER: 13, CTRL: 17 };
        
        let menu = null,
            itemInContext = null,
            selectedItems = [],
            selectedItem = null,
            selectedItemIdx = -1,
            isCtrlPressed = false,
            flag = 0;  //0:menu off, 1:menu on
        
        /**
         * context menu on.
         * 
         * @param {Object} state {1=context-menu-control 메뉴 on, 2=context-menu-componet 메뉴 on}
         */
        const toggleMenuOn = function (state) {
            if (flag !== 1) {
                flag = 1;
                menu.classList.add('on');
            }
            menu.scrollTop = 0;
            let controlMenu = menu.querySelector('#context-menu-control');
            let componentMenu = menu.querySelector('#context-menu-componet');
            if (state === 1 && !controlMenu.classList.contains('active')) {
                controlMenu.classList.add('active');
                componentMenu.classList.remove('active');
            } else if (state === 2 && !componentMenu.classList.contains('active')) {
                componentMenu.classList.add('active');
                controlMenu.classList.remove('active');
            }
        };
        
        /**
         * context menu off.
         */
        const toggleMenuOff = function () {
            if (flag !== 0) {
                flag = 0;
                selectedItems = [];
                selectedItem = null;
                selectedItemIdx = -1;
                menu.classList.remove('on');
                let componentMenu = menu.querySelector('#context-menu-componet');
                for (let i = 0, len = componentMenu.children.length; i < len; i++) {
                    let item = componentMenu.children[i];
                    if (item.style.display === 'none') {
                        item.style.display = 'block';
                    }
                    if (item.classList.contains('active')) {
                        item.classList.remove('active');
                    }
                }
            }
        };
        
        /**
         * 검색어와 일치하는 context menu item on.
         * 
         * @param {Object} e 이벤트
         * @param {String} searchText 검색어
         * @return {Boolean}
         */
        const menuItemSearch = function (searchText) {
            let componentMenu = menu.querySelector('#context-menu-componet');
            let rslt = false;
            selectedItems = [];
            for (let i = 0, len = componentMenu.children.length; i < len; i++) {
                let item = componentMenu.children[i];
                if (item.classList.contains('active')) {
                    item.classList.remove('active');
                }
                if (searchText === '/') {
                    item.style.display = 'block';
                    if (!rslt) {
                        selectedItem = item;
                        selectedItem.classList.add('active');
                        selectedItemIdx = 0;
                    }
                    selectedItems.push(item);
                    rslt = true;
                } else {
                    let link = item.querySelector('a');
                    let text = link.textContent || link.innerText;
                    searchText = searchText.replace('/', '');
                    if (text.slice(0, searchText.length).toLowerCase() !== searchText.toLowerCase()) {
                        item.style.display = 'none';
                    } else {
                        item.style.display = 'block';
                        if (!rslt) {
                            selectedItem = item;
                            selectedItem.classList.add('active');
                            selectedItemIdx = 0;
                        }
                        selectedItems.push(item);
                        rslt = true;
                    }
                }
            }
            return rslt;
        };
        /**
         * 특정 클래스 이름을 가진 요소 내부를 클릭했는지 확인.
         * 
         * @param {Object} e 이벤트
         * @param {String} className 클래스명
         * @return {Boolean}
         */
        const clickInsideElement = function(e, className) {
            let el = e.srcElement || e.target;
            if (el.classList.contains(className)) {
                return el;
            } else {
                while (el = el.parentNode) {
                    if (el.classList && el.classList.contains(className)) {
                        return el;
                    }
                }
            }
            return false;
        };
        
        /**
         * 마우스, 키보드 클릭 위치.
         * 
         * @param {Object} e 이벤트
         * @return {Object} 마우스, 키보드 클릭 좌표
         */
        const getPosition = function(e) {
            var posX = 0;
            var posY = 0;
            if (!e) var e = window.event;
            if (e.type === 'keyup') {
                let rect = e.target.getBoundingClientRect();
                posX = rect.left + 15;
                posY = rect.top + 30;
            } else {
                if (e.pageX || e.pageY) {
                    posX = e.pageX;
                    posY = e.pageY;
                } else if (e.clientX || e.clientY) {
                    posX = e.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
                    posY = e.clientY + document.body.scrollTop + document.documentElement.scrollTop;
                }
            }
            return { x: posX, y: posY };
        };
        
        /**
         * 컨텍스트 메뉴 scroll 
         */
        const scrollMenu = function() {
            let contextHeight = menu.offsetHeight;
            let contextScrollTop = menu.scrollTop;
            let viewPort = contextScrollTop + contextHeight;
            let elem = menu.getElementsByClassName('active')[0];
            let itemHeight = elem.firstElementChild.offsetHeight;
            let contextOffset = itemHeight * selectedItemIdx;
            if (contextOffset < contextScrollTop || (contextOffset + itemHeight) > viewPort) {
                menu.scrollTop = contextOffset;
            }
        };
        
        /**
         * 컨텍스트 메뉴 위치 조정.
         * 
         * @param {Object} e 이벤트
         */
        const positionMenu = function(e) {
            let clickCoords = getPosition(e);
            let clickCoordsX = clickCoords.x;
            let clickCoordsY = clickCoords.y;
            
            let menuWidth = menu.offsetWidth + 4;
            let menuHeight = menu.offsetHeight + 4;
            
            let windowWidth = window.innerWidth;
            let windowHeight = window.innerHeight;
            
            if ( (windowWidth - clickCoordsX) < menuWidth ) {
                menu.style.left = windowWidth - menuWidth + 'px';
            } else {
                menu.style.left = clickCoordsX + 'px';
            }
            
            if ( (windowHeight - clickCoordsY) < menuHeight ) {
                menu.style.top = windowHeight - menuHeight + 'px';
            } else {
                menu.style.top = clickCoordsY + 'px';
            }
        };
        
        const onKeyDownHandler = function(e) {
            let keyCode = e.keyCode ? e.keyCode : e.which;
            if (keyCode === KEYCODE.CTRL) { isCtrlPressed = true;}
            if (flag === 1 && selectedItem) {
                let len = selectedItems.length - 1;
                switch (keyCode) {
                    case KEYCODE.ARROW_UP:
                        selectedItem.classList.remove('active');
                        if (selectedItemIdx > 0) {
                            selectedItem = selectedItems[selectedItemIdx - 1];
                            selectedItemIdx--;
                        } else {
                            selectedItem = selectedItems[len];
                            selectedItemIdx = len;
                        }
                        selectedItem.classList.add('active');
                        scrollMenu();
                        break;
                    case KEYCODE.ARROW_DOWN:
                        selectedItem.classList.remove('active');
                        if (selectedItemIdx < len) {
                            selectedItem = selectedItems[selectedItemIdx + 1];
                            selectedItemIdx++;
                        } else {
                            selectedItem = selectedItems[0];
                            selectedItemIdx = 0;
                        }
                        selectedItem.classList.add('active');
                        scrollMenu();
                        break;
                    case KEYCODE.ENTER:
                        menuItemListener(selectedItem);
                        selectedItems = [];
                        selectedItem = null;
                        selectedItemIdx = -1;
                        break;
                }
            }
        };
        
        const onKeyPressHandler = function(e) {
            let keyCode = e.keyCode ? e.keyCode : e.which;
            if (keyCode === KEYCODE.ENTER) {
                if (flag === 0) {
                    e.preventDefault();
                    if (e.target.isContentEditable) {
                        if (e.target.textContent.length === 0 && itemInContext !== null) { 
                            addComponent('editbox'); 
                        }
                        e.target.innerHTML = '';
                    }
                }
            }
        };
        
        const onKeyUpHandler = function(e) {
            let keyCode = e.keyCode ? e.keyCode : e.which;
            if (isCtrlPressed && flag === 1 && itemInContext) { 
                toggleMenuOff();
                itemInContext = null;
            }
            isCtrlPressed = false;
            
            if (selectedItem && (keyCode === KEYCODE.ARROW_UP || keyCode === KEYCODE.ARROW_DOWN)) return;
            if (selectedItem && keyCode === KEYCODE.ENTER) {
                selectedItems = [];
                selectedItem = null;
                selectedItemIdx = 0;
                return;
            }
            itemInContext = clickInsideElement(e, 'component');
            if (itemInContext) {
                let box = itemInContext.querySelector('[contenteditable=true]');
                if (box) {
                    let text = box.textContent;
                    if (text.length > 0 && text.charAt(0) !== '/') return;
                    if (text.length > 0 && menuItemSearch(text)) {
                        toggleMenuOn(2);
                        positionMenu(e);
                    } else {
                        toggleMenuOff();
                    }
                }
            }
        };
        
        const onRightClickHandler = function(e) {
            if (itemInContext) { //기존 eidtbox에서 검색을 시도한 경우 초기화
                let box = itemInContext.querySelector('[contenteditable=true]');
                if (box && box.textContent.length > 0) {
                    box.innerHTML = '';
                }
                flag = 1;
                toggleMenuOff();
            }
            
            itemInContext = clickInsideElement(e, 'component');
            if (itemInContext) {
                e.preventDefault();
                toggleMenuOn(1);
                positionMenu(e);
            } else {
                itemInContext = null;
                toggleMenuOff();
                component.hidePropertyPanel();
            }
        };
        
        const onLeftClickHandler = function(e) {
            let clickedElem = clickInsideElement(e, 'context-item-link');
            if (clickedElem) { //contex 메뉴 클릭
                e.preventDefault();
                menuItemListener(clickedElem);
            } else {
                if (itemInContext) { //기존 eidtbox에서 검색을 시도한 경우 초기화
                    let box = itemInContext.querySelector('[contenteditable=true]');
                    if (box && box.textContent.length > 0) {
                        box.innerHTML = '';
                    }
                    flag = 1;
                    toggleMenuOff();
                    component.hidePropertyPanel();
                }
                let button = e.button ? e.button : e.which;
                if (button === 1) { 
                    itemInContext = clickInsideElement(e, 'component');
                    if (itemInContext) {
                        let compType = itemInContext.getAttribute('data-type');
                        if (isCtrlPressed) { //Ctrl + editbox 클릭시 전체 컴포넌트 리스트 출력
                            if (compType === 'editbox') {
                                toggleMenuOn(2);
                                positionMenu(e);
                            } else {
                                itemInContext = null;
                                toggleMenuOff();
                            }
                        }
                        if (itemInContext !== null && compType !== 'editbox') {
                            component.showPropertyPanel(itemInContext.id);
                        }
                    }
                }
            }
        };
        /**
         * context menu item 클릭 이벤트.
         * 
         * @param {HTMLElement} item 선택된 element
         */
        const menuItemListener = function(item) {
            let clickedComponent = itemInContext;
            switch (item.getAttribute('data-action')) {
                case 'copy':
                    break;
                case 'delete':
                    break;
                case 'addEditboxUp':
                    break;
                case 'addEditboxDown':
                    break;
                default:
                    addComponent(item.getAttribute('data-action'), clickedComponent.id);
            }
            toggleMenuOff();
            itemInContext = null;
        };
        
        const init = function() {
            menu = document.querySelector('#context-menu');
            menu.addEventListener('mousewheel', function (e) { //컨텍스트 메뉴에 스크롤이 잡히도록 추가
                let d = -e.deltaY || e.detail;
                this.scrollTop += ( d < 0 ? 1 : -1 ) * 30;
                e.preventDefault();
            }, false);
            document.addEventListener('keydown', onKeyDownHandler, false);
            document.addEventListener('keypress', onKeyPressHandler, false);
            document.addEventListener('keyup', onKeyUpHandler, false);
            document.addEventListener('contextmenu', onRightClickHandler, false);
            document.addEventListener('click', onLeftClickHandler, false);
            window.onresize = function(e) { toggleMenuOff(); };
        };
        
        return {
            init: init
        };
        
    }());
    
    let data = {};
    
    /**
     * 컴포넌트 신규 추가
     *
     * @param type 컴포넌트 타입
     * @param componentId 컴포넌트 Id
     * @param attrs 컴포넌트 세부 속성
     */
    function addComponent(type, componentId, attrs) {
        attrs = attrs || {};
        if (Object.keys(attrs).length === 0 && JSON.stringify(attrs) === JSON.stringify({})) {
            attrs.isNew = true; //추가
        } else {
            attrs.isNew = false; //수정
        }
        let comp = component.add({type: type, attrs: attrs, componentId: componentId, isFocus: true});
        if (type !== 'editbox') {
            let newElem = component.add({type: 'editbox', isFocus: true});
            let compIdx = comp.getAttribute('data-index');
            if (attrs.isNew && (compIdx + 1) !== newElem.getAttribute('data-index')) { //재정렬
                let elems = document.querySelectorAll('.component');
                let lastElem = newElem.innerHTML;
                let lastElemType = newElem.getAttribute('data-type');
                for (let i = elems.length - 1; i >= compIdx; i--) {
                    let cur = elems[i];
                    let prev = elems[i - 1];
                    if (cur.getAttribute('data-type') !== prev.getAttribute('data-type')) {
                        cur.innerHTML = prev.innerHTML;
                        cur.setAttribute('data-type', prev.getAttribute('data-type'));
                    }
                    if (i == compIdx) {
                        cur.innerHTML = lastElem;
                        cur.setAttribute('data-type', lastElemType);
                        cur.querySelector('.group').focus();
                    }
                }
            }
        }
    }
    
    /**
     * 폼 디자이너 저장
     */
    function saveForm() {
        aliceJs.sendXhr({
            method: 'POST',
            url: '/rest/forms/data',
            callbackFunc: function(xhr) {
                if (xhr.responseText === '1') { //TODO: return 값은 engine 쪽 개발자와 추후 협의 필요!! 현재는 임시로..
                    alert('저장되었습니다.');
                } else {
                    alert('저장실패');
                }
            },
            contentType: 'application/json; charset=utf-8',
            params: JSON.stringify(data)
        });
    }
    
    /**
     * 작업 취소
     */
    function undoForm() {
        //TODO: 작업 취소
    }
    
    /**
     * 작업 재실행
     */
    function redoForm() {
        //TODO: 작업 재실행
    }
    
    /**
     * 미리보기
     */
    function previewForm() {
        //TODO: 미리보기
    }
    
    /**
     * export
     */
    function exportForm() {
        //TODO: export
    }
    
    /**
     * import
     */
    function importForm() {
        //TODO: import
    }
    
    /**
     * 데이터 조회
     * 
     * @param id 컴포넌트 id
     * @return {Object} component 정보
     */
    function getData(id) {
        for (let i = 0, len = formEditor.data.components.length; i < len; i ++) {
            let comp = formEditor.data.components[i];
            if (comp.id === id) { return comp; }
        }
        return null;
    }
    
    /**
     * 데이터 추가/수정
     * 
     * @param obj 컴포넌트 정보
     */
    function changeData(obj) {
        let isExist = false;
        for (let i = 0, len = formEditor.data.components.length; i < len; i ++) {
            let comp = formEditor.data.components[i];
            if (comp.id === obj.id) { 
                //수정
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            //추가
        }
    }
    
    /**
     * 조회된 문서양식 데이터로 component 를 추가한다.
     * 
     * @param data 문서 정보
     */
    function drawWorkflow(data) {
        console.debug(JSON.parse(data));
        formEditor.data = JSON.parse(data);
        document.querySelector('.form-name').textContent = formEditor.data.form.name;
        if (formEditor.data.components.length > 0 ) {
            for (let i = 0, len = formEditor.data.components.length; i < len; i ++) {
                let comp = formEditor.data.components[i];
                addComponent(comp.type, comp.id, comp);
            }
        } else {
            addComponent('editbox');
        }
    }
    
    /**
     * 폼 디자이너 편집 화면 초기화
     *
     * @param form 폼 정보
     */
    function init(form) {
        console.info('form editor initialization. [FORM ID: ' + form.formId + ']');
        workflowUtil.polyfill();
        component.init();
        context.init();
        
        // load form data.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/forms/data/' + form.formId,
            callbackFunc: function(xhr) {
                drawWorkflow(xhr.responseText);
            },
            contentType: 'application/json; charset=utf-8'
        });
    }
    
    exports.init = init;
    exports.save = saveForm;
    exports.undo = undoForm;
    exports.redo = redoForm;
    exports.preview = previewForm;
    exports.exportform = exportForm;
    exports.importform = exportForm;
    exports.getData = getData;
    exports.changeData = changeData;
    
    Object.defineProperty(exports, '__esModule', { value: true });
})));
