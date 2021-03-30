/**
 * Component 공통 Class
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

import * as MIXIN from '../lib/mixin.js';
import {inputBoxMixin} from './component/inputBox.js';

export default class Component {
    constructor(data = {}) {
        this.type = data.type;
        this.id = data.id || workflowUtil.generateUUID();
        this.parent = null;        // 부모 객체
        this.children = [];        // 자식 객체
        this.displayOrder = 0;     // 표시 순서
        this.columnWidth = data.columnWidth || '12';
        this.displayType = data.displayType || 'editable'; // 출력 타입 (readonly, editable, required, hidden)
        this.isTopic = data.isTopic || false;
        this.mapId = data.mapId || '';
        this.tags = data.tags || [];
        this.value = data.value || '${default}';
        this.label = data.label || {};
        this.element = data.element || {};
        this.validate = data.validate || {};

        // Control Mixin import
        MIXIN.importMixin(this, MIXIN.controlMixin);
        // 타입에 따른 Mixin import
        MIXIN.importMixin(this, this.getMixinByType());

        this.init();
    }
    // 초기화
    init() {
        this.domElem = this.makeDomElement();
        // TODO: 라벨 추가
        // element 추가
        this.domElem.appendChild(this.makeElement());
    }
    // DOM 엘리먼트 생성
    makeDomElement() {
        const component = document.createElement('div');
        component.id = this.id;
        component.className = this.type;
        return component;
    }
    // 타입에 따른 믹스인 호출
    getMixinByType() {
        switch(this.type) {
        case 'inputBox':
            return inputBoxMixin;
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
    }
}