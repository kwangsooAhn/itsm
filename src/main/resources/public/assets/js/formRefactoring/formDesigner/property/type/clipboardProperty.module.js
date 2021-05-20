/**
 * Clipboard Property Class
 *
 * 기본적으로는 input type 과 동일한 속성 입력을 지원하지만
 * 속성 값을 입력하는 항목 옆에 값을 복사하는 아이콘이 있는 형태이다.
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

export default class ClipboardProperty {
    constructor(name) {
        this.property = new Property(name, 'clipboardProperty');
    }

    getPropertyTypeConfig() {
        return this.property.getPropertyConfig();
    }
}