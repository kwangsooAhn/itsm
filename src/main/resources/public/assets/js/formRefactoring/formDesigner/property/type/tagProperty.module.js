/**
 * Tag Property Class
 *
 * 태그를 추가하는 속성항목을 위한 클래스이다.
 *
 * @author woodajung wdj@brainz.co.kr
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

export default class TagProperty {
    constructor(name) {
        this.property = new Property(name, 'tagProperty');
    }

    getPropertyTypeConfig() {
        return this.property.getPropertyConfig();
    }
}