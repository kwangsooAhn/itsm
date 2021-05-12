/**
 * SliderTypeProperty Class
 *
 * 슬라이드 형식의 속성타입을 위한 클랙이다.
 * 예를 들어 컬럼 너비와 같은 항목은 슬라이를 드래그하면서 너비를 늘리거나 줄일 수 있다.
 * 이러한 형식의 속성 항목을 위한 클래스이다.
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

export default class SliderTypeProperty {
    constructor(name) {
        this.property = new Property(name, 'slider');
    }

    getPropertyTypeConfig() {
        return this.property.getPropertyConfig();
    }
}