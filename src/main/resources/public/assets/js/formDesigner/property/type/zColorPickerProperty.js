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
import { zValidation } from '../../../lib/zValidation.js';
import ZProperty from '../zProperty.js';
import { UIDiv, UIInput } from '../../../lib/zUI.js';

const propertyExtends = {
    /* 추가적인 설정이 없다. */
};

export default class ZColorPickerProperty extends ZProperty {
    constructor(key, name, value) {
        super(key, name, 'colorPickerProperty', value);
    }
    // DOM Element 생성
    makeProperty(panel) {
        this.panel = panel;

        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);
        this.UIElement.UIColorPicker = new UIInput()
            .setUIId(this.key)
            .setUIValue(this.value)
            .onUIChange(this.updateProperty.bind(this));
        // TODO: 라이브러리 적용
        new zColorPicker(this.UIElement.UIColorPicker, { type: 'fill' });

        // color picker
        /*const colorPickerOption = {
            isOpacity: (this.isOpacityUsed), // 불투명도 사용시
            data: {
                isSelected: true, // 기존 색상 선택 여부
                selectedClass: 'selected', // 기존 값 색상에 css 적용 (테두리)
                value: this.value // 기존 값
            }
        };
        this.UIElement.UIColorPicker = new UIColor(colorPickerOption).setUIId(this.key);
        this.UIElement.UIColorPicker.UIColor.UIInput.onUIChange(this.updateProperty.bind(this));*/
        this.UIElement.addUI(this.UIElement.UIColorPicker);

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
        this.panel.update.call(this.panel, e.target.id, e.target.value);
    }
}