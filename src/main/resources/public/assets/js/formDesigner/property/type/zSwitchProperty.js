/**
 * Switch Property Class
 *
 * 속성을 선택하는 아이콘들 중에서 1개만 선택할 수 있는 속성 타입을 위한 클래스이다.
 * 예를 들어 정렬 속성 같은 경우, 좌측, 중앙, 우측 정렬중 1개만 선택이 가능하다.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import { UIDiv, UISpan, UISwitch } from '../../../lib/zUI.js';
import { zValidation } from '../../../lib/zValidation.js';
import ZProperty from '../zProperty.js';

const propertyExtends = {
    /* 슬라이드 속성 타입은 추가적인 설정이 없다. */
};

export default class ZSwitchProperty extends ZProperty {
    constructor(key, name, value, isAlwaysEditable) {
        super(key, name, 'switchProperty', value, isAlwaysEditable);
    }
    // DOM Element 생성
    makeProperty(panel) {
        this.panel = panel;
        // 속성 편집 가능여부 체크 - 문서가 '편집'이거나 또는 (문서가 '사용/발행' 이고 항시 편집 가능한 경우) 또는 (문서가 '사용/발행' 이고 연결된 업무흐름이 없는 경우)
        this.isEditable = this.panel.editor.isEditable || (!this.panel.editor.isDestory && this.isAlwaysEditable);

        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        this.UIElement.UISwitch = new UISwitch(this.value)
            .setUIId(this.key)
            .setUITextContent(i18n.msg(this.name))
            .setUIDisabled(!this.isEditable);
        this.UIElement.UISwitch.UISpan.addUIClass('property-label');
        // 툴팁(도움말) 기능 추가
        if (this.help !== '') {
            this.UIElement.UISwitch.UITooltip = new UIDiv().setUIClass('help-tooltip');
            this.UIElement.UISwitch.UITooltip.addUI(new UISpan().setUIClass('icon').addUIClass('i-tooltip'));
            this.UIElement.UISwitch.UITooltip.UIContent = new UIDiv().setUIClass('tooltip-contents');
            this.UIElement.UISwitch.UITooltip.UIContent.addUI(new UISpan().setUIInnerHTML(i18n.msg(this.help)));
            this.UIElement.UISwitch.UITooltip.addUI(this.UIElement.UISwitch.UITooltip.UIContent);
            this.UIElement.UISwitch.addUI(this.UIElement.UISwitch.UITooltip);
        }
        this.UIElement.UISwitch.UICheckbox.onUIChange(this.updateProperty.bind(this));
        this.UIElement.addUI(this.UIElement.UISwitch);

        return this.UIElement;
    }

    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {}

    // 속성 변경시 발생하는 이벤트 핸들러
    updateProperty(e) {
        e.stopPropagation();
        e.preventDefault();

        this.panel.validationStatus = true;

        // change 일 경우 minLength, maxLength 체크
        if (e.type === 'change' && !zValidation.changeValidationCheck(e.target)) {
            this.panel.validationStatus = false; // 유효성 검증 실패
            return false;
        }
        this.panel.update.call(this.panel, e.target.id, e.target.checked);
    }
}
