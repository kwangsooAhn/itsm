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

import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZLabelProperty from '../../formDesigner/property/type/zLabelProperty.js';
import ZOptionListProperty from '../../formDesigner/property/type/zOptionListProperty.js';
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZSwitchProperty from '../../formDesigner/property/type/zSwitchProperty.js';
import { FORM } from '../../lib/zConstants.js';
import { UIDiv, UISelect } from '../../lib/zUI.js';
import { zValidation } from '../../lib/zValidation.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '10',
        options: [ FORM.DEFAULT_OPTION_ROW ]
    },
    validation: {
        required: false, // 필수값 여부
    }
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const dropdownMixin = {

    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
        this._validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.data.validation);
        this._value = this.data.value || '';
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass('element')
            .setUIProperty('--data-column', this.elementColumnWidth);

        element.UIDropdown = new UISelect()
            .addUIClass('align-left')
            .setUIOptions(this.element.options)
            .onUIChange(this.updateValue.bind(this));
        element.addUI(element.UIDropdown);

        if (this._value !== '') {
            element.UIDropdown.setUIValue(this._value);
        }
        return element;
    },
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
        // 신청서 양식 편집 화면에 따른 처리
        if (this.displayType === FORM.DISPLAY_TYPE.READONLY) {
            this.UIElement.UIComponent.UIElement.UIDropdown.addUIClass('readonly');
            // 필수값 표시가 된 대상에 대해 Required off 처리한다.
            this.UIElement.UIComponent.UILabel.UIRequiredText.hasUIClass('on') ?
                this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('on').addUIClass('none') : '';

        }

        if (!zValidation.isEmpty(this.parent) && !zValidation.isEmpty(this.parent.parent) &&
            !zValidation.isEmpty(this.parent.parent.parent) && this.parent.parent.parent.status !== FORM.STATUS.EDIT &&
            this.displayType === FORM.DISPLAY_TYPE.EDITABLE  && this.value === '') {
            let checkedYn = false;
            for (let i = 0; i < this.element.options.length; i++) {
                checkedYn = this.element.options[i].checked || false;
                if (checkedYn) {
                    this.value = this.element.options[i].value;
                }
            }
        }
        // Designed Select Box
        aliceJs.initDesignedSelectTag();
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
        this.UIElement.UIComponent.UIElement.UIDropdown.setUIOptions(options);
    },
    get elementOptions() {
        return this._element.options;
    },
    set validation(validation) {
        this._validation = validation;
    },
    get validation() {
        return this._validation;
    },
    set validationRequired(boolean) {
        this._validation.required = boolean;
        if (boolean) {
            this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('none').addUIClass('on');
        } else {
            this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('on').addUIClass('none');
        }
    },
    get validationRequired() {
        return this._validation.required;
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
    // 세부 속성 조회
    getProperty() {
        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZSliderProperty('elementColumnWidth', 'element.columnWidth', this.elementColumnWidth))
                .addProperty(new ZOptionListProperty('elementOptions', 'element.options', this.elementOptions, false)
                    .setValidation(true, '', '', '', '', '')),
            new ZGroupProperty('group.validation')
                .addProperty(new ZSwitchProperty('validationRequired', 'validation.required', this.validationRequired))
        ];
    },
    // json 데이터 추출 (서버에 전달되는 json 데이터)
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
            element: this._element,
            validation: this._validation
        };
    },
    // 발행을 위한 validation 체크
    validationCheckOnPublish() {
        return !zValidation.isEmptyOptions(this.element.options);
    }
};
