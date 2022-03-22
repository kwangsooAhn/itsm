/**
 * Default Value Dropdown Code Property Class
 *
 * 컴포넌트의 기본 값을 어떤 방식으로 제공할지 선택하는 속성항목이다.
 * 현재는 Dropdown Code 컴포넌트 에서만 사용되고 있으며 옵션들도 해당 컴포넌트 용으로 맞추어져 있다.
 *
 * @author Woo Da Jung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import ZProperty from '../zProperty.js';
import { zValidation } from '../../../lib/zValidation.js';
import { UIButton, UIDiv, UIInput, UISelect, UISpan } from '../../../lib/zUI.js';
import { FORM } from '../../../lib/zConstants.js';

const propertyExtends = {
    options : [
        { name: 'form.properties.code', value: 'code' },
        { name: 'form.properties.mapId', value: 'mappingId' }
    ]
};

export default class ZDefaultValueDropdownCodeProperty extends ZProperty {
    constructor(key, name, value, isAlwaysEditable) {
        super(key, name, 'defaultValueDropdownCodeProperty', value, isAlwaysEditable);

        this.options = propertyExtends.options;
    }
    // DOM Element 생성
    makeProperty(panel) {
        this.panel = panel;
        // 속성 편집 가능여부 체크 - 문서가 '편집'이거나 또는 (문서가 '사용/발행' 이고 항시 편집 가능한 경우)
        this.isEditable = this.panel.editor.isEditable || (!this.panel.editor.isDestory && this.isAlwaysEditable);

        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);

        // switch button
        this.UIElement.UIButtonGroup = new UIDiv().setUIClass('z-button-switch-group');
        this.options.forEach((item) => {
            const name = item.value.substr(0, 1).toUpperCase() +
                item.value.substr(1, item.value.length);
            this.UIElement.UIButtonGroup['UIButton' + name] = new UIButton().setUIId(this.key)
                .setUIAttribute('data-type', item.value)
                .setUIClass('z-button-switch')
                .setUIDisabled(!this.isEditable)
                .onUIClick(this.updateProperty.bind(this))
                .addUI(new UISpan().setUIClass('z-text').setUITextContent(i18n.msg(item.name)));

            if (!this.isEditable) {
                this.UIElement.UIButtonGroup['UIButton' + name].addUIClass('disabled');
            }

            if (this.value.hasOwnProperty(item.value)) {
                this.UIElement.UIButtonGroup['UIButton' + name].addUIClass('selected');
            }
            this.UIElement.UIButtonGroup.addUI(this.UIElement.UIButtonGroup['UIButton' + name]);
        });
        this.UIElement.addUI(this.UIElement.UIButtonGroup);

        // input
        const inputValueKey = this.value.hasOwnProperty(FORM.DROPDOWN_CODE.CODE) ?
            FORM.DROPDOWN_CODE.CODE : FORM.DROPDOWN_CODE.MAPPING_ID;
        this.UIElement.UIInput = new UIInput().setUIId(this.key)
            .setUIValue(this.value[inputValueKey])
            .setUIAttribute('data-validation-min-length', this.validation.minLength)
            .setUIAttribute('data-validation-max-length', this.validation.maxLength)
            .setUIReadOnly(!this.isEditable)
            .onUIKeyUp(this.updateProperty.bind(this))
            .onUIChange(this.updateProperty.bind(this));
        this.UIElement.addUI(this.UIElement.UIInput);

        if (this.value.hasOwnProperty(FORM.DROPDOWN_CODE.CODE)) {
            // 기본값 코드 라벨
            this.UIElement.UILabel = this.makeLabelProperty('form.properties.element.defaultCodeValue');
            this.UIElement.UILabel.addUIClass('mt-3');
            this.UIElement.addUI(this.UIElement.UILabel);
            // 기본값 코드 input
            this.UIElement.UIInput = new UIInput().setUIId(this.key)
                .setUIAttribute('data-type', FORM.DROPDOWN_CODE.DEFAULT_CODE)
                .setUIValue(this.value[FORM.DROPDOWN_CODE.DEFAULT_CODE])
                .setUIAttribute('data-validation-min-length', this.validation.minLength)
                .setUIAttribute('data-validation-max-length', this.validation.maxLength)
                .setUIReadOnly(!this.isEditable)
                .onUIKeyUp(this.updateProperty.bind(this))
                .onUIChange(this.updateProperty.bind(this));
            this.UIElement.addUI(this.UIElement.UIInput);
        }
        return this.UIElement;
    }

    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {}

    // 속성 변경시 발생하는 이벤트 핸들러
    updateProperty(e) {
        e.stopPropagation();
        e.preventDefault();

        this.panel.validationStatus = true;

        // enter, tab 입력시
        if (e.type === 'keyup' && (e.keyCode === 13 || e.keyCode === 9)) { return false; }
        if (e.type === 'click' && e.target.classList.contains('selected')) { return false; }
        // change 일 경우 minLength, maxLength 체크
        if (e.type === 'change' && !zValidation.changeValidationCheck(e.target)) {
            this.panel.validationStatus = false; // 유효성 검증 실패
            return false;
        }
        console.log(this.value);
        console.log(e.type);
        console.log(e.target);
        //this.panel.update.call(this.panel, e.target.id, this.getPropertyValue(e.type, e.target));
    }

    /**
     * 속성 엘리먼트의 값 조회
     * @param evtType 이벤트 타입
     * @param e 이벤트객체
     */
    getPropertyValue(evtType, element) {
        switch (evtType) {
            case 'keyup': // input
                return 'input|' + element.value;
            case 'change': // select box, input
                return ((element.type === 'text') ? 'input|' : 'select|') + element.value;
            case 'click': // button
                const buttonGroup = element.parentNode;
                // 초기화
                for (let i = 0, len = buttonGroup.childNodes.length ; i< len; i++) {
                    let child = buttonGroup.childNodes[i];
                    if (child.classList.contains('selected')) {
                        child.classList.remove('selected');
                    }
                }
                element.classList.add('selected');

                const defaultTypeGroup = buttonGroup.parentNode;
                const input = defaultTypeGroup.querySelector('input[type=text]');
                const select = defaultTypeGroup.querySelector('select');
                if (element.getAttribute('data-type') === 'input') { // input 활성화
                    input.classList.remove('off');
                    select.classList.add('off');
                    return 'input|';
                } else { // select 활성화
                    select.classList.remove('off');
                    input.classList.add('off');
                    select.selectedIndex = 0;
                    return 'select|' + select.options[0].value;
                }
            default:
                return '';
        }
    }
}