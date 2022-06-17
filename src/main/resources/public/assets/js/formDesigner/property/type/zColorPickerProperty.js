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
    constructor(key, name, value, isAlwaysEditable) {
        super(key, name, 'colorPickerProperty', value, isAlwaysEditable);
    }
    // DOM Element 생성
    makeProperty(panel) {
        this.panel = panel;
        // 속성 편집 가능여부 체크 - 문서가 '편집'이거나 또는 (문서가 '사용/발행' 이고 항시 편집 가능한 경우) 또는 (문서가 '사용/발행' 이고 연결된 업무흐름이 없는 경우)
        this.isEditable = this.panel.editor.isEditable || (!this.panel.editor.isDestory && this.isAlwaysEditable);

        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);
        this.UIElement.UIColorPicker = new UIInput()
            .setUIId(this.key)
            .setUIValue(this.value)
            .onUIChange(this.updateProperty.bind(this));
        this.UIElement.addUI(this.UIElement.UIColorPicker);

        return this.UIElement;
    }

    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
        new zColorPicker(this.UIElement.UIColorPicker.domElement, { type: 'fill', readOnly: !this.isEditable });
    }

    // 속성 변경시 발생하는 이벤트 핸들러
    updateProperty(e) {
        e.stopPropagation();
        e.preventDefault();

        this.panel.validationStatus = true;

        // change 일 경우 minLength, maxLength 체크
        if (e.type === 'change' && !zValidation.changeValidationCheck(e.target)) {
            this.panel.validationStatus = false; // 유효성 검증 실패
            return false;
        }
        this.panel.update.call(this.panel, e.target.id, e.target.value);
    }
}
