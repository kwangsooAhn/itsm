/**
 * Image Mixin
 *
 *
 * @author
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import { FORM, CLASS_PREFIX, UNIT } from '../../lib/constants.js';
import { zValidation } from '../../lib/validation.js';
import {UIDiv, UIImg, UISpan} from '../../lib/ui.js';
import InputBoxProperty from '../../formDesigner/property/type/inputBoxProperty.module.js';
import GroupProperty from '../../formDesigner/property/type/groupProperty.module.js';
import SliderProperty from '../../formDesigner/property/type/sliderProperty.module.js';
import CommonProperty from '../../formDesigner/property/type/commonProperty.module.js';
import SwitchButtonProperty from '../../formDesigner/property/type/switchButtonProperty.module.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '10',
        path: '', // 이미지 경로
        width: '', // 이미지 너비
        height: '', // 이미지 높이
        align: 'left'
    },
    validation: {
        required: false // 필수값 여부
    }
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const imageMixin = {

    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
        this._validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.data.validation);
    },
    // component 엘리먼트 생성
    makeElement() {
        // label 숨김 처리
        this.labelPosition = FORM.LABEL.POSITION.HIDDEN;

        const element = new UIDiv().setUIClass(CLASS_PREFIX + 'element')
            .setUIProperty('--data-column', this.elementColumnWidth)
            .setUITextAlign(this.elementAlign);

        element.UIImg = new UIImg().setUIClass('imagebox').setUIId('imagebox' + this.id)
            .setUIAttribute('data-path', aliceJs.filterXSS(this.elementPath))
            .setUIWidth(this.elementWidth + UNIT.PX)
            .setUIHeight(this.elementHeight + UNIT.PX);
        element.addUI(element.UIImg);
        // path 추가
        if (!zValidation.isEmpty(this.elementPath)) {
            if (this.elementPath.startsWith('file:///')) {
                aliceJs.fetchJson('/rest/images/' + this.elementPath.split('file:///')[1], {
                    method: 'GET'
                }).then((imageData) => {
                    element.UIImg.setUISrc('data:image/' + imageData.extension + ';base64,' + imageData.data);
                });
            } else {
                element.UIImg.setUISrc(this.elementPath);
            }
        }
        // placeholder
        element.UIDiv = new UIDiv().setUIClass('imagebox-placeholder')
            .addUI(new UISpan().setUIClass('icon-no-image'))
            .addUI(new UISpan().setUIInnerHTML(i18n.msg('image.label.placeholder')));
        return element;
    },
    // set, get
    set element(element) {
        this._element = element;
    },
    get element() {
        return this._element;
    },
    set elementColumnWidth(width) {
        this._element.columnWidth = width;
        this.UIElement.UIComponent.UIElement.setUIProperty('--data-column', width);
        this.UIElement.UIComponent.UILabel.setUIProperty('--data-column',
            this.getLabelColumnWidth(this.labelPosition));
    },
    get elementColumnWidth() {
        return this._element.columnWidth;
    },
    set elementPath(path) {
        this._element.path = path;
        if (path.startsWith('file:///')) {
            aliceJs.fetchJson('/rest/images/' + path.split('file:///')[1], {
                method: 'GET'
            }).then((imageData) => {
                this.UIElement.UIComponent.UIElement.UIImg.setUISrc('data:image/' + imageData.extension + ';base64,' + imageData.data);
            });
        } else {
            this.UIElement.UIComponent.UIElement.UIImg.setUISrc(path);
        }
    },
    get elementPath() {
        return this._element.path;
    },
    set elementWidth(width) {
        this._element.width = width;
        this.UIElement.UIComponent.UIElement.UIImg.setUIWidth(width + UNIT.PX);
    },
    get elementWidth() {
        return this._element.width;
    },
    set elementHeight(height) {
        this._element.height = height;
        this.UIElement.UIComponent.UIElement.UIImg.setUIHeight(height + UNIT.PX);
    },
    get elementHeight() {
        return this._element.height;
    },
    set elementAlign(align) {
        this._element.align = align;
        this.UIElement.UIComponent.UIElement.setTextAlign(align);
    },
    get elementAlign() {
        return this._element.align;
    },
    set validation(validation) {
        this._validation = validation;
    },
    get validation() {
        return this._validation;
    },
    set validationRequired(boolean) {
        this._validation.required = boolean;
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIAttribute('data-validation-required', boolean);
        if (boolean) {
            this.UIElement.UIComponent.UIElement.UILabel.UIRequiredText.removeUIClass('off').addUIClass('on');
        } else {
            this.UIElement.UIComponent.UIElement.UILabel.UIRequiredText.removeUIClass('on').addUIClass('off');
        }
    },
    get validationRequired() {
        return this._validation.required;
    },
    set value(value) {
        this._value = value;
    },
    get value() {
        // this._value === '${default}' 일 경우, 신청서에서 변경되지 않은 값을 의미하므로 기본값을 표시한다.
        // 사용자 변경시 해당 값이 할당된다.
        return this._value;
    },
    // input box 값 변경시 이벤트 핸들러
    updateValue(e) {
        e.stopPropagation();
        e.preventDefault();
        // enter, tab 입력시
        if (e.type === 'keyup' && (e.keyCode === 13 || e.keyCode === 9)) {
            return false;
        }
        // 유효성 검증
        // keyup 일 경우 type, min, max 체크
        if (e.type === 'keyup' && !zValidation.keyUpValidationCheck(e.target)) {
            return false;
        }
        // change 일 경우 minLength, maxLength 체크
        if (e.type === 'change' && !zValidation.changeValidationCheck(e.target)) {
            return false;
        }

        this.value = e.target.value;
    },
    getProperty() {
        const elementWidthProperty = new InputBoxProperty('element.width', this.elementWidth)
            .setValidation(false, 'number', '0', '', '', '');
        elementWidthProperty.unit = UNIT.PX;

        const elementHeightProperty = new InputBoxProperty('element.height', this.elementWidth)
            .setValidation(false, 'number', '0', '', '', '');
        elementHeightProperty.unit = UNIT.PX;

        // label - align
        const elementAlignProperty = new SwitchButtonProperty('element.align', this.elementAlign, [
            { 'name': 'icon-align-left', 'value': 'left' },
            { 'name': 'icon-align-center', 'value': 'center' },
            { 'name': 'icon-align-right', 'value': 'right' }
        ]);

        return [
            ...new CommonProperty(this).getCommonProperty(),
            new GroupProperty('group.element')
                .addProperty(new SliderProperty('element.columnWidth', this.elementColumnWidth))
                //.addProperty(new ImageProperty('element.path', this.elementPlaceholder))
                .addProperty(elementWidthProperty)
                .addProperty(elementHeightProperty)
                .addProperty(elementAlignProperty)
        ];
    },
    // json 데이터 추출 (서버에 전달되는 json 데이터)
    toJson() {
        return {
            id: this._id,
            type: this._type,
            display: this._display,
            isTopic: this._isTopic,
            mapId: this._mapId,
            tags: this._tags,
            value: this._value,
            label: this._label,
            element: this._element,
            validation: this._validation
        };
    }
};