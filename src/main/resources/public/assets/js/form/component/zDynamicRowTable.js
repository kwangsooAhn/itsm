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
            .setUIClass(CLASS_PREFIX + 'button-icon')
            .addUIClass('extra')
            .onUIClick(this.addTableRow.bind(this, element.UITable, {}))
            .addUI(new UISpan().addUIClass(CLASS_PREFIX + 'icon').addUIClass('i-plus'));
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
        this._element.columns = this.changeDateTimeFormat(columns, FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT);
        this.UIElement.UIComponent.UIElement.UITable.clearUIRow().clearUI();
        this.makeTable(this.UIElement.UIComponent.UIElement.UITable);
    },
    get elementColumns() {
        return this.changeDateTimeFormat(this._element.columns, FORM.DATE_TYPE.FORMAT.USERFORMAT);
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
                .addUI(this.getElementByColumnType(column, columnData[index], index));
            row.addUICell(td);
        });
        // 데이터 추가
        if (zValidation.isEmpty(data)) {
            this.value[targetTable.rows.length - 1] = columnData;
        }
        // 삭제 버튼
        const removeButton = new UIButton()
            .setUIClass(CLASS_PREFIX + 'button-icon')
            .addUIClass('extra')
            .onUIClick(this.removeTableRow.bind(this, targetTable, row))
            .addUI(new UISpan().setUIClass(CLASS_PREFIX + 'icon').addUIClass('i-clear'));
        const td = new UICell(row)
            .addUIClass('align-center')
            .setUICSSText('width:35' + UNIT.PX)
            .addUI(removeButton);
        row.addUICell(td);
        targetTable.addUIRow(row);
    },
    // column Type 에 따른 cell 반환
    getElementByColumnType(column, cellValue, index) {
        switch (column.columnType) {
            case 'input':
                return this.getInputBoxForColumn(column, cellValue);
            case 'dropdown':
                return this.getDropDownForColumn(column, cellValue);
            case 'date':
                return this.getDateForColumn(column, cellValue, index);
            case 'time':
                return this.getTimeForColumn(column, cellValue, index);
            case 'dateTime':
                return this.getDateTimeForColumn(column, cellValue, index);
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
    getDateForColumn(column, cellValue, index) {
        let dateWrapper = new UIDiv().setUIClass(CLASS_PREFIX + 'element');
        let date = new UIInput().setUIPlaceholder(i18n.dateFormat)
            .setUIClass(CLASS_PREFIX + 'input i-date-picker')
            .setUIId('date' + index +  ZWorkflowUtil.generateUUID())
            .setUIAttribute('name', 'date' + index)
            .setUIValue(this.getDefaultValueForDate(column, cellValue))
            .setUIAttribute('type', FORM.DATE_TYPE.DATE_PICKER)
            .setUIAttribute('data-validation-min-date', this._element.columns[index].columnValidation.minDate)
            .setUIAttribute('data-validation-max-date', this._element.columns[index].columnValidation.maxDate);
        dateWrapper.addUI(date);
        zDateTimePicker.initDatePicker(date.domElement, this.updateDateTimeValue.bind(this));
        return dateWrapper;
    },
    getTimeForColumn(column, cellValue, index) {
        let timeWrapper = new UIDiv().setUIClass(CLASS_PREFIX + 'element');

        let time = new UIInput().setUIPlaceholder(i18n.timeFormat)
            .setUIClass(CLASS_PREFIX + 'input i-time-picker')
            .setUIId('time' + index +  ZWorkflowUtil.generateUUID())
            .setUIAttribute('name', 'time' + index)
            .setUIValue(this.getDefaultValueForTime(column, cellValue))
            .setUIAttribute('type', FORM.DATE_TYPE.TIME_PICKER)
            .setUIAttribute('data-validation-min-time', this._element.columns[index].columnValidation.minTime)
            .setUIAttribute('data-validation-max-time', this._element.columns[index].columnValidation.maxTime);
        timeWrapper.addUI(time);

        zDateTimePicker.initTimePicker(time.domElement, this.updateDateTimeValue.bind(this));
        return timeWrapper;
    },
    getDateTimeForColumn(column, cellValue, index) {
        let dateTimeWrapper = new UIDiv().setUIClass(CLASS_PREFIX + 'element');

        let dateTime = new UIInput().setUIPlaceholder(i18n.dateTimeFormat)
            .setUIClass(CLASS_PREFIX + 'input i-datetime-picker')
            .setUIId('datetime' + index +  ZWorkflowUtil.generateUUID())
            .setUIAttribute('name', 'datetime' + index)
            .setUIValue(this.getDefaultValueForDateTime(column, cellValue))
            .setUIAttribute('type', FORM.DATE_TYPE.DATETIME_PICKER)
            .setUIAttribute('data-validation-min-datetime', this._element.columns[index].columnValidation.minDateTime)
            .setUIAttribute('data-validation-max-datetime', this._element.columns[index].columnValidation.maxDateTime);
        dateTimeWrapper.addUI(dateTime);

        zDateTimePicker.initDateTimePicker(dateTime.domElement, this.updateDateTimeValue.bind(this));
        return dateTimeWrapper;
    },
    getDefaultValueForDate(column, cellValue) {
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
                    date = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, column.columnType, zValidation.isEmpty(defaultValueArray[1]) ? '' : defaultValueArray[1]);
                    break;
            }
            return date;
        } else {
            return aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, column.columnType, cellValue);
        }
    },
    getDefaultValueForTime(column, cellValue) {
        if (cellValue === '${default}') {
            // none, now, date|-3, time|2, datetime|7|0, datetimepicker|2020-03-20 09:00 등 기본 값이 전달된다.
            const defaultValueArray = column.columnElement.defaultValueRadio.split('|');
            let time = '';
            switch (defaultValueArray[0]) {
                case FORM.DATE_TYPE.NONE:
                    break;
                case FORM.DATE_TYPE.NOW:
                    time = i18n.getTime();
                    break;
                case FORM.DATE_TYPE.HOURS:
                    const offset = {
                        hours: zValidation.isEmpty(defaultValueArray[1]) || isNaN(Number(defaultValueArray[1])) ?
                            0 : Number(defaultValueArray[1])
                    };
                    time = i18n.getTime(offset);
                    break;
                case FORM.DATE_TYPE.TIME_PICKER:
                    time = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, column.columnType, zValidation.isEmpty(defaultValueArray[1]) ? '' : defaultValueArray[1]);
                    break;
            }
            return time;
        } else {
            return aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, column.columnType, cellValue);
        }
    },
    getDefaultValueForDateTime(column, cellValue) {
        if (cellValue === '${default}') {
            // none, now, date|-3, time|2, datetime|7|0, datetimepicker|2020-03-20 09:00 등 기본 값이 전달된다.
            const defaultValueArray = column.columnElement.defaultValueRadio.split('|');
            let dateTime = '';
            switch (defaultValueArray[0]) {
                case FORM.DATE_TYPE.NONE:
                    break;
                case FORM.DATE_TYPE.NOW:
                    dateTime = i18n.getDateTime();
                    break;
                case FORM.DATE_TYPE.DATETIME:
                    const offset = {
                        days: zValidation.isEmpty(defaultValueArray[1]) || isNaN(Number(defaultValueArray[1])) ?
                            0 : Number(defaultValueArray[1]),
                        hours: zValidation.isEmpty(defaultValueArray[2]) || isNaN(Number(defaultValueArray[2])) ?
                            0 : Number(defaultValueArray[2])
                    };
                    dateTime = i18n.getDateTime(offset);
                    break;
                case FORM.DATE_TYPE.DATETIME_PICKER:
                    dateTime = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, column.columnType, zValidation.isEmpty(defaultValueArray[1]) ? '' : defaultValueArray[1]);
                    break;
            }
            return dateTime;
        } else {
            return aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, column.columnType, cellValue);
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
    changeDateTimeFormat(columns, format) {
        for (let i = 0; i < columns.length; i++) {
            switch (columns[i].columnType) {
                case FORM.DATE_TYPE.DAYS:
                    columns[i].columnValidation.minDate = aliceJs.convertDateFormat(format, columns[i].columnType, columns[i].columnValidation.minDate);
                    columns[i].columnValidation.maxDate = aliceJs.convertDateFormat(format, columns[i].columnType, columns[i].columnValidation.maxDate);
                    break;
                case FORM.DATE_TYPE.HOURS:
                    columns[i].columnValidation.minTime = aliceJs.convertDateFormat(format, columns[i].columnType, columns[i].columnValidation.minTime);
                    columns[i].columnValidation.maxTime = aliceJs.convertDateFormat(format, columns[i].columnType, columns[i].columnValidation.maxTime);
                    break;
                case FORM.DATE_TYPE.DATETIME:
                    columns[i].columnValidation.minTime = aliceJs.convertDateFormat(format, columns[i].columnType, columns[i].columnValidation.minDateTime);
                    columns[i].columnValidation.maxTime = aliceJs.convertDateFormat(format, columns[i].columnType, columns[i].columnValidation.maxDateTime);
                    break;
                default:
                    break;
            }
        }
        return columns;
    },
    isBetweenMaxDateTimeAndMinDatetime(target) {  // 최소 날짜 ,최대 날짜 유효성 검증
        let isValidationPass = true;

        switch (target.getAttribute('type').replace('Picker', '')) {
            case FORM.DATE_TYPE.DAYS:
                if (!zValidation.isEmpty(target.getAttribute('data-validation-min-date'))) {
                    isValidationPass = i18n.compareSystemDate(target.getAttribute('data-validation-min-date'), target.value);
                    zValidation.setDOMElementError(isValidationPass, target, i18n.msg('common.msg.selectAfterDate', target.getAttribute('data-validation-min-date')));
                }
                if (isValidationPass && !zValidation.isEmpty(target.getAttribute('data-validation-max-date'))) {
                    isValidationPass = i18n.compareSystemDate(target.value, target.getAttribute('data-validation-max-date'));
                    zValidation.setDOMElementError(isValidationPass, target, i18n.msg('common.msg.selectBeforeDate', target.getAttribute('data-validation-max-date')));
                }
                break;
            case FORM.DATE_TYPE.HOURS:
                if (!zValidation.isEmpty(target.getAttribute('data-validation-min-time'))) {
                    isValidationPass = i18n.compareSystemTime(target.getAttribute('data-validation-min-time'), target.value);
                    zValidation.setDOMElementError(isValidationPass, target, i18n.msg('common.msg.selectAfterTime', target.getAttribute('data-validation-min-time')));
                }
                if (isValidationPass && !zValidation.isEmpty(target.getAttribute('data-validation-max-time'))) {
                    isValidationPass = i18n.compareSystemTime(target.value, target.getAttribute('data-validation-max-time'));
                    zValidation.setDOMElementError(isValidationPass, target, i18n.msg('common.msg.selectBeforeTime', target.getAttribute('data-validation-max-time')));
                }
                break;
            case FORM.DATE_TYPE.DATETIME:
                if (!zValidation.isEmpty(target.getAttribute('data-validation-min-datetime'))) {
                    isValidationPass = i18n.compareSystemDateTime(target.getAttribute('data-validation-min-datetime'), target.value);
                    zValidation.setDOMElementError(isValidationPass, target, i18n.msg('common.msg.selectAfterDateTime', target.getAttribute('data-validation-min-datetime')));
                }
                if (isValidationPass && !zValidation.isEmpty(target.getAttribute('data-validation-max-datetime'))) {
                    isValidationPass = i18n.compareSystemDateTime(target.value, target.getAttribute('data-validation-max-datetime'));
                    zValidation.setDOMElementError(isValidationPass, target, i18n.msg('common.msg.selectBeforeDateTime', target.getAttribute('data-validation-max-datetime')));
                }
                break;
            default:
                break;
        }
        return isValidationPass;
    },
    // 신청서, 처리할문서에서 row에 값 변경시 이벤트 핸들러
    updateValue(e) {
        // input, dropdown, date, time, datetime, customCode 등 여러 타입의 값을 처리
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
    },
    updateDateTimeValue(e) {
        if (!this.isBetweenMaxDateTimeAndMinDatetime(e)) {
            return false;
        }
        const newValue = JSON.parse(JSON.stringify(this.value));
        const rowIndex = e.parentNode.parentNode.parentNode.parentNode.rowIndex - 1; // 헤더 제외
        const cellIndex = e.parentNode.parentNode.parentNode.cellIndex;
        newValue[rowIndex][cellIndex] = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT, e.getAttribute('type').replace('Picker', ''), e.value);

        this.value = newValue;
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
