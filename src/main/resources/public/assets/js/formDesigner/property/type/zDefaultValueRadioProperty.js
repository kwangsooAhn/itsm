/**
 * Default Value Radio Property Class
 *
 * 컴포넌트의 기본 값을 어떤 방식으로 제공할지 선택하는 속성항목이다.
 * 현재는 date, time, datTime 에서 사용되고 있으며 옵션들도 date, time, datTime 용으로 맞추어져 있다.
 *
 * @author Woo Da Jung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import { UIDiv, UIInput, UILabel, UIRadioButton, UISpan } from '../../../lib/zUI.js';
import { zValidation } from '../../../lib/zValidation.js';
import ZProperty from '../zProperty.js';

const propertyExtends = {
    /* 추가적인 설정이 없다. */
};

export default class ZDefaultValueRadioProperty extends ZProperty {
    constructor(name, value, options) {
        super(name, 'defaultValueDateProperty', value);

        this.options = options;
    }

    // DOM Element 생성
    makeProperty(panel) {
        this.panel = panel;

        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);

        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);
        // 그룹
        this.UIElement.UIGroup = new UIDiv().setUIClass('default-type-radio');
        this.UIElement.addUI(this.UIElement.UIGroup);

        const defaultValueArray = this.value.split('|'); // none, now, date|-3, time|2, datetime|7|0 등

        this.options.forEach((item) => {
            const radioGroup = new UIDiv().setUIClass('radio-property-group').addUIClass('vertical');
            const radioId = item.value.substr(0, 1).toUpperCase() + item.value.substr(1, item.value.length);
            // 라벨
            radioGroup.UILabel = new UILabel().setUIClass('radio').setUIFor('radioProperty' + radioId);
            radioGroup.addUI(radioGroup.UILabel);
            // 라디오 버튼
            radioGroup.UILabel.UIRadio = new UIRadioButton(defaultValueArray[0] === item.value)
                .setUIId('radioProperty' + radioId)
                .setUIAttribute('name', this.getKeyId())
                .setUIClass('hidden-radio')
                .setUIAttribute('data-value', item.value)
                .onUIChange(this.updateProperty.bind(this));
            radioGroup.UILabel.addUI(radioGroup.UILabel.UIRadio);
            radioGroup.UILabel.addUI(new UISpan());
            if (item.name !== '') {
                radioGroup.UILabel.addUI(new UISpan().setUIClass('label').setUIInnerHTML(i18n.msg(item.name)));
            }

            switch (item.value) {
            case 'date':
                radioGroup.UIDiv = new UIDiv().setUIClass('radio-item');
                radioGroup.addUI(radioGroup.UIDiv);

                radioGroup.UIDiv.UIInput = new UIInput((defaultValueArray[0] === item.value ? defaultValueArray[1] : ''))
                    .setUIAttribute('name', this.getKeyId())
                    .setUIAttribute('data-validation-type', 'number')
                    .setUIAttribute('data-validation-max', '1000')
                    .onUIKeyUp(this.updateProperty.bind(this));
                radioGroup.UIDiv.addUI(radioGroup.UIDiv.UIInput);
                radioGroup.UIDiv.addUI(new UISpan().setUITextContent(i18n.msg('form.properties.option.date')));
                break;
            case 'datepicker':
                radioGroup.UIInput = new UIInput((defaultValueArray[0] === item.value ? defaultValueArray[1] : ''))
                    .setUIClass(item.value)
                    .setUIId('dateProperty')
                    .setUIAttribute('name', this.getKeyId());
                radioGroup.addUI(radioGroup.UIInput);

                zDateTimePicker.initDatePicker(radioGroup.UIInput.domElement, this.updateProperty.bind(this));
                break;
            case 'time':
                break;
            case 'timepicker':
                break;
            case 'datetime':
                break;
            case 'datetimepicker':
                break;
            }

            this.UIElement.UIGroup.addUI(radioGroup);
        });

        return this.UIElement;
    }

    // 속성 변경 시, 발생하는 이벤트 핸들러
    // 값을 none, now, date|-3, time|2, datetime|7|0, datetimepicker|2020-03-20 09:00 등으로 저장한다.
    updateProperty(e) {
        if (e.preventDefault) {
            e.preventDefault();
        }
        // enter, tab 입력시
        if (e.type === 'keyup' && (e.keyCode === 13 || e.keyCode === 9)) {
            return false;
        }

        const elem = e.target || e;
        // 유효성 검증
        if (elem.type === 'text' && !zValidation.keyUpValidationCheck(e.target)) {
            return false;
        }

        const parentElem = elem.parentNode.parentNode;
        const curRadioElem = parentElem.querySelector('input[type=radio]');
        // 변경된 값의 radio 버튼이 선택되지 않았다면 값을 처리하지 않는다.
        if (!curRadioElem.checked) { return false; }

        // radio 변경시
        const defaultValue = curRadioElem.getAttribute('data-value'); // none, now, date, datepicker 등
        if (defaultValue === 'date') {
            const inputElems = parentElem.querySelectorAll('input[type=text]');
            let changeValue = '';
            inputElems.forEach((cell) => {
                changeValue += ('|' + cell.value);
            });
            this.panel.update.call(this.panel, elem.name, defaultValue + changeValue);
        } else if (defaultValue === 'datepicker') {
            const  datepickerElem = parentElem.querySelector('.datepicker');
            this.panel.update.call(this.panel, elem.name, defaultValue + '|' + datepickerElem.value);
        } else {
            this.panel.update.call(this.panel, elem.name, defaultValue);
        }
    }
}