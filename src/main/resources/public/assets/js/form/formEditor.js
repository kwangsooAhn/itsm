(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.formEditor = global.formEditor || {})));
}(this, (function (exports) {
    'use strict';
    
    const _contextCompProperties = [
            {"type": "text", "name": "텍스트", "icon": ""},
            {"type": "textarea", "name": "문단", "icon": ""},
            {"type": "list", "name": "리스트 선택", "icon": ""},
            {"type": "radio", "name": "라디오 버튼", "icon": ""},
            {"type": "checkbox", "name": "체크박스", "icon": ""},
            {"type": "label", "name": "라벨", "icon": ""},
            {"type": "image", "name": "이미지", "icon": ""},
            {"type": "line", "name": "라인", "icon": ""},
            {"type": "date", "name": "날짜", "icon": ""},
            {"type": "time", "name": "시간", "icon": ""},
            {"type": "datetime", "name": "일시", "icon": ""},
            {"type": "fileupload", "name": "파일업로드", "icon": ""}
        ],
        _contextMenuProperties = [
            {"type": "delete", "name": "삭제", "icon": ""},
            {"type": "copy", "name": "복사", "icon": ""},
        ],
        _KEYCODE = { ARROW_UP: 38, ARROW_DOWN: 40, ENTER: 13 };
    let _context = null,
        data = {},
        eventHandler = {
        onBoxKeyDownHandler: function(e) {
            if (_context.childLength === 0) return;
            let keyCode = e.keyCode ? e.keyCode : e.which;
            switch (keyCode) {
                case _KEYCODE.ARROW_UP:
                    if (_context.selected !== null) {
                        _context.selected.classList.remove("active");
                        if (_context.selectedIndex > 0) {
                            _context.selected = _context.control.childNodes[_context.selectedIndex - 1];
                            _context.selectedIndex--;
                        } else {
                            _context.selected = _context.control.childNodes[_context.childLength];
                            _context.selectedIndex = _context.childLength;
                        }
                        _context.selected.classList.add("active");
                    } else {
                        _context.selected = _context.control.childNodes[_context.childLength];
                        _context.selectedIndex = _context.childLength;
                        _context.selected.classList.add("active");
                    }
                    break;
                case _KEYCODE.ARROW_DOWN:
                    if (_context.selected !== null) {
                       _context.selected.classList.remove("active");
                        if (_context.selectedIndex < _context.childLength) {
                           _context.selected =_context.control.childNodes[_context.selectedIndex + 1];
                           _context.selectedIndex++;
                        } else {
                           _context.selected = _context.control.childNodes[0];
                           _context.selectedIndex = 0;
                        }
                       _context.selected.classList.add("active");
                    } else {
                       _context.selected = _context.control.childNodes[0];
                       _context.selectedIndex = 0;
                       _context.selected.classList.add("active");
                    }
                    break;
                case _KEYCODE.ENTER:
                    if (_context.selected !== null) {
                        _context.hide();
                        addComponent(_context.selected.dataset.type,_context.selected.dataset.name, this);
                        _context.selectedIndex = 0;
                        _context.selected = null;
                    }
                    break;
            }
            if (keyCode === _KEYCODE.ARROW_UP || keyCode === _KEYCODE.ARROW_DOWN) {
                _context.resize();
            }
        },
        onBoxKeyPressHandler: function(e) {
            let keyCode = e.keyCode ? e.keyCode : e.which;
            if (keyCode === _KEYCODE.ENTER) {
                if (this.textContent.length === 0) {
                    e.preventDefault();
                    if (_context.selected === null) {
                        addBox(true);
                    }
                } else if (this.textContent.length > 0 && this.textContent.charAt(0) === "/") {
                    e.preventDefault();
                }
            }
        },
        onBoxKeyUpHandler: function(e) {
            let keyCode = e.keyCode ? e.keyCode : e.which;
            if (_context.control === null) return;
            if (_context.selected && (keyCode === _KEYCODE.ARROW_UP || keyCode === _KEYCODE.ARROW_DOWN)) return;
            _context.hide();
            if (_context.selected && keyCode === _KEYCODE.ENTER) {
                _context.selected = null;
                _context.selectedIndex = 0;
                return;
            }
            let _this = this;
            let text = _this.textContent;
            if (text.length > 0 && text.charAt(0) !== "/") return;
            
            let rect = _this.getBoundingClientRect();
            let searchTexts = [];
            if (text.length > 0) {
                searchTexts = _context.getContextItems(_contextCompProperties, text);
                if (searchTexts.length > 0) {
                    _context.top = rect.top;
                    _context.left = rect.left;
                    _context.show(searchTexts, _this);
                }
            }
        },
        onFormClickHandler: function(e) {
            _context.hide();
            if (e.target.classList.contains('component') && e.target.dataset.type !== "box" &&
                Component.getSelectedComponentId() !== e.target.id) {
                Component.setSelectedComponentId(e.target.id);
                Component.hidePropertyPanel();
                Component.showPropertyPanel(e.target.id);
            } else {
                Component.setSelectedComponentId("");
                Component.hidePropertyPanel();
            }
        },
        onCompRightClickHandler: function(e) {
            e.preventDefault();
            _context.hide();
            let rect = this.getBoundingClientRect();
            let items = _context.getContextItems(_contextMenuProperties);
            if (items.length > 0) {
                _context.top = rect.top;
                _context.left = rect.left;
                _context.show(items, this);
            }
        }
    };
    /**
     * 컨텍스트 메뉴 
     */
    function ContextMenu() {
        this.control = document.getElementById("component-context");
        if (this.control !== null) { this.control.innerHTML = ""; };
        
        this.top = 0;
        this.left = 0;
        this.selected = null;
        this.childLength = 0;
        this.selectedIndex = 0;
    }
    Object.assign( ContextMenu.prototype, {
        constructor: ContextMenu,
        hide: function() {
            if (this.control.style.display === "block") {
                this.control.innerHTML = "";
                this.control.style.display = "none";
                this.childLength = 0;
            }
        },
        show: function(items, eventElem) {
            for (let i = 0, len = items.length; i < len; i++) {
                let item = items[i];
                let el = document.createElement("li");
                el.className = "context-item";
                el.setAttribute('data-type', item.type);
                el.setAttribute('data-name', item.name);
                el.textContent = item.name;
                el.onclick = function (e) {
                    _context.hide();
                    if (e.target.dataset.type === "delete") {
                        Component.remove(eventElem.id);
                    } else if (e.target.dataset.type === "copy") {
                        Component.copy(eventElem);
                    } else {
                        addComponent(this.dataset.type, this.dataset.name, eventElem);
                    }
                    _context.selectedIndex = 0;
                    _context.selected = null;
                };
                this.control.appendChild(el);
            }
            this.control.style.top = (this.top + 30) + "px";
            this.control.style.left = (this.left + 30) + "px";
            this.control.style.display = "block";
            this.childLength = items.length -1;
        },
        resize: function() {
            let contextHeight = this.control.offsetHeight;
            let contextScrollTop = this.control.scrollTop;
            let viewPort = contextScrollTop + contextHeight;
            let itemHeight = this.control.childNodes[0].offsetHeight;
            let contextOffset = itemHeight * this.selectedIndex;
            if (contextOffset < contextScrollTop || (contextOffset + itemHeight) > viewPort) {
                this.control.scrollTop = contextOffset;
            }
        },
        getContextItems: function (items, search) {
            let result = [];
            search = search || "/";
            let text = search.replace("/", ""); //검색어
            for (let i = 0, len = items.length; i < len; i++) {
                let item = items[i];
                if (search === "/") { //'/'를 입력한 경우 모두 검색
                    result.push(item);
                } else {
                    if (text === item.name.slice(0, text.length)) {
                        result.push(item);
                    }
                }
            }
            return result;
        }
    });
    /**
     * 컴포넌트 신규 추가
     *
     * @function addComponent.
     * @param type 컴포넌트 타입.
     * @param name 컴포넌트 명.
     * @param targetElement 컴포넌트를 추가할 위치를 나타내는 element.
     * @access private.
     */
    function addComponent(type, name, targetElement) {
        let comp = Component.add({type: type, name: name, attrs: {}, isFocus: true, targetElement: targetElement});
        comp.addEventListener( 'contextmenu', eventHandler.onCompRightClickHandler, false);
        let box = document.querySelectorAll("[contenteditable=true]");
        if (box.length === 0 || Component.getLastComponentId() === comp.id) { addBox(false);}
    }
    function addBox(isFocus) {
        let box = Component.add({type: 'box', name: '', isFocus: isFocus});
        box.addEventListener( 'keydown', eventHandler.onBoxKeyDownHandler, false);
        box.addEventListener( 'keypress', eventHandler.onBoxKeyPressHandler, false);
        box.addEventListener( 'keyup', eventHandler.onBoxKeyUpHandler, false);
        box.addEventListener( 'contextmenu', eventHandler.onCompRightClickHandler, false);
    }
    function init(form) {
        console.info('form editor initialization. [FORM ID: ' + form.formId + ']');
        Component.init();
        _context = new ContextMenu();
        
        let formDesigner = document.getElementById("form-designer");
        formDesigner.addEventListener( 'click', eventHandler.onFormClickHandler, false);
        addBox(true);
 
        // add save button event handler.
        document.querySelector('#btnSave').addEventListener('click', saveForm);

        // load form data.
        const xhr = createXmlHttpRequestObject('GET', '/rest/forms/data/' + form.formId);
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    data = JSON.parse(xhr.responseText);
                    setComponents();
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

    /**
     * 조회된 문서양식 데이터로 component 를 추가한다.
     */
    function setComponents() {
        //TODO: add component.
        console.debug(data);
    }

    /**
     * 문서양식 저장.
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
    
    exports.init = init;
    Object.defineProperty(exports, '__esModule', { value: true });
})));