/**
 * RGB Class
 *
 * 색상을 선택하는 속성항목을 위한 클래스이다.
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
    /* 추가적인 설정이 없다. */
}

export default class RgbTypeProperty {
    constructor(name) {
        this.property = new Property(name, 'rgb');
    }

    getPropertyTypeConfig() {
        return this.property.getPropertyConfig();
    }
}