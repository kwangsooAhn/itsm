/**
 * Image Property Class
 *
 * 이미지를 선택하는 속성항목을 위한 클래스이다.
 *
 * @author Woo Da Jung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import ZProperty from '../zProperty.js';
import { UIButton, UIDiv, UIInput, UISpan } from '../../../lib/zUI.js';

const propertyExtends = {
    /* 추가적인 설정이 없다. */
};

export default class ZImageProperty extends ZProperty {
    constructor(key, name, value) {
        super(key, name, 'imageProperty', value);
    }
    // DOM Element 생성
    makeProperty(panel) {
        this.panel = panel;

        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);

        // input box + button
        this.UIElement.UIDiv = new UIDiv().setUIClass('input-button').addUIClass('flex-row');

        // input box
        this.UIElement.UIDiv.UIInput = new UIInput()
            .setUIId(this.key).setUIValue(this.value)
            .setUIReadOnly(true)
            .onUIFocusout(this.updateProperty.bind(this));
        this.UIElement.UIDiv.addUI(this.UIElement.UIDiv.UIInput);

        // button
        this.UIElement.UIDiv.UIButton = new UIButton().addUIClass('ghost-line')
            .setUIId('imageUploadPopUp')
            .onUIClick(this.openThumbnailModal.bind(this));
        this.UIElement.UIDiv.UIButton.addUI(new UISpan().setUIClass('icon').addUIClass('icon-search'));
        this.UIElement.UIDiv.addUI(this.UIElement.UIDiv.UIButton);

        this.UIElement.addUI(this.UIElement.UIDiv);

        return this.UIElement;
    }
    // 속성 변경시 발생하는 이벤트 핸들러
    updateProperty(e) {
        e.stopPropagation();
        e.preventDefault();

        this.panel.update.call(this.panel, e.target.id, e.target.value);
    }

    openThumbnailModal() {
        aliceJs.thumbnail({
            title: i18n.msg('image.label.popupTitle'),
            targetId: this.key,
            type: 'image',
            isThumbnailInfo: true,
            isFilePrefix: true,
            thumbnailDoubleClickUse: true,
            selectedPath: this.value
        });
    }
}