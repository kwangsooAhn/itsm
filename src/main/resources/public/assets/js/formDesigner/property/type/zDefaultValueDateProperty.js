import {UIDiv, UIInput, UIRadioButton, UISpan} from "../../../lib/zUI.js";
import ZProperty from '../zProperty.js';

const propertyExtends = {
    /* date 속성 타입은 추가적인 설정이 없다. */
}

export default class ZDefaultValueDateProperty extends ZProperty {
    constructor(name, value) {
        super(name, 'defaultValueDateProperty', value);
    }
    // DOM Element 생성
    makeProperty(panel){
        this.panel = panel;

        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);

        switch(this.name) {
            case 'form.properties.element.defaultValue':
                // 기본값
                // 없음
                this.UIElement.UIDiv = new UIDiv().setUIClass('flex-row');
                this.UIElement.UIDiv.UICheckbox = new UIRadioButton();
                this.UIElement.UIDiv.addUI(this.UIElement.UIDiv.UICheckbox).addUI(new UISpan().setUIClass('text')
                    .setUITextContent(i18n.msg('form.properties.option.none')))
                    .onUIChange(this.updateProperty.bind(this));
                this.UIElement.addUI(this.UIElement.UIDiv);

                // 현재
                this.UIElement.UIDiv = new UIDiv().setUIClass('flex-row');
                this.UIElement.UIDiv.UICheckbox = new UIRadioButton();
                this.UIElement.UIDiv.addUI(this.UIElement.UIDiv.UICheckbox).addUI(new UISpan().setUIClass('text')
                    .setUITextContent(i18n.msg('form.properties.option.now')))
                    .onUIChange(this.updateProperty.bind(this));
                this.UIElement.addUI(this.UIElement.UIDiv);

                // 일 후
                this.UIElement.UIDiv = new UIDiv().setUIClass('flex-row');
                this.UIElement.UIDiv.UICheckbox = new UIRadioButton();
                this.UIElement.UIDiv.addUI(this.UIElement.UIDiv.UICheckbox).addUI(new UISpan().setUIClass('text')
                    .setUITextContent(i18n.msg('form.properties.option.date')))
                    .onUIChange(this.updateProperty.bind(this));
                this.UIElement.addUI(this.UIElement.UIDiv);

                // 달력
                this.UIElement.UIDiv = new UIDiv().setUIClass('flex-row');
                this.UIElement.UIDiv.UICheckbox = new UIRadioButton();
                this.UIElement.UIDiv.addUI(this.UIElement.UIDiv.UICheckbox).addUI(new UIInput()
                    .setUIId(this.getKeyId())
                    .setUIValue(this.value)
                    .addUIClass('datepicker')
                    .onUIChange(this.updateProperty.bind(this)));
                this.UIElement.addUI(this.UIElement.UIDiv);
                break;
            case 'form.properties.validation.minDate':
            case 'form.properties.validation.maxDate':
                this.UIElement.UIInput = new UIInput()
                    .setUIId(this.getKeyId())
                    .setUIValue(this.value)
                    .addUIClass('datepicker')
                    .onUIChange(this.updateProperty.bind(this));
                this.UIElement.addUI(this.UIElement.UIInput);
                break;
            default:
                break;
        }

        return this.UIElement;
    }
    // 속성 변경 시, 발생하는 이벤트 핸들러
    updateProperty(e) {
        e.stopPropagation();
        e.preventDefault();

        if (e.type === 'change') {
            console.log('change Event');
            return false;
        }
        this.panel.update.call(this.panel, e.target.id, e.target.value);
    }

    /**
     * date, time, datetime default 포멧 변경시,
     * default 값을 none, now, date|-3, time|2, datetime|7|0, datetimepicker|2020-03-20 09:00 등으로 저장한다.
     * @param {Object} e 이벤트 대상
     */
    setDateFormat(e) {
        let el = e.target || e;
        let parentEl = el.parentNode.parentNode;
        let checkedRadio = parentEl.parentNode.querySelector('input[type=radio]:checked');
        if (checkedRadio !== null) { // radio 버튼 존재시
            if (parentEl.querySelector('input[type=radio]').id !== checkedRadio.id) { return false; }
            let checkedPropertiesArr = checkedRadio.name.split('-');
            let changeValue = checkedRadio.value;
            if (changeValue === 'none' || changeValue === 'now') {
                changePropertiesValue(changeValue, checkedPropertiesArr[0], checkedPropertiesArr[1]);
            } else {
                let inputCells = parentEl.querySelectorAll('input[type=text]');
                if (changeValue === 'datepicker' || changeValue === 'timepicker' || changeValue === 'datetimepicker') {
                    changeValue += ('|' + inputCells[0].value);
                } else {
                    for (let i = 0, len = inputCells.length; i < len; i++ ) {
                        changeValue += ('|' + inputCells[i].value);
                    }
                }
                changePropertiesValue(changeValue, checkedPropertiesArr[0], checkedPropertiesArr[1]);
            }
        } else {
            let changePropertiesArr = parentEl.id.split('-');
            changePropertiesValue(el.value, changePropertiesArr[0], changePropertiesArr[1]);
        }
    }

    initDatePicket() {
        const datepickerElems = this.panel.querySelectorAll('.datepicker');
        let i, len;
        for (i = 0, len = datepickerElems.length; i < len; i++) {
            dateTimePicker.initDatePicker(datepickerElems[i].id, setDateFormat);
        }
    }
}