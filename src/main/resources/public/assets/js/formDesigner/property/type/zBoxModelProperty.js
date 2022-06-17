/**
 * Box Model Property Class
 *
 * 기본적으로는 input type 과 동일한 속성 입력을 지원하지만 (그런면에서 input 타입과 유사하지만)
 * 상하좌우와 같이 복수개의 데이터를 한 묶음으로 처리하는 클래스이다.
 *
 * @author Jung Hee Chan <hcjung@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import { zValidation } from '../../../lib/zValidation.js';
import ZProperty from '../zProperty.js';
import { UIDiv, UIInput } from '../../../lib/zUI.js';

const propertyExtends = {
    /* 추가적인 설정이 없다. */
};

export default class ZBoxModelProperty extends ZProperty {
    constructor(key, name, value, isAlwaysEditable) {
        super(key, name, 'boxModelProperty', value, isAlwaysEditable);
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

        this.UIElement.UIBox = new UIDiv().setUIClass('box-model');
        this.UIElement.addUI(this.UIElement.UIBox);

        // inputbox : top, right, bottom, left 박스 모델
        const boxValueArray = this.value.split(' ');
        ['Top', 'Left', 'Bottom', 'Right'].forEach((item, index) => {
            this.UIElement.UIBox['UIInput' + item] = new UIInput()
                .setUIId(this.key + item)
                .setUIValue(boxValueArray[index])
                .setUIReadOnly(!this.isEditable)
                .setUIAttribute('data-validation-required', this.validation.required)
                .setUIAttribute('data-validation-required-name', i18n.msg(this.name))
                .setUIAttribute('data-validation-type', this.validation.type)
                .setUIAttribute('data-validation-min', this.validation.min)
                .setUIAttribute('data-validation-max', this.validation.max)
                .setUIAttribute('data-validation-min-length', this.validation.minLength)
                .setUIAttribute('data-validation-max-length', this.validation.maxLength)
                .onUIKeyUp(this.updateProperty.bind(this))
                .onUIChange(this.updateProperty.bind(this));
            // 단위 추가
            if (this.unit !== '') {
                this.UIElement.UIBox['UIInput' + item].addUIClass('i-unit-' + this.unit);
            }
            this.UIElement.UIBox.addUI(this.UIElement.UIBox['UIInput' + item]);
        });
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
        if (e.type === 'keyup' && (e.keyCode === 13 || e.keyCode === 9)) {
            return false;
        }
        // keyup 일 경우 type, min, max 체크
        if (e.type === 'keyup' && !zValidation.keyUpValidationCheck(e.target)) {
            this.panel.validationStatus = false; // 유효성 검증 실패
            return false;
        }
        // change 일 경우 minLength, maxLength 체크
        if (e.type === 'change' && !zValidation.changeValidationCheck(e.target)) {
            this.panel.validationStatus = false; // 유효성 검증 실패
            return false;
        }
        this.panel.update.call(this.panel, e.target.id, e.target.value);
    }
}
