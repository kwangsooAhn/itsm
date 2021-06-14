/**
 * Label Mixin
 *
 *
 * @author
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import { SESSION, FORM, CLASS_PREFIX } from '../../lib/zConstants.js';
import { zValidation } from '../../lib/zValidation.js';
import { UIDiv, UILabel } from '../../lib/zUI.js';
import ZInputBoxProperty from '../../formDesigner/property/type/zInputBoxProperty.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZSwitchButtonProperty from '../../formDesigner/property/type/zSwitchButtonProperty.js';
import ZToggleButtonProperty from '../../formDesigner/property/type/zToggleButtonProperty.js';
import ZColorPickerProperty from '../../formDesigner/property/type/zColorPickerProperty.js';
import {UNIT} from '../../lib/zConstants.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '12',
        labelWidth: '10',
        text: '',
        fontSize: '12',
        align: 'left',
        fontOption: '',
        fontOptionBold: 'N',
        fontOptionItalic: 'N',
        fontOptionUnderline: 'N',
        fontColor: '',
    },
    validation: {
        required: false, // 필수값 여부
    }
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const labelMixin = {

    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
        this._validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.data.validation);
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass(CLASS_PREFIX + 'element')
            .setUIProperty('--data-column', this.elementColumnWidth)
            .setUITextAlign(this.elementAlign);

        const elementCssText = `color:${this.elementFontColor};` +
            `font-size:${this.elementFontSize + UNIT.PX};` +
            `${this.elementFontOptionBold ? 'font-weight:bold;' : ''}` +
            `${this.elementFontOptionItalic ? 'font-style:italic;' : ''}` +
            `${this.elementFontOptionItalic ? 'text-decoration:underline;' : ''}`;

        element.UILabel = new UILabel().setUICSSText(elementCssText)
            .setUITextContent(this.elementText);
        element.addUI(element.UILabel);

        return element;
    },
    set element(element) {
        this._element = element;
    },
    get element() {
        return this._element;
    },
    set elementColumnWidth(width) {
        this._element.columnWidth = width;
        this.UIElement.UIComponent.UIElement.setUIProperty('--data-column', width);
        this.UIElement.UIComponent.UILabel.setUIProperty('--data-column',
            this.getLabelColumnWidth(this.labelPosition));
    },
    get elementColumnWidth() {
        return this._element.columnWidth;
    },
    set elementLabelWidth(width) {
        this._element.labelWidth = width;
        this.UIElement.UIComponent.UIElement.setUIProperty('--data-column', width);
        this.UIElement.UIComponent.UILabel.setUIProperty('--data-column',
            this.getLabelColumnWidth(this.labelPosition));
    },
    get elementLabelWidth() {
        return this._element.labelWidth;
    },
    set elementText(text) {
        this._element.text = text;
        this.UIElement.UIComponent.UIElement.UILabel.setUITextContent(text);
    },
    get elementText() {
        return this._element.text;
    },
    set elementFontSize(size) {
        this._element.fontSize = size;
        this.UIElement.UIComponent.UIElement.UILabel.setUIFontSize(size + UNIT.PX);
    },
    get elementFontSize() {
        return this._element.fontSize;
    },
    set elementAlign(align) {
        this._element.align = align;
        this.UIElement.UIComponent.UIElement.setUITextAlign(align);
    },
    get elementAlign() {
        return this._element.align;
    },
    set elementFontOption(option) {
        this._element.fontOption = option;
    },
    get elementFontOption() {
        return this._element.fontOption;
    },
    set elementFontOptionBold(boolean) {
        this._label.bold = boolean;
        this.UIElement.UIComponent.UIElement.UILabel
            .setUIFontWeight((boolean === 'true' ? 'bold' : ''));
    },
    get elementFontOptionBold() {
        return this._label.bold;
    },
    set elementFontOptionItalic(boolean) {
        this._label.italic = boolean;
        this.UIElement.UIComponent.UIElement.UILabel
            .setUIFontStyle((boolean === 'true' ? 'italic' : ''));
    },
    get elementFontOptionItalic() {
        return this._label.italic;
    },
    set elementFontOptionUnderline(boolean) {
        this._label.underline = boolean;
        this.UIElement.UIComponent.UIElement.UILabel
            .setUITextDecoration((boolean === 'true' ? 'underline' : ''));
    },
    get elementFontOptionUnderline() {
        return this._label.underline;
    },
    set elementFontColor(color) {
        this._element.fontColor = color;
        this.UIElement.UIComponent.UIElement.UILabel.setUIColor(color);
    },
    get elementFontColor() {
        return this._element.fontColor;
    },
    set value(value) {
        this._value = value;
    },
    get value() {
        return this._value;
    },
    // input box 값 변경시 이벤트 핸들러
    updateValue(e) {
        e.stopPropagation();
        e.preventDefault();
        // enter, tab 입력시
        if (e.type === 'keyup' && (e.keyCode === 13 || e.keyCode === 9)) {
            return false;
        }
        // 유효성 검증
        // keyup 일 경우 type, min, max 체크
        if (e.type === 'keyup' && !zValidation.keyUpValidationCheck(e.target)) {
            return false;
        }
        // change 일 경우 minLength, maxLength 체크
        if (e.type === 'change' && !zValidation.changeValidationCheck(e.target)) {
            return false;
        }

        this.value = e.target.value;
    },
    getProperty() {

        // element - text
        const elementTextProperty = new ZInputBoxProperty('element.text', this.elementText);
        elementTextProperty.columnWidth = '8';

        // element - fontSize
        const elementFontSizeProperty = new ZInputBoxProperty('element.fontSize', this.elementFontSize)
            .setValidation(false, 'number', '10', '100', '', '');
        elementFontSizeProperty.unit = UNIT.PX;
        elementFontSizeProperty.columnWidth = '3';

        // element - align
        const elementAlignProperty = new ZSwitchButtonProperty('element.align', this.elementAlign, [
            { 'name': 'icon-align-left', 'value': 'left' },
            { 'name': 'icon-align-center', 'value': 'center' },
            { 'name': 'icon-align-right', 'value': 'right' }
        ]);
        elementAlignProperty.columnWidth = '5';

        // element - fontOption
        const elementFontOption = [
            { 'name': 'icon-bold', 'value': 'bold' },
            { 'name': 'icon-italic', 'value': 'italic' },
            { 'name': 'icon-underline', 'value': 'underline' }
        ];
        const elementFontValue = elementFontOption.map((item) => {
            const method = item.value.substr(0, 1).toUpperCase() + item.value.substr(1, item.value.length);
            return this['elementFontOption' + method] ? 'Y' : 'N';
        }).join('|');
        const elementFontOptionProperty = new ZToggleButtonProperty('element.fontOption', elementFontValue, elementFontOption);
        elementFontOptionProperty.columnWidth = '5';

        // element - fontColor
        const elementFontColorProperty = new ZColorPickerProperty('element.fontColor', this.elementFontColor, false)
            .setValidation(false, 'rgb', '', '', '', '25');
        elementFontColorProperty.columnWidth = '12';

        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            new ZGroupProperty('group.element')
                .addProperty(elementTextProperty)
                .addProperty(elementFontSizeProperty)
                .addProperty(elementAlignProperty)
                .addProperty(elementFontOptionProperty)
                .addProperty(elementFontColorProperty)
        ];
    },
    toJson() {
        return {
            id: this._id,
            type: this._type,
            display: this._display,
            isTopic: this._isTopic,
            mapId: this._mapId,
            tags: this._tags,
            value: this._value,
            label: this._label,
            element: this._element
        };
    }
};
