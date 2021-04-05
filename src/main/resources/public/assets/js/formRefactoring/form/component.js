/**
 * Component 공통 Class
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

import * as util from '../lib/util.js';
import * as mixin from '../lib/mixins.js';
import { CLASS_PREFIX, FORM } from '../lib/constants.js';
import { inputBoxMixin } from './component/inputBox.js';
import { UIDiv } from '../lib/ui.js';


export default class Component {
    constructor(data = {}) {
        this.type = data.type;
        this.id = data.id || workflowUtil.generateUUID();
        this.parent = null;        // 부모 객체
        this.children = [];        // 자식 객체
        this.displayOrder = 0;     // 표시 순서
        this.columnWidth = data.columnWidth || '12';
        this.displayType = data.displayType || 'editable'; // (readonly, editable, required, hidden)
        this.isTopic = data.isTopic || false;
        this.mapId = data.mapId || '';
        this.tags = data.tags || [];
        this.value = data.value || '${default}';
        this.label = data.label || util.mergeObject(data.label || FORM.DEFAULT.COMPONENT_LABEL);
        this.element = data.element || {};
        this.validate = data.validate || {};

        // Control Mixin import
        util.importMixin(this, mixin.controlMixin);
        // 타입에 따른 Mixin import
        util.importMixin(this, this.getMixinByType());
        // 라벨 Mixin import
        util.importMixin(this, mixin.componentLabelMixin);

        this.init();
    }
    // 초기화
    init() {
        // 내부 property 초기화
        this.setProperty();
        // 컴포넌트용 툴팁
        const componentTooltip = new UIComponentTooltip();
        // 컴포넌트 추가
        componentTooltip.UIComponent = new UIComponent()
            .setId(this.id)
            .addClass(this.type)
            .setAttribute('displayType', this.displayType)
            .setStyle('--data-column', this.columnWidth);
        // 내부 엘리먼트 추가
        componentTooltip.UIComponent.UIField = this.makeField();
        componentTooltip.UIComponent.add(componentTooltip.UIComponent.UIField);

        componentTooltip.add(componentTooltip.UIComponent);
        this.UIElement = componentTooltip;
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

export class UIComponentTooltip extends UIDiv {
    constructor() {
        super();
        this.domElement.className = CLASS_PREFIX + 'component-tooltip';
    }
}

export class UIComponent extends UIDiv {
    constructor() {
        super();
        this.domElement.className = CLASS_PREFIX + 'component';
        this.domElement.tabIndex = 0;
    }
}