import {UIDiv, UIInput, UIRadioButton, UISpan} from '../../../lib/zUI.js';
import ZProperty from '../zProperty.js';

const propertyExtends = {
    /* date 속성 타입은 추가적인 설정이 없다. */
};

export default class ZDefaultValueRadioProperty extends ZProperty {
    constructor(name, value) {
        super(name, 'defaultValueDateProperty', value);
    }

    // DOM Element 생성
    makeProperty(panel) {
        this.panel = panel;
        switch (this.name) {
        case 'form.properties.element.none':
            // none Property
            this.UIElement = new UIDiv().setUIClass('property')
                .setUIProperty('--data-column', this.columnWidth);

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
            // current Property
            this.UIElement = new UIDiv().setUIClass('property');
            this.UIElement.UIDiv = new UIDiv().setUIClass('flex-row');
            this.UIElement.UIDiv.UIRadioButton = new UIRadioButton(this.value)
                .setUIId(this.getKeyId())
                .onUIClick(this.updateProperty.bind(this));
            this.UIElement.UIDiv.UISpan = new UISpan()
                .setUIClass('text')
                .setUITextContent(i18n.msg('form.properties.option.now'));
            this.UIElement.UIDiv
                .addUI(this.UIElement.UIDiv.UIRadioButton)
                .addUI(this.UIElement.UIDiv.UISpan);
            this.UIElement.addUI(this.UIElement.UIDiv);
            break;
        case 'form.properties.element.date':
            // date Property
            this.UIElement = new UIDiv().setUIClass('property');
            this.UIElement.UIDiv = new UIDiv().setUIClass('flex-row');
            this.UIElement.UIDiv.UIRadioButton = new UIRadioButton(this.value)
                .setUIId(this.getKeyId())
                .onUIClick(this.updateProperty.bind(this));
            this.UIElement.UIDiv.UIInput = new UIInput()
                .setUIId('elementDateInput')
                .setUIValue(this.bindValue)
                .addUIClass('property-value')
                .onUIKeyUp(this.updateProperty.bind(this));
            this.UIElement.UIDiv.UISpan = new UISpan().setUIClass('text').setUITextContent(i18n.msg('form.properties.option.date'));
            this.UIElement.UIDiv
                .addUI(this.UIElement.UIDiv.UIRadioButton)
                .addUI(this.UIElement.UIDiv.UIInput)
                .addUI(this.UIElement.UIDiv.UISpan);
            this.UIElement.addUI(this.UIElement.UIDiv);
            break;
        case 'form.properties.element.datepicker':
            // datePicker Property
            this.UIElement = new UIDiv().setUIClass('property');
            this.UIElement.UIDiv = new UIDiv().setUIClass('flex-row');
            this.UIElement.UIDiv.UIRadioButton = new UIRadioButton(this.value)
                .setUIId(this.getKeyId())
                .onUIClick(this.updateProperty.bind(this));
            this.UIElement.UIDiv.UIInput = new UIInput()
                .setUIId('elementDatepickerInput')
                .setUIValue(this.bindValue)
                .addUIClass('datepicker')
                .onUIClick(this.updateProperty.bind(this));
            this.UIElement.UIDiv
                .addUI(this.UIElement.UIDiv.UIRadioButton)
                .addUI(this.UIElement.UIDiv.UIInput);
            this.UIElement.addUI(this.UIElement.UIDiv);
            break;
        case 'form.properties.validation.minDate':
        case 'form.properties.validation.maxDate':
            // minDate, maxDate 속성
            this.UIElement = new UIDiv().setUIClass('property')
                .setUIProperty('--data-column', this.columnWidth);
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