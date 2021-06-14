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
    makeProperty(panel) {
        this.panel = panel;

        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);

        switch (this.name) {
            case 'form.properties.element.defaultValue':
                // 기본값
                // 없음
                this.UIElement.UIDiv = new UIDiv().setUIClass('flex-row');
                this.UIElement.UIDiv.UIRadioButton = new UIRadioButton(true);
                this.UIElement.UIDiv.addUI(this.UIElement.UIDiv.UIRadioButton).addUI(new UISpan().setUIClass('text')
                    .setUITextContent(i18n.msg('form.properties.option.none')))
                    .onUIChange(this.updateProperty.bind(this));
                this.UIElement.addUI(this.UIElement.UIDiv);

                // 현재
                this.UIElement.UIDiv = new UIDiv().setUIClass('flex-row');
                this.UIElement.UIDiv.UIRadioButton = new UIRadioButton(false);
                this.UIElement.UIDiv.addUI(this.UIElement.UIDiv.UIRadioButton).addUI(new UISpan().setUIClass('text')
                    .setUITextContent(i18n.msg('form.properties.option.now')))
                    .onUIChange(this.updateProperty.bind(this));
                this.UIElement.addUI(this.UIElement.UIDiv);

                // 일 후
                this.UIElement.UIDiv = new UIDiv().setUIClass('flex-row');
                this.UIElement.UIDiv.UIRadioButton = new UIRadioButton(false);
                this.UIElement.UIDiv.addUI(this.UIElement.UIDiv.UIRadioButton).addUI(new UISpan().setUIClass('text')
                    .setUITextContent(i18n.msg('form.properties.option.date')))
                    .onUIChange(this.updateProperty.bind(this));
                this.UIElement.addUI(this.UIElement.UIDiv);

                // 달력
                this.UIElement.UIDiv = new UIDiv().setUIClass('flex-row');
                this.UIElement.UIDiv.UIRadioButton = new UIRadioButton(false);
                this.UIElement.UIDiv.addUI(this.UIElement.UIDiv.UIRadioButton).addUI(new UIInput()
                    .setUIId(this.getKeyId())
                    .setUIValue(this.value)
                    .addUIClass('datepicker')
                    .onUIClick(this.updateProperty.bind(this)))
                    .onUIChange(this.updateProperty.bind(this));
                this.UIElement.addUI(this.UIElement.UIDiv);
                break;
            case 'form.properties.validation.minDate':
            case 'form.properties.validation.maxDate':
                this.UIElement.UIInput = new UIInput()
                    .setUIId(this.getKeyId())
                    .setUIValue(this.value)
                    .addUIClass('datepicker')
                    .onUIClick(this.updateProperty.bind(this))
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
        if (e.type === 'change' && e.target.classList.contains('radio')) {
            this.setDateFormat(e)
        }

        if (e.type === 'click' && e.target.classList.contains('datepicker')) {
            if (!e.target.classList.contains('picker-main')) {
                zDateTimePicker.initDatePicker(e.target.id, '');
                e.target.click();
            }
        }
        this.panel.update.call(this.panel, e.target.id, e.target.value);
    }

    setDateFormat(e) {
        let el = e.target || e;
        let parentEl = el.parentNode.parentNode;
        let checkedRadio = parentEl.parentNode.querySelector('input[type=radio]:checked');
        if (checkedRadio) {
            checkedRadio.checked = false;
            e.target.checked = true;
        }
    }
}