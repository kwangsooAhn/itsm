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
     * CI 모달에 표시되는 세부 데이터 호출
     */
    function restSubmit(url, progressbar, callbackFunc) {
        aliceJs.sendXhr({
            method: 'GET',
            url: url,
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
     * 신규 CI 등록 모달
     */
    function openRegisterModal(e) {
        const ciComponent = aliceJs.clickInsideElement(e, 'component');
        /*restSubmit('/cmdb/cis/new', false, function (content) {
            const ciRegisterModal = new modal({
                title: i18n.msg('cmdb.ci.label.register'),
                body: content,
                classes: 'cmdb-ci-register-modal',
                buttons: [{
                    content: i18n.msg('common.btn.register'),
                    classes: "point-fill",
                    bindKey: false,
                    callback: function (modal) {
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
                    // 스크롤바 추가
                    OverlayScrollbars(document.querySelector('.cmdb-ci-content-edit'), {className: 'scrollbar'});
                    // 태그 추가
                    new Tagify(document.getElementById('ciTags'), {
                        pattern: /^.{0,100}$/,
                        editTags: false,
                        callbacks: {
                            'add': onAddTag,
                            'remove': onRemoveTag
                        },
                        placeholder: i18n.msg('token.msg.tag')
                    });
                }
            });
            ciRegisterModal.show();
        });*/
    }

    /**
     * 기존 CI 변경 모달
     */
    function openUpdateModal(e) {
        const ciComponent = aliceJs.clickInsideElement(e, 'component');
        // TODO: 변경 모달 출력
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
                            const ci = { actionType: targetBtn.getAttribute('data-actionType') };
                            const ciTbCells = document.getElementById('ci-' + chkElem.value).children;
                            Array.from(ciTbCells).forEach(function(cell) {
                                if (typeof cell.id !== undefined) {
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
                restSubmit('/cmdb/cis/view-pop', false, function (content) {
                    document.getElementById('ciList').innerHTML = content;
                    // 스크롤바 추가
                    OverlayScrollbars(document.querySelector('.list-body'), {className: 'scrollbar'});

                    setSelectedCI(componentData);
                });
                // 검색 이벤트 추가
                document.querySelectorAll('#search, #tagSearch').forEach(function (searchElem) {
                    searchElem.addEventListener('keyup', function (e) {
                        let urlParam = aliceJs.serialize(document.getElementById('searchFrm'))
                        restSubmit('/cmdb/cis/view-pop?' + urlParam, true, function (content) {
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
                        if (actionType === ACTION_TYPE_REGISTER) {
                            tdTemplate += `<input type="text" value="${data[opt.id]}" maxlength="100"/>`;
                        } else {
                            tdTemplate += `${data[opt.id]}`;
                        }
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
                        tdTemplate += `<button type="button"><span class="icon icon-edit"></span></button>`;
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
            componentData.value.splice(ciIdx, 1);
            ciTb.deleteRow(ciIdx + 1);
        }
        // 데이터가 존재하지 않으면 '데이터가 존재하지 않습니다 ' 문구 표시
        if (ciTb.rows.length === 1) {
            CI.addRow(ciComponent);
        }
    }

    exports.getProperty = getProperty;
    exports.addTag = addTag;
    exports.removeTag = removeTag;
    exports.openRegisterModal = openRegisterModal;
    exports.openUpdateModal = openUpdateModal;
    exports.openSelectModal = openSelectModal;
    exports.addRow = addRow;
    exports.removeRow = removeRow;

    Object.defineProperty(exports, '__esModule', { value: true });
})));
