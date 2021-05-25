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
import { zValidation } from '../../../lib/validation.js';

const propertyExtends = {
    /* 추가적인 설정이 없다. */
};

export default class TextAreaProperty extends Property {
    constructor(name, value) {
        super(name, 'textAreaProperty', value);
    }
    // DOM Element 생성
    makeProperty(panel) {
        this.panel = panel;

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
            .onUIChange(this.updateProperty.bind(this));
        this.UIElement.addUI(this.UIElement.UITextArea);

        return this.UIElement;
    }
    // 속성 변경시 발생하는 이벤트 핸들러
    updateProperty(e) {
        e.stopPropagation();
        e.preventDefault();

        // change 일 경우 minLength, maxLength 체크
        if (e.type === 'change' && !zValidation.changeValidationCheck(e.target)) {
            this.panel.validationStatus = false; // 유효성 검증 실패
            return false;
        }
        this.panel.update.call(this, [e.target.id, e.target.value]);
    }
}