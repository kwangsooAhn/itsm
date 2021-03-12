/**
 * @projectDescription CI Component Library
 *
 * @author woodajung
 * @version 1.0
 *
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.CI = global.CI || {})));
}(this, (function (exports) {
    'use strict';

    const ACTION_TYPE_REGISTER = 'register';
    const ACTION_TYPE_DELETE = 'delete';
    const ACTION_TYPE_MODIFY = 'modify';

    // CI 상태
    const CI_STATUS_USE = 'use';
    const CI_STATUS_DELETE = 'delete';

    const CIData = {
        ciId: '',
        ciNo: '',
        ciIcon: '',
        typeId: '',
        typeName: '',
        ciName: '',
        ciDesc: '',
        classId: ''
    };

    let CITag;
    /**
     * 편집 가능 여부에 따라 표시될 데이터 반환
     * @param isEditable
     */
    function getProperty(isEditable) {
        // CI 컴포넌트 편집 가능여부가 true 일때 = 구분, CI 아이콘, CI Type, CI 이름, CI 설명, 편집 아이콘,  row 삭제 아이콘  7개
        // CI 컴포넌트 편집 가능여부가 false 일때 =  CI 아이콘, CI Type , CI 이름, 세부 정보 조회 아이콘, row 삭제 아이콘  5개
        return [
            { id: 'actionType', name: 'form.label.actionType', type: (isEditable ? 'readonly' : 'hidden'), column: '1', class: (isEditable ? 'first': '') },
            { id: 'ciId', name: '', type: 'hidden', column: '0', class: '' },
            { id: 'ciNo', name: '', type: 'hidden', column: '0', class: '' },
            { id: 'ciIcon', name: '', type: 'image', column: '1', class: (isEditable ? '': 'first') },
            { id: 'typeId', name: '', type: 'hidden', column: '0', class: '' },
            { id: 'typeName', name: 'cmdb.ci.label.type', type: 'editable', column: (isEditable ? '3' : '4'), class: '' },
            { id: 'ciName', name: 'cmdb.ci.label.name', type: 'editable', column: (isEditable ? '3' : '4'), class: '' },
            { id: 'ciDesc', name: 'cmdb.ci.label.description', type: 'editable', column: '4', class: '' },
            { id: 'classId', name: '', type: 'hidden', column: '0', class: '' },
            { id: 'editIcon', name: '', type: (isEditable ? 'icon-edit' : 'icon-search'), column: '1', class: '' },
            { id: 'deleteIcon', name: '', type: 'icon-delete', column: '1', class: 'last' }
        ];
    }

    /**
     * 선택된 CI 화면에서 처리
     * @param tag 태그 정보
     */
    function setSelectedCI(componentData) {
        const ciChkElems = document.querySelectorAll('input[type=checkbox]');
        // 총 갯수 표시
        aliceJs.showTotalCount(document.getElementById('ciCount').value, 'ciListCount');
        // 이미 선택된 CI 들은 선택 불가능
        if (componentData.value.length > 0) {
            ciChkElems.forEach(function (chkElem) {
                componentData.value.forEach(function (ci) {
                    if (chkElem.value === ci.ciId) {
                        chkElem.checked = true;
                        chkElem.disabled = true;
                    }
                });
            });
        }
    }

    /**
     * 서버 비동기 통신
     */
    function restSubmit(url, method, data, progressbar, callbackFunc) {
        aliceJs.sendXhr({
            method: method,
            url: url,
            params: JSON.stringify(data),
            callbackFunc: function (xhr) {
                if (typeof callbackFunc === 'function') {
                    callbackFunc(xhr.responseText);
                }
            },
            contentType: 'application/json; charset=utf-8',
            showProgressbar: progressbar
        });
    }

    /**
     * 태그 추가
     * @param target 표시할 대상 element
     * @param tagData 태그 데이터
     */
    function addTags(target, tagData) {
        target.removeAttribute("onclick");
        // 기존 데이터가 존재하면 추가
        const ciTags = tagData.map(function (tag) {
            return tag.tagName;
        });
        CITag.addTags(ciTags);
    }

    /**
     * CI 세부 속성 데이터 저장
     * 
     * @param {String} actionType 타입
     * @param {Object} comp 컴포넌트
     * @param {Function} callbackFunc callback 함수
     */
    function saveCIComponentData(actionType, comp, callbackFunc) {
        if (isValidRequiredAll() && hasErrorClass()) {
            const saveCIData = {};
            Object.keys(CIData).forEach(function(key) {
                const elem = document.getElementById((key === 'ciIcon') ? 'typeIcon': key);
                if (elem !== null) {
                    saveCIData[key] = elem.value;
                }
            });
            saveCIData.ciStatus = CI_STATUS_USE;

            const compIdx = aliceDocument.getComponentIndex(comp.id);
            const componentData = aliceDocument.data.form.components[compIdx];
            if (actionType === ACTION_TYPE_REGISTER) { // 신규 추가
                saveCIData.ciId = workflowUtil.generateUUID();
                saveCIData.actionType = ACTION_TYPE_REGISTER;
                addRow(comp, saveCIData);
                componentData.value.push(saveCIData);
            } else { // 수정
                saveCIData.actionType = ACTION_TYPE_MODIFY;
                const ciId = document.getElementById('ciId').value;
                const ciIdx = componentData.value.findIndex(function (ci) { return ci.ciId === ciId; });
                addRow(comp, saveCIData, ciIdx);
                componentData.value[ciIdx] = saveCIData;
            }

            const saveData = {
                ciId: saveCIData.ciId,
                componentId: comp.id,
                values: { ciAttributes: [], ciTags: [] },
                instanceId: aliceDocument.data.instanceId
            };
            document.querySelectorAll('.attribute').forEach(function(el) {
                let ciAttribute = {};
                const attributeType = el.getAttribute('data-attributeType');
                switch (attributeType) {
                    case 'inputbox':
                        const inputElem = el.querySelector('input');
                        ciAttribute.id = inputElem.id;
                        ciAttribute.value = inputElem.value;
                        break;
                    case 'dropdown':
                        const selectElem = el.querySelector('select');
                        ciAttribute.id = selectElem.id;
                        ciAttribute.value = selectElem.value;
                        break;
                    case 'radio':
                        const radioElem = el.querySelector('input[name="attribute-radio"]:checked');
                        ciAttribute.id = radioElem.id.split('-')[0];
                        ciAttribute.value = radioElem.value;
                        break;
                    case 'checkbox':
                        let checkValues = [];
                        let strValues = "";
                        el.querySelectorAll('input[name="attribute-checkbox"]').forEach(function(chkElem, idx) {
                            if (idx === 0) {
                                ciAttribute.id = chkElem.id.split('-')[0];
                            }
                            if (chkElem.checked) {
                                checkValues.push(chkElem.value);
                            }
                        });
                        if (checkValues.length > 0) {
                            for (let i = 0; i < checkValues.length; i++) {
                                if (strValues === "") {
                                    strValues = checkValues[i];
                                } else {
                                    strValues = strValues + "," + checkValues[i];
                                }
                            }
                        }
                        ciAttribute.value = strValues;
                        break;
                    case 'custom-code':
                        const customElem = el.querySelector('input');
                        ciAttribute.id = customElem.parentNode.id;
                        ciAttribute.value = customElem.getAttribute('custom-data');
                        break;
                    default:
                        break;
                }
                if (Object.keys(ciAttribute).length !== 0) {
                    saveData.values.ciAttributes.push(ciAttribute);
                }
            });

            // 태그 추가
            const tagElems = CITag.getTagElms();
            tagElems.forEach(function (tag) {
                saveData.values.ciTags.push({'id': workflowUtil.generateUUID(), 'value': tag.getAttribute('value')})
            });
            restSubmit('/rest/cmdb/cis/' + saveData.ciId + '/data', 'POST', saveData, false, callbackFunc);
        }
    }

    /**
     * 신규 CI 등록 모달
     */
    function openRegisterModal(e) {
        const ciComponent = aliceJs.clickInsideElement(e, 'component');
        restSubmit('/cmdb/cis/component/new', 'GET', {}, false, function (content) {
            const ciRegisterModal = new modal({
                title: i18n.msg('cmdb.ci.label.register'),
                body: content,
                classes: 'cmdb-ci-register-modal',
                buttons: [{
                    content: i18n.msg('common.btn.register'),
                    classes: "point-fill",
                    bindKey: false,
                    callback: function (modal) {
                        // 세부 속성 저장
                        saveCIComponentData(ACTION_TYPE_REGISTER, ciComponent, function() {
                            modal.hide();
                        });
                    }
                }, {
                    content: i18n.msg('common.btn.cancel'),
                    classes: "default-line",
                    bindKey: false,
                    callback: function (modal) {
                        aliceJs.confirmIcon(i18n.msg('cmdb.ci.msg.deleteInformation'), function () {
                            modal.hide();
                        });
                    }
                }],
                close: {
                    closable: false,
                },
                onCreate: function (modal) {
                    // 스크롤바 추가
                    OverlayScrollbars(document.querySelector('.cmdb-ci-content-edit'), {className: 'scrollbar'});
                    OverlayScrollbars(document.querySelectorAll('textarea'), {
                        className: 'scrollbar',
                        resize: 'vertical',
                        sizeAutoCapable: true,
                        textarea: {
                            dynHeight: false,
                            dynWidth: false,
                            inheritedAttrs: "class"
                        }
                    });

                    // 태그
                    CITag = new Tagify(document.getElementById('ciTags'), {
                        pattern: /^.{0,100}$/,
                        editTags: false,
                        placeholder: i18n.msg('cmdb.ci.msg.tag')
                    });
                }
            });
            ciRegisterModal.show();
        });
    }

    /**
     * 기존 CI 변경 모달
     */
    function openUpdateModal(componentId, ciId, elem) {
        const compIdx = aliceDocument.getComponentIndex(componentId);
        const componentData = aliceDocument.data.form.components[compIdx];
        const ciIdx = componentData.value.findIndex(function (ci) { return ci.ciId === ciId; });

        if (ciIdx === -1) { return false; }
        const ciData = componentData.value[ciIdx];
        // 인스턴스 ID
        const instanceId = aliceDocument.data.instanceId;
        const ciModalTitle = (ciData.actionType === ACTION_TYPE_MODIFY) ? 'cmdb.ci.label.update' : 'cmdb.ci.label.register';
        restSubmit('/cmdb/cis/component/edit?ciId=' + ciId + '&componentId=' + componentId + '&instanceId=' + instanceId, 'POST', ciData, false, function (content) {
            const ciUpdateModal = new modal({
                title: i18n.msg(ciModalTitle),
                body: content,
                classes: 'cmdb-ci-update-modal',
                buttons: [{
                    content: i18n.msg('common.btn.modify'),
                    classes: "point-fill",
                    bindKey: false,
                    callback: function (modal) {
                        // 세부 속성 저장
                        saveCIComponentData(ACTION_TYPE_MODIFY, document.getElementById(componentId), function() {
                            modal.hide();
                        });
                    }
                }, {
                    content: i18n.msg('common.btn.cancel'),
                    classes: "default-line",
                    bindKey: false,
                    callback: function (modal) {
                        aliceJs.confirmIcon(i18n.msg('cmdb.ci.msg.deleteInformation'), function () {
                            modal.hide();
                        });
                    }
                }],
                close: {
                    closable: false,
                },
                onCreate: function (modal) {
                    // 수정된 데이터가 존재할 경우 수정 데이터로 변경
                    document.getElementById('ciAttributes').click();

                    // 타입 변경을 막음
                    if (ciData.actionType === ACTION_TYPE_MODIFY) {
                        document.getElementById('typeSelectBtn').disabled = true;
                    }

                    // 스크롤바 추가
                    OverlayScrollbars(document.querySelector('.cmdb-ci-content-edit'), {className: 'scrollbar'});
                    OverlayScrollbars(document.querySelectorAll('textarea'), {
                        className: 'scrollbar',
                        resize: 'vertical',
                        sizeAutoCapable: true,
                        textarea: {
                            dynHeight: false,
                            dynWidth: false,
                            inheritedAttrs: "class"
                        }
                    });

                    // 태그
                    CITag = new Tagify(document.getElementById('ciTags'), {
                        pattern: /^.{0,100}$/,
                        editTags: false,
                        placeholder: i18n.msg('cmdb.ci.msg.tag')
                    });
                    document.getElementById('ciTags').click();
                }
            });
            ciUpdateModal.show();
        });
    }

    /**
     * 기존 CI 상세 조회 모달
     */
    function openViewModal(componentId, ciId, elem) {
        restSubmit('/cmdb/cis/component/view?ciId=' + ciId, 'GET', {}, false, function (content) {
            const ciViewModal = new modal({
                title: i18n.msg('cmdb.ci.label.view'),
                body: content,
                classes: 'cmdb-ci-view-modal',
                buttons: [{
                    content: i18n.msg('common.btn.close'),
                    classes: "default-line",
                    bindKey: false,
                    callback: function (modal) {
                        modal.hide();
                    }
                }],
                close: {
                    closable: false,
                },
                onCreate: function (modal) {
                    // 세부 데이터가 존재할 경우
                    document.getElementById('ciAttributes').click();

                    // 스크롤바 추가
                    OverlayScrollbars(document.querySelector('.cmdb-ci-content-view'), {className: 'scrollbar'});
                    OverlayScrollbars(document.querySelectorAll('textarea'), {
                        className: 'scrollbar',
                        resize: 'vertical',
                        sizeAutoCapable: true,
                        textarea: {
                            dynHeight: false,
                            dynWidth: false,
                            inheritedAttrs: "class"
                        }
                    });
                    // 태그 추가
                    CITag = new Tagify(document.getElementById('ciTags'), {
                        pattern: /^.{0,100}$/,
                        editTags: false
                    });
                    document.getElementById('ciTags').click();
                }
            });
            ciViewModal.show();
        });
    }

    /**
     * 기존 CI 조회 모달
     */
    function openSelectModal(e) {
        const targetBtn = e.target;
        const ciComponent = aliceJs.clickInsideElement(e, 'component');
        const compIdx = aliceDocument.getComponentIndex(ciComponent.id);
        let componentData = aliceDocument.data.form.components[compIdx];
        const ciRegisterModal = new modal({
            title: i18n.msg('cmdb.ci.label.select'),
            body: `<form id="searchFrm">` +
                      `<input type="text" class="search col-5 mr-2" name="search" id="search" maxlength="100" placeholder="${i18n.msg('cmdb.ci.label.searchPlaceholder')}"/>` +
                      `<input type="text" class="search col-3 mr-2" name="tagSearch" id="tagSearch" maxlength="100" placeholder="${i18n.msg('cmdb.ci.label.tagPlaceholder')}"/>` +
                      `<input type="hidden" name="flag" id="flag" value="component"/>` +
                      `<span id="ciListCount" class="txt-num"></span>` +
                  `</form>` +
                  `<div class="table-set" id="ciList"></div>`,
            classes: 'cmdb-ci-list-modal',
            buttons: [{
                content: i18n.msg('common.btn.check'),
                classes: "point-fill",
                bindKey: false,
                callback: function (modal) {
                    // 체크된 CI 출력
                    let isChecked = false;
                    document.querySelectorAll('input[type=checkbox]:not([disabled])').forEach(function (chkElem) {
                        if (chkElem.checked) {
                            isChecked = true;
                            const actionType = targetBtn.getAttribute('data-actionType');
                            const ci = {
                                actionType: actionType,
                                ciStatus: (actionType === ACTION_TYPE_DELETE) ? CI_STATUS_DELETE : CI_STATUS_USE
                            };
                            const ciTbCells = document.getElementById('ci-' + chkElem.value).children;
                            Array.from(ciTbCells).forEach(function (cell) {
                                if (typeof cell.id !== 'undefined' && cell.id.trim() !== '') {
                                    ci[cell.id] = (cell.id === 'ciId') ? chkElem.value : cell.textContent;
                                }
                            });
                            addRow(ciComponent, ci);
                            componentData.value.push(ci);
                        }
                    });
                    if (isChecked) {
                        modal.hide();
                    } else {
                        aliceJs.alertWarning(i18n.msg('cmdb.ci.msg.selectCI'));
                    }
                }
            }, {
                content: i18n.msg('common.btn.cancel'),
                classes: "default-line",
                bindKey: false,
                callback: function (modal) {
                    modal.hide();
                }
            }],
            close: {
                closable: false,
            },
            onCreate: function (modal) {
                searchCis(componentData);
                document.getElementById('search').onkeyup = function(e) {
                    searchCis(componentData);
                }
                // 태그 조회가 아직 미완성이기 때문에 임시적으로 엔터로 변경한다.
                document.getElementById('tagSearch').onkeyup = function(e) {
                    if (e.key === 'Enter') {
                        e.preventDefault();
                        searchCis(componentData);
                    }
                };
            }
        });
        ciRegisterModal.show();
    }

    /**
     * CI 조회 - [CI 상태가 사용중이고, 문서에 CI가 진행중이지 않는 CI 조회]
     * @param {Object} 선택된 CI
     */
    function searchCis(componentData) {
        let urlParam = aliceJs.serialize(document.getElementById('searchFrm'));
        restSubmit('/cmdb/cis/component/list?' + urlParam, 'GET', {}, false, function (content) {
            document.getElementById('ciList').innerHTML = content;
            // 스크롤바 추가
            OverlayScrollbars(document.querySelector('.list-body'), {className: 'scrollbar'});
            setSelectedCI(componentData);
        });
    }

    /**
     * CI 테이블 Row 추가
     * @param {Object} comp 컴포넌트
     * @param {Object} data 데이터
     * @param {Number} idx 인덱스
     */
    function addRow(comp, data, idx) {
        const ciTb = comp.querySelector('.ci-table-body');
        const row = document.createElement('tr');
        const rowBorderColor = ciTb.getAttribute('data-border');
        let rowTemplate = ``;
        if (typeof data === 'undefined') { // 데이터가 없을 경우
            row.className = 'no-data-found-list';
            rowTemplate = `<td colspan="11" class="on align-center first last" style="border-color: ${rowBorderColor};">` + i18n.msg('common.msg.noData') + `</td>`;
        } else {
            // '데이터가 존재하지 않습니다 ' 문구 삭제
            if (ciTb.querySelector('.no-data-found-list') !== null) {
                ciTb.innerHTML = '';
            }
            const ciHeaderProperty = getProperty(comp.getAttribute('data-isEditable') === 'true');
            const actionType = data.actionType;
            rowTemplate = ciHeaderProperty.map(function (opt, idx) {
                const thWidth = (Number(opt.column) / 12) * 100; // table이 100%를 12 등분하였을때 차지하는 너비의 퍼센트 값
                let tdTemplate = `<td class="align-left ${opt.type === 'hidden' ? '' : 'on'} ${opt.class}" style="border-color: ${rowBorderColor}; width: ${thWidth}%;">`;
                switch (opt.type) {
                    case 'editable':
                        tdTemplate += `${aliceJs.filterXSS(data[opt.id])}`;
                        break;
                    case 'readonly':
                        tdTemplate += `${i18n.msg('cmdb.ci.actionType.' + data.actionType)}`;
                        break;
                    case 'image':
                        tdTemplate += `<img src="/assets/media/images/cmdb/${data[opt.id]}" width="20" height="20"/>`;
                        break;
                    case 'icon-edit': // CI 등록 / 수정
                        if (actionType === ACTION_TYPE_DELETE) {
                            tdTemplate += `<button type="button" onclick="javascript:CI.openViewModal('${comp.id}', '${data.ciId}', this);"><span class="icon icon-search"></span></button>`;
                        } else {
                            tdTemplate += `<button type="button" onclick="javascript:CI.openUpdateModal('${comp.id}', '${data.ciId}', this);"><span class="icon icon-edit"></span></button>`;
                        }
                        break;
                    case 'icon-search': // CI 상세 조회
                        tdTemplate += `<button type="button" onclick="javascript:CI.openViewModal('${comp.id}', '${data.ciId}', this);"><span class="icon icon-search"></span></button>`;
                        break;
                    case 'icon-delete': // Row 삭제
                        tdTemplate += `<button type="button" onclick="javascript:CI.removeRow('${comp.id}', '${data.ciId}', this);">`+
                                          `<span class="icon icon-delete"></span>` +
                                      `</button>`;
                        break;
                    default: // hidden
                        tdTemplate += `<input type="hidden" value="${data[opt.id]}" maxlength="100"/>`;
                        break;
                }
                tdTemplate += `</td>`;
                return tdTemplate;
            }).join('');
        }
        row.insertAdjacentHTML('beforeend', rowTemplate);
        // 인덱스가 존재할 경우, 해당 idx의 element 를 대체한다.
        if (typeof idx !== 'undefined') {
            const updateRow = ciTb.rows[idx];
            updateRow.parentNode.insertBefore(row, updateRow);
            ciTb.deleteRow(idx + 1);
        } else {
            ciTb.appendChild(row);
        }
    }

    /**
     * CI 테이블 Row 삭제
     * @param {Object} elem row 엘리먼트
     */
    function removeRow(componentId, ciId, elem) {
        const ciComponent = document.getElementById(componentId);
        const ciTb = ciComponent.querySelector('.ci-table');
        const compIdx = aliceDocument.getComponentIndex(componentId);
        const componentData = aliceDocument.data.form.components[compIdx];
        const ciIdx = componentData.value.findIndex(function (ci) { return ci.ciId === ciId; });
        if (ciIdx === -1) { return false; }

        const actionType = componentData.value[ciIdx].actionType;
        const alertMsg = (actionType === ACTION_TYPE_REGISTER || actionType === ACTION_TYPE_MODIFY) ? 'cmdb.ci.msg.deleteEditableCI' : 'cmdb.ci.msg.deleteReadableCI';
        aliceJs.confirmIcon(i18n.msg(alertMsg), function () {
            if (actionType === ACTION_TYPE_REGISTER || actionType === ACTION_TYPE_MODIFY) {
                // action 타입이 Register, Modify 일 경우, wf_component_ci_data 테이블에 데이터 삭제
                restSubmit('/rest/cmdb/cis/data?ciId=' + ciId + '&componentId=' + componentId, 'DELETE', {}, true);
            }
            // 화면 데이터 삭제
            componentData.value.splice(ciIdx, 1);
            ciTb.deleteRow(ciIdx + 1);

            // 데이터가 존재하지 않으면 '데이터가 존재하지 않습니다 ' 문구 표시
            if (ciTb.rows.length === 1) {
                addRow(ciComponent);
            }
        });
    }

    /**
     * CI 타입 선택 모달
     * @param {String} typeId 타입 ID
     */
    function openSelectTypeModal(typeId) {
        tree.load({
            view: 'modal',
            dataUrl: '/rest/cmdb/types',
            title: i18n.msg('cmdb.ci.label.type'),
            source: 'ciType',
            target: 'modalTreeList',
            text: 'typeName',
            selectedValue: typeId,
            rootAvailable: false,
            callbackFunc: function(response) {
                if (response.id !== 'root') {
                    // 아이콘과 클래스가 없을 경우, 타입 변경시 기본 값을 추가해준다.
                    restSubmit('/rest/cmdb/types/' + response.id, 'GET', {}, false, function (responseData) {
                        let responseJson = JSON.parse(responseData);
                        document.getElementById('classId').value = responseJson.defaultClassId;
                        document.getElementById('className').value = responseJson.defaultClassName;
                        document.getElementById('typeIcon').value = responseJson.typeIcon;
                        setAttributeDetail(responseJson.defaultClassId);
                    });
                    document.getElementById('typeName').value = response.dataset.name;
                    document.getElementById('typeId').value = response.id;
                } else {
                    aliceJs.alertWarning(i18n.msg('cmdb.type.msg.selectAvailableType'));
                }
            }
        });
    }

    /**
     * 타입 아이콘 선택 모달
     * @param {String} typeIcon 아이콘 경로
     */
    function openSelectIconModal(typeIcon) {
        aliceJs.thumbnail({
            title: i18n.msg('cmdb.type.label.icon'),
            targetId: 'typeIcon',
            type: 'cmdb-icon',
            isThumbnailInfo: false,
            isFilePrefix: false,
            thumbnailDoubleClickUse: true,
            selectedPath: document.querySelector('#typeIcon').value
        });

    }
    
    /**
     * 클래스 선택 모달 
     * 2021-02-09 정희찬 팀장님 요청에 따라 타입선택시 클래스가 변경되는 것 외에 클래스 선택 기능은 막음
     * @param {String} typeIcon 아이콘 경로
     */
    /*function openSelectClassModal(classId) {
        tree.load({
            view: 'modal',
            title: i18n.msg('cmdb.type.label.class'),
            source: 'ciClass',
            target: 'modalTreeList',
            text: 'className',
            selectedValue: classId,
            rootAvailable: false,
            callbackFunc: function(response) {
                if (response.id !== 'root') {
                    document.getElementById('className').value = response.dataset.name;
                    document.getElementById('classId').value = response.id;
                    setAttributeDetail(response.id);
                } else {
                    aliceJs.alertWarning(i18n.msg('cmdb.type.msg.selectAvailableClass'));
                }
            }
        });
    }*/

    /**
     * CLass 상세 속성 속성 표시
     */
    function setAttributeDetail(classId) {
        restSubmit('/rest/cmdb/classes/' + classId + '/attributes', 'GET', {}, false, function (responseData) {
            let responseJson = JSON.parse(responseData);
            attribute.drawEditDetails(document.getElementById('ciAttributes'), responseJson);
        });
    }

    exports.getProperty = getProperty;
    exports.addTags = addTags;
    exports.openRegisterModal = openRegisterModal;
    exports.openUpdateModal = openUpdateModal;
    exports.openSelectModal = openSelectModal;
    exports.openViewModal = openViewModal;
    exports.addRow = addRow;
    exports.removeRow = removeRow;
    exports.openSelectTypeModal = openSelectTypeModal;
    exports.openSelectIconModal = openSelectIconModal;

    Object.defineProperty(exports, '__esModule', { value: true });
})));
