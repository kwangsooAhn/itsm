/**
 * Default Value Radio Property Class
 *
 * 컴포넌트의 기본 값을 어떤 방식으로 제공할지 선택하는 속성항목이다.
 * 현재는 date, time, datTime 에서 사용되고 있으며 옵션들도 date, time, dateTime 용으로 맞추어져 있다.
 *
 * @author Woo Da Jung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import { FORM } from '../../../lib/zConstants.js';
import { UIDiv, UIInput, UILabel, UIRadioButton, UISpan } from '../../../lib/zUI.js';
import { zValidation } from '../../../lib/zValidation.js';
import ZProperty from '../zProperty.js';

const propertyExtends = {
    /* 추가적인 설정이 없다. */
};

export default class ZDefaultValueRadioProperty extends ZProperty {
    constructor(key, name, value, options) {
        super(key, name, 'defaultValueRadioProperty', value);

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
                .setUIAttribute('name', this.key)
                .setUIClass('hidden-radio')
                .setUIAttribute('data-value', item.value)
                .onUIChange(this.updateProperty.bind(this));
            radioGroup.UILabel.addUI(radioGroup.UILabel.UIRadio);
            radioGroup.UILabel.addUI(new UISpan());
            
            if (!zValidation.isEmpty(item.name)) {
                radioGroup.UILabel.addUI(new UISpan().setUIClass('label').setUIInnerHTML(i18n.msg(item.name)));
            }

            switch (item.value) {
            case FORM.DATE_TYPE.DAYS:
                radioGroup.UIDiv = new UIDiv().setUIClass('radio-item');
                radioGroup.addUI(radioGroup.UIDiv);

                radioGroup.UIDiv.UIInput = new UIInput((defaultValueArray[0] === item.value ? defaultValueArray[1] : ''))
                    .setUIAttribute('name', this.key)
                    .setUIAttribute('data-validation-type', 'number')
                    .setUIAttribute('data-validation-max', '1000')
                    .onUIKeyUp(this.updateProperty.bind(this))
                    .onUIChange(this.updateProperty.bind(this));
                radioGroup.UIDiv.addUI(radioGroup.UIDiv.UIInput);
                radioGroup.UIDiv.addUI(new UISpan().setUITextContent(i18n.msg('form.properties.option.days')));
                break;
            case FORM.DATE_TYPE.DATE_PICKER:
                radioGroup.UIInput = new UIInput((
                    aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, FORM.DATE_TYPE.DAYS,
                        defaultValueArray[0] === item.value ? defaultValueArray[1] : '')))
                    .setUIClass('datepicker')
                    .addUIClass('picker')
                    .setUIId('dateProperty')
                    .setUIAttribute('name', this.key);
                radioGroup.addUI(radioGroup.UIInput);

                zDateTimePicker.initDatePicker(radioGroup.UIInput.domElement, this.updateProperty.bind(this));
                break;
            case FORM.DATE_TYPE.HOURS:
                radioGroup.UIDiv = new UIDiv().setUIClass('radio-item');
                radioGroup.addUI(radioGroup.UIDiv);

                radioGroup.UIDiv.UIInput = new UIInput(defaultValueArray[0] === item.value ? defaultValueArray[1] : '')
                    .setUIAttribute('name', this.key)
                    .setUIAttribute('data-validation-type', 'number')
                    .setUIAttribute('data-validation-max', '1000')
                    .onUIKeyUp(this.updateProperty.bind(this))
                    .onUIChange(this.updateProperty.bind(this));
                radioGroup.UIDiv.addUI(radioGroup.UIDiv.UIInput);
                radioGroup.UIDiv.addUI(new UISpan().setUITextContent(i18n.msg('form.properties.option.hours')));
                break;
            case FORM.DATE_TYPE.TIME_PICKER:
                radioGroup.UIInput = new UIInput(aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, FORM.DATE_TYPE.HOURS,
                    defaultValueArray[0] === item.value ? defaultValueArray[1] : ''))
                    .setUIClass('datepicker')
                    .addUIClass('picker')
                    .setUIId('timeProperty')
                    .setUIAttribute('name', this.key);
                radioGroup.addUI(radioGroup.UIInput);
                zDateTimePicker.initTimePicker(radioGroup.UIInput.domElement, this.updateProperty.bind(this));
                break;
            case FORM.DATE_TYPE.DATETIME:
                radioGroup.UIDiv = new UIDiv().setUIClass('radio-item');
                radioGroup.addUI(radioGroup.UIDiv);
                radioGroup.UIDiv.addUI(new UIInput((defaultValueArray[0] === item.value ? defaultValueArray[1] : ''))
                    .setUIAttribute('name', this.key)
                    .setUIAttribute('data-validation-type', 'number')
                    .setUIAttribute('data-validation-max', '1000')
                    .onUIKeyUp(this.updateProperty.bind(this))
                    .onUIChange(this.updateProperty.bind(this)));
                radioGroup.UIDiv.addUI(new UISpan().setUITextContent(i18n.msg('form.properties.option.day')));
                radioGroup.UIDiv.addUI(new UIInput((defaultValueArray[0] === item.value ? defaultValueArray[2] : ''))
                    .setUIAttribute('name', this.key)
                    .setUIAttribute('data-validation-type', 'number')
                    .setUIAttribute('data-validation-max', '1000')
                    .onUIKeyUp(this.updateProperty.bind(this))
                    .onUIChange(this.updateProperty.bind(this)));
                radioGroup.UIDiv.addUI(new UISpan().setUITextContent(i18n.msg('form.properties.option.hours')));
                break;
            case FORM.DATE_TYPE.DATETIME_PICKER:
                radioGroup.UIInput = new UIInput(aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, FORM.DATE_TYPE.DATETIME,
                    defaultValueArray[0] === item.value ? defaultValueArray[1] : ''))
                    .setUIClass('datepicker')
                    .addUIClass('picker')
                    .setUIId('timeProperty')
                    .setUIAttribute('name', this.key);
                radioGroup.addUI(radioGroup.UIInput);
                zDateTimePicker.initDateTimePicker(radioGroup.UIInput.domElement, this.updateProperty.bind(this));
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
        if (elem.type === 'text' && e.type === 'keyup' && !zValidation.keyUpValidationCheck(e.target)) {
            return false;
        }

        const parentElem = elem.parentNode.parentNode;
        const curRadioElem = parentElem.querySelector('input[type=radio]');
        // 변경된 값의 radio 버튼이 선택되지 않았다면 값을 처리하지 않는다.
        if (!curRadioElem.checked) { return false; }

        // radio 변경시
        const defaultValue = curRadioElem.getAttribute('data-value'); // none, now, date, datepicker 등
        if (defaultValue === FORM.DATE_TYPE.DAYS || defaultValue === FORM.DATE_TYPE.HOURS ||
            defaultValue === FORM.DATE_TYPE.DATETIME) {
            const inputElems = parentElem.querySelectorAll('input[type=text]');
            let changeValue = '';
            inputElems.forEach((cell) => {
                changeValue += ('|' + cell.value);
            });
            this.panel.update.call(this.panel, elem.name, defaultValue + changeValue);
        } else if (defaultValue === FORM.DATE_TYPE.DATE_PICKER ||
            defaultValue === FORM.DATE_TYPE.TIME_PICKER ||
            defaultValue === FORM.DATE_TYPE.DATETIME_PICKER) {
            const datepickerElem = parentElem.querySelector('.picker');
            this.panel.update.call(this.panel
                , elem.name
                , defaultValue + '|'
            + aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT, defaultValue.replace('Picker', ''), datepickerElem.value));
        } else {
            this.panel.update.call(this.panel, elem.name, defaultValue);
        }
    }
}
