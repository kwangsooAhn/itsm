import Property from "../property.js";

const propertyExtends = {
    /* 슬라이드 속성 타입은 추가적인 설정이 없다. */
}

export default class SwitchTypeProperty {
    constructor(name) {
        this.property = new Property(name, 'switch');
    }

    getPropertyTypeConfig() {
        return this.property.getPropertyConfig();
    }
}