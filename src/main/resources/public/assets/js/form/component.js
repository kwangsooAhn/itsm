(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
    typeof define === 'function' && define.amd ? define(['exports'], factory) :
    (factory((global.component = global.component || {})));
}(this, (function (exports) {
    'use strict';
    
    const _defaultColWidth = 8.33,  //폼 패널을 12등분하였을때, 1개의 너비
          _defaultPlaceholder= '+ Typing for add component',
          _compProperties = [  //세부속성에서 사용할 제목
              {'type': 'text', 'name': 'Text', 'icon': ''},
              {'type': 'textarea', 'name': 'Text Box', 'icon': ''},
              {'type': 'list', 'name': 'Dropdown', 'icon': ''},
              {'type': 'radio', 'name': 'Radio Button', 'icon': ''},
              {'type': 'checkbox', 'name': 'Checkbox', 'icon': ''},
              {'type': 'label', 'name': 'Label', 'icon': ''},
              {'type': 'image', 'name': 'Image', 'icon': ''},
              {'type': 'line', 'name': 'Line', 'icon': ''},
              {'type': 'date', 'name': 'Date', 'icon': ''},
              {'type': 'time', 'name': 'Time', 'icon': ''},
              {'type': 'datetime', 'name': 'Date Time', 'icon': ''},
              {'type': 'fileupload', 'name': 'Fileupload', 'icon': ''}
          ];
    
    let formPanel = null,
        propertyPanel = null,
        lastComponentId = 0,
        componentIndex = 0,
        selectedComponentId = '',
        attr = {},
        dragComponent = null,
        //_children = [],
        eventHandler = {
            /*onDragStartHandler: function(e) {
                dragComponent = e.target;
                e.dataTransfer.effectAllowed = 'move';
                e.dataTransfer.setData('text/html', this.innerHTML);
            },
            onDragHandler: function(e) {
            },
            onDragEndHandler: function(e) {
                if (_children.length > 0) {
                    for (let i = 0, len = _children.length; i < len; i ++) {
                       let child = _children[i];
                       child.classList.remove('over');
                    }
                }
            },
            onDragOverHandler: function(e) {
                e.preventDefault(); // 필수 이 부분이 없으면 drop 이벤트가 발생하지 않습니다.
                e.dataTransfer.dropEffect = 'move';
            },
            onDragEnterHandler: function(e) {
                if (dragComponent !== e.target) {
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
                if (dragComponent !== e.target) {
                    let targetType = e.target.dataset.type;
                    let targetName = e.target.dataset.name;
                    if (targetType !== dragComponent.dataset.type) {
                        if (dragComponent.dataset.type === 'editbox') {
                            e.target.setAttribute('contenteditable', 'true');
                            e.target.setAttribute('placeholder', _defaultPlaceholder);
                            dragComponent.removeAttribute('contenteditable');
                            dragComponent.removeAttribute('placeholder');
                        }
                        if (targetType === 'editbox') {
                            dragComponent.setAttribute('contenteditable', 'true');
                            dragComponent.setAttribute('placeholder', _defaultPlaceholder);
                            e.target.removeAttribute('contenteditable');
                            e.target.removeAttribute('placeholder');
                        }
                        e.target.dataset.type = dragComponent.dataset.type;
                        dragComponent.dataset.type = targetType;
                        e.target.dataset.name = dragComponent.dataset.name;
                        dragComponent.dataset.name = targetName;
                    }
                    dragComponent.innerHTML = e.target.innerHTML;
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
     */
    function createElement(template) {
        let elem = document.createElement('div');
        elem.classList.add('group');
        elem.innerHTML = template;
        return elem;
    }
    
    /**
     * 컴포넌트 추가
     *
     * @param options 옵션
     */
    function addComponent(options) {
        options = options || {};
        
        let elem = null;
        if (options.componentId !== undefined) {
            elem = document.getElementById(options.componentId);
        }
        if (elem) { //editbox 삭제 후 컴포넌트 추가
            elem.removeChild(elem.childNodes[1]);
            /*let obj = {
                id: options.componentId,
                type: options.type,
            };
            let detailAttr = component.attr[options.type];
            Object.keys(detailAttr).forEach(function(pAttr) {
                obj[pAttr] = {};
                Object.keys(detailAttr[pAttr]).forEach(function(attr) {
                    obj[pAttr][detailAttr[pAttr][attr].id] = detailAttr[pAttr][attr].value;
                });
            });
            obj.display.order = elem.getAttribute('data-index');
            formEditor.changeData(obj);*/
        } else {
            elem = document.createElement('div');
            elem.classList.add('component');
            let compId = (options.componentId !== undefined) ? options.componentId  : workflowUtil.generateUUID();
            lastComponentId = compId;
            elem.setAttribute('id', compId);
            elem.setAttribute('data-index', (++componentIndex));
            
            let img = document.createElement('img'); 
            img.classList.add('move-icon');
            elem.appendChild(img);
            formPanel.appendChild(elem);
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
                        <img src='https://placehold.it/800X100' alt=''>
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
                console.info('Component does not exist.');
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
     */
    function copyComponent() {
        //TODO: 컴포넌트 복사 후 재정렬
    }
    /**
     * 컴포넌트 삭제
     */
    function removeComponent() {
        //TODO: 컴포넌트 삭제 후 재정렬
    }
    /**
     * 우측 property panel 세부 속성 출력
     *
     * @param id 조회할 컴포넌트 id
     */
    function showPropertyPanel(id) {
        if (selectedComponentId === id) { return; }
        
        selectedComponentId = id;
        let compAttr = formEditor.getData(id);
        if (compAttr === null) { return; }
        let detailAttr = component.attr[compAttr.type];
        //세부 속성 재할당
        Object.keys(compAttr).forEach(function(comp) {
            if (compAttr[comp] !== null && typeof(compAttr[comp]) === 'object')  {
                if (detailAttr.hasOwnProperty(comp)) {
                    Object.keys(compAttr[comp]).forEach(function(attr) {
                        Object.keys(detailAttr[comp]).forEach(function(d) {
                            if (attr === detailAttr[comp][d].id) {
                                detailAttr[comp][d].value = compAttr[comp][attr];
                            }
                        });
                    });
                }
            }
        });
        //세부 속성 출력
        setComponentTitle(compAttr.type);
        Object.keys(detailAttr).forEach(function(group) {
            let groupDiv = document.createElement('div');
            groupDiv.setAttribute('id', group);
            groupDiv.classList.add('property-group');
            groupDiv.textContent = group;
            propertyPanel.appendChild(groupDiv);
            
            if (detailAttr[group] !== null && typeof(detailAttr[group]) === 'object')  { //세부 속성
                Object.keys(detailAttr[group]).forEach(function(field) {
                    let fieldArr = detailAttr[group][field];
                    
                    let fieldGroupDiv = document.createElement('div');
                    fieldGroupDiv.classList.add('property-field');
                    fieldGroupDiv.setAttribute('id', fieldArr.id);
                    
                    let propertyName = document.createElement('span');
                    propertyName.classList.add('property-field-name');
                    propertyName.textContent = fieldArr.name;
                    fieldGroupDiv.appendChild(propertyName);
                    
                    let propertyValue = document.createElement('input');
                    propertyValue.classList.add('property-field-value');
                    propertyValue.setAttribute('type', 'text');
                    propertyValue.setAttribute('value', fieldArr.value);
                    fieldGroupDiv.appendChild(propertyValue);
                    if (fieldArr.unit !== '') {
                        let propertyUnit = document.createElement('span');
                        propertyUnit.classList.add('property-field-unit');
                        propertyUnit.textContent = fieldArr.unit;
                        fieldGroupDiv.appendChild(propertyUnit);
                    }
                    
                    /*switch (field.type) {
                        case "inputbox":
                            break;
                        case "inputbox-underline":
                            break;
                        case "select":
                            break;
                        case "slider":
                            break;
                        case "rgb":
                            break;
                        case "radio":
                            break;
                        case "button":
                            break;
                    }*/
                    groupDiv.appendChild(fieldGroupDiv);
                });
            }
        });
    }
    
    /**
     * 우측 property panel 삭제
     */
    function hidePropertyPanel() {
        propertyPanel.innerHTML = '';
        selectedComponentId = '';
    }
    /**
     * property panel 제목 출력
     * 
     * @param type 컴포넌트 타입
     */
    function setComponentTitle(type) {
        let search = {};
        for (let i = 0, len = _compProperties.length; i < len; i++) {
            let prop = _compProperties[i];
            if (prop.type === type) {
                search = prop;
                break;
            }
        }
        let compTitle = document.createElement('div');
        compTitle.classList.add('property-title');
        compTitle.textContent = search.name;
        //TODO: 제목 icon 추가
        propertyPanel.appendChild(compTitle);
    }
    /**
     * 선택된 컴포넌트 ID 조회
     */
    function getSelectedComponentId() {
        return selectedComponentId;
    }
    /**
     * 선택된 컴포넌트 ID 초기화
     *
     * @param id 변경할 id
     */
    function setSelectedComponentId(id) {
        selectedComponentId = id;
    }
    /**
     * 마지막 추가된 컴포넌트 ID 조회
     */
    function getLastComponentId() {
        return lastComponentId;
    }
    /**
     * 컴포넌트 기본 속성 조회
     */
    function loadAttribute(data) {
        component.attr = JSON.parse(data);
    }
    /**
     * 컴포넌트 초기화
     */
    function init() {
        formPanel = document.getElementById('panel-form');
        propertyPanel = document.getElementById('panel-property');
        
        //load component default data.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/assets/js/form/componentAttribute.json',
            callbackFunc: function(xhr) {
                loadAttribute(xhr.responseText)
            },
            contentType: 'application/json; charset=utf-8'
        });
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
