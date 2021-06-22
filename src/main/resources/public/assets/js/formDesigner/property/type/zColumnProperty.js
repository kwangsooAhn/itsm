/**
 * dropdown Property Class
 *
 * SELECT 형태의 속성항목 타입을 위한 클래스이다.
 * 실제 속성 값으로 선택할 수 있는 데이터를 options 필드에 추가한다.
 *
 * @author Jung Hee Chan <hcjung@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import { FORM } from '../../../lib/zConstants.js';
import { UIButton, UIDiv, UISpan, UITabPanel } from '../../../lib/zUI.js';
import { zValidation } from '../../../lib/zValidation.js';
import ZProperty from '../zProperty.js';
import ZGroupProperty from './zGroupProperty.js';
import ZInputBoxProperty from './zInputBoxProperty.js';

const propertyExtends = {
    columnCommon: {
        columnName: 'COLUMN', // 컬럼명
        columnType: 'input', // input, date, time, datetime, radio, checkbox, select 등 입력 유형
        columnWidth: '12', // 컬럼 너비
        columnHeadFontSize: '14',
        columnHeadFontColor: 'rgba(141, 146, 153, 1)',
        columnHeadBold: true,
        columnHeadItalic: false,
        columnHeadUnderline: false,
        columnAlign: 'left',
        columnBodyFontSize: '14',
        columnBodyFontColor: 'rgba(50, 50, 51, 1)',
        columnBodyBold: false,
        columnBodyItalic: false,
        columnBodyUnderline: false,
        columnElement: {} // 타입별 세부 속성
    },
    columnType: [
        { name: 'input', value: 'input' }
    ],
    columnElement: {
        input: {
            placeholder: '',
            validationType: 'none', // none | char | num | numchar | email | phone
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
                this.UIElement.UITabPanel.tabs.length));
        this.UIElement.UITabPanel.tabsDiv.addUI(this.UIElement.UITabPanel.tabsDiv.UIButton);

        return this.UIElement;
    }
    // 컬럼 추가
    addColumn(parent, option, index) {
        // 옵션
        const columnOption = Object.assign({}, propertyExtends.columnCommon, option);
        // 열 속성 기본 값 조회
        if (zValidation.isEmpty(columnOption.columnElement)) {
            Object.assign(columnOption.columnElement, propertyExtends.columnElement[columnOption.columnType]);
        }
        console.log(columnOption);
        console.log(index);

        const columnDiv = new UIDiv();

        // 컬럼 세부 속성 표시
        const property = this.getPropertyInColumnType(columnOption);
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
            columnDiv.addUI(propertyObjectElement);
        });
        // 탭 추가
        parent.addUITab(
            'column' + index,
            new UISpan().addUIClass('icon').addUIClass('icon-checkbox'),
            columnDiv
        );
        // 옵션 추가
        console.log(this.key);
        console.log(this.value);
        // this.panel.update.call(this.panel, e.target.id, e.target.value);
    }
    // 컬럼 삭제
    removeColumn() {

    }

    // 컬럼공통 속성 조회
    getPropertyInColumnCommon(option, index) {
        return [
            new ZGroupProperty('group.column')
                .addProperty(new ZInputBoxProperty(index + '|columnName', 'element.columnName', option.columnName))
        ];
    }
    // 컬럼입력유형에 따른 속성 조회
    getPropertyInColumnType(option, index) {
        switch (option.columnType) {
        case 'input':
            return [
                ...this.getPropertyInColumnCommon(option, index),
                new ZGroupProperty('group.columnElement')
                    .addProperty(new ZInputBoxProperty(index + '|columnElement|placeholder', 'element.placeholder', option.columnElement.placeholder))
            ];
        }
    }
    
    // 컬럼 세부 속성 변경시 호출되는 이벤트 핸들러
    update(key, value) {
        console.log(key, value);
    }

    // 속성 변경시 발생하는 이벤트 핸들러
    updateProperty(e) {
        e.stopPropagation();
        e.preventDefault();

        // change 일 경우 minLength, maxLength 체크
        if (e.type === 'change' && !zValidation.changeValidationCheck(e.target)) {
            this.panel.validationStatus = false; // 유효성 검증 실패
            return false;
        }
        this.panel.update.call(this.panel, e.target.id, e.target.value);
    }
}