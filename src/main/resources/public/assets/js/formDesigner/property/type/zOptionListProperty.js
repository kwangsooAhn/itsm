/**
 * Option List Property Class
 *
 * select box, check box, radio button 과 같은 컴포넌트는 선택할 수 있는 옵션들을 복수개 포함한다.
 * 이러한 옵션 리스트를 관리하는 속성 클래스이다.
 *
 * @author Jung Hee Chan <hcjung@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import ZProperty from '../zProperty.js';
import {UICell, UIDiv, UIInput, UIRow, UITable} from '../../../lib/zUI.js';
import {CLASS_PREFIX, FORM} from "../../../lib/zConstants.js";

const propertyExtends = {
    options : []
};

export default class ZOptionListProperty extends ZProperty {
    constructor(name, value, options) {
        super(name, 'optionListProperty', value);
        this.options = options
    }

    makeProperty(panel) {
        this.panel = panel;

        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);

        const optionTable = new UITable()
            .setUIClass(CLASS_PREFIX + 'option-table')
            .addUIClass('mt-2')

        const header = new UIRow(optionTable).setUIClass(CLASS_PREFIX + 'option-table-header');
        optionTable.addUIRow(header);

        const checkTD = new UICell(header).setUICSSText('width: 20%');
        const nameTD = new UICell(header).setUICSSText('width: 40%').setUITextContent('항목 이름');
        const valueTD = new UICell(header).setUICSSText('width: 40%').setUITextContent('항목 값');
        header.addUICell(checkTD);
        header.addUICell(nameTD);
        header.addUICell(valueTD);

        this.options.forEach((option) => {
            const optionRow = new UIRow(optionTable).setUIClass(CLASS_PREFIX + 'option-table-row');
            optionTable.addUIRow(optionRow);

            const checkTD = new UICell(optionRow).setUICSSText('width: 20%');
            const nameTD = new UICell(optionRow).setUICSSText('width: 40%').setUITextContent(option.name);
            const valueTD = new UICell(optionRow).setUICSSText('width: 40%').setUITextContent(option.value);
            optionRow.addUICell(checkTD);
            optionRow.addUICell(nameTD);
            optionRow.addUICell(valueTD);
        });

        this.UIElement.UIOptionTable = optionTable;
        this.UIElement.addUI(this.UIElement.UIOptionTable);
        return this.UIElement;
    }

    // 속성 변경시 발생하는 이벤트 핸들러
    updateProperty(e) {
        e.stopPropagation();
        e.preventDefault();
        // enter, tab 입력시
        if (e.type === 'keyup' && (e.keyCode === 13 || e.keyCode === 9)) {
            return false;
        }

        this.panel.update.call(this.panel, e.target.id, e.target.value);
    }
}