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

import { SESSION, FORM, CLASS_PREFIX } from '../../lib/constants.js';
import * as util from '../../lib/util.js';
import { UIDiv, UIInput } from '../../lib/ui.js';

export const inputBoxMixin = {
    // property 초기화
    setProperty() {
        // 엘리먼트 property 초기화
        util.mergeObject(this.element || FORM.DEFAULT.INPUTBOX.ELEMENT);
        util.mergeObject(this.validate || FORM.DEFAULT.INPUTBOX.VALIDATE);
    },
    // field 엘리먼트 생성
    makeField() {
        // field > label + element
        const field = new UIDiv()
            .setClass(CLASS_PREFIX + 'field flex-row align-items-center flex-wrap');
        // label
        field.UILabel = this.makeLabel();
        field.add(field.UILabel);
        // element
        const element = new UIDiv().setClass(CLASS_PREFIX + 'element')
            .setProperty('--data-column', this.element.columnWidth);
        element.UIInputbox = new UIInput().setPlaceholder(this.element.placeholder)
            .setRequired((this.displayType === FORM.DISPLAY_TYPE.REQUIRED))
            .setValue(this.getValue())
            .setAttribute('data-validate-type', this.validate.validateType)
            .setAttribute('data-validate-maxLength', this.validate.lengthMax)
            .setAttribute('data-validate-minLength', this.validate.lengthMin);
        element.add(element.UIInputbox);

        field.UIElement = element;
        field.add(field.UIElement);

        return field;
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