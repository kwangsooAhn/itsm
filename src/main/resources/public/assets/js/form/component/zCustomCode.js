/**
 * Custom Code Mixin
 *
 *
 * @author Woo Da Jung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZDefaultValueCustomCodeProperty from '../../formDesigner/property/type/zDefaultValueCustomCodeProperty.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZLabelProperty from '../../formDesigner/property/type/zLabelProperty.js';
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZSwitchProperty from '../../formDesigner/property/type/zSwitchProperty.js';
import { FORM } from '../../lib/zConstants.js';
import { ZSession } from '../../lib/zSession.js';
import {UIButton, UIDiv, UIInput, UIRemoveButton, UISpan} from '../../lib/zUI.js';
import { zValidation } from '../../lib/zValidation.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '10',
        defaultValueCustomCode: '' // 커스텀코드 기본값 (폼 디자이너에서 설정한 값)
    },
    validation: {
        required: false // 필수값 여부
    }
};

Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const customCodeMixin = {
    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
        this._validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.data.validation);
        this._value = (this.data.value === undefined) ? '${default}' : this.data.value;

        // customCode|none|없음  customCode|session|세션값  customCode|code|코드값|코드명
        if (zValidation.isEmpty(this._element.defaultValueCustomCode) && FORM.CUSTOM_CODE.length > 0) {
            this._element.defaultValueCustomCode = FORM.CUSTOM_CODE[0].customCodeId + '|' + FORM.CUSTOM.NONE + '|';
        }
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass('element').addUIClass('align-left')
            .setUIProperty('--data-column', this.elementColumnWidth);
        element.UIInputButton = new UIDiv()
            .setUIClass('custom-code')
            .addUIClass('flex-row').addUIClass('input-button')
            .setUIId('customcode' + this.id)
            .setUIAttribute('data-validation-required', this.validationRequired);
        element.UIInput = new UIInput()
            .setUIClass('input')
            .setUIReadOnly(true)
            .setUIAttribute('data-custom-data', (this.value === '${default}') ?
                this.elementDefaultValueCustomCode : this.value)
            .onUIChange(this.updateValue.bind(this))
            .setUIAttribute('data-validation-required', this.validationRequired);
        element.UIButton = new UIButton()
            .setUIClass('button')
            .setUIClass('btn--secondary')
            .addUIClass('btn__icon')
            .onUIClick(this.openCustomCodeModal.bind(this))
            .addUI(new UISpan().setUIClass('ic-search'));

        element.addUI(element.UIInputButton.addUI(element.UIInput).addUI(element.UIButton));

        // remove 버튼 생성 (단, customCode 값이 입력되면 display)
        element.UIRemoveButton = new UIRemoveButton()
            .addUIClass('item-remove')
            .onUIClick(this.removeValue.bind(this))
            .addUI(new UISpan().setUIClass('ic-remove'));
        element.addUI(
            element.UIInputButton.addUI(element.UIInput).addUI(element.UIRemoveButton).addUI(element.UIButton));

        this.setDefaultValue(element.UIInput);
        return element;
    },
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
        // 신청서 양식 편집 화면에 따른 처리
        if (this.displayType === FORM.DISPLAY_TYPE.READONLY) {
            this.UIElement.UIComponent.UIElement.UIButton.setUIDisabled(true);
            this.UIElement.UIComponent.UIElement.UIRemoveButton !== undefined ?
                this.UIElement.UIComponent.UIElement.UIRemoveButton.setUIDisabled(true) : '';
            // 필수값 표시가 된 대상에 대해 Required off 처리한다.
            this.UIElement.UIComponent.UILabel.UIRequiredText.hasUIClass('on') ?
                this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('on').addUIClass('none') : '';
        }
        // 문서의 상태가 사용이 아닌 경우 = 신청서 진행 중이고
        // 신청서 양식 편집 화면에서 처리한 group 컴포넌트가 숨김이 아니며
        // 기본값이 '${default}' 이면 실제 값을 저장한다.
        if (!zValidation.isEmpty(this.parent) && !zValidation.isEmpty(this.parent.parent) &&
            !zValidation.isEmpty(this.parent.parent.parent) && this.parent.parent.parent.status !== FORM.STATUS.EDIT &&
            this.displayType === FORM.DISPLAY_TYPE.EDITABLE && this.value === '${default}') {
            const defaultValues = this.elementDefaultValueCustomCode.split('|');
            switch (defaultValues[1]) {
                case FORM.CUSTOM.NONE:
                    this.value = '';
                    break;
                case FORM.CUSTOM.SESSION:
                    if (!zValidation.isEmpty(ZSession.get(defaultValues[2]))) {
                        aliceJs.fetchJson('/rest/custom-codes/' + defaultValues[0], {
                            method: 'GET'
                        }).then((response) => {
                            if (response.status === aliceJs.response.success && !zValidation.isEmpty(response.data)) {
                                const selectedCustomData = response.data.data.find((item) =>
                                    item.codeName === ZSession.get(defaultValues[2])
                                );
                                this.value = selectedCustomData.code;
                            }
                        });
                    } else {
                        this.value = '';
                    }
                    break;
                case FORM.CUSTOM.CODE:
                    this.value = defaultValues[2];
                    break;
            }
        }
        // '${default}' 에서 실제 값이 빈 값일 때에 remove 버튼을 제거한다.
        const removeButton = this.UIElement.UIComponent.UIElement.UIRemoveButton;
        removeButton.domElement.style.display = (this.value !== '') ? 'block' : 'none';
    },
    // set, get
    set elementColumnWidth(width) {
        this._element.columnWidth = width;
        this.UIElement.UIComponent.UIElement.setUIProperty('--data-column', width);
        this.UIElement.UIComponent.UILabel.setUIProperty('--data-column', this.getLabelColumnWidth(this.labelPosition));
    },
    get elementColumnWidth() {
        return this._element.columnWidth;
    },
    set elementDefaultValueCustomCode(value) {
        this._element.defaultValueCustomCode = value;
        this.UIElement.UIComponent.UIElement.UIInput.setUIAttribute('data-custom-data', value);
        this.setDefaultValue(this.UIElement.UIComponent.UIElement.UIInput);
    },
    get elementDefaultValueCustomCode() {
        return this._element.defaultValueCustomCode;
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
    // 기본값 조회
    setDefaultValue(target) {
        const defaultValues = this.elementDefaultValueCustomCode.split('|');
        if (this.value === '${default}') {
            switch (defaultValues[1]) {
                case FORM.CUSTOM.NONE:
                    target.setUIValue('');
                    break;
                case FORM.CUSTOM.SESSION:
                    target.setUIValue(ZSession.get(defaultValues[2]) || '');
                    break;
                case FORM.CUSTOM.CODE:
                    target.setUIValue(defaultValues[3]);
                    break;
            }
        } else if (!zValidation.isEmpty(this.value)) {
            aliceJs.fetchJson('/rest/custom-codes/' + defaultValues[0], {
                method: 'GET'
            }).then((response) => {
                if (response.status === aliceJs.response.success && !zValidation.isEmpty(response.data)) {
                    const selectedCustomData = response.data.data.find((item) => item.code === this.value);
                    target.setUIValue(selectedCustomData.codeName);
                } else {
                    target.setUIValue('');
                }
            });
        }
    },
    // input box 값 변경시 이벤트 핸들러
    updateValue(e) {
        e.stopPropagation();
        e.preventDefault();

        const customData = e.target.getAttribute('data-custom-data').split('|');

        // 값이 입력되었을 경우 remove button display
        const removeButton = this.UIElement.UIComponent.UIElement.UIRemoveButton;
        removeButton.domElement.style.display = (customData[2].trim() !== '') ? 'block' : 'none';

        // 값이 입력되었을 경우 error 없애기
        if (zValidation.isRequired(customData)) {
            zValidation.removeDOMElementError(e.target);
        }
        this.value = customData[2];
    },
    // remove 버튼 클릭시 custom data 제거
    removeValue() {
        let customData = this.elementDefaultValueCustomCode.split('|');
        customData[1] = 'none';
        customData[2] = '';
        const dataCustomData = `${customData[0]}|${customData[1]}|${customData[2]}`;
        this.UIElement.UIComponent.UIElement.UIInput.setUIAttribute('data-custom-data', dataCustomData);
        this.UIElement.UIComponent.UIElement.UIRemoveButton.domElement.style.display = 'none'
        this.value = customData[2];
    },
    // 커스텀 코드 모달
    openCustomCodeModal(e) {
        e.stopPropagation();
        let customCodeData = {
            componentId: this.id,
            componentValue: this.elementDefaultValueCustomCode
        };
        const storageName = 'alice_custom-codes-search-' + this.id;
        sessionStorage.setItem(storageName, JSON.stringify(customCodeData));

        const defaultValues = this.elementDefaultValueCustomCode.split('|');

        // 기존 커스텀 코드 선택
        let selectedRadioId = '';
        if (this.value === '${default}') {
            if (defaultValues[1] === FORM.CUSTOM.CODE) {
                selectedRadioId = defaultValues[2];
            }
        } else {
            selectedRadioId = this.value;
        }
        tree.load({
            view: 'modal',
            title: i18n.msg('form.label.customCodeTarget'),
            dataUrl: '/rest/custom-codes/' + defaultValues[0],
            target: 'treeList',
            text: 'codeName',
            rootAvailable: false,
            leafIcon: '/assets/media/icons/tree/icon_tree_leaf.svg',
            selectedValue: selectedRadioId,
            callbackFunc: (response) => {
                const customData = customCodeData.componentValue.split('|')[0] + '|code|' +
                    response.id + '|' + response.textContent;
                this.UIElement.UIComponent.UIElement.UIInput
                    .setUIAttribute('data-custom-data', customData)
                    .setUIValue(response.textContent);
                this.UIElement.UIComponent.UIElement.UIInput.domElement.dispatchEvent(new Event('change'));
            }
        });
    },
    // 세부 속성 조회
    getProperty() {
        const customCodeProperty = new ZDefaultValueCustomCodeProperty('elementDefaultValueCustomCode',
            'element.defaultValueCustomCode', this.elementDefaultValueCustomCode);
        customCodeProperty.help = 'form.help.custom-code';
        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZSliderProperty('elementColumnWidth', 'element.columnWidth', this.elementColumnWidth))
                .addProperty(customCodeProperty),
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
        return true;
    }
};
