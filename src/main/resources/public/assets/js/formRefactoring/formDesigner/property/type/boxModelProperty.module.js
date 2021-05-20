/**
 * Box Model Property Class
 *
 * 기본적으로는 input type 과 동일한 속성 입력을 지원하지만 (그런면에서 input 타입과 유사하지만)
 * 상하좌우와 같이 복수개의 데이터를 한 묶음으로 처리하는 클래스이다.
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

export default class BoxModelProperty {
    constructor(name) {
        this.property = new Property(name, 'boxModelProperty');
    }

    getPropertyTypeConfig() {
        return this.property.getPropertyConfig();
    }
}