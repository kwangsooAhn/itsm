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

import { FORM, CLASS_PREFIX, UNIT, SESSION } from '../../lib/zConstants.js';
import { zValidation } from '../../lib/zValidation.js';
import { UIDiv, UICell, UIRow, UIInput, UISpan, UITable, UIButton, UISelect } from '../../lib/zUI.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZLabelProperty from '../../formDesigner/property/type/zLabelProperty.js';
import ZSwitchProperty from '../../formDesigner/property/type/zSwitchProperty.js';
import ZColumnProperty, { propertyExtends } from '../../formDesigner/property/type/zColumnProperty.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '12',
        columns: [{
            ...propertyExtends.columnCommon,
            ...propertyExtends.input
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
        // 데이터 : "value" :[['1행 1열', '1행 2열', '1행 3열'], ['2행 1열', '2행 2열', '2행 2열']]
        this._value = this.data.value || '';
        // 데이터 초기화
        if (!zValidation.isEmpty(this._value)) {
            this._value = JSON.parse(this._value);
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
        if (columns[0].columnType ===  FORM.DATE_TYPE.DAYS) {
            columns[0].columnValidation.minDate = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT, columns[0].columnType, columns[0].columnValidation.minDate);
            columns[0].columnValidation.maxDate = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT, columns[0].columnType, columns[0].columnValidation.maxDate);
        }
        this._element.columns = columns;
        this.UIElement.UIComponent.UIElement.UITable.clearUIRow().clearUI();
        this.makeTable(this.UIElement.UIComponent.UIElement.UITable);
    },
    get elementColumns() {
        if (this._element.columns[0].columnType ===FORM.DATE_TYPE.DAYS) {
            this._element.columns[0].columnValidation.minDate = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, this._element.columns[0].columnType, this._element.columns[0].columnValidation.minDate);
            this._element.columns[0].columnValidation.maxDate = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, this._element.columns[0].columnType, this._element.columns[0].columnValidation.maxDate);
        }
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
        if (zValidation.isEmpty(this._value)) { this.value = []; }
        
        // row 추가
        const row = new UIRow(targetTable).setUIClass(CLASS_PREFIX + 'dr-table-row');
        // td 추가
        const columnData = [];
        this.elementColumns.forEach((column, index) => {
            columnData.push(zValidation.isEmpty(data[index]) ? '${default}' : data[index]);
            const tdWidth = (Number(column.columnWidth) / FORM.COLUMN) * 100;
            const tdCssText = `width:${tdWidth}%;` +
                `color:${column.columnContent.fontColor};` +
                `font-size:${column.columnContent.fontSize + UNIT.PX};` +
                `${column.columnContent.bold ? 'font-weight:bold;' : ''}` +
                `${column.columnContent.italic ? 'font-style:italic;' : ''}` +
                `${column.columnContent.underline ? 'text-decoration:underline;' : ''}`;

            const td = new UICell(row)
                .setUICSSText(tdCssText)
                .addUI(this.getElementByColumnType(column, columnData[index]));
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
    getElementByColumnType(column, cellValue) {
        switch (column.columnType) {
            case 'input':
                return this.getInputBoxForColumn(column, cellValue);
            case 'dropdown':
                return this.getDropDownForColumn(column, cellValue);
            case 'date':
                return this.getDateForColumn(column, cellValue);
            default:
                return new UISpan().setUIInnerHTML(cellValue);
        }
    },
    //column Type - input
    getInputBoxForColumn(column, cellValue) {
        let defaultValue = cellValue;
        if (cellValue === '${default}') {
            // 직접입력일 경우 : none|입력값
            const defaultValues = column.columnElement.defaultValueSelect.split('|');
            if (defaultValues[0] === 'input') {
                defaultValue = defaultValues[1];
            } else {  // 자동일경우 : select|userKey
                defaultValue = SESSION[defaultValues[1]] || '';
            }
        }
        return new UIInput().setUIPlaceholder(column.columnElement.placeholder)
            .setUIValue(defaultValue)
            .setUIAttribute('data-validation-type', column.columnValidation.validationType)
            .setUIAttribute('data-validation-max-length', column.columnValidation.maxLength)
            .setUIAttribute('data-validation-min-length', column.columnValidation.minLength)
            .onUIKeyUp(this.updateValue.bind(this))
            .onUIChange(this.updateValue.bind(this));
    },
    // column Type - dropdown
    getDropDownForColumn(column, cellValue) {
        const selectOptionValue = cellValue === '${default}' ? column.columnElement.options[0].value : cellValue;
        return new UISelect()
            .setUIOptions(column.columnElement.options)
            .setUIValue(selectOptionValue)
            .onUIChange(this.updateValue.bind(this));
    },
    getDateForColumn(column, cellValue) {
        let dateWrapper = new UIDiv().setUIClass(CLASS_PREFIX + 'element');
        let date = new UIInput().setUIPlaceholder(i18n.dateFormat)
            .setUIClass('datepicker')
            //.setUICSSText('width: 100%')
            .setUIValue(this.getDefaultValue(column, cellValue))
            .setUIAttribute('type', FORM.DATE_TYPE.DATE_PICKER)
            .setUIAttribute('data-validation-max-date', this._element.columns[0].columnValidation.maxDate)
            .setUIAttribute('data-validation-min-date', this._element.columns[0].columnValidation.minDate);
        dateWrapper.addUI(date);
        zDateTimePicker.initDatePicker(date.domElement, this.updateValue.bind(this));
        return dateWrapper;

    },
    getDefaultValue(column, cellValue) {
        if (cellValue === '${default}') {
            // none, now, date|-3, time|2, datetime|7|0, datetimepicker|2020-03-20 09:00 등 기본 값이 전달된다.
            const defaultValueArray = column.columnElement.defaultValueRadio.split('|');
            let date = '';
            switch (defaultValueArray[0]) {
                case FORM.DATE_TYPE.NONE:
                    break;
                case FORM.DATE_TYPE.NOW:
                    date = i18n.getDate();
                    break;
                case FORM.DATE_TYPE.DAYS:
                    const offset = {
                        days: zValidation.isEmpty(defaultValueArray[1]) || isNaN(Number(defaultValueArray[1])) ?
                            0 : Number(defaultValueArray[1])
                    };
                    date = i18n.getDate(offset);
                    break;
                case FORM.DATE_TYPE.DATE_PICKER:
                    date = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, FORM.DATE_TYPE.DAYS, zValidation.isEmpty(defaultValueArray[1]) ? '' : defaultValueArray[1]);
                    break;
            }
            return date;
        } else {
            return aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, FORM.DATE_TYPE.DAYS, cellValue);
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
        // input, dropdown, date, time, datetime, customCode 등 여러 타입의 값을 처리

        if (e.type === 'change' || e.type === 'keyup') { //inputbox, dropbox 일경우
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
            const newValue = JSON.parse(JSON.stringify(this.value));
            const rowIndex = e.target.parentNode.parentNode.rowIndex - 1; // 헤더 제외
            const cellIndex = e.target.parentNode.cellIndex;
            newValue[rowIndex][cellIndex] = e.target.value;
            this.value = newValue;
        } else {  //date, time, datetime 일 경우 (date, time, datetime은  type이 input으로 박혀있음)
            let isValidationPass = true;
            if (!zValidation.isEmpty(this._element.columns[0].columnValidation.minDate)) {
                isValidationPass = i18n.compareSystemDate(this._element.columns[0].columnValidation.minDate, e.value);
                zValidation.setDOMElementError(isValidationPass, e, i18n.msg('common.msg.selectAfterDate', this._element.columns[0].columnValidation.minDate));

            }
            if (isValidationPass && !zValidation.isEmpty(this._element.columns[0].columnValidation.maxDate)) {
                isValidationPass = i18n.compareSystemDate(e.value, this._element.columns[0].columnValidation.maxDate);
                zValidation.setDOMElementError(isValidationPass, e, i18n.msg('common.msg.selectBeforeDate', this._element.columns[0].columnValidation.maxDate));
            }
            const newValue = this.value;
            const rowIndex = e.parentNode.parentNode.parentNode.parentNode.rowIndex - 1; // 헤더 제외
            const cellIndex = e.parentNode.parentNode.parentNode.cellIndex;
            newValue[rowIndex][cellIndex] = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT, e.type, e.value);
            this.value = newValue;
        }
    },
    // 세부 속성 조회
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
