/**
 * Text Editor Mixin
 *
 *
 * @author
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import { FORM, CLASS_PREFIX } from '../../lib/zConstants.js';
import { zValidation } from '../../lib/zValidation.js';
import { UIDiv } from '../../lib/zUI.js';
import ZInputBoxProperty from '../../formDesigner/property/type/zInputBoxProperty.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZSwitchProperty from '../../formDesigner/property/type/zSwitchProperty.js';
import ZLabelProperty from '../../formDesigner/property/type/zLabelProperty.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '12',
        rows: '3',
        placeholder: ''
    },
    validation: {
        required: false, // 필수값 여부
        minLength: '0',
        maxLength: '512'
    }
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const textEditorMixin = {

    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
        this._validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.data.validation);
        this._value = this.data.value || '';
    },
    // component 엘리먼트 생성
    makeElement() {
        // label 숨김 처리
        this.labelPosition = FORM.LABEL.POSITION.HIDDEN;

        const element = new UIDiv().setUIClass(CLASS_PREFIX + 'element')
            .addUIClass('align-left')
            .setUIProperty('--data-column', this.elementColumnWidth);

        element.UIDiv = new UIDiv().setUIClass(CLASS_PREFIX + 'text-editor').setUIId('editorContainer')
            .setUIProperty('--data-row', this.elementRows)
            .setUIAttribute('data-validation-required', this.validationRequired);
        element.addUI(element.UIDiv);

        if (this._value !== '${default}') {
            this.value = JSON.parse(this.value);
            this.editor.setContents(this.value);
        }
        return element;
    },
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
        this.editor = new Quill(this.UIElement.UIComponent.UIElement.UIDiv.domElement, {
            modules: {
                toolbar: [
                    [{'header': [1, 2, 3, 4, false]}],
                    ['bold', 'italic', 'underline'],
                    [{'color': []}, {'background': []}],
                    [{'align': []}, { 'list': 'bullet' }],
                    ['image']
                ]
            },
            placeholder: this.elementPlaceholder,
            theme: 'snow',
            readOnly: false
        });

        this.editor.on('text-change', (delta, oldDelta, source) => {
            if (source === 'user') {
                setTimeout(() => {
                    // 유효성 검증 - min length
                    let isValidationPass = true;
                    if (!zValidation.isEmpty(this.validationMinLength)) {
                        isValidationPass = this.editor.getLength() > Number(this.validationMinLength);
                        zValidation.setDOMElementError(isValidationPass, this.editor.container,
                            i18n.msg('validation.msg.minLength', this.validationMinLength), () => {
                                this.editor.focus();
                            });
                    }
                    // 유효성 검증 - max length
                    if (isValidationPass && !zValidation.isEmpty(this.validationMaxLength)) {
                        isValidationPass = this.editor.getLength() < Number(this.validationMaxLength) + 1;
                        zValidation.setDOMElementError(isValidationPass, this.editor.container,
                            i18n.msg('validation.msg.maxLength', this.validationMaxLength), () => {
                                this.editor.focus();
                            });
                    }
                    if (isValidationPass) {
                        this.value = this.editor.getContents();
                    }
                }, 100);
            }
        });
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
        this.UIElement.UIComponent.UIElement.UIDiv.setUIProperty('--data-row', rows);
    },
    get elementRows() {
        return this._element.rows;
    },
    set elementPlaceholder(placeholder) {
        this._element.placeholder = placeholder;
        this.editor.root.dataset.placeholder = placeholder;
    },
    get elementPlaceholder() {
        return this._element.placeholder;
    },
    set validation(validation) {
        this._validation = validation;
    },
    get validation() {
        return this._validation;
    },
    set validationRequired(boolean) {
        this._validation.required = boolean;
        this.UIElement.UIComponent.UIElement.UIDiv.setUIAttribute('data-validation-required', boolean);
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
    },
    get validationMinLength() {
        return this._validation.minLength;
    },
    set validationMaxLength(max) {
        this._validation.maxLength = max;
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
    getProperty() {
        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZSliderProperty('element.columnWidth', this.elementColumnWidth))
                .addProperty(new ZInputBoxProperty('element.rows', this.elementRows))
                .addProperty(new ZInputBoxProperty('element.placeholder', this.elementPlaceholder)),
            new ZGroupProperty('group.validation')
                .addProperty(new ZSwitchProperty('validation.required', this.validationRequired))
                .addProperty(new ZInputBoxProperty('validation.minLength', this.validationMinLength))
                .addProperty(new ZInputBoxProperty('validation.maxLength', this.validationMaxLength))
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
    }
};