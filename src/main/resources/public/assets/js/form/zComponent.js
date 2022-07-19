/**
 * Component 공통 Class
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import { FORM, UNIT } from '../lib/zConstants.js';
import * as mixin from '../lib/zMixins.js';
import { UIDiv, UILabel, UILi, UISpan } from '../lib/zUI.js';
import { checkBoxMixin } from './component/zCheckBox.js';
import { ciMixin } from './component/zCI.js';
import { customCodeMixin } from './component/zCustomCode.js';
import { dateMixin } from './component/zDate.js';
import { dataTimeMixin } from './component/zDateTime.js';
import { dividerMixin } from './component/zDivider.js';
import { dropdownMixin } from './component/zDropdown.js';
import { dynamicRowTableMixin } from './component/zDynamicRowTable.js';
import { fileUploadMixin } from './component/zFileUpload.js';
import { imageMixin } from './component/zImage.js';
import { inputBoxMixin } from './component/zInputBox.js';
import { labelMixin } from './component/zLabel.js';
import { radioMixin } from './component/zRadio.js';
import { textAreaMixin } from './component/zTextArea.js';
import { textEditorMixin } from './component/zTextEditor.js';
import { timeMixin } from './component/zTime.js';
import { fileDownloadMixin } from './component/zFileDownload.js';
import { dropdownCodeMixin } from './component/zDropdownCode.js';
import { userSearchMixin } from './component/zUserSearch.js';
import { organizationSearchMixin } from './component/zOrganizationSearch.js';
import { modalButtonMixin } from './component/zModalButton.js';
import { zValidation } from '../lib/zValidation.js';

const DEFAULT_PROPERTY = {
    label: {
        position: 'left', // 라벨 위치 hidden | top | left
        fontSize: '14',
        fontColor: '#8d9299',
        bold: false,
        italic: false,
        underline: false,
        align: 'left',
        text: 'COMPONENT LABEL'
    },
    display: {
        displayOrder: 0,     // 표시 순서
        columnWidth: '12'
    }
};
Object.freeze(DEFAULT_PROPERTY);

export default class ZComponent {
    constructor(data = {}) {
        this.parent = null;        // 부모 객체
        this.children = [];        // 자식 객체
        this.data = data;
        this._type = data.type || 'component';
        this._id = data.id || ZWorkflowUtil.generateUUID();
        this._isTopic = data.isTopic || false;
        this._mapId = data.mapId || '';
        this._tags = data.tags || [];
        this._displayType = data.displayType || FORM.DISPLAY_TYPE.EDITABLE;
        this._display = Object.assign({}, DEFAULT_PROPERTY.display, data.display);
        this._label = Object.assign({}, DEFAULT_PROPERTY.label, data.label);
        this._propertyName = 'form.component.' + data.type || ''; // i18n message name

        // 라벨 위치 초기화
        if (!Object.prototype.hasOwnProperty.call(this.data, 'label')) {
            this.initLabelPosition(this._type);
        }

        // Control Mixin import
        aliceJs.importMixin(this, mixin.controlMixin);
        // 타입에 따른 Mixin import
        aliceJs.importMixin(this, this.getMixinByType(this._type));
        // Tooltip Mixin import
        aliceJs.importMixin(this, mixin.toolTipMenuMixin);

        this.init();
    }

    // 초기화
    init() {
        // 내부 property 초기화
        this.initProperty();
        // 컴포넌트용 툴팁
        this.UIElement = new UIComponentTooltip()
            .setUIProperty('--data-column', this.displayColumnWidth);
        // 컴포넌트 추가
        this.UIElement.UIComponent = new UIComponent()
            .setUIId(this.id)
            .setUIAttribute('data-mappingId', this.mapId)
            .addUIClass(this.type);
        // 라벨 추가
        this.UIElement.UIComponent.UILabel = this.makeLabel();
        this.UIElement.UIComponent.addUI(this.UIElement.UIComponent.UILabel);

        // 엘리먼트 추가
        this.UIElement.UIComponent.UIElement = this.makeElement();
        this.UIElement.UIComponent.addUI(this.UIElement.UIComponent.UIElement);

        this.UIElement.addUI(this.UIElement.UIComponent);
        // 툴팁
        this.UIElement.UITooltipMenu = this.makeTooltip();

        // 툴팁에 create template 버튼 추가
        const tooltipUl = this.UIElement.UITooltipMenu.UIUl;
        tooltipUl.UILiCreateTemplate = new UILi().setUIClass('tooltip-menu-item')
            .addUIClass('palette-tooltip')
            .setUIAttribute('data-action', 'createTemplate')
            .addUI(new UISpan().setUIClass('icon').addUIClass('i-create-template'))
            // 컴포넌트 템플릿 등록 모달
            .onUIClick(this.openCreateTemplateModal.bind(this))
            .addUI(new UIDiv().setUIClass('palette-tooltip-contents')
                .addUI(new UISpan().setUIClass('palette-tooltip-text').setUITextContent(i18n.msg('tooltip.label.template')))
            );
        tooltipUl.domElement.insertBefore(tooltipUl.UILiCreateTemplate.domElement, tooltipUl.domElement.firstChild);

        this.UIElement.addUI(this.UIElement.UITooltipMenu);
    }

    // 라벨 위치 초기화
    initLabelPosition(type) {
        switch (type) {
            case 'textArea':
            case 'dynamicRowTable':
            case 'ci':
                // 라벨 상단
                this._label.position = FORM.LABEL.POSITION.TOP;
                break;
            case 'textEditor':
            case 'image':
            case 'divider':
            case 'fileUpload':
            case 'fileDownload':
            case 'modalButton':
                // 라벨 숨김
                this._label.position = FORM.LABEL.POSITION.HIDDEN;
                break;
            default:
                break;
        }
    }

    // 타입에 따른 믹스인 호출
    getMixinByType(type) {
        switch (type) {
            case 'inputBox':
                return inputBoxMixin;
            case 'textArea':
                return textAreaMixin;
            case 'textEditor':
                return textEditorMixin;
            case 'dropdown':
                return dropdownMixin;
            case 'radio':
                return radioMixin;
            case 'checkBox':
                return checkBoxMixin;
            case 'label':
                return labelMixin;
            case 'image':
                return imageMixin;
            case 'divider':
                return dividerMixin;
            case 'date':
                return dateMixin;
            case 'time':
                return timeMixin;
            case 'dateTime':
                return dataTimeMixin;
            case 'fileUpload':
                return fileUploadMixin;
            case 'customCode':
                return customCodeMixin;
            case 'dynamicRowTable':
                return dynamicRowTableMixin;
            case 'ci':
                return ciMixin;
            case 'fileDownload':
                return fileDownloadMixin;
            case 'dropdownCode':
                return dropdownCodeMixin;
            case 'userSearch':
                return userSearchMixin;
            case 'organizationSearch':
                return organizationSearchMixin;
            case 'modalButton':
                return modalButtonMixin;
            default:
                break;
        }
    }

    get type() {
        return this._type;
    }

    set type(type) {
        this._type = type;
    }

    get id() {
        return this._id;
    }

    set id(id) {
        this._id = id;
    }

    set mapId(mapId) {
        this._mapId = mapId;
        this.UIElement.UIComponent.setUIAttribute('data-mappingId', mapId);
    }

    get mapId() {
        return this._mapId;
    }

    set isTopic(boolean) {
        this._isTopic = boolean;
    }

    get isTopic() {
        return this._isTopic;
    }

    set tags(tags) {
        this._tags = tags;
    }

    get tags() {
        return this._tags;
    }

    get displayType() {
        return this._displayType;
    }

    set displayType(displayType) {
        this._displayType = displayType;
    }

    set display(display) {
        this._display = display;
    }

    get display() {
        return this._display;
    }

    set displayDisplayOrder(displayOrder) {
        this._display.displayOrder = displayOrder;
    }

    get displayDisplayOrder() {
        return this._display.displayOrder;
    }

    set displayColumnWidth(width) {
        this._display.columnWidth = width;
        this.UIElement.setUIProperty('--data-column', width);
    }

    get displayColumnWidth() {
        return this._display.columnWidth;
    }

    set label(label) {
        this._label = label;
    }

    get label() {
        return this._label;
    }

    set labelPosition(value) {
        this._label.position = value;
        if (value === FORM.LABEL.POSITION.HIDDEN) {
            this.UIElement.UIComponent.UILabel.removeUIClass('flex').addUIClass('none');
        } else {
            this.UIElement.UIComponent.UILabel.removeUIClass('none').addUIClass('flex');
        }
        this.UIElement.UIComponent.UILabel.setUIProperty('--data-column', this.getLabelColumnWidth(value));
    }

    get labelPosition() {
        return this._label.position;
    }

    set labelFontColor(color) {
        this._label.fontColor = color;
        this.UIElement.UIComponent.UILabel.UILabelText.setUIColor(color);
    }

    get labelFontColor() {
        return this._label.fontColor;
    }

    set labelFontSize(size) {
        if (size > FORM.LABEL.SIZE.MAX || size < FORM.LABEL.SIZE.MIN) {
            return false;
        }
        this._label.fontSize = size;
        this.UIElement.UIComponent.UILabel.UILabelText
            .setUIFontSize(size + UNIT.PX);
    }

    get labelFontSize() {
        return this._label.fontSize;
    }

    set labelAlign(value) {
        this._label.align = value;
        this.UIElement.UIComponent.UILabel.setUITextAlign(value);
        this.UIElement.UIComponent.UILabel.setUIProperty('--data-align', this.getLabelAlign(value));
    }

    get labelAlign() {
        return this._label.align;
    }

    set labelFontOptionBold(boolean) {
        this._label.bold = boolean;
        this.UIElement.UIComponent.UILabel.UILabelText
            .setUIFontWeight((boolean ? 'bold' : ''));
    }

    get labelFontOptionBold() {
        return this._label.bold;
    }

    set labelFontOptionItalic(boolean) {
        this._label.italic = boolean;
        this.UIElement.UIComponent.UILabel.UILabelText
            .setUIFontStyle((boolean ? 'italic' : ''));
    }

    get labelFontOptionItalic() {
        return this._label.italic;
    }

    set labelFontOptionUnderline(boolean) {
        this._label.underline = boolean;
        this.UIElement.UIComponent.UILabel.UILabelText
            .setUITextDecoration((boolean ? 'underline' : ''));
    }

    get labelFontOptionUnderline() {
        return this._label.underline;
    }

    set labelText(text) {
        this._label.text = text;
        this.UIElement.UIComponent.UILabel.UILabelText.setUITextContent(text);
    }

    get labelText() {
        return this._label.text;
    }

    set propertyName(name) {
        this._propertyName = name;
    }

    get propertyName() {
        return this._propertyName;
    }

    /**
     * 라벨 DOM 객체 생성
     */
    makeLabel() {
        const label = new UILabel().setUIClass('component-label')
            .addUIClass((this.labelPosition === FORM.LABEL.POSITION.HIDDEN ? 'none' : 'flex'))
            .setUICSSText(`text-align: ${this.labelAlign};`)
            .setUIProperty('--data-align', this.getLabelAlign(this.labelAlign))
            .setUIProperty('--data-column', this.getLabelColumnWidth(this.labelPosition));
        // 라벨 문구
        const labelCssText = `color:${this.labelFontColor};` +
            `font-size:${this.labelFontSize + UNIT.PX};` +
            `${this.labelFontOptionBold ? 'font-weight:bold;' : ''}` +
            `${this.labelFontOptionItalic ? 'font-style:italic;' : ''}` +
            `${this.labelFontOptionItalic ? 'text-decoration:underline;' : ''}`;

        label.UILabelText = new UISpan().setUIClass('component-label-text')
            .setUICSSText(labelCssText)
            .setUITextContent(this.labelText);
        label.addUI(label.UILabelText);
        // 필수 여부
        label.UIRequiredText = new UISpan().setUIClass('required')
            .addUIClass((this.validationRequired ? 'inline' : 'none'));
        label.addUI(label.UIRequiredText);

        return label;
    }

    /**
     * 라벨 너비 계산
     * @param position 위치
     */
    getLabelColumnWidth(position) {
        let labelColumnWidth = FORM.COLUMN; // 12
        if (position === FORM.LABEL.POSITION.HIDDEN) {
            labelColumnWidth = 0;
        } else if (position === FORM.LABEL.POSITION.LEFT) {
            labelColumnWidth -= Number(this.elementColumnWidth);
        }
        return labelColumnWidth;
    }

    /**
     * 라벨 정렬
     */
    getLabelAlign(align) {
        switch (align) {
            case 'left':
                return 'flex-start';
            case 'center':
                return 'center';
            case 'right':
                return 'flex-end';
        }
    }

    /**
     * component 객체 템플릿 등록 modal
     */
    openCreateTemplateModal() {
        let _this = this;
        const templateModal = new modal({
            title: i18n.msg('form.label.registerTemplate'),
            body: `<div class="flex-column">` +
                `<label class="field-label" for="templateName">` +
                `${i18n.msg('form.label.templateName')}<span class="required"></span>` +
                `</label>` +
                `<input type="text" id="templateName" class="input" maxlength="50">` +
                `</div>`,
            classes: 'create-template',
            buttons: [{
                content: i18n.msg('common.btn.register'),
                classes: 'btn__text--box btn--primary',
                bindKey: false,
                callback: function (modal) {
                    if (zValidation.isEmpty(document.getElementById('templateName').value)) {
                        zAlert.warning(i18n.msg('form.msg.templateNameRequired'));
                        return false;
                    }

                    // 원본 컴포넌트 정보 (component id 제거)
                    const orgData = _this.toJson();
                    delete orgData.id;
                    // component template data
                    const templateData = {
                        'templateName': document.getElementById('templateName').value,
                        'type': _this.type,
                        'data': JSON.stringify(orgData) // 선택된 컴포넌트의 실시간 설정 데이터
                    };
                    aliceJs.fetchJson('/rest/forms/component/template', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(templateData),
                        showProgressbar: true
                    }).then((response) => {
                        switch (response.status) {
                            case aliceJs.response.success:
                                zAlert.success(i18n.msg('common.msg.register'), () => {
                                    // 컴포넌트 팔레트 새로고침
                                    zFormDesigner.initComponentTemplatePalette();
                                    modal.hide();
                                });
                                break;
                            case aliceJs.response.duplicate:
                                zAlert.warning(i18n.msg('form.msg.duplicateTemplateName'));
                                break;
                            case aliceJs.response.error:
                                zAlert.danger(i18n.msg('common.msg.fail'));
                                break;
                            default:
                                break;
                        }
                    });
                }
            }, {
                content: i18n.msg('common.btn.cancel'),
                classes: 'button secondary',
                bindKey: false,
                callback: function (modal) {
                    modal.hide();
                }
            }],
            close: {
                closable: false,
            },
            onCreate: function () {
            }
        });
        templateModal.show();
    }

    /**
     * 현재 객체를 대상이 되는 객체로 변경 (복사) 하여 반환한다
     * @param source 대상 객체
     * @param flag 객체의 키가 되는 id도 복제할지 여부 (true이면 id도 복제됨)
     */
    copy(source, flag) {
        this.parent = source.parent;
        this.children = source.children;
        this.data = source.data;
        this._type = source.type;
        this._display = source.display;
        this._isTopic = source.isTopic;
        this._mapId = source.mapId;
        this._tags = source.tags;
        this._value = source.value;
        this._label = source.label;
        this._propertyName = source.propertyName;
        if (flag) {
            this._id = source.id;
        }

        this.init();

        return this;
    }
}

export class UIComponentTooltip extends UIDiv {
    constructor() {
        super();
        this.domElement.className = 'component-tooltip';
    }
}

export class UIComponent extends UIDiv {
    constructor() {
        super();
        this.domElement.className = 'component';
        this.domElement.tabIndex = 0;
    }
}
