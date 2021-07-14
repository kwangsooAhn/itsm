/**
 * Slider Property Class
 *
 * 슬라이드 형식의 속성타입을 위한 클래스이다.
 * 예를 들어 컬럼 너비와 같은 항목은 슬라이를 드래그하면서 너비를 늘리거나 줄일 수 있다.
 * 이러한 형식의 속성 항목을 위한 클래스이다.
 *
 * @author Jung Hee Chan <hcjung@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import {UIDiv, UISlider} from '../../../lib/zUI.js';
import { FORM } from '../../../lib/zConstants.js';
import { zValidation } from '../../../lib/zValidation.js';
import ZProperty from '../zProperty.js';

const propertyExtends = {
    /* 슬라이드 속성 타입은 추가적인 설정이 없다. */
};

export default class ZSliderProperty extends ZProperty {
    constructor(key, name, value) {
        super(key, name, 'sliderProperty', value);
    }
    // DOM Element 생성
    makeProperty(panel) {
        this.panel = panel;

        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);

        // slider
        this.UIElement.UISlider = new UISlider(this.value, FORM.COLUMN).setUIMin(1).setUIMax(FORM.COLUMN);
        this.UIElement.UISlider.UIInput.setUIId(this.key)
            .onUIChange(this.updateProperty.bind(this));
        this.UIElement.addUI(this.UIElement.UISlider);

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