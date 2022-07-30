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
import { UIButton, UIDiv, UIInput, UISpan } from '../../../lib/zUI.js';
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
        // 속성 편집 가능여부 체크 - 문서가 '편집'이거나 또는 (문서가 '사용/발행' 이고 항시 편집 가능한 경우) 또는 (문서가 '사용/발행' 이고 연결된 업무흐름이 없는 경우)
        this.isEditable = this.panel.editor.isEditable || (!this.panel.editor.isDestory && this.isAlwaysEditable);

        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);

        // switch button
        this.UIElement.UIButtonGroup = new UIDiv().setUIClass('btn__toggle__button').setUICSSText(`width:30%;`);
        this.options.forEach((item) => {
            const name = item.value.substr(0, 1).toUpperCase() +
                item.value.substr(1, item.value.length);
            this.UIElement.UIButtonGroup['UIButton' + name] = new UIButton().setUIId(this.key)
                .setUIAttribute('data-type', item.value)
                .setUIClass('btn__toggle__button--single')
                .setUIDisabled(!this.isEditable)
                .onUIClick(this.updateProperty.bind(this))
                .addUI(new UISpan().setUIClass('text').setUITextContent(i18n.msg(item.name)));

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
            .addUIClass('mt-2')
            .setUIValue(this.value[inputValueKey])
            .setUIAttribute('data-validation-min-length', this.validation.minLength)
            .setUIAttribute('data-validation-max-length', this.validation.maxLength)
            .setUIReadOnly(!this.isEditable)
            .onUIKeyUp(this.updateProperty.bind(this))
            .onUIChange(this.updateProperty.bind(this));
        this.UIElement.addUI(this.UIElement.UIInput);

        // 기본값 코드 라벨
        this.UIElement.UILabel = this.makeLabelProperty('form.properties.element.defaultCodeValue')
            .addUIClass('mt-3')
            .addUIClass('default-code-label')
            .addUIClass((this.value.hasOwnProperty(FORM.DROPDOWN_CODE.CODE) ? 'on' : 'none'));
        this.UIElement.addUI(this.UIElement.UILabel);
        this.UIElement.UILabel.domElement.removeChild(this.UIElement.UILabel.domElement.children[1]);

        // 기본값 코드 input
        this.UIElement.UIInput = new UIInput().setUIId(this.key)
            .addUIClass((this.value.hasOwnProperty(FORM.DROPDOWN_CODE.CODE) ? 'on' : 'none'))
            .setUIAttribute('data-type', FORM.DROPDOWN_CODE.DEFAULT_CODE)
            .setUIValue(this.value[FORM.DROPDOWN_CODE.DEFAULT_CODE])
            .setUIAttribute('data-validation-min-length', this.validation.minLength)
            .setUIAttribute('data-validation-max-length', this.validation.maxLength)
            .setUIReadOnly(!this.isEditable)
            .onUIKeyUp(this.updateProperty.bind(this))
            .onUIChange(this.updateProperty.bind(this));
        this.UIElement.addUI(this.UIElement.UIInput);

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
        this.panel.update.call(this.panel, e.target.id, JSON.stringify(this.getPropertyValue(e.type, e.target)));
    }

    /**
     * 속성 엘리먼트의 값 조회
     * @param evtType 이벤트 타입
     * @param e 이벤트객체
     */
    getPropertyValue(evtType, element) {
        const property = (evtType === 'click') ? element.parentNode.parentNode : element.parentNode;
        const selectedButton = property.querySelector('.selected');
        const firstInput = property.querySelector('input[type=text]');
        const defaultCodeLabel = property.querySelector('.default-code-label');
        const defaultCode = property.querySelector('input[data-type="'+ FORM.DROPDOWN_CODE.DEFAULT_CODE +'"]');
        let elementDataType = selectedButton.getAttribute('data-type');
        let tempValue = {};
        switch (evtType) {
            case 'keyup': // input
            case 'change':
                if (zValidation.isEmpty(element.getAttribute('data-type'))) {
                    tempValue[elementDataType] = element.value;
                    if (elementDataType === FORM.DROPDOWN_CODE.CODE) {
                        tempValue[FORM.DROPDOWN_CODE.DEFAULT_CODE] = defaultCode.value;
                    }
                } else {
                    tempValue[elementDataType] = firstInput.value;
                    tempValue[element.getAttribute('data-type')] = element.value;
                }
                break;
            case 'click': // button
                // 버튼 토글
                selectedButton.classList.remove('selected');
                element.classList.add('selected');
                elementDataType = element.getAttribute('data-type');

                // 값 초기화
                firstInput.value = '';
                tempValue[elementDataType] = '';

                // 기본값 코드 숨기기
                defaultCode.value = '';
                if (elementDataType === FORM.DROPDOWN_CODE.CODE) {
                    tempValue[FORM.DROPDOWN_CODE.DEFAULT_CODE] = ''; // 초기값 할당
                    
                    defaultCodeLabel.classList.remove('none');
                    defaultCode.classList.remove('none');
                    defaultCodeLabel.classList.add('on');
                    defaultCode.classList.add('on');
                } else {
                    defaultCodeLabel.classList.remove('on');
                    defaultCode.classList.remove('on');
                    defaultCodeLabel.classList.add('none');
                    defaultCode.classList.add('none');
                }
                break;
            default:
                break;
        }
        return tempValue;
    }
}
