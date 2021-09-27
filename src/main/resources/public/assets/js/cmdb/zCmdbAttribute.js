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
            (factory((global.zCmdbAttribute = global.zCmdbAttribute || {})));
}(this, (function (exports) {
    'use strict';

    // Attribute 타입 목록
    const attributeTypeList = [
        {'type': 'inputbox', 'name': 'Input Box'},
        {'type': 'dropdown', 'name': 'Dropdown'},
        {'type': 'radio', 'name': 'Radio Button'},
        {'type': 'checkbox', 'name': 'Checkbox'},
        {'type': 'custom-code', 'name': 'Custom Code'},
        {'type': 'group-list', 'name': 'Group List'}
    ];

    // Validation 목록
    let validationList = [
        {'text': 'None', 'value': ''},
        {'text': 'Char', 'value': 'char'},
        {'text': 'Number', 'value': 'number'}
    ];

    let parent = null;
    let customCodeList = null;
    let attributeId = '';
    let attributeMap = []; // 저장된 데이터
    let attributeMapTemp = []; // 화면에서 사용자가 변경 중인 데이터

    /**
     * 초기 데이터 셋팅.
     *
     * @param target
     */
    function init(target) {
        parent = target;
        attributeId = document.getElementById('attributeId').value;

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
        if (typeof parent === 'undefined') {
            return false;
        }
        parent.innerHTML = '';
        attributeMap.length = 0;
        attributeMapTemp.length = 0;
        if (parent.previousElementSibling.querySelector('#button_add') !== null) {
            parent.previousElementSibling.querySelector('#button_add').remove();
        }

        const attributesProperty = Object.assign({}, data);
        let attributeObject = null;
        switch (attributeType) {
            case 'inputbox':
                attributeObject = new InputBox(attributesProperty);
                break;
            case 'dropdown':
                attributeObject = new Dropdown(attributesProperty);
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
            case 'group-list':
                attributeObject = new GroupList(attributesProperty);
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
        return validations;
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
            return `<option value='${validation.value}' ` +
                `${property.validate === validation.value ? 'selected=\'true\'' : ''}>` +
                `${aliceJs.filterXSS(validation.text)}</option>`;
        }).join('');
        const booleanOptions = [{'text': 'Y', 'value': 'true'}, {'text': 'N', 'value': 'false'}].map(function(option) {
            return `<option value='${option.value}' ` +
                `${property.required === option.value ? 'selected=\'true\'' : ''}>` +
                `${aliceJs.filterXSS(option.text)}</option>`;
        }).join('');
        const maxLengthValue = property.maxLength !== undefined ? property.maxLength : '100';
        const minLengthValue = property.minLength !== undefined ? property.minLength : '0';
        this.template =
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4">` +
                `<label>` +
                    `<span class="mr-1">${i18n.msg('cmdb.attribute.label.option.required')}</span>` +
                    `<span class="required"></span>` +
                `</label>` +
            `</div>` +
            `<div class="flex-column col-9"><select id="${objectId}-required">${booleanOptions}</select></div>` +
            `</div>` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4">` +
                `<label>` +
                    `<span class="mr-1">${i18n.msg('cmdb.attribute.label.option.validate')}</span>` +
                    `<span class="required"></span>` +
                `</label>` +
            `</div>` +
            `<div class="flex-column col-9"><select id="${objectId}-validation">${validationOptions}</select></div>` +
            `</div>` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4">` +
                `<label><span class="mr-1">${i18n.msg('cmdb.attribute.label.option.maxLength')}</span></label>` +
            `</div>` +
            `<div class="flex-column col-9">` +
            `<input type="text" class="z-input" id="${objectId}-maxLength" maxlength="100" value="${maxLengthValue}">` +
            `</div>` +
            `</div>` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4">` +
                `<label><span class="mr-1">${i18n.msg('cmdb.attribute.label.option.minLength')}</span></label>` +
            `</div>` +
            `<div class="flex-column col-9">` +
            `<input type="text" class="z-input" id="${objectId}-minLength" maxlength="100" value="${minLengthValue}">` +
            `</div>` +
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
            `<div class="float-right" id="button_add">` +
                `<button id="${objectId}_add" type="button" class="z-button-icon extra">` +
                    `<span class="z-icon i-plus"></span>` +
                `</button>` +
            `</div>`;

        parent.previousElementSibling.insertAdjacentHTML('beforeend', this.template);

        const addBtn = document.getElementById(objectId + '_add');
        addBtn.addEventListener('click', function (e) {
            e.stopPropagation();
            let rowId = ZWorkflowUtil.generateUUID();
            let rowElement =
                `<div class="flex-row mt-2">` +
                `<div class="flex-column col-1">` +
                    `<label>` +
                        `<span class="mr-1">${i18n.msg('cmdb.attribute.label.option.label')}</span>` +
                        `<span class="required"></span>` +
                    `</label>` +
                `</div>` +
                `<div class="flex-column col-5 mr-4">` +
                    `<input type="text" class="z-input" maxlength="50" required="true" required ` +
                    `data-validation-required-name="${i18n.msg('cmdb.attribute.label.option.label')}">` +
                `</div>` +
                `<div class="flex-column col-1"><label>` +
                    `<span>${i18n.msg('cmdb.attribute.label.option.value')}</span></label></div>` +
                `<div class="flex-column col-5"><input type="text" class="z-input" maxlength="50"></div>` +
                `<div class="flex-column col-1">` +
                    `<button id="${rowId}_delete" type="button" class="z-button-icon extra">` +
                        `<span class="z-icon i-delete"></span>` +
                    `</button>` +
                `</div>` +
                `</div>`;
            parent.insertAdjacentHTML('beforeend', rowElement);

            const deleteBtn = document.getElementById(rowId + '_delete');
            deleteBtn.addEventListener('click', function (e) {
                e.stopPropagation();
                this.parentElement.parentElement.remove();
            });
        });

        if (property.option !== undefined && property.option !== null) {
            property.option.forEach(function () {
                addBtn.click();
            });
            document.querySelectorAll('#details > .flex-row').forEach(function (object, index) {
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
            `<div class="float-right" id="button_add">` +
                `<button id="${objectId}_add" type="button" class="z-button-icon extra">` +
                    `<span class="z-icon i-plus"></span>` +
                `</button>` +
            `</div>`;

        parent.previousElementSibling.insertAdjacentHTML('beforeend', this.template);

        const addBtn = document.getElementById(objectId + '_add');
        addBtn.addEventListener('click', function (e) {
            e.stopPropagation();
            let rowId = ZWorkflowUtil.generateUUID();
            let rowElement =
                `<div class="flex-row mt-2">` +
                `<div class="flex-column col-1">` +
                    `<label>` +
                        `<span class="mr-1">${i18n.msg('cmdb.attribute.label.option.label')}</span>` +
                        `<span class="required"></span>` +
                    `</label>` +
                `</div>` +
                `<div class="flex-column col-5 mr-4">` +
                    `<input type="text" class="z-input" maxlength="50" required="true" ` +
                    `data-validation-required-name="${i18n.msg('cmdb.attribute.label.option.label')}">` +
                `</div>` +
                `<div class="flex-column col-1">` +
                    `<label><span>${i18n.msg('cmdb.attribute.label.option.value')}</span></label>` +
                `</div>` +
                `<div class="flex-column col-5"><input type="text" class="z-input" maxlength="50"></div>` +
                `<div class="flex-column col-1">` +
                    `<button id="${rowId}_delete" type="button" class="z-button-icon extra">` +
                        `<span class="z-icon i-delete"></span>` +
                    `</button>` +
                `</div>` +
                `</div>`;
            parent.insertAdjacentHTML('beforeend', rowElement);

            const deleteBtn = document.getElementById(rowId + '_delete');
            deleteBtn.addEventListener('click', function (e) {
                e.stopPropagation();
                this.parentElement.parentElement.remove();
            });
        });

        if (property.option !== undefined && property.option !== null) {
            property.option.forEach(function () {
                addBtn.click();
            });
            document.querySelectorAll('#details > .flex-row').forEach(function (object, index) {
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
            `<div class="float-right" id="button_add">` +
                `<button id="${objectId}_add" type="button" class="z-button-icon extra">` +
                    `<span class="z-icon i-plus"></span>` +
                `</button>` +
            `</div>`;

        parent.previousElementSibling.insertAdjacentHTML('beforeend', this.template);

        const addBtn = document.getElementById(objectId + '_add');
        addBtn.addEventListener('click', function (e) {
            e.stopPropagation();
            let rowId = ZWorkflowUtil.generateUUID();
            let rowElement =
                `<div class="flex-row mt-2">` +
                `<div class="flex-column col-1">` +
                    `<label><span class="mr-1">${i18n.msg('cmdb.attribute.label.option.label')}</span>` +
                    `<span class="required"></span></label>` +
                `</div>` +
                `<div class="flex-column col-4 mr-4">` +
                    `<input type="text" class="z-input" maxlength="50" required="true" required ` +
                    `data-validation-required-name="${i18n.msg('cmdb.attribute.label.option.label')}">` +
                `</div>` +
                `<div class="flex-column col-1">` +
                    `<label><span>${i18n.msg('cmdb.attribute.label.option.value')}</span></label>` +
                `</div>` +
                `<div class="flex-column col-4 mr-4"><input type="text" class="z-input" maxlength="50"></div>` +
                `<div class="flex-column col-1">` +
                    `<label><span>${i18n.msg('cmdb.attribute.label.option.check')}</span></label>` +
                `</div>` +
                `<div class="flex-column col-1">` +
                    `<label class="z-checkbox">` +
                        `<input type="checkbox">` +
                        `<span></span>` +
                    `</label>` +
                `</div>` +
                `<div class="flex-column col-1">` +
                    `<button id="${rowId}_delete" type="button" class="z-button-icon extra">` +
                        `<span class="z-icon i-delete"></span>` +
                    `</button>` +
                `</div>` +
                `</div>`;
            parent.insertAdjacentHTML('beforeend', rowElement);

            const deleteBtn = document.getElementById(rowId + '_delete');
            deleteBtn.addEventListener('click', function (e) {
                e.stopPropagation();
                this.parentElement.parentElement.remove();
            });
        });

        if (property.option !== undefined && property.option !== null) {
            property.option.forEach(function () {
                addBtn.click();
            });
            document.querySelectorAll('#details > .flex-row').forEach(function (object, index) {
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
        // required
        const booleanOptions = [{'text': 'Y', 'value': 'true'}, {'text': 'N', 'value': 'false'}].map(function(option) {
            return `<option value='${option.value}' ${property.required === option.value ? 'selected=\'true\'' : ''}>` +
                `${aliceJs.filterXSS(option.text)}</option>`;
        }).join('');
        // custom-code
        const customCodeOptions = customCodeList.data.map(function(option) {
            return `<option value='${option.customCodeId}' ` +
                `${property.customCode === option.customCodeId ? 'selected=\'true\'' : ''}>` +
                `${aliceJs.filterXSS(option.customCodeName)}</option>`;
        }).join('');

        const defaultType = property.default !== undefined ? property.default.type : 'none';
        const defaultValue = property.default !== undefined ? (defaultType === 'code' ?
            property.default.value.split('|')[0] : property.default.value) : '';
        const buttonText = property.button !== undefined ? property.button : '';

        // session
        const sessionOptions = [{
            'text': i18n.msg('user.label.name'),
            'value': 'userName'
        }, {'text': i18n.msg('user.label.department'), 'value': 'department'}].map(function (option) {
            return `<option value='${option.value}' ` +
                `${(defaultType === 'session' && defaultValue === option.value) ? 'selected=\'true\'' : ''}>` +
                `${aliceJs.filterXSS(option.text)}</option>`;
        }).join('');

        this.template =
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4">` +
                `<label>` +
                    `<span class="mr-1">${i18n.msg('cmdb.attribute.label.option.required')}</span>` +
                    `<span class="required"></span>` +
                `</label>` +
            `</div>` +
            `<div class="flex-column col-9"><select id="${objectId}-required">${booleanOptions}</select></div>` +
            `</div>` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4">` +
                `<label><span>${i18n.msg('customCode.label.customCode')}</span></label>` +
            `</div>` +
            `<div class="flex-column col-9"><select id="${objectId}-select">${customCodeOptions}</select></div>` +
            `</div>` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4">` +
                `<label><span>${i18n.msg('cmdb.attribute.label.default')}</span></label>` +
            `</div>` +
            `<div class="flex-column col-1">` +
                `<label class="z-radio">` +
                    `<input name="${objectId}-default" id="${objectId}-none" type="radio" value="none" ${defaultType === 'none' ? 'checked=\'true\'' : ''}>` +
                    `<span></span>` +
                    `<span class="label">${i18n.msg('cmdb.attribute.label.option.none')}</span>` +
                `</label>` +
            `</div>` +
            `<div class="flex-column col-8"></div>` +
            `</div>` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4"><label><span></span></label></div>` +
            `<div class="flex-column col-1">` +
                `<label class="z-radio">` +
                    `<input name="${objectId}-default" id="${objectId}-session" type="radio" value="session" ${defaultType === 'session' ? 'checked=\'true\'' : ''}>` +
                    `<span></span>` +
                    `<span class="label">${i18n.msg('cmdb.attribute.label.option.session')}</span>` +
                `</label>` +
            `</div>` +
            `<div class="flex-column col-1"></div>` +
            `<div class="flex-column col-7"><select id="${objectId}-default-session" ${defaultType === 'session' ? '' : 'disabled=\'true\''}>${sessionOptions}</select></div>` +
            `</div>` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4"><label><span></span></label></div>` +
            `<div class="flex-column col-1"><label class="z-radio"><input name="${objectId}-default" id="${objectId}-code" type="radio" value="code" ${defaultType === 'code' ? 'checked=\'true\'' : ''}><span></span><span class="label">${i18n.msg('cmdb.attribute.label.option.code')}</span></label></div>` +
            `<div class="flex-column col-1"></div>` +
            `<div class="flex-column col-7"><select id="${objectId}-default-code" ${defaultType === 'code' ? '' : 'disabled=\'true\''}></select></div>` +
            `</div>` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4">` +
                `<label><span>${i18n.msg('cmdb.attribute.label.buttonText')}</span></label>` +
            `</div>` +
            `<div class="flex-column col-9">` +
                `<input type="text" class="z-input" id="${objectId}-button" maxlength="100" value="${buttonText}">` +
            `</div>` +
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
                        return `<option value='${option.key}' ` +
                            `${defaultValue === option.key ? 'selected=\'true\'' : ''}>` +
                            `${aliceJs.filterXSS(option.value)}</option>`;
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
     * Group list.
     *
     * @param {Object} property Attribute 데이터
     * @constructor
     */
    function GroupList(property) {
        const objectId = attributeTypeList[5].type; // group-list
        this.template =
            `<div class="float-right" id="button_add">` +
                `<button id="${objectId}_add" type="button" class="z-button-icon extra">` +
                    `<span class="z-icon i-plus"></span>` +
                `</button>` +
            `</div>`;

        parent.previousElementSibling.insertAdjacentHTML('beforeend', this.template);

        const addBtn = document.getElementById(objectId + '_add');
        addBtn.addEventListener('click', openAttributeListModal, false);

        if (property.option !== undefined && property.option !== null && property.option.length > 0) {
            // Attribute  목록 조회 - id 만 서버에 담고 있기 때문에 Attribute 명을 가져온다.
            aliceJs.fetchJson('/rest/cmdb/attributes', {
                method: 'GET'
            }).then((attributeData) => {
                if (attributeData.data.length > 0) {
                    for (let i = 0; i < attributeData.data.length; i++) {
                        const attribute = attributeData.data[i];
                        for (let j = 0; j < property.option.length; j++) {
                            if (attribute.attributeId === property.option[j].id) {
                                attributeMap.push({
                                    key: attribute.attributeId,
                                    value: attribute.attributeName,
                                    order: property.option[j].order,
                                    type: attribute.attributeType
                                });
                            }
                        }
                    }
                    // 정렬
                    attributeMap.sort((a, b) =>
                        a.order < b.order ? -1 : a.order > b.order ? 1 : 0
                    );
                    attributeMapTemp = JSON.parse(JSON.stringify(attributeMap));
                    attributeMap.forEach(function (attr) {
                        addGroupList({ key: attr.key, value: attr.value, order: attr.order, type: attr.type });
                    });
                }
            });
        }
    }

    /**
     * Group list - 속성 추가
     */
    function addGroupList(data) {
        let rowId = ZWorkflowUtil.generateUUID();
        let rowElement =
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-4">` +
                `<input type="text" class="z-input" maxlength="50" readonly="readonly" ` +
                `id="${data.key}" value="${data.value}">` +
            `</div>` +
            `<div class="flex-column col-1">` +
                `<label>` +
                    `<span class="mr-1">${i18n.msg('cmdb.attribute.label.type')}</span>` +
                `</label>` +
            `</div>` +
            `<div class="flex-column col-4">` +
                `<input type="text" class="z-input" maxlength="50" readonly="readonly" value="${data.type}">` +
            `</div>` +
            `<div class="flex-column col-1">` +
                `<label>` +
                    `<span class="mr-1">${i18n.msg('cmdb.attribute.label.seq')}</span><span class="required"></span>` +
                `</label>` +
            `</div>` +
            `<div class="flex-column col-2">` +
                `<input type="text" class="z-input" id="${data.key}_order" value="${data.order}" maxlength="50" ` +
                `onKeyup="this.value=this.value.replace(/[^0-9]/g,'');" required="required" />` +
            `</div>` +
            `<div class="flex-column col-1">` +
               `<button id="${rowId}_delete" type="button" class="z-button-icon extra">` +
                   `<span class="z-icon i-delete"></span>` +
               `</button>` +
            `</div>` +
            `</div>`;
        parent.insertAdjacentHTML('beforeend', rowElement);
        document.getElementById(data.key + '_order').addEventListener('change', function (e) {
            const attributeKeys = e.target.id.split('_');
            const changeIndex = attributeMapTemp.findIndex(function(attr) {
                return attr.key === attributeKeys[0];
            });
            attributeMap[changeIndex].order = e.target.value;
        });

        const deleteBtn = document.getElementById(rowId + '_delete');
        deleteBtn.addEventListener('click', function (e) {
            e.stopPropagation();
            const attributeKey = this.parentElement.parentElement.querySelector('input[type=text]').id;
            const removeIndex = attributeMapTemp.findIndex(function(attr) {
                return attr.key === attributeKey;
            });
            attributeMap.splice(removeIndex, 1);
            this.parentElement.parentElement.remove();
        });
    }

    /**
     * Attribute 목록 모달 오픈
     */
    function openAttributeListModal() {
        // 저장된 데이터를 담는다.
        attributeMapTemp.length = 0;
        attributeMapTemp = JSON.parse(JSON.stringify(attributeMap));

        // 모달 내부 template
        const attributeListModalContent = `<div class="cmdb-class-attribute-list">` +
                `<input class="z-input i-search col-5 mr-2" type="text" name="search" id="attributeSearch" ` +
                `maxlength="100" placeholder="${i18n.msg('cmdb.attribute.label.searchPlaceholder')}"/>` +
                `<span id="spanTotalCount" class="search-count"></span>` +
                `<div class="table-set" id="ciClassAttributeList"></div>` +
            `</div>`;
        /**
         * 세부 속성 검색
         */
        const getAttributeList = function (search, showProgressbar) {
            const url = '/cmdb/attributes/' + attributeId + '/list-modal?search=' + search.trim();
            aliceJs.fetchText(url, {
                method: 'GET',
                showProgressbar: showProgressbar
            }).then((htmlData) => {
                document.getElementById('ciClassAttributeList').innerHTML = htmlData;
                aliceJs.showTotalCount(document.querySelectorAll('.attribute-list').length);

                document.querySelectorAll('input[type=checkbox]').forEach(function (checkbox) {
                    checkbox.addEventListener('change', function (e) {
                        if (e.target.checked) {
                            attributeMapTemp.push({
                                key: e.target.value,
                                value: e.target.name,
                                order: '',
                                type: e.target.getAttribute('data-attribute-type')
                            });
                        } else {
                            const removeIndex = attributeMapTemp.findIndex(function(attr) {
                                return attr.key === e.target.value;
                            });
                            attributeMapTemp.splice(removeIndex, 1);
                        }
                    });
                    attributeMapTemp.forEach(function (attr) {
                        if (checkbox.value === attr.key) { checkbox.checked = true; }
                    });
                });

                // 스크롤바 추가
                OverlayScrollbars(document.querySelector('.z-list-body'), { className: 'scrollbar' });
            });
        };

        const attributeListModal = new modal({
            title: i18n.msg('cmdb.class.label.attributeList'),
            body: attributeListModalContent,
            classes: 'cmdb-class-attribute-modal',
            buttons: [{
                content: i18n.msg('common.btn.select'),
                classes: 'z-button primary',
                bindKey: false,
                callback: function (modal) {
                    attributeMap.length = 0;
                    // 기존 목록 삭제
                    while (parent.firstChild) {
                        parent.removeChild(parent.lastChild);
                    }
                    // 추가
                    attributeMap = JSON.parse(JSON.stringify(attributeMapTemp));
                    attributeMapTemp.forEach(function (attr) {
                        addGroupList({ key: attr.key, value: attr.value, order: attr.order, type: attr.type });
                    });
                    modal.hide();
                }
            }, {
                content: i18n.msg('common.btn.cancel'),
                classes: 'z-button secondary',
                bindKey: false,
                callback: function (modal) {
                    modal.hide();
                }
            }],
            close: {
                closable: false,
            },
            onCreate: function () {
                document.getElementById('attributeSearch').addEventListener('keyup', function () {
                    getAttributeList(this.value, false);
                });
                getAttributeList(document.getElementById('attributeSearch').value, false);
            }
        });
        attributeListModal.show();
    }

    /**
     * 중복 유효성 검사.
     *
     * @param type
     * @return {boolean}
     */
    function checkDuplicate(type) {
        let isValid = true;
        if (type === 'dropdown' || type === 'radio' || type === 'checkbox') {
            let detailsObject = document.querySelectorAll('#details > .flex-row:not(:first-child)');
            let labels = [];
            let values = [];
            for (let i = 0, len = detailsObject.length; i < len; i++) {
                let labelObject = detailsObject[i].querySelectorAll('input')[0];
                let valueObject = detailsObject[i].querySelectorAll('input')[1];
                if (labels.indexOf(labelObject.value.trim()) > -1 || values.indexOf(valueObject.value.trim()) > -1) {
                    aliceAlert.alertWarning(i18n.msg('validation.msg.dataNotDuplicate'));
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
                details.maxLength = parent.querySelector('#' + attributeTypeList[0].type + '-maxLength').value;
                details.minLength = parent.querySelector('#' + attributeTypeList[0].type + '-minLength').value;
                break;
            case 'dropdown':
                let dropdownOption = [];
                document.querySelectorAll('#details > .flex-row').forEach(function (object) {
                    dropdownOption.push({
                        text: object.querySelectorAll('input')[0].value.trim(),
                        value: object.querySelectorAll('input')[1].value.trim()
                    });
                });
                details.option = dropdownOption;
                break;
            case 'radio':
                let radioOption = [];
                document.querySelectorAll('#details > .flex-row').forEach(function (object) {
                    radioOption.push({
                        text: object.querySelectorAll('input')[0].value.trim(),
                        value: object.querySelectorAll('input')[1].value.trim()
                    });
                });
                details.option = radioOption;
                break;
            case 'checkbox':
                let checkOption = [];
                document.querySelectorAll('#details > .flex-row').forEach(function (object) {
                    checkOption.push({
                        text: object.querySelectorAll('input')[0].value.trim(),
                        value: object.querySelectorAll('input')[1].value.trim(),
                        checked: object.querySelectorAll('input')[2].checked,
                    });
                });
                details.option = checkOption;
                break;
            case 'custom-code':
                details.required = parent.querySelector('#' + attributeTypeList[4].type + '-required').value;
                const defaultType = document.querySelector('input[name="custom-code-default"]:checked').value;
                let defaultValue = '';
                switch (defaultType) {
                    case 'session':
                        defaultValue = parent.querySelector('#' + attributeTypeList[4].type + '-default-session').value;
                        break;
                    case 'code':
                        const codeSelect = parent.querySelector('#' + attributeTypeList[4].type + '-default-code');
                        defaultValue = codeSelect.value + '|' + codeSelect.options[codeSelect.selectedIndex].text;
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
            case 'group-list':
                let groupListOption = [];
                document.querySelectorAll('#details > .flex-row').forEach(function (object) {
                    groupListOption.push({
                        id: object.querySelectorAll('input')[0].id,
                        order: object.querySelectorAll('input')[2].value.trim()
                    });
                });
                details.option = groupListOption;
                break;
            default:
                break;
        }

        return details;
    }

    /**
     * Attribute 세부 정보 데이터를 토대로 화면에 출력 (Edit / Register)
     * @param target 표시할 대상 element
     * @param attributeData 세부 데이터
     */
    function drawEditDetails(target, attributeData, userInfo) {
        target.removeAttribute('onclick');
        target.innerHTML = '';

        for (let i = 0, iLen = attributeData.length; i < iLen; i++) {
            const groupAttribute = attributeData[i];
            const groupAttributeElem = document.createElement('div');
            groupAttributeElem.className = 'attribute-group';

            for (let j = 0, jLen = groupAttribute.attributes.length; j < jLen; j++) {
                const attributes = groupAttribute.attributes[j];
                const childAttributeElem = document.createElement('div');
                childAttributeElem.className = 'flex-column edit-row attribute';
                childAttributeElem.setAttribute('data-attributeType', attributes.attributeType);
                // 라벨
                const labelElem = document.createElement('label');
                labelElem.className = 'field-label';
                const labelTextElem = document.createElement('span');
                labelTextElem.textContent = attributes.attributeText;
                labelElem.appendChild(labelTextElem);
                childAttributeElem.appendChild(labelElem);

                const attributeValue = (attributes.attributeValue === null) ? '' :
                    JSON.parse(attributes.attributeValue);

                switch (attributes.attributeType) {
                    case 'inputbox':
                        const inputElem = document.createElement('input');
                        inputElem.type = 'text';
                        inputElem.className = 'z-input';
                        inputElem.id = attributes.attributeId;
                        inputElem.value = attributes.value;
                        if (attributeValue !== '') {
                            if (attributeValue.required === 'true') {
                                inputElem.required = true;
                                inputElem.setAttribute('data-validation-required', 'true');
                                inputElem.setAttribute('data-validation-required-name', attributes.attributeText);
                                labelElem.insertAdjacentHTML('beforeend', `<span class="required"></span>`);
                            }
                            // 유효성 검증
                            inputElem.addEventListener('keyup', function (e) {
                                e.preventDefault();
                                let userKeyCode = e.keyCode ? e.keyCode : e.which;
                                if (userKeyCode === 13) {
                                    return false;
                                } else {
                                    const elem = e.target;
                                    if (attributeValue.validate === 'char') {
                                        isValidNumber(elem.id, true);
                                    } else if (attributeValue.validate === 'number') {
                                        isValidChar(elem.id, true);
                                    }

                                    if (attributeValue.maxLength !== '') {
                                        isValidMaxLength(elem.id, attributeValue.maxLength, true);
                                    }
                                    if (attributeValue.minLength !== '') {
                                        isValidMinLength(elem.id, attributeValue.minLength, true);
                                    }
                                }
                            });
                        }
                        childAttributeElem.appendChild(inputElem);
                        break;
                    case 'dropdown':
                        const selectElem = document.createElement('select');
                        selectElem.id = attributes.attributeId;
                        if (attributeValue !== '' && typeof attributeValue.option !== 'undefined') {
                            for (let opt = 0, optLen = attributeValue.option.length; opt < optLen; opt++) {
                                const attributeOption = attributeValue.option[opt];
                                const selectOption = document.createElement('option');
                                selectOption.textContent = attributeOption.text;
                                selectOption.value = attributeOption.value;
                                if (selectOption.value === attributes.value) {
                                    selectOption.selected = true;
                                }
                                selectElem.appendChild(selectOption);
                            }
                        }
                        childAttributeElem.appendChild(selectElem);
                        break;
                    case 'radio':
                        if (attributeValue !== '' && typeof attributeValue.option !== 'undefined') {
                            for (let opt = 0, optLen = attributeValue.option.length; opt < optLen; opt++) {
                                const attributeOption = attributeValue.option[opt];
                                const radioGroup = document.createElement('label');
                                radioGroup.className = 'z-radio';
                                radioGroup.tabindex = 0;
                                radioGroup.htmlFor = attributes.attributeId + '-' + opt;

                                const radio = document.createElement('input');
                                radio.type = 'radio';
                                radio.id = attributes.attributeId + '-' + opt;
                                radio.name = 'attribute-radio';
                                radio.value = attributeOption.value;
                                if (attributeOption.value === attributes.value) {
                                    radio.checked = true;
                                }
                                if (attributes.value === '' && opt === 0) {
                                    radio.checked = true;
                                }
                                radioGroup.appendChild(radio);

                                const radioSpan = document.createElement('span');
                                radioGroup.appendChild(radioSpan);

                                const radioLabel = document.createElement('span');
                                radioLabel.className = 'label';
                                radioLabel.textContent = attributeOption.text;
                                radioGroup.appendChild(radioLabel);
                                childAttributeElem.appendChild(radioGroup);
                            }
                        }
                        break;
                    case 'checkbox':
                        if (attributeValue !== '' && typeof attributeValue.option !== 'undefined') {
                            for (let opt = 0, optLen = attributeValue.option.length; opt < optLen; opt++) {
                                const attributeOption = attributeValue.option[opt];
                                const chkGroup = document.createElement('label');
                                chkGroup.className = 'z-checkbox';
                                chkGroup.tabindex = 0;
                                chkGroup.htmlFor = attributes.attributeId + '-' + opt;

                                const chk = document.createElement('input');
                                chk.type = 'checkbox';
                                chk.id = attributes.attributeId + '-' + opt;
                                chk.name = 'attribute-checkbox';
                                chk.value = attributeOption.value;
                                if (attributes.value !== null) {
                                    if (attributes.value.indexOf(attributeOption.value) > -1) {
                                        chk.checked = true;
                                    }
                                } else {
                                    if (attributeOption.checked) {
                                        chk.checked = true;
                                    }
                                }
                                chkGroup.appendChild(chk);

                                const chkSpan = document.createElement('span');
                                chkGroup.appendChild(chkSpan);

                                const chkLabel = document.createElement('span');
                                chkLabel.className = 'z-label';
                                chkLabel.textContent = attributeOption.text;
                                chkGroup.appendChild(chkLabel);
                                childAttributeElem.appendChild(chkGroup);
                            }
                        }
                        break;
                    case 'custom-code':
                        let customValueArr = '';
                        if (attributes.value !== null) {
                            customValueArr = attributes.value.split('|');
                        } else {
                            if (attributeValue !== '') {
                                customValueArr = attributeValue.default.value.split('|');
                            }
                        }

                        const inputButtonElem = document.createElement('div');
                        inputButtonElem.id = attributes.attributeId;
                        inputButtonElem.className = 'flex-row input-button';

                        const customInputElem = document.createElement('input');
                        customInputElem.type = 'text';
                        customInputElem.className = 'z-input col-pct-12 inherit';
                        customInputElem.value = (customValueArr.length > 0) ? customValueArr[1] : '';
                        customInputElem.readOnly = true;
                        inputButtonElem.appendChild(customInputElem);

                        const customBtnElem = document.createElement('button');
                        customBtnElem.type = 'button';
                        customBtnElem.className = 'z-button form';
                        inputButtonElem.appendChild(customBtnElem);

                        let customData = attributes.value; // 'key|값'
                        let defaultValue = '';
                        if (attributeValue !== '') {
                            customBtnElem.textContent = attributeValue.button;
                            // 커스텀 코드 기본 값 넣기
                            if (attributes.value === '' || attributes.value === null) {
                                switch (attributeValue.default.type) {
                                    case 'session':
                                        if (attributeValue.default.value === 'userName') {
                                            customData = userInfo.userKey + '|' + userInfo.userName;
                                            defaultValue = userInfo.userName;
                                        } else if (attributeValue.default.value === 'department') {
                                            customData = userInfo.department + '|' + userInfo.departmentName;
                                            defaultValue = userInfo.departmentName;
                                        }
                                        break;
                                    case 'code':
                                        customData = attributeValue.default.value;
                                        defaultValue = customData.split('|')[1];
                                        break;
                                    default: //none
                                        customData = attributeValue.default.type + '|';
                                        break;
                                }
                                customInputElem.value = defaultValue;
                            }
                            customInputElem.setAttribute('value', defaultValue);
                            customInputElem.setAttribute('custom-data', customData);

                            customBtnElem.addEventListener('click', function () {
                                const customCodeData = {
                                    componentId: attributes.attributeId,
                                    componentValue: customInputElem.getAttribute('custom-data')
                                };
                                const itemName = 'alice_custom-codes-search-' + attributes.attributeId;
                                sessionStorage.setItem(itemName, JSON.stringify(customCodeData));
                                let url = '/custom-codes/' + attributeValue.customCode + '/search';
                                window.open(url, itemName, 'width=500, height=655');
                            });
                        }
                        childAttributeElem.appendChild(inputButtonElem);
                        break;
                    default:
                        break;
                }

                groupAttributeElem.appendChild(childAttributeElem);
            }
            target.appendChild(groupAttributeElem);
        }
        aliceJs.initDesignedSelectTag();
    }

    /**
     * Attribute 세부 정보 데이터를 토대로 화면에 출력 (View)
     * @param target 표시할 대상 element
     * @param attributeData 세부 데이터
     */
    function drawViewDetails(target, attributeData, userInfo) {
        target.removeAttribute('onclick');
        target.innerHTML = '';
        for (let i = 0, iLen = attributeData.length; i < iLen; i++) {
            const groupAttribute = attributeData[i];
            const groupAttributeElem = document.createElement('div');
            groupAttributeElem.className = 'attribute-group';
            for (let j = 0, jLen = groupAttribute.attributes.length; j < jLen; j++) {
                const attributes = groupAttribute.attributes[j];
                const childAttributeElem = document.createElement('div');
                childAttributeElem.className = 'flex-column z-view-row attribute';
                childAttributeElem.setAttribute('data-attributeType', attributes.attributeType);
                // 라벨
                const labelElem = document.createElement('label');
                labelElem.className = 'field-label';
                const labelTextElem = document.createElement('span');
                labelTextElem.textContent = attributes.attributeText;
                labelElem.appendChild(labelTextElem);
                childAttributeElem.appendChild(labelElem);

                const attributeValue = (attributes.attributeValue === null) ? '' :
                    JSON.parse(attributes.attributeValue);
                switch (attributes.attributeType) {
                    case 'inputbox':
                        const inputElem = document.createElement('input');
                        inputElem.type = 'text';
                        inputElem.className = 'z-input';
                        inputElem.id = attributes.attributeId;
                        inputElem.value = attributes.value;
                        inputElem.readOnly = true;
                        if (attributeValue !== '') {
                            if (attributeValue.required === 'true') {
                                inputElem.required = true;
                                inputElem.setAttribute('data-validation-required', 'true');
                                inputElem.setAttribute('data-validation-required-name', attributes.attributeText);
                                labelElem.insertAdjacentHTML('beforeend', `<span class="required"></span>`);
                            }
                        }
                        childAttributeElem.appendChild(inputElem);
                        break;
                    case 'dropdown':
                        const selectElem = document.createElement('input');
                        selectElem.type = 'text';
                        selectElem.className = 'z-input';
                        selectElem.id = attributes.attributeId;
                        selectElem.readOnly = true;

                        if (attributeValue !== '' && typeof attributeValue.option !== 'undefined') {
                            for (let opt = 0, optLen = attributeValue.option.length; opt < optLen; opt++) {
                                const attributeOption = attributeValue.option[opt];
                                if (attributeOption.value === attributes.value) {
                                    selectElem.value = attributeOption.text;
                                }
                            }
                        }
                        childAttributeElem.appendChild(selectElem);
                        break;
                    case 'radio':
                        if (attributeValue !== '' && typeof attributeValue.option !== 'undefined') {
                            const radio = document.createElement('input');
                            radio.type = 'text';
                            radio.className = 'z-input';
                            radio.name = 'attribute-radio';
                            radio.readOnly = true;

                            for (let opt = 0, optLen = attributeValue.option.length; opt < optLen; opt++) {
                                const attributeOption = attributeValue.option[opt];
                                if (attributeOption.value === attributes.value) {
                                    radio.value = attributeOption.text;
                                }
                            }
                            childAttributeElem.appendChild(radio);
                        }
                        break;
                    case 'checkbox':
                        if (attributeValue !== '' && typeof attributeValue.option !== 'undefined') {
                            for (let opt = 0, optLen = attributeValue.option.length; opt < optLen; opt++) {
                                const attributeOption = attributeValue.option[opt];
                                const chkGroup = document.createElement('label');
                                chkGroup.className = 'z-checkbox';
                                chkGroup.tabindex = 0;
                                chkGroup.htmlFor = attributes.attributeId + '-' + opt;

                                const chk = document.createElement('input');
                                chk.type = 'checkbox';
                                chk.id = attributes.attributeId + '-' + opt;
                                chk.name = 'attribute-checkbox';
                                chk.value = attributeOption.value;
                                chk.readOnly = true;
                                chk.onclick = function () {
                                    return false;
                                };
                                if (attributes.value !== null) {
                                    if (attributes.value.indexOf(attributeOption.value) > -1) {
                                        chk.checked = true;
                                    }
                                } else {
                                    if (attributeOption.checked) {
                                        chk.checked = true;
                                    }
                                }
                                chkGroup.appendChild(chk);

                                const chkSpan = document.createElement('span');
                                chkGroup.appendChild(chkSpan);

                                const chkLabel = document.createElement('span');
                                chkLabel.className = 'z-label';
                                chkLabel.textContent = attributeOption.text;
                                chkGroup.appendChild(chkLabel);
                                childAttributeElem.appendChild(chkGroup);
                            }
                        }
                        break;
                    case 'custom-code':
                        let customValueArr = '';
                        if (attributes.value !== null) {
                            customValueArr = attributes.value.split('|');
                        } else {
                            customValueArr = attributeValue.default.value.split('|');
                        }
                        const inputButtonElem = document.createElement('div');
                        inputButtonElem.id = attributes.attributeId;
                        inputButtonElem.className = 'flex-row input-button';

                        const customInputElem = document.createElement('input');
                        customInputElem.type = 'text';
                        customInputElem.className = 'z-input col-pct-12 inherit';
                        customInputElem.value = (customValueArr.length > 0) ? customValueArr[1] : '';
                        customInputElem.readOnly = true;
                        inputButtonElem.appendChild(customInputElem);
                        const customBtnElem = document.createElement('button');
                        customBtnElem.type = 'button';
                        customBtnElem.className = 'z-button form';
                        customBtnElem.disabled = true;
                        inputButtonElem.appendChild(customBtnElem);

                        let customData = attributes.value; // 'key|값'
                        let defaultValue = '';
                        if (attributeValue !== '') {
                            customBtnElem.textContent = attributeValue.button;
                            // 커스텀 코드 기본 값 넣기
                            if (attributes.value === '' || attributes.value === null) {
                                switch (attributeValue.default.type) {
                                    case 'session':
                                        if (attributeValue.default.value === 'userName') {
                                            customData = userInfo.userKey + '|' + userInfo.userName;
                                            defaultValue = userInfo.userName;
                                        } else if (attributeValue.default.value === 'department') {
                                            customData = userInfo.department + '|' + userInfo.departmentName;
                                            defaultValue = userInfo.departmentName;
                                        }
                                        break;
                                    case 'code':
                                        customData = attributeValue.default.value;
                                        defaultValue = customData.split('|')[1];
                                        break;
                                    default: //none
                                        customData = attributeValue.default.type + '|';
                                        break;
                                }
                                customInputElem.value = defaultValue;
                            }
                            customInputElem.setAttribute('value', defaultValue);
                            customInputElem.setAttribute('custom-data', customData);
                        }
                        childAttributeElem.appendChild(inputButtonElem);
                        break;
                    default:
                        break;
                }
                groupAttributeElem.appendChild(childAttributeElem);
            }
            target.appendChild(groupAttributeElem);
        }
        aliceJs.initDesignedSelectTag();
    }

    exports.attributeTypeList = attributeTypeList;
    exports.init = init;
    exports.makeDetails = makeDetails;
    exports.checkDuplicate = checkDuplicate;
    exports.setDetails = setDetails;
    exports.drawEditDetails = drawEditDetails;
    exports.drawViewDetails = drawViewDetails;

    Object.defineProperty(exports, '__esModule', { value: true });
})));
