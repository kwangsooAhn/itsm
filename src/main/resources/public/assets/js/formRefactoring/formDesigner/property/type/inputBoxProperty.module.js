/**
 * InputBox Property Class
 *
 * 속성의 값을 사용자가 직접 타이핑해서 입력하는 input box 형태의 속성 타입을 위한 클래스이다.
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
    /* input box 속성 타입은 추가적인 설정이 없다. */
};

export default class InputBoxProperty {
    constructor(name) {
        this.property = new Property(name, 'inputBoxProperty');
    }

    getPropertyTypeConfig() {
        return this.property.getPropertyConfig();
    }
}