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
import { UIButton, UIDiv, UIInput, UISpan } from '../../lib/zUI.js';
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
        this._value = this.data.value || '${default}'; // 서버에 저장되는 값은 키 값만 저장된다. 왜냐하면 assignee Id 로 key만 전달되어야 하기 떄문이다.

        // customCode|none|없음  customCode|session|세션값  customCode|code|코드값|코드명
        if (zValidation.isEmpty(this._element.defaultValueCustomCode) && FORM.CUSTOM_CODE.length > 0) {
            this._element.defaultValueCustomCode = FORM.CUSTOM_CODE[0].customCodeId + '|' + FORM.CUSTOM.NONE + '|';
        }
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass('z-element').addUIClass('align-left')
            .setUIProperty('--data-column', this.elementColumnWidth);
        element.UIInputButton = new UIDiv()
            .setUIClass('z-custom-code')
            .addUIClass('flex-row')
            .setUIId('customcode' + this.id)
            .setUIAttribute('data-validation-required', this.validationRequired);
        element.UIInput = new UIInput()
            .setUIClass('z-input z-input-button')
            .setUIReadOnly(true)
            .setUIAttribute('data-custom-data', (this.value === '${default}') ? this.elementDefaultValueCustomCode : this.value)
            .onUIChange(this.updateValue.bind(this))
            .setUIAttribute('data-validation-required', this.validationRequired);
        element.UIButton = new UIButton()
            .setUIClass('z-button')
            .setUIClass('secondary')
            .addUIClass('z-button-icon')
            .onUIClick(this.openCustomCodeModal.bind(this))
            .addUI(new UISpan().setUIClass('z-icon').addUIClass('i-search'));

        element.addUI(element.UIInputButton.addUI(element.UIInput).addUI(element.UIButton));

        this.setDefaultValue(element.UIInput);
        return element;
    },
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
        // 신청서 양식 편집 화면에 따른 처리
        if (this.displayType === FORM.DISPLAY_TYPE.READONLY) {
            this.UIElement.UIComponent.UIElement.UIButton.setUIDisabled(true);
        }
        // 문서의 상태가 사용이 아닌 경우 = 신청서 진행 중이고
        // 신청서 양식 편집 화면에서 처리한 group 컴포넌트가 숨김이 아니며
        // 기본값이 '${default}' 이면 실제 값을 저장한다.
        if (this.parent?.parent?.parent?.status !== FORM.STATUS.EDIT &&
            this.displayType !== FORM.DISPLAY_TYPE.HIDDEN &&
            this.value === '${default}') {
            const defaultValues = this.elementDefaultValueCustomCode.split('|');
            switch (defaultValues[1]) {
                case FORM.CUSTOM.NONE:
                    this.value = '';
                    break;
                case FORM.CUSTOM.SESSION:
                    if (!zValidation.isEmpty(ZSession.get(defaultValues[2]))) {
                        aliceJs.fetchJson('/rest/custom-codes/' + defaultValues[0], {
                            method: 'GET'
                        }).then((customCodeData) => {
                            if (!zValidation.isEmpty(customCodeData)) {
                                const selectedCustomData = customCodeData.find((item) =>
                                    item.value === ZSession.get(defaultValues[2])
                                );
                                this.value = selectedCustomData.key;
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
            this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('off').addUIClass('on');
        } else {
            this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('on').addUIClass('off');
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
            }).then((customCodeData) => {
                if (!zValidation.isEmpty(customCodeData)) {
                    const selectedCustomData = customCodeData.find((item) => item.key === this.value);
                    target.setUIValue(zValidation.isEmpty(selectedCustomData.name) ? selectedCustomData.value :
                        selectedCustomData.name);
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

        // 값이 입력되었을 경우 error 없애기
        if (zValidation.isRequired(customData)) {
            zValidation.removeDOMElementError(e.target);
        }
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
        const selectModal = new modal({
            title: i18n.msg('form.label.customCodeTarget'),
            body: this.getSelectModalContent(),
            classes: 'custom-code-list',
            buttons: [{
                content: i18n.msg('common.btn.add'),
                classes: 'z-button primary',
                bindKey: false,
                callback: () => {
                    let isChekced = false;
                    document.getElementsByName('customCode').forEach((chkElem) => {
                        if (chkElem.checked) {
                            isChekced = true;
                            const customData = customCodeData.componentValue.split('|')[0] + '|code|' + chkElem.id + '|' + chkElem.value;
                            const labelText = chkElem.parentNode.querySelector('.label').textContent;
                            this.UIElement.UIComponent.UIElement.UIInput.setUIAttribute('data-custom-data', customData).setUIValue(labelText);
                            this.UIElement.UIComponent.UIElement.UIInput.domElement.dispatchEvent(new Event('change'));
                        }
                    });
                    isChekced ? selectModal.hide() : zAlert.warning(i18n.msg('common.msg.dataSelect', i18n.msg('common.label.data')));
                }
            }, {
                content: i18n.msg('common.btn.cancel'),
                classes: 'z-button secondary',
                bindKey: false,
                callback: (modal) => {
                    modal.hide();
                }
            }],
            close: {
                closable: false,
            },
            onCreate: () => {
                this.getCustomCode();
                document.getElementById('search').addEventListener('keyup', (e) => {
                    e.preventDefault();
                    this.searchCustomCode();
                });
                OverlayScrollbars(document.querySelectorAll('.modal-content'), {className: 'scrollbar'});
            }
        });
        selectModal.show();
    },

    getCustomCode() { // 서버에서 데이터 가져오기
        const defaultValues = this.elementDefaultValueCustomCode.split('|');
        let url = '/custom-codes/' + defaultValues[0] + '/search';
        aliceJs.fetchText(url, {
            method: 'GET',
            showProgressbar: true
        }).then((htmlData) => {
            document.getElementById('customCodeList').innerHTML = htmlData;
            // 기존 커스텀 코드 선택
            let selectedRadioId = '';
            if (this.value === '${default}') {
                if (defaultValues[1] === FORM.CUSTOM.CODE) {
                    selectedRadioId = defaultValues[2];
                }
            } else {
                selectedRadioId = this.value;
            }

            // 세션 값은 selectedRadioId를 받아와도, 해당 값이 커스텀 코드 목록 내에 없을 수 있다.
            // 따라서 selectedRadioId가 아닌 해당 아이디를 가진 dom 항목이 있을 경우 checked 되도록 해야한다.
            if (document.getElementById(selectedRadioId)) {
                document.getElementById(selectedRadioId).checked = true;
            }
            OverlayScrollbars(document.querySelector('.modal-content'), { className: 'scrollbar' });
        });
    },
    // 서버에서 가져온 데이터에서 검색하기
    getSelectModalContent() {
        return `<div class="flex-column view-row">` +
            `<div class="flex-row justify-content-start input-search">` +
            `<input class="z-input i-search col-5 " type="text" id="search" placeholder="${i18n.msg('customCode.msg.enterSearchTerm')}">` +
            `<span id="ciListTotalCount" class="z-search-count"></span>` +
            `</div>` +
            `</div>` +
            `<div class="custom-code-main" id="customCodeList"></div>`;
    },
    // 서버에서 가져온 데이터내에서 검색
    searchCustomCode() {
        let searchValue = document.getElementById('search').value;
        let customCodeList = document.getElementsByName('custom-code-list');

        for (let i = 0; i < customCodeList.length; i++) {
            let code = customCodeList[i].getElementsByClassName('label');
            if (code[0].innerHTML.indexOf(searchValue) != -1) {
                customCodeList[i].style.display = '';
            } else {
                customCodeList[i].style.display = 'none';
            }
        }
    },
    // 세부 속성 조회
    getProperty() {
        const customCodeProperty = new ZDefaultValueCustomCodeProperty('elementDefaultValueCustomCode', 'element.DefaultValueCustomCode',
            this.elementDefaultValueCustomCode);
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
