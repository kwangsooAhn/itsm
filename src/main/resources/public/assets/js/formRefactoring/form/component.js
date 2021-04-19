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

const DEFAULT_LABEL_PROPERTY = {
    position: 'left', // 라벨 위치 hidden | top | left
    fontSize: '16',
    fontColor: 'rgba(0,0,0,1)',
    bold: false,
    italic: false,
    underline: false,
    align: 'left',
    text: 'COMPONENT LABEL'
};

export default class Component {
    constructor(data = {}) {
        this.type = data.type || 'component';
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
        this.label = util.mergeObject(DEFAULT_LABEL_PROPERTY, data.label);

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
        this.initProperty();
        // 컴포넌트용 툴팁
        const componentTooltip = new UIComponentTooltip()
            .setProperty('--data-column', this.columnWidth);
        // 컴포넌트 추가
        componentTooltip.UIComponent = new UIComponent()
            .setId(this.id)
            .addClass(this.type)
            .setAttribute('data-displayType', this.displayType);
        // 라벨 추가
        componentTooltip.UIComponent.UILabel = this.makeLabel();
        componentTooltip.UIComponent.add(componentTooltip.UIComponent.UILabel);

        // 엘리먼트 추가
        componentTooltip.UIComponent.UIElement = this.makeElement();
        componentTooltip.UIComponent.add(componentTooltip.UIComponent.UIElement);

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

    setMapId(id) {
        this.mapId = id;
    }

    setIsTopic(boolean) {
        this.isTopic = boolean;
    }
    // TODO: 태그 기능 추후 구현 예정
    setTags() {}

    setColumnWidth(width) {
        this.columnWidth = width;
        this.UIElement.setProperty('--data-column', width);
    }

    setLabelPosition(value) {
        this.label.position = value;
        if (value === FORM.LABEL.POSITION.HIDDEN) {
            this.UIElement.UIComponent.UILabel.removeClass('on').addClass('off');
        } else {
            this.UIElement.UIComponent.UILabel.removeClass('off').addClass('on');
        }
        this.UIElement.UIComponent.UILabel.setProperty('--data-column', this.getLabelColumnWidth(value));
    }

    setLabelFontColor(color) {
        this.label.fontColor = color;
        this.UIElement.UIComponent.UILabel.UILabelText.setColor(color);
    }

    setLabelFontSize(size) {
        this.label.fontSize = size;
        this.UIElement.UIComponent.UILabel.UILabelText.setFontSize(size);
    }

    setLabelAlign(value) {
        this.label.align = value;
        this.UIElement.UIComponent.UILabel.setTextAlign(value);
    }

    setLabelFontOptionBold(boolean) {
        this.label.bold = boolean;
        this.UIElement.UIComponent.UILabel.UILabelText
            .setFontWeight((boolean === 'true' ? 'bold' : ''));
    }

    setLabelFontOptionItalic(boolean) {
        this.UIElement.UIComponent.UILabel.UILabelText
            .setFontStyle((boolean === 'true' ? 'italic' : ''));
        this.label.italic = boolean;
    }

    setLabelFontOptionUnderline(boolean) {
        this.label.underline = boolean;
        this.UIElement.UIComponent.UILabel.UILabelText
            .setTextDecoration((boolean === 'true' ? 'underline' : ''));
    }

    setLabelText(text) {
        this.label.text = text;
        this.UIElement.UIComponent.UILabel.UILabelText.setTextContent(text);
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