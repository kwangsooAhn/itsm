/**
 * Label Property Class
 *
 * 속성 라벨을 표시하는 클래스이다.
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

export default class LabelProperty {
    constructor(name) {
        this._name = 'form.properties' + name;
        this._type = 'labelProperty';
    }

    getPropertyTypeConfig() {
        return {
            name: this._name,
            type: this._type
        };
    }
}