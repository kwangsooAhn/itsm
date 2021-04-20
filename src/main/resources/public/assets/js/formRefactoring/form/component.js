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
        util.importMixin(this, this.getMixinByType(this.type));
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
            .setUIProperty('--data-column', this.columnWidth);
        // 컴포넌트 추가
        componentTooltip.UIComponent = new UIComponent()
            .setUIId(this.id)
            .addUIClass(this.type)
            .setUIAttribute('data-displayType', this.displayType);
        // 라벨 추가
        componentTooltip.UIComponent.UILabel = this.makeLabel();
        componentTooltip.UIComponent.addUI(componentTooltip.UIComponent.UILabel);

        // 엘리먼트 추가
        componentTooltip.UIComponent.UIElement = this.makeElement();
        componentTooltip.UIComponent.addUI(componentTooltip.UIComponent.UIElement);

        componentTooltip.addUI(componentTooltip.UIComponent);
        this.UIElement = componentTooltip;
    }
    // 타입에 따른 믹스인 호출
    getMixinByType(type) {
        switch(type) {
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
        this.UIElement.setUIProperty('--data-column', width);
    }

    setLabelPosition(value) {
        this.label.position = value;
        if (value === FORM.LABEL.POSITION.HIDDEN) {
            this.UIElement.UIComponent.UILabel.removeUIClass('on').addUIClass('off');
        } else {
            this.UIElement.UIComponent.UILabel.removeUIClass('off').addUIClass('on');
        }
        this.UIElement.UIComponent.UILabel.setUIProperty('--data-column', this.getLabelColumnWidth(value));
    }

    setLabelFontColor(color) {
        this.label.fontColor = color;
        this.UIElement.UIComponent.UILabel.UILabelText.setUIColor(color);
    }

    setLabelFontSize(size) {
        this.label.fontSize = size;
        this.UIElement.UIComponent.UILabel.UILabelText.setUIFontSize(size);
    }

    setLabelAlign(value) {
        this.label.align = value;
        this.UIElement.UIComponent.UILabel.setUITextAlign(value);
    }

    setLabelFontOptionBold(boolean) {
        this.label.bold = boolean;
        this.UIElement.UIComponent.UILabel.UILabelText
            .setUIFontWeight((boolean === 'true' ? 'bold' : ''));
    }

    setLabelFontOptionItalic(boolean) {
        this.UIElement.UIComponent.UILabel.UILabelText
            .setUIFontStyle((boolean === 'true' ? 'italic' : ''));
        this.label.italic = boolean;
    }

    setLabelFontOptionUnderline(boolean) {
        this.label.underline = boolean;
        this.UIElement.UIComponent.UILabel.UILabelText
            .setUITextDecoration((boolean === 'true' ? 'underline' : ''));
    }

    setLabelText(text) {
        this.label.text = text;
        this.UIElement.UIComponent.UILabel.UILabelText.setUITextContent(text);
    }

    // 복사 (자식 포함)
    copy(source) {
        this.type = source.type;
        this.id =  source.id;
        this.parent = source.parent;
        this.children = source.children;
        this.displayOrder = source.displayOrder;
        this.columnWidth = source.columnWidth;
        this.displayType = source.displayType;
        this.isTopic = source.isTopic;
        this.mapId = source.mapId;
        this.tags = source.tags;
        this.value = source.value;
        this.label = source.label;
        this.element = source.element;
        this.validate = source.validate;
        this.UIElement = source.UIElement;
        return this;
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