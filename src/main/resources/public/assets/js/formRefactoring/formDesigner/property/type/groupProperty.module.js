/**
 * Group Property Class
 *
 * 그룹 형태로 하위 속성을 묶어주는 클래스이다.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
const propertyExtends = {
    /* 추가적인 설정이 없다. */
};

export default class GroupProperty {
    constructor(name) {
        this._name = 'form.properties' + name;
        this._type = 'groupProperty';
        this._children = {};
    }

    getPropertyTypeConfig() {
        return {
            name: this._name,
            type: this._type,
            children: this._children
        };
    }
}