/**
 * Box Model Property Class
 *
 * 기본적으로는 input type 과 동일한 속성 입력을 지원하지만 (그런면에서 input 타입과 유사하지만)
 * 상하좌우와 같이 복수개의 데이터를 한 묶음으로 처리하는 클래스이다.
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
    /* 추가적인 설정이 없다. */
};

export default class BoxModelProperty extends Property {
    constructor(name, value) {
        super(name, 'boxModelProperty', value);
    }

    makeProperty(panel) {
        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);

        this.UIElement.UIBox = new UIDiv().setUIClass('box-model');
        this.UIElement.addUI(this.UIElement.UIBox);

        // inputbox : top, right, bottom, left 박스 모델
        const boxValueArray = this.value.split(' ');
        ['Top', 'Left', 'Bottom', 'Right'].forEach((item, index) => {
            this.UIElement.UIBox['UIInput' + item] = new UIInput()
                .setUIId(this.getKeyId() + item)
                .setUIValue(boxValueArray[index])
                .setUIAttribute('data-validation-required', this.validation.required)
                .setUIAttribute('data-validation-required-name', i18n.msg(this.name))
                .setUIAttribute('data-validation-type', this.validation.type)
                .setUIAttribute('data-validation-min', this.validation.min)
                .setUIAttribute('data-validation-max', this.validation.max)
                .setUIAttribute('data-validation-minLength', this.validation.minLength)
                .setUIAttribute('data-validation-maxLength', this.validation.maxLength)
                .onUIKeyUp(panel.updateProperty.bind(panel))
                .onUIChange(panel.updateProperty.bind(panel));
            // 단위 추가
            if (this.unit !== '') {
                this.UIElement.UIBox['UIInput' + item].addUIClass('icon-unit-' + this.unit);
            }
            this.UIElement.UIBox.addUI(this.UIElement.UIBox['UIInput' + item]);
        });
        return this.UIElement;
    }
}