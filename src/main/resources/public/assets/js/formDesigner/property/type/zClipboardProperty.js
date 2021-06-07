/**
 * Clipboard Property Class
 *
 * 기본적으로는 input type 과 동일한 속성 입력을 지원하지만
 * 속성 값을 입력하는 항목 옆에 값을 복사하는 아이콘이 있는 형태이다.
 *
 * @author Jung Hee Chan <hcjung@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import ZProperty from '../zProperty.js';
import { UIClipboard, UIDiv } from '../../../lib/ui.js';

const propertyExtends = {
    /* 추가적인 설정이 없다. */
};

export default class ZClipboardProperty extends ZProperty {
    constructor(name, value) {
        super(name, 'clipboardProperty', value);
    }

    // DOM Element 생성
    makeProperty(panel) {
        this.panel = panel;

        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);
        // 클립보드
        this.UIElement.UIClipboard = new UIClipboard();
        this.UIElement.UIClipboard.UIInput.setUIValue(this.value);
        this.UIElement.addUI(this.UIElement.UIClipboard);
        return this.UIElement;
    }
}