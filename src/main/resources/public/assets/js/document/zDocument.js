/**
 * @projectDescription 신청서 Library.
 *
 * @author Woo Da Jung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import { FORM, DOCUMENT, SESSION } from '../lib/zConstants.js';
import { zValidation } from '../lib/zValidation.js';
import ZForm from '../form/zForm.js';
import ZGroup from '../form/zGroup.js';
import ZRow from '../form/zRow.js';
import ZComponent from '../form/zComponent.js';
import { zFormButton } from './zFormButton.js';

class ZDocument {
    constructor() {}

    /**
     * 신청서를 표시하는 모달 생성
     */
    initDocumentModal() {
        const documentModalTemplate = document.getElementById('documentModalTemplate');
        this.documentModal = new modal({
            title: '',
            body: documentModalTemplate.content.cloneNode(true),
            classes: 'document-modal-dialog z-document-container',
            buttons:[],
            close: { closable: false },
            onCreate: () => {
                this.domElement = document.getElementById('documentDrawingBoard');

                // history.back 시 신청서 목록으로 이동
                window.history.pushState(null, '', location.href);
                window.onpopstate = function(e) {
                    location.reload();
                }
            },
            onHide: () => {
                this.domElement.innerHTML = '';
            }
        });
    }
    /**
     * 신청서 데이터 조회 후 모달 오픈
     * @param documentId 신청서 아이디
     */
    openDocument(documentId) {
        aliceJs.fetchJson('/rest/documents/' + documentId + '/data', {
            method: 'GET'
        }).then((documentData) => {
            // 정렬 (기준 : displayOrder)
            this.sortJson(documentData.form);
            this.data = documentData;
            this.editable = true; // 신청서는 view, edit 모드가 존재하지 않는다. 권한만 있으면 editable 가능하다.
            document.getElementById('instanceId').value = this.data.instanceId;

            zFormButton.init(documentData, this); // 버튼 초기화
            this.makeDocument(this.data.form); // Form 생성
            this.documentModal.show(); // 모달 표시
            aliceJs.initDesignedSelectTag();
        });
    }
    /**
     * JSON 데이터 정렬 (Recursive)
     * @param data JSON 데이터
     */
    sortJson(data) {
        if (Object.prototype.hasOwnProperty.call(data, 'group')) { // form
            data.group.sort((a, b) =>
                a.displayDisplayOrder < b.displayDisplayOrder ? -1 : a.displayDisplayOrder > b.displayDisplayOrder ? 1 : 0
            );
            data.group.forEach( (g) => {
                this.sortJson(g);
            });
        } else if (Object.prototype.hasOwnProperty.call(data, 'row')) { // group
            data.row.sort((a, b) =>
                a.displayDisplayOrder < b.displayDisplayOrder ? -1 : a.displayDisplayOrder > b.displayDisplayOrder ? 1 : 0
            );
            data.row.forEach( (r) => {
                this.sortJson(r);
            });
        } else { // row
            data.component.sort((a, b) =>
                a.displayDisplayOrder < b.displayDisplayOrder ? -1 : a.displayDisplayOrder > b.displayDisplayOrder ? 1 : 0
            );
        }
    }
    /**
     * FORM 생성 (Recursive)
     * @param data JSON 데이터
     * @param parent 부모 객체
     * @param index 추가될 객체의 index
     */
    makeDocument(data, parent, index) {
        if (Object.prototype.hasOwnProperty.call(data, 'group')) { // form
            this.form = this.addObjectByType(FORM.LAYOUT.FORM, data);
            this.form.parent = parent;
            this.domElement.appendChild(this.form.UIElement.domElement);
            this.form.afterEvent();

            data.group.forEach( (g, gIndex) => {
                this.makeDocument(g, this.form, gIndex);
            });
        } else if (Object.prototype.hasOwnProperty.call(data, 'row')) { // group
            const group = this.addObjectByType(FORM.LAYOUT.GROUP, data, parent, index);
            data.row.forEach( (r, rIndex) => {
                this.makeDocument(r, group, rIndex);
            });
        } else if (Object.prototype.hasOwnProperty.call(data, 'component')) { // row
            const row = this.addObjectByType(FORM.LAYOUT.ROW, data, parent, index);
            data.component.forEach( (c, cIndex) => {
                this.makeDocument(c, row, cIndex);
            });
        } else { // component
            this.addObjectByType(FORM.LAYOUT.COMPONENT, data, parent, index);
        }
    }
    /**
     * form, group, row, component 타입에 따른 객체 추가
     * @param type form, group, row, component 타입
     * @param data JSON 데이터
     * @param parent 부모 객체
     * @param index 추가될 객체의 index
     */
    addObjectByType(type, data, parent, index) {
        let addObject = null; // 추가된 객체

        switch(type) {
            case FORM.LAYOUT.FORM:
                addObject = new ZForm(data);
                break;
            case FORM.LAYOUT.GROUP:
                addObject = new ZGroup(data);
                break;
            case FORM.LAYOUT.ROW:
                addObject = new ZRow(data);
                break;
            case FORM.LAYOUT.COMPONENT:
                addObject = new ZComponent(data);
                break;
            default:
                break;
        }
        if (parent !== undefined) {
            parent.add(addObject, index);
            addObject.afterEvent();
        }

        return addObject;
    }
    /**
     * 신청서 닫기
     */
    close() {
        this.documentModal.hide();
    }
    /**
     * 컴포넌트 value 데이터 조회
     */
    getComponentData(object, array) {
        object.children.forEach((child) => {
            if (child instanceof ZComponent) {
                array.push({ componentId: child.id, value: (typeof child.value === 'object' ? JSON.stringify(child.value) : child.value) });
            } else {
                this.getComponentData(child, array);
            }
        });
        return array;
    }

    /**
     *  저장시 유효성 체크
     */
    saveValidationCheck() {
        if (zValidation.hasDOMElementError(this.domElement)) { return false; }

        let isValid = true;
        const requiredElements =
            document.querySelectorAll('input[data-validation-required="true"],textarea[data-validation-required="true"]');

        for (let i = 0; i < requiredElements.length; i++) {
            if (!zValidation.isRequired(requiredElements[i])) {
                isValid = false;
                break;
            }
        }

        // TODO: 텍스트에디터 필수 체크

        // DR 테이블, CI 테이블 필수인 경우 row를 1개 이상 등록하라는 경고 후 포커싱
        if (isValid) {
            const validationCheckTables = document.querySelectorAll('table[data-validation-required="true"]');
            for (let i = 0; i < validationCheckTables.length; i++) {
                const table = validationCheckTables[i];
                if (table.rows.length === 2 && table.querySelector('.no-data-found-list')) {
                    isValid = false;
                    aliceAlert.alertWarning(i18n.msg('form.msg.failedAllColumnDelete'), function () {
                        table.focus();
                    });
                    break;
                }
            }
        }

        return isValid;
    }

    /**
     * 신청서 저장, 처리, 취소, 회수, 즉시 종료 등 동적 버튼 클릭시 호출됨
     */
    processAction(actionType) {
        // 아래 상태를 가질 경우 유효성 체크를 진행하지 않음
        const validationUncheckActionType = ['save', 'cancel', 'terminate', 'reject', 'withdraw'];
        const isActionTypeCheck = validationUncheckActionType.includes(actionType);
        if (!isActionTypeCheck && !this.saveValidationCheck()) {
            return false;
        }
        const saveData = {
            'documentId': this.data.documentId,
            'instanceId': this.data.instanceId,
            'tokenId': (zValidation.isDefined(this.data.tokenId) ? this.data.tokenId : ''),
            'isComplete': (actionType !== 'save'),
            'assigneeId': (actionType === 'save') ? SESSION['userKey'] : '',
            'assigneeType': (actionType === 'save') ? DOCUMENT.ASSIGNEE_TYPE : '',
            'action': actionType
        };
        // 컴포넌트 값
        saveData.componentData = this.getComponentData(this.form, []);
        console.debug(saveData);

        const actionMsg = (actionType === 'save') ? 'common.msg.save' : 'document.msg.process';
        const url = (saveData.tokenId === '') ? '/rest/tokens/data' : '/rest/tokens/' + saveData.tokenId + '/data';
        aliceJs.fetchText(url, {
            method: (saveData.tokenId === '') ? 'post' : 'put',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(saveData)
        }).then(rtn => {
            if (rtn === 'true') {
                aliceAlert.alertSuccess(i18n.msg(actionMsg),  () => {
                    if (zValidation.isDefined(window.opener)) {
                        opener.location.reload();
                        window.close();
                    } else {
                        this.documentModal.hide();
                    }
                });
            } else {
                aliceAlert.alertDanger(i18n.msg('common.msg.fail'));
            }
        });
    }
}

export const zDocument = new ZDocument();
