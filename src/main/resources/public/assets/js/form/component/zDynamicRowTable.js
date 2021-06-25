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

import {FORM, CLASS_PREFIX, UNIT, CI} from '../../lib/zConstants.js';
import { zValidation } from '../../lib/zValidation.js';
import {UIDiv, UICell, UIRow, UIInput, UISpan, UITable, UIButton} from '../../lib/zUI.js';
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
        columnWidth: '12',
        columns: [{
            columnName: 'COLUMN',
            columnType: 'input', // input, dropdown, date, time, datetime, customCode 등 입력 유형
            columnWidth: '12', // 컬럼 너비
            columnHead: {
                fontSize: '14',
                fontColor: 'rgba(141, 146, 153, 1)',
                align: 'left',
                bold: true,
                italic: false,
                underline: false
            },
            columnContent: {
                fontSize: '14',
                fontColor: 'rgba(50, 50, 51, 1)',
                align: 'left',
                bold: true,
                italic: false,
                underline: false
            },
            columnElement: {  // 타입별 세부 속성
                placeholder: '',
                validationType: 'none', // none | char | num | numchar | email | phone 등 유효성
                minLength: '0',
                maxLength: '100'
            }
        }]
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
        // 데이터 : "value" : [{0: "1행 1열", 1: "1행 2열", 2:"", 3:"", 4:"" ...}, {0: "", 1: "", 2:"", 3:"", 4:"" ...} ... ]
        this._value = this.data.value || '';
        // 데이터 초기화
        if (this._value !== '') {
            this.value = JSON.parse(this._value);
        }
    },
    // component 엘리먼트 생성
    makeElement() {
        // label 상단 처리
        this.labelPosition = FORM.LABEL.POSITION.TOP;

        const element = new UIDiv().setUIClass(CLASS_PREFIX + 'element').addUIClass('align-left')
            .setUIProperty('--data-column', this.elementColumnWidth);

        // 테이블
        element.UITable = new UITable()
            .setUIClass(CLASS_PREFIX + 'dr-table')
            .addUIClass('mt-2')
            .setUIId('drTable' + this.id)
            .setUIAttribute('data-validation-required', this.validationRequired);
        element.addUI(element.UITable);

        this.makeTable(element.UITable);

        // 추가 버튼
        element.UIDiv = new UIDiv().setUIClass(CLASS_PREFIX + 'dr-table-button-group');
        element.addUI(element.UIDiv);

        element.UIDiv.addUIButton = new UIButton()
            .onUIClick(this.addTableRow.bind(this, element.UITable, {}))
            .addUI(new UISpan().addUIClass('icon').addUIClass('icon-plus'));
        element.UIDiv.addUI(element.UIDiv.addUIButton);

        return element;
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
        this.UIElement.UIComponent.UIElement.UITable.clearUIRow().clearUI();

        this.makeTable(this.UIElement.UIComponent.UIElement.UITable);
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
    makeTable(table) {
        // 테이블 제목
        const row = new UIRow(table).setUIClass(CLASS_PREFIX + 'dr-table-header');
        table.addUIRow(row);

        this.elementColumns.forEach((column) => {
            const tdWidth = (Number(column.columnWidth) / FORM.COLUMN) * 100;
            const tdCssText = `width:${tdWidth}%;` +
                `color:${column.columnHead.fontColor};` +
                `font-size:${column.columnHead.fontSize + UNIT.PX};` +
                `${column.columnHead.bold ? 'font-weight:bold;' : ''}` +
                `${column.columnHead.italic ? 'font-style:italic;' : ''}` +
                `${column.columnHead.underline ? 'text-decoration:underline;' : ''}`;
            const td = new UICell(row)
                .addUIClass('align-' + column.columnHead.align)
                .setUICSSText(tdCssText)
                .addUI(new UISpan().addUIClass(CLASS_PREFIX + 'dr-table-header-cell').setUIInnerHTML(column.columnName));
            row.addUICell(td);
        });
        // row 삭제 버튼 영역
        const td = new UICell(row)
            .addUIClass('align-center')
            .setUICSSText('width:35' + UNIT.PX);
        row.addUICell(td);

        if (Array.isArray(this.value) && this.value.length > 0) {
            this.value.forEach((rowData) => {
                this.addTableRow(table, rowData);
            });
        } else {
            this.setEmptyTable(table);
        }
    },
    // 데이터가 없을때
    setEmptyTable(targetTable) {
        const row = new UIRow(targetTable).setUIClass('no-data-found-list');
        targetTable.addUIRow(row);

        const td = new UICell(row).setUIClass('on align-center first-column last-column')
            .setColspan(this.elementColumns.length + 1)
            .setUITextContent(i18n.msg('common.msg.noData'));
        row.addUICell(td);
    },
    // 테이블 row 추가
    addTableRow(targetTable, data) {
        // 데이터가 없을 경우 삭제
        if (targetTable.rows.length === 2 && targetTable.rows[1].hasUIClass('no-data-found-list')) {
            targetTable.removeUIRow(targetTable.rows[1]);
        }
        // value가 문자일 경우 배열로 변환
        if (this.value === '') { this.value = []; }
        
        // row 추가
        const row = new UIRow(targetTable).setUIClass(CLASS_PREFIX + 'dr-table-row');
        // td 추가
        const columnData = {};
        this.elementColumns.forEach((column, index) => {
            columnData[index] = zValidation.isEmpty(data[index]) ? '' : data[index];
            const tdWidth = (Number(column.columnWidth) / FORM.COLUMN) * 100;
            const tdCssText = `width:${tdWidth}%;` +
                `color:${column.columnContent.fontColor};` +
                `font-size:${column.columnContent.fontSize + UNIT.PX};` +
                `${column.columnContent.bold ? 'font-weight:bold;' : ''}` +
                `${column.columnContent.italic ? 'font-style:italic;' : ''}` +
                `${column.columnContent.underline ? 'text-decoration:underline;' : ''}`;

            const td = new UICell(row)
                .setUICSSText(tdCssText)
                .addUI(this.getCellInColumnType(column, columnData[index]));
            row.addUICell(td);
        });
        // 데이터 추가
        if (zValidation.isEmpty(data)) {
            this.value[targetTable.rows.length - 1] = columnData;
        }
        // 삭제 버튼
        const removeButton = new UIButton()
            .onUIClick(this.removeTableRow.bind(this, targetTable, row))
            .addUI(new UISpan().setUIClass('icon').addUIClass('icon-close'));
        const td = new UICell(row)
            .addUIClass('align-center')
            .setUICSSText('width:35' + UNIT.PX)
            .addUI(removeButton);
        row.addUICell(td);
        targetTable.addUIRow(row);
    },
    // column Type 에 따른 cell 반환
    getCellInColumnType(column, cellValue) {
        switch (column.columnType) {
        case 'input':
            return new UIInput().setUIPlaceholder(column.columnElement.placeholder)
                .setUIValue(cellValue)
                .setUIAttribute('data-validation-type', column.columnElement.validationType)
                .setUIAttribute('data-validation-max-length', column.columnElement.maxLength)
                .setUIAttribute('data-validation-min-length', column.columnElement.minLength)
                .onUIKeyUp(this.updateValue.bind(this))
                .onUIChange(this.updateValue.bind(this));
        default:
            return new UISpan().setUIInnerHTML(cellValue);
        }
    },
    // 테이블 row 삭제
    removeTableRow(targetTable, row) {
        const removeIndex = targetTable.getIndexUIRow(row);
        targetTable.removeUIRow(row);

        const newValue = JSON.parse(JSON.stringify(this.value));
        newValue.splice(removeIndex - 1, 1);
        this.value = newValue;

        // 데이터가 존재하지 않으면 '데이터가 존재하지 않습니다 ' 문구 표시
        if (Array.isArray(this.value) && this.value.length === 0) {
            this.setEmptyTable(targetTable);
        }
    },
    // 신청서, 처리할문서에서 row에 값 변경시 이벤트 핸들러
    updateValue(e) {
        e.stopPropagation();
        e.preventDefault();
        // input, dropdown, date, time, datetime, customCode 등 여러 타입의 값을 처리

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
        const newValue = JSON.parse(JSON.stringify(this.value));
        if (e.target instanceof HTMLInputElement) {
            const rowIndex = e.target.parentNode.parentNode.rowIndex - 1; // 헤더 제외
            const cellIndex = e.target.parentNode.cellIndex;
            newValue[rowIndex][cellIndex] = e.target.value;
        }
        this.value = newValue;
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