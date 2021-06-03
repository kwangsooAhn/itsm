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
import { UIDiv } from '../lib/ui.js';
import { inputBoxMixin } from './component/inputBox.js';
import { textAreaMixin } from './component/textArea.js';
import { textEditorMixin } from './component/textEditor.js';
import { dropdownMixin } from './component/dropdown.js';
import { radioMixin } from './component/radio.js';
import { checkBoxMixin } from './component/checkBox.js';
import { labelMixin } from './component/label.js';
import { imageMixin } from './component/image.js';
import { dividerMixin } from './component/divider.js';
import { dateMixin } from './component/date.js';
import { timeMixin } from './component/time.js';
import { dataTimeMixin } from './component/dateTime.js';
import { fileUploadMixin } from './component/fileUpload.js';
import { customCodeMixin } from './component/customCode.js';
import { dynamicRowTableMixin } from './component/dynamicRowTable.js';
import { ciMixin } from './component/ci.js';

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
        this.parent = null;        // 부모 객체
        this.children = [];        // 자식 객체
        this.data = data;
        this._type = data.type || 'component';
        this._id = data.id || workflowUtil.generateUUID();
        this._displayType = data.displayType || 'editable';
        this._isTopic = data.isTopic || false;
        this._mapId = data.mapId || '';
        this._tags = data.tags || [];
        this._value = data.value || '${default}';
        this._display = Object.assign({}, DEFAULT_PROPERTY.display, data.display);
        this._label = Object.assign({}, DEFAULT_PROPERTY.label, data.label);

        // Control Mixin import
        aliceJs.importMixin(this, mixin.controlMixin);
        // 타입에 따른 Mixin import
        aliceJs.importMixin(this, this.getMixinByType(this._type));
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
            .setUIProperty('--data-column', this.displayColumnWidth);
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
            return textAreaMixin;
        case 'textEditor':
            return textEditorMixin;
        case 'dropdown':
            return dropdownMixin;
        case 'radio':
            return radioMixin;
        case 'checkBox':
            return checkBoxMixin;
        case 'label':
            return labelMixin;
        case 'image':
            return imageMixin;
        case 'divider':
            return dividerMixin;
        case 'date':
            return dateMixin;
        case 'time':
            return timeMixin;
        case 'dateTime':
            return dataTimeMixin;
        case 'fileUpload':
            return fileUploadMixin;
        case 'customCode':
            return customCodeMixin;
        case 'dynamicRowTable':
            return dynamicRowTableMixin;
        case 'ci':
            return ciMixin;
        default:
            break;
        }
    }

    get type() {
        return this._type;
    }

    set type(type) {
        this._type = type;
    }

    get id() {
        return this._id;
    }

    set id(id) {
        this._id = id;
    }

    get displayType() {
        return this._displayType;
    }

    set displayType(displayType) {
        this._displayType = displayType;
    }

    set mapId(mapId) {
        this._mapId = mapId;
    }

    get mapId() {
        return this._mapId;
    }

    set isTopic(boolean) {
        this._isTopic = boolean;
    }

    get isTopic() {
        return this._isTopic;
    }

    set tags(tags) {
        this._tags = tags;
    }

    get tags() {
        return this._tags;
    }

    set display(display) {
        this._display = display;
    }

    get display() {
        return this._display;
    }

    set displayDisplayOrder(displayOrder) {
        this._display.displayOrder = displayOrder;
    }

    get displayDisplayOrder() {
        return this._display.displayOrder;
    }

    set displayColumnWidth(width) {
        this._display.columnWidth = width;
        this.UIElement.setUIProperty('--data-column', width);
    }

    get displayColumnWidth() {
        return this._display.columnWidth;
    }

    set label(label) {
        this._label = label;
    }

    get label() {
        return this._label;
    }

    set labelPosition(value) {
        this._label.position = value;
        if (value === FORM.LABEL.POSITION.HIDDEN) {
            this.UIElement.UIComponent.UILabel.removeUIClass('on').addUIClass('off');
        } else {
            this.UIElement.UIComponent.UILabel.removeUIClass('off').addUIClass('on');
        }
        this.UIElement.UIComponent.UILabel.setUIProperty('--data-column', this.getLabelColumnWidth(value));
    }

    get labelPosition() {
        return this._label.position;
    }

    set labelFontColor(color) {
        this._label.fontColor = color;
        this.UIElement.UIComponent.UILabel.UILabelText.setUIColor(color);
    }

    get labelFontColor() {
        return this._label.fontColor;
    }

    set labelFontSize(size) {
        this._label.fontSize = size;
        this.UIElement.UIComponent.UILabel.UILabelText.setUIFontSize(size);
    }

    get labelFontSize() {
        return this._label.fontSize;
    }

    set labelAlign(value) {
        this._label.align = value;
        this.UIElement.UIComponent.UILabel.setUITextAlign(value);
    }

    get labelAlign() {
        return this._label.align;
    }

    set labelFontOptionBold(boolean) {
        this._label.bold = boolean;
        this.UIElement.UIComponent.UILabel.UILabelText
            .setUIFontWeight((boolean === 'true' ? 'bold' : ''));
    }

    get labelFontOptionBold() {
        return this._label.bold;
    }

    set labelFontOptionItalic(boolean) {
        this.UIElement.UIComponent.UILabel.UILabelText
            .setUIFontStyle((boolean === 'true' ? 'italic' : ''));
        this._label.italic = boolean;
    }

    get labelFontOptionItalic() {
        return this._label.italic;
    }

    set labelFontOptionUnderline(boolean) {
        this._label.underline = boolean;
        this.UIElement.UIComponent.UILabel.UILabelText
            .setUITextDecoration((boolean === 'true' ? 'underline' : ''));
    }

    get labelFontOptionUnderline() {
        return this._label.underline;
    }

    set labelText(text) {
        this._label.text = text;
        this.UIElement.UIComponent.UILabel.UILabelText.setUITextContent(text);
    }

    get labelText() {
        return this._label.text;
    }

    /**
     * 현재 객체를 대상이 되는 객체로 변경 (복사) 하여 반환한다
     * @param source 대상 객체
     * @param flag 객체의 키가 되는 id도 복제할지 여부 (true이면 id도 복제됨)
     */
    copy(source, flag) {
        this.parent = source.parent;
        this.children = source.children;
        this.data = source.data;
        this._type = source.type;
        this._display = source.display;
        this._displayType = source.displayType;
        this._isTopic = source.isTopic;
        this._mapId = source.mapId;
        this._tags = source.tags;
        this._value = source.value;
        this._label = source.label;
        if (flag) { this._id = source.id; }

        this.init();
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