/**
 * Component 공통 Class
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import * as mixin from '../lib/mixins.js';
import { CLASS_PREFIX, FORM } from '../lib/constants.js';
import { inputBoxMixin } from './component/inputBox.js';
import { UIDiv } from '../lib/ui.js';

const DEFAULT_PROPERTY = {
    label: {
        position: 'left', // 라벨 위치 hidden | top | left
        fontSize: '16',
        fontColor: 'rgba(0,0,0,1)',
        bold: false,
        italic: false,
        underline: false,
        align: 'left',
        text: 'COMPONENT LABEL'
    },
    display: {
        displayOrder: 0,     // 표시 순서
        columnWidth: '12'
    }
};
Object.freeze(DEFAULT_PROPERTY);

export default class Component {
    constructor(data = {}) {
        this.type = data.type || 'component';
        this.id = data.id || workflowUtil.generateUUID();
        this.parent = null;        // 부모 객체
        this.children = [];        // 자식 객체
        this.displayType = data.displayType || 'editable'; // (readonly, editable, required, hidden)
        this.isTopic = data.isTopic || false;
        this.mapId = data.mapId || '';
        this.tags = data.tags || [];
        this.value = data.value || '${default}';

        this.display = Object.assign({}, DEFAULT_PROPERTY.display, data.display);
        this.label = Object.assign({}, DEFAULT_PROPERTY.label, data.label);
        this.element = data.element || {};
        this.validate = data.validate || {};

        // Control Mixin import
        aliceJs.importMixin(this, mixin.controlMixin);
        // 타입에 따른 Mixin import
        aliceJs.importMixin(this, this.getMixinByType(this.type));
        // 라벨 Mixin import
        aliceJs.importMixin(this, mixin.componentLabelMixin);
        // Tooltip Mixin import
        aliceJs.importMixin(this, mixin.toolTipMenuMixin);

        this.init();
    }
    // 초기화
    init() {
        // 내부 property 초기화
        this.initProperty();
        // 컴포넌트용 툴팁
        const componentTooltip = new UIComponentTooltip()
            .setUIProperty('--data-column', this.display.columnWidth);
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
        // 툴팁
        componentTooltip.UITooltipMenu = this.makeTooltip();
        componentTooltip.addUI(componentTooltip.UITooltipMenu);

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

    getMapId() {
        return this.mapId;
    }

    setIsTopic(boolean) {
        this.isTopic = boolean;
    }

    getIsTopic() {
        return this.isTopic;
    }

    setTags(tags) {
        this.tags = tags;
    }

    getTags() {
        return this.tags;
    }

    setDisplayColumnWidth(width) {
        this.display.columnWidth = width;
        this.UIElement.setUIProperty('--data-column', width);
    }

    getDisplayColumnWidth() {
        return this.display.columnWidth;
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

    getLabelPosition() {
        return this.label.position;
    }

    setLabelFontColor(color) {
        this.label.fontColor = color;
        this.UIElement.UIComponent.UILabel.UILabelText.setUIColor(color);
    }

    getLabelFontColor() {
        return this.label.fontColor;
    }

    setLabelFontSize(size) {
        this.label.fontSize = size;
        this.UIElement.UIComponent.UILabel.UILabelText.setUIFontSize(size);
    }

    getLabelFontSize() {
        return this.label.fontSize;
    }

    setLabelAlign(value) {
        this.label.align = value;
        this.UIElement.UIComponent.UILabel.setUITextAlign(value);
    }

    getLabelAlign() {
        return this.label.align;
    }

    setLabelFontOptionBold(boolean) {
        this.label.bold = boolean;
        this.UIElement.UIComponent.UILabel.UILabelText
            .setUIFontWeight((boolean === 'true' ? 'bold' : ''));
    }

    getLabelFontOptionBold() {
        return this.label.bold;
    }

    setLabelFontOptionItalic(boolean) {
        this.UIElement.UIComponent.UILabel.UILabelText
            .setUIFontStyle((boolean === 'true' ? 'italic' : ''));
        this.label.italic = boolean;
    }

    getLabelFontOptionItalic() {
        return this.label.italic;
    }

    setLabelFontOptionUnderline(boolean) {
        this.label.underline = boolean;
        this.UIElement.UIComponent.UILabel.UILabelText
            .setUITextDecoration((boolean === 'true' ? 'underline' : ''));
    }

    getLabelFontOptionUnderline() {
        return this.label.underline;
    }

    setLabelText(text) {
        this.label.text = text;
        this.UIElement.UIComponent.UILabel.UILabelText.setUITextContent(text);
    }

    getLabelText() {
        return this.label.text;
    }

    /**
     * 현재 객체를 대상이 되는 객체로 변경 (복사) 하여 반환한다
     * @param source 대상 객체
     * @param flag 객체의 키가 되는 id도 복제할지 여부 (true이면 id도 복제됨)
     */
    copy(source, flag) {
        this.type = source.type;
        this.parent = source.parent;
        this.children = source.children;
        this.display = source.display;
        this.displayType = source.displayType;
        this.isTopic = source.isTopic;
        this.mapId = source.mapId;
        this.tags = source.tags;
        this.value = source.value;
        this.label = source.label;
        this.element = source.element;
        this.validate = source.validate;
        if (flag) { this.id = source.id; }

        this.init();
        return this;
    }
    // json 데이터 추출
    toJson() {
        return {
            id: this.id,
            type: this.type,
            display: this.display,
            displayType: this.displayType,
            isTopic: this.isTopic,
            mapId: this.mapId,
            tags: this.tags,
            value: this.value,
            label: this.label,
            element: this.element,
            validate: this.validate
        };
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