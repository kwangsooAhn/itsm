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

export default class ZFileProperty extends ZProperty {
    constructor(key, name, value, type = 'image', isAlwaysEditable) {
        super(key, name, 'imageProperty', value, isAlwaysEditable);
        this.type = type;
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

        // input box + button
        this.UIElement.UIDiv = new UIDiv().setUIClass('input--remove').addUIClass('flex-row');

        // input box
        this.UIElement.UIDiv.UIInput = new UIInput()
            .setUIId(this.key).setUIValue(this.value)
            .setUIReadOnly(true)
            .onUIFocusout(this.updateProperty.bind(this));
        this.UIElement.UIDiv.addUI(this.UIElement.UIDiv.UIInput);

        // button
        this.UIElement.UIDiv.UIButton = new UIButton().setUIClass('btn__ic').addUIClass('button-attach-file')
            .setUIId('fileUploadPopUp')
            .setUIDisabled(!this.isEditable)
            .onUIClick(this.openThumbnailModal.bind(this));
        this.UIElement.UIDiv.UIButton.addUI(new UISpan().setUIClass('ic-folder'));
        this.UIElement.UIDiv.addUI(this.UIElement.UIDiv.UIButton);

        this.UIElement.addUI(this.UIElement.UIDiv);

        return this.UIElement;
    }

    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {}

    // 속성 변경시 발생하는 이벤트 핸들러
    updateProperty(e) {
        e.stopPropagation();
        e.preventDefault();

        this.panel.update.call(this.panel, e.target.id, e.target.value);
    }

    openThumbnailModal() {
        aliceJs.thumbnail({
            title: i18n.msg('resource.label.popupTitle'),
            targetId: this.key,
            type: this.type,
            isThumbnailInfo: true,
            isFilePrefix: true,
            thumbnailDoubleClickUse: true,
            selectedPath: this.value
        });
    }
}
