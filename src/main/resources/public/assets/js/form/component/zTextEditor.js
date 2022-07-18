/**
 * Text Editor Mixin
 *
 *
 * @author woodajung <wdj@brainz.co.kr>
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
import ZTextAreaProperty from '../../formDesigner/property/type/zTextAreaProperty.js';
import { FORM } from '../../lib/zConstants.js';
import { UIDiv } from '../../lib/zUI.js';
import { zValidation } from '../../lib/zValidation.js';

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
        const element = new UIDiv().setUIClass('element')
            .addUIClass('align-left')
            .setUIProperty('--data-column', this.elementColumnWidth);

        element.UIDiv = new UIDiv().setUIClass('z-text-editor').setUIId('editorContainer')
            .setUIAttribute('data-validation-required', this.validationRequired);
        element.addUI(element.UIDiv);

        if (this._value !== '' && typeof this._value === 'string') {
            this.value = JSON.parse(this.value);
        }
        return element;
    },
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
        // 필수값 표시가 된 대상에 대해 Required off 처리한다.
        if (this.displayType === FORM.DISPLAY_TYPE.READONLY) {
            this.UIElement.UIComponent.UILabel.UIRequiredText.hasUIClass('on') ?
                this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('on').addUIClass('off') : '';
        }
        this.editor = new zQuill(this.UIElement.UIComponent.UIElement.UIDiv.domElement, {
            placeholder: this.elementPlaceholder,
            readOnly: (this.displayType === FORM.DISPLAY_TYPE.READONLY),
            content: (this.value !== '') ? this.value : ''
        });
        this.editor.root.style.setProperty('--data-row', this.elementRows);
        this.editor.on('text-change', (delta, oldDelta, source) => {
            if (source === 'user') {
                setTimeout(() => {
                    // 유효성 검증 - min length
                    let isValidationPass = true;
                    if (!zValidation.isEmpty(this.validationMinLength)) {
                        // quill editor에서 space를 기본값으로 처리하므로 아무 입력이 없을시 길이가 1이 나온다.
                        isValidationPass = this.editor.getLength() > Number(this.validationMinLength);
                        zValidation.setDOMElementError(isValidationPass, this.editor.container,
                            i18n.msg('validation.msg.minLength', this.validationMinLength), () => {
                                this.editor.focus();
                            });
                    }
                    // 유효성 검증 - max length
                    if (isValidationPass && !zValidation.isEmpty(this.validationMaxLength)) {
                        isValidationPass = this.editor.getLength() <= Number(this.validationMaxLength) + 1;
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
        this.editor.root.style.setProperty('--data-row', rows);
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
    // 세부 속성 조회
    getProperty() {
        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZSliderProperty('elementColumnWidth', 'element.columnWidth', this.elementColumnWidth))
                .addProperty(new ZInputBoxProperty('elementRows', 'element.rows', this.elementRows))
                .addProperty(new ZTextAreaProperty('elementPlaceholder', 'element.placeholder', this.elementPlaceholder, true)),
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
