/**
 * @projectDescription 신청서 Library.
 *
 * @author Woo Da Jung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

import ZComponent from '../form/zComponent.js';
import ZForm from '../form/zForm.js';
import ZGroup from '../form/zGroup.js';
import ZRow from '../form/zRow.js';
import { DOCUMENT, FORM, UNIT } from '../lib/zConstants.js';
import { ZSession } from '../lib/zSession.js';
import { zValidation } from '../lib/zValidation.js';

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
                };
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
    openDocumentPopup(documentId) {
        let popUpUrl = '/documents/' + documentId + '/edit';
        window.open(popUpUrl, 'document_' + documentId, 'width=' + (screen.width - 50) + ', height=' + (screen.height - 150));
    }
    /**
     * 진행 중 문서 초기화
     * @param formDataJson 그리고자 하는 폼에 대한 JSON 데이터
     * @param editable 편집 가능여부
     */
    init(formDataJson, editable) {
        this.domElement = document.getElementById('documentDrawingBoard'); // 문서 엘리먼트
        this.propertiesElement = document.getElementById('documentProperties'); // 우측 문서 정보, 의견, 태그가 표시되는 엘리먼트
        this.data = formDataJson;
        this.formDataJson = this.data.form;
        this.editable = editable;
        this.sortJsonToForm(this.formDataJson); // 정렬
        this.isAssignee = (typeof formDataJson.stakeholders !== 'undefined') ? formDataJson.stakeholders.isAssignee : true; // 문서 열람 권한 체크
        this.makeDocument(this.formDataJson); // 화면 출력

        // 문서 너비에 맞게 문서번호 위치하도록
        const formWidth = this.formDataJson.display.width;
        const documentNumber = document.getElementById('documentNumber');
        if(documentNumber !== null) {
            documentNumber.style.width = formWidth + UNIT.PX;
        }
        // 문서 너비가 1400px이 넘으면 우측 탭을 접는다.
        if (Number(formWidth) > 1400) {
            const toggleTabBtn = document.querySelector('.toggle--tab');
            if (toggleTabBtn !== null) {
                toggleTabBtn.classList.add('off');
            }
        }
    }
    /**
     * JSON 데이터를 폼의 구성요소 3가지(Group, Row, Component)의 출력 순서로 정렬한다. (Recursive)
     * @param data JSON 데이터
     */
    sortJsonToForm(data) {
        if (Object.prototype.hasOwnProperty.call(data, 'group')) { // form
            data.group.sort((a, b) =>
                a.displayDisplayOrder < b.displayDisplayOrder ? -1 : a.displayDisplayOrder > b.displayDisplayOrder ? 1 : 0
            );
            data.group.forEach( (g) => {
                this.sortJsonToForm(g);
            });
        } else if (Object.prototype.hasOwnProperty.call(data, 'row')) { // group
            data.row.sort((a, b) =>
                a.displayDisplayOrder < b.displayDisplayOrder ? -1 : a.displayDisplayOrder > b.displayDisplayOrder ? 1 : 0
            );
            data.row.forEach( (r) => {
                this.sortJsonToForm(r);
            });
        } else { // row
            data.component.sort((a, b) =>
                a.displayDisplayOrder < b.displayDisplayOrder ? -1 : a.displayDisplayOrder > b.displayDisplayOrder ? 1 : 0
            );
        }
    }
    /**
     * 폼의 형태로 Document 생성 (Recursive)
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
                // #12443 진행 중 문서 - 그룹 숨김 처리
                if (!this.isAssignee && g.displayType === FORM.DISPLAY_TYPE.EDITABLE) {
                    g.displayType = FORM.DISPLAY_TYPE.READONLY;
                }
                this.makeDocument(g, this.form, gIndex);
            });
        } else if (Object.prototype.hasOwnProperty.call(data, 'row')) { // group
            const group = this.addObjectByType(FORM.LAYOUT.GROUP, data, parent, index);
            data.row.forEach( (r, rIndex) => {
                r.displayType = group.displayType;
                this.makeDocument(r, group, rIndex);
            });
        } else if (Object.prototype.hasOwnProperty.call(data, 'component')) { // row
            const row = this.addObjectByType(FORM.LAYOUT.ROW, data, parent, index);
            data.component.forEach( (c, cIndex) => {
                c.displayType = row.displayType;
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
        const MaxLengthActionType = ['save', 'progress'];
        const isMaxLengthCheck = MaxLengthActionType.includes(actionType);

        if (isMaxLengthCheck && zValidation.hasDOMElementError(this.domElement)) { return false; }

        // 아래 상태를 가질 경우 유효성 체크를 진행하지 않음 (필수값)
        const validationUncheckActionType = ['save', 'cancel', 'terminate', 'reject', 'withdraw', 'review'];

        const isActionTypeCheck = validationUncheckActionType.includes(actionType);
        if (!isActionTypeCheck && !this.saveValidationCheck()) {
            return false;
        }

        const saveData = {
            'documentId': this.data.documentId,
            'instanceId': this.data.instanceId,
            'tokenId': (zValidation.isDefined(this.data.tokenId) ? this.data.tokenId : ''),
            'isComplete': (actionType !== 'save'),
            'assigneeId': (actionType === 'save') ? ZSession.get('userKey') : '',
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
            body: JSON.stringify(saveData),
            showProgressbar: true
        }).then(rtn => {
            if (rtn === 'true') {
                zAlert.success(i18n.msg(actionMsg),  () => {
                    opener.location.reload();
                    window.close();
                });
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
        sessionStorage.setItem('alice_print', JSON.stringify(this.data.form));
        window.open('/tokens/' + this.data.tokenId + '/print', '_blank');
    }
    /**
     * 문서 닫기
     */
    close() {
        window.close();
    }
    /**
     * 미리보기 뒤로가기
     */
    back() {
        this.documentModal.hide();
    }
}

export const zDocument = new ZDocument();
