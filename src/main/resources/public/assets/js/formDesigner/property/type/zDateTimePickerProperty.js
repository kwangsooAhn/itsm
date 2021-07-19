/**
 * Date Time picker Property Class
 *
 * zDateTimePicker 라이브러리를 사용하는 속성항목이다.
 *
 * @author Woo Da Jung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import { CLASS_PREFIX, FORM } from '../../../lib/zConstants.js';
import { UIDiv, UIInput } from '../../../lib/zUI.js';
import ZProperty from '../zProperty.js';

const propertyExtends = {
    /* 추가적인 설정이 없다. */
};

export default class ZDateTimePickerProperty extends ZProperty {
    constructor(key, name, value, pickerType) {
        super(key, name, 'dateTimePickerProperty', value);
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
            .setUIId(this.key)
            .setUIClass(CLASS_PREFIX + 'input i-date-picker');

        this.UIElement.addUI(this.UIElement.UIInput);

        switch (this.pickerType) {
        case FORM.DATE_TYPE.DATE_PICKER:
            zDateTimePicker.initDatePicker(this.UIElement.UIInput.domElement, this.updateProperty.bind(this));
            break;
        case FORM.DATE_TYPE.TIME_PICKER:
            zDateTimePicker.initTimePicker(this.UIElement.UIInput.domElement, this.updateProperty.bind(this));
            break;
        case FORM.DATE_TYPE.DATETIME_PICKER:
            zDateTimePicker.initDateTimePicker(this.UIElement.UIInput.domElement, this.updateProperty.bind(this));
            break;
        }

        return this.UIElement;
    }
    // 속성 변경시 발생하는 이벤트 핸들러
    updateProperty(elem) {
        this.panel.update.call(this.panel, elem.id, elem.value);
    }
}
