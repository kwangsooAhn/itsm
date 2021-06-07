/**
 * Dynamic Row Table Mixin
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
import InputBoxProperty from '../../formDesigner/property/type/inputBoxProperty.js';
import GroupProperty from '../../formDesigner/property/type/groupProperty.js';
import SliderProperty from '../../formDesigner/property/type/sliderProperty.js';
import CommonProperty from '../../formDesigner/property/type/commonProperty.js';
import DefaultValueSelectProperty from '../../formDesigner/property/type/defaultValueSelectProperty.js';
import DropdownProperty from '../../formDesigner/property/type/dropdownProperty.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        placeholder: '',
        columnWidth: '10',
        defaultValueSelect: 'input|', // input|사용자입력 / select|세션값
    }
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const dynamicRowTableMixin = {

    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass(CLASS_PREFIX + 'element')
            .setUIProperty('--data-column', this.elementColumnWidth);

        element.UIText = new UIText('아직 구현되지 않았습니다. 얼렁 개발해주세요.');
        element.addUI(element.UIText);
        return element;
    },
    // set, get
    set value(value) {
        this._value = value;
    },
    get value() {
        // this._value === '${default}' 일 경우, 신청서에서 변경되지 않은 값을 의미하므로 기본값을 표시한다.
        // 사용자 변경시 해당 값이 할당된다.
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
        return [];
    }
};