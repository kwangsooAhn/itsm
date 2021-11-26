/**
 * TextArea Mixin
 *
 *
 * @author phc <phc@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZInputBoxProperty from '../../formDesigner/property/type/zInputBoxProperty.js';
import ZLabelProperty from '../../formDesigner/property/type/zLabelProperty.js';
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZSwitchProperty from '../../formDesigner/property/type/zSwitchProperty.js';
import { FORM } from '../../lib/zConstants.js';
import { UIDiv, UITextArea } from '../../lib/zUI.js';
import { zValidation } from '../../lib/zValidation.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '12',
        rows: '2',
        placeholder: ''
    },
    validation: {
        required: false,
        minLength: '0',
        maxLength: '512'
    }
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const textAreaMixin = {

    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
        this._validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.data.validation);
        this._value = this.data.value || '';
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass('z-element')
            .setUIProperty('--data-column', this.elementColumnWidth);

        element.UITextArea = new UITextArea().addUIClass('textarea-scroll-wrapper')
            .addUIClass('align-left')
            .setUIPlaceholder(this.elementPlaceholder)
            .setUIProperty('--data-row', this.elementRows)
            .setUIAttribute('data-validation-required', this.validationRequired)
            .setUIValue(this.value)
            .onUIKeyUp(this.updateValue.bind(this))
            .onUIChange(this.updateValue.bind(this));
        element.addUI(element.UITextArea);
        return element;
    },
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
        // 신청서 양식 편집 화면에 따른 처리
        if (this.displayType === FORM.DISPLAY_TYPE.READONLY) {
            this.UIElement.UIComponent.UIElement.UITextArea.setUIReadOnly(true);
        }
        // 스크롤바 추가
        OverlayScrollbars(this.UIElement.UIComponent.UIElement.UITextArea.domElement, {
            className: 'scrollbar',
            resize: 'vertical',
            sizeAutoCapable: true,
            textarea: {
                dynHeight: false,
                dynWidth: false,
                inheritedAttrs: ['class', 'style']
            }
        });

        if (document.querySelector('textarea').hasAttribute("readonly") === true) {
            document.querySelectorAll('div.z-textarea').forEach(function (elem) {
                elem.classList.add('z-textarea-readonly');
            });
        } else if (document.querySelector('textarea').hasAttribute("disabled") === true) {
            document.querySelectorAll('div.z-textarea').forEach(function (elem) {
                elem.classList.add('z-textarea-disabled');
            });
        }
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
    set elementRows(rows) {
        this._element.rows = rows;
        const UIElement = this.UIElement.UIComponent.UIElement;
        UIElement.domElement.firstChild.style.setProperty('--data-row', rows);
        UIElement.UITextArea.setUIProperty('--data-row', rows);
    },
    get elementRows() {
        return this._element.rows;
    },
    set elementPlaceholder(placeholder) {
        this._element.placeholder = placeholder;
        this.UIElement.UIComponent.UIElement.UITextArea.setUIPlaceholder(placeholder);
    },
    get elementPlaceholder() {
        return this._element.placeholder;
    },
    set validation(validation) {
        return this._validation = validation;
    },
    get validation() {
        return this._validation;
    },
    set validationRequired(boolean) {
        this._validation.required = boolean;
        this.UIElement.UIComponent.UIElement.UITextArea.setUIAttribute('data-validation-required', boolean);
        if (boolean) {
            this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('off').addUIClass('on');
        } else {
            this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('on').addUIClass('off');
        }
    },
    get validationRequired() {
        return this._validation.required;
    },
    set validationMinLength(min) {
        this._validation.minLength = min;
        this.UIElement.UIComponent.UIElement.UITextArea.setUIAttribute('data-validation-min-length', min);
    },
    get validationMinLength() {
        return this._validation.minLength;
    },
    set validationMaxLength(max) {
        this._validation.maxLength = max;
        this.UIElement.UIComponent.UIElement.UITextArea.setUIAttribute('data-validation-max-length', max);
    },
    get validationMaxLength() {
        return this._validation.maxLength;
    },
    set value(value) {
        this._value = value;
    },
    get value() {
        return this._value;
    },
    // 값 변경시 이벤트 핸들러
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
    // 세부 속성 조회
    getProperty() {
        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZSliderProperty('elementColumnWidth', 'element.columnWidth', this.elementColumnWidth))
                .addProperty(new ZInputBoxProperty('elementRows', 'element.rows', this.elementRows))
                .addProperty(new ZInputBoxProperty('elementPlaceholder', 'element.placeholder', this.elementPlaceholder)),
            new ZGroupProperty('group.validation')
                .addProperty(new ZSwitchProperty('validationRequired', 'validation.required', this.validationRequired))
                .addProperty(new ZInputBoxProperty('validationMinLength', 'validation.minLength', this.validationMinLength))
                .addProperty(new ZInputBoxProperty('validationMaxLength', 'validation.maxLength', this.validationMaxLength))
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
        return true;
    }
};
