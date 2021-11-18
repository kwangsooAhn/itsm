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
import { DOCUMENT, FORM } from '../lib/zConstants.js';
import { zValidation } from '../lib/zValidation.js';
import { ZSession } from '../lib/zSession.js';
import ZComponent from '../form/zComponent.js';
import ZForm from '../form/zForm.js';
import ZGroup from '../form/zGroup.js';
import ZRow from '../form/zRow.js';

class ZFormToken {
    constructor() {}
    /**
     * 클래스 초기화
     *
     * @param formDataJson 그리고자 하는 폼에 대한 JSON 데이터
     * @param editable 편집 가능여부
     */
    init(formDataJson, editable) {
        this.domElement = document.getElementById('documentDrawingBoard'); // 문서 엘리먼트
        this.propertiesElement = document.getElementById('documentProperties'); // 우측 문서 정보, 의견, 태그가 표시되는 엘리먼트
        this.data = formDataJson;
        this.formDataJson = this.data.form;
        this.editable = editable;
        // 정렬
        this.sortFormObject(this.formDataJson);
        // 화면 출력
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
            data.row.forEach( (r, rIndex) => {
                r.displayType = group.displayType;
                this.makeForm(r, group, rIndex);
            });
        } else if (Object.prototype.hasOwnProperty.call(data, 'component')) { // row
            const row = this.addObjectByType(FORM.LAYOUT.ROW, data, parent, index);
            data.component.forEach( (c, cIndex) => {
                c.displayType = row.displayType;
                this.makeForm(c, row, cIndex);
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
     *  저장시 유효성 체크
     */
    saveValidationCheck() {
        let isValid = true;
        // 1. displayType 이 편집 가능일 경우
        const parentElements =
            document.querySelectorAll('.z-group-tooltip[data-displaytype="document.displayType.editable"]');
        outer : for (let i = 0; i < parentElements.length; i++) {
            // 2. 필수값 검증
            const requiredElements = parentElements[i]
                .querySelectorAll('input[data-validation-required="true"],textarea[data-validation-required="true"]');
            for (let j = 0; j < requiredElements.length; j++) {
                if (!zValidation.isRequired(requiredElements[j])) {
                    isValid = false;
                    break outer;
                }
            }
            // 3. 테이블 필수값 검증
            const requiredTableElements =
                parentElements[i].querySelectorAll('table[data-validation-required="true"]');
            for (let j = 0; j < requiredTableElements.length; j++) {
                const table = requiredTableElements[j];
                // row를 1개 이상 등록하라는 경고 후 포커싱
                if (table.rows.length === 2 && table.querySelector('.no-data-found-list')) {
                    isValid = false;
                    zAlert.warning(i18n.msg('form.msg.failedAllColumnDelete'), function() {
                        table.focus();
                    });
                    break outer;
                }
            }
            // 4. 텍스트에디터 필수 체크
            const requiredTextEditorElements =
                parentElements[i].querySelectorAll('.z-text-editor[data-validation-required="true"]');
            for (let k = 0; k < requiredTextEditorElements.length; k++) {
                // 해당 text editor 내부에 입력된 텍스트가 있는지 확인 (공백 포함)
                if (requiredTextEditorElements[k].querySelector('p').textContent.length === 0) {
                    isValid = false;
                    zAlert.warning(i18n.msg('common.msg.requiredEnter'), function() {
                        requiredTextEditorElements[k].focus();
                    });
                    break outer;
                }
            }

            // 5. 라디오 / 체크박스 필수 체크
            const requiredCheckedElements =
                parentElements[i].querySelectorAll('.z-element[data-validation-required="true"]');
            for (let l = 0; l < requiredCheckedElements.length; l++) {
                // 필수값 체크가 필요한 체크박스 또는 라디오
                const requiredElement = requiredCheckedElements[l].querySelector('input[type=checkbox], input[type=radio]');
                if (!zValidation.isRequired(requiredElement)) {
                    isValid = false;
                    break outer;
                }
            }
        }
        return isValid;
    }

    /**
     * 신청서 저장, 처리, 취소, 회수, 즉시 종료 등 동적 버튼 클릭시 호출됨
     */
    processAction(actionType) {
        // 유효성 체크 (최대 글자 수)
        const MaxLengthActionType = ['save', 'progress']
        const isMaxLengthCheck = MaxLengthActionType.includes(actionType);

        if (isMaxLengthCheck && zValidation.hasDOMElementError(this.domElement)) { return false; }

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
            'assigneeId' : (actionType === 'save') ? ZSession.get('userKey') : '',
            'assigneeType' : (actionType === 'save') ? DOCUMENT.ASSIGNEE_TYPE : '',
            'action': actionType
        };
        // 컴포넌트 값
        saveData.componentData = this.getComponentData(this.form, []);
        console.debug(saveData);

        const actionMsg = (actionType === 'save') ? 'common.msg.save' : 'document.msg.process';
        const url = zValidation.isEmpty(saveData.tokenId) ? '/rest/tokens/data' :
            '/rest/tokens/' + saveData.tokenId + '/data';
        aliceJs.fetchText(url, {
            method: zValidation.isEmpty(saveData.tokenId) ? 'POST' : 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(saveData),
            showProgressbar: true
        }).then(rtn => {
            if (rtn === 'true') {
                zAlert.success(i18n.msg(actionMsg),  () => {
                    if (zValidation.isDefined(window.opener)) {
                        opener.location.reload();
                        window.close();
                    } else {
                        this.documentModal.hide();
                    }
                });
            } else {
                zAlert.danger(i18n.msg('common.msg.fail'));
            }
        });
    }

    /**
     * 프로세스 맵 팝업 호출
     */
    openProcessStatusPopUp() {
        window.open('/process/' + this.data.instanceId + '/status', 'process_status_' + this.data.instanceId,
            'width=1300, height=500');
    }
    /**
     * 서버에서 전달받은 데이터의 날짜 포맷을 변경한다.
     */
    setDateTimeFormat() {
        document.querySelectorAll('.dateFormatFromNow').forEach((element) => {
            element.textContent = dateFormatFromNow(element.textContent);
        });

        document.querySelectorAll('.date-time').forEach((element) => {
            element.textContent = i18n.userDateTime(element.textContent);
        });
    }
    /**
     * 문서 인쇄
     */
    print() {
        const printData  =  this.form.toJson();
        sessionStorage.setItem('alice_print', JSON.stringify(printData));
        window.open('/tokens/' + this.data.tokenId + '/print', '_blank');
    }
    /**
     * 문서 닫기
     */
    close() {
        window.close();
    }
}

export const zFormToken = new ZFormToken();
