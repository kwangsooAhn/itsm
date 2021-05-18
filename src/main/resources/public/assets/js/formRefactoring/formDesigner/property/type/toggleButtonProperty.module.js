/**
 * Toggle Button Property Class
 *
 * 속성을 선택하는 아이콘들을 모임이며 중복선택이 가능한 속성 타입을 위한 클래스이다.
 * 예를 들어 폰트 옵션의 경우 Bold, Italic 등의 중복 선택이 가능하다.
 *
 * @author Jung Hee Chan <hcjung@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import Property from '../property.module.js';

const propertyExtends = {
    /* 슬라이드 속성 타입은 추가적인 설정이 없다. */
};

export default class ToggleButtonProperty {
    constructor(name) {
        this.property = new Property(name, 'toggleButtonProperty');
    }

    getPropertyTypeConfig() {
        return this.property.getPropertyConfig();
    }
}