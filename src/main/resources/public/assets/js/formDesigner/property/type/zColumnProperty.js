/**
 * column Property Class
 *
 * Table Column을 추가하는 형태의 속성항목 타입을 위한 클래스이다.
 * 현재는 dynamic row table 에서만 사용되고 있으며 옵션들도 dynamic row table 용으로 맞추어져 있다.
 *
 * @author Woo Da Jung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import { FORM, UNIT } from '../../../lib/zConstants.js';
import { UIButton, UIDiv, UISpan } from '../../../lib/zUI.js';
import { zValidation } from '../../../lib/zValidation.js';
import ZProperty from '../zProperty.js';
import ZGroupProperty from './zGroupProperty.js';
import ZInputBoxProperty from './zInputBoxProperty.js';
import ZDropdownProperty from './zDropdownProperty.js';
import ZSliderProperty from './zSliderProperty.js';
import ZSwitchButtonProperty from './zSwitchButtonProperty.js';
import ZToggleButtonProperty from './zToggleButtonProperty.js';
import ZColorPickerProperty from './zColorPickerProperty.js';
import ZDefaultValueSelectProperty from './zDefaultValueSelectProperty.js';
import ZOptionListProperty from './zOptionListProperty.js';
import ZDefaultValueRadioProperty from './zDefaultValueRadioProperty.js';
import ZDateTimePickerProperty from './zDateTimePickerProperty.js';
import ZSwitchProperty from './zSwitchProperty.js';

export const propertyExtends = {
    columnCommon: {
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
        }
    },
    input: {
        columnElement: {
            placeholder: '',
            defaultValueSelect: 'input|', // input|사용자입력 / select|세션값
        },
        columnValidation: {
            required: false, // 필수값 여부
            validationType: 'none',
            minLength: '0',
            maxLength: '100'
        }
    },
    dropdown: {
        columnElement: {
            options: [ FORM.DEFAULT_OPTION_ROW ]
        }
    },
    date: {
        columnElement: {
            defaultValueRadio: 'none'
        },
        columnValidation: {
            minDate: '',
            maxDate: ''
        }
    },
    time: {
        columnElement: {
            defaultValueRadio: 'none'
        },
        columnValidation: {
            minTime: '',
            maxTime: ''
        }
    },
    dateTime: {
        columnElement: {
            defaultValueRadio: 'none'
        },
        columnValidation: {
            minDateTime: '',
            maxDateTime: ''
        }
    }
};

export default class ZColumnProperty extends ZProperty {
    constructor(key, name, value) {
        super(key, name, 'columnProperty', value);

        this.tabs = [];
        this.panels = [];
        this.selectedTabId = '';

        this.validationStatus = true;
    }
    // DOM Element 생성
    makeProperty(panel) {
        this.panel = panel;

        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);

        // tab panel > tagGroup, panelGroup
        this.UITabPanel = new UIDiv().setUIClass('z-tab-panel');
        this.UITabPanel.tabGroup = new UIDiv().setUIClass('z-tabs');
        this.UITabPanel.panelGroup = new UIDiv().setUIClass('z-panels');
        this.UITabPanel.addUI(this.UITabPanel.tabGroup).addUI(this.UITabPanel.panelGroup);
        this.UIElement.addUI(this.UITabPanel);

        // this.value 에 따라서 tab 추가
        this.value.forEach((item, index) => {
            this.addColumn(item, index);
        });

        // tab panel > tagGroup > addButton : 컬럼 추가 버튼
        this.UITabPanel.tabGroup.addButton = new UIButton()
            .setUIClass('z-button-icon')
            .addUIClass('extra')
            .addUIClass((this.value.length >= FORM.MAX_COLUMN_IN_TABLE ? 'off' : 'on'))
            .addUI(new UISpan().addUIClass('z-icon').addUIClass('i-plus'))
            .onUIClick(this.addColumn.bind(this, { columnType: 'input' }, -1));
        this.UITabPanel.tabGroup.addUI(this.UITabPanel.tabGroup.addButton);

        return this.UIElement;
    }
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {}
    // 컬럼 추가
    addColumn(option, index) {
        if (index === -1 ) { index = this.value.length; }
        // 공통 컬럼 옵션
        const commonColumnOption = Object.assign({}, propertyExtends.columnCommon, option);
        // 열 속성 기본 값 조회
        const columnOption = aliceJs.mergeObject(propertyExtends[commonColumnOption.columnType], commonColumnOption);
        // tab 버튼
        const tab = new UIButton()
            .setUIClass('z-button-icon')
            .addUIClass('z-tab')
            .setUIId('column' + index)
            .addUI(new UISpan().addUIClass('z-icon').addUIClass('i-check'))
            .onUIClick(this.selectColumn.bind(this, 'column' + index));
        this.UITabPanel.tabGroup.addUI(tab);
        this.tabs.push(tab);

        // panel 추가
        const panel = new UIDiv()
            .setUIClass('z-panel')
            .setUIId('column' + index)
            .setUIDisplay('none');
        panel.UICommon = this.addColumnForColumnCommon(columnOption, index); // 컬럼 공통 속성
        panel.UIColumn = this.addColumnForColumnType(columnOption, index); // 입력 유형별 속성
        panel.addUI(panel.UICommon).addUI(panel.UIColumn);
        this.UITabPanel.panelGroup.addUI(panel);
        this.panels.push(panel);

        this.selectColumn('column' + index);

        // 옵션 신규 추가
        if (this.value.length <= index) {
            this.value.push(columnOption);
            // 신규 추가일 경우 + 추가 버튼과 위치를 변경한다. (재정렬)
            aliceJs.swapNode(this.UITabPanel.tabGroup.addButton.domElement, this.tabs[index].domElement);
            // 최대값을 넘어가는 순간 추가 버튼을 숨긴다.
            if ((index + 1 ) === FORM.MAX_COLUMN_IN_TABLE) {
                this.UITabPanel.tabGroup.addButton.removeUIClass('on').addUIClass('off');
            }
            this.panel.update.call(this.panel, this.key, JSON.parse(JSON.stringify(this.value)));
        }
    }
    addColumnForColumnCommon(option, index) {
        const columnCommonGroup = new UIDiv().setUIClass('z-panel-common');
        // 순서 변경 < > 버튼 추가
        const arrowLeftButton = new UIButton().setUIClass('z-button-icon')
            .addUI(new UISpan().setUIClass('z-icon').addUIClass('i-arrow-right').addUIClass('z-prev'))
            .onUIClick(this.swapColumn.bind(this, 'column' + index, - 1));
        const arrowRightButton = new UIButton().setUIClass('z-button-icon')
            .addUI(new UISpan().setUIClass('z-icon').addUIClass('i-arrow-right').addUIClass('z-next'))
            .onUIClick(this.swapColumn.bind(this, 'column' + index, + 1));
        // 패널 삭제 버튼 추가
        const deleteButton = new UIButton().setUIClass('z-button-icon').addUIClass('panel-delete-button')
            .addUI(new UISpan().setUIClass('z-icon').addUIClass('i-delete'))
            .onUIClick(this.removeColumn.bind(this, 'column' + index));

        columnCommonGroup.addUI(
            new UISpan().setUIClass('panel-name').setUIInnerHTML(i18n.msg('form.properties.element.columnOrder')),
            arrowLeftButton,
            arrowRightButton,
            deleteButton
        );
        const property = this.getPropertyForColumnCommon(option, 'column' + index);
        this.makePropertyRecursive(columnCommonGroup, property);
        return columnCommonGroup;
    }
    addColumnForColumnType(option, index) {
        const columnIndividualGroup = new UIDiv().setUIClass('z-panel-individual');
        const property = this.getPropertyForColumnType(option, 'column' + index);
        this.makePropertyRecursive(columnIndividualGroup, property);
        return columnIndividualGroup;
    }
    // 세부 속성 추가 (Recursive)
    makePropertyRecursive(target, property) {
        property.map(propertyObject => {
            const propertyObjectElement = propertyObject.makeProperty(this);

            if (!zValidation.isDefined(propertyObjectElement)) { return false; }
            target.addUI(propertyObjectElement);
            propertyObject.afterEvent();

            if (propertyObject instanceof ZGroupProperty) {
                // 그룹에 포함될 경우
                propertyObject.children.map(childPropertyObject => {
                    const childPropertyObjectElement = childPropertyObject.makeProperty(this);
                    propertyObjectElement.addUI(childPropertyObjectElement);
                    childPropertyObject.afterEvent();
                });
            }
        });
    }
    // 컬럼 선택
    selectColumn(id) {
        let tab;
        let panel;
        const scope = this;
        if (this.selectedTabId && this.selectedTabId.length) {
            tab = this.tabs.find(function(item) {
                return item.domElement.id === scope.selectedTabId;
            });

            panel = this.panels.find(function(item) {
                return item.domElement.id === scope.selectedTabId;
            });

            if (tab) {
                tab.removeUIClass('selected');
            }

            if (panel) {
                panel.setUIDisplay('none');
            }
        }

        tab = this.tabs.find(function (item) {
            return item.domElement.id === id;
        });

        panel = this.panels.find(function (item) {
            return item.domElement.id === id;
        });

        if (tab) {
            tab.addUIClass('selected');
        }

        if (panel) {
            panel.setUIDisplay('');
        }

        this.selectedTabId = id;

        return this;
    }
    // 컬럼 삭제
    removeColumn(id) {
        const index = this.tabs.findIndex((tab) => tab.getUIId() === id);

        if (index === -1) { return false; }

        if (this.value.length === 1) {
            zAlert.warning(i18n.msg('form.msg.failedAllColumnDelete'));
        } else {
            this.UITabPanel.tabGroup.removeUI(this.tabs[index]);
            this.tabs.splice(index, 1);

            this.UITabPanel.panelGroup.removeUI(this.panels[index]);
            this.panels.splice(index, 1);

            this.value.splice(index, 1);
            if (this.UITabPanel.tabGroup.addButton.hasUIClass('off')) {
                this.UITabPanel.tabGroup.addButton.removeUIClass('off').addUIClass('on');
            }
            // 이전 탭 선택
            const prevTab = this.tabs[index - 1];
            this.selectColumn(prevTab.getUIId());

            this.panel.update.call(this.panel, this.key, JSON.parse(JSON.stringify(this.value)));
        }
    }
    // 컬럼 순서 변경
    swapColumn(id, offset) {
        const curIndex = this.tabs.findIndex((tab) => tab.getUIId() === id);
        const changeIndex = curIndex + offset;
        if (changeIndex === -1 || changeIndex === this.value.length) { return false; }

        aliceJs.swapNode(this.tabs[curIndex].domElement, this.tabs[changeIndex].domElement);
        [this.tabs[curIndex], this.tabs[changeIndex]] = [this.tabs[changeIndex], this.tabs[curIndex]];

        aliceJs.swapNode(this.panels[curIndex].domElement, this.panels[changeIndex].domElement);
        [this.panels[curIndex], this.panels[changeIndex]] = [this.panels[changeIndex], this.panels[curIndex]];

        [this.value[curIndex], this.value[changeIndex]] = [this.value[changeIndex], this.value[curIndex]];
        this.panel.update.call(this.panel, this.key, JSON.parse(JSON.stringify(this.value)));
    }
    // 컬럼 세부 속성 - 공통
    getPropertyForColumnCommon(option, id) {
        // 입력 유형
        const columnTypeProperty = new ZDropdownProperty(id + '|columnType', 'element.columnType',
            option.columnType, [ // input, dropdown, date, time, datetime
                { name: i18n.msg('form.properties.columnType.input'), value: 'input' },
                { name: i18n.msg('form.properties.columnType.dropdown'), value: 'dropdown' },
                { name: i18n.msg('form.properties.columnType.date'), value: 'date' },
                { name: i18n.msg('form.properties.columnType.time'), value: 'time' },
                { name: i18n.msg('form.properties.columnType.dateTime'), value: 'dateTime' }
            ]);

        // head - input
        const columnHeadInputProperty = new ZInputBoxProperty(id + '|columnName', 'element.columnName', option.columnName);
        columnHeadInputProperty.columnWidth = '9';

        // head - fontColor
        const columnHeadColorProperty = new ZColorPickerProperty(id + '|columnHead.fontColor', 'columnHead.fontColor', option.columnHead.fontColor, false)
            .setValidation(false, 'rgb', '', '', '', '25');
        columnHeadColorProperty.columnWidth = '9';

        // head - fontSize
        const columnHeadFontSizeProperty = new ZInputBoxProperty(id + '|columnHead.fontSize', 'columnHead.fontSize', option.columnHead.fontSize)
            .setValidation(false, 'number', '10', '100', '', '');
        columnHeadFontSizeProperty.unit = UNIT.PX;
        columnHeadFontSizeProperty.columnWidth = '3';

        // head - align
        const columnHeadAlignProperty = new ZSwitchButtonProperty(id + '|columnHead.align', 'columnHead.align', option.columnHead.align, [
            { 'name': 'i-align-left', 'value': 'left' },
            { 'name': 'i-align-center', 'value': 'center' },
            { 'name': 'i-align-right', 'value': 'right' }
        ]);
        columnHeadAlignProperty.columnWidth = '6';

        // head - fontOption
        const columnHeadFontOption = [
            { 'name': 'i-bold', 'value': 'bold'},
            { 'name': 'i-italic', 'value': 'italic' },
            { 'name': 'i-underline', 'value': 'underline' }
        ];
        const columnHeadFontValue = columnHeadFontOption.map((item) => option.columnHead[item.value] ? 'Y' : 'N').join('|');
        const columnHeadFontOptionProperty = new ZToggleButtonProperty(id + '|columnHead.', 'columnHead.fontOption', columnHeadFontValue, columnHeadFontOption);
        columnHeadFontOptionProperty.columnWidth = '6';

        // content - fontColor
        const columnContentColorProperty = new ZColorPickerProperty(id + '|columnContent.fontColor', 'columnContent.fontColor', option.columnContent.fontColor, false)
            .setValidation(false, 'rgb', '', '', '', '25');
        columnContentColorProperty.columnWidth = '9';

        // content - fontSize
        const columnContentFontSizeProperty = new ZInputBoxProperty(id + '|columnContent.fontSize', 'columnContent.fontSize', option.columnContent.fontSize)
            .setValidation(false, 'number', '10', '100', '', '');
        columnContentFontSizeProperty.unit = UNIT.PX;
        columnContentFontSizeProperty.columnWidth = '3';

        // content - align
        const columnContentAlignProperty = new ZSwitchButtonProperty(id + '|columnContent.align', 'columnContent.align', option.columnContent.align, [
            { 'name': 'i-align-left', 'value': 'left' },
            { 'name': 'i-align-center', 'value': 'center' },
            { 'name': 'i-align-right', 'value': 'right' }
        ]);
        columnContentAlignProperty.columnWidth = '6';

        // content - fontOption
        const columnContentFontOption = [
            { 'name': 'i-bold', 'value': 'bold'},
            { 'name': 'i-italic', 'value': 'italic' },
            { 'name': 'i-underline', 'value': 'underline' }
        ];
        const columnContentFontValue = columnContentFontOption.map((item) => option.columnContent[item.value] ? 'Y' : 'N').join('|');
        const columnContentFontOptionProperty = new ZToggleButtonProperty(id + '|columnContent.', 'columnContent.fontOption', columnContentFontValue, columnContentFontOption);
        columnContentFontOptionProperty.columnWidth = '6';

        return [
            // new ZInputBoxProperty(id + '|columnName', 'element.columnName', option.columnName),
            columnTypeProperty,
            new ZSliderProperty(id + '|columnWidth', 'element.columnWidth', option.columnWidth),
            new ZGroupProperty('group.columnHead')
                .addProperty(columnHeadInputProperty)
                .addProperty(columnHeadFontSizeProperty)
                .addProperty(columnHeadAlignProperty)
                .addProperty(columnHeadFontOptionProperty)
                .addProperty(columnHeadColorProperty),
            new ZGroupProperty('group.columnContent')
                .addProperty(columnContentColorProperty)
                .addProperty(columnContentFontSizeProperty)
                .addProperty(columnContentAlignProperty)
                .addProperty(columnContentFontOptionProperty),

        ];
    }
    // 컬럼입력유형에 따른 속성 조회
    getPropertyForColumnType(option, id) {
        switch (option.columnType) {
            case 'input':
                return this.getPropertyForColumnTypeInput(option, id);
            case 'dropdown':
                return this.getPropertyForColumnTypeDropdown(option, id);
            case 'date':
                return this.getPropertyForColumnTypeDate(option, id);
            case 'time':
                return this.getPropertyForColumnTypeTime(option, id);
            case 'dateTime':
                return this.getPropertyForColumnTypeDateTime(option, id);
            default:
                return [];
        }
    }
    // 컬럼 세부 속성 - input
    getPropertyForColumnTypeInput(option, id) {
        const validationTypeProperty = new ZDropdownProperty(id + '|columnValidation.validationType', 'validation.validationType',
            option.columnValidation.validationType, [
                { name: i18n.msg('form.properties.none'), value: 'none' },
                { name: i18n.msg('form.properties.char'), value: 'char' },
                { name: i18n.msg('form.properties.number'), value: 'number' },
                { name: i18n.msg('form.properties.email'), value: 'email' },
                { name: i18n.msg('form.properties.phone'), value: 'phone' }
            ]);
        return [
            new ZGroupProperty('group.columnElement')
                .addProperty(new ZInputBoxProperty(id + '|columnElement.placeholder', 'element.placeholder', option.columnElement.placeholder))
                .addProperty(new ZDefaultValueSelectProperty(id + '|columnElement.defaultValueSelect', 'element.defaultValueSelect', option.columnElement.defaultValueSelect)),
            new ZGroupProperty('group.columnValidation')
                .addProperty(new ZSwitchProperty(id + '|columnValidation.required', 'validation.requiredInput', option.columnValidation.required))
                .addProperty(validationTypeProperty)
                .addProperty(new ZInputBoxProperty(id + '|columnValidation.minLength', 'validation.minLength', option.columnValidation.minLength))
                .addProperty(new ZInputBoxProperty(id + '|columnValidation.maxLength', 'validation.maxLength', option.columnValidation.maxLength))
        ];
    }
    // 컬럼 세부 속성 - input
    getPropertyForColumnTypeDropdown(option, id) {
        return [
            new ZGroupProperty('group.columnElement')
                .addProperty(new ZOptionListProperty(id + '|columnElement.options', 'element.options', option.columnElement.options))
        ];
    }
    getPropertyForColumnTypeDate(option, id) {
        const defaultValueRadioProperty = new ZDefaultValueRadioProperty(id + '|columnElement.defaultValueRadio', 'element.defaultValueRadio',
            option.columnElement.defaultValueRadio,
            [
                { name: 'form.properties.option.none', value: FORM.DATE_TYPE.NONE },
                { name: 'form.properties.option.now', value: FORM.DATE_TYPE.NOW },
                { name: '', value: FORM.DATE_TYPE.DAYS },
                { name: '', value: FORM.DATE_TYPE.DATE_PICKER }
            ]);
        return [
            new ZGroupProperty('group.columnElement')
                .addProperty(defaultValueRadioProperty),
            new ZGroupProperty('group.validation')
                .addProperty(new ZDateTimePickerProperty(id + '|columnValidation.minDate', 'validation.minDate', option.columnValidation.minDate, FORM.DATE_TYPE.DATE_PICKER))
                .addProperty(new ZDateTimePickerProperty(id + '|columnValidation.maxDate', 'validation.maxDate', option.columnValidation.maxDate, FORM.DATE_TYPE.DATE_PICKER))
        ];
    }

    getPropertyForColumnTypeTime(option, id) {
        const defaultValueRadioProperty = new ZDefaultValueRadioProperty(id + '|columnElement.defaultValueRadio', 'element.defaultValueRadio',
            option.columnElement.defaultValueRadio,
            [
                {name: 'form.properties.option.none', value: FORM.DATE_TYPE.NONE},
                {name: 'form.properties.option.now', value: FORM.DATE_TYPE.NOW},
                {name: '', value: FORM.DATE_TYPE.HOURS},
                {name: '', value: FORM.DATE_TYPE.TIME_PICKER}
            ]);
        return [
            new ZGroupProperty('group.columnElement')
                .addProperty(defaultValueRadioProperty),
            new ZGroupProperty('group.validation')
                .addProperty(new ZDateTimePickerProperty(id + '|columnValidation.minTime', 'validation.minTime', option.columnValidation.minTime, FORM.DATE_TYPE.TIME_PICKER))
                .addProperty(new ZDateTimePickerProperty(id + '|columnValidation.maxTime', 'validation.maxTime', option.columnValidation.maxTime, FORM.DATE_TYPE.TIME_PICKER))
        ];
    }

    getPropertyForColumnTypeDateTime(option, id) {
        const defaultValueRadioProperty = new ZDefaultValueRadioProperty(id + '|columnElement.defaultValueRadio', 'element.defaultValueRadio',
            option.columnElement.defaultValueRadio,
            [
                {name: 'form.properties.option.none', value: FORM.DATE_TYPE.NONE},
                {name: 'form.properties.option.now', value: FORM.DATE_TYPE.NOW},
                {name: '', value: FORM.DATE_TYPE.DATETIME},
                {name: '', value: FORM.DATE_TYPE.DATETIME_PICKER}
            ]);
        return [
            new ZGroupProperty('group.columnElement')
                .addProperty(defaultValueRadioProperty),
            new ZGroupProperty('group.validation')
                .addProperty(new ZDateTimePickerProperty(id + '|columnValidation.minDateTime', 'validation.minDateTime', option.columnValidation.minDateTime, FORM.DATE_TYPE.DATETIME_PICKER))
                .addProperty(new ZDateTimePickerProperty(id + '|columnValidation.maxDateTime', 'validation.maxDateTime', option.columnValidation.maxDateTime, FORM.DATE_TYPE.DATETIME_PICKER))
        ];
    }
    // 입력 유형 타입 변경
    changeColumnType(prevData, index) {
        Object.assign(prevData, propertyExtends[prevData.columnType]);

        this.panels[index].UIColumn.clearUI();
        delete this.panels[index].UIColumn;

        this.panels[index].UIColumn = this.addColumnForColumnType(prevData, index); // 입력 유형별 속성
        this.panels[index].addUI(this.panels[index].UIColumn);
    }
    // 컬럼 세부 속성 변경시 호출되는 이벤트 핸들러
    update(key, value) {
        // id|key.key 속성 형태로 값이 전달되며 첫번째는 column 순서 즉 index를 의미한다.
        const keyArray = key.split('|');
        const curIndex = this.tabs.findIndex((tab) => tab.getUIId() === keyArray[0]);
        const changeValue = JSON.parse(JSON.stringify(this.value));

        // property 검색
        const propertyNameArray =  keyArray[1].split('.');
        let changeColumnOption = changeValue[curIndex];
        for (let i = 0; i < propertyNameArray.length - 1; i++) {
            changeColumnOption = changeColumnOption[propertyNameArray[i]];
        }
        const lastPropertyName = propertyNameArray[propertyNameArray.length - 1];
        const method = lastPropertyName.substr(0, 1).toLowerCase() +
            lastPropertyName.substr(1, lastPropertyName.length);
        changeColumnOption[method] = value;

        // 입력 유형 타입 변경시, 입력 유형 엘리먼트 속성을 변경한다.
        if (keyArray[1] === 'columnType') {
            this.changeColumnType(changeColumnOption, curIndex);
        }
        this.value = changeValue;
        this.panel.update.call(this.panel, this.key, JSON.parse(JSON.stringify(this.value)));
    }
}
