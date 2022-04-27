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
import { FORM } from '../../../lib/zConstants.js';
import { UIDiv, UIInput } from '../../../lib/zUI.js';
import ZProperty from '../zProperty.js';

const propertyExtends = {
    /* 추가적인 설정이 없다. */
};

export default class ZDateTimePickerProperty extends ZProperty {
    constructor(key, name, value, pickerType, isAlwaysEditable) {
        super(key, name, 'dateTimePickerProperty', value, isAlwaysEditable);
        this.pickerType = pickerType;
    }
    // DOM Element 생성
    makeProperty(panel) {
        this.panel = panel;
        // 속성 편집 가능여부 체크 - 문서가 '편집'이거나 또는 (문서가 '사용/발행' 이고 항시 편집 가능한 경우) 또는 (문서가 '사용/발행' 이고 연결된 업무흐름이 없는 경우)
        this.isEditable = this.panel.editor.isEditable || (!this.panel.editor.isDestory && this.isAlwaysEditable)
            || (!this.panel.editor.isDestory && !this.panel.editor.isCreatedWorkFlow);

        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);
        // inputbox
        this.UIElement.UIInput = new UIInput(this.value)
            .setUIReadOnly(!this.isEditable)
            .setUIId(this.key);

        switch (this.pickerType) {
            case FORM.DATE_TYPE.DATE_PICKER:
                this.UIElement.UIInput.setUIClass('z-input i-date-picker text-ellipsis');
                break;
            case FORM.DATE_TYPE.TIME_PICKER:
                this.UIElement.UIInput.setUIClass('z-input i-time-picker text-ellipsis');
                break;
            case FORM.DATE_TYPE.DATETIME_PICKER:
                this.UIElement.UIInput.setUIClass('z-input i-datetime-picker text-ellipsis');
                break;
        }

        this.UIElement.addUI(this.UIElement.UIInput);

        return this.UIElement;
    }

    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
        if (!this.isEditable) { return false; }
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
    }

    // 속성 변경시 발생하는 이벤트 핸들러
    updateProperty(elem) {
        this.panel.update.call(this.panel, elem.id, elem.value);
    }
}
