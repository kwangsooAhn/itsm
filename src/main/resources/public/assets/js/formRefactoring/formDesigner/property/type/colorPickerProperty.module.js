/**
 * ColorPicker Property Class
 *
 * 색상을 선택하는 속성항목을 위한 클래스이다.
 *
 * @author Jung Hee Chan <hcjung@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import Property from '../property.module.js';
import {UIColor, UIDiv} from '../../../lib/ui.js';

const propertyExtends = {
    /* 추가적인 설정이 없다. */
};

export default class ColorPickerProperty extends Property {
    constructor(name, value, isOpacityUsed = true) {
        super(name, 'colorPickerProperty', value);

        this.isOpacityUsed = isOpacityUsed;
    }

    makeProperty(panel) {
        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);

        // color picker
        const colorPickerOption = {
            isOpacity: (this.isOpacityUsed), // 불투명도 사용시
            data: {
                isSelected: true, // 기존 색상 선택 여부
                selectedClass: 'selected', // 기존 값 색상에 css 적용 (테두리)
                value: this.value // 기존 값
            }
        };
        this.UIElement.UIColorPicker = new UIColor(colorPickerOption).setUIId(this.getKeyId());
        this.UIElement.UIColorPicker.UIColor.UIInput.onUIChange(panel.updateProperty.bind(panel));
        this.UIElement.addUI(this.UIElement.UIColorPicker);

        return this.UIElement;
    }
}