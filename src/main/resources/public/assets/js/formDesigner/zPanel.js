/**
 * 세부 속성 패널 Class.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import { zValidation } from '../lib/zValidation.js ';
import ZGroupProperty from './property/type/zGroupProperty.js';

export default class ZPanel {
    constructor(editor) {
        this.domElement = document.getElementById('propertyPanel'); // 속성 패널
        this.editor = editor;

        // 유효성이 만족할 경우 true, 만족하지 않을 경우 false
        // 유효성 실패시, 다른 이벤트의 동작을 멈추기 위한 용도이다.
        this.validationStatus = true;
    }
    /**
     * 세부 속성 출력
     */
    on() {
        if (!this.editor.selectedObject) { return false; }
        // 세부 속성 이름 출력
        this.setPropertyName(this.editor.selectedObject.propertyName);

        // 세부 속성 표시
        const property = this.editor.selectedObject.getProperty();
        property.map(propertyObject => {
            const propertyObjectElement = propertyObject.makeProperty(this);

            if (!zValidation.isDefined(propertyObjectElement)) { return false; }

            if (propertyObject instanceof ZGroupProperty) {
                // display, 라벨, 엘리먼트, 유효성 등 그룹에 포함될 경우
                propertyObject.children.map(childPropertyObject => {
                    const childPropertyObjectElement = childPropertyObject.makeProperty(this);
                    propertyObjectElement.addUI(childPropertyObjectElement);
                });
            }
            this.domElement.appendChild(propertyObjectElement.domElement);
            aliceJs.initDesignedSelectTag(propertyObjectElement.domElement);
        });
        return this;
    }
    /**
     * 세부 속성 삭제
     */
    off() {
        this.domElement.innerHTML = '';
        this.setPropertyName('');
        return this;
    }
    /**
     * 세부 속성 이름 표시
     */
    setPropertyName(name) {
        // TODO: Breadcrumb 기능 추가
        document.getElementById('propertyName').textContent = i18n.msg(name);
    }
    /**
     * 세부 속성 변경시 이벤트 핸들러
     * @param e 이벤트객체
     */
    update(key, value) {
        if (!zValidation.isDefined(this.editor.selectedObject)) { return false; }
        // 기존 값과 동일할 경우
        const prevValue = this.editor.selectedObject[key];
        if (prevValue === value) { return false; }
        // 이력 저장
        this.editor.history.save([{
            type: 'change',
            id: this.editor.selectedObject.id,
            method: key,
            from: prevValue,
            to: value
        }]);
        // 변경
        this.editor.selectedObject[key] = value;
        aliceJs.initDesignedSelectTag();
    }
    /**
     * 세부 속성 첫번째 inputbox 포커싱 - 단축키에서 사용
     */
    selectFirstProperty() {
        const selectElements = this.domElement.querySelectorAll('input[type=text]:not([readonly])');
        if (selectElements.length === 0) {
            return false;
        }

        selectElements[0].focus();
    }
}
