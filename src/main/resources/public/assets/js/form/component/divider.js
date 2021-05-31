/**
 * Divider Mixin
 *
 *
 * @author
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import { SESSION, FORM, CLASS_PREFIX } from '../../lib/constants.js';
import { zValidation } from '../../lib/validation.js';
import { UIDiv, UIText } from '../../lib/ui.js';
import InputBoxProperty from '../../formDesigner/property/type/inputBoxProperty.module.js';
import GroupProperty from '../../formDesigner/property/type/groupProperty.module.js';
import SliderProperty from '../../formDesigner/property/type/sliderProperty.module.js';
import CommonProperty from '../../formDesigner/property/type/commonProperty.module.js';
import DefaultValueSelectProperty from '../../formDesigner/property/type/defaultValueSelectProperty.module.js';
import DropdownProperty from '../../formDesigner/property/type/dropdownProperty.module.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        placeholder: '',
        columnWidth: '10',
        defaultValueSelect: 'input|', // input|사용자입력 / select|세션값
    },
    validation: {
        validationType: 'none', // none | char | num | numchar | email | phone
        minLength: '0',
        maxLength: '100'
    }
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const dividerMixin = {

    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this.element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.element);
        this.validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.validation);
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass(CLASS_PREFIX + 'element')
            .setUIProperty('--data-column', this.element.columnWidth);

        element.UIText = new UIText('아직 구현되지 않았습니다. 얼렁 개발해주세요.')
        element.addUI(element.UIText);
        return element;
    },
    // set, get

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

        this.setValue(e.target.value);
    },
    // 기본 값 변경
    setValue(value) {
        this.value = value;
    },
    // 기본 값 조회
    getValue() {
        if (this.value === '${default}') {
            // 직접입력일 경우 : none|입력값
            const defaultValues = this.element.defaultValueSelect.split('|');
            if (defaultValues[0] === 'input') {
                return defaultValues[1];
            } else {  // 자동일경우 : select|userKey
                return SESSION[defaultValues[1]] || '';
            }
        } else {
            return this.value;
        }
    },
    getProperty() {
        // validation - validation Type
        const validationTypeProperty = new DropdownProperty('validation.validationType', this.validation.validationType, [
            {name: 'form.properties.none', value: 'none'},
            {name: 'form.properties.char', value: 'char'},
            {name: 'form.properties.number', value: 'number'},
            {name: 'form.properties.email', value: 'email'},
            {name: 'form.properties.phone', value: 'phone'}
        ]);

        return [
            ...new CommonProperty(this).getCommonProperty(),
            new GroupProperty('group.element')
                .addProperty(new InputBoxProperty('element.placeholder', this.element.placeholder))
                .addProperty(new SliderProperty('element.columnWidth', this.element.columnWidth))
                .addProperty(new DefaultValueSelectProperty('element.defaultValueSelect', this.element.defaultValueSelect)),
            new GroupProperty('group.validation')
                .addProperty(validationTypeProperty)
                .addProperty(new InputBoxProperty('validation.minLength', this.validation.minLength))
                .addProperty(new InputBoxProperty('validation.maxLength', this.validation.maxLength))
        ];
    }
};