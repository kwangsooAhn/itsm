/**
 * User Search Property Class
 *
 * zUserSearch 라이브러리를 사용하는 속성항목이다.
 *
 * @author Lim Ji Young <jy.lim@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import { UIDiv, UIInput, UISelect } from '../../../lib/zUI.js';
import ZProperty from '../zProperty.js';

const propertyExtends = {
    selectOptions: [
        {name: i18n.msg('form.properties.userSearch.department'), value: 'department'},
        {name: i18n.msg('form.properties.userSearch.custom'), value:'custom'}
    ]
};

export default class ZUserSearchProperty extends ZProperty {
    constructor(key, name, value, isAlwaysEditable) {
        super(key, name, 'userSearchProperty', value, isAlwaysEditable);
        console.log(key)
        console.log(name)

        this.selectOptions = propertyExtends.selectOptions;
    }
    // DOM Element 생성
    makeProperty(panel) {
        this.panel = panel;
        // 속성 편집 가능여부 체크 - 문서가 '편집'이거나 또는 (문서가 '사용/발행' 이고 항시 편집 가능한 경우)
        this.isEditable = this.panel.editor.isEditable || (!this.panel.editor.isDestory && this.isAlwaysEditable);

        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);

        // 조회 대상 기준
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.UILabel.addUIClass('mt-3');
        this.UIElement.addUI(this.UIElement.UILabel);
        console.log(this.selectOptions[0].value);
        this.UIElement.UISelect = new UISelect()
            .setUIId('userSearch')
            .setUIAttribute('name', this.key)
            .setUIOptions(JSON.parse(JSON.stringify(this.selectOptions)))
            .setUIValue(this.selectOptions[0].value)
            .setUIAttribute('data-value', 'userSearch')
            .onUIChange(this.updateUserSearchTarget.bind(this));
        this.UIElement.addUI(this.UIElement.UISelect);

        // 전부 그리고 hidden 처리

        // 부서별 조회 form
        // 대상 목록 지정 form

        if (!this.isEditable) {
            this.UIElement.UISelect.addUIClass('readonly');
        }

        return this.UIElement;
    }

    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
    }

    // 검색조건 변경 시, 하위항목 form을 업데이트한다.
    updateUserSearchTarget(e) {
        switch (e.target.value) {
            case 'organization':   // 부서별 조회
                break;
            case 'custom':      // 대상목록 지정
                break;
        }
    }

    // 속성 변경시 발생하는 이벤트 핸들러
    updateProperty(elem) {
    }
}
