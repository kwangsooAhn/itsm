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
import Property from '../property.module.js';
import { UIDiv, UIInput } from '../../../lib/ui.js';

const propertyExtends = {
    /* input box 속성 타입은 추가적인 설정이 없다. */
};

export default class InputBoxProperty extends Property {
    constructor(name, value) {
        super(name, 'inputBoxProperty', value);
    }

    makeProperty(panel) {
        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);
        // inputbox
        this.UIElement.UIInput = new UIInput()
            .setUIId(this.getKeyId())
            .setUIValue(this.value)
            .setUIAttribute('data-validation-required', this.validation.required)
            .setUIAttribute('data-validation-required-name', i18n.msg(this.name))
            .setUIAttribute('data-validation-type', this.type)
            .setUIAttribute('data-validation-min', this.validation.min)
            .setUIAttribute('data-validation-max', this.validation.max)
            .setUIAttribute('data-validation-minLength', this.validation.minLength)
            .setUIAttribute('data-validation-maxLength', this.validation.maxLength)
            .onUIKeyUp(panel.updateProperty.bind(panel))
            .onUIChange(panel.updateProperty.bind(panel));
        // 단위 추가
        if (this.unit !== '') {
            this.UIElement.UIInput.addUIClass('icon-unit-' + this.unit);
        }
        this.UIElement.addUI(this.UIElement.UIInput);

        return this.UIElement;
    }
}