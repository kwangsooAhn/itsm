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
import {FORM, UNIT} from '../../../lib/zConstants.js';
import { UIButton, UIDiv, UISpan, UITabPanel } from '../../../lib/zUI.js';
import { zValidation } from '../../../lib/zValidation.js';
import ZProperty from '../zProperty.js';
import ZGroupProperty from './zGroupProperty.js';
import ZInputBoxProperty from './zInputBoxProperty.js';
import ZDropdownProperty from './zDropdownProperty.js';
import ZSliderProperty from './zSliderProperty.js';
import ZSwitchButtonProperty from './zSwitchButtonProperty.js';
import ZToggleButtonProperty from './zToggleButtonProperty.js';
import ZColorPickerProperty from './zColorPickerProperty.js';

const propertyExtends = {
    columnCommon: {
        columnName: 'COLUMN', // 컬럼명
        columnType: 'input', // input, date, time, datetime, radio, checkbox, select 등 입력 유형
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
        columnElement: {} // 타입별 세부 속성
    },
    columnType: [
        { name: 'input', value: 'input' }
    ],
    columnElement: {
        input: {
            placeholder: '',
            validationType: 'none', // none | char | num | numchar | email | phone 등 유효성
            minLength: '0',
            maxLength: '100'
        }
    }
};

export default class ZColumnProperty extends ZProperty {
    constructor(key, name, value) {
        super(key, name, 'columnProperty', value);

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

        // tab panel
        this.UIElement.UITabPanel = new UITabPanel();
        this.UIElement.addUI(this.UIElement.UITabPanel);

        // this.value 에 따라서 tab 추가
        this.value.forEach((item, index) => {
            this.addColumn(this.UIElement.UITabPanel, item, index);
        });

        // 추가 버튼
        // 최대값을 넘어가는 순간 버튼을 숨긴다.
        this.UIElement.UITabPanel.tabsDiv.UIButton = new UIButton()
            .addUIClass('ghost-line')
            .addUIClass((this.value > FORM.MAX_COLUMN_IN_TABLE ? 'off' : 'on'))
            .addUI(new UISpan().addUIClass('icon').addUIClass('icon-plus'))
            .onUIClick(this.addColumn.bind(this, this.UIElement.UITabPanel, { columnType: 'input' },
                this.value.length));
        this.UIElement.UITabPanel.tabsDiv.addUI(this.UIElement.UITabPanel.tabsDiv.UIButton);

        return this.UIElement;
    }
    // 컬럼 추가
    addColumn(parent, option, index) {
        console.log(index);
        // 옵션
        const columnOption = Object.assign({}, propertyExtends.columnCommon, option);
        // 열 속성 기본 값 조회
        if (zValidation.isEmpty(columnOption.columnElement)) {
            Object.assign(columnOption.columnElement, propertyExtends.columnElement[columnOption.columnType]);
        }
        const columnPropertyGroup = new UIDiv();

        // 컬럼 세부 속성 Tab 추가
        const property = this.getPropertyInColumnType(columnOption, index);
        property.map(propertyObject => {
            const propertyObjectElement = propertyObject.makeProperty(this);

            if (!zValidation.isDefined(propertyObjectElement)) { return false; }

            if (propertyObject instanceof ZGroupProperty) {
                // display, 라벨, 엘리먼트, 유효성 등 그룹에 포함될 경우
                propertyObject.children.map(childPropertyObject => {
                    const childPropertyObjectElement = childPropertyObject.makeProperty(this);
                    propertyObjectElement.addUI(childPropertyObjectElement);
                });
            }
            columnPropertyGroup.addUI(propertyObjectElement);
        });
        parent.addUITab(
            'column' + index,
            new UISpan().addUIClass('icon').addUIClass('icon-checkbox'),
            columnPropertyGroup
        );
        parent.tabs[index].addUIClass('ghost-line');

        // 옵션 신규 추가
        if (this.value.length <= index) {
            this.value.push(columnOption);
            this.panel.update.call(this.panel, this.key, this.value);

            // 신규 추가일 경우 + 추가 버튼과 위치를 변경한다. (재정렬)
            //aliceJs.swapNode(this.UIElement.UITabPanel.tabsDiv.UIButton.domElement, parent.tabs[index].domElement);
        }
    }
    // 컬럼 삭제
    removeColumn() {

    }

