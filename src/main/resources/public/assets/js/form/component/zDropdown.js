/**
 * Dropdown Mixin
 *
 * HTML 요소중 select box 형태의 컴포넌트를 제공한다.
 *
 * @author Jung Hee Chan (hcjung@brainz.co.kr)
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import { CLASS_PREFIX, FORM } from '../../lib/zConstants.js';
import { UIDiv, UISelect } from '../../lib/zUI.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZOptionListProperty from "../../formDesigner/property/type/zOptionListProperty.js";

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '10',
        options: [ FORM.DEFAULT_OPTION_ROW ]
    }
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const dropdownMixin = {

    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass(CLASS_PREFIX + 'element')
            .setUIProperty('--data-column', this.elementColumnWidth);

        element.UIDropdown = new UISelect().setUIOptions(this.element.options)
        element.addUI(element.UIDropdown);
        return element;
    },
    // set, get
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
    set elementOptions(options) {
        this._element.options = options;
        this.UIElement.UIComponent.UIElement.UIDropdown.setUIOptions(options)

    },
    get elementOptions() {
        return this._element.options;
    },
    set value(value) {
        this._value = value;
    },
    get value() {
        return this._value;
    },
    // 선택된 값 변경 시 이벤트 핸들러
    updateValue(e) {
        e.stopPropagation();
        e.preventDefault();
        this.value = e.target.value;
    },
    getProperty() {
        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZSliderProperty('element.columnWidth', this.elementColumnWidth))
                .addProperty(new ZOptionListProperty('element.options', this.elementOptions))
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