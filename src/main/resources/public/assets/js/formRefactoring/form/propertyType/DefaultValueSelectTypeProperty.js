/**
 * DefaultValueSelectTypeProperty Class
 *
 * 컴포넌트의 기본 값을 어떤 방식으로 제공할지 선택하는 속성항목이다.
 * 현재는 inputBox 에서만 사용되고 있으며 옵션들도 inputBox 용으로 맞추어져 있다.
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
    options : [
        {name: 'form.properties.direct', value: 'input'},
        {name: 'form.properties.auto', value: 'select'}
    ],
    selectOption : [
        {name: 'form.properties.userKey', value: 'userKey'},
        {name: 'form.properties.userId', value: 'userId'},
        {name: 'form.properties.userName', value: 'userName'},
        {name: 'form.properties.email', value: 'email'},
        {name: 'form.properties.jobPosition', value: 'position'},
        {name: 'form.properties.department', value: 'department'},
        {name: 'form.properties.officeNumber', value: 'officeNumber'},
        {name: 'form.properties.officeNumber', value: 'officeNumber'}
    ]
}

export default class DefaultValueSelectTypeProperty {
    constructor(name) {
        this.property = new Property(name, 'default-value-select');
    }

    getPropertyTypeConfig() {
        let propertyTypeConfig = this.property.getPropertyConfig();
        propertyTypeConfig.option = propertyExtends.options;
        propertyTypeConfig.selectOption = propertyExtends.selectOption;
        return propertyTypeConfig;
    }
}