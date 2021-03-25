/**
 * Group 내부를 이루는 구성 Class.
 *
 *  내부적으로 너비를 가지는 복수개의 엘리먼트들로 구성된다.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

import InputBox from './component/inputBox.js';

let displayOrder = 0; // row 출력 순서

export default class Row {
    constructor(data = {}) {
        this.id =  data.id || workflowUtil.generateUUID();
        this.margin = data.margin || '10 0 10 0'; // row 간 간격(위 오른쪽 아래 왼쪽)
        this.padding = data.padding || '10 10 10 10'; // row 내부 여백(위 오른쪽 아래 왼쪽)
        this.displayOrder = data.displayOrder || ++displayOrder;

        // component 추가
        this.components = [];
        if (data.hasOwnProperty('components')) {
            data.components.forEach( r => {
                this.addComponent(r.type, r);
            });
        }
    }
    // 컴포넌트 추가
    addComponent(type, data) {
        switch(type) {
        case 'inputBox':
            this.components.push(new InputBox(data));
            break;
        case 'textArea':
            // this.components.push(new TextArea(data));
            break;
        case 'textEditor':
            // this.components.push(new TextEditor(data));
            break;
        case 'dropdown':
            // this.components.push(new Dropdown(data));
            break;
        case 'radio':
            // this.components.push(new RadioBox(data));
            break;
        case 'checkBox':
            // this.components.push(new CheckBox(data));
            break;
        case 'label':
            // this.components.push(new Label(data));
            break;
        case 'image':
            // this.components.push(new ImageBox(data));
            break;
        case 'divider':
            // this.components.push(new Divider(data));
            break;
        case 'date':
            // this.components.push(new DateBox(data));
            break;
        case 'time':
            // this.components.push(new TimeBox(data));
            break;
        case 'dateTime':
            // this.components.push(new DateTimeBox(data));
            break;
        case 'fileUpload':
            // this.components.push(new FileUpload(data));
            break;
        case 'customCode':
            // this.components.push(new CustomCode(data));
            break;
        case 'dynamicRowTable':
            // this.components.push(new DynamicRowTable(data));
            break;
        case 'ci':
            // this.components.push(new ConfigurationItem(data));
            break;
        default:
            break;
        }
    }
}