/**
 * @projectDescription Form Designer Library
 *
 * @author woodajung
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */


import Form from '../form/form.js';
import *  as CONST from '../constant.js';

export default class FormDesigner {
    constructor(sessionData) {
        Object.assign(CONST.SESSION, sessionData);
        console.log(CONST.SESSION);

        // TODO: 프로미스
        // TODO: 1.화면 레이아웃 구성
        // TODO: 2. 세부 속성 데이터 로드
        /*aliceJs.sendXhr({
            method: 'GET',
            url: '/assets/js/form/componentProperties.json',
            callbackFunc: function(xhr) {
                Object.assign(componentProperties, JSON.parse(xhr.responseText));
                if (typeof callback === 'function') {
                    callback.apply(null, params);
                }
            },
            contentType: 'application/json; charset=utf-8'
        });*/
        // TODO: 3. 커스텀 코드 정보 로드
        /*aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/custom-codes?viewType=editor',
            callbackFunc: function(xhr) {
                customCodeList = JSON.parse(xhr.responseText);
            },
            contentType: 'application/json; charset=utf-8'
        });*/
        // TODO: 4. 폼 데이터 로드
        /*aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/form/' + formId + '/data',
            callbackFunc: function(xhr) {
                let responseObject = JSON.parse(xhr.responseText);
                // 4. 전달된 데이터의 서버 시간에 따른 날짜 처리
                responseObject.components = aliceForm.reformatCalendarFormat('read',
                    responseObject.components);
                // 5. 폼 생성
                editor.data = responseObject;
                drawForm();
            },
            contentType: 'application/json; charset=utf-8'
        });*/
        // TODO: 가데이터 - 삭제 예정
        aliceJs.sendXhrPromise({
            method: 'GET',
            url: '/assets/js/formDesigner/data_210320.json',
        }).then((data) => {
            const render = document.getElementById('form-panel'); // 폼이 그려질 대상
            this.form = new Form(JSON.parse(data), render);
        });
    }
}

