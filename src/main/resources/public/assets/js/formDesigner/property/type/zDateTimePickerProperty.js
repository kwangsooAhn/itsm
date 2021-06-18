/**
 * Date picker Property Class
 *
 * 컴포넌트의 기본 값을 어떤 방식으로 제공할지 선택하는 속성항목이다.
 * 현재는 inputBox 에서만 사용되고 있으며 옵션들도 inputBox 용으로 맞추어져 있다.
 *
 * @author Woo Da Jung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import { FORM } from '../../../lib/zConstants.js';
import { UIDiv, UIInput } from '../../../lib/zUI.js';
import { zValidation } from '../../../lib/zValidation.js';
import ZProperty from '../zProperty.js';

const propertyExtends = {
    /* 추가적인 설정이 없다. */
};

export default class ZDateTimePickerProperty extends ZProperty {
    constructor(name, value, pickerType) {
        super(name, 'dateTimePickerProperty', value);
        this.pickerType = pickerType;
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
        this.UIElement.UIInput = new UIInput(this.value)
            .setUIId(this.getKeyId())
            .setUIClass(this.pickerType);

        this.UIElement.addUI(this.UIElement.UIInput);

        switch (this.pickerType) {
        case FORM.DATE_TYPE.DATE_PICKER:
            zDateTimePicker.initDatePicker(this.UIElement.UIInput.domElement, this.updateProperty.bind(this));
            break;
        case FORM.DATE_TYPE.TIME_PICKER:
            break;
        case FORM.DATE_TYPE.DATETIME_PICKER:
            break;
        }

        return this.UIElement;
    }
    // 속성 변경시 발생하는 이벤트 핸들러
    updateProperty(elem) {
        this.panel.update.call(this.panel, elem.id, elem.value);
    }
}