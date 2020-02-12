(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
    typeof define === 'function' && define.amd ? define(['exports'], factory) :
    (factory((global.component = global.component || {})));
}(this, (function (exports) {
    'use strict';
    
    const _defaultColWidth = 8.33,  //폼 패널을 12등분하였을때, 1개의 너비
          _defaultPlaceholder= '+ Typing for add component';
    
    let _formPanel = null,
        _propertyPanel = null,
        _lastComponentId = 0,
        _componentIndex = 0,
        _selectedComponentId = '',
        _dragComponent = null,
        _children = [],
        eventHandler = {
            /*onDragStartHandler: function(e) {
                _dragComponent = e.target;
                e.dataTransfer.effectAllowed = 'move';
                e.dataTransfer.setData('text/html', this.innerHTML);
            },
            onDragHandler: function(e) {
            },
            onDragEndHandler: function(e) {
                if (_children.length > 0) {
                    for (let i = 0, len = _children.length; i < len; i ++) {
                       var child = _children[i];
                       child.classList.remove('over');
                    }
                }
            },
            onDragOverHandler: function(e) {
                e.preventDefault(); // 필수 이 부분이 없으면 drop 이벤트가 발생하지 않습니다.
                e.dataTransfer.dropEffect = 'move';
            },
            onDragEnterHandler: function(e) {
                if (_dragComponent !== e.target) {
                    e.target.classList.add('over');
                }
            },
            onDragLeaveHandler: function(e) {
                e.target.classList.remove('over');
            },
            onDragDropHandler: function(e) {
                if (e.stopPropagation) {
                    e.stopPropagation(); 
                }
                if (_dragComponent !== e.target) {
                    let targetType = e.target.dataset.type;
                    let targetName = e.target.dataset.name;
                    if (targetType !== _dragComponent.dataset.type) {
                        if (_dragComponent.dataset.type === 'editbox') {
                            e.target.setAttribute('contenteditable', 'true');
                            e.target.setAttribute('placeholder', _defaultPlaceholder);
                            _dragComponent.removeAttribute('contenteditable');
                            _dragComponent.removeAttribute('placeholder');
                        }
                        if (targetType === 'editbox') {
                            _dragComponent.setAttribute('contenteditable', 'true');
                            _dragComponent.setAttribute('placeholder', _defaultPlaceholder);
                            e.target.removeAttribute('contenteditable');
                            e.target.removeAttribute('placeholder');
                        }
                        e.target.dataset.type = _dragComponent.dataset.type;
                        _dragComponent.dataset.type = targetType;
                        e.target.dataset.name = _dragComponent.dataset.name;
                        _dragComponent.dataset.name = targetName;
                    }
                    _dragComponent.innerHTML = e.target.innerHTML;
                    e.target.innerHTML = e.dataTransfer.getData('text/html');
                }
                return false;
            }*/
    };
    
    /**
     * element 생성
     *
     * @method createElement
     * @param template 템플릿 리터럴
     * @return elem 생성된 element
     * @access private
     */
    function createElement(template) {
        var elem = document.createElement('div');
        elem.classList.add('group');
        elem.innerHTML = template;
        return elem;
    }
    /**
     * 특정 컴포넌트 초회
     *
     * @method getComponentById
     * @param id 조회할 컴포넌트 id
     * @return component 컴포넌트 엘리먼트
     * @access private
     */
    function getComponentById(id) {
       if (_children.length > 0) {
            for (let i = 0, len = _children.length; i < len; i ++) {
               var child = _children[i];
               if (child.id === id) {
                   return child;
               }
            }
        }
        return null;
    }
    /**
     * 컴포넌트 추가
     *
     * @method addComponent
     * @param options 옵션
     * @access public
     */
    function addComponent(options) {
        options = options || {};
        
        let elem = null;
        if (options.componentId !== undefined) {
            elem = _formPanel.querySelector('#' + options.componentId);
            elem.removeChild(elem.childNodes[1]);
            _selectedComponentId = elem.id;
            //TODO: showPropertyPanel(elem.id);
        } else {
            elem = document.createElement('div');
            elem.classList.add('component');
            let compId = workflowUtil.generateUUID();
            _lastComponentId = compId;
            elem.setAttribute('id', 'component_' + compId);
            elem.setAttribute('data-index', (++_componentIndex));
            
            let img = document.createElement('img'); 
            img.classList.add('move-icon');
            elem.appendChild(img);
            _formPanel.appendChild(elem);
            _children.push(elem);
        }
        elem.setAttribute('data-type', options.type);
        let comp = null;
        switch (options.type) {
            case 'text':
                comp = createElement(`
                    <div class='field'>
                        <div class='label'>TEXT</div>
                    </div>
                    <div class='field'>
                        <input type='text' placeholder='' readonly />
                    </div>`);
                comp.firstElementChild.style.flexBasis  = (_defaultColWidth * 2) + '%';
                comp.lastElementChild.style.flexBasis = (_defaultColWidth * 10) + '%';
                elem.appendChild(comp);
                break;
            case 'textarea':
                comp = createElement(`
                    <div class='field'>
                        <div class='label'>TEXT</div>
                    </div>
                    <div class='field'>
                        <textarea placeholder='' rows='4' readonly ></textarea>
                    </div>`);
                comp.firstElementChild.style.flexBasis  = (_defaultColWidth * 2) + '%';
                comp.lastElementChild.style.flexBasis = (_defaultColWidth * 10) + '%';
                elem.appendChild(comp);
                break;
            case 'list':
                comp = createElement(`
                    <div class='field'>
                        <div class='label'>TEXT</div>
                    </div>
                    <div class='field'>
                        <select>
                            <option value=''>Option</option>
                        </select>
                    </div>`);
                comp.firstElementChild.style.flexBasis  = '100%';
                comp.lastElementChild.style.flexBasis = '100%';
                elem.appendChild(comp);
                break;
            case 'radio':
                comp = createElement(`
                    <div class='field'>
                        <div class='label'>TEXT</div>
                    </div>
                    <div class='field'>
                        <input type='radio' name='' value=''/> Option
                    </div>`);
                comp.firstElementChild.style.flexBasis  = '100%';
                comp.lastElementChild.style.flexBasis = '100%';
                elem.appendChild(comp);
                break;
            case 'checkbox':
                comp = createElement(`
                        <div class='field'>
                            <div class='label'>TEXT</div>
                        </div>
                        <div class='field'>
                            <input type='checkbox' name='' value=''/> Option
                        </div>`);
                    comp.firstElementChild.style.flexBasis  = '100%';
                    comp.lastElementChild.style.flexBasis = '100%';
                    elem.appendChild(comp);
                break;
            case 'label':
                comp = createElement(`
                    <div class='field' style='flex-basis: 100%;'>
                        <div class='label'>TEXT</div>
                    </div>`);
                elem.appendChild(comp);
                break;
            case 'image':
                comp = createElement(`
                    <div class='field' style='flex-basis: 100%;'>
                        <img src='' alt=''>
                    </div>`);
                elem.appendChild(comp);
                break;
            case 'line':
                comp = createElement(`
                    <div class='field' style='flex-basis: 100%;'>
                        <hr>
                    </div>`);
                elem.appendChild(comp);
                break;
            case 'date':
                //TODO: datepicker 추가
                comp = createElement(`
                    <div class='field'>
                        <div class='label'>TEXT</div>
                    </div>
                    <div class='field'>
                        <input type='text' placeholder='yyyy-MM-dd' readonly />
                    </div>`);
                comp.firstElementChild.style.flexBasis  = (_defaultColWidth * 2) + '%';
                comp.lastElementChild.style.flexBasis = (_defaultColWidth * 10) + '%';
                elem.appendChild(comp);
                break;
            case 'time':
                //TODO: datepicker 추가
                comp = createElement(`
                    <div class='field'>
                        <div class='label'>TEXT</div>
                    </div>
                    <div class='field'>
                        <input type='text' placeholder='HH:mm:ss' readonly />
                    </div>`);
                comp.firstElementChild.style.flexBasis  = (_defaultColWidth * 2) + '%';
                comp.lastElementChild.style.flexBasis = (_defaultColWidth * 10) + '%';
                elem.appendChild(comp);
                break;
            case 'datetime':
                //TODO: datepicker 추가
                comp = createElement(`
                    <div class='field'>
                        <div class='label'>TEXT</div>
                    </div>
                    <div class='field'>
                        <input type='text' placeholder='yyyy-MM-dd HH:mm:ss' readonly />
                    </div>`);
                comp.firstElementChild.style.flexBasis  = (_defaultColWidth * 2) + '%';
                comp.lastElementChild.style.flexBasis = (_defaultColWidth * 10) + '%';
                elem.appendChild(comp);
                break;
            case 'fileupload':
                comp = createElement(`
                    <div class='field' style='flex-basis: 100%;'>
                        <input type='file' name='files[]' multiple />
                    </div>`);
                elem.appendChild(comp);
                break;
            case 'editbox':
                comp = document.createElement('div');
                comp.classList.add('group');
                comp.setAttribute('contenteditable', 'true');
                comp.setAttribute('placeholder', _defaultPlaceholder);
                elem.appendChild(comp);
                break;
            default:
                console.info('component does not exist.');
        }
        comp.setAttribute('tabIndex', elem.getAttribute('data-index'));
        
        /*elem.addEventListener('dragstart', eventHandler.onDragStartHandler, false);
        elem.addEventListener('drag', eventHandler.onDragHandler, false);
        elem.addEventListener('dragend', eventHandler.onDragEndHandler, false);
        elem.addEventListener('dragover', eventHandler.onDragOverHandler, false);
        elem.addEventListener('dragenter', eventHandler.onDragEnterHandler, false);
        elem.addEventListener('dragleave', eventHandler.onDragLeaveHandler, false);
        elem.addEventListener('drop', eventHandler.onDragDropHandler, false);
        */
        if (options.isFocus) {
            comp.focus();
        }
        return elem;
    }
    /**
     * 컴포넌트 복사
     *
     * @method copyComponent
     * @access public
     */
    function copyComponent() {
        //TODO: 컴포넌트 복사 후 재정렬
    }
    /**
     * 컴포넌트 삭제
     *
     * @method removeComponent
     * @access public
     */
    function removeComponent() {
        //TODO: 컴포넌트 삭제 후 재정렬
    }
    /**
     * 우측 property panel 세부 속성 출력
     *
     * @method showPropertyPanel
     * @param id 조회할 컴포넌트 id
     * @access public
     */
    function showPropertyPanel(id) {
       let component = getComponentById(id);
        let title = document.createElement('div');
        title.classList.add('title');
        title.textContent = component.dataset.name;
        _propertyPanel.appendChild(title);
        
        //TODO:세부속성 출력
    }
    /**
     * 우측 property panel 삭제
     *
     * @method hidePropertyPanel
     * @access public
     */
    function hidePropertyPanel() {
        _propertyPanel.innerHTML = '';
    }
    /**
     * 선택된 컴포넌트 ID 조회
     *
     * @method getSelectedComponentId
     * @access public
     */
    function getSelectedComponentId() {
        return _selectedComponentId;
    }
    /**
     * 선택된 컴포넌트 ID 초기화
     *
     * @method setSelectedComponentId
     * @param id 변경할 id
     * @access public
     */
    function setSelectedComponentId(id) {
        _selectedComponentId = id;
    }
    /**
     * 마지막 추가된 컴포넌트 ID 조회
     *
     * @method getLastComponentId
     * @access public
     */
    function getLastComponentId() {
        return _lastComponentId;
    }
    /**
     * 컴포넌트 초기화
     *
     * @method init
     * @access public
     */
    function init() {
        _formPanel = document.getElementById('panel-form');
        _propertyPanel = document.getElementById('panel-property');
    }
    
    exports.init = init;
    exports.add = addComponent;
    exports.copy = copyComponent;
    exports.remove = removeComponent;
    exports.setSelectedComponentId = setSelectedComponentId;
    exports.getSelectedComponentId = getSelectedComponentId;
    exports.getLastComponentId = getLastComponentId;
    exports.showPropertyPanel = showPropertyPanel;
    exports.hidePropertyPanel = hidePropertyPanel;
    
    Object.defineProperty(exports, '__esModule', { value: true });
})));
