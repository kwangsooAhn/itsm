/**
 * @projectDescription Form Designer Library.
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
        // edit, view, complete 등 문서의 상태에 따라 아코디언, 컴포넌트 등 동작을 막음
        this.domElement.classList.add('edit');
        this.history = new History(this);  // 이력 추가
        this.panel = new Panel(this); // 세부 속성 추가
        this.selectedObject = null;

        // 커스텀 코드 정보 load - 커스텀 코드 컴포넌트에서 사용되기 때문에 우선 로드해야 함
        util.fetchJson({ method: 'GET', url: '/rest/custom-codes?viewType=editor' })
            .then((customData) => {
                FORM.CUSTOM_CODE = customData;
            });

        this.initMenuBar();
        this.initShortcut();
        this.initComponentPalette();
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
                fallbackOnBody: true,
                swapThreshold: 0.65,
                onChoose: function () {
                    // drag 시작시, 기존 선택된 객체 선택 해제
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
                    // drop 후 추가된 객체를 선택하는 이벤트
                    const clickEvent = document.createEvent('Event');
                    clickEvent.initEvent('click', true, true);

                    if (evt.to.classList.contains(CLASS_PREFIX + FORM.LAYOUT.FORM)) {
                        // 신규 group / row / component 추가
                        const group = this.options.editor.addObjectByType(FORM.LAYOUT.GROUP,
                            {}, parentObject, evt.newDraggableIndex);
                        const row = this.options.editor.addObjectByType(FORM.LAYOUT.ROW,
                            {}, group, 0);
                        this.options.editor.addObjectByType(FORM.LAYOUT.COMPONENT,
                            {type: evt.item.id}, row, 0);
                        // 그룹 선택
                        group.UIElement.domElement.dispatchEvent(clickEvent);
                    } else if (evt.to.classList.contains(CLASS_PREFIX + FORM.LAYOUT.GROUP)) {
                        // 신규 row / component 추가
                        const row = this.options.editor.addObjectByType(FORM.LAYOUT.ROW,
                            {}, parentObject, evt.newDraggableIndex);
                        this.options.editor.addObjectByType(FORM.LAYOUT.COMPONENT,
                            {type: evt.item.id}, row, 0);
                        // row 선택
                        row.UIElement.domElement.dispatchEvent(clickEvent);
                    } else if (evt.to.classList.contains(CLASS_PREFIX + FORM.LAYOUT.ROW)) {
                        // 신규 component 추가
                        const component = this.options.editor.addObjectByType(FORM.LAYOUT.COMPONENT,
                            { type : evt.item.id }, parentObject, evt.newDraggableIndex);
                        // component 선택
                        component.UIElement.domElement.dispatchEvent(clickEvent);
                    }
                    // 기존 fake element 삭제
                    evt.to.removeChild(evt.item);
                }
            });
        });
    }
    // 폼 초기화 및 이벤트 추가.
    initForm(formId) {
        // TODO: 폼 데이터 load. > 가데이터 삭제 필요
        //util.fetchJson({ method: 'GET', url: '/rest/form/' + formId + '/data' })
        util.fetchJson({
            method: 'GET',
            url: '/assets/js/formRefactoring/formDesigner/data_210320.json'
        }).then((formData) => {
            // TODO: 전달된 데이터의 서버 시간에 따른 날짜/시간 처리
            //this.data = aliceForm.reformatCalendarFormat('read', response.json());
            // TODO: displayOrder 로 정렬
            this.data = formData;

            this.makeDomElement(); // DOM 엘리먼트 생성
            this.setFormName(); // 폼 디자이너 상단 이름 출력
        });

        document.addEventListener('click', onLeftClickHandler.bind(this), false);
        document.getElementById('formMain').addEventListener('mousewheel',
            onScrollHandler.bind(this), false);
    }
    // 폼 디자이너 상단 이름 출력
    setFormName() {
        document.getElementById('formName').textContent =
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
        case FORM.LAYOUT.FORM:
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
                fallbackOnBody: true,
                swapThreshold: 0.65,
                onChoose: function () {
                    this.options.editor.deSelectObject();
                },
                onEnd: function (evt) {
                    // form children = group swap and sort
                    if (evt.oldDraggableIndex !== evt.newDraggableIndex) {
                        const form = this.options.editor.form;
                        util.swapObject(form.children, evt.oldDraggableIndex, evt.newDraggableIndex);
                        form.sort(0);
                        // 그룹 선택
                        const group = form.children[evt.newDraggableIndex];
                        group.UIElement.domElement.dispatchEvent(new Event('click'));
                    }
                }
            });
            break;
        case FORM.LAYOUT.GROUP:
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
                fallbackOnBody: true,
                swapThreshold: 0.65,
                onChoose: function () {
                    this.options.editor.deSelectObject();
                },
                onClone: function (evt) {
                    // clone 대상이되는 엘리먼트 디자인 변경
                    evt.clone.classList.add('drag-ghost');
                },
                onEnd: function (evt) {
                    evt.clone.classList.remove('drag-ghost');

                    const fromObject = this.options.editor.form.getById(evt.from.id);
                    if (evt.from.id === evt.to.id) {
                        if (evt.oldDraggableIndex !== evt.newDraggableIndex) { // swap
                            util.swapObject(fromObject.children, evt.oldDraggableIndex, evt.newDraggableIndex);
                            fromObject.sort(0);
                            // row 선택
                            const row = fromObject.children[evt.newDraggableIndex];
                            row.UIElement.domElement.dispatchEvent(new Event('click'));
                        }
                    } else { // 다른 위치로 이동
                        const toObject = this.options.editor.form.getById(evt.to.id);
                        const rowObject = fromObject.children[evt.oldDraggableIndex];

                        if (evt.to.classList.contains(CLASS_PREFIX + FORM.LAYOUT.FORM)) {
                            // form 내에 신규 group 추가
                            const group = this.options.editor.addObjectByType(FORM.LAYOUT.GROUP,
                                {}, toObject, evt.newDraggableIndex);
                            group.add(rowObject, 0);
                            group.UIElement.domElement.dispatchEvent(new Event('click'));
                        } else {
                            // 다른 group 내로 이동
                            toObject.add(rowObject, evt.newDraggableIndex);
                            rowObject.UIElement.domElement.dispatchEvent(new Event('click'));
                        }
                        this.options.editor.removeObjectByChildrenEmpty(fromObject);
                        // clone 후 기존 row 삭제
                        evt.from.removeChild(evt.clone);
                    }
                }
            });
            break;
        case FORM.LAYOUT.ROW:
            object = new Row(data);
            // drag & drop 이벤트 추가
            object.UIElement.addClass('list-group-item');
            object.UIElement.UIRow.addClass('list-group');
            new Sortable(object.UIElement.UIRow.domElement, {
                group: {
                    name: 'row',
                    pull: 'clone',
                    put: function (to) {
                        if (to.el.classList.contains(CLASS_PREFIX + 'row') && 
                          to.el.children.length >= FORM.MAX_COMPONENT_IN_ROW) {
                            // row의 컴포넌트 갯수 제한
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
                fallbackOnBody: true,
                swapThreshold: 0.65,
                onChoose: function () {
                    this.options.editor.deSelectObject();
                },
                onClone: function (evt) {
                    // clone 대상이되는 엘리먼트 디자인 변경
                    evt.clone.classList.add('drag-ghost');
                },
                onEnd: function (evt) {
                    evt.clone.classList.remove('drag-ghost');

                    const fromObject = this.options.editor.form.getById(evt.from.id);
                    if (evt.from.id === evt.to.id) {
                        if (evt.oldDraggableIndex !== evt.newDraggableIndex) { // swap
                            util.swapObject(fromObject.children, evt.oldDraggableIndex, evt.newDraggableIndex);
                            fromObject.sort(0);
                            // component 선택
                            const component = fromObject.children[evt.newDraggableIndex];
                            component.UIElement.domElement.dispatchEvent(new Event('click'));
                        }
                    } else { // 다른 위치로 이동
                        const toObject = this.options.editor.form.getById(evt.to.id);
                        const componentObject = fromObject.children[evt.oldDraggableIndex];
                        
                        if (evt.to.classList.contains(CLASS_PREFIX + FORM.LAYOUT.FORM)) {
                            // 신규 group, row 추가 후 component 이동
                            const group = this.options.editor.addObjectByType(FORM.LAYOUT.GROUP,
                                {}, toObject, evt.newDraggableIndex);
                            const row = this.options.editor.addObjectByType(FORM.LAYOUT.ROW,
                                {}, group, 0);
                            row.add(componentObject, 0);
                            // group 선택
                            group.UIElement.domElement.dispatchEvent(new Event('click'));
                        } else if (evt.to.classList.contains(CLASS_PREFIX + FORM.LAYOUT.GROUP)) {
                            // 신규 row 추가 후 component 이동
                            const row = this.options.editor.addObjectByType(FORM.LAYOUT.ROW,
                                {}, toObject, evt.newDraggableIndex);
                            row.add(componentObject, 0);
                            // group 선택
                            row.UIElement.domElement.dispatchEvent(new Event('click'));
                        } else { // component 이동
                            toObject.add(componentObject, evt.newDraggableIndex);
                            // component 선택
                            componentObject.UIElement.domElement.dispatchEvent(new Event('click'));
                        }
                        this.options.editor.removeObjectByChildrenEmpty(fromObject);
                        // clone 후 기존 row 삭제
                        evt.from.removeChild(evt.clone);
                    }
                }
            });
            break;
        case FORM.LAYOUT.COMPONENT:
            object = new Component(data);
            object.UIElement.addClass('list-group-item'); // drag & drop 이벤트 추가
            break;
        default:
            break;
        }
        // 선택 이벤트 추가
        object.UIElement.onClick(this.selectObject.bind(object));

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
    removeObjectByChildrenEmpty(object) {
        const parentObject = object.parent;
        if (object.children.length === 0 && parentObject !== null) {
            this.removeObject(object);
            this.removeObjectByChildrenEmpty(parentObject);
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
        // 이전 선택된 객체를 다시 선택할 경우
        if (editor.selectedObject === this) { return false; }

        // 이전 선택된 객체 디자인 삭제
        if (editor.selectedObject !== null) {
            editor.selectedObject.UIElement.removeClass('selected');
            editor.panel.off();
        }
        // 현재 선택된 객체 디자인 추가
        this.UIElement.addClass('selected');
        editor.selectedObject = this;
        editor.panel.on(); // 세부 속성 출력
    }
    // 객체 선택 해제
    deSelectObject() {
        // 이전 선택된 객체 디자인 삭제
        if (this.selectedObject !== null) {
            this.selectedObject.UIElement.removeClass('selected');
            this.selectedObject = null;
            this.panel.off();
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
    if (!aliceJs.clickInsideElement(e, CLASS_PREFIX + FORM.LAYOUT.FORM)) {
        //this.deSelectObject();
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
