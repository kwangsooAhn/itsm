/**
 * SelectTypeProperty Class
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
import Property from "../property.js";

const propertyExtends = {
    options : []
}

export default class SelectTypeProperty {
    constructor(name, options) {
        this.property = new Property(name, 'select');
        this.options = options;
    }

    getPropertyTypeConfig() {
        let propertyTypeConfig = this.property.getPropertyConfig();
        propertyTypeConfig.options = this.options;
        return propertyTypeConfig;
    }
}