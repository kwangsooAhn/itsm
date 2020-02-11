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
         * @param {Object} state {1=controls on, 2=components on}
         */
        const toggleMenuOn = function (state) {
            if (flag !== 1) {
                flag = 1;
                menu.classList.add('on');
            }
            menu.scrollTop = 0;
            let controlsMenu = menu.querySelector("#controls");
            let conponetsMenu = menu.querySelector("#conponets");
            if (state === 1 && !controlsMenu.classList.contains('active')) {
                controlsMenu.classList.add('active');
                conponetsMenu.classList.remove('active');
            } else if (state === 2 && !conponetsMenu.classList.contains('active')) {
                conponetsMenu.classList.add('active');
                controlsMenu.classList.remove('active');
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
                let conponetsMenu = menu.querySelector("#conponets");
                for (let i = 0, len = conponetsMenu.children.length; i < len; i++) {
                    let item = conponetsMenu.children[i];
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
            let conponetsMenu = menu.querySelector('#conponets');
            let rslt = false;
            selectedItems = [];
            for (let i = 0, len = conponetsMenu.children.length; i < len; i++) {
                let item = conponetsMenu.children[i];
                if (item.classList.contains('active')) {
                    item.classList.remove('active');
                }
                let link = item.querySelector('a');
                let text = link.textContent || link.innerText;
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
            var posx = 0;
            var posy = 0;
            if (!e) var e = window.event;
            if (e.type === 'keyup') {
                let rect = e.target.getBoundingClientRect();
                posx = rect.left + 15;
                posy = rect.top + 30;
            } else {
                if (e.pageX || e.pageY) {
                    posx = e.pageX;
                    posy = e.pageY;
                } else if (e.clientX || e.clientY) {
                    posx = e.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
                    posy = e.clientY + document.body.scrollTop + document.documentElement.scrollTop;
                }
            }
            return { x: posx, y: posy };
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
                        break;
                    case KEYCODE.ENTER:
                        menuItemListener(selectedItem.querySelector('.context-item-link'));
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
                }
                let button = e.button ? e.button : e.which;
                if (isCtrlPressed && button === 1) { //Ctrl + editbox 클릭시 전체 컴포넌트 리스트 출력
                    itemInContext = clickInsideElement(e, 'component');
                    if (itemInContext) {
                        let compType = itemInContext.getAttribute('data-type');
                        if (compType === 'editbox') {
                            toggleMenuOn(2);
                            positionMenu(e);
                        } else {
                            itemInContext = null;
                            toggleMenuOff();
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
            if (item.getAttribute('data-action') === 'copy') {
                
            } else if (item.getAttribute('data-action') === 'delete') {
                
            } else {
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
     * @function addComponent.
     * @param type 컴포넌트 타입.
     * @param componentId 컴포넌트 Id.
     * @access private.
     */
    function addComponent(type, componentId) {
        let comp = component.add({type: type, attrs: {}, componentId: componentId, isFocus: true});
        let box = document.querySelectorAll('[contenteditable=true]');
        if (box.length === 0 || component.getLastComponentId() === comp.id) {
            component.add({type: 'editbox', isFocus: false})
        }
    }
    
    /**
     * 폼 디자이너 저장
     *
     * @method saveForm
     * @access public
     */
    function saveForm() {
        const xhr = createXmlHttpRequestObject('POST', '/rest/forms/data');
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    if (xhr.responseText === '1') { //TODO: return 값은 engine 쪽 개발자와 추후 협의 필요!! 현재는 임시로..
                        alert('저장되었습니다.');
                    } else {
                        alert('저장실패');
                    }
                } else if (xhr.status === 400) {
                    alert('There was an error 400');
                } else {
                    console.log(xhr);
                    alert('something else other than 200 was returned. ' + xhr.status);
                }
            }
        };
        xhr.setRequestHeader('Content-Type', 'application/json; charset=utf-8');
        xhr.send(JSON.stringify(data));
    }
    /**
     * 작업 취소
     *
     * @method undoForm
     * @access public
     */
    function undoForm() {
        //TODO: 작업 취소
    }
    /**
     * 작업 재실행
     *
     * @method redoForm
     * @access public
     */
    function redoForm() {
        //TODO: 작업 재실행
    }
    /**
     * 미리보기
     *
     * @method previewForm
     * @access public
     */
    function previewForm() {
        //TODO: 미리보기
    }
    /**
     * export
     *
     * @method exportForm
     * @access public
     */
    function exportForm() {
        //TODO: export
    }
    /**
     * import
     *
     * @method importForm
     * @access public
     */
    function importForm() {
        //TODO: import
    }
    /**
     * 조회된 문서양식 데이터로 component 를 추가한다.
     * 
     * @method drawWorkflow
     * @param data 문서 정보
     * @access private
     */
    function drawWorkflow(data) {
        console.debug(JSON.parse(data));
        formEditor.data = JSON.parse(data);
        document.querySelector('.form-name').textContent = formEditor.data.form.name;
        //TODO: Add Component
        //마지막에 빈 컴포넌트를 추가할 수 있도록 추가
        addComponent('editbox');
    }
    /**
     * 폼 디자이너 편집 화면 초기화
     *
     * @function init
     * @param form 폼 정보
     * @access public
     */
    function init(form) {
        console.info('form editor initialization. [FORM ID: ' + form.formId + ']');
        workflowUtil.polyfill();
        component.init();
        context.init();
        
        // load form data.
        const xhr = createXmlHttpRequestObject('GET', '/rest/forms/data/' + form.formId);
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    drawWorkflow(xhr.responseText);
                } else if (xhr.status === 400) {
                    alert('There was an error 400');
                } else {
                    console.log(xhr);
                    alert('something else other than 200 was returned. ' + xhr.status);
                }
            }
        };
        xhr.send();
    }
    
    exports.init = init;
    exports.save = saveForm;
    exports.undo = undoForm;
    exports.redo = redoForm;
    exports.preview = previewForm;
    exports.exportform = exportForm;
    exports.importform = exportForm;
    
    Object.defineProperty(exports, '__esModule', { value: true });
})));
