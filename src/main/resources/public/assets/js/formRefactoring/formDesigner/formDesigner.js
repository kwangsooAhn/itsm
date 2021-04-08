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
import { FORM } from '../lib/constants.js';
import History from './history.js';
import Panel from './panel.js';
import Form from '../form/form.js';
import Group, { UIGroupTooltip } from '../form/group.js';
import Row, { UIRowTooltip } from '../form/row.js';
import Component, { UIComponentTooltip } from '../form/component.js';
import { Dragger } from './dragger.js';

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

        // 초기화
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
    // 컴포넌트 팔레트 초기화 및 이벤트 등록
    initComponentPalette() {
        // TODO: 커스텀 컴포넌트 load

        //  TODO: drag & drop 이벤트 등록
        this.dragger = new Dragger(this);
        const dragItems = document.querySelectorAll('.draggable');
        dragItems.forEach(item => {
            this.dragger.addDrag(item);
        });
    }
    // 폼 초기화
    initForm(formId) {
        // 폼 데이터 load
        //util.fetchJson({ method: 'GET', url: '/rest/form/' + formId + '/data' })
        util.fetchJson({ method: 'GET', url: '/assets/js/formRefactoring/formDesigner/data_210320.json' })
            .then((formData) => {
                // TODO: 전달된 데이터의 서버 시간에 따른 날짜/시간 처리
                //this.data = aliceForm.reformatCalendarFormat('read', response.json());
                // TODO: displayOrder 로 정렬

                this.data = formData;
                this.makeDomElement();
                this.setFormName();
                this.initComponentPalette();
            });
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
        if (this.data.hasOwnProperty('groups')) {
            this.data.groups.forEach( g => {
                const group = this.addObjectByType(FORM.LAYOUT.GROUP, g, this.form);
                if (g.hasOwnProperty('rows')) {
                    g.rows.forEach( r => {
                        const row = this.addObjectByType(FORM.LAYOUT.ROW, r, group);
                        if (r.hasOwnProperty('components')) {
                            r.components.forEach( c => {
                                this.addObjectByType(FORM.LAYOUT.COMPONENT, c, row);
                            });
                        }
                    });
                }
            });
        }
        this.domElement.appendChild(this.form.UIElement.domElement);
    }
    // form, group, row, component 객체 추가
    addObjectByType(type, data, parent) {
        let object = null;
        switch(type) {
        case 'form':
            object = new Form(data);
            object.UIElement.onClick(this.selectObject.bind(object)); // 선택 이벤트 추가
            break;
        case 'group':
            object = new Group(data);
            object.UIElement.UIGroup.onClick(this.selectObject.bind(object));
            break;
        case 'row':
            object = new Row(data);
            object.UIElement.UIRow.onClick(this.selectObject.bind(object));
            break;
        case 'component':
            object = new Component(data);
            object.UIElement.UIComponent.onClick(this.selectObject.bind(object));
            break;
        default:
            break;
        }
        if (parent !== undefined) {
            parent.add(object);
        }
        return object;
    }
    // group, row, component 객체 삭제
    removeObject(object) {
        if (object.parent === null) { return false; } // 폼은 삭제 불가
        return object.parent.remove(object);
    }
    // form 저장
    saveForm() {}
    // 다른 이름으로 form 저장
    saveAsForm() {}
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
}

