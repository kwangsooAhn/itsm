/**
 * @projectDescription Form Designer Library
 *
 * @author woodajung
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import { FORM } from '../lib/constant.js';
import History from './history.js';
import Panel from './panel.js';
import Form from '../layout/form.js';
import Group from '../layout/group.js';
import Row from '../layout/row.js';
import Component from '../layout/component.js';

export default class FormDesigner {
    constructor(formId) {
        this.domElem = document.getElementById('drawingBoard') || document.body;
        // TODO: 1. 화면 커스텀 컴포넌트 load
        // this.componentPalette = document.getElementById('componentPalette'); // 컴포넌트 팔레트

        // 2. 커스텀 코드 정보 load - 커스텀 코드 컴포넌트에서 사용되기 때문에 우선 로드해야 함
        fetch('/rest/custom-codes?viewType=editor').then((response) =>
            response.json()
        ).then((customData) => {
            FORM.CUSTOM_CODE = customData;
            // 3. 세부 속성 load
            return fetch('/assets/js/formRefactoring/form/properties.json');
        }).then((response) => response.json()).then((propertiesData) => {
            this.panel = new Panel(propertiesData);
            // TODO: 4. 폼 데이터 로드
            //return fetch('/rest/form/' + formId + '/data');
            return fetch('/assets/js/formRefactoring/form/data_210320.json');
        }).then((response) => response.json()).then((formData) => {
            // TODO: 5. 전달된 데이터의 서버 시간에 따른 날짜/시간 처리
            //this.data = aliceForm.reformatCalendarFormat('read', response.json());
            // TODO: 6. displayOrder 로 정렬
            this.data = formData;
            this.setForm();
        }).then(() => {
            // 6. 이력 추가
            this.history = new History(this);
            this.setName();

            // TODO: 7. 버튼 초기화
            // TODO: - 상단 메뉴 버튼 액션 추가
            // TODO: - 단축키 추가

            // TODO: 8. drag & drop 이벤트 등록

            // TODO: 9. 세부 속성 표시
            this.panel.on(COMPONENT.FORM);
        });
    }
    // 폼 초기화
    setForm() {
        this.form = this.addRenderer(COMPONENT.FORM, this.data);
        if (this.data.hasOwnProperty('groups')) {
            this.data.groups.forEach( g => {
                const group = this.addRenderer(COMPONENT.GROUP, g, this.form);
                if (g.hasOwnProperty('rows')) {
                    g.rows.forEach( r => {
                        const row = this.addRenderer(COMPONENT.ROW, r, group);
                        if (r.hasOwnProperty('components')) {
                            r.components.forEach( c => {
                                this.addRenderer(c.type, c, row);
                            });
                        }
                    });
                }
            });
        }
        this.domElem.appendChild(this.form.domElem);
    }
    // Render 객체 추가
    addRenderer(type, data, parent) {
        let object = null;
        switch(type) {
        case 'form':
            object = new Form(data);
            break;
        case 'group':
            object = new Group(data);
            break;
        case 'row':
            object = new Row(data);
            break;
        case 'inputBox':
            object = new InputBox(data);
            break;
        case 'textArea':
            //object = new TextArea(data);
            break;
        case 'textEditor':
            //object = new TextEditor(data);
            break;
        case 'dropdown':
            //object = new Dropdown(data);
            break;
        case 'radio':
            //object = new Radio(data);
            break;
        case 'checkBox':
            //object = new CheckBox(data);
            break;
        case 'label':
            //object = new Label(data);
            break;
        case 'image':
            //object = new ImageBox(data);
            break;
        case 'divider':
            //object = new Divider(data);
            break;
        case 'date':
            //object = new Date(data);
            break;
        case 'time':
            //object = new Time(data);
            break;
        case 'dateTime':
            //object = new dataTime(data);
            break;
        case 'fileUpload':
            //object = new FileUpload(data);
            break;
        case 'customCode':
            //object = new CustomCode(data);
            break;
        case 'dynamicRowTable':
            //object = new DynamicRowTable(data);
            break;
        case 'ci':
            //object = new ConfigurationItem(data);
            break;
        default:
            break;
        }
        if (parent !== undefined) {
            parent.add(object);
        }
        return object;
    }
    // 컴포넌트 삭제
    removeRenderer(object) {
        if (object.parent === null) { return false; } // 폼은 삭제 불가
        return object.parent.remove(object);
    }
    // 폼 디자이너 상단 이름 출력
    setName() {
        document.getElementById('form-name').textContent =
            (this.history.status ? '*' : '') + (this.data.name);
    }
}

