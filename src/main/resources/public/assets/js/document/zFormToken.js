/**
 * 문서함 폼 그리기 Class
 *
 * 폼은 폼 디자이너, 신청서, 문서함등에서 그려지며, 약간씩 차이가 있다.
 * 여기서는 문서함에서 열게 되는 처리할 문서, 진행중 문서, 완료된 문서등을 대상으로 폼을 그린다.
 *
 * @author jung hee chan (hcjung@brainz.co.kr)
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import ZComponent from '../form/zComponent.js';
import ZForm from '../form/zForm.js';
import ZGroup from '../form/zGroup.js';
import ZRow from '../form/zRow.js';
import { DOCUMENT, FORM, SESSION } from '../lib/zConstants.js';
import { zValidation } from '../lib/zValidation.js';

class ZFormToken {
    constructor() {
    }
    /**
     * 클래스 초기화
     *
     * @param domElement Form 그리고자 하는 대상 DOM Element
     * @param formDataJson 그리고자 하는 폼에 대한 JSON 데이터
     */
    init(domElement, formDataJson) {
        this.domElement = domElement;
        this.formDataJson = formDataJson.form;
        this.sortFormObject(this.formDataJson);
        this.makeForm(this.formDataJson);
    }
    /**
     * Form 의 구성요소 3가지(Group, Row, Component)를 출력 순서로 정렬한다.
     *
     * @param formObject JSON 데이터
     */
    sortFormObject(formObject) {
        if (Object.prototype.hasOwnProperty.call(formObject, 'group')) {
            formObject.group.sort((a, b) =>
                a.displayDisplayOrder < b.displayDisplayOrder ? -1 : a.displayDisplayOrder > b.displayDisplayOrder ? 1 : 0
            );
            formObject.group.forEach( (g) => {
                this.sortFormObject(g);
            });
        } else if (Object.prototype.hasOwnProperty.call(formObject, 'row')) {
            formObject.row.sort((a, b) =>
                a.displayDisplayOrder < b.displayDisplayOrder ? -1 : a.displayDisplayOrder > b.displayDisplayOrder ? 1 : 0
            );
            formObject.row.forEach( (r) => {
                this.sortFormObject(r);
            });
        } else {
            formObject.component.sort((a, b) =>
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
    makeForm(data, parent, index) {
        if (Object.prototype.hasOwnProperty.call(data, 'group')) { // form
            this.form = this.addObjectByType(FORM.LAYOUT.FORM, data);
            this.form.parent = parent;
            this.domElement.appendChild(this.form.UIElement.domElement);
            this.form.afterEvent();

            data.group.forEach( (g, gIndex) => {
                this.makeForm(g, this.form, gIndex);
            });
        } else if (Object.prototype.hasOwnProperty.call(data, 'row')) { // group
            const group = this.addObjectByType(FORM.LAYOUT.GROUP, data, parent, index);
            // TODO: #10540 폼 리팩토링 - 신청서 양식 편집시 설계에 따라 바뀔 수 있음
            // row 에 포함된 component displayType 이 모두 hidden 이면 group 도 숨긴다.
            const checkDisplay = data.row.some( (row) => row.component.some((component) =>
                component.displayType !== FORM.DISPLAY_TYPE.HIDDEN));
            if (!checkDisplay) {
                group.UIElement.addUIClass('off');
            }
            data.row.forEach( (r, rIndex) => {
                this.makeForm(r, group, rIndex);
            });
        } else if (Object.prototype.hasOwnProperty.call(data, 'component')) { // row
            const row = this.addObjectByType(FORM.LAYOUT.ROW, data, parent, index);
            // TODO: #10540 폼 리팩토링 - 신청서 양식 편집시 설계에 따라 바뀔 수 있음
            // component displayType 이 모두 hidden 이면 row 도 숨긴다.
            const checkDisplay = data.component.some((component) =>
                component.displayType !== FORM.DISPLAY_TYPE.HIDDEN);
            if (!checkDisplay) {
                row.UIElement.addUIClass('off');
            }
            data.component.forEach( (c, cIndex) => {
                this.makeForm(c, row, cIndex);
            });
        } else { // component
            const component = this.addObjectByType(FORM.LAYOUT.COMPONENT, data, parent, index);
            // TODO: #10540 폼 리팩토링 - 신청서 양식 편집시 설계에 따라 바뀔 수 있음
            // component displayType 이 hidden 이면 component 를 숨긴다.
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
        if (parent && addObject) {
            parent.add(addObject, index);
            addObject.afterEvent();
        }

        return addObject;
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
            'documentId': this.formDataJson.documentId,
            'instanceId': this.formDataJson.instanceId,
            'tokenId': (zValidation.isDefined(this.formDataJson.tokenId) ? this.formDataJson.tokenId : ''),
            'isComplete': (actionType !== 'save'),
            'assigneeId' : (actionType === 'save') ? SESSION['userKey'] : '',
            'assigneeType' : (actionType === 'save') ? DOCUMENT.ASSIGNEE_TYPE : '',
            'action': actionType
        };
        // 컴포넌트 값
        saveData.componentData = this.zForm.getComponentData(this.zForm.form, []);

        //TODO: #10547 폼 리팩토링 - 신청서 저장 - 서버 진행 후 return false 제거
        console.log(saveData);
        return false;

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
    openProcessStatusPopUp() {
        window.open('/process/[[${instanceId}]]/status', 'process_status_[[${instanceId}]]', 'width=1300, height=500');
    }
    /**
     * TODO: 문서 인쇄
     */
    print() {}
    /**
     * 문서 닫기
     */
    close() {
        window.close();
    }
}

export const zFormToken = new ZFormToken();