/**
 * @projectDescription Form Designer Library
 *
 * @author woodajung
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import * as util from '../lib/util.js';
import { CLASS_PREFIX, FORM } from '../lib/constants.js';
import History from './history.js';
import Panel from './panel.js';
import Form from '../form/form.js';
import Group, { UIGroupTooltip } from '../form/group.js';
import Row, { UIRowTooltip } from '../form/row.js';
import Component, { UIComponentTooltip } from '../form/component.js';

export default class FormDesigner {
    constructor(formId) {
        this.domElement = document.getElementById('drawingBoard') || document.body;
        this.history = new History(this);  // 이력 추가
        this.panel = new Panel(); // 세부 속성 추가
        this.selectedObject = null;

        // 커스텀 코드 정보 load - 커스텀 코드 컴포넌트에서 사용되기 때문에 우선 로드해야 함
        util.fetchJson({ method: 'GET', url: '/rest/custom-codes?viewType=editor' })
            .then((customData) => {
                FORM.CUSTOM_CODE = customData;
            });

        // 폼 상태 추가
        this.domElement.classList.add('edit'); // edit, view, complete 등 문서의 상태

        this.initMenuBar();
        this.initShortcut();
        this.initForm(formId);
    }
    // 상단 메뉴바 초기화 및 이벤트 등록
    initMenuBar() {
        //TODO: - 상단 메뉴 버튼 액션 추가
    }
    // TODO: 단축키 등록
    initShortcut() {}
    // 컴포넌트 팔레트 초기화 및 이벤트 추가
    initComponentPalette() {
        // TODO: 커스텀 컴포넌트 load

        // drag & drop 이벤트 추가
        const componentIconBoxs = document.querySelectorAll('.component-icon-box');
        componentIconBoxs.forEach(icon => {
            new Sortable(icon, {
                group: {
                    name: 'palette',
                    pull: 'clone',
                    put: false
                },
                animation: 150,
                sort: false,
                chosenClass: 'drag-on',
                editor: this,
                draggable: '.list-group-item',
                swapThreshold: 0.5,
                onChoose: function () {
                    this.options.editor.deSelectObject();
                },
                onMove: function (evt) {
                    if (evt.from !== evt.to && evt.dragged.classList.contains('component-icon')) {
                        // TODO: form 내부이면, placeholder 로 가상의 group, row, component 표시
                        // TODO: 아니면 타입에 따라 component만 표시
                        evt.dragged.classList.add('component-icon-drag-in');
                    }
                },
                onEnd: function (evt) {
                    if (evt.from === evt.to) { return false; }
                    // evt.to.id 를 가지고 객체 조회
                    const parentObject = this.options.editor.form.getById(evt.to.id);
                    const clickEvent = document.createEvent('Event');
                    clickEvent.initEvent('click', true, true);

                    if (evt.to.classList.contains(CLASS_PREFIX + 'form')) { // 신규 그룹 추가
                        const group = this.options.editor.addObjectByType(FORM.LAYOUT.GROUP,
                            {}, parentObject, evt.newDraggableIndex);
                        const row = this.options.editor.addObjectByType(FORM.LAYOUT.ROW,
                            {}, group, 0);
                        this.options.editor.addObjectByType(FORM.LAYOUT.COMPONENT,
                            {type: evt.item.id}, row, 0);
                        // 그룹 선택 이벤트 추가
                        group.UIElement.UIGroup.domElement.dispatchEvent(clickEvent);
                    } else if (evt.to.classList.contains(CLASS_PREFIX + 'group')) { // 신규 row 추가
                        const row = this.options.editor.addObjectByType(FORM.LAYOUT.ROW,
                            {}, parentObject, evt.newDraggableIndex);
                        this.options.editor.addObjectByType(FORM.LAYOUT.COMPONENT,
                            {type: evt.item.id}, row, 0);
                        // row 선택 이벤트 추가
                        row.UIElement.UIRow.domElement.dispatchEvent(clickEvent);
                    } else if (evt.to.classList.contains(CLASS_PREFIX + 'row')) { // 신규 컴포넌트 추가
                        const component = this.options.editor.addObjectByType(FORM.LAYOUT.COMPONENT,
                            { type : evt.item.id }, parentObject, evt.newDraggableIndex);
                        // 컴포넌트 선택 이벤트 추가
                        component.UIElement.UIComponent.domElement.dispatchEvent(clickEvent);
                    }
                    // 기존 fake element 삭제
                    evt.to.removeChild(evt.item);
                }
            });
        });
    }
    // 폼 초기화 및 이벤트 추가
    initForm(formId) {
        // 폼 데이터 load
        //util.fetchJson({ method: 'GET', url: '/rest/form/' + formId + '/data' })
        util.fetchJson({
            method: 'GET',
            url: '/assets/js/formRefactoring/formDesigner/data_210320.json'
        }).then((formData) => {
            // TODO: 전달된 데이터의 서버 시간에 따른 날짜/시간 처리
            //this.data = aliceForm.reformatCalendarFormat('read', response.json());
            // TODO: displayOrder 로 정렬

            this.data = formData;
            this.makeDomElement();
            this.setFormName();
            this.initComponentPalette();
        });

        document.addEventListener('click', onLeftClickHandler.bind(this), false);
        document.getElementById('formMain').addEventListener('mousewheel',
            onScrollHandler.bind(this), false);
    }
    // 폼 디자이너 상단 이름 출력
    setFormName() {
        document.getElementById('form-name').textContent =
            (this.history.status ? '*' : '') + (this.data.name);
    }
    // DOM 엘리먼트 생성
    makeDomElement() {
        this.form = this.addObjectByType(FORM.LAYOUT.FORM, this.data);
        this.form.parent = this;
        if (Object.prototype.hasOwnProperty.call(this.data, 'groups')) {
            this.data.groups.forEach( (g, gIndex) => {
                const group = this.addObjectByType(FORM.LAYOUT.GROUP, g, this.form, gIndex);
                if (Object.prototype.hasOwnProperty.call(g, 'rows')) {
                    g.rows.forEach( (r, rIndex) => {
                        const row = this.addObjectByType(FORM.LAYOUT.ROW, r, group, rIndex);
                        if (Object.prototype.hasOwnProperty.call(r, 'components')) {
                            r.components.forEach( (c, cIndex) => {
                                this.addObjectByType(FORM.LAYOUT.COMPONENT, c, row, cIndex);
                            });
                        }
                    });
                }
            });
        }
        this.domElement.appendChild(this.form.UIElement.domElement);
    }
    // form, group, row, component 객체 추가
    addObjectByType(type, data, parent, index) {
        let object = null;
        switch(type) {
        case 'form':
            object = new Form(data);
            // drag & drop 이벤트 추가
            object.UIElement.addClass('list-group');
            new Sortable(object.UIElement.domElement, {
                group: {
                    name: 'form',
                    pull: false,
                    put: ['palette', 'group', 'row'],
                },
                animation: 150,
                sort: true,
                chosenClass: 'drag-in',
                editor: this,
                draggable: '.list-group-item',
                swapThreshold: 0.5,
                onChoose: function () {
                    this.options.editor.deSelectObject();
                },
                onEnd: function (evt) {
                    // 그룹 swap 및 재정렬
                    if (evt.oldDraggableIndex !== evt.newDraggableIndex) {
                        const form = this.options.editor.form;
                        util.swapObject(form.children, evt.oldDraggableIndex, evt.newDraggableIndex);
                        form.sort(0);
                    }
                }
            });
            object.UIElement.onClick(this.selectObject.bind(object)); // 선택 이벤트 추가
            break;
        case 'group':
            object = new Group(data);
            // drag & drop 이벤트 추가
            object.UIElement.addClass('list-group-item');
            object.UIElement.UIGroup.addClass('list-group');
            new Sortable(object.UIElement.UIGroup.domElement, {
                group: {
                    name: 'group',
                    pull: 'clone',
                    put: ['palette', 'group', 'row']
                },
                animation: 150,
                sort: true,
                chosenClass: 'drag-in',
                editor: this,
                draggable: '.list-group-item',
                swapThreshold: 0.5,
                onChoose: function () {
                    this.options.editor.deSelectObject();
                },
                onClone: function (evt) { // clone 대상이되는 엘리먼트 디자인 변경
                    evt.clone.classList.add('drag-ghost');
                },
                onEnd: function (evt) {
                    evt.clone.classList.remove('drag-ghost');

                    const formObject = this.options.editor.form.getById(evt.from.id);
                    if (evt.from.id === evt.to.id) {
                        if (evt.oldDraggableIndex !== evt.newDraggableIndex) { // swap
                            util.swapObject(formObject.children, evt.oldDraggableIndex, evt.newDraggableIndex);
                            formObject.sort(0);
                        }
                    } else { // 다른 위치로 이동
                        const toObject = this.options.editor.form.getById(evt.to.id);
                        const rowObject = formObject.children[evt.oldDraggableIndex];
                        const clickEvent = document.createEvent('Event');
                        clickEvent.initEvent('click', true, true);

                        if (evt.to.classList.contains(CLASS_PREFIX + 'form')) { // 폼으로 이동
                            const group = this.options.editor.addObjectByType(FORM.LAYOUT.GROUP,
                                {}, toObject, evt.newDraggableIndex);
                            group.add(rowObject, 0);
                            group.UIElement.UIGroup.domElement.dispatchEvent(clickEvent);
                        } else { // 다른 group 이동
                            toObject.add(rowObject, evt.newDraggableIndex);
                            rowObject.UIElement.UIRow.domElement.dispatchEvent(clickEvent);
                        }
                        this.options.editor.removeObjectByChildren(formObject);
                        // clone 후 row 삭제
                        evt.from.removeChild(evt.clone);
                    }
                }
            });
            object.UIElement.UIGroup.onClick(this.selectObject.bind(object));
            break;
        case 'row':
            object = new Row(data);
            object.UIElement.addClass('list-group-item'); // drag & drop 이벤트 추가
            object.UIElement.UIRow.addClass('list-group');
            new Sortable(object.UIElement.UIRow.domElement, {
                group: {
                    name: 'row',
                    pull: 'clone',
                    put: function (to) {
                        if (to.el.classList.contains(CLASS_PREFIX + 'row') && 
                          to.el.children.length >= FORM.MAX_ROW) { // row의 컴포넌트 갯수 제한
                            return 'false';
                        } else {
                            return ['palette', 'row'];
                        }
                    }
                },
                direction: function(evt, target, dragEl) { // 하나의 row에 여러개 컴포넌트 추가 용도
                    if (target !== null &&
                        target.className.includes(CLASS_PREFIX + 'component-tooltip') &&
                        (dragEl.className.includes(CLASS_PREFIX + 'component-tooltip') ||
                            dragEl.className.includes('component-icon'))) {
                        return 'horizontal';
                    }
                    return 'vertical';
                },
                animation: 150,
                sort: true,
                chosenClass: 'drag-in',
                editor: this,
                draggable: '.list-group-item',
                swapThreshold: 0.5,
                onChoose: function () {
                    this.options.editor.deSelectObject();
                },
                onClone: function (evt) {
                    // clone 대상이되는 엘리먼트 디자인 변경
                    evt.clone.classList.add('drag-ghost');
                },
                onEnd: function (evt) {
                    evt.clone.classList.remove('drag-ghost');
                    const formObject = this.options.editor.form.getById(evt.from.id);
                    if (evt.from.id === evt.to.id) { // swap
                        if (evt.oldDraggableIndex !== evt.newDraggableIndex) { // swap
                            util.swapObject(formObject.children, evt.oldDraggableIndex, evt.newDraggableIndex);
                            formObject.sort(0);
                        }
                    } else { // 다른 위치로 이동
                        const toObject = this.options.editor.form.getById(evt.to.id);
                        const componentObject = formObject.children[evt.oldDraggableIndex];
                        const clickEvent = document.createEvent('Event');
                        clickEvent.initEvent('click', true, true);
                        if (evt.to.classList.contains(CLASS_PREFIX + 'form')) { // 폼으로 이동
                            const group = this.options.editor.addObjectByType(FORM.LAYOUT.GROUP,
                                {}, toObject, evt.newDraggableIndex);
                            const row = this.options.editor.addObjectByType(FORM.LAYOUT.ROW,
                                {}, group, 0);
                            row.add(componentObject, 0);
                            // 그룹 선택 이벤트 추가
                            group.UIElement.UIGroup.domElement.dispatchEvent(clickEvent);
                        } else if (evt.to.classList.contains(CLASS_PREFIX + 'group')) { // group으로 이동
                            const row = this.options.editor.addObjectByType(FORM.LAYOUT.ROW,
                                {}, toObject, evt.newDraggableIndex);
                            row.add(componentObject, 0);
                            // 그룹 선택 이벤트 추가
                            row.UIElement.UIRow.domElement.dispatchEvent(clickEvent);
                        } else { // 다른 row로 이동
                            toObject.add(componentObject, evt.newDraggableIndex);
                            // 컴포넌트 선택 이벤트 추가
                            componentObject.UIElement.UIComponent.domElement.dispatchEvent(clickEvent);
                        }
                        this.options.editor.removeObjectByChildren(formObject);
                        // clone 후 row 삭제
                        evt.from.removeChild(evt.clone);
                    }
                }
            });
            object.UIElement.UIRow.onClick(this.selectObject.bind(object));
            break;
        case 'component':
            object = new Component(data);
            object.UIElement.addClass('list-group-item'); // drag & drop 이벤트 추가
            object.UIElement.UIComponent.onClick(this.selectObject.bind(object));
            break;
        default:
            break;
        }
        if (parent !== undefined) {
            parent.add(object, index);
        }
        return object;
    }
    // group, row, component 객체 삭제
    removeObject(object) {
        if (object.parent === null) { return false; } // 폼은 삭제 불가
        return object.parent.remove(object);
    }
    // 자식이 존재하지 않은 부모 삭제
    removeObjectByChildren(object) {
        const parentObject = object.parent;
        if (object.children.length === 0 && parentObject !== null) {
            this.removeObject(object);
            this.removeObjectByChildren(parentObject);
        }
    }
    // 객체 선택
    selectObject(e) {
        e.stopPropagation();
        // 내부에 체크박스, 라디오 버튼이 존재할 경우 2번 호출되므로 삭제
        if (e.target.type === 'checkbox' || e.target.type === 'radio') { return false; }

        let editor = this.parent;
        if (this.UIElement instanceof UIGroupTooltip) {
            editor = this.parent.parent;
        } else if (this.UIElement instanceof UIRowTooltip) {
            editor = this.parent.parent.parent;
        } else if (this.UIElement instanceof UIComponentTooltip) {
            editor = this.parent.parent.parent.parent;
        }
        // 이전 선택된 객체 디자인 삭제
        if (editor.selectedObject !== null) {
            editor.selectedObject.UIElement.removeClass('selected');
        }
        // 현재 선택된 객체 디자인 추가
        this.UIElement.addClass('selected');
        editor.selectedObject = this;
        // TODO: 세부 속성 출력
        // editor.panel.on(객체);
    }
    // 객체 선택 해제
    deSelectObject() {
        // 이전 선택된 객체 디자인 삭제
        if (this.selectedObject !== null) {
            this.selectedObject.UIElement.removeClass('selected');
        }
    }
    // form 저장
    saveForm() {}
    // 다른 이름으로 form 저장
    saveAsForm() {}
}
/**
 * 마우스 좌클릭 이벤트 핸들러
 * @param e 이벤트객체
 */
function onLeftClickHandler(e) {
    // 폼 내부를 선택한게 아니라면, 기존 선택된 항목을 초기화한다.
    if (!aliceJs.clickInsideElement(e, CLASS_PREFIX + 'form')) {
        this.deSelectObject();
    }
}
/**
 * 마우스 스크롤 이벤트 핸들러
 * @param e 이벤트객체
 */
function onScrollHandler(e) {
    // TODO: 스크롤 실행시, 컨텍스트 메뉴가 열려 있으면 메뉴를 닫는다.
    // contextMenuOff();
}
