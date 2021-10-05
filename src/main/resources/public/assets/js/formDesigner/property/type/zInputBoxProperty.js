/**
 * InputBox Property Class
 *
 * 속성의 값을 사용자가 직접 타이핑해서 입력하는 input box 형태의 속성 타입을 위한 클래스이다.
 *
 * @author Jung Hee Chan <hcjung@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import { UIDiv, UIInput } from '../../../lib/zUI.js';
import { zValidation } from '../../../lib/zValidation.js';
import ZProperty from '../zProperty.js';

const propertyExtends = {
    /* input box 속성 타입은 추가적인 설정이 없다. */
};

export default class ZInputBoxProperty extends ZProperty {
    constructor(key, name, value) {
        super(key, name, 'inputBoxProperty', value);
    }
    // DOM Element 생성
    makeProperty(panel) {
        this.panel = panel;

        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);
        // inputbox
        this.UIElement.UIInput = new UIInput()
            .setUIId(this.key)
            .setUIValue(this.value)
            .setUIAttribute('data-validation-required', this.validation.required)
            .setUIAttribute('data-validation-required-name', i18n.msg(this.name))
            .setUIAttribute('data-validation-type', this.validation.type)
            .setUIAttribute('data-validation-min', this.validation.min)
            .setUIAttribute('data-validation-max', this.validation.max)
            .setUIAttribute('data-validation-min-length', this.validation.minLength)
            .setUIAttribute('data-validation-max-length', this.validation.maxLength)
            .onUIKeyUp(this.updateProperty.bind(this))
            .onUIChange(this.updateProperty.bind(this));
        // 단위 추가
        if (this.unit !== '') {
            this.UIElement.UIInput.addUIClass('i-unit-' + this.unit);
        }
        this.UIElement.addUI(this.UIElement.UIInput);

        return this.UIElement;
    }

    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {}

    // 속성 변경시 발생하는 이벤트 핸들러
    updateProperty(e) {
        e.stopPropagation();
        e.preventDefault();

        this.panel.validationStatus = true;

        // enter, tab 입력시
        if (e.type === 'keyup' && (e.keyCode === 13 || e.keyCode === 9)) {
            return false;
        }
        // keyup 일 경우 type, min, max 체크
        if (e.type === 'keyup' && !zValidation.keyUpValidationCheck(e.target)) {
            this.panel.validationStatus = false; // 유효성 검증 실패
            return false;
        }
        // change 일 경우 minLength, maxLength 체크
        if (e.type === 'change' && !zValidation.changeValidationCheck(e.target)) {
            this.panel.validationStatus = false; // 유효성 검증 실패
            return false;
        }
        this.panel.update.call(this.panel, e.target.id, e.target.value);
    }
}