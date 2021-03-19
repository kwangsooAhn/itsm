/**
 * @projectDescription Form Designer Library
 *
 * @author woodajung
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

// 폼을 구성하기 위한 세부 속성 로드

import Form from '../form/form';

let DATA = {};
/**
 *  세부 속성 데이터 로드 (Properties Panel에 그려지는 용도)
 */
function loadProperties(option) {
    return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();
        const async = (option.async === undefined || option.async === null) ? true : option.async;
        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    resolve(xhr.response);
                } else {
                    reject('Error: ' + xhr.responseText);
                }
            }
        };
        xhr.open(option.method, option.url, async);
        xhr.send(option.params);
    });
}
// 1. 세부 속성 로드
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

// 2. 세션 정보 로드
/*let user = JSON.parse(userInfo);
let departmentName = '';
if (user['department'] !== '') {
    aliceJs.sendXhr({
        method: 'GET',
        async: false,
        url: '/rest/codes/' + user['department'],
        callbackFunc: function(xhr) {
            let result = JSON.parse(xhr.responseText);
            user['departmentName'] = result.codeName;
            departmentName = result.codeName;
        },
        contentType: 'application/json; charset=utf-8'
    });
}
user['departmentName'] = departmentName;
Object.assign(sessionInfo, user);*/

// 3. 커스텀 코드 정보 로드
/*aliceJs.sendXhr({
    method: 'GET',
    url: '/rest/custom-codes?viewType=editor',
    callbackFunc: function(xhr) {
        customCodeList = JSON.parse(xhr.responseText);
    },
    contentType: 'application/json; charset=utf-8'
});*/

// 4. 폼 데이터 로드
aliceJs.sendXhr({
    method: 'GET',
    url: '/assets/js/formDesigner/data_210319.json',
    callbackFunc: function(xhr) {
        console.log(JSON.parse(xhr.responseText));
        // 5. 전달된 데이터의 서버 시간에 따른 날짜 처리
        // 6. 폼 생성
        DATA = JSON.parse(xhr.responseText);
        const form = new Form(DATA);
        console.log(form);
    },
    contentType: 'application/json; charset=utf-8'
});
/*aliceJs.sendXhr({
    method: 'GET',
    url: '/rest/form/' + formId + '/data',
    callbackFunc: function(xhr) {
        let responseObject = JSON.parse(xhr.responseText);
        responseObject.components = aliceForm.reformatCalendarFormat('read',
            responseObject.components);
        editor.data = responseObject;
        drawForm();
    },
    contentType: 'application/json; charset=utf-8'
});*/
