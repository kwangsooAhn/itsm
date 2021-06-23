/**
 * Dynamic Row Table Mixin
 *
 *
 * @author Woo Da Jung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import {FORM, CLASS_PREFIX, UNIT} from '../../lib/zConstants.js';
import { zValidation } from '../../lib/zValidation.js';
import {UIDiv, UIElement as UICell, UIElement as UIRow, UITable} from '../../lib/zUI.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZLabelProperty from '../../formDesigner/property/type/zLabelProperty.js';
import ZSwitchProperty from '../../formDesigner/property/type/zSwitchProperty.js';
import ZColumnProperty from '../../formDesigner/property/type/zColumnProperty.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '10',
        columns: []
    },
    validation: {
        required: false // 필수값 여부
    }
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const dynamicRowTableMixin = {

    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
        this._validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.data.validation);
        this._value = this.data.value || [];
    },
    // component 엘리먼트 생성
    makeElement() {
        // label 상단 처리
        this.labelPosition = FORM.LABEL.POSITION.TOP;

        const element = new UIDiv().setUIClass(CLASS_PREFIX + 'element').addUIClass('align-left')
            .setUIProperty('--data-column', this.elementColumnWidth);

        // 테이블
        return this.makeTable(element);
    },
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {},
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
    set elementColumns(columns) {
        this._element.columns = columns;
        this.UIElement.UIComponent.UIElement.clearUI();
        this.makeTable(this.UIElement.UIComponent.UIElement);
    },
    get elementColumns() {
        return this._element.columns;
    },
    set validation(validation) {
        this._validation = validation;
    },
    get validation() {
        return this._validation;
    },
    set validationRequired(boolean) {
        this._validation.required = boolean;
        this.UIElement.UIComponent.UIElement.UITable.setUIAttribute('data-validation-required', boolean);
        if (boolean) {
            this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('off').addUIClass('on');
        } else {
            this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('on').addUIClass('off');
        }
    },
    get validationRequired() {
        return this._validation.required;
    },
    set value(value) {
        this._value = value;
    },
    get value() {
        return this._value;
    },
    makeTable(object) {
        // 테이블
        object.UITable = new UITable()
            .setUIClass(CLASS_PREFIX + 'dr-table')
            .addUIClass('mt-2')
            .setUIId('drTable' + this.id)
            .setUIAttribute('data-validation-required', this.validationRequired);
        
        // 테이블 제목
        const row = new UIRow(object.UITable).setUIClass(CLASS_PREFIX + 'dr-table-header');
        object.UITable.addUIRow(row);
        
        this.elementColumns.forEach((column) => {
            const tdWidth = (Number(column.columnWidth) / FORM.COLUMN) * 100;
            const tdCssText = `width:${tdWidth}%;` +
                `color:${column.columnHead.fontSize};` +
                `font-size:${column.columnHead.fontColor + UNIT.PX};` +
                `${column.columnHead.bold ? 'font-weight:bold;' : ''}` +
                `${column.columnHead.italic ? 'font-style:italic;' : ''}` +
                `${column.columnHead.underline ? 'text-decoration:underline;' : ''}`;
            const td = new UICell(row)
                .addUIClass('align-' + column.columnHead.align)
                .setUICSSText(tdCssText)
                .setUITextContent((column.columnName !== '' ? i18n.msg(column.columnName) : ''));
            row.addUICell(td);
        });

        if (Array.isArray(this.value) && this.value.length > 0) {
            this.value.forEach((rowData) => {
                this.addTableRow(object.UITable, rowData);
            });
        } else {
            this.setEmptyTable(object.UITable);
        }
        return object;
    },
    // 데이터가 없을때
    setEmptyTable(targetTable) {
        const row = new UIRow(targetTable).setUIClass('no-data-found-list');
        targetTable.addUIRow(row);

        const td = new UICell(row).setUIClass('on align-center first-column last-column')
            .setColspan(12)
            .setUITextContent(i18n.msg('common.msg.noData'));
        row.addUICell(td);
    },
    // 테이블 row 추가
    addTableRow(targetTable, data) {
        // 데이터가 없을 경우
        if (targetTable.rows.length === 2 && targetTable.rows[1].hasUIClass('no-data-found-list')) {
            this.removeTableRow(targetTable, 1);
        }
        // row 추가
        const row = new UIRow(targetTable).setUIClass(CLASS_PREFIX + 'dr-table-row');
        targetTable.addUIRow(row);
        // td 추가
        this.elementColumns.forEach((column) => {
            row.addUICell(this.getCellInColumnType(row, column, data));
        });
    },
    // TODO: column Type 에 따른 cell 반환
    getCellInColumnType(row, column, data) {

    },
    // 테이블 row 삭제
    removeTableRow(targetTable, rowIndex) {
        targetTable.removeUIRow(targetTable.rows[rowIndex]);

        const newValue = JSON.parse(JSON.stringify(this.value));
        newValue.splice(rowIndex - 1, 1);
        this.value = newValue;

        // 데이터가 존재하지 않으면 '데이터가 존재하지 않습니다 ' 문구 표시
        if (Array.isArray(this.value) && this.value.length === 0) {
            this.setEmptyTable(targetTable);
        }
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
        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZSliderProperty('elementColumnWidth', 'element.columnWidth', this.elementColumnWidth))
                .addProperty(new ZColumnProperty('elementColumns', 'element.columns', this.elementColumns)),
            new ZGroupProperty('group.validation')
                .addProperty(new ZSwitchProperty('validationRequired', 'validation.required', this.validationRequired))
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