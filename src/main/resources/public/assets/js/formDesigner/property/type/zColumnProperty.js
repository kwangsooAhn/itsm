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
import { UIButton, UIDiv, UISpan, UITabPanel, UITab } from '../../../lib/zUI.js';
import { zValidation } from '../../../lib/zValidation.js';
import ZProperty from '../zProperty.js';

const propertyExtends = {
    /* 추가적인 설정이 없다. */
};

export default class ZColumnProperty extends ZProperty {
    constructor(name, value) {
        super(name, 'columnProperty', value);
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
        this.value.forEach((item) => {
            this.addColumn(this.UIElement.UITabPanel, item);
        });
        
        // 추가 버튼
        // 최대값을 넘어가는 순간 버튼을 숨긴다.
        this.UIElement.UITabPanel.tabsDiv.UIButton = new UIButton()
            .addUIClass('ghost-line')
            .addUIClass((this.value > FORM.DYNAMIC_ROW_TABLE.MAX_COLUMN ? 'off' : 'on'))
            .addUI(new UISpan().addUIClass('icon').addUIClass('icon-plus'))
            .onUIClick(this.addColumn.bind(this, this.UIElement.UITabPanel, null));
        this.UIElement.UITabPanel.tabsDiv.addUI(this.UIElement.UITabPanel.tabsDiv.UIButton);

        return this.UIElement;
    }
    // 컬럼 추가
    addColumn(parent, option) {
        if (option === null) { // 신규 추가 - 기본 패널

        } else { // 기존 패널 출력
            
        }
        // parent.addTab(id, new UISpan().addUIClass('icon'), items);
    }
    // 컬럼 삭제
    removeColumn() {

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