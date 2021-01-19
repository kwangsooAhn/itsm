/**
 * @projectDescription CMDB Attribute Library
 *
 * @author phc
 * @version 1.0
 *
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.attribute = global.attribute || {})));
}(this, (function (exports) {
    'use strict';

    const attributeTypeList = [
        {'type': 'inputbox', 'name': 'Input Box'},
        {'type': 'dropdown', 'name': 'Dropdown'},
        {'type': 'radio', 'name': 'Radio Button'},
        {'type': 'checkbox', 'name': 'Checkbox'},
        {'type': 'custom-code', 'name': 'Custom Code'},
        {'type': 'table', 'name': 'Table'}
    ];

    function checkDetails(type) {
        if (type === attributeTypeList[0].type) {

        }
        return true
    }

    exports.attributeTypeList = attributeTypeList
    exports.checkDetails = checkDetails

    Object.defineProperty(exports, '__esModule', { value: true });
})));
