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

    // Attribute 타입 목록
    const attributeTypeList = [
        {'type': 'inputbox', 'name': 'Input Box'},
        {'type': 'dropdown', 'name': 'Dropdown'},
        {'type': 'radio', 'name': 'Radio Button'},
        {'type': 'checkbox', 'name': 'Checkbox'},
        {'type': 'custom-code', 'name': 'Custom Code'}
    ];

    // Validation 목록
    let validationList = [
        {'text': 'None', 'value': ''},
        {'text': 'Char', 'value': 'char'},
        {'text': 'Number', 'value': 'number'}
    ];

    let parent = null;
    let customCodeList = null;

    /**
     * 초기 데이터 셋팅.
     * 
     * @param target
     */
    function init(target) {
        parent = target;

        // load custom-code list.
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

    /**
     * Attribute 타입에 따른 세부 설정 구현.
     *
     * @param attributeType
     * @param data
     * @return {null|boolean}
     */
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
     * Validation 목록에서 원하는 리스트만 추출.
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
            `<div class="flex-column col-2 mr-4"><label><span class="mr-1">${i18n.msg('cmdb.attribute.label.option.required')}</span><span class="required"></span></label></div>` +
            `<div class="flex-column col-9"><select id="${objectId}-required">${booleanOptions}</select></div>` +
            `</div>` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4"><label><span class="mr-1">${i18n.msg('cmdb.attribute.label.option.validate')}</span><span class="required"></span></label></div>` +
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
                `<div class="flex-column col-1"><label><span class="mr-1">${i18n.msg('cmdb.attribute.label.option.label')}</span><span class="required"></span></label></div>` +
                `<div class="flex-column col-5 mr-4"><input maxlength="50" required></div>` +
                `<div class="flex-column col-1"><label><span>${i18n.msg('cmdb.attribute.label.option.value')}</span></label></div>` +
                `<div class="flex-column col-5"><input maxlength="50"></div>` +
                `<div class="flex-column col-1"><button id="${rowId}_delete" type="button" class="btn-delete"><span class="icon-delete-gray"></span></button></div>` +
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
                `<div class="flex-column col-1"><label><span class="mr-1">${i18n.msg('cmdb.attribute.label.option.label')}</span><span class="required"></span></label></div>` +
                `<div class="flex-column col-5 mr-4"><input maxlength="50" required></div>` +
                `<div class="flex-column col-1"><label><span>${i18n.msg('cmdb.attribute.label.option.value')}</span></label></div>` +
                `<div class="flex-column col-5"><input maxlength="50"></div>` +
                `<div class="flex-column col-1"><button id="${rowId}_delete" type="button" class="btn-delete"><span class="icon-delete-gray"></span></button></div>` +
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
                `<div class="flex-column col-1"><label><span class="mr-1">${i18n.msg('cmdb.attribute.label.option.label')}</span><span class="required"></span></label></div>` +
                `<div class="flex-column col-4 mr-4"><input maxlength="50" required></div>` +
                `<div class="flex-column col-1"><label><span>${i18n.msg('cmdb.attribute.label.option.value')}</span></label></div>` +
                `<div class="flex-column col-4 mr-4"><input maxlength="50"></div>` +
                `<div class="flex-column col-1"><label><span>${i18n.msg('cmdb.attribute.label.option.check')}</span></label></div>` +
                `<div class="flex-column col-1"><input type="checkbox"></div>` +
                `<div class="flex-column col-1"><button id="${rowId}_delete" type="button" class="btn-delete"><span class="icon-delete-gray"></span></button></div>` +
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
        const sessionOptions = [{'text': i18n.msg('user.label.name'), 'value': 'userName'}, {'text': i18n.msg('user.label.department'), 'value': 'department'}].map(function(option) {
            return `<option value='${option.value}' ${(defaultType === 'session' && defaultValue === option.value) ? "selected='true'" : ""}>${aliceJs.filterXSS(option.text)}</option>`
        }).join('');

        this.template =
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4"><label><span>${i18n.msg('customCode.label.customCode')}</span></label></div>` +
            `<div class="flex-column col-9"><select id="${objectId}-select">${customCodeOptions}</select></div>` +
            `</div>` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4"><label><span>${i18n.msg('cmdb.attribute.label.default')}</span></label></div>` +
            `<div class="flex-column col-1"><label class="radio"><input name="${objectId}-default" id="${objectId}-none" type="radio" value="none" ${defaultType === 'none' ? "checked='true'" : ""}><span></span><span class="label">${i18n.msg('cmdb.attribute.label.option.none')}</span></label></div>` +
            `<div class="flex-column col-8"></div>` +
            `</div>` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4"><label><span></span></label></div>` +
            `<div class="flex-column col-1"><label class="radio"><input name="${objectId}-default" id="${objectId}-session" type="radio" value="session" ${defaultType === 'session' ? "checked='true'" : ""}><span></span><span class="label">${i18n.msg('cmdb.attribute.label.option.session')}</span></label></div>` +
            `<div class="flex-column col-1"></div>` +
            `<div class="flex-column col-7"><select id="${objectId}-default-session" ${defaultType === 'session' ? '': "disabled='true'"}>${sessionOptions}</select></div>` +
            `</div>` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4"><label><span></span></label></div>` +
            `<div class="flex-column col-1"><label class="radio"><input name="${objectId}-default" id="${objectId}-code" type="radio" value="code" ${defaultType === 'code' ? "checked='true'" : ""}><span></span><span class="label">${i18n.msg('cmdb.attribute.label.option.code')}</span></label></div>` +
            `<div class="flex-column col-1"></div>` +
            `<div class="flex-column col-7"><select id="${objectId}-default-code" ${defaultType === 'code' ? '': "disabled='true'"}></select></div>` +
            `</div>` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4"><label><span>${i18n.msg('cmdb.attribute.label.buttonText')}</span></label></div>` +
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
                        return `<option value='${option.key}' ${defaultValue === option.key ? "selected='true'" : ""}>${aliceJs.filterXSS(option.value)}</option>`
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

        // disabled
        const defaultNoneObject = document.getElementById(objectId + '-none');
        const defaultSessionObject = document.getElementById(objectId + '-session');
        const defaultCodeObject = document.getElementById(objectId + '-code');

        defaultNoneObject.addEventListener('click', function(e) {
            e.stopPropagation();
            document.getElementById(objectId + '-default-session').disabled = true;
            document.getElementById(objectId + '-default-code').disabled = true;
            aliceJs.initDesignedSelectTag();
        });
        defaultSessionObject.addEventListener('click', function(e) {
            e.stopPropagation();
            document.getElementById(objectId + '-default-session').disabled = false;
            document.getElementById(objectId + '-default-code').disabled = true;
            aliceJs.initDesignedSelectTag();
        });
        defaultCodeObject.addEventListener('click', function(e) {
            e.stopPropagation();
            document.getElementById(objectId + '-default-session').disabled = true;
            document.getElementById(objectId + '-default-code').disabled = false;
            aliceJs.initDesignedSelectTag();
        });
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
            let labels = [];
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
                if (labels.indexOf(labelObject.value.trim()) > -1 || values.indexOf(valueObject.value.trim()) > -1) {
                    aliceJs.alertWarning(i18n.msg('validation.msg.dataNotDuplicate'));
                    isValid = false;
                    break;
                }
                labels.push(labelObject.value.trim());
                values.push(valueObject.value.trim());
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
