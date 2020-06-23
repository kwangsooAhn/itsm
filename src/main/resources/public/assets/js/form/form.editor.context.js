/**
* @projectDescription Form Desigener Context menu Library
*
* @author woodajung
* @version 1.0
*/
(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
    typeof define === 'function' && define.amd ? define(['exports'], factory) :
    (factory((global.context = global.context || {})));
}(this, (function (exports) {
    'use strict';

    const keycode = { arrowUp: 38, arrowDown: 40, enter: 13, ctrl: 17 };
    let menu = null,           // 컨텍스트 메뉴
        flag = 0,              // 0:menu off, 1:menu on

        itemInContext = null,  // 해당 element가 컨텍스트 메뉴를 출력하는 객체인지 여부 판단

        searchItems = [],      // '/' 검색시 일치하는 item 리스트
        selectedItem = null,   // 컨텍스트 메뉴 오픈 시 선택된  item
        selectedItemIdx = -1,  // 컨텍스트 메뉴 오픈 시 선택된 item 순서

        isCtrlPressed = false,
        dragComponent = null,
        lastComponent = null;  // 드래그시, 마지막 번째에 drag 되기 위해 추가된 컴포넌트 > drop 이후 삭제 됨


    /**
     * context menu on
     * @param {Object} state {1=control 메뉴 on, 2=component 메뉴 on}
     */
    function menuOn(state) {
        if (flag === 1) { return false; }
        
        flag = 1;
        menu.classList.add('on');
        menu.scrollTop = 0;

        // 비활성 메뉴 활성화
        let controlMenu = document.getElementById('context-menu-control');
        const menuItems = controlMenu.querySelectorAll('li');
        for (let i = 0, len = menuItems.length; i < len; i++) {
            if (menuItems[i].classList.contains('disabled')) {
                menuItems[i].classList.remove('disabled');
            }
        }
        // 메뉴 on
        let componentMenu = document.getElementById('context-menu-component');
        if (state === 1 && !controlMenu.classList.contains('active')) {
            controlMenu.classList.add('active');
            componentMenu.classList.remove('active');
        } else if (state === 2 && !componentMenu.classList.contains('active')) {
            componentMenu.classList.add('active');
            controlMenu.classList.remove('active');
        }

        // editbox 컴포넌트 1개만 존재할 경우 삭제 메뉴 비활성화
        if (document.querySelectorAll('.component').length === 1 &&
            document.querySelector('.component').getAttribute('data-type') === aliceForm.options.defaultType) {
            const controlMenuDeleteItem = controlMenu.querySelector('[data-action="delete"]'); // 컨텍스트 메뉴 - 삭제
            if (!controlMenuDeleteItem.classList.contains('disabled')) {
                controlMenuDeleteItem.classList.add('disabled');
            }
        }

        // 다중 컴포넌트 선택일 경우, 위/아래 컴포넌트 추가 메뉴 비활성화
        if (editor.selectedComponentIds.length > 1) {
            const controlMenuAddUpItem = controlMenu.querySelector('[data-action="addEditboxUp"]'); // 컨텍스트 메뉴 - 위에 editbox 추가
            if (!controlMenuAddUpItem.classList.contains('disabled')) {
                controlMenuAddUpItem.classList.add('disabled');
            }
            const controlMenuAddDownItem = controlMenu.querySelector('[data-action="addEditboxDown"]'); // 컨텍스트 메뉴 - 아래에 editbot 추가
            if (!controlMenuAddDownItem.classList.contains('disabled')) {
                controlMenuAddDownItem.classList.add('disabled');
            }

        }
    }

    /**
     * context menu off
     */
    function menuOff() {
        if (flag === 0) { return false; }

        flag = 0;
        menu.classList.remove('on');
        searchItems = [];
        selectedItem = null;
        selectedItemIdx = -1;

        // 메뉴 검색 초기화
        const componentMenu = document.getElementById('context-menu-component');
        for (let i = 0, len = componentMenu.children.length; i < len; i++) {
            const item = componentMenu.children[i];

            if (item.style.display === 'none') {
                item.style.display = 'block';
            }
            if (item.classList.contains('active')) {
                item.classList.remove('active');
            }
        }
        // 기존 eidtbox에서 검색을 시도한 경우 초기화
        if (itemInContext) {
            const box = itemInContext.querySelector('[contenteditable=true]');
            if (box && box.textContent.length > 0) {
                box.innerHTML = '';
            } 
        }
    }

    /**
     * 검색어와 일치하는 component 메뉴 on
     * @param {String} searchText 검색어
     * @return {Boolean} 검색어와 일치하는 component 메뉴가 존재하면 true 아니면 false
     */
    function menuItemSearch(searchText) {
        const componentMenu = document.getElementById('context-menu-component');
        let rslt = false;
        let tempText = searchText.replace('/', '');
        tempText = tempText.replace(/\s/gi, '').toLowerCase();
        searchItems = [];
        
        for (let i = 0, len = componentMenu.children.length; i < len; i++) {
            const item = componentMenu.children[i];

            if (item.classList.contains('active')) { item.classList.remove('active'); }
            if (searchText === '/') {
                item.style.display = 'block';

                if (!rslt) {
                    selectedItem = item;
                    selectedItem.classList.add('active');
                    selectedItemIdx = 0;
                }
                searchItems.push(item);
                rslt = true;
            } else {
                let text = item.querySelector('label').textContent || item.querySelector('label').innerText;
                text = text.replace(/\s/gi, '').toLowerCase();
                if (text.indexOf(tempText) === -1) {
                    item.style.display = 'none';
                } else {
                    item.style.display = 'block';
                    if (!rslt) {
                        selectedItem = item;
                        selectedItem.classList.add('active');
                        selectedItemIdx = 0;
                    }
                    searchItems.push(item);
                    rslt = true;
                }
            }
        }
        return rslt;
    }

    /**
     * 특정 클래스 이름을 가진 요소 내부를 클릭했는지 확인
     * @param {Object} e 이벤트객체
     * @param {String} className 클래스명
     * @return {Object} 존재하면 객제 반환
     */
    function clickInsideElement(e, className) {
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
        return null;
    }

    /**
     * 마우스, 키보드 클릭 위치
     * @param {Object} e 이벤트
     * @return {Object} 마우스, 키보드 클릭 좌표
     */
    function getPosition(e) {
        if (!e) { e = window.event; }

        let posX = 0,
            posY = 0;

        if (e.type === 'keyup') {
            let rect = e.target.getBoundingClientRect();

            posX = rect.left + 10;
            posY = rect.top + 50;
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
    }
        
    /**
     * 키보드 위 아래 키로 컨텍스트 메뉴 scroll시 위치 재조정
     */
    function setScrollMenu() {
        let contextHeight = menu.offsetHeight;
        let contextScrollTop = menu.scrollTop;
        let viewPort = contextScrollTop + contextHeight;
        let elem = menu.getElementsByClassName('active')[0];
        let itemHeight = elem.querySelector('.active').offsetHeight;
        let contextOffset = itemHeight * selectedItemIdx;

        if (contextOffset < contextScrollTop || (contextOffset + itemHeight) > viewPort) {
            menu.scrollTop = contextOffset;
        }
    }

    /**
     * 컨텍스트 메뉴 위치 재조정
     * @param {Object} e 이벤트
     */
    function setPositionMenu(e) {
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
    }
    
    /**
     * 컴포넌트 drag on
     */
    function componentDragOn() {
        let comps = document.querySelectorAll('.component');
        for (let i = 0, len = comps.length; i < len; i++) {
            let comp = comps[i];
            comp.setAttribute('draggable', 'true');
        }
    }
    
    /**
    * 컴포넌트 drag off
    */
    function componentDragOff() {
        let comps = document.querySelectorAll('.component');
        for (let i = 0, len = comps.length; i < len; i++) {
            let comp = comps[i];
            comp.removeAttribute('draggable');
            
            if (comp.classList.contains('over')) {
                comp.classList.remove('over');
            }
        }
    }
    
    /**
     * keydown 이벤트 핸들러
     * @param {Object} e 이벤트객체
     */
    function onKeyDownHandler(e) {
        let userKeyCode = e.keyCode ? e.keyCode : e.which;
        isCtrlPressed = ( userKeyCode === keycode.ctrl );

        if (flag === 1 && selectedItem) { //컨텍스트 메뉴를 오픈한체 키보드 ↑, ↓, enter 클릭시 동작
            let len = searchItems.length - 1;

            switch (userKeyCode) {
                case keycode.arrowUp:
                    selectedItem.classList.remove('active');

                    if (selectedItemIdx > 0) {
                        selectedItem = searchItems[selectedItemIdx - 1];
                        selectedItemIdx--;
                    } else {
                        selectedItem = searchItems[len];
                        selectedItemIdx = len;
                    }
                    selectedItem.classList.add('active');
                    setScrollMenu();
                    break;
                case keycode.arrowDown:
                    selectedItem.classList.remove('active');

                    if (selectedItemIdx < len) {
                        selectedItem = searchItems[selectedItemIdx + 1];
                        selectedItemIdx++;
                    } else {
                        selectedItem = searchItems[0];
                        selectedItemIdx = 0;
                    }
                    selectedItem.classList.add('active');
                    setScrollMenu();
                    break;
                case keycode.enter:
                    menuItemListener(selectedItem);
                    searchItems = [];
                    selectedItem = null;
                    selectedItemIdx = -1;
                    break;
            }
        }
    }
    
    /**
     * keypress 이벤트 핸들러
     * @param {Object} e 이벤트객체
     */
    function onKeyPressHandler(e) {
        let userKeyCode = e.keyCode ? e.keyCode : e.which;
        if (userKeyCode === keycode.enter && flag === 0) {
            e.preventDefault();
            // editbox 에서 enter키를 입력하면 editbox를 아래 추가한다.
            if (e.target.isContentEditable) {
                if (e.target.textContent.length === 0 && itemInContext !== null) {
                    editor.addComponent();
                }
                e.target.innerHTML = '';
            }
        }
    }
    
    /**
     * keyup 이벤트 핸들러
     * @param {Object} e 이벤트객체
     */
    function onKeyUpHandler(e) {
        isCtrlPressed = false;
        if (clickInsideElement(e, 'alice-form-properties-panel')) { return false; }

        let userKeyCode = e.keyCode ? e.keyCode : e.which;
        if (selectedItem && (userKeyCode === keycode.arrowUp || userKeyCode === keycode.arrowDown)) { return false; }
        if (selectedItem && userKeyCode === keycode.enter) {
            searchItems = [];
            selectedItem = null;
            selectedItemIdx = 0;
            return false;
        }

        itemInContext = clickInsideElement(e, 'component');
        if (itemInContext) { //editbox에 컴포넌트명을 입력하면 컨텍스트 메뉴 출력
            const box = itemInContext.querySelector('[contenteditable=true]');
            if (box) {
                let text = box.textContent;
                if (text.length > 0 && text.charAt(0) !== '/') { return false; }
                if (text.length > 0 && menuItemSearch(text)) {
                    menuOn(2);
                    setPositionMenu(e);
                } else {
                    menuOff();
                }
            }
        }
    }
    
    /**
     * 마우스 우클릭 이벤트 핸들러
     * @param {Object} e 이벤트객체
     */
    function onRightClickHandler(e) {
        menuOff();

        //상단메뉴 및 우측 세부 속성창을 클릭한 경우, 아무 동작도 하지 않는다.
        if (clickInsideElement(e, 'alice-form-properties-panel') || clickInsideElement(e, 'alice-form-toolbar')) { return false; }

        itemInContext = clickInsideElement(e, 'component');
        if (itemInContext) {
            e.preventDefault();
            menuOn(1);
            setPositionMenu(e);
            if (editor.selectedComponentIds.indexOf(itemInContext.id) === -1) {
                editor.selectedComponentIds.length = 0;
                editor.selectedComponentIds.push(itemInContext.id);

                editor.showComponentProperties();
            }
        }
    }
    
    /**
     * 마우스 좌클릭 이벤트 핸들러
     * @param {Object} e 이벤트객체
     */
    function onLeftClickHandler(e) {
        //상단메뉴 및 우측 세부 속성창을 클릭한 경우, 아무 동작도 하지 않는다.
        if (clickInsideElement(e, 'alice-form-properties-panel') || clickInsideElement(e, 'alice-form-toolbar')) {
            if (flag === 1) {
                menuOff();
            }
            return false;
        }

        const clickedElem = clickInsideElement(e, 'context-item-link');
        if (clickedElem) { // control context 메뉴가 오픈된 상태에서 해당 메뉴 선택한 경우
            e.preventDefault();
            menuItemListener(clickedElem);
        } else {
            menuOff();
            if (e.target.classList.contains('alice-form-panel') || e.target.classList.contains('drawing-board')) {
                editor.showFormProperties();
            }
            itemInContext = clickInsideElement(e, 'component');
            if (itemInContext) {
                const box = itemInContext.querySelector('[contenteditable=true]');
                if (box && e.target.classList.contains('add-icon')) { //+ 아이콘 클릭시 전체 컴포넌트 컨텍스트 메뉴
                    menuOn(2);
                    setPositionMenu(e);
                }
                if (isCtrlPressed) {  //배열에 담음
                    const removeIdx = editor.selectedComponentIds.indexOf(itemInContext.id);
                    if (removeIdx === -1) {
                        editor.selectedComponentIds.push(itemInContext.id);
                    } else {
                        editor.selectedComponentIds.splice(removeIdx, 1);
                    }
                } else { //배열 초기화 후 현재 선택된 컴포넌트만 표시
                    editor.selectedComponentIds.length = 0;
                    editor.selectedComponentIds.push(itemInContext.id);
                }
                editor.showComponentProperties();
            }
        }
    }
    
    /**
     * 마우스 down 이벤트 핸들러
     * @param {Object} e 이벤트객체
     */
    function onMouseDownHandler(e) {
        if (e.target.classList.contains('add-icon')) { return false; }
        if (e.target.classList.contains('move-icon')) {
            e.target.parentNode.setAttribute('draggable', 'true');
        }
    }
    
    /**
     * drag 이벤트 핸들러
     * @param {Object} e 이벤트객체
     */
    function onDragStartHandler(e) {
        menuOff();

        dragComponent = e.target;
        if (dragComponent) {
            let dragComponentIndex = editor.getSelectComponentIndex();
            if (editor.selectedComponentIds.indexOf(dragComponent.id) === -1 || dragComponentIndex.length === 0) { // drag할 컴포넌트가 포함되지 않았다면 재선택
                dragComponentIndex.push(Number(dragComponent.getAttribute('data-index')));
                editor.selectedComponentIds.length = 0;
                editor.selectedComponentIds.push(dragComponent.id);
                editor.showComponentProperties();
            }
            //맨 마지막에 추가되길 원할 경우 필요한 drag 영역
            lastComponent = document.createElement('component');
            lastComponent.classList.add('component');
            lastComponent.style.height = '2px';
            dragComponent.parentNode.appendChild(lastComponent);
            
            componentDragOn();

            e.dataTransfer.effectAllowed = 'move';
        }
    }

    function onDragOverHandler(e) {
        if (e.preventDefault) {
            e.preventDefault(); // 필수 이 부분이 없으면 drop 이벤트가 발생하지 않습니다.
        }
        let targetComponent = clickInsideElement(e, 'component');
        if (targetComponent && dragComponent !== targetComponent) {
            let lastCompIndex = component.getLastIndex();
            if (lastCompIndex === Number(dragComponent.getAttribute('data-index')) 
                && targetComponent === lastComponent) { // 맨 마지막에 컴포넌트를 옮길 때 맨 아래 가이드 라인 미출력
                return false; 
            }
            // 연속된 컴포넌트를 선택하여 이동하고자 하는 경우, 선택한 컴포넌트에 마우스 오버시, 라인 미출력
            if (editor.selectedComponentIds.length > 1 && editor.selectedComponentIds.indexOf(targetComponent.id) !== -1) {
                return false;
            }
            
            targetComponent.classList.add('over');
            e.dataTransfer.dropEffect = 'move';
        }
    }

    function onDragEnterHandler(e) {}

    function onDragHandler(e) {}

    function onDragDropHandler(e) {
        if (e.stopPropagation) {
            e.stopPropagation(); 
        }
        let targetComponent = clickInsideElement(e, 'component');
        if (targetComponent && dragComponent !== targetComponent) {
            //같은 위치에 drag 하고자 하는 경우
            let dragIdx = Number(dragComponent.getAttribute('data-index'));
            if (targetComponent !== lastComponent && Number(targetComponent.getAttribute('data-index')) === (dragIdx + 1)) { return false; }

            //연속하여 선택된 컴포넌트 중 하나에 drag 하고자 하는 경우
            if (editor.selectedComponentIds.length > 1 && editor.selectedComponentIds.indexOf(targetComponent.id) !== -1) { return false; }

            let components = document.querySelectorAll('.selected');
            let histories = [];
            for (let i = 0, len = components.length; i < len; i++) {
                let moveComponent =  components[i];
                targetComponent.insertAdjacentHTML('beforebegin', moveComponent.outerHTML);
                targetComponent.parentNode.removeChild(moveComponent);

                const compIdx = editor.getComponentIndex(moveComponent.id);
                histories.push({0: JSON.parse(JSON.stringify(editor.data.components[compIdx])), 1: {}});
            }
            targetComponent.parentNode.removeChild(lastComponent);
            lastComponent = null;
            //재정렬
            editor.reorderComponent();
            componentDragOff();

            for (let i = 0, len = histories.length; i < len; i++) {
                for (let j = 0, compLen = editor.data.components.length; j < compLen; j++) {
                    if (histories[i][0].componentId === editor.data.components[j].componentId) {
                        histories[i][1] = JSON.parse(JSON.stringify(editor.data.components[j]));
                        break;
                    }
                }
            }
            // add history
            editor.history.saveHistory(histories);
        }
    }

    function onDragLeaveHandler(e) {
        let targetComponent = clickInsideElement(e, 'component');
        if (targetComponent && dragComponent !== targetComponent && targetComponent.classList.contains('over')) {
            targetComponent.classList.remove('over');
        }
    }

    function onDragEndHandler(e) {
        componentDragOff();
        
        if (lastComponent !== null) { 
            lastComponent.remove();
            lastComponent = null;
        }
        dragComponent = null;
    }
    
    /**
     * context menu item 클릭시 이벤트 호출
     * @param {HTMLElement} elem 컨텍스트 메뉴의 item 중 선택된 elem 객체
     */
    function menuItemListener(elem) {
        menuOff();
        let clickedComponent = itemInContext;
        editor.hideComponentProperties();
        
        switch (elem.getAttribute('data-action')) {
            case 'copy': //컴포넌트 복사
                editor.copyComponent(clickedComponent.id);
                break;
            case 'delete': //컴포넌트 삭제
                editor.deleteComponent();
                break;
            case 'addEditboxUp': //위에 editbox 컴포넌트 추가
                editor.addEditboxUp(clickedComponent.id);
                break;
            case 'addEditboxDown': //아래에 editbox 컴포넌트 추가
                editor.addEditboxDown(clickedComponent.id);
                break;
            default:
                editor.addComponent(elem.getAttribute('data-action'), clickedComponent.id);
        }
        itemInContext = null;
    }

    /**
     * context menu 초기화 및 이벤트 등록
     */
    function init() {
        menu = document.getElementById('context-menu');
        itemInContext = menu;

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
        
        //컴포넌트 drag & drop 이벤트
        document.addEventListener('mousedown', onMouseDownHandler, false);
        document.addEventListener('dragstart', onDragStartHandler, false);
        document.addEventListener('dragover', onDragOverHandler, false);
        document.addEventListener('dragenter', onDragEnterHandler, false);
        document.addEventListener('drag', onDragHandler, false);
        document.addEventListener('drop', onDragDropHandler, false);
        document.addEventListener('dragleave', onDragLeaveHandler, false);
        document.addEventListener('dragend', onDragEndHandler, false);
        
        window.onresize = function(e) { menuOff(); };
    }
    
    exports.init = init;
    
    Object.defineProperty(exports, '__esModule', { value: true });
})));