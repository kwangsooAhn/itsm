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

import {UIDiv, UIInput, UILabel, UIRadioButton, UISpan} from '../../../lib/zUI.js';
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
        console.log(defaultValueArray);
        this.options.forEach((item) => {
            const radioGroup = new UIDiv().setUIClass('radio-property-group').addUIClass('vertical');
            // 라벨
            radioGroup.UILabel = new UILabel().setUIClass('radio').setUIFor('radio-property-' + item.value);
            radioGroup.addUI(radioGroup.UILabel);
            // 라디오 버튼
            console.log(item.name + ' :  '+ defaultValueArray[0] === item.value);
            radioGroup.UILabel.UIRadio = new UIRadioButton(defaultValueArray[0] === item.value)
                .setUIId('radio-property-' + item.value)
                .setUIAttribute('name', this.getKeyId())
                .setUIClass('hidden-radio')
                .setUIValue(item.value);
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
                    .setUIId('date-property')
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
    updateProperty(e) {
        console.log(e);

        return false;
        // date 속성 입력 시, 기본값 처리
        if (e.type === 'keyup') {
            e.stopPropagation();
            e.preventDefault();
            if (e.target.value === undefined || e.target.value === '') {
                e.target.value = 0;
            }
            this.panel.update.call(this.panel, e.target.id, e.target.value);
        }

        // datePicker 초기화
        if (e.type === 'click' && e.target.type === 'text' && e.target.classList.contains('datepicker')) {
            e.stopPropagation();
            e.preventDefault();
            if (!e.target.classList.contains('picker-main')) {
                zDateTimePicker.initDatePicker(e.target, undefined);
                e.target.click();
            }
            this.panel.update.call(this.panel, e.target.id, e.target.value);
        }

        // 라디오 버튼 선택 이벤트
        if (e.type === 'click' && e.target.type === 'radio') {
            e.target.parentNode.parentNode.parentNode.parentNode.querySelectorAll('input[type=radio]').forEach((property) => {
                if (e.target === property) {
                    e.target.checked = true;
                    this.panel.update.call(this.panel, e.target.id, e.target.checked);
                } else {
                    property.checked = false;
                    this.panel.update.call(this.panel, property.id, property.checked);
                }
            });
        }
    }
}