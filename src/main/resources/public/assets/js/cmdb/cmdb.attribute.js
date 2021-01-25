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

    let validationList = [
        {'text': 'None', 'value': ''},
        {'text': 'Char', 'value': 'char'},
        {'text': 'Char128', 'value': 'char128'},
        {'text': 'Number', 'value': 'number'},
        {'text': 'Number8', 'value': 'number8'}
    ];

    let parent = null;
    function init(target) {
        parent = target;
    }

    function makeDetails(attributeType, data) {
        if (typeof parent === 'undefined') { return false; }
        parent.innerHTML = '';
        let attributesProperty = {};
        if (typeof data !== 'undefined' && data !== null) {
            attributesProperty = data;
        }
        let attributeObject = null;
        switch (attributeType) {
            case 'inputbox':
                attributeObject =  new InputBox(attributesProperty);
                break;
            case 'dropdown':
                attributeObject =  new Dropdown(attributesProperty);
                break;
            default:
                break;
        }
        return attributeObject;
    }

    /**
     * Validation 목옥에서 원하는 리스트만 추출.
     *
     * @param list
     * @return {[text, value]}
     */
    function setValidations(list) {
        let validations = [];
        list.forEach(function(v) {
            validationList.map(function (validation) {
                if (v === validation.value) {
                    validations.push(validation);
                }
            });
        });
        return validations
    }

    function InputBox(property) {
        let list = ['', 'char', 'char128', 'number', 'number8'];
        let validations = setValidations(list);
        const validationOptions = validations.map(function (validation) {
            return `<option value='${validation.value}' ${property.validate === validation.value ? "selected='true'" : ""}>${aliceJs.filterXSS(validation.text)}</option>`;
        }).join('');
        const booleanOptions = [{'text': 'Y', 'value': 'true'}, {'text': 'N', 'value': 'false'}].map(function(option) {
            return `<option value='${option.value}' ${property.required === option.value ? "selected='true'" : ""}>${aliceJs.filterXSS(option.text)}</option>`
        }).join('');
        this.template =
            `<div class="flex-column"><label><span class="required">필수조건</span></label><select id="inputbox-required">${booleanOptions}</select></div>` +
            `<div class="flex-column"><label><span class="required">유효성</span></label><select id="inputbox-validation">${validationOptions}</select></div>`;
        parent.insertAdjacentHTML('beforeend', this.template);
    }

    //+ 버튼을 만들어서 클릭시마다 row 추가
    function Dropdown(property) {
        this.template =
            `<div class="attribute">` +
            `<div>` +
            `<div>버튼</div>` +
            `<span>Option</span><span><select><option>11</option></select></span>` +
            `<span>Option</span><span><select><option>11</option></select></span>` +
            `</div>` +
            `</div>`;
        parent.insertAdjacentHTML('beforeend', this.template);
    }

    function checkDetails(type) {
        if (type === attributeTypeList[0].type) {

        }
        return true
    }

    function setDetails(attributeType) {
        let details = {};
        switch (attributeType) {
            case 'inputbox':
                details.validate = parent.querySelector('#inputbox-validation').value;
                details.required = parent.querySelector('#inputbox-required').value;
                break;
            case 'dropdown':

                break;
            default:
                break;
        }

        return details
    }

    exports.attributeTypeList = attributeTypeList

    exports.init = init
    exports.makeDetails = makeDetails
    exports.checkDetails = checkDetails
    exports.setDetails = setDetails

    Object.defineProperty(exports, '__esModule', { value: true });
})));
