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
        {'type': 'custom-code', 'name': 'Custom Code'}
    ];

    let validationList = [
        {'text': 'None', 'value': ''},
        {'text': 'Char', 'value': 'char'},
        {'text': 'Number', 'value': 'number'}
    ];

    let parent = null;
    let customCodeList = null;

    function init(target) {
        parent = target;

        //load custom-code list.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/custom-codes?viewType=editor',
            async: false,
            callbackFunc: function(xhr) {
                customCodeList = JSON.parse(xhr.responseText);
            },
            contentType: 'application/json; charset=utf-8'
        });
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
            case 'radio':
                attributeObject = new Radiobox(attributesProperty);
                break;
            case 'checkbox':
                attributeObject = new Checkbox(attributesProperty);
                break;
            case 'custom-code':
                attributeObject = new CustomCode(attributesProperty);
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

    /**
     * Inputbox.
     *
     * @param {Object} property Attribute 데이터
     * @constructor
     */
    function InputBox(property) {
        const objectId = attributeTypeList[0].type; // inputbox
        const list = ['', 'char', 'number'];
        const validations = setValidations(list);
        const validationOptions = validations.map(function (validation) {
            return `<option value='${validation.value}' ${property.validate === validation.value ? "selected='true'" : ""}>${aliceJs.filterXSS(validation.text)}</option>`;
        }).join('');
        const booleanOptions = [{'text': 'Y', 'value': 'true'}, {'text': 'N', 'value': 'false'}].map(function(option) {
            return `<option value='${option.value}' ${property.required === option.value ? "selected='true'" : ""}>${aliceJs.filterXSS(option.text)}</option>`
        }).join('');
        this.template =
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4"><label><span class="mr-1">필수조건</span><span class="required"></span></label></div>` +
            `<div class="flex-column col-9"><select id="${objectId}-required">${booleanOptions}</select></div>` +
            `</div>` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4"><label><span class="mr-1">유효성</span><span class="required"></span></label></div>` +
            `<div class="flex-column col-9"><select id="${objectId}-validation">${validationOptions}</select></div>` +
            `</div>`;
        parent.insertAdjacentHTML('beforeend', this.template);
    }

    /**
     * Dropdown.
     *
     * @param {Object} property Attribute 데이터
     * @constructor
     */
    function Dropdown(property) {
        const objectId = attributeTypeList[1].type; // dropdown
        this.template =
            `<div class="flex-row justify-content-end"><button id="${objectId}_add" type="button" class="default-line plus"><span class="icon-plus"></span></button></div>`;

        parent.insertAdjacentHTML('beforeend', this.template);

        const addBtn = document.getElementById(objectId + '_add');
        addBtn.addEventListener('click', function (e) {
            e.stopPropagation();
            let rowId = workflowUtil.generateUUID();
            let rowElement =
                `<div class="flex-row mt-2">` +
                `<div class="flex-column col-1"><label><span class="mr-1">Label</span><span class="required"></span></label></div>` +
                `<div class="flex-column col-5 mr-4"><input maxlength="50" required></div>` +
                `<div class="flex-column col-1"><label><span>Value</span></label></div>` +
                `<div class="flex-column col-5"><input maxlength="50"></div>` +
                `<div class="flex-column col-1"><button id="${rowId}_delete" type="button" class="delete"><span class="icon-delete-gray"></span></button></div>` +
                `</div>`;
            parent.insertAdjacentHTML('beforeend', rowElement);

            const deleteBtn = document.getElementById(rowId + '_delete');
            deleteBtn.addEventListener('click', function (e) {
                e.stopPropagation();
                this.parentElement.parentElement.remove();
            });
        });

        if (property.option !== undefined && property.option !== null) {
            property.option.forEach(function() { addBtn.click(); });
            document.querySelectorAll('#details > .flex-row:not(:first-child)').forEach(function (object, index) {
                object.querySelectorAll('input')[0].value = property.option[index].text;
                object.querySelectorAll('input')[1].value = property.option[index].value;
            });
        }
    }

    /**
     * Radio.
     *
     * @param {Object} property Attribute 데이터
     * @constructor
     */
    function Radiobox(property) {
        const objectId = attributeTypeList[2].type; // radio
        this.template =
            `<div class="flex-row justify-content-end"><button id="${objectId}_add" type="button" class="default-line plus"><span class="icon-plus"></span></button></div>`;

        parent.insertAdjacentHTML('beforeend', this.template);

        const addBtn = document.getElementById(objectId + '_add');
        addBtn.addEventListener('click', function (e) {
            e.stopPropagation();
            let rowId = workflowUtil.generateUUID();
            let rowElement =
                `<div class="flex-row mt-2">` +
                `<div class="flex-column col-1"><label><span class="mr-1">Label</span><span class="required"></span></label></div>` +
                `<div class="flex-column col-5 mr-4"><input maxlength="50" required></div>` +
                `<div class="flex-column col-1"><label><span>Value</span></label></div>` +
                `<div class="flex-column col-5"><input maxlength="50"></div>` +
                `<div class="flex-column col-1"><button id="${rowId}_delete" type="button" class="delete"><span class="icon-delete-gray"></span></button></div>` +
                `</div>`;
            parent.insertAdjacentHTML('beforeend', rowElement);

            const deleteBtn = document.getElementById(rowId + '_delete');
            deleteBtn.addEventListener('click', function (e) {
                e.stopPropagation();
                this.parentElement.parentElement.remove();
            });
        });

        if (property.option !== undefined && property.option !== null) {
            property.option.forEach(function() { addBtn.click(); });
            document.querySelectorAll('#details > .flex-row:not(:first-child)').forEach(function (object, index) {
                object.querySelectorAll('input')[0].value = property.option[index].text;
                object.querySelectorAll('input')[1].value = property.option[index].value;
            });
        }
    }

    /**
     * Checkbox.
     *
     * @param {Object} property Attribute 데이터
     * @constructor
     */
    function Checkbox(property) {
        const objectId = attributeTypeList[3].type; // checkbox
        this.template =
            `<div class="flex-row justify-content-end"><button id="${objectId}_add" type="button" class="default-line plus"><span class="icon-plus"></span></button></div>`;

        parent.insertAdjacentHTML('beforeend', this.template);

        const addBtn = document.getElementById(objectId + '_add');
        addBtn.addEventListener('click', function (e) {
            e.stopPropagation();
            let rowId = workflowUtil.generateUUID();
            let rowElement =
                `<div class="flex-row mt-2">` +
                `<div class="flex-column col-1"><label><span class="mr-1">Label</span><span class="required"></span></label></div>` +
                `<div class="flex-column col-4 mr-4"><input maxlength="50" required></div>` +
                `<div class="flex-column col-1"><label><span>Value</span></label></div>` +
                `<div class="flex-column col-4 mr-4"><input maxlength="50"></div>` +
                `<div class="flex-column col-1"><label><span>Check</span></label></div>` +
                `<div class="flex-column col-1"><input type="checkbox"></div>` +
                `<div class="flex-column col-1"><button id="${rowId}_delete" type="button" class="delete"><span class="icon-delete-gray"></span></button></div>` +
                `</div>`;
            parent.insertAdjacentHTML('beforeend', rowElement);

            const deleteBtn = document.getElementById(rowId + '_delete');
            deleteBtn.addEventListener('click', function (e) {
                e.stopPropagation();
                this.parentElement.parentElement.remove();
            });
        });

        if (property.option !== undefined && property.option !== null) {
            property.option.forEach(function() { addBtn.click(); });
            document.querySelectorAll('#details > .flex-row:not(:first-child)').forEach(function (object, index) {
                object.querySelectorAll('input')[0].value = property.option[index].text;
                object.querySelectorAll('input')[1].value = property.option[index].value;
                object.querySelectorAll('input')[2].checked = property.option[index].checked;
            });
        }
    }

    /**
     * Custom-code.
     *
     * @param {Object} property Attribute 데이터
     * @constructor
     */
    function CustomCode(property) {
        const objectId = attributeTypeList[4].type; // custom-code

        // custom-code
        const customCodeOptions = customCodeList.map(function(option) {
            return `<option value='${option.customCodeId}' ${property.customCode === option.customCodeId ? "selected='true'" : ""}>${aliceJs.filterXSS(option.customCodeName)}</option>`
        }).join('');

        const defaultType = property.default !== undefined ? property.default.type : 'none';
        const defaultValue = property.default !== undefined ? property.default.value : '';
        const buttonText = property.button !== undefined ? property.button : '';

        // session
        const sessionOptions = [{'text': '이름', 'value': 'userName'}, {'text': '부서', 'value': 'department'}].map(function(option) {
            return `<option value='${option.value}' ${(defaultType === 'session' && defaultValue === option.value) ? "selected='true'" : ""}>${aliceJs.filterXSS(option.text)}</option>`
        }).join('');

        this.template =
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4"><label><span>커스텀 코드</span></label></div>` +
            `<div class="flex-column col-9"><select id="${objectId}-select">${customCodeOptions}</select></div>` +
            `</div>` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4"><label><span>기본값</span></label></div>` +
            `<div class="flex-column col-9"><label><input name="${objectId}-default" id="${objectId}-none" type="radio" value="none" ${defaultType === 'none' ? "checked='true'" : ""}><span class="label ml-2">없음</span></label></div>` +
            `</div>` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4"><label><span></span></label></div>` +
            `<div class="flex-column col-2 mr-4"><label><input name="${objectId}-default" id="${objectId}-session" type="radio" value="session" ${defaultType === 'session' ? "checked='true'" : ""}><span class="label ml-2">세션</span></label></div>` +
            `<div class="flex-column col-7"><select id="${objectId}-default-session">${sessionOptions}</select></div>` +
            `</div>` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4"><label><span></span></label></div>` +
            `<div class="flex-column col-2 mr-4"><label><input name="${objectId}-default" id="${objectId}-code" type="radio" value="code" ${defaultType === 'code' ? "checked='true'" : ""}><span class="label ml-2">코드</span></label></div>` +
            `<div class="flex-column col-7"><select id="${objectId}-default-code"></select></div>` +
            `</div>` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4"><label><span>버튼</span></label></div>` +
            `<div class="flex-column col-9"><input id="${objectId}-button" maxlength="100" value="${buttonText}"></div>` +
            `</div>`;

        parent.insertAdjacentHTML('beforeend', this.template);

        // custom-code 변경 시 데이터 변경
        const customCodeObject = document.getElementById(objectId + '-select');
        customCodeObject.addEventListener('change', function(e) {
            e.stopPropagation();
            aliceJs.sendXhr({
                method: 'GET',
                url: '/rest/custom-codes/' + this.value,
                callbackFunc: function(xhr) {
                    let customCodeData = JSON.parse(xhr.responseText);
                    let customCodeDataObject = document.getElementById(objectId + '-default-code');
                    customCodeDataObject.innerHTML = customCodeData.map(function(option) {
                        return `<option value='${option.value}' ${defaultValue === option.value ? "selected='true'" : ""}>${aliceJs.filterXSS(option.key)}</option>`
                    }).join('');
                    aliceJs.initDesignedSelectTag();
                },
                contentType: 'application/json; charset=utf-8',
                showProgressbar: false
            });
        });

        const evt = document.createEvent('HTMLEvents');
        evt.initEvent('change', false, true);
        customCodeObject.dispatchEvent(evt);

        //disabled
    }

    /**
     * Validation Check.
     *
     * @param type
     * @return {boolean}
     */
    function checkDetails(type) {
        let isValid = true;
        if (type === 'dropdown' || type === 'radio' || type === 'checkbox') {
            let detailsObject = document.querySelectorAll('#details > .flex-row:not(:first-child)');
            let values = [];
            for (let i = 0, len = detailsObject.length; i < len; i++) {
                let labelObject = detailsObject[i].querySelectorAll('input')[0];
                let valueObject = detailsObject[i].querySelectorAll('input')[1];
                if (labelObject.value.trim() === '') {
                    aliceJs.alertWarning(i18n.msg('common.msg.requiredEnter'), function() {
                        labelObject.focus();
                    });
                    isValid = false;
                    break;
                }
                if (values.indexOf(valueObject.value) > -1) {
                    aliceJs.alertWarning(i18n.msg('validation.msg.dataNotDuplicate'));
                    isValid = false;
                    break;
                }
                values.push(valueObject.value);
            }
        }
        return isValid;
    }

    /**
     * Attribute 세부 정보 데이터를 저장하기 위한 작업.
     *
     * @param attributeType
     * @return {{String}}
     */
    function setDetails(attributeType) {
        let details = {};
        switch (attributeType) {
            case 'inputbox':
                details.validate = parent.querySelector('#' + attributeTypeList[0].type + '-validation').value;
                details.required = parent.querySelector('#' + attributeTypeList[0].type + '-required').value;
                break;
            case 'dropdown':
                let dropdownOption = [];
                document.querySelectorAll('#details > .flex-row:not(:first-child)').forEach(function (object) {
                    dropdownOption.push({
                        text: object.querySelectorAll('input')[0].value.trim(),
                        value: object.querySelectorAll('input')[1].value.trim()
                    });
                });
                details.option = dropdownOption;
                break;
            case 'radio':
                let radioOption = [];
                document.querySelectorAll('#details > .flex-row:not(:first-child)').forEach(function (object) {
                    radioOption.push({
                        text: object.querySelectorAll('input')[0].value.trim(),
                        value: object.querySelectorAll('input')[1].value.trim()
                    });
                });
                details.option = radioOption;
                break;
            case 'checkbox':
                let checkOption = [];
                document.querySelectorAll('#details > .flex-row:not(:first-child)').forEach(function (object) {
                    checkOption.push({
                        text: object.querySelectorAll('input')[0].value.trim(),
                        value: object.querySelectorAll('input')[1].value.trim(),
                        checked: object.querySelectorAll('input')[2].checked,
                    });
                });
                details.option = checkOption;
                break;
            case 'custom-code':
                const defaultType = document.querySelector('input[name="custom-code-default"]:checked').value;
                let defaultValue = '';
                switch (defaultType) {
                    case 'session':
                        defaultValue = parent.querySelector('#' + attributeTypeList[4].type + '-default-session').value;
                        break;
                    case 'code':
                        defaultValue = parent.querySelector('#' + attributeTypeList[4].type + '-default-code').value;
                        break;
                    default:
                        break;
                }
                details.customCode = parent.querySelector('#' + attributeTypeList[4].type + '-select').value;
                details.default = {
                    type: defaultType,
                    value: defaultValue
                };
                details.button = parent.querySelector('#' + attributeTypeList[4].type + '-button').value;
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
