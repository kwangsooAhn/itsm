/**
 * @projectDescription 신청서 Library.
 *
 * @author woodajung
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
            classes: 'document-modal-dialog document-container',
            buttons:[],
            close: { closable: false },
            onCreate: () => {
                this.domElement = document.getElementById('documentDrawingBoard');
                this.btnDomElement = document.getElementById('documentButtonArea');
            },
            onHide: () => {
                this.domElement.innerHTML = '';
                this.btnDomElement.innerHTML = '';
            }
        });
    }
    /**
     * 신청서 데이터 조회 후 모달 오픈
     * @param documentId 신청서 아이디
     */
    openDocument(documentId) {
        // TODO: 신청서 데이터 load. > 가데이터 삭제 필요
        aliceJs.fetchJson('/rest/documents/' + documentId + '/data', {
            method: 'GET'
        }).then((documentData) => {
            // 정렬 (기준 : displayOrder)
            this.sortJson(documentData.form);
            this.data = documentData;

            const documentButtonArea =  document.getElementById('documentButtonArea');
            documentButtonArea.innerHTML = '';

            zFormButton.init(documentButtonArea, documentData, this);
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
            // TODO: #10540 폼 리팩토링 - 신청서 양식 편집시 설계에 따라 바뀔 수 있음
            // row 에 포함된 component displayType이 모두 hidden이면 group도 숨긴다.
            const checkDisplay = data.row.some( (row) => row.component.some((component) =>
                component.displayType !== FORM.DISPLAY_TYPE.HIDDEN));
            if (!checkDisplay) {
                group.UIElement.addUIClass('off');
            }
            data.row.forEach( (r, rIndex) => {
                this.makeDocument(r, group, rIndex);
            });
        } else if (Object.prototype.hasOwnProperty.call(data, 'component')) { // row
            const row = this.addObjectByType(FORM.LAYOUT.ROW, data, parent, index);
            // TODO: #10540 폼 리팩토링 - 신청서 양식 편집시 설계에 따라 바뀔 수 있음
            // component displayType 이 모두 hidden이면 row도 숨긴다.
            const checkDisplay = data.component.some((component) =>
                component.displayType !== FORM.DISPLAY_TYPE.HIDDEN);
            if (!checkDisplay) {
                row.UIElement.addUIClass('off');
            }
            data.component.forEach( (c, cIndex) => {
                this.makeDocument(c, row, cIndex);
            });
        } else { // component
            const component = this.addObjectByType(FORM.LAYOUT.COMPONENT, data, parent, index);
            // TODO: #10540 폼 리팩토링 - 신청서 양식 편집시 설계에 따라 바뀔 수 있음
            // component displayType 이 hidden이면 component를 숨긴다.
            if (data.displayType === FORM.DISPLAY_TYPE.HIDDEN) {
                component.UIElement.addUIClass('off');
            }
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
     * 신청서 저장, 처리, 취소, 회수, 즉시 종료 등 동적 버튼 클릭시 호출됨
     */
    processAction(actionType) {
        // 유효성 체크
        let validationUncheckActionType = ['save', 'cancel', 'terminate', 'reject', 'withdraw'];
        if (!validationUncheckActionType.includes(actionType) && zValidation.hasDOMElementError(this.domElement)) {
            return false;
        }
        // TODO: DR 테이블, CI 테이블 필수값 체크
        
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
    /**
     * 신청서 인쇄
     */
    print() {
        const printData  =  this.form.toJson();
        sessionStorage.setItem('alice_print', JSON.stringify(printData));
        window.open('/documents/' + this.data.documentId + '/print', '_blank');
    }
}

export const zDocument = new ZDocument();