    // 컬럼공통 속성 조회
    getPropertyInColumnCommon(option, index) {
        // 입력 유형
        const columnTypeProperty = new ZDropdownProperty(index + '|columnType', 'element.columnType',
            option.columnType, [
                {name: 'form.columnType.input', value: 'input'},
                {name: 'form.columnType.radio', value: 'radio'},
                {name: 'form.columnType.checkbox', value: 'checkbox'},
                {name: 'form.columnType.select', value: 'select'},
                {name: 'form.columnType.date', value: 'date'},
                {name: 'form.columnType.time', value: 'time'},
                {name: 'form.columnType.datetime', value: 'datetime'}
            ]);

        // head - fontColor
        const columnHeadColorProperty = new ZColorPickerProperty(index + '|columnHead.fontColor', 'columnHead.fontColor', option.columnHead.fontColor, false)
            .setValidation(false, 'rgb', '', '', '', '25');
        columnHeadColorProperty.columnWidth = '9';

        // head - fontSize
        const columnHeadFontSizeProperty = new ZInputBoxProperty(index + '|columnHead.fontSize', 'columnHead.fontSize', option.columnHead.fontSize)
            .setValidation(false, 'number', '10', '100', '', '');
        columnHeadFontSizeProperty.unit = UNIT.PX;
        columnHeadFontSizeProperty.columnWidth = '3';

        // head - align
        const columnHeadAlignProperty = new ZSwitchButtonProperty(index + '|columnHead.align', 'columnHead.align', option.columnHead.align, [
            { 'name': 'icon-align-left', 'value': 'left' },
            { 'name': 'icon-align-center', 'value': 'center' },
            { 'name': 'icon-align-right', 'value': 'right' }
        ]);
        columnHeadAlignProperty.columnWidth = '6';

        // head - fontOption
        const columnHeadFontOption = [
            { 'name': 'icon-bold', 'value': 'bold'},
            { 'name': 'icon-italic', 'value': 'italic' },
            { 'name': 'icon-underline', 'value': 'underline' }
        ];
        const columnHeadFontValue = columnHeadFontOption.map((item) => option.columnHead[item.value] ? 'Y' : 'N').join('|');
        const columnHeadFontOptionProperty = new ZToggleButtonProperty(index + '|columnHead.', 'columnHead.fontOption', columnHeadFontValue, columnHeadFontOption);
        columnHeadFontOptionProperty.columnWidth = '6';

        // content - fontColor
        const columnContentColorProperty = new ZColorPickerProperty(index + '|columnContent.fontColor', 'columnContent.fontColor', option.columnContent.fontColor, false)
            .setValidation(false, 'rgb', '', '', '', '25');
        columnContentColorProperty.columnWidth = '9';

        // content - fontSize
        const columnContentFontSizeProperty = new ZInputBoxProperty(index + '|columnContent.fontSize', 'columnContent.fontSize', option.columnContent.fontSize)
            .setValidation(false, 'number', '10', '100', '', '');
        columnContentFontSizeProperty.unit = UNIT.PX;
        columnContentFontSizeProperty.columnWidth = '3';

        // content - align
        const columnContentAlignProperty = new ZSwitchButtonProperty(index + '|columnContent.align', 'columnContent.align', option.columnContent.align, [
            { 'name': 'icon-align-left', 'value': 'left' },
            { 'name': 'icon-align-center', 'value': 'center' },
            { 'name': 'icon-align-right', 'value': 'right' }
        ]);
        columnContentAlignProperty.columnWidth = '6';

        // content - fontOption
        const columnContentFontOption = [
            { 'name': 'icon-bold', 'value': 'bold'},
            { 'name': 'icon-italic', 'value': 'italic' },
            { 'name': 'icon-underline', 'value': 'underline' }
        ];
        const columnContentFontValue = columnContentFontOption.map((item) => option.columnContent[item.value] ? 'Y' : 'N').join('|');
        const columnContentFontOptionProperty = new ZToggleButtonProperty(index + '|columnContent.', 'columnContent.fontOption', columnContentFontValue, columnContentFontOption);
        columnContentFontOptionProperty.columnWidth = '6';

        return [
            new ZInputBoxProperty(index + '|columnName', 'element.columnName', option.columnName),
            columnTypeProperty,
            new ZSliderProperty(index + '|columnWidth', 'element.columnWidth', option.columnWidth),
            new ZGroupProperty('group.columnHead')
                .addProperty(columnHeadColorProperty)
                .addProperty(columnHeadFontSizeProperty)
                .addProperty(columnHeadAlignProperty)
                .addProperty(columnHeadFontOptionProperty),
            new ZGroupProperty('group.columnContent')
                .addProperty(columnContentColorProperty)
                .addProperty(columnContentFontSizeProperty)
                .addProperty(columnContentAlignProperty)
                .addProperty(columnContentFontOptionProperty),

        ];
    }
    // 컬럼입력유형에 따른 속성 조회
    getPropertyInColumnType(option, index) {
        switch (option.columnType) {
        case 'input':
            // validation Type
            const validationTypeProperty = new ZDropdownProperty(index + '|columnElement.validationType', 'validation.validationType',
                option.columnElement.validationType, [
                    {name: 'form.properties.none', value: 'none'},
                    {name: 'form.properties.char', value: 'char'},
                    {name: 'form.properties.number', value: 'number'},
                    {name: 'form.properties.email', value: 'email'},
                    {name: 'form.properties.phone', value: 'phone'}
                ]);
            return [
                ...this.getPropertyInColumnCommon(option, index),
                new ZGroupProperty('group.columnElement')
                    .addProperty(new ZInputBoxProperty(index + '|columnElement.placeholder', 'element.placeholder', option.columnElement.placeholder))
                    .addProperty(validationTypeProperty)
                    .addProperty(new ZInputBoxProperty(index + '|columnElement.minLength', 'validation.minLength', option.columnElement.minLength))
                    .addProperty(new ZInputBoxProperty(index + '|columnElement.maxLength', 'validation.maxLength', option.columnElement.maxLength))
            ];
        default:
            return [];
        }
    }
    
    // 컬럼 세부 속성 변경시 호출되는 이벤트 핸들러
    update(key, value) {
        // 0|key.key 속성 형태로 값이 전달되며 첫번째는 column 순서 즉 index를 의미한다.
        const keyArray = key.split('|');
        // property 검색
        const propertyNameArray =  keyArray[1].split('.');
        let changeColumnOption = this.value[keyArray[0]];
        for (let i = 0; i < propertyNameArray.length - 1; i++) {
            changeColumnOption = changeColumnOption[propertyNameArray[i]];
        }
        const lastPropertyName = propertyNameArray[propertyNameArray.length - 1];
        const method = lastPropertyName.substr(0, 1).toLowerCase() +
            lastPropertyName.substr(1, lastPropertyName.length);

        changeColumnOption[method] = value;
        this.panel.update.call(this.panel, this.key, this.value);
    }
}