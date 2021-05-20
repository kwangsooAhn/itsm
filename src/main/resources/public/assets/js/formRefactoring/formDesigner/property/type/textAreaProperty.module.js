/**
 * Textarea Property Class
 *
 * Textarea 형태의 입력항목을 가지는 속성 타입을 위한 클래스이다.
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
    /* 추가적인 설정이 없다. */
};

export default class TextAreaProperty {
    constructor(name) {
        this.property = new Property(name, 'textAreaProperty');
    }

    getPropertyTypeConfig() {
        return this.property.getPropertyConfig();
    }
}