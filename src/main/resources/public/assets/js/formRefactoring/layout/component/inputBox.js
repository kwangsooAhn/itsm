/**
 * Inputbox Class
 *
 * 사용자 입력을 받는 input box.
 * 정규식을 이용하여 숫자, 문자, 전화번호, 이메일 등 제어가 가능하다.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

import { SESSION, FORM }  from '../../lib/constant.js';
const DEFAULT = {
    type: 'inputBox',
    value: '${default}',
    columnWidth: '12',
    displayType: 'editable', // 컴포넌트 출력 타입 (readonly, editable, required, hidden)
    mapId: '', // 매핑 ID
    isTopic: false,
    tags: [],
    label: {
        position: 'left',
        fontSize: '16px',
        fontColor: 'rgba(0,0,0,1)',
        bold: false,
        italic: false,
        underline: false,
        align: 'left',
        text: 'COMPONENT LABEL'
    },
    element: {
        placeholder: '',
        columnWidth: '10',
        defaultType: 'none|',
    },
    validate: {
        validateType: 'none', // none | char | num | numchar | email | phone
        lengthMin: '0',
        lengthMax: '100'
    }
};

export const inputBoxMixin = {
    // DOM 엘리먼트 생성
    makeElement() {
        //  엘리먼트 그룹
        const elementBox = document.createElement('div');
        elementBox.className = 'component-element-box';
        elementBox.style.cssText = `--data-column:${this.element.columnWidth};`;

        // inputbox
        const element = document.createElement('input');
        element.type = 'text';
        element.className = 'component-element';
        element.placeholder = this.element.placeholder;
        element.required = (this.displayType === FORM.DISPLAY_TYPE.REQUIRED);
        element.value = this.getValue();

        // 유효성 추가
        element.setAttribute('data-validate-maxLength', this.validate.lengthMax);
        element.setAttribute('data-validate-minLength', this.validate.lengthMin);
        element.setAttribute('data-validate-regexpType', this.validate.validateType);

        elementBox.appendChild(element);
        return elementBox;
    },
    // 기본 값 조회
    getValue() {
        if (this.value === '${default}') {
            // 직접입력일 경우 : none|입력값
            const defaultValues = this.element.defaultType.split('|');
            if (defaultValues[0] === 'none') {
                return defaultValues[1];
            } else {  // 자동일경우 : select|userKey
                return SESSION[defaultValues[1]] || '';
            }
        } else {
            return this.value;
        }
    }
};