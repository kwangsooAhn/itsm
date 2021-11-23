/**
 * @projectDescription Form Designer Library.
 *
 * @author woodajung
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import { zDocument } from '../document/zDocument.js';
import { zFormButton } from '../document/zFormButton.js';
import ZComponent, { UIComponentTooltip } from '../form/zComponent.js';
import ZForm from '../form/zForm.js';
import ZGroup, { UIGroupTooltip } from '../form/zGroup.js';
import ZRow, { UIRowTooltip } from '../form/zRow.js';
import { FORM, RESPONSE_CODE } from '../lib/zConstants.js';
import { zValidation } from '../lib/zValidation.js';
import ZHistory from './zHistory.js';
import ZPanel from './zPanel.js';

class ZFormDesigner {
    constructor() {
        this.domElement = document.getElementById('formDrawingBoard') || document.body;
        // edit, view, complete 등 문서의 상태에 따라 아코디언, 컴포넌트 등 동작을 막음
        this.domElement.classList.add('edit');
        this.isView = false; // 편집 가능 여부

        this.history = new ZHistory(this);  // 이력 관리
        this.panel = new ZPanel(this); // 세부 속성 관리
        this.selectedObject = null;

        // 커스텀 코드 정보 load - 커스텀 코드 컴포넌트에서 사용되기 때문에 우선 로드해야 함
        aliceJs.fetchJson('/rest/custom-codes?viewType=editor', {
            method: 'GET'
        }).then((customData) => {
            FORM.CUSTOM_CODE = zValidation.isDefined(customData.data) ? customData.data : [];
        }).catch(err => {
            zAlert.warning(err);
        });

        // 초기화
        this.initMenuBar();
        this.initShortcut();
        this.initComponentPalette();
        // 미리보기 초기화
        zDocument.initDocumentModal();
    }
    /**
     * 상단 메뉴바 초기화 및 이벤트 등록
     */
    initMenuBar() {
        // 사용자가 페이지를 떠날 때 정말로 떠날 것인지 묻는 확인창 표시
        window.addEventListener('beforeunload', (e) => {
            if (this.history.status && !this.isView) {
                e.preventDefault(); // 표준에 따라 기본 동작 방지
                e.returnValue = ''; // Chrome에서는 returnValue 설정이 필요함
            }
        });
    }
    /**
     * 단축키 등록
     */
    initShortcut() {
        const shortcuts = [
            { 'keys': 'ctrl+s', 'command': 'zFormDesigner.saveForm(false);', 'force': true },                        //폼 양식 저장
            { 'keys': 'ctrl+shift+s', 'command': 'zFormDesigner.openSaveAsModal();', 'force': true },                //폼 양식 다른이름으로 저장
            { 'keys': 'ctrl+z', 'command': 'zFormDesigner.history.undo();', 'force': false },                        //폼 편집 화면 작업 취소
            { 'keys': 'ctrl+shift+z', 'command': 'zFormDesigner.history.redo();', 'force': false },                  //폼 편집 화면 작업 재실행
            { 'keys': 'ctrl+e', 'command': 'zFormDesigner.preview();', 'force': false },                             //폼 양식 미리보기
            { 'keys': 'insert', 'command': 'zFormDesigner.copyObject();', 'force': false },           //복사하여 바로 아래 추가
            { 'keys': 'ctrl+x,delete', 'command': 'zFormDesigner.removeObject();', 'force': false },  //객체 삭제
            { 'keys': 'ctrl+home', 'command': 'zFormDesigner.selectFirstGroup();', 'force': false },                 //첫번째 그룹 선택
            { 'keys': 'ctrl+end', 'command': 'zFormDesigner.selectLastGroup();', 'force': false },                   //마지막 그룹 선택
            { 'keys': 'up', 'command': 'zFormDesigner.selectUpObject();', 'force': false },                          //바로 위 동일 타입 객체 선택
            { 'keys': 'down', 'command': 'zFormDesigner.selectDownObject();', 'force': false },                      //바로 아래 동일 타입 객체 선택
            { 'keys': 'alt+e', 'command': 'zFormDesigner.panel.selectFirstProperty();', 'force': false }             //세부 속성 편집: 제일 처음으로 이동
        ];

        zShortcut.init();

        for (let i = 0; i < shortcuts.length; i++) {
            zShortcut.add(shortcuts[i].keys, shortcuts[i].command, shortcuts[i].force);
        }
    }
    /**
     * 컴포넌트 팔레트 초기화 및 이벤트 추가
     */
    initComponentPalette() {
        // drag & drop 이벤트 추가
        const componentIconBoxes = document.querySelectorAll('.z-component-icon-box');
        componentIconBoxes.forEach(icon => {
            new Sortable(icon, {
                group: {
                    name: 'palette',
                    pull: 'clone',
                    put: false
                },
                animation: 150,
                sort: false,
                ghostClass: 'placeholder', // Class name for the drop placeholder
                chosenClass: 'drag', // Class name for the chosen item
                editor: this,
                draggable: '.list-group-item',
                fallbackOnBody: true,
                swapThreshold: 0.65,
                onChoose: function () {
                    // drag 시작시, 기존 선택된 객체 선택 해제
                    this.options.editor.deSelectObject();
                },
                onClone: function (evt) {
                    // drag & drop시 디자인 추가
                    evt.clone.classList.add('placeholder');
                },
                onMove: function (evt) {
                    if (evt.from !== evt.to && evt.dragged.classList.contains('z-component-icon')) {
                        // drag시 컴포넌트 표시
                        evt.dragged.classList.add('z-component-icon-drag-in');
                        if (evt.dragged.children.length < 3) {
                            const component = new ZComponent({ type: evt.dragged.id });
                            evt.dragged.appendChild(component.UIElement.domElement);
                            component.afterEvent();
                        }
                    }
                },
                onEnd: function (evt) {
                    if (evt.from === evt.to) {
                        if (evt.item.children.length === 3) {
                            evt.item.removeChild(evt.item.children[2]);
                        }
                        evt.item.classList.remove('z-component-icon-drag-in');
                        return false;
                    }
                    const histories = [];  // 이력 저장용
                    const editor = this.options.editor;
                    const parentObject = editor.form.getById(evt.to.id); // 부모 객체

                    if (evt.to.classList.contains('z-' + FORM.LAYOUT.FORM)) {
                        // 신규 group / row / component 추가
                        const group = editor.addObjectByType(FORM.LAYOUT.GROUP, {}, parentObject, evt.newDraggableIndex);
                        const row = editor.addObjectByType(FORM.LAYOUT.ROW, {}, group, 0);
                        editor.addObjectByType(FORM.LAYOUT.COMPONENT, { type: evt.item.id }, row, 0);
                        // 이력 추가
                        histories.push({
                            type: 'add',
                            from: { id: '', clone: null },
                            to: { id: parentObject.id, clone: group.clone(true).toJson() }
                        });
                        // group 선택
                        group.UIElement.domElement.dispatchEvent(new Event('click'));
                    } else if (evt.to.classList.contains('z-' + FORM.LAYOUT.GROUP)) {
                        // 신규 row / component 추가
                        const row = editor.addObjectByType(FORM.LAYOUT.ROW, {}, parentObject, evt.newDraggableIndex);
                        editor.addObjectByType(FORM.LAYOUT.COMPONENT, {type: evt.item.id}, row, 0);
                        // 이력 추가
                        histories.push({
                            type: 'add',
                            from: { id: '', clone: null },
                            to: { id: parentObject.id, clone: row.clone(true).toJson() }
                        });
                        // row 선택
                        row.UIElement.domElement.dispatchEvent(new Event('click'));
                    } else if (evt.to.classList.contains('z-' + FORM.LAYOUT.ROW)) {
                        // 신규 component 추가
                        const component = editor.addObjectByType(FORM.LAYOUT.COMPONENT, { type : evt.item.id }, parentObject, evt.newDraggableIndex);
                        // 이력 추가
                        histories.push({
                            type: 'add',
                            from: { id: '', clone: null },
                            to: { id: parentObject.id, clone: component.clone(true, { type: component.type }).toJson() }
                        });
                        // component 선택
                        component.UIElement.domElement.dispatchEvent(new Event('click'));
                    }

                    // drag & drop시 추가된 디자인 제거
                    if (zValidation.isDefined(evt.clone) && evt.clone.classList.contains('placeholder')) {
                        evt.clone.classList.remove('placeholder');
                    }
                    // 기존 fake element 삭제
                    evt.to.removeChild(evt.item);

                    // 이력 저장
                    this.options.editor.history.save(histories);
                }
            });
        });
    }
    /**
     * 폼 초기화 및 이벤트 추가
     * @param formId 폼 아이디
     * @param isView 편집화면인지 보기화면인지 판단
     */
    initForm(formId, isView) {
        this.isView = (isView === 'true');
        this.formId = formId;
        aliceJs.fetchJson('/rest/forms/' + this.formId + '/data', {
            method: 'GET',
            showProgressbar: true
        }).then((formData) => {
            this.reflowForm(formData);
        }).catch(err => {
            zAlert.warning(err);
        });

        document.addEventListener('click', this.onLeftClickHandler.bind(this), false);
    }
    /**
     * JSON 데이터 정렬 (Recursive)
     * @param data JSON 데이터
     */
    sortJson(data) {
        if (Object.prototype.hasOwnProperty.call(data, 'group')) { // form
            data.group.sort((a, b) =>
                a.displayDisplayOrder < b.displayDisplayOrder ? -1 : a.displayDisplayOrder > b.displayDisplayOrder ? 1 : 0
            );
            data.group.forEach( (g) => {
                this.sortJson(g);
            });
        } else if (Object.prototype.hasOwnProperty.call(data, 'row')) { // group
            data.row.sort((a, b) =>
                a.displayDisplayOrder < b.displayDisplayOrder ? -1 : a.displayDisplayOrder > b.displayDisplayOrder ? 1 : 0
            );
            data.row.forEach( (r) => {
                this.sortJson(r);
            });
        } else { // row
            data.component.sort((a, b) =>
                a.displayDisplayOrder < b.displayDisplayOrder ? -1 : a.displayDisplayOrder > b.displayDisplayOrder ? 1 : 0
            );
        }
    }
    /**
     * 폼 디자이너 상단 이름 출력
     */
    setFormName(name) {
        const changeName = name || this.form.name;
        document.getElementById('formName').textContent =
            (this.history.status ? '*' : '') + changeName;
    }
    /**
     * FORM 생성 (Recursive)
     * @param data JSON 데이터
     * @param parent 부모 객체
     * @param index 추가될 객체의 index
     */
    makeForm(data, parent, index) {
        if (Object.prototype.hasOwnProperty.call(data, 'group')) { // form
            this.form = this.addObjectByType(FORM.LAYOUT.FORM, data);
            this.form.parent = parent;
            this.domElement.appendChild(this.form.UIElement.domElement);
            this.form.afterEvent();

            data.group.forEach( (g, gIndex) => {
                this.makeForm(g, this.form, gIndex);
            });
        } else if (Object.prototype.hasOwnProperty.call(data, 'row')) { // group
            const group = this.addObjectByType(FORM.LAYOUT.GROUP, data, parent, index);
            data.row.forEach( (r, rIndex) => {
                this.makeForm(r, group, rIndex);
            });
        } else if (Object.prototype.hasOwnProperty.call(data, 'component')) { // row
            const row = this.addObjectByType(FORM.LAYOUT.ROW, data, parent, index);
            data.component.forEach( (c, cIndex) => {
                this.makeForm(c, row, cIndex);
            });
        } else { // component
            this.addObjectByType(FORM.LAYOUT.COMPONENT, data, parent, index);
        }
    }
    /**
     * form, group, row, component 타입에 따른 객체 추가
     * @param type form, group, row, component 타입
     * @param data JSON 데이터
     * @param parent 부모 객체
     * @param index 추가될 객체의 index
     */
    addObjectByType(type, data, parent, index) {
        let addObject = null; // 추가된 객체

        switch(type) {
            case FORM.LAYOUT.FORM:
                addObject = new ZForm(data);

                // drag & drop 이벤트 추가
                addObject.UIElement.addUIClass('list-group');
                new Sortable(addObject.UIElement.domElement, {
                    group: {
                        name: 'form',
                        pull: false,
                        put: ['palette', 'group', 'row']
                    },
                    animation: 150,
                    sort: true,
                    chosenClass: 'z-component-drag-in',
                    editor: this,
                    draggable: '.list-group-item',
                    fallbackOnBody: true,
                    swapThreshold: 0.65,
                    filter: '.' + 'z-tooltip-menu',
                    preventOnFilter: true,
                    onChoose: function () {
                        this.options.editor.deSelectObject();
                    },
                    onEnd: function (evt) {
                        const editor = this.options.editor;
                        const swapObject = editor.swapObject(editor.form, evt.oldDraggableIndex, evt.newDraggableIndex);
                        if (swapObject) {
                            swapObject.UIElement.domElement.dispatchEvent(new Event('click'));
                        }
                    }
                });
                break;
            case FORM.LAYOUT.GROUP:
                addObject = new ZGroup(data);

                // drag & drop 이벤트 추가
                addObject.UIElement.addUIClass('list-group-item');
                addObject.UIElement.UIGroup.addUIClass('list-group');
                new Sortable(addObject.UIElement.UIGroup.domElement, {
                    group: {
                        name: 'group',
                        pull: 'clone',
                        put: ['palette', 'group', 'row']
                    },
                    animation: 150,
                    sort: true,
                    chosenClass: 'z-component-drag-in',
                    editor: this,
                    draggable: '.list-group-item',
                    fallbackOnBody: true,
                    swapThreshold: 0.65,
                    filter: '.' + 'z-tooltip-menu',
                    preventOnFilter: true,
                    onChoose: function () {
                        this.options.editor.deSelectObject();
                    },
                    onClone: function (evt) {
                    // clone 대상이되는 엘리먼트 디자인 변경
                        evt.clone.classList.add('z-component-drag-ghost');
                    },
                    onEnd: function (evt) {
                        evt.clone.classList.remove('z-component-drag-ghost');

                        const editor = this.options.editor;
                        const fromObject = editor.form.getById(evt.from.id);

                        if (evt.from.id === evt.to.id) {
                            const swapObject = editor.swapObject(fromObject, evt.oldDraggableIndex, evt.newDraggableIndex);
                            if (swapObject) {
                                swapObject.UIElement.domElement.dispatchEvent(new Event('click'));
                            }
                        } else { // 다른 위치로 이동
                            const histories = [];  // 이력 저장용
                            const toObject = editor.form.getById(evt.to.id);
                            const sortObject = fromObject.children[evt.oldDraggableIndex];
                            // 이력 추가
                            histories.push({
                                type: 'remove',
                                from: { id: sortObject.parent.id, clone: sortObject.clone(true).toJson() },
                                to: { id: '', clone: null }
                            });

                            if (evt.to.classList.contains('z-' + FORM.LAYOUT.FORM)) {
                            // form 내에 신규 group 추가
                                const group = editor.addObjectByType(FORM.LAYOUT.GROUP, {}, toObject, evt.newDraggableIndex);
                                group.add(sortObject, 0);
                                // 이력 추가
                                histories.push({
                                    type: 'add',
                                    from: { id: '', clone: null },
                                    to: { id: toObject.id, clone: group.clone(true).toJson() }
                                });
                                // group 선택
                                group.UIElement.domElement.dispatchEvent(new Event('click'));
                            } else {
                            // 다른 group 내로 이동
                                toObject.add(sortObject, evt.newDraggableIndex);
                                // 이력 추가
                                histories.push({
                                    type: 'add',
                                    from: { id: '', clone: null },
                                    to: { id: toObject.id, clone: sortObject.clone(true).toJson() }
                                });
                                // row 선택
                                sortObject.UIElement.domElement.dispatchEvent(new Event('click'));
                            }
                            // 그룹에 자식이 없을 경우 그룹 삭제
                            if (fromObject.children !== undefined && fromObject.children.length === 0) {
                                histories.push({
                                    type: 'remove',
                                    from: { id: fromObject.parent.id, clone: fromObject.clone(true).toJson() },
                                    to: { id: '', clone: null }
                                });
                                fromObject.parent.remove(fromObject);
                            }
                            // clone 후 기존 row 삭제
                            evt.from.removeChild(evt.clone);
                            // 이력 저장
                            editor.history.save(histories.reverse());
                        }
                    }
                });
                break;
            case FORM.LAYOUT.ROW:
                addObject = new ZRow(data);

                // drag & drop 이벤트 추가
                addObject.UIElement.addUIClass('list-group-item');
                addObject.UIElement.UIRow.addUIClass('list-group');
                new Sortable(addObject.UIElement.UIRow.domElement, {
                    group: {
                        name: 'row',
                        pull: 'clone',
                        put: function (to) { // row 컴포넌트 갯수 제한
                            if (to.el.classList.contains('z-row') &&
                          to.el.children.length >= FORM.MAX_COMPONENT_IN_ROW) {
                                return 'false';
                            } else {
                                return ['palette', 'row'];
                            }
                        }
                    },
                    direction: function(evt, target, dragEl) { // 하나의 row에 여러개 컴포넌트 추가 용도
                        if (target !== null &&
                        target.className.includes('z-component-tooltip') &&
                        (dragEl.className.includes('z-component-tooltip') ||
                            dragEl.className.includes('z-component-icon'))) {
                            return 'horizontal';
                        }
                        return 'vertical';
                    },
                    animation: 150,
                    sort: true,
                    chosenClass: 'z-component-drag-in',
                    editor: this,
                    draggable: '.list-group-item',
                    fallbackOnBody: true,
                    swapThreshold: 0.65,
                    filter: '.' + 'z-tooltip-menu',
                    preventOnFilter: true,
                    onChoose: function () {
                        this.options.editor.deSelectObject();
                    },
                    onClone: function (evt) {
                    // clone 대상이되는 엘리먼트 디자인 변경
                        evt.clone.classList.add('z-component-drag-ghost');
                    },
                    onEnd: function (evt) {
                        evt.clone.classList.remove('z-component-drag-ghost');

                        const editor = this.options.editor;

                        const fromObject = editor.form.getById(evt.from.id);
                        if (evt.from.id === evt.to.id) {
                            const swapObject = editor.swapObject(fromObject, evt.oldDraggableIndex, evt.newDraggableIndex);
                            if (swapObject) {
                                swapObject.UIElement.domElement.dispatchEvent(new Event('click'));
                            }
                        } else { // 다른 위치로 이동
                            const histories = [];  // 이력 저장용
                            const toObject = editor.form.getById(evt.to.id);
                            const sortObject = fromObject.children[evt.oldDraggableIndex];
                            // 이력 추가
                            histories.push({
                                type: 'remove',
                                from: { id: sortObject.parent.id, clone: sortObject.clone(true, { type: sortObject.type }).toJson() },
                                to: { id: '', clone: null }
                            });
                            if (evt.to.classList.contains('z-' + FORM.LAYOUT.FORM)) {
                            // 신규 group, row 추가 후 component 이동
                                const group = editor.addObjectByType(FORM.LAYOUT.GROUP, {}, toObject, evt.newDraggableIndex);
                                const row = editor.addObjectByType(FORM.LAYOUT.ROW, {}, group, 0);
                                row.add(sortObject, 0);
                                // 이력 추가
                                histories.push({
                                    type: 'add',
                                    from: { id: '', clone: null },
                                    to: { id: toObject.id, clone: group.clone(true).toJson() }
                                });
                                // group 선택
                                group.UIElement.domElement.dispatchEvent(new Event('click'));
                            } else if (evt.to.classList.contains('z-' + FORM.LAYOUT.GROUP)) {
                            // 신규 row 추가 후 component 이동
                                const row = editor.addObjectByType(FORM.LAYOUT.ROW, {}, toObject, evt.newDraggableIndex);
                                row.add(sortObject, 0);
                                // 이력 추가
                                histories.push({
                                    type: 'add',
                                    from: { id: '', clone: null },
                                    to: { id: toObject.id, clone: row.clone(true).toJson() }
                                });
                                // group 선택
                                row.UIElement.domElement.dispatchEvent(new Event('click'));
                            } else { // component 이동
                                toObject.add(sortObject, evt.newDraggableIndex);
                                // 이력 추가
                                histories.push({
                                    type: 'add',
                                    from: { id: '', clone: null },
                                    to: { id: toObject.id, clone: sortObject.clone(true, { type: sortObject.type }).toJson() }
                                });
                                // component 선택
                                sortObject.UIElement.domElement.dispatchEvent(new Event('click'));
                            }
                            // row에 자식이 없을 경우 Row 삭제
                            editor.deleteRowChildrenEmpty(fromObject, histories);
                            // clone 후 기존 row 삭제
                            evt.from.removeChild(evt.clone);
                            // 이력 저장
                            editor.history.save(histories.reverse());
                        }
                    }
                });
                break;
            case FORM.LAYOUT.COMPONENT:
                addObject = new ZComponent(data);
                addObject.UIElement.addUIClass('list-group-item'); // drag & drop 이벤트 추가
                break;
            default:
                break;
        }
        // 선택 이벤트 추가
        addObject.UIElement.onUIClick(this.selectObject.bind(addObject));

        if (parent !== undefined) {
            parent.add(addObject, index);
            addObject.afterEvent();
        }

        return addObject;
    }
    /**
     * group, row, component 객체 위치 이동
     * @param object group, row, component 객체
     * @param oldIndex 이전 index
     * @param newIndex 이후 index
     */
    swapObject(object, oldIndex, newIndex) {
        if (oldIndex === newIndex) { return false; }

        this.history.save([{
            type: 'sort',
            from: { id: object.id, clone: object.children[oldIndex].clone(true, { type: object.children[oldIndex].type }).toJson() },
            to: { id: object.id, clone: object.children[newIndex].clone(true, { type: object.children[newIndex].type }).toJson() }
        }]);

        aliceJs.moveObject(object.children, oldIndex, newIndex);
        object.sort(0); // 재정렬

        return object.children[newIndex]; // 변경된 객체
    }

    /**
     * FORM 데이터 정렬 및 패널 세부 속성 출력
     * @param data 서버로부터 전달된 데이터
     */
    reflowForm(data) {
        this.domElement.innerHTML = '';
        // displayOrder 로 데이터 재정렬
        this.sortJson(data);
        this.data = data;
        // 폼 디자이너 상단 이름 출력
        this.setFormName(this.data.name);
        // FORM 생성
        this.makeForm(this.data, this);
        this.form.UIElement.domElement.dispatchEvent(new Event('click')); // 폼 속성 패널 출력
    }
    /**
     * form, group, row, component 객체 선택
     */
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

        // 세부 속성 유효성 검증 실패시 동작을 중지함
        if (!editor.panel.validationStatus) { return false; }

        // 이전 선택된 객체 디자인 삭제
        if (editor.selectedObject !== null) {
            editor.selectedObject.UIElement.removeUIClass('selected');
            editor.panel.off();
        }
        // 현재 선택된 객체 디자인 추가
        this.UIElement.addUIClass('selected');
        editor.selectedObject = this;
        editor.panel.on(); // 세부 속성 출력
    }
    /**
     * 첫번째 group 객체 선택
     */
    selectFirstGroup() {
        if (this.form.children.length === 0) { return false; }

        this.form.children[0].UIElement.domElement.dispatchEvent(new Event('click'));
    }
    /**
     * 마지막 group 객체 선택
     */
    selectLastGroup() {
        if (this.form.children.length === 0) { return false; }

        const lastIndex = this.form.children.length - 1;
        this.form.children[lastIndex].UIElement.domElement.dispatchEvent(new Event('click'));
    }
    /**
     * 선택된 객체의 바로 위 동일 타입 객체 선택
     */
    selectUpObject() {
        if (this.selectedObject === null) { return false; }
        if (this.selectedObject instanceof ZForm) { return false; }

        const parentObject = this.selectedObject.parent;
        const selectIndex =  (this.selectedObject.displayDisplayOrder - 1) === -1 ?
            parentObject.children.length -1 : (this.selectedObject.displayDisplayOrder - 1);

        parentObject.children[selectIndex].UIElement.domElement.dispatchEvent(new Event('click'));
    }
    /**
     * 선택된 객체의 바로 아래 동일 타입 객체 선택
     */
    selectDownObject() {
        if (this.selectedObject === null) { return false; }
        if (this.selectedObject instanceof ZForm) { return false; }

        const parentObject = this.selectedObject.parent;
        const selectIndex =  (this.selectedObject.displayDisplayOrder + 1) === parentObject.children.length ?
            0 : (this.selectedObject.displayDisplayOrder + 1);

        parentObject.children[selectIndex].UIElement.domElement.dispatchEvent(new Event('click'));
    }
    /**
     * group, row, component 객체 복제
     */
    copyObject() {
        if (this.selectedObject === null) { return false; }
        if (this.selectedObject instanceof ZForm) { return false; }

        this.selectedObject.copyObject();
    }
    /**
     * group, row, component 객체 삭제
     */
    removeObject() {
        if (this.selectedObject === null) { return false; }
        if (this.selectedObject instanceof ZForm) { return false; }

        this.selectedObject.removeObject();
    }
    /**
     * 선택된 객체의 선택 해제
     */
    deSelectObject() {
        // 세부 속성 유효성 검증 실패시 동작을 중지함
        if (!this.panel.validationStatus) { return false; }

        // 이전 선택된 객체 디자인 삭제
        if (this.selectedObject !== null) {
            this.selectedObject.UIElement.removeUIClass('selected');
            this.selectedObject = null;
            this.panel.off();
        }
    }
    // 자식이 없는 group, row 삭제
    /**
     * 자식이 없는 row 삭제
     * row 삭제 후 group도 자식이 없으면 group도 삭제
     */
    deleteRowChildrenEmpty(object, histories) {
        if (object.type === FORM.LAYOUT.ROW && object.children.length === 0) {
            // group 에 자식이 없으면
            if (object.parent.type === FORM.LAYOUT.GROUP && object.parent.children.length === 1) {
                histories.push({ // group 삭제
                    type: 'remove',
                    from: { id: object.parent.parent.id, clone: object.parent.clone(true).toJson() },
                    to: { id: '', clone: null }
                });
                object.parent.parent.remove(object.parent);
            } else {
                histories.push({
                    type: 'remove',
                    from: { id: object.parent.id, clone: object.clone(true).toJson() },
                    to: { id: '', clone: null }
                });
                object.parent.remove(object);
            }
        }
    }
    /**
     * 폼 저장
     * @param boolean 저장후  팝업 닫을지 여부
     */
    saveForm(boolean) {
        // 세부 속성 유효성 검증 실패시 동작을 중지한다.
        if (!this.panel.validationStatus) { return false; }

        // 발행, 사용 상태일 경우, 저장이 불가능하다.
        const deployableStatus = ['form.status.publish', 'form.status.use'];
        if (deployableStatus.includes(this.data.status)) {
            zAlert.warning(i18n.msg('common.msg.onlySaveInEdit'));
            return false;
        }

        // '편집' 상태에서 '발행' or '사용' 상태로 저장하려는 경우 유효성 검증
        // 컴포넌트별 설정이 발행이 가능한지 확인한다.
        if (this.data.status === 'form.status.edit' && deployableStatus.includes(this.form.status)) {
            if (!this.form.validationCheckOnPublish()) {
                return false;
            }
        }

        // 저장할 데이터 가져오기
        const saveData  =  this.form.toJson();
        console.debug(saveData);

        // 저장
        aliceJs.fetchJson('/rest/forms/' + this.formId + '/data', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(saveData),
            showProgressbar: true
        }).then((formData) => {
            switch(formData.toString()) {
                case RESPONSE_CODE.STATUS_SUCCESS:
                    this.data = saveData;
                    this.history.saveHistoryIndex = this.history.undoList.length;
                    this.history.status = 0;
                    this.setFormName();
                    // 팝업 닫기
                    if (boolean) {
                        zAlert.success(i18n.msg('common.msg.save'), () => {
                            if (window.opener && !window.opener.closed) {
                                opener.location.reload();
                            }
                            window.close();
                        });
                    } else {
                        const date = new Date();
                        document.getElementById('saveInfo').innerText = i18n.msg('form.msg.saveInfo'
                            , i18n.userDateTime(date.toISOString()));
                        zAlert.success(i18n.msg('common.msg.save'));
                    }
                    break;
                case RESPONSE_CODE.STATUS_ERROR_DUPLICATE:
                    zAlert.warning(i18n.msg('form.msg.duplicateFormName'));
                    break;
                default:
                    zAlert.danger(i18n.msg('common.label.fail'));
                    break;
            }
        }).catch(err => {
            zAlert.warning(err);
        });
    }

    /**
     * 다른이름으로 저장하기 모달 오픈
     */
    openSaveAsModal() {
        if (!this.panel.validationStatus) { return false; }

        const saveAsModalTemplate = document.getElementById('saveAsModalTemplate');
        const saveAsModal = new modal({
            title: i18n.msg('common.btn.saveAs'),
            body: saveAsModalTemplate.content.cloneNode(true),
            classes: 'save-as',
            buttons: [
                {
                    content: i18n.msg('common.btn.save'),
                    classes: 'z-button primary',
                    bindKey: false,
                    callback: (modal) => {
                        const newFormName = document.getElementById('newFormName');
                        if (zValidation.emit('required', newFormName)) {
                            this.saveAsForm();
                            modal.hide();
                        }
                    }
                }, {
                    content: i18n.msg('common.btn.cancel'),
                    classes: 'z-button secondary',
                    bindKey: false,
                    callback: (modal) => {
                        modal.hide();
                    }
                }
            ],
            close: { closable: false },
            onCreate: function(modal) {
                OverlayScrollbars(document.getElementById('newFormDesc'), {
                    className: 'scrollbar',
                    resize: 'none',
                    sizeAutoCapable: true,
                    textarea: {
                        dynHeight: false,
                        dynWidth: false,
                        inheritedAttrs: 'class'
                    }
                });
            }
        });
        saveAsModal.show();
    }
    /**
     * 다른이름으로 저장
     */
    saveAsForm() {
        // 저장할 데이터 가져오기
        const saveData  =  this.form.toJson();
        saveData.name = document.getElementById('newFormName').value;
        saveData.desc = document.getElementById('newFormDesc').value;
        console.debug(saveData);
        // 저장
        aliceJs.fetchText('/rest/forms?saveType=saveas', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(saveData),
            showProgressbar: true
        }).then((formId) => {
            if (formId) {
                zAlert.success(i18n.msg('common.msg.save'), () => {
                    if (window.opener && !window.opener.closed) {
                        opener.location.reload();
                    }
                    window.name = 'form_' + formId + '_edit';
                    location.href = '/forms/' + formId + '/edit';
                });
            } else {
                zAlert.danger(i18n.msg('common.label.fail'));
            }
        }).catch(err => {
            zAlert.warning(err);
        });
    }
    /**
     * TODO: 미리보기
     */
    preview() {
        const previewData = this.form.toJson();
        zDocument.editable = false;
        zFormButton.init({ form: previewData}, zDocument);
        zDocument.makeDocument(previewData); // Form 생성
        zDocument.documentModal.show(); // 모달 표시
    }
    /**
     * 마우스 좌클릭 이벤트 핸들러
     * @param e 이벤트객체
     */
    onLeftClickHandler(e) {
        // 상단 드롭 다운 메뉴가 오픈되어 있으면 닫는다.
        if (e.target != null && !e.target.classList.contains('z-header-button-dropdown')) {
            document.querySelectorAll('.' + 'z-header-button-dropdown').forEach(function (dropdown) {
                if (dropdown.classList.contains('active')) {
                    dropdown.classList.remove('active');
                }
            });
        }
        // 폼 내부를 선택한게 아니라면, 기존 선택된 항목을 초기화한다.
        if (aliceJs.clickInsideElement(e, 'z-form-properties-panel-header') ||
            aliceJs.clickInsideElement(e, 'z-form-properties-panel')) {
            return false;
        }
    }

    /**
     * 상단 드롭다운 이벤트 핸들러
     */
    onDropdownClickHandler(e) {
        const target = e.target || e;
        const targetId = target.getAttribute('data-targetId');
        const changeTarget = document.getElementById(targetId);

        const actionType = target.getAttribute('data-actionType');
        if (changeTarget.firstElementChild.getAttribute('data-actionType') !== actionType) {
            // 기존 버튼 삭제
            changeTarget.removeChild(changeTarget.firstElementChild);
            // 버튼 추가
            const buttonTemplate = document.getElementById(actionType + 'ButtonTemplate');
            changeTarget.appendChild(buttonTemplate.content.cloneNode(true));
            // 이벤트 할당
            changeTarget.firstElementChild.addEventListener('click', this.onDropdownClickHandler.bind(this));
        }

        // 이벤트 실행
        switch (actionType) {
            case 'undo':
                this.history.undo();
                break;
            case 'redo':
                this.history.redo();
                break;
            case 'save':
                this.saveForm(false);
                break;
            case 'saveAs':
                this.openSaveAsModal();
                break;
            default:
                break;
        }
    }
}

export const zFormDesigner = new ZFormDesigner();
