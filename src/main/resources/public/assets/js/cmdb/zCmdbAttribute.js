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
        {'type': 'group-list', 'name': 'Group List'},
        {'type': 'date', 'name': 'Date'},
        {'type': 'datetime', 'name': 'Date Time'},
        {'type': 'userSearch', 'name': 'User Search'},
        {'type': 'organizationSearch', 'name': 'Organization Search'}
    ];

    // Validation 목록
    let validationList = [
        {'text': 'None', 'value': ''},
        {'text': 'Char', 'value': 'char'},
        {'text': 'Number', 'value': 'number'}
    ];

    let parent = null;
    let customCodeList = [];
    let targetUserArray = [];
    let defaultCustomUser = [];
    let userInfo = null;
    let attributeDetailData = null; // 서버에 저장된 세부 속성 데이터
    let displayMode = 'view'; // edit | view
    let attributeId = '';
    let attributeMap = []; // 저장된 데이터
    let attributeMapTemp = []; // 화면에서 사용자가 변경 중인 데이터
    const inputTypeAttributeDefaultMaxLength = 1000;
    const inputTypeAttributeDefaultMinLength = 0;

    /**
     * 초기 데이터 셋팅.
     *
     * @param target
     */
    function init(target) {
        parent = target;
        attributeId = document.getElementById('attributeId').value;
    }

    /**
     * Attribute 타입에 따른 세부 설정 구현.
     *
     * @param attributeType
     * @param data
     * @return {null|boolean}
     */
    async function makeDetails(attributeType, data) {
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
                // load custom-code list
                await aliceJs.fetchJson('/rest/custom-codes?viewType=editor', {
                    method: 'GET'
                }).then((response) => {
                    if (response.status === aliceJs.response.success) {
                        customCodeList = response.data;
                    }
                });
                attributeObject = new CustomCode(attributesProperty);
                break;
            case 'group-list':
                attributeObject = new GroupList(attributesProperty);
                break;
            case 'date':
                attributeObject = new Date(attributesProperty);
                break;
            case 'datetime':
                attributeObject = new DateTime(attributesProperty);
                break;
            case 'userSearch':
                attributeObject = new UserSearch(attributesProperty);
                break;
            case 'organizationSearch':
                attributeObject = new OrganizationSearch(attributesProperty);
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
        list.forEach(function (v) {
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
        const requiredTemplate = getRequiredAttributeTemplate(objectId, property.required);
        const list = ['', 'char', 'number'];
        const validations = setValidations(list);
        const validationOptions = validations.map(function (validation) {
            return `<option value='${validation.value}' ` +
                `${property.validate === validation.value ? 'selected=\'true\'' : ''}>` +
                `${aliceJs.filterXSS(validation.text)}</option>`;
        }).join('');
        const maxLengthValue = property.maxLength !== undefined ? property.maxLength :
            inputTypeAttributeDefaultMaxLength;
        const minLengthValue = property.minLength !== undefined ? property.minLength :
            inputTypeAttributeDefaultMinLength;
        this.template = `${requiredTemplate}` +
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
            `<input type="number" class="z-input" id="${objectId}-maxLength" max="1000" value="${maxLengthValue}">` +
            `</div>` +
            `</div>` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4">` +
            `<label><span class="mr-1">${i18n.msg('cmdb.attribute.label.option.minLength')}</span></label>` +
            `</div>` +
            `<div class="flex-column col-9">` +
            `<input type="number" class="z-input" id="${objectId}-minLength" max="1000" min="0" ` +
                `value="${minLengthValue}">` +
            `</div>` +
            `</div>`;
        parent.insertAdjacentHTML('beforeend', this.template);
        aliceJs.initDesignedSelectTag();
    }

    /**
     * Dropdown.
     *
     * @param {Object} property Attribute 데이터
     * @constructor
     */
    function Dropdown(property) {
        const objectId = attributeTypeList[1].type; // dropdown
        const booleanOptions = [{'text': 'Y', 'value': 'true'}, {'text': 'N', 'value': 'false'}].map(function (option) {
            return `<option value='${option.value}' ` +
                `${property.required === option.value ? 'selected=\'true\'' : ''}>` +
                `${aliceJs.filterXSS(option.text)}</option>`;
        }).join('');
        this.template =
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-3">` +
            `<label>` +
            `<span class="mr-1">${i18n.msg('cmdb.attribute.label.option.required')}</span>` +
            `<span class="required"></span>` +
            `</label>` +
            `</div>` +
            `<div class="flex-column col-9"><select id="${objectId}-required">${booleanOptions}</select></div>` +
            `</div>` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-3">` +
            `<label>` +
            `<span class="mr-1">${i18n.msg('cmdb.attribute.label.option.listData')}</span>` +
            `<span class="required"></span>` +
            `</label>` +
            `</div>` +
            `<div class="flex-column col-9">` +
            `<div class="inline-flex justify-content-end" id="button_add">` +
            `<button id="${objectId}_add" type="button" class="z-button-icon extra">` +
                `<span class="z-icon i-plus"></span></button>` +
            `</div>` +
            `</div>` +
            `</div>` +
            `<div class="flex-row justify-content-end mt-2">` +
            `<div class="col-9" id="dropdownListData">` +
            `<div class="flex-row">` +
            `<div class="flex-column col-1">` +
            `<label>` +
            `<span class="mr-1">${i18n.msg('cmdb.attribute.label.option.label')}</span>` +
            `</label>` +
            `</div>` +
            `<div class="flex-column col-5 mr-4">` +
            `<input type="text" class="z-input" maxlength="50" value="선택하세요." readonly>` +
            `</div>` +
            `<div class="flex-column col-1">` +
            `<label>` +
            `<span>${i18n.msg('cmdb.attribute.label.option.value')}</span>` +
            `</label>` +
            `</div>` +
            `<div class="flex-column col-5">` +
            `<input type="text" class="z-input" maxlength="50" readonly>` +
            `</div>` +
            `</div>` +
            `</div>` +
            `</div>`;
        parent.insertAdjacentHTML('beforeend', this.template);

        const addBtn = document.getElementById(objectId + '_add');
        const dropdownListData = document.getElementById('dropdownListData');
        addBtn.addEventListener('click', function (e) {
            e.stopPropagation();
            let rowId = ZWorkflowUtil.generateUUID();
            let rowElement =
                `<div class="flex-row mt-2">` +
                `<div class="flex-column col-1">` +
                `<label>` +
                `<span class="mr-1">${i18n.msg('cmdb.attribute.label.option.label')}</span>` +
                `</label>` +
                `</div>` +
                `<div class="flex-column col-5 mr-4">` +
                `<input type="text" class="z-input" maxlength="50" required="true" required ` +
                    `data-validation-required-name="${i18n.msg('cmdb.attribute.label.option.label')}">` +
                `</div>` +
                `<div class="flex-column col-1">` +
                `<label>` +
                `<span>${i18n.msg('cmdb.attribute.label.option.value')}</span>` +
                `</label>` +
                `</div>` +
                `<div class="flex-column col-5">` +
                `<input type="text" class="z-input" maxlength="50" required="true" required ` +
                    `data-validation-required-name="${i18n.msg('cmdb.attribute.label.option.value')}">` +
                `</div>` +
                `<div class="flex-column">` +
                `<button id="${rowId}_delete" type="button" class="z-button-icon extra">` +
                `<span class="z-icon i-delete"></span>` +
                `</button>` +
                `</div>` +
                `</div>`;
            dropdownListData.insertAdjacentHTML('beforeend', rowElement);

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
            const dropdownRowList = document.querySelectorAll('#dropdownListData .flex-row:not(:first-child)');
            dropdownRowList.forEach(function (object, index) {
                object.querySelectorAll('input')[0].value = property.option[index].text;
                object.querySelectorAll('input')[1].value = property.option[index].value;
            });
            document.querySelector('#dropdownListData').children[1].remove();
        }

        aliceJs.initDesignedSelectTag();
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
                `<div class="flex-column">` +
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
                `<div class="flex-column">` +
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
        const requiredTemplate = getRequiredAttributeTemplate(objectId, property.required);
        // custom-code
        const customCodeOptions = customCodeList.map(function (option) {
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
        this.template = `${requiredTemplate}` +
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
            `<input name="${objectId}-default" id="${objectId}-none" type="radio" value="none" ` +
                `${defaultType === 'none' ? 'checked=\'true\'' : ''}>` +
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
            `<input name="${objectId}-default" id="${objectId}-session" type="radio" value="session" ` +
                `${defaultType === 'session' ? 'checked=\'true\'' : ''}>` +
            `<span></span>` +
            `<span class="label">${i18n.msg('cmdb.attribute.label.option.session')}</span>` +
            `</label>` +
            `</div>` +
            `<div class="flex-column col-1"></div>` +
            `<div class="flex-column col-7"><select id="${objectId}-default-session" ` +
                `${defaultType === 'session' ? '' : 'disabled=\'true\''}>${sessionOptions}</select></div>` +
            `</div>` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4"><label><span></span></label></div>` +
            `<div class="flex-column col-1"><label class="z-radio"><input type="radio" name="${objectId}-default" ` +
                `id="${objectId}-code" value="code" ${defaultType === 'code' ? 'checked=\'true\'' : ''}>` +
                `<span></span><span class="label">${i18n.msg('cmdb.attribute.label.option.code')}</span>` +
                `</label></div>` +
            `<div class="flex-column col-1"></div>` +
            `<div class="flex-column col-7">` +
            `<div class="flex-row z-input-button">` +
            `<input type="text" class="z-input" readonly="true" id="${objectId}-default-code-text" ` +
                `value="${defaultType === 'code' ? property.default.value.split('|')[1] : ''}" ` +
                `data-value="${defaultType === 'code' ? property.default.value.split('|')[0] : ''}" ` +
                `${defaultType === 'code' ? '' : 'disabled=\'true\''}>` +
            `<button class="z-button-icon z-button-code" type="button" id="${objectId}-default-code" ` +
                `data-value="${property.customCode}" ${defaultType === 'code' ? '' : 'disabled=\'true\''}>` +
                `<span class="z-icon i-search"></span></button>` +
            `</div>` +
            `</div>` +
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
        aliceJs.initDesignedSelectTag();

        // custom-code 변경 시 데이터 변경
        const customCodeObject = document.getElementById(objectId + '-select');
        customCodeObject.addEventListener('change', function (e) {
            e.stopPropagation();

            const codeBtn = document.getElementById(objectId + '-default-code');
            codeBtn.setAttribute('data-value', this.value);

            const codeText = document.getElementById(objectId + '-default-code-text');
            codeText.value = '';
            codeText.setAttribute('data-value', '');
        });

        // 코드 선택 모달 오픈
        document.getElementById(objectId + '-default-code').addEventListener('click', openCustomCodeTree, false);

        // disabled
        const defaultNoneObject = document.getElementById(objectId + '-none');
        const defaultSessionObject = document.getElementById(objectId + '-session');
        const defaultCodeObject = document.getElementById(objectId + '-code');

        defaultNoneObject.addEventListener('click', function (e) {
            e.stopPropagation();
            document.getElementById(objectId + '-default-session').disabled = true;
            document.getElementById(objectId + '-default-code-text').disabled = true;
            document.getElementById(objectId + '-default-code').disabled = true;
            aliceJs.initDesignedSelectTag();
        });
        defaultSessionObject.addEventListener('click', function (e) {
            e.stopPropagation();
            document.getElementById(objectId + '-default-session').disabled = false;
            document.getElementById(objectId + '-default-code-text').disabled = true;
            document.getElementById(objectId + '-default-code').disabled = true;
            aliceJs.initDesignedSelectTag();
        });
        defaultCodeObject.addEventListener('click', function (e) {
            e.stopPropagation();
            document.getElementById(objectId + '-default-session').disabled = true;
            document.getElementById(objectId + '-default-code-text').disabled = false;
            document.getElementById(objectId + '-default-code').disabled = false;
            aliceJs.initDesignedSelectTag();
        });
    }

    /**
     * 커스텀 코드 데이터 트리 조회
     */
    function openCustomCodeTree(e) {
        const customCodeId = e.target.getAttribute('data-value');
        const inputElem = document.getElementById(e.target.id + '-text');
        const selectedValue = inputElem.getAttribute('data-value');
        tree.load({
            view: 'modal',
            title: i18n.msg('form.label.customCodeTarget'),
            dataUrl: '/rest/custom-codes/' + customCodeId,
            target: 'treeList',
            text: 'codeName',
            rootAvailable: false,
            leafIcon: '/assets/media/icons/tree/icon_tree_leaf.svg',
            selectedValue: selectedValue,
            callbackFunc: (response) => {
                inputElem.value = response.textContent;
                inputElem.setAttribute('data-value', response.id);
            }
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
            }).then((response) => {
                if (response.status === aliceJs.response.success && response.data.length > 0) {
                    for (let i = 0; i < response.data.length; i++) {
                        const attribute = response.data[i];
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
                        addGroupList({key: attr.key, value: attr.value, order: attr.order, type: attr.type});
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
            `<div class="flex-column col-2 align-right mr-2">` +
            `<label>` +
            `<span>${i18n.msg('cmdb.attribute.label.type')}</span>` +
            `</label>` +
            `</div>` +
            `<div class="flex-column col-3">` +
            `<input type="text" class="z-input" maxlength="50" readonly="readonly" value="${data.type}">` +
            `</div>` +
            `<div class="flex-column col-2 align-right mr-2">` +
            `<label>` +
            `<span">${i18n.msg('cmdb.attribute.label.seq')}</span><span class="required"></span>` +
            `</label>` +
            `</div>` +
            `<div class="flex-column col-3">` +
            `<input type="text" class="z-input" id="${data.key}_order" value="${data.order}" maxlength="50" ` +
            `onKeyup="this.value=this.value.replace(/[^0-9]/g,'');" required="required" />` +
            `</div>` +
            `<div class="flex-column">` +
            `<button id="${rowId}_delete" type="button" class="z-button-icon extra ml-1">` +
            `<span class="z-icon i-delete"></span>` +
            `</button>` +
            `</div>` +
            `</div>`;
        parent.insertAdjacentHTML('beforeend', rowElement);
        document.getElementById(data.key + '_order').addEventListener('change', function (e) {
            const attributeKeys = e.target.id.split('_');
            const changeIndex = attributeMapTemp.findIndex(function (attr) {
                return attr.key === attributeKeys[0];
            });
            attributeMap[changeIndex].order = e.target.value;
        });

        const deleteBtn = document.getElementById(rowId + '_delete');
        deleteBtn.addEventListener('click', function (e) {
            e.stopPropagation();
            const attributeKey = this.parentElement.parentElement.querySelector('input[type=text]').id;
            const removeIndex = attributeMapTemp.findIndex(function (attr) {
                return attr.key === attributeKey;
            });
            attributeMap.splice(removeIndex, 1);
            this.parentElement.parentElement.remove();
        });
    }

    /**
     * Date.
     *
     * @param {Object} property Attribute 데이터
     * @constructor
     */
    function Date(property) {
        const objectId = attributeTypeList[6].type; // date
        const requiredTemplate = getRequiredAttributeTemplate(objectId, property.required);
        const minDate = property.minDate !== undefined ? property.minDate : '';
        const maxDate = property.maxDate !== undefined ? property.maxDate : '';
        this.template = `${requiredTemplate}` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4">` +
            `<label>` +
            `<span class="mr-1">${i18n.msg('cmdb.attribute.label.option.minDate')}</span>` +
            `</label>` +
            `</div>` +
            `<div class="flex-column col-9">` +
            `<input name="${objectId}-minDate" id="${objectId}-minDate" 
                        class="z-input i-date-picker search-date col-3 mr-2" 
                        value="${minDate}" placeholder="${i18n.dateFormat}">` +
            `</div>` +
            `</div>` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4">` +
            `<label>` +
            `<span class="mr-1">${i18n.msg('cmdb.attribute.label.option.maxDate')}</span>` +
            `</label>` +
            `</div>` +
            `<div class="flex-column col-9">` +
            `<input name="${objectId}-maxDate" id="${objectId}-maxDate" 
                        class="z-input i-date-picker search-date col-3 mr-2" 
                        value="${maxDate}" placeholder="${i18n.dateFormat}">` +
            `</div>` +
            `</div>`;
        parent.insertAdjacentHTML('beforeend', this.template);
        aliceJs.initDesignedSelectTag();

        const minDateElement = document.getElementById(objectId + '-minDate');
        const maxDateElement = document.getElementById(objectId + '-maxDate');
        zDateTimePicker.initDatePicker(minDateElement);
        zDateTimePicker.initDatePicker(maxDateElement);
    }

    /**
     * Date Time.
     *
     * @param {Object} property Attribute 데이터
     * @constructor
     */
    function DateTime(property) {
        const objectId = attributeTypeList[7].type; // datetime
        const requiredTemplate = getRequiredAttributeTemplate(objectId, property.required);
        const minDateTime = property.minDateTime !== undefined ? property.minDateTime : '';
        const maxDateTime = property.maxDateTime !== undefined ? property.maxDateTime : '';
        this.template = `${requiredTemplate}` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4">` +
            `<label>` +
            `<span class="mr-1">${i18n.msg('cmdb.attribute.label.option.minDateTime')}</span>` +
            `</label>` +
            `</div>` +
            `<div class="flex-column col-9">` +
            `<input name="${objectId}-minDateTime" id="${objectId}-minDateTime" 
                        class="z-input i-datetime-picker search-datetime col-3 mr-2" 
                        value="${minDateTime}" placeholder="${i18n.dateTimeFormat}">` +
            `</div>` +
            `</div>` +
            `<div class="flex-row mt-2">` +
            `<div class="flex-column col-2 mr-4">` +
            `<label>` +
            `<span class="mr-1">${i18n.msg('cmdb.attribute.label.option.maxDateTime')}</span>` +
            `</label>` +
            `</div>` +
            `<div class="flex-column col-9">` +
            `<input name="${objectId}-maxDateTime" id="${objectId}-maxDateTime" 
                        class="z-input i-datetime-picker search-datetime col-3 mr-2" 
                        value="${maxDateTime}" placeholder="${i18n.dateTimeFormat}">` +
            `</div>` +
            `</div>`;
        parent.insertAdjacentHTML('beforeend', this.template);
        aliceJs.initDesignedSelectTag();

        const minDateTimeElement = document.getElementById(objectId + '-minDateTime');
        const maxDateTimeElement = document.getElementById(objectId + '-maxDateTime');
        zDateTimePicker.initDateTimePicker(minDateTimeElement);
        zDateTimePicker.initDateTimePicker(maxDateTimeElement);
    }

    /**
     * User Search.
     *
     * {
     *     "required":"true",
     *     "targetCriteria":"organization|custom",
     *     "searchKey":[{"id": "4028b2d57d37168e017d3716cgf00000", "value": "조직구성"}],
     *     "defaultValue":{"type":"none","data":""}
     * }
     * @param {Object} property Attribute 데이터
     * @constructor
     */
    function UserSearch(property) {
        const objectId = attributeTypeList[8].type; // userSearch
        // 필수값
        const requiredTemplate = getRequiredAttributeTemplate(objectId, property.required);
        // 기본 값
        const defaultValueTemplate = getDefaultValueTemplate(objectId, property.defaultValue);

        // 조회 대상 기준
        const targetCriteria = property.targetCriteria !== undefined ? property.targetCriteria : 'organization';
        const searchKey = property.searchKey !== undefined ? property.searchKey : [];
        const targetOptions = [
            {'text': i18n.msg('form.properties.userSearch.organization'), 'value': 'organization'},
            {'text': i18n.msg('form.properties.userSearch.custom'), 'value': 'custom'}].map(function (option) {
            const isSelected = (property.targetCriteria === option.value);
            return `<option value='${option.value}' selected=${isSelected}>` +
                `${aliceJs.filterXSS(option.text)}</option>`;
        }).join('');
        this.template = `${requiredTemplate}` +
            `<div class="flex-row mt-2">
                <div class="flex-column col-2 mr-4">
                    <label><span>${i18n.msg('form.properties.element.searchTargetCriteria')}</span></label>
                </div>
                <div class="flex-column col-9">
                    <select id="${objectId}Criteria">${targetOptions}</select>
                </div>
            </div>
            <div class="flex-row mt-2">
                <div class="flex-column col-2 mr-4">
                    <label>
                        <span>${i18n.msg('form.properties.element.searchTarget')}</span>
                        <span class="required"></span>
                    </label>
                </div>
                <div class="flex-column col-pct-9" id="changeTargetCriteria">
                </div>
            </div>`.trim() + `${defaultValueTemplate}`;

        parent.insertAdjacentHTML('beforeend', this.template);
        aliceJs.initDesignedSelectTag();

        // 조회 대상 기준 변경시
        const targetSelect = document.getElementById(objectId + 'Criteria');
        targetSelect.addEventListener('change', changeTargetCriteriaHandler, false);
        targetSelect.dispatchEvent(new Event('change'));

        // 검색 조건 설정
        setTargetCriteria(parent, { 'targetCriteria': targetCriteria, 'searchKey': searchKey });

        // 기본값 radio 관련 disabled event 설정
        disableUncheckedRadio(objectId);

        // 지정 사용자 선택 모달 오픈
        document.getElementById(objectId + '-default-custom')
            .addEventListener('click', openUserListModal, false);
    }

    /**
     * 검색 조건 설정
     */
    function setTargetCriteria(target, data) {
        if (!data.searchKey.length) { return false; }

        if (data.targetCriteria === 'organization') {
            const inputElem = target.querySelector('#searchTarget');
            if (inputElem !== null) {
                inputElem.value = data.searchKey[0].value;
                inputElem.setAttribute('data-value', data.searchKey[0].id);
            }
        } else {
            targetUserArray = data.searchKey;
            addUserInTargetUser(data.searchKey);
        }
    }

    /**
     * 조회 대상 기준 변경 이벤트 핸들러
     */
    function changeTargetCriteriaHandler(e) {
        // 삭제
        const targetCriteria = document.getElementById('changeTargetCriteria');
        targetCriteria.innerHTML = '';
        targetUserArray.length = 0;

        // 신규 생성
        if (e.target.value === 'organization') {
            const organizationTemplate = `<div class="flex-row z-input-button">
                <input type="text" class="z-input" readonly="true" id="searchTarget" required="true" data-value="">
                <button class="z-button-icon z-button-code" type="button" id="searchOrganization">` +
                `<span class="z-icon i-search"></span></button>
            </div>`;
            targetCriteria.insertAdjacentHTML('beforeend', organizationTemplate);

            const searchOrganization = targetCriteria.querySelector('#searchOrganization');
            searchOrganization.addEventListener('click', openOrganizationTreeModal, false);
        } else {
            const customTemplate = `<div class="align-right">
                <button type="button" class="z-button secondary" id="searchUserList">`   +
                    `${i18n.msg('common.btn.add')}</button>
            </div>`;
            targetCriterial.insertAdjacentHTML('beforeend', customTemplate);

            const searchUserList = targetCriteria.querySelector('#searchUserList');
            searchUserList.addEventListener('click', openUserListModal, false);
        }
    }

    /**
     * 부서 선택 모달
     */
    function openOrganizationTreeModal(e) {
        const inputElem = e.target.parentNode.querySelector('#searchTarget');
        const selectedValue = (inputElem !== null) ? inputElem.getAttribute('data-value') : '';
        tree.load({
            view: 'modal',
            title: i18n.msg('department.label.deptList'),
            dataUrl: '/rest/organizations',
            target: 'treeList',
            source: 'organization',
            text: 'organizationName',
            nodeNameLabel: i18n.msg('common.msg.dataSelect', i18n.msg('department.label.deptName')),
            defaultIcon: '/assets/media/icons/tree/icon_tree_organization.svg',
            selectedValue: selectedValue,
            callbackFunc: (response) => {
                if (inputElem !== null) {
                    inputElem.value = response.textContent;
                    inputElem.setAttribute('data-value', response.id);
                }
            }
        });
    }

    /**
     * 사용자 선택 모달
     */
    function openUserListModal(e) {
        const targetUserModalTemplate = `<div class="target-user-list">` +
                `<input class="z-input i-search col-5 mr-2" type="text" name="search" id="search" maxlength="100" ` +
                `placeholder="` + i18n.msg('user.label.userSearchPlaceholder') + `">` +
                `<span id="spanTotalCount" class="search-count"></span>` +
                `<div class="table-set" id="targetUserList"></div>` +
            `</div>`;

        const isMulti = e.target.id === 'searchUserList';
        const type = isMulti ? 'searchCriteria' : 'defaultCustom';
        const targetArray = isMulti ? targetUserArray : defaultCustomUser;
        const defaultCustom = document.getElementById('userSearch-default-custom-text');

        const targetUserModal = new modal({
            title: i18n.msg('form.properties.userList'),
            body: targetUserModalTemplate,
            classes: 'target-user-modal',
            buttons: [{
                content: i18n.msg('common.btn.select'),
                classes: 'z-button primary',
                bindKey: false,
                callback: (modal) => {
                    if (!targetUserArray.length) {
                        zAlert.warning(i18n.msg('form.msg.selectTargetUser'));
                        return false;
                    }
                    switch (type) {
                        case 'searchCriteria':
                            addUserInTargetUser(targetArray);
                            break;
                        case 'defaultCustom':
                            defaultCustom.value = targetArray[0].value;
                            defaultCustom.setAttribute('data-search-value', targetArray[0].id);
                            break;
                    }
                    modal.hide();
                }
            }, {
                content: i18n.msg('common.btn.cancel'),
                classes: 'z-button secondary',
                bindKey: false,
                callback: (modal) => {
                    modal.hide();
                }
            }],
            close: { closable: false },
            onCreate: function () {
                document.getElementById('search').addEventListener('keyup', aliceJs.debounce ((e) => {
                    getTargetUserList(e.target.value, isMulti, targetArray, false);
                }), false);
                getTargetUserList(document.getElementById('search').value, isMulti, targetArray,true);

                // 기존 사용자 목록
                targetArray.length = 0;
                switch (type) {
                    case 'searchCriteria':
                        const targetCriteria = document.getElementById('changeTargetCriteria');
                        targetCriteria.querySelectorAll('.user-search-item').forEach( (elem) => {
                            const inputElem = elem.querySelector('.z-input');
                            if (inputElem) {
                                targetArray.push({id: inputElem.getAttribute('data-user-id'), value: inputElem.value});
                            }
                        });
                    break;
                    case 'defaultCustom':
                        // todo: if문 검증
                        if (defaultCustom.value !== '') {
                            targetArray.push({
                                id: defaultCustom.getAttribute('data-search-value'),
                                value: defaultCustom.value
                            });
                        }
                    break;
                }
            }
        });
        targetUserModal.show();
    }

    /**
     * 사용자 조회
     */
    function getTargetUserList(search, isMulti, targetArray, showProgressbar) {
        let strUrl = '/users/substituteUsers?search=' + encodeURIComponent(search.trim())
            + '&from=&to=&userKey=&multiSelect=' + isMulti;
        aliceJs.fetchText(strUrl, {
            method: 'GET',
            showProgressbar: showProgressbar
        }).then((htmlData) => {
            const targetUserList = document.getElementById('targetUserList');
            targetUserList.innerHTML = htmlData;
            OverlayScrollbars(targetUserList.querySelector('.z-table-body'), {className: 'scrollbar'});
            // 갯수 가운트
            aliceJs.showTotalCount(targetUserList.querySelectorAll('.z-table-row').length);
            // 체크 이벤트
            targetUserList.querySelectorAll('input[type=checkbox], input[type=radio]').forEach((element) => {
                element.addEventListener('change', function(e) {
                    if (e.target.checked) {
                        isMulti ? targetArray.push({id: e.target.id, value: e.target.value})
                            : targetArray.splice(0, targetArray.length, {id: e.target.id, value: e.target.value});
                    } else {
                        targetArray = targetArray.filter((item) => item.id !== e.target.id);
                    }
                });
            });
            // 기존 선택값 표시
            targetArray.forEach( (target) => {
                const targetCheckBox = targetUserList.querySelector('input[id="' + target.id + '"]');
                if (targetCheckBox) {
                    targetCheckBox.checked = true;
                }
            });
        });
    }

    /**
     * 사용자 추가
     */
    function addUserInTargetUser(dataList) {
        const targetCriteria = document.getElementById('changeTargetCriteria');
        // 전체 목록 삭제 후
        targetCriteria.querySelectorAll('.user-search-item').forEach( (elem) => {
            elem.remove();
        });

        // 다시 그려줌
        let listTemplate = ``;
        dataList.forEach( (data) => {
            listTemplate += `<div class="flex-row mt-2 user-search-item">` +
                `<div class="flex-column col-10 mr-4">` +
                    `<input class="z-input" readonly data-user-id="${data.id}" value="${data.value}">` +
                `</div>` +
                `<div class="flex-column">` +
                    `<button type="button" class="z-button-icon extra user-search-delete-btn"` +
                        ` data-user-id="${data.id}"><span class="z-icon i-delete"></span>` +
                    `</button>` +
                `</div>` +
            `</div>`;
        });
        targetCriteria.insertAdjacentHTML('beforeend', listTemplate);

        // 삭제 이벤트
        targetCriteria.querySelectorAll('.user-search-delete-btn').forEach((btn) => {
            btn.addEventListener('click', function (e) {
                e.target.parentNode.parentNode.remove();

                const removeIndex = targetUserArray.findIndex(function (user) {
                    return user.id === e.target.getAttribute('data-user-id');
                });
                targetUserArray.splice(removeIndex, 1);
            });
        });
    }
    /**
     * Organization Search.
     *
     * @param {Object} property Attribute 데이터
     * @constructor
     */
    function OrganizationSearch(property) {
        const objectId = attributeTypeList[9].type; // organizationSearch

        // 필수값
        const requiredTemplate = getRequiredAttributeTemplate(objectId, property.required);
        // 기본 값
        const defaultValueTemplate = getDefaultValueTemplate(objectId, property.defaultValue);

        this.template = `${requiredTemplate}` + `${defaultValueTemplate}`;
        parent.insertAdjacentHTML('beforeend', this.template);

        // 기본값 radio 관련 disabled event 설정
        disableUncheckedRadio(objectId);

        // 지정 부서 선택 모달 오픈 todo
        document.getElementById(objectId + '-default-custom')
            .addEventListener('click', openOrganizationSearchModal, false);
    }

    /**
     * 공통 필수여부 템플릿 반환
     * @param id 속성타입
     * @param selectedValue 선택된 값
     * @returns {string} 템플릿리터럴
     */
    function getRequiredAttributeTemplate(id, selectedValue) {
        const booleanOptions = [{'text': 'Y', 'value': 'true'}, {'text': 'N', 'value': 'false'}].map(function (option) {
            return `<option value='${option.value}' ${selectedValue === option.value ? 'selected=\'true\'' : ''}>` +
                `${aliceJs.filterXSS(option.text)}</option>`;
        }).join('');

        return `<div class="flex-row mt-2">` +
                `<div class="flex-column col-2 mr-4">` +
                    `<label>` +
                        `<span class="mr-1">${i18n.msg('cmdb.attribute.label.option.required')}</span>` +
                        `<span class="required"></span>` +
                    `</label>` +
                `</div>` +
                `<div class="flex-column col-9">` +
                    `<select id="${id}-required">${booleanOptions}</select>` +
                `</div>` +
            `</div>`;
    }

    /**
     * userSearch, organizationSearch 기본값 설정 템플릿 반환
     * @param id 속성타입
     * @param selectedValue 선택된 값
     * @returns {string} 템플릿리터럴
     */
    function getDefaultValueTemplate(id, selectedValue) {
        const defaultType = selectedValue !== undefined ? selectedValue.type : 'none';
        let defaultData = selectedValue !== undefined ? selectedValue.data : '';
        defaultData = defaultData.includes('|') ? defaultData.split('|') : ['', ''];

        return `<div class="flex-row mt-2">
                <div class="flex-column col-2 mr-4">
                    <label>
                        <span>${i18n.msg('form.properties.element.defaultValue')}</span>
                    </label>
                </div>
                <div class="flex-column col-9">
                    <label class="z-radio">
                        <input name="${id}-default" id="${id}-none" type="radio" value="none" ${defaultType === 'none' ? 'checked=\'true\'' : ''}>
                        <span></span>
                        <span class="label">${i18n.msg('form.properties.option.none')}</span>
                    </label>
                </div>
            </div>
            <div class="flex-row mt-2">
                <div class="flex-column col-2 mr-4"><label><span></span></label></div>
                <div class="flex-column col-9">
                    <label class="z-radio">
                        <input name="${id}-default" id="${id}-session" type="radio" value="session" ${defaultType === 'session' ? 'checked=\'true\'' : ''}>
                        <span></span>
                        <span class="label">${i18n.msg('form.properties.default.session')}</span>
                    </label>
                </div>
            </div>
            <div class="flex-row mt-2">
                <div class="flex-column col-2 mr-4"><label><span></span></label></div>
                <div class="flex-column col-2">
                    <label class="z-radio">
                        <input name="${id}-default" id="${id}-custom" type="radio" value="custom" ${defaultType === 'custom' ? 'checked=\'true\'' : ''}>
                        <span></span>
                        <span class="label">${i18n.msg('form.properties.default.custom')}</span>
                    </label>
                </div>
                <div class="flex-column col-7">
                    <div class="flex-row z-input-button">
                        <input class="z-input" type="text" readonly="true" id="${id}-default-custom-text" value="${defaultData[1]}" 
                            data-search-value="${defaultData[0]}" ${defaultType === 'custom' ? '' : 'disabled=\'true\''}/>
                        <button class="z-button-icon z-button-code" type="button" id="${id}-default-custom" 
                            data-value="${defaultData[0]}" ${defaultType === 'custom' ? '' : 'disabled=\'true\''}>
                            <span class="z-icon i-search"></span>
                        </button>
                    </div>
                </div>
            </div>`.trim();
    }

    /**
     * Attribute 목록 모달 오픈
     */
    function openAttributeListModal(e) {
        // 저장된 데이터를 담는다.
        attributeMapTemp.length = 0;
        attributeMapTemp = JSON.parse(JSON.stringify(attributeMap));

        // 모달 내부 template
        const attributeListModalContent = `<div class="cmdb-class-attribute-list">` +
            `<input class="z-input i-search col-5 mr-2" type="text" name="search" id="attributeSearch" ` +
            `maxlength="100" placeholder="${i18n.msg('cmdb.attribute.label.searchPlaceholder')}"/>` +
            `<span id="spanTotalCount" class="z-search-count"></span>` +
            `<div class="table-set" id="ciClassAttributeList"></div>` +
            `</div>`;

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
                        addGroupList({key: attr.key, value: attr.value, order: attr.order, type: attr.type});
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
                document.getElementById('attributeSearch').addEventListener('keyup', aliceJs.debounce ((e) => {
                    getAttributeList(e.target.value, false);
                }), false);
                getAttributeList(document.getElementById('attributeSearch').value, false);
            }
        });
        attributeListModal.show();
    }

    /**
     * 세부 속성 검색
     */
    function getAttributeList(search, showProgressbar) {
        const url = '/cmdb/attributes/list-modal?search=' + encodeURIComponent(search.trim()) +
            '&attributeId=' + attributeId;
        aliceJs.fetchText(url, {
            method: 'GET',
            showProgressbar: showProgressbar
        }).then((htmlData) => {
            document.getElementById('ciClassAttributeList').innerHTML = htmlData;
            aliceJs.showTotalCount(document.querySelectorAll('.attribute-list').length);
            OverlayScrollbars(document.querySelector('.z-table-body'), {className: 'scrollbar'});

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
                        const removeIndex = attributeMapTemp.findIndex(function (attr) {
                            return attr.key === e.target.value;
                        });
                        attributeMapTemp.splice(removeIndex, 1);
                    }
                });
                attributeMapTemp.forEach(function (attr) {
                    if (checkbox.value === attr.key) {
                        checkbox.checked = true;
                    }
                });
            });
        });
    }

    /**
     * 중복 유효성 검사.
     *
     * @param type
     * @return {boolean}
     */
    function checkDuplicate(type) {
        let isValid = true;
        if (type === 'radio' || type === 'checkbox') {
            let detailsObject = document.querySelectorAll('#details .flex-row');
            let labels = [];
            let values = [];
            for (let i = 0, len = detailsObject.length; i < len; i++) {
                let labelObject = detailsObject[i].querySelectorAll('input')[0];
                let valueObject = detailsObject[i].querySelectorAll('input')[1];
                if (labels.indexOf(labelObject.value.trim()) > -1 || values.indexOf(valueObject.value.trim()) > -1) {
                    zAlert.warning(i18n.msg('validation.msg.dataNotDuplicate'));
                    isValid = false;
                    break;
                }
                labels.push(labelObject.value.trim());
                values.push(valueObject.value.trim());
            }
        } else if (type === 'dropdown') {
            let detailsObject = document.querySelectorAll('#dropdownListData .flex-row:not(:first-child)');
            let labels = [];
            let values = [];
            for (let i = 0, len = detailsObject.length; i < len; i++) {
                let labelObject = detailsObject[i].querySelectorAll('input')[0];
                let valueObject = detailsObject[i].querySelectorAll('input')[1];
                if (labels.indexOf(labelObject.value.trim()) > -1 || values.indexOf(valueObject.value.trim()) > -1) {
                    zAlert.warning(i18n.msg('validation.msg.dataNotDuplicate'));
                    isValid = false;
                    break;
                }
                labels.push(labelObject.value.trim());
                values.push(valueObject.value.trim());
            }
        } else if (type === 'userSearch') {
            // 사용자 검색시 조회대상이 존재하지 않으면 체크
            const userList = document.querySelectorAll('#details .user-search-item');
            const targetCriteria = document.getElementById('userSearchCriteria');
            if (targetCriteria.value === 'custom' && !userList.length) {
                zAlert.warning(i18n.msg('common.msg.required', i18n.msg('form.properties.element.searchTarget')));
                isValid = false;
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

                if (parseInt(details.maxLength) <= parseInt(details.minLength)) {
                    zAlert.warning(i18n.msg('cmdb.attribute.msg.comepareWithMaxLength'));
                    return false;
                }
                if (parseInt(details.maxLength) > inputTypeAttributeDefaultMaxLength) {
                    zAlert.warning(i18n.msg('cmdb.attribute.msg.maxLength'));
                    return false;
                }
                break;
            case 'dropdown':
                details.required = parent.querySelector('#' + attributeTypeList[1].type + '-required').value;

                let dropdownOption = [];
                document.querySelectorAll('#dropdownListData > .flex-row').forEach(function (object) {
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
                        const codeText = parent.querySelector('#' + attributeTypeList[4].type + '-default-code-text');
                        defaultValue = codeText.getAttribute('data-value') + '|' + codeText.value;
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
            case 'date':
                details.required = parent.querySelector('#' + attributeTypeList[6].type + '-required').value;
                details.minDate = parent.querySelector('#' + attributeTypeList[6].type + '-minDate').value;
                details.maxDate = parent.querySelector('#' + attributeTypeList[6].type + '-maxDate').value;
                if ((details.minDate !== '' && details.maxDate !== '') && details.maxDate < details.minDate) {
                    zAlert.warning(i18n.msg('cmdb.attribute.msg.maxDate'));
                    return false;
                }
                break;
            case 'datetime':
                details.required = parent.querySelector('#' + attributeTypeList[7].type + '-required').value;
                details.minDateTime = parent.querySelector('#' + attributeTypeList[7].type + '-minDateTime').value;
                details.maxDateTime = parent.querySelector('#' + attributeTypeList[7].type + '-maxDateTime').value;
                if ((details.minDateTime !== '' && details.maxDateTime !== '') && details.maxDateTime < details.minDateTime) {
                    zAlert.warning(i18n.msg('cmdb.attribute.msg.maxDateTime'));
                    return false;
                }
                break;
            case 'userSearch':
                const userDefaultType = parent.querySelector(`input[name="${attributeTypeList[8].type}-default"]:checked`).value;
                const userDefaultData = parent.querySelector('#' + attributeTypeList[8].type + '-default-custom-text');
                const targetCriteria = parent.querySelector('#' + attributeTypeList[8].type + 'Criteria').value;
                const targetCriteriaElem = parent.querySelector('#changeTargetCriteria');
                let searchkeys = [];
                if (targetCriteria === 'organization') { // 부서별 조회
                    const searchTargetElem = targetCriteriaElem.querySelector('#searchTarget');
                    if (searchTargetElem) {
                        searchkeys.push({
                            id: searchTargetElem.getAttribute('data-value'),
                            value: searchTargetElem.value
                        });
                    }
                } else { // 대상 목록 지정
                    searchkeys = targetUserArray;
                }
                details.required = parent.querySelector('#' + attributeTypeList[8].type + '-required').value;
                details.targetCriteria = targetCriteria;
                details.searchKey = searchkeys;
                details.defaultValue = {
                    type: userDefaultType,
                    data: userDefaultType === 'custom'
                        ? userDefaultData.getAttribute('data-search-value') + '|' + userDefaultData.value : ''
                };
                break;
            case 'organizationSearch':
                const organizationDefaultType = parent.querySelector(`input[name="${attributeTypeList[9].type}-default"]:checked`).value;
                const organizationDefaultData = parent.querySelector('#' + attributeTypeList[9].type + '-default-custom-text');

                details.required = parent.querySelector('#' + attributeTypeList[9].type + '-required').value;
                details.defaultValue = {
                    type: organizationDefaultType,
                    data: organizationDefaultType === 'custom'
                        ? organizationDefaultData.getAttribute('data-search-value') + '|' + organizationDefaultData.value : ''
                };
                break;
            default:
                break;
        }

        return details;
    }

    /**
     * 데이터를 Recursive 하게 돌며 세부 속성을 출력한다.
     * @param data 데이터
     * @param parent 엘리먼트 객체
     * @param type 타입
     */
    function makeCIAttribute(data, parent, type) {
        if (type === 'root') {
            data.forEach(function (g) {
                makeCIAttribute(g, parent, 'group');
            });
        } else if (type === 'group') {
            const group = addCIAttribute('group', data, parent);
            data.attributes.forEach(function (r) {
                makeCIAttribute(r, group, 'row');
            });
        } else if (type === 'row') {
            const row = addCIAttribute('row', data, parent);
            // 라벨 추가
            addCIAttribute('label', data, row);
            // 엘리먼트 추가
            const element = addCIAttribute(data.attributeType, data, row);
            // 'Group List' 일 경우
            if (data.attributeType === 'group-list' && element) {
                // 2차원 배열로 데이터 재정의 (순서 맞추기 위함)
                const attributeIds = element.getAttribute('data-attributeOrder').split('|');
                const childAttributes = JSON.parse(JSON.stringify(data['childAttributes']));
                const rowCount = parseInt(childAttributes.length / attributeIds.length);
                const rowArray = [];
                for (let i = 0; i < rowCount; i++) {
                    const columnArray = [];
                    for (let j = 0; j < attributeIds.length; j++) {
                        columnArray.push(
                            childAttributes.find(function (child) {
                                return (child['attributeOrder'] === i) && child.attributeId === attributeIds[j];
                            }));
                    }
                    rowArray.push(columnArray);
                }
                data['childAttributes'] = rowArray;
                // 세부 속성 출력
                rowArray.forEach(function (childArray) {
                    const rowElem = addCIAttribute('group-list-row', childArray, element);
                    childArray.forEach(function (column) {
                        makeCIAttribute(column, rowElem, 'child');
                    });
                });
            }
        } else if (type === 'child') {
            const child = addCIAttribute('child-row', data, parent);
            // 라벨 추가
            addCIAttribute('label', data, child);
            // 엘리먼트 추가
            addCIAttribute(data.attributeType, data, child);
        } else {
            console.error('Invalid Type');
        }
    }

    /**
     * 세부 속성을 표시하기 위한 엘리먼트 객체를 생성하며 생선한 엘리먼트 객체를 반환한다.
     * @param type 타입
     * @param data 데이터
     * @param parent 부모 element 객체
     * @returns element 객체
     */
    function addCIAttribute(type, data, parent) {
        const attributeValue = (typeof data.attributeValue === 'undefined' || data.attributeValue === null) ? '' :
            JSON.parse(data.attributeValue);
        let elem = null;
        switch (type) {
            case 'group':
                // class 제목
                const classTitleElem = document.createElement('h3');
                classTitleElem.className = 'sub-title under-bar bold mt-4';
                classTitleElem.textContent = data.className;
                parent.appendChild(classTitleElem);

                elem = document.createElement('div');
                elem.className = 'attribute-group mb-2';
                parent.appendChild(elem);
                return elem;
            case 'row':
                elem = document.createElement('div');
                elem.className = 'flex-column ' + displayMode + '-row attribute';
                elem.setAttribute('data-attributeType', data.attributeType);
                parent.appendChild(elem);
                return elem;
            case 'child-row':
                elem = document.createElement('div');
                elem.className = 'flex-column ' + displayMode + '-row child-attribute';
                elem.setAttribute('data-attributeType', data.attributeType);
                parent.appendChild(elem);
                return elem;
            case 'label':
                elem = document.createElement('label');
                elem.className = 'field-label';
                // 문구
                const labelTextElem = document.createElement('span');
                labelTextElem.textContent = data.attributeText;
                elem.appendChild(labelTextElem);
                parent.appendChild(elem);
                // 필수여부
                if (typeof attributeValue.required !== 'undefined' && attributeValue.required === 'true' && displayMode === 'edit') {
                    elem.insertAdjacentHTML('beforeend', `<span class="required"></span>`);
                }
                return elem;
            case 'inputbox':
                elem = document.createElement('input');
                elem.type = 'text';
                elem.className = 'z-input';
                elem.id = ZWorkflowUtil.generateUUID();
                elem.setAttribute('data-attributeId', data.attributeId);
                elem.value = data.value;
                elem.readOnly = (displayMode === 'view');
                if (attributeValue !== '') {
                    if (attributeValue.required === 'true') {
                        elem.required = true;
                        elem.setAttribute('data-validation-required', 'true');
                        elem.setAttribute('data-validation-required-name', data.attributeText);
                    }
                    // 유효성 검증
                    elem.addEventListener('keyup', function (e) {
                        e.preventDefault();
                        const userKeyCode = e.keyCode ? e.keyCode : e.which;
                        if (userKeyCode === 13) {
                            return false;
                        }

                        const target = e.target;
                        if (attributeValue.validate === 'char') {
                            isValidChar(target.id, true);
                        } else if (attributeValue.validate === 'number') {
                            isValidNumber(target.id, true);
                        }

                        if (attributeValue.maxLength !== '') {
                            isValidMaxLength(target.id, attributeValue.maxLength, true);
                        }
                        if (attributeValue.minLength !== '') {
                            isValidMinLength(target.id, attributeValue.minLength, true);
                        }
                    });
                }
                parent.appendChild(elem);
                return elem;
            case 'dropdown':
                elem = document.createElement('select');
                elem.id = ZWorkflowUtil.generateUUID();
                elem.setAttribute('data-attributeId', data.attributeId);
                elem.className = (displayMode === 'view') ? 'readonly' : '';
                if (typeof attributeValue.option !== 'undefined') {
                    for (let opt = 0, optLen = attributeValue.option.length; opt < optLen; opt++) {
                        const attributeOption = attributeValue.option[opt];
                        const selectOption = document.createElement('option');
                        selectOption.textContent = (elem.className == 'readonly' && attributeOption.value == '') ? '' : attributeOption.text;
                        selectOption.value = attributeOption.value;
                        if (selectOption.value === data.value) {
                            selectOption.selected = true;
                        }
                        elem.appendChild(selectOption);
                    }
                }
                parent.appendChild(elem);
                return elem;
            case 'radio':
                if (typeof attributeValue.option !== 'undefined') {
                    const radioId = ZWorkflowUtil.generateUUID();
                    for (let opt = 0, optLen = attributeValue.option.length; opt < optLen; opt++) {
                        const attributeOption = attributeValue.option[opt];
                        const radioGroup = document.createElement('label');
                        radioGroup.className = 'z-radio' + (opt > 0 ? ' mt-1' : '');
                        radioGroup.tabindex = 0;
                        radioGroup.htmlFor = radioId + '-' + opt;
                        if (displayMode === 'view') {
                            radioGroup.classList.add('readonly');
                        }

                        const radio = document.createElement('input');
                        radio.type = 'radio';
                        radio.id = radioId + '-' + opt;
                        radio.setAttribute('data-attributeId', data.attributeId);
                        radio.name = 'attribute-radio';
                        radio.value = attributeOption.value;
                        if (attributeOption.value === data.value) {
                            radio.setAttribute('checked', 'checked');
                        }
                        if (data.value === '' && opt === 0) {
                            radio.setAttribute('checked', 'checked');
                        }
                        radioGroup.appendChild(radio);

                        const radioSpan = document.createElement('span');
                        radioGroup.appendChild(radioSpan);

                        const radioLabel = document.createElement('span');
                        radioLabel.className = 'label';
                        radioLabel.textContent = attributeOption.text;
                        radioGroup.appendChild(radioLabel);
                        parent.appendChild(radioGroup);
                    }
                }
                return elem;
            case 'checkbox':
                if (typeof attributeValue.option !== 'undefined') {
                    const checkboxId = ZWorkflowUtil.generateUUID();
                    for (let opt = 0, optLen = attributeValue.option.length; opt < optLen; opt++) {
                        const attributeOption = attributeValue.option[opt];
                        const chkGroup = document.createElement('label');
                        chkGroup.className = 'z-checkbox';
                        chkGroup.tabindex = 0;
                        chkGroup.htmlFor = checkboxId + '-' + opt;
                        if (displayMode === 'view') {
                            chkGroup.classList.add('readonly');
                        }

                        const chk = document.createElement('input');
                        chk.type = 'checkbox';
                        chk.id = checkboxId + '-' + opt;
                        chk.setAttribute('data-attributeId', data.attributeId);
                        chk.name = 'attribute-checkbox';
                        chk.value = attributeOption.value;
                        if (data.value !== null) {
                            if (data.value.indexOf(attributeOption.value) > -1) {
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
                        parent.appendChild(chkGroup);
                    }
                }
                return elem;
            case 'custom-code':
                const customCodeId = ZWorkflowUtil.generateUUID();
                let customValueArr = '';
                if (data.value !== null) {
                    customValueArr = data.value.split('|');
                } else {
                    if (attributeValue !== '') {
                        customValueArr = attributeValue.default.value.split('|');
                    }
                }

                elem = document.createElement('div');
                elem.id = customCodeId;
                elem.setAttribute('data-attributeId', data.attributeId);
                elem.className = 'z-custom-code flex-row';

                const customInputElem = document.createElement('input');
                customInputElem.type = 'text';
                customInputElem.className = 'z-input z-input-button col-pct-12 inherit';
                customInputElem.value = (customValueArr.length > 0) ? customValueArr[1] : '';
                customInputElem.readOnly = true;
                elem.appendChild(customInputElem);

                const customBtnElem = document.createElement('button');
                customBtnElem.type = 'button';
                customBtnElem.className = 'z-button z-button-icon secondary';
                customBtnElem.disabled = (displayMode === 'view');
                customBtnElem.setAttribute('data-custom-code', attributeValue.customCode);

                const customSpanElem = document.createElement('span');
                customSpanElem.className = 'z-icon i-search';
                customBtnElem.appendChild(customSpanElem);
                elem.appendChild(customBtnElem);

                let customData = data.value; // 'key|값'
                if (attributeValue !== '') {
                    let defaultValue = '';
                    customSpanElem.textContent = attributeValue.button;
                    // 커스텀 코드 기본 값 넣기
                    if (data.value === '' || data.value === null) {
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
                    customInputElem.setAttribute('data-custom-data', customData);
                    customBtnElem.addEventListener('click', openCustomCodeModal, false);
                }
                parent.appendChild(elem);
                return elem;
            case 'group-list':
                elem = document.createElement('div');
                elem.className = 'child-attribute-group pt-2';
                elem.setAttribute('data-attributeId', data.attributeId);
                if (typeof attributeValue.option !== 'undefined' && data.childAttributes.length > 0) {
                    // 순서 표시
                    if (attributeValue.option.length > 1) {
                        attributeValue.option.sort(function (a, b) {
                            return a.order < b.order ? -1 : a.order > b.order ? 1 : 0;
                        });
                    }
                    const attributeOrderIds = attributeValue.option.reduce(function (acc, cur) {
                        return acc + '|' + cur.id;
                    }, '');
                    elem.setAttribute('data-attributeOrder', attributeOrderIds.substr(1));
                    // 추가 버튼
                    if (displayMode === 'edit') {
                        const addBtn = document.createElement('button');
                        addBtn.type = 'button';
                        addBtn.className = 'z-button-icon secondary z-button-attribute-add';
                        addBtn.insertAdjacentHTML('beforeend', `<span class="z-icon i-plus"></span>`);
                        addBtn.addEventListener('click', addCIAttributeChild, false);
                        elem.appendChild(addBtn);
                    }
                }
                parent.appendChild(elem);
                return elem;
            case 'group-list-row':
                elem = document.createElement('div');
                elem.className = 'child-attribute-row mt-2';
                // 삭제 버튼
                if (parent.children.length > 1 && displayMode === 'edit') {
                    const removeBtn = document.createElement('button');
                    removeBtn.type = 'button';
                    removeBtn.className = 'z-button-icon extra z-button-attribute-delete';
                    removeBtn.insertAdjacentHTML('beforeend', `<span class="z-icon i-delete"></span>`);
                    removeBtn.addEventListener('click', removeCIAttributeChild, false);
                    elem.appendChild(removeBtn);
                }
                parent.appendChild(elem);
                return elem;
            case 'date':
                elem = document.createElement('div');
                const dateElem = document.createElement('input');
                dateElem.className = 'z-input i-date-picker search-date col-3';
                dateElem.id = ZWorkflowUtil.generateUUID();
                dateElem.setAttribute('data-attributeId', data.attributeId);
                dateElem.value = i18n.userDate(data.value);
                dateElem.readOnly = (displayMode === 'view');
                dateElem.placeholder = i18n.dateFormat;
                if (attributeValue !== '') {
                    if (attributeValue.required === 'true') {
                        dateElem.required = true;
                        dateElem.setAttribute('data-validation-required', 'true');
                        dateElem.setAttribute('data-validation-required-name', data.attributeText);
                        dateElem.setAttribute('data-validation-min-date', attributeValue.minDate !== undefined ? attributeValue.minDate : '');
                        dateElem.setAttribute('data-validation-max-date', attributeValue.maxDate !== undefined ? attributeValue.maxDate : '');
                    }
                }

                elem.append(dateElem);
                zDateTimePicker.initDatePicker(dateElem, validateDateTimeValue);
                parent.appendChild(elem);
                return elem;
            case 'datetime':
                elem = document.createElement('div');
                const dateTimeElem = document.createElement('input');
                dateTimeElem.className = 'z-input i-datetime-picker search-datetime col-3';
                dateTimeElem.id = ZWorkflowUtil.generateUUID();
                dateTimeElem.setAttribute('data-attributeId', data.attributeId);
                dateTimeElem.setAttribute('type', type);
                dateTimeElem.value = i18n.userDateTime(data.value);
                dateTimeElem.readOnly = (displayMode === 'view');
                dateTimeElem.placeholder = i18n.dateTimeFormat;
                if (attributeValue !== '') {
                    if (attributeValue.required === 'true') {
                        dateTimeElem.required = true;
                        dateTimeElem.setAttribute('data-validation-required', 'true');
                        dateTimeElem.setAttribute('data-validation-required-name', data.attributeText);
                        dateTimeElem.setAttribute('data-validation-min-date', attributeValue.minDateTime !== undefined ? attributeValue.minDateTime : '');
                        dateTimeElem.setAttribute('data-validation-max-date', attributeValue.maxDateTime !== undefined ? attributeValue.maxDateTime : '');
                    }
                }
                elem.append(dateTimeElem);
                zDateTimePicker.initDateTimePicker(dateTimeElem, validateDateTimeValue);
                parent.appendChild(elem);
                return elem;
            case 'userSearch':
                elem = document.createElement('input');
                elem.type = 'text';
                elem.className = 'z-input i-user-search text-ellipsis';
                elem.id = ZWorkflowUtil.generateUUID();
                elem.setAttribute('data-attributeId', data.attributeId);
                elem.setAttribute('data-attributeValue', data.attributeValue);
                elem.setAttribute('data-modalTitle', data.attributeText);
                elem.setAttribute('oncontextmenu', 'return false;');
                elem.setAttribute('onkeypress', 'return false;');
                elem.setAttribute('onkeydown', 'return false;');
                elem.setAttribute('data-realTimeSelectedUser', ((data.value !== null && data.value !== '') ? data.value : ''));
                elem.readOnly = (displayMode === 'view');
                if (attributeValue.required === 'true') {
                    elem.required = true;
                    elem.setAttribute('data-validation-required', 'true');
                    elem.setAttribute('data-validation-required-name', data.attributeText);
                }
                elem.addEventListener('click', openUserSearchModal, false);

                // 기본 값 설정
                const userDefaultType = (attributeValue.defaultValue !== undefined && attributeValue.defaultValue !== null)
                    ? attributeValue.defaultValue.type : 'none';
                let userDefaultData = (attributeValue.defaultValue !== undefined && attributeValue.defaultValue !== null)
                    ? attributeValue.defaultValue.data : '';
                switch(userDefaultType) {
                    case 'session':
                        userDefaultData = [userInfo.userKey, userInfo.userName];
                        break;
                    case 'custom':
                        userDefaultData = userDefaultData.includes('|') ? userDefaultData.split('|') : ['', ''];
                        break;
                    default:
                        userDefaultData = ['', ''];
                        break;
                }
                const userDefaultValues = (data.value !== null && data.value !== '') ? data.value.split('|') : userDefaultData;
                elem.setAttribute('data-user-search', userDefaultValues[0]);
                elem.value = userDefaultValues[1];

                // 기본 값 유효성 검증 - 설정된 기본값이 설정한 검색조건 내에 없을 경우 공란으로 표시
                getUserList(elem, userDefaultData[1], false, true);

                parent.appendChild(elem);
                return elem;
            case 'organizationSearch':
                elem = document.createElement('input');
                elem.type = 'text';
                elem.className = 'z-input i-organization-search text-ellipsis';
                elem.id = ZWorkflowUtil.generateUUID();
                elem.setAttribute('data-attributeId', data.attributeId);
                elem.setAttribute('data-modalTitle', data.attributeText);
                elem.setAttribute('oncontextmenu', 'return false;');
                elem.setAttribute('onkeypress', 'return false;');
                elem.setAttribute('onkeydown', 'return false;');
                elem.readOnly = (displayMode === 'view');
                if (attributeValue.required === 'true') {
                    elem.required = true;
                    elem.setAttribute('data-validation-required', 'true');
                    elem.setAttribute('data-validation-required-name', data.attributeText);
                }
                elem.addEventListener('click', openOrganizationSearchModal, false);

                // 기본 값 설정
                const organizationDefaultType = (attributeValue.defaultValue !== undefined && attributeValue.defaultValue !== null)
                    ? attributeValue.defaultValue.type : 'none';
                let organizationDefaultData = (attributeValue.defaultValue !== undefined && attributeValue.defaultValue !== null)
                    ? attributeValue.defaultValue.data : '';
                switch(organizationDefaultType) {
                    case 'session':
                        organizationDefaultData = [userInfo.department, userInfo.departmentName];
                        break;
                    case 'custom':
                        organizationDefaultData = organizationDefaultData.includes('|')
                            ? organizationDefaultData.split('|') : ['', ''];
                        break;
                    default:
                        organizationDefaultData = ['', ''];
                        break;
                }
                const defaultValues = (data.value !== null && data.value !== '') ? data.value.split('|') : organizationDefaultData;
                elem.setAttribute('data-organization-search', defaultValues[0]);
                elem.value = defaultValues[1];

                parent.appendChild(elem);
                return elem;
            default:
                break;
        }
    }

    /**
     * Date, DateTime 유효성 검증
     * @param target
     */
    function validateDateTimeValue(target) {
        // 최소 날짜 ,최대 날짜 유효성 검증
        if (target.getAttribute('data-validation-min-date') && target.value < target.getAttribute('data-validation-min-date')) {
            zAlert.warning(i18n.msg('common.msg.selectAfterDate', target.getAttribute('data-validation-min-date')), () => {
                target.classList.add('error');
                target.focus();
            });
        } else if (target.getAttribute('data-validation-max-date') && target.value > target.getAttribute('data-validation-max-date')) {
            zAlert.warning(i18n.msg('common.msg.selectBeforeDate', target.getAttribute('data-validation-max-date')), () => {
                target.classList.add('error');
                target.focus();
            });
        } else {
            target.classList.remove('error');
        }
    }

    /**
     * 커스텀 코드 모달
     * @param e 이벤트
     */
    function openCustomCodeModal(e) {
        e.stopPropagation();

        const customGroup = e.target.parentNode;
        const customInput = customGroup.querySelector('.z-input-button');
        let customCodeData = {
            componentId: customGroup.id,
            componentValue: customInput.getAttribute('data-custom-data'),
            customCode: e.target.getAttribute('data-custom-code')
        };
        const storageName = 'alice_custom-codes-search-' + customGroup.id;
        sessionStorage.setItem(storageName, JSON.stringify(customCodeData));
        const defaultValues = customCodeData.componentValue.split('|');

        tree.load({
            view: 'modal',
            title: i18n.msg('form.label.customCodeTarget'),
            dataUrl: '/rest/custom-codes/' + customCodeData.customCode,
            target: 'treeList',
            text: 'codeName',
            rootAvailable: false,
            leafIcon: '/assets/media/icons/tree/icon_tree_leaf.svg',
            selectedValue: defaultValues[0],
            callbackFunc: (response) => {
                const customData = response.id + '|' + response.textContent;
                customInput.setAttribute('data-custom-data', customData);
                customInput.value = response.textContent;
            }
        });
    }

    /**
     * 하위 세부 속성 추가시 (+ 버튼 클릭시) 호출되는 이벤트
     * @param e 이벤트
     */
    function addCIAttributeChild(e) {
        e.preventDefault();

        const childAttributeGroup = e.target.parentNode;
        const groupListAttribute = childAttributeGroup.parentNode;
        const attributeGroup = groupListAttribute.parentNode;
        const ciAttribute = attributeGroup.parentNode;
        const groupIdx = Array.prototype.indexOf.call(ciAttribute.querySelectorAll('.attribute-group'), attributeGroup);
        const attributeIdx = Array.prototype.indexOf.call(attributeGroup.children, groupListAttribute);
        const addIdx = childAttributeGroup.children.length - 1;
        const addData = JSON.parse(JSON.stringify(
            attributeDetailData[groupIdx].attributes[attributeIdx].childAttributes[0]
        ));

        // 하위 속성 추가
        const rowElem = addCIAttribute('group-list-row', [], childAttributeGroup);
        addData.forEach(function (childData) {
            childData.attributeOrder = addIdx;
            childData.value = '';
            makeCIAttribute(childData, rowElem, 'child');
        });
    }

    /**
     * 하위 세부 속성 삭제시 (휴지통 버튼 클릭시) 호출되는 이벤트
     * @param e 이벤트
     */
    function removeCIAttributeChild(e) {
        e.preventDefault();
        // 삭제
        const childAttributeRow = e.target.parentNode;
        childAttributeRow.parentElement.removeChild(childAttributeRow);
    }

    /**
     * 사용자 검색 모달
     */
    function openUserSearchModal(e) {
        const target = e.target || e;
        const targetUserModalTemplate = `<div class="target-user-list">` +
            `<input class="z-input i-search col-5 mr-2" type="text" name="search" id="search" maxlength="100" ` +
            `placeholder="` + i18n.msg('user.label.userSearchPlaceholder') + `">` +
            `<span id="spanTotalCount" class="search-count"></span>` +
            `<div class="table-set" id="searchUserList"></div>` +
            `</div>`;

        const targetUserModal = new modal({
            title: target.getAttribute('data-modalTitle'),
            body: targetUserModalTemplate,
            classes: 'target-user-modal',
            buttons: [{
                content: i18n.msg('common.btn.select'),
                classes: 'z-button primary',
                bindKey: false,
                callback: (modal) => {
                    // 최근 선택값이 있는 경우, 해당 사용자 id와 이름을 전달한다.
                    if (target.getAttribute('data-realTimeSelectedUser') === '') {
                        zAlert.warning(i18n.msg('form.msg.selectTargetUser'));
                        return false;
                    } else {
                        const realTimeSelectedUserArr = target.getAttribute('data-realTimeSelectedUser').split('|');
                        target.setAttribute('data-user-search', realTimeSelectedUserArr[0]);
                        target.setAttribute('data-user-id', realTimeSelectedUserArr[2]);
                        target.value = realTimeSelectedUserArr[1];
                    }
                    modal.hide();
                }
            }, {
                content: i18n.msg('common.btn.cancel'),
                classes: 'z-button secondary',
                bindKey: false,
                callback: (modal) => {
                    modal.hide();
                }
            }],
            close: { closable: false },
            onCreate: function () {
                // 기존 선택된 값 할당
                if (target.getAttribute('data-user-search') !== '') {
                    const realTimeSelectedUser = `${target.getAttribute('data-user-search')}|` +
                        `${target.value}|${target.getAttribute('data-user-id')}`;
                    target.setAttribute('data-realTimeSelectedUser', realTimeSelectedUser);
                }
                document.getElementById('search').addEventListener('keyup', aliceJs.debounce ((e) => {
                    getUserList(target, e.target.value, false);
                }), false);
                getUserList(target, document.getElementById('search').value, true);
                OverlayScrollbars(document.querySelector('.modal-content'), {className: 'scrollbar'});
            }
        });
        targetUserModal.show();
    }

    /**
     * 사용자 검색 모달 - 사용자 조회
     */
    function getUserList(target, search, showProgressbar, isValidate) {
        const attributeValue = JSON.parse(target.getAttribute('data-attributeValue'));
        const targetCriteria = attributeValue.targetCriteria;
        let searchKeys = '';
        attributeValue.searchKey.forEach( (elem, index) => {
            searchKeys += (index > 0) ? '+' + elem.id : elem.id;
        });
        // 검색
        const strUrl = '/users/searchUsers?searchValue=' + encodeURIComponent(search.trim()) +
            '&targetCriteria=' + targetCriteria + '&searchKeys=' + searchKeys;
        aliceJs.fetchText(strUrl, {
            method: 'GET',
            showProgressbar: showProgressbar
        }).then((htmlData) => {
            if (!isValidate) {
                const searchUserList = document.getElementById('searchUserList');
                searchUserList.innerHTML = htmlData;
                OverlayScrollbars(searchUserList.querySelector('.z-table-body'), {className: 'scrollbar'});
                // 갯수 가운트
                aliceJs.showTotalCount(searchUserList.querySelectorAll('.z-table-row').length);
                // 체크 이벤트
                searchUserList.querySelectorAll('input[type=radio]').forEach((element) => {
                    element.addEventListener('change', () => {
                        const userId = element.getAttribute('data-user-id');
                        const realTimeSelectedUser = element.checked ? `${element.id}|${element.value}|${userId}` : '';
                        target.setAttribute('data-realTimeSelectedUser', realTimeSelectedUser);
                    });
                });
                // 기존 선택값 표시
                const realTimeSelectedUser = target.getAttribute('data-realTimeSelectedUser');
                const checkedTargetId = realTimeSelectedUser.split('|')[0];
                const checkedTargetRadio = searchUserList.querySelector('input[id="' + checkedTargetId + '"]');
                if (checkedTargetId !== '' && checkedTargetRadio !== null) {
                    checkedTargetRadio.checked = true;
                }
            } else {
                // 기본 값 사용자 조회
                const userListElem = new DOMParser().parseFromString(htmlData.toString(), 'text/html');
                if (userListElem.querySelectorAll('.z-table-row').length === 0) {
                    target.value = '';
                    target.setAttribute('data-user-search', '');
                }
            }
        });
    }

    /**
     * 기본값 radio 관련 disabled 처리
     */
    function disableUncheckedRadio(objectId) {
        const defaultNoneObject = document.getElementById(objectId + '-none');
        const defaultSessionObject = document.getElementById(objectId + '-session');
        const defaultCodeObject = document.getElementById(objectId + '-custom');

        defaultNoneObject.addEventListener('click', function (e) {
            e.stopPropagation();
            document.getElementById(objectId + '-default-custom-text').disabled = true;
            document.getElementById(objectId + '-default-custom').disabled = true;
        });
        defaultSessionObject.addEventListener('click', function (e) {
            e.stopPropagation();
            document.getElementById(objectId + '-default-custom-text').disabled = true;
            document.getElementById(objectId + '-default-custom').disabled = true;
        });
        defaultCodeObject.addEventListener('click', function (e) {
            e.stopPropagation();
            document.getElementById(objectId + '-default-custom-text').disabled = false;
            document.getElementById(objectId + '-default-custom').disabled = false;
        });
    }

    /**
     * 부서 선택 모달
     */
    function openOrganizationSearchModal(e) {
        e.stopPropagation();

        // 부서 검색 component || 부서 검색 기본값 - 부서 지정
       const target = e.target.getAttribute('data-organization-search')
            ? e.target : e.target.parentNode.querySelector('input[type=text]');

        const organizationSearchData = target.getAttribute('data-organization-search')
            || target.getAttribute('data-search-value');


        tree.load({
            view: 'modal',
            title: target.getAttribute('data-modalTitle')
                || i18n.msg('form.properties.department') + ' ' + i18n.msg('form.properties.default.custom'),
            dataUrl: '/rest/organizations',
            target: 'treeList',
            source: 'organization',
            text: 'organizationName',
            nodeNameLabel: i18n.msg('common.msg.dataSelect', i18n.msg('department.label.deptName')),
            defaultIcon: '/assets/media/icons/tree/icon_tree_organization.svg',
            selectedValue: organizationSearchData,
            callbackFunc: (response) => {
                target.value = response.textContent;
                target.id === 'organizationSearch-default-custom'
                    ? target.setAttribute('data-search-value', response.id)
                    : target.setAttribute('data-organization-search', response.id);
            }
        });
    }

    /**
     * Attribute 세부 정보 데이터를 토대로 화면에 출력 (Edit / Register)
     * @param target 표시할 대상 element
     * @param data 세부 데이터
     * @param mode edit|view
     */
    function drawDetails(target, data, sessionInfo, mode) {
        attributeDetailData = JSON.parse(JSON.stringify(data));
        displayMode = mode;
        target.removeAttribute('onclick');
        target.innerHTML = '';
        userInfo = JSON.parse(JSON.stringify(sessionInfo));

        makeCIAttribute(attributeDetailData, target, 'root');
        aliceJs.initDesignedSelectTag();
    }

    exports.attributeTypeList = attributeTypeList;
    exports.init = init;
    exports.makeDetails = makeDetails;
    exports.checkDuplicate = checkDuplicate;
    exports.setDetails = setDetails;
    exports.drawDetails = drawDetails;

    Object.defineProperty(exports, '__esModule', {value: true});
})));
