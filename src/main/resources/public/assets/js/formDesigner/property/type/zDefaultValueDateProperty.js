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
        switch (this.name) {
            case 'form.properties.element.none':
                // 기본값
                // 없음
                this.UIElement = new UIDiv().setUIClass('property')
                    .setUIProperty('--data-column', this.columnWidth);
                // 라벨
                this.UIElement.UILabel = this.makeLabelProperty('form.properties.element.defaultValue');
                this.UIElement.addUI(this.UIElement.UILabel);
                this.UIElement.UIDiv = new UIDiv().setUIClass('flex-row');
                this.UIElement.UIDiv.UIRadioButton = new UIRadioButton(this.value).setUIId(this.getKeyId()).onUIClick(this.updateProperty.bind(this));
                this.UIElement.UIDiv.UISpan = new UISpan().setUIClass('text').setUITextContent(i18n.msg('form.properties.option.none'));
                this.UIElement.UIDiv
                    .addUI(this.UIElement.UIDiv.UIRadioButton)
                    .addUI(this.UIElement.UIDiv.UISpan);
                this.UIElement.addUI(this.UIElement.UIDiv);
                break;
            case 'form.properties.element.current':
                // 현재
                this.UIElement = new UIDiv().setUIClass('property')
                this.UIElement.UIDiv = new UIDiv().setUIClass('flex-row');
                this.UIElement.UIDiv.UIRadioButton = new UIRadioButton(this.value).setUIId(this.getKeyId()).onUIClick(this.updateProperty.bind(this));
                this.UIElement.UIDiv.UISpan = new UISpan().setUIClass('text').setUITextContent(i18n.msg('form.properties.option.now'));
                this.UIElement.UIDiv
                    .addUI(this.UIElement.UIDiv.UIRadioButton)
                    .addUI(this.UIElement.UIDiv.UISpan);
                this.UIElement.addUI(this.UIElement.UIDiv);
                break;
            case 'form.properties.element.date':
                // 일 후
                this.UIElement = new UIDiv().setUIClass('property')
                this.UIElement.UIDiv = new UIDiv().setUIClass('flex-row');
                this.UIElement.UIDiv.UIRadioButton = new UIRadioButton(false).onUIClick(this.updateProperty.bind(this));
                this.UIElement.UIDiv.UIInput = new UIInput().setUIId(this.getKeyId()).setUIValue(this.value).onUIKeyUp(this.updateProperty.bind(this));
                this.UIElement.UIDiv.UISpan = new UISpan().setUIClass('text').setUITextContent(i18n.msg('form.properties.option.date'));
                this.UIElement.UIDiv
                    .addUI(this.UIElement.UIDiv.UIRadioButton)
                    .addUI(this.UIElement.UIDiv.UIInput)
                    .addUI(this.UIElement.UIDiv.UISpan);
                this.UIElement.addUI(this.UIElement.UIDiv);
                break;
            case 'form.properties.element.datepicker':
                // 달력
                this.UIElement = new UIDiv().setUIClass('property')
                this.UIElement.UIDiv = new UIDiv().setUIClass('flex-row');
                this.UIElement.UIDiv.UIRadioButton = new UIRadioButton(false).onUIClick(this.updateProperty.bind(this));
                this.UIElement.UIDiv.UIInput = new UIInput().setUIId(this.getKeyId()).setUIValue(this.value).addUIClass('datepicker').onUIClick(this.updateProperty.bind(this));
                this.UIElement.UIDiv
                    .addUI(this.UIElement.UIDiv.UIRadioButton)
                    .addUI(this.UIElement.UIDiv.UIInput);
                this.UIElement.addUI(this.UIElement.UIDiv);
                break;
            case 'form.properties.validation.minDate':
            case 'form.properties.validation.maxDate':
                this.UIElement = new UIDiv().setUIClass('property')
                    .setUIProperty('--data-column', this.columnWidth);
                // 라벨
                this.UIElement.UILabel = this.makeLabelProperty();
                this.UIElement.addUI(this.UIElement.UILabel);
                this.UIElement.UIInput = new UIInput()
                    .setUIId(this.getKeyId())
                    .setUIValue(this.value)
                    .addUIClass('datepicker')
                    .onUIClick(this.updateProperty.bind(this));
                this.UIElement.addUI(this.UIElement.UIInput);
                break;
            default:
                break;
        }

        return this.UIElement;
    }

    // 속성 변경 시, 발생하는 이벤트 핸들러
    updateProperty(e) {
        // init datepicker
        if (e.type === 'keyup') {
            e.stopPropagation();
            e.preventDefault();
            this.panel.update.call(this.panel, e.target.id, e.target.value);
        }

        if (e.type === 'click' && e.target.type === 'text' && e.target.classList.contains('datepicker')) {
            e.stopPropagation();
            e.preventDefault();
            if (!e.target.classList.contains('picker-main')) {
                zDateTimePicker.initDatePicker(e.target.id, undefined);
                e.target.click();
            }
            this.panel.update.call(this.panel, e.target.id, e.target.value);
        }

        if (e.type === 'click' && e.target.type === 'radio') {
            e.target.parentNode.parentNode.parentNode.parentNode.querySelectorAll('input[type=radio]').forEach((property) => {
                if (e.target === property) {
                    e.target.checked = true;
                    this.panel.update.call(this.panel, e.target.id, e.target.checked);
                } else {
                    property.checked = false;
                    this.panel.update.call(this.panel, property.id, property.checked);
                }
            })
        }
    }
}