/**
 * Modal Button Mixin
 *
 * @author jy.lim <jy.lim@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import ZColumnProperty, { propertyExtends } from '../../formDesigner/property/type/zColumnProperty.js';
import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZInputBoxProperty from '../../formDesigner/property/type/zInputBoxProperty.js';
import ZLabelProperty from '../../formDesigner/property/type/zLabelProperty.js';
import ZSwitchProperty from '../../formDesigner/property/type/zSwitchProperty.js';
import { FORM, UNIT } from '../../lib/zConstants.js';
import { UIButton, UIDiv, UISpan } from '../../lib/zUI.js';
import { zValidation } from '../../lib/zValidation.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '12',
        fields: [{
            ...propertyExtends.fieldCommon
        }],
        text: i18n.msg('common.btn.inquiry'),
        size: {
            w: 960,
            h: 800
        },
        sort: {
            field: '',
            order: FORM.FIELD_ORDER_BY.ASC
        },
        table: '',
        keyField: ''
    },
    validation: {
        required: false, // 필수값 여부
    },
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const modalButtonMixin = {
    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
        this._validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.data.validation);
        this._value = this.data.value || '';
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv()
            .setUIClass('z-element')
            .addUIClass('align-right')
            .setUIProperty('--data-column', this.elementColumnWidth);
        element.UIButton = new UIButton()
            .setUIClass('z-button')
            .addUIClass('z-modal-button')
            .addUIClass('secondary')
            .onUIClick(this.openModal.bind(this));
        element.UIButton.UIText = new UISpan()
            .setUIId('elementText')
            .addUIClass('z-modal-button-text')
            .addUIClass('text-ellipsis')
            .setUITextContent(this.elementText)
        element.UIButton.addUI(element.UIButton.UIText);
        element.addUI(element.UIButton);
        return element;
    },
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {},
    // set, get
    set elementColumnWidth(width) {
        this._element.columnWidth = width;
        this.UIElement.UIComponent.UIElement.setUIProperty('--data-column', width);
        this.UIElement.UIComponent.UILabel.setUIProperty('--data-column', this.getLabelColumnWidth(this.labelPosition));
    },
    get elementColumnWidth() {
        return this._element.columnWidth;
    },
    set element(element) {
        this._element = element;
    },
    get element() {
        return this._element;
    },
    set elementText(value) {
        this._element.text = value;
        this.UIElement.UIComponent.UIElement.UIButton.UIText.setUITextContent(value);
    },
    get elementText() {
        return this._element.text;
    },
    set elementSizeW(value) {
        this._element.size.w = value;
    },
    get elementSizeW() {
        return this._element.size.w;
    },
    set elementSizeH(value) {
        this._element.size.h = value;
    },
    get elementSizeH() {
        return this._element.size.h;
    },
    set elementTable(value) {
        this._element.table = value;
    },
    get elementTable() {
        return this._element.table;
    },
    set elementFields(columns) {
        this._element.fields = columns;
    },
    get elementFields() {
        return this._element.fields;
    },
    set elementKeyField(value) {
        this._element.keyField = value;
    },
    get elementKeyField() {
        return this._element.keyField;
    },
    set elementSortField(value) {
        this._element.sort.field = value;
    },
    get elementSortField() {
        return this._element.sort.field;
    },
    set elementSortOrder(value) {
        this._element.sort.order = value ? FORM.FIELD_ORDER_BY.ASC : FORM.FIELD_ORDER_BY.DESC;
    },
    get elementSortOrder() {
        return (this._element.sort.order === FORM.FIELD_ORDER_BY.ASC);
    },
    set validation(validation) {
        this._validation = validation;
    },
    get validation() {
        return this._validation;
    },
    // 기본 값 변경
    set value(value) {
        this._value = value;
    },
    // 기본 값 조회
    get value() {
        return this._value;
    },
    // 모달 화면 내 grid 구성
    drawGridTemplate(data) {
        const field = data.fields;
        const rowData = data.data[0] || '';
        // 전체 column
        let totalColumn = 0;
        field.filter(it => totalColumn += Number(it.width));
        // grid 관련 변수 초기화
        let columnsWidth = '';
        let gridHead = '';
        let gridBodyRow = '';
        let gridBody = '';

        field.map((item) => {
            // 그리드 구성을 위한 width
            columnsWidth += `${Number(item.width) + UNIT.PX} `;
            // table header template
            gridHead += `<div class="grid__cell pr-2 pl-2" data-grid-sortable="false">` +
                `<span class="text-ellipsis" title="${item.alias}">${item.alias}</span>` +
                `</div>`;
        });

        // table body template
        if (rowData.length > 0) {
            rowData.map((row) => {
                gridBody = '';
                row.forEach((item) => {
                    gridBody += `<div class="grid__cell pr-2 pl-2">` +
                        `<span class="text-ellipsis" title="${item}">${item}</span>` +
                        `</div>`;
                });
                gridBodyRow += `<div class="grid__row">${gridBody}</div>`;
            });
        } else {
            // noData
            gridBodyRow = `<div class="grid__row grid--noData read-only">` +
                `<div class="grid__cell"><span>${i18n.msg('common.msg.noData')}</span></div>` +
                `</div>`;
        }

        // modal template
        return `<div class="grid" style="--data-columns-width: ${columnsWidth}">` +
            `<div class="grid__head">` +
            `<div class="grid__row">${gridHead}</div>` +
            `</div>` +
            `<div class="grid__body">${gridBodyRow}</div>` +
            `</div>`;
    },
    // 설정된 데이터에 따라 모달 생성
    openModal() {
        const documentNo = document.getElementById('documentNumber')?.getAttribute('document-no') || '';
        const componentId = this.id;
        const strUrl = `/rest/documents/components/${componentId}/value?documentNo=${documentNo}`;

        aliceJs.fetchJson(strUrl, {
            method: 'GET'
        }).then((data) => {
            const targetTableModal = new modal({
                title: this.elementText,
                body: this.drawGridTemplate(data.data),
                classes: 'modal-button-search-modal',
                buttons: [{
                    content: i18n.msg('common.btn.close'),
                    classes: 'z-button secondary',
                    bindKey: false,
                    callback: (modal) => modal.hide()
                }],
                close: {
                    closable: false,
                },
                onCreate: () => {
                    // 컴포넌트 설정에 따라 모달 크기 리사이즈
                    const modalContent = document.querySelector('.modal-button-search-modal');
                    modalContent.style.setProperty('--default-modal-width', this.elementSizeW + UNIT.PX);
                    modalContent.style.setProperty('--default-modal-height', this.elementSizeH + UNIT.PX);

                    // 모달 내부 스크롤 바 추가
                    OverlayScrollbars(document.querySelector('.modal-content'), {className: 'scrollbar'});
                }
            });
            targetTableModal.show();
        });
    },
    // 세부 속성 조회
    getProperty() {
        // element - 모달 크기
        const modalWidthProperty = new ZInputBoxProperty('elementSizeW', 'element.modalWidth', this.elementSizeW)
            .setValidation(false, 'number', '400', '1720', '', '');
        modalWidthProperty.columnWidth = '6';
        modalWidthProperty.unit = UNIT.PX;
        const modalHeightProperty = new ZInputBoxProperty('elementSizeH', 'element.modalHeight', this.elementSizeH)
            .setValidation(false, 'number', '120', '800', '', '');
        modalHeightProperty.columnWidth = '6';
        modalHeightProperty.unit = UNIT.PX;

        // 조회 대상 테이블
        const targetTableProperty = new ZInputBoxProperty('elementTable', 'element.searchTargetTable', this.elementTable)
            .setValidation(true, '', '', '', '', '');
        targetTableProperty.help = 'form.help.target-table';

        /**
         * @date 2022-04-13
         * @author jy.lim
         * @summary 이력 조회 외에도 특정 테이블 내역 조회를 위해 keyField 속성이 추가되었습니다. (레드마인 #12830 참고)
         */
        // 조회 대상 기준 필드
        const keyFieldProperty = new ZInputBoxProperty('elementKeyField', 'element.searchTargetField', this.elementKeyField)
            .setValidation(true, '', '', '', '', '');

        // orderBy 정렬 조건
        const orderTableProperty = new ZInputBoxProperty('elementSortField', 'element.orderByCondition', this.elementSortField);
        orderTableProperty.help = 'form.help.order-table';

        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZInputBoxProperty('elementText', 'element.buttonText', this.elementText)
                    .setValidation(true, '', '', '', '', ''))
                .addProperty(modalWidthProperty)
                .addProperty(modalHeightProperty),
            new ZGroupProperty('group.display')
                .addProperty(targetTableProperty)
                .addProperty(keyFieldProperty)
                .addProperty(new ZColumnProperty('elementFields', '', FORM.COLUMN_PROPERTY.FIELD, this.elementFields))
                .addProperty(orderTableProperty)
                .addProperty(new ZSwitchProperty('elementSortOrder', 'element.orderByAsc', this.elementSortOrder))
        ];
    },
    // json 데이터 추출 (서버에 전달되는 json 데이터)
    toJson() {
        return {
            id: this._id,
            type: this._type,
            display: this._display,
            displayType: this._displayType,
            isTopic: this._isTopic,
            mapId: this._mapId,
            tags: this._tags,
            value: this._value,
            label: this._label,
            element: this._element,
            validation: this._validation
        };
    },
    // 발행을 위한 validation 체크
    validationCheckOnPublish() {
        // 모달 테이블 속성 중 필드가 누락되었을 때
        const modalTableOption = this.elementFields;
        for (let i = 0; i < modalTableOption.length; i++) {
            if (zValidation.isEmpty(modalTableOption[i].name)) {
                zAlert.warning(i18n.msg('form.msg.modalTableRequired', i + 1));
                return false;
            }
        }
        return true;
    }
};
