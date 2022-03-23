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
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZSwitchProperty from '../../formDesigner/property/type/zSwitchProperty.js';
import ZDefaultValueDropdownCodeProperty
    from '../../formDesigner/property/type/zDefaultValueDropdownCodeProperty.js';
import { FORM } from '../../lib/zConstants.js';
import { UIDiv, UISelect } from '../../lib/zUI.js';
import { zValidation } from '../../lib/zValidation.js';


/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '10',
        defaultValueDropdownCode: '{"code":"","defaultCode":""}',
    },
    validation: {
        required: false, // 필수값 여부
    }
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const dropdownCodeMixin = {
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
        // 초기값은 '선택하세요.' 이며, 발행/사용 중일 경우 즉 더이상 수정 불가능할때, 실제 서버 데이터를 조회한다.
        element.UIDropdown = new UISelect()
            .onUIChange(this.updateValue.bind(this));
        element.addUI(element.UIDropdown);

        return element;
    },
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
        // 옵션 초기화
        this.setDefaultOption(this.UIElement.UIComponent.UIElement.UIDropdown).then((rtn) => {
            // 신청서 양식 편집 화면에 따른 처리
            if (this.displayType === FORM.DISPLAY_TYPE.READONLY) {
                this.UIElement.UIComponent.UIElement.UIDropdown.addUIClass('readonly');
                // 필수값 표시가 된 대상에 대해 Required off 처리한다.
                this.UIElement.UIComponent.UILabel.UIRequiredText.hasUIClass('on') ?
                    this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('on').addUIClass('off') : '';
            }

            if (this.parent?.parent?.parent?.status !== FORM.STATUS.EDIT &&
                this.displayType === FORM.DISPLAY_TYPE.EDITABLE  && this.value === '') {
                const dropDown = this.UIElement.UIComponent.UIElement.UIDropdown.domElement;
                this.value = dropDown.options[dropDown.selectedIndex].value;
            }

            // Designed Select Box
            aliceJs.initDesignedSelectTag();
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
    // { code: 코드, defaultCode: 기본값 코드} or { mappingId: 매핑 아이디 }
    set elementDefaultValueDropdownCode(options) {
        this._element.defaultValueDropdownCode = options;
        this.setDefaultOption(this.UIElement.UIComponent.UIElement.UIDropdown).then((rtn) => {
            // Designed Select Box
            aliceJs.initDesignedSelectTag();
        });
    },
    get elementDefaultValueDropdownCode() {
        return this._element.defaultValueDropdownCode;
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
    // 선택된 값 변경 시 이벤트 핸들러
    updateValue(e) {
        e.stopPropagation();
        e.preventDefault();
        // TODO: 구현 예정
        //this.value = e.target.value;
    },
    // 기본값 조회
    async setDefaultOption(target) {
        const defaultDropdownCode = JSON.parse(this.elementDefaultValueDropdownCode);
        const isDropdownTypeCode = defaultDropdownCode.hasOwnProperty(FORM.DROPDOWN_CODE.CODE); // 코드인지?
        const defaultOptions = [{ name: i18n.msg('common.msg.select'), value: '', checked: true }];
        const defaultCodeValue = zValidation.isEmpty(this.value) && isDropdownTypeCode ?
            defaultDropdownCode[FORM.DROPDOWN_CODE.DEFAULT_CODE] : this.value;
        // 매핑ID 조회시 부모 코드로 사용할 데이터
        target.setUIAttribute('data-parent-code', defaultCodeValue);

        // 코드면서 코드가 존재하지 않을 경우 / 매핑ID 이면서 매핑ID가 존재하지 않을 경우 = 기본 값
        if ((isDropdownTypeCode && zValidation.isEmpty(defaultDropdownCode[FORM.DROPDOWN_CODE.CODE]))||
            (!isDropdownTypeCode && zValidation.isEmpty(defaultDropdownCode[FORM.DROPDOWN_CODE.MAPPING_ID]))) {
            target.setUIOptions(defaultOptions);
            return false;
        }

        let code = '';
        if (isDropdownTypeCode) {
            code = defaultDropdownCode[FORM.DROPDOWN_CODE.CODE];
        } else {
            // 매핑 아이디이면 매핑아이디가 일치하는 dropdownCode 컴포넌트의 선택된값을 가져와서 조회
            const matchComponent = document.querySelector('.dropdownCode[data-mappingid="' +
                defaultDropdownCode[FORM.DROPDOWN_CODE.MAPPING_ID] + '"]');
            if (matchComponent) {
                code = matchComponent.querySelector('select').getAttribute('data-parent-code');
            }
        }
        // 조회 대상이 비어있을 경우
        if (zValidation.isEmpty(code)) {
            target.setUIOptions(defaultOptions);
            return false;
        }
        
        // 조회 대상이 존재할 경우
        await aliceJs.fetchJson('/rest/codes/related/' + code, {
            method: 'GET'
        }).then((codeList) => {
            if (codeList.length > 0) {
                codeList.forEach((codeData) => {
                    defaultOptions.push({
                        name: codeData.codeName,
                        value: codeData.code,
                        checked: (codeData.code === defaultCodeValue)
                    });
                    if (codeData.code === defaultCodeValue) {
                        target.setUIValue(codeData.code);
                    }
                });
            }
            target.setUIOptions(defaultOptions);
        });
        return true;
    },
    // 세부 속성 조회
    getProperty() {
        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZSliderProperty('elementColumnWidth', 'element.columnWidth', this.elementColumnWidth))
                .addProperty(new ZDefaultValueDropdownCodeProperty('elementDefaultValueDropdownCode',
                    'element.defaultValueDropdownCode', JSON.parse(this.elementDefaultValueDropdownCode), false)
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
        const defaultDropdownCode = JSON.parse(this.elementDefaultValueDropdownCode);
        const isDropdownTypeCode = defaultDropdownCode.hasOwnProperty(FORM.DROPDOWN_CODE.CODE); // 코드인지?
        // 코드 or 매핑 아이디 미입력 확인
        if ((isDropdownTypeCode && zValidation.isEmpty(defaultDropdownCode[FORM.DROPDOWN_CODE.CODE]))||
            (!isDropdownTypeCode && zValidation.isEmpty(defaultDropdownCode[FORM.DROPDOWN_CODE.MAPPING_ID]))) {
            zAlert.warning(i18n.msg('common.msg.required', i18n.msg('form.component.dropdownCode') + '-' +
                i18n.msg('form.properties.element.defaultValueDropdownCode')));
            return false;
        }

        // 매핑아이디를 사용하는 경우, 일치하는 dropdownCode 컴포넌트가 존재하는지 체크
        if (!isDropdownTypeCode) {
            const matchComponent = document.querySelector('.dropdownCode[data-mappingid="' +
                defaultDropdownCode[FORM.DROPDOWN_CODE.MAPPING_ID] + '"]');
            if (!zValidation.isDOMElement(matchComponent)) {
                zAlert.warning(i18n.msg('form.msg.dropdownCodeNotExist'));
                return false;
            }
        }
        // 필수값인데 옵션이 하나도 존재하지 않을때 경고
        if (this.validationRequired && isDropdownTypeCode && this.UIElement.UIComponent.UIElement.UIDropdown.getUIOptionCount() < 2) {
            zAlert.warning(i18n.msg('form.msg.dropdownCodeOptionRequired'));
            return false;
        }

        return true;
    }
};
