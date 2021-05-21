/**
 * Textarea Property Class
 *
 * Textarea 형태의 입력항목을 가지는 속성 타입을 위한 클래스이다.
 *
 * @author Jung Hee Chan <hcjung@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import Property from '../property.module.js';
import { UIDiv, UITextArea } from '../../../lib/ui.js';

const propertyExtends = {
    /* 추가적인 설정이 없다. */
};

export default class TextAreaProperty extends Property {
    constructor(name, value) {
        super(name, 'textAreaProperty', value);
    }

    makeProperty(panel) {
        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);
        // textarea
        this.UIElement.UITextArea = new UITextArea()
            .setUIId(this.getKeyId())
            .addUIClass('textarea-scroll-wrapper')
            .setUIValue(this.value)
            .setUIAttribute('data-validation-required', this.validation.required)
            .setUIAttribute('data-validation-required-name', i18n.msg(this.name))
            .setUIAttribute('data-validation-minLength', this.validation.minLength)
            .setUIAttribute('data-validation-maxLength', this.validation.maxLength)
            .onUIChange(panel.updateProperty.bind(panel));
        this.UIElement.addUI(this.UIElement.UITextArea);

        return this.UIElement;
    }
}