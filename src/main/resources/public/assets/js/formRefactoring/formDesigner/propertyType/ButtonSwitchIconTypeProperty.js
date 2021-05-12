/**
 * Button Switch Icon Class
 *
 * 속성을 선택하는 아이콘들 중에서 1개만 선택할 수 있는 속성 타입을 위한 클래스이다.
 * 예를 들어 정렬 속성 같은 경우, 좌측, 중앙, 우측 정렬중 1개만 선택이 가능하다.
 *
 * @author Jung Hee Chan <hcjung@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import Property from "../property.js";

const propertyExtends = {
    /* 슬라이드 속성 타입은 추가적인 설정이 없다. */
}

export default class ButtonSwitchIconTypeProperty {
    constructor(name) {
        this.property = new Property(name, 'button-switch-icon');
    }

    getPropertyTypeConfig() {
        return this.property.getPropertyConfig();
    }
}