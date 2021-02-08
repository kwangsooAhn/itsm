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
    /**
     * 편집 가능 여부에 따라 표시될 데이터 반환
     * @param isEditable
     */
    function getProperty(isEditable) {
        // CI 컴포넌트 편집 가능여부가 true 일때 = 구분, CI 아이콘, CI Type, CI 이름, CI 설명, 편집 아이콘,  row 삭제 아이콘  7개
        // CI 컴포넌트 편집 가능여부가 false 일때 =  CI 아이콘, CI Type , CI 이름, 세부 정보 조회 아이콘, row 삭제 아이콘  5개
        return [
            { id: 'actionType', name: 'form.label.actionType', type: (isEditable ? 'readonly' : 'hidden'), column: '2', class: (isEditable ? 'first': '') },
            { id: 'ciId', name: '', type: 'hidden', column: '0', class: '' },
            { id: 'ciNo', name: '', type: 'hidden', column: '0', class: '' },
            { id: 'ciIcon', name: '', type: 'image', column: '1', class: (isEditable ? '': 'first') },
            { id: 'typeId', name: '', type: 'hidden', column: '0', class: '' },
            { id: 'typeName', name: 'cmdb.ci.label.type', type: 'editable', column: (isEditable ? '2' : '3'), class: '' },
            { id: 'ciName', name: 'cmdb.ci.label.name', type: 'editable', column: (isEditable ? '3' : '4'), class: '' },
            { id: 'ciDesc', name: 'cmdb.ci.label.description', type: 'editable', column: '4', class: '' },
            { id: 'classId', name: '', type: 'hidden', column: '0', class: '' },
            { id: 'editIcon', name: '', type: (isEditable ? 'icon-edit' : 'icon-search'), column: '1', class: '' },
            { id: 'deleteIcon', name: '', type: 'icon-delete', column: '1', class: 'last' }
        ];
    }

    /**
     * 태그 추가
     * @param tag 태그 정보
     */
    function addTag(tag) {
        // TODO: 태그 기능 추가
    }

    /**
     * 태그 삭제
     * @param tag 태그 정보
     */
    function removeTag(tag) {
        // TODO: 태그 기능 추가
    }

    /**
     * 선택된 CI 화면에서 처리
     * @param tag 태그 정보
     */
    function setSelectedCI(componentData) {
        const ciChkElems = document.querySelectorAll('input[type=checkbox]');
        // 총 갯수 표시
        aliceJs.showTotalCount(ciChkElems.length, 'ciListCount');
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
     * 필수값 체크
     */
    function checkRequired() {
        const requiredElem = document.querySelectorAll('[required]');
        for (let i = 0, len = requiredElem.length; i < len; i++) {
            if (requiredElem[i].value.trim() === '') {
                requiredElem[i].classList.add('error');
                aliceJs.alertWarning(i18n.msg('common.msg.requiredEnter'), function() {
                    requiredElem[i].focus();
                });
                return false;
            }
            requiredElem[i].classList.remove('error');
        }
        return true;
    }

    /**
     * CI 세부 속성 데이터 저장
     * 
     * @param {String} actionType 타입
     * @param {Object} comp 컴포넌트
     * @param {Function} callbackFunc callback 함수
     */
    function saveCIComponentData(actionType, comp, callbackFunc) {
        if (checkRequired()) {
            const instanceElements = document.getElementById('instanceId');
            const instanceId = (instanceElements !== null) ? instanceElements.getAttribute('data-id') : '';

            const saveCIData = {};
            Object.keys(CIData).forEach(function(key) {
                const elem = document.getElementById((key === 'ciIcon') ? 'typeIcon': key);
                if (elem !== null) {
                    saveCIData[key] = elem.value;
                }
            });

            const compIdx = aliceDocument.getComponentIndex(comp.id);
                const componentData = aliceDocument.data.form.components[compIdx];
            if (actionType === ACTION_TYPE_REGISTER) {
                saveCIData.ciId = workflowUtil.generateUUID();
                saveCIData.actionType = ACTION_TYPE_REGISTER;
                saveCIData.ciStatus = CI_STATUS_USE;
                addRow(comp, saveCIData);
                componentData.value.push(saveCIData);
            } else {
                saveCIData.actionType = ACTION_TYPE_MODIFY;
                // TODO CI 컴포넌트 - 테이블 데이터 수정
            }

            const saveData = {
                ciId: saveCIData.ciId,
                componentId: comp.id,
                values: { ciAttributes: [], ciTags: [] },
                instanceId: instanceId
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
                        el.querySelectorAll('input[name="attribute-checkbox"]').forEach(function(chkElem, idx) {
                            if (idx === 0) {
                                ciAttribute.id = chkElem.id.split('-')[0];
                            }
                            if (chkElem.checked) {
                                checkValues.push(chkElem.value);
                            }
                        });
                        ciAttribute.value = checkValues;
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
            restSubmit('/rest/cmdb/cis/' + saveData.ciId + '/data', 'POST', saveData, false, callbackFunc);
        }
    }

    /**
     * 신규 CI 등록 모달
     */
    function openRegisterModal(e) {
        const ciComponent = aliceJs.clickInsideElement(e, 'component');
        restSubmit('/cmdb/cis/new', 'GET', {}, false, function (content) {
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
                        saveCIComponentData(e.target.getAttribute('data-actionType'), ciComponent, function() {
                            modal.hide();
                        });
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
                    // TODO: 태그 기능 추가
                    /*new Tagify(document.getElementById('ciTags'), {
                        pattern: /^.{0,100}$/,
                        editTags: false,
                        callbacks: {
                            'add': onAddTag,
                            'remove': onRemoveTag
                        },
                        placeholder: i18n.msg('token.msg.tag')
                    });*/
                }
            });
            ciRegisterModal.show();
        });
    }

    /**
     * 기존 CI 변경 모달
     */
    function openUpdateModal(componentId, ciId, elem) {
        const ciComponent = document.getElementById(componentId);
        console.log(ciComponent);
        restSubmit('/cmdb/cis/new?ciId=' + ciId + '&componentId=' + componentId, 'GET', {}, false, function (content) {
            const ciUpdateModal = new modal({
                title: i18n.msg('cmdb.ci.label.update'),
                body: content,
                classes: 'cmdb-ci-update-modal',
                buttons: [{
                    content: i18n.msg('common.btn.update'),
                    classes: "point-fill",
                    bindKey: false,
                    callback: function (modal) {
                        // 세부 속성 저장
                        //saveCIComponentData(e.target.getAttribute('data-actionType'), ciComponent, function() {
                        //    modal.hide();
                        //});
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
                    // TODO: 태그 기능 추가
                    /*new Tagify(document.getElementById('ciTags'), {
                        pattern: /^.{0,100}$/,
                        editTags: false,
                        callbacks: {
                            'add': onAddTag,
                            'remove': onRemoveTag
                        },
                        placeholder: i18n.msg('token.msg.tag')
                    });*/
                }
            });
            ciUpdateModal.show();
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
                    document.querySelectorAll('input[type=checkbox]:not([disabled])').forEach(function (chkElem) {
                        if (chkElem.checked) {
                            const actionType = targetBtn.getAttribute('data-actionType');
                            const ci = { actionType: actionType, ciStatus: (actionType === ACTION_TYPE_DELETE) ? CI_STATUS_DELETE : CI_STATUS_USE };
                            const ciTbCells = document.getElementById('ci-' + chkElem.value).children;
                            Array.from(ciTbCells).forEach(function(cell) {
                                if (typeof cell.id !== 'undefined' && cell.id.trim() !== '') {
                                    ci[cell.id] = (cell.id === 'ciId') ? chkElem.value : cell.textContent;
                                }
                            });
                            addRow(ciComponent, ci);
                            componentData.value.push(ci);
                        }
                    });
                    modal.hide();
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
                restSubmit('/cmdb/cis/view-pop', 'GET', {}, false, function (content) {
                    document.getElementById('ciList').innerHTML = content;
                    // 스크롤바 추가
                    OverlayScrollbars(document.querySelector('.list-body'), {className: 'scrollbar'});

                    setSelectedCI(componentData);
                });
                // 검색 이벤트 추가
                document.querySelectorAll('#search, #tagSearch').forEach(function (searchElem) {
                    searchElem.addEventListener('keyup', function (e) {
                        let urlParam = aliceJs.serialize(document.getElementById('searchFrm'))
                        restSubmit('/cmdb/cis/view-pop?' + urlParam, 'GET', {}, true, function (content) {
                            document.getElementById('ciList').innerHTML = content;
                            // 스크롤바 추가
                            OverlayScrollbars(document.querySelector('.list-body'), {className: 'scrollbar'});

                            setSelectedCI(componentData);
                        });
                    });
                });
            }
        });
        ciRegisterModal.show();
    }

    /**
     * CI 테이블 Row 추가
     * @param {Object} comp 컴포넌트
     * @param {Object} data 데이터
     */
    function addRow(comp, data) {
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
                        tdTemplate += `${data[opt.id]}`;
                        break;
                    case 'readonly':
                        tdTemplate += `${i18n.msg('cmdb.ci.actionType.' + data.actionType)}`;
                        break;
                    case 'image':
                        if (data[opt.id] !== '') {
                            // TODO: 아이콘 넣기
                            //tdTemplate += `<img src="/assets/media/icons/cmdb/${data[opt.id]}" width="32" height="32"/>`;
                        }
                        break;
                    case 'icon-edit': // CI 등록 / 수정
                        if (actionType === ACTION_TYPE_DELETE) {
                            tdTemplate += `<button type="button"><span class="icon icon-search"></span></button>`;
                        } else {
                            tdTemplate += `<button type="button" onclick="javascript:CI.openUpdateModal('${comp.id}', '${data.ciId}', this);"><span class="icon icon-edit"></span></button>`;
                        }
                        break;
                    case 'icon-search': // CI 상세 조회
                        tdTemplate += `<button type="button"><span class="icon icon-search"></span></button>`;
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
        ciTb.appendChild(row);
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
        if (ciIdx > -1) {
            const actionType = componentData.value[ciIdx].actionType;
            if (actionType === ACTION_TYPE_REGISTER || actionType === ACTION_TYPE_MODIFY) {
                // action 타입이 Register, Modify 일 경우, wf_component_ci_data 테이블에 데이터 삭제
                restSubmit('/rest/cmdb/cis/data?ciId=' + ciId + '&componentId=' + componentId, 'DELETE', {}, true);
            }
            // 화면 데이터 삭제
            componentData.value.splice(ciIdx, 1);
            ciTb.deleteRow(ciIdx + 1);

        }
        // 데이터가 존재하지 않으면 '데이터가 존재하지 않습니다 ' 문구 표시
        if (ciTb.rows.length === 1) {
            addRow(ciComponent);
        }
    }

    /**
     * CI 타입 선택 모달
     * @param {String} typeId 타입 ID
     */
    function openSelectTypeModal(typeId) {
        tree.load({
            view: 'modal',
            title: i18n.msg('cmdb.ci.label.type'),
            source: 'ciType',
            target: 'modalTreeList',
            text: 'typeName',
            selectedValue: typeId,
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
        // TODO: 아이콘 선택 모달
    }
    
    /**
     * 클래스 선택 모달
     * @param {String} typeIcon 아이콘 경로
     */
    function openSelectClassModal(classId) {
        tree.load({
            view: 'modal',
            title: i18n.msg('cmdb.type.label.class'),
            source: 'ciClass',
            target: 'modalTreeList',
            text: 'className',
            selectedValue: classId,
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
    }

    /**
     * CLass 상세 속성 속성 표시
     */
    function setAttributeDetail(classId) {
        /*// 가데이터
        const CIClasses = [
            {"attributes": [
                {"attributeId":"799afe719cd0bfe38797172bb77ae5d8","attributeName":"Licensing policy","attributeText":"라이센스 정책","attributeType":"dropdown","attributeOrder":"1","attributeValue":{"option":[{"text":"FPP","value":"fpp"},{"text":"ESD","value":"esd"},{"text":"OEM","value":"oem"},{"text":"COEM DSP","value":"coem"},{"text":"Volumn","value":"volumn"}]},"value":"oem"},
                {"attributeId":"489a14a0ebdca14b6eb42cf804330145","attributeName":"Licenses","attributeText":"라이센스","attributeType":"inputbox","attributeOrder":"2","attributeValue":{"validate":"","required":"false"},"value":""},
                {"attributeId":"2c9180887759cbaf01775c049af50000","attributeName":"Test#1","attributeText":"라디오버튼","attributeType":"radio","attributeOrder":"3","attributeValue":{"option":[{"text":"여자","value":"female"},{"text":"남자","value":"male"}]},"value":"male"},
                {"attributeId":"072fcb3be4056095a9af82dc6505b1e8","attributeName":"Test#2","attributeText":"커스텀코드","attributeType":"custom-code","attributeOrder":"4","attributeValue":{"customCode":"40288a9170f18a8b0170f1a0be9c0002","default":{"type":"session","value":"department"},"button":"부서선택"},"value":""}
            ]},
            {"attributes": [
                {"attributeId":"df0e88d216ace73e0164f3dbf7ade131","attributeName":"Version_OS_Windows","attributeText":"버전","attributeType":"dropdown","attributeOrder":"1","attributeValue":{"option":[{"text":"윈도우  XP","value":"xp"},{"text":"윈도우 7","value":"7"},{"text":"윈도우 8","value":"8"},{"text":"윈도우 9","value":"9"},{"text":"윈도우 10","value":"10"}]},"value":"10"}
            ]}
       ];*/
        // TODO: 서버 단 상세 속성 조회
        restSubmit('/rest/cmdb/classes/' + classId + '/attributes', 'GET', {}, false, function (responseData) {
            let responseJson = JSON.parse(responseData);
            console.log(responseJson);
            // attribute.drawDetails(document.getElementById('ciAttributes'), CIClasses);
        });
    }

    exports.getProperty = getProperty;
    exports.addTag = addTag;
    exports.removeTag = removeTag;
    exports.openRegisterModal = openRegisterModal;
    exports.openUpdateModal = openUpdateModal;
    exports.openSelectModal = openSelectModal;
    exports.addRow = addRow;
    exports.removeRow = removeRow;
    exports.openSelectTypeModal = openSelectTypeModal;
    exports.openSelectIconModal = openSelectIconModal;
    exports.openSelectClassModal = openSelectClassModal;

    Object.defineProperty(exports, '__esModule', { value: true });
})));
