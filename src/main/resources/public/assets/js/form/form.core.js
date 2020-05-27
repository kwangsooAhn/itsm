/**
 * @projectDescription Form Designer Core Library
 *
 * @author woodajung
 * @version 1.0
 */
(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.aliceForm = global.aliceForm || {})));
}(this, (function (exports) {
    'use strict';

    let options = {
        lang: 'en',
        datetimeFormat: 'YYYY-MM-DD HH:mm A',
        dateFormat: 'YYYY-MM-DD',
        hourFormat: 'HH:mm',
        timezone: 'Asia/Seoul',
        sessionInfo: {},
        columnWidth: 8.33  //폼 양식을 12등분 하였을 때, 1개의 너비
    };
    /**
     * Init.
     *
     * @param {String} userInfo 사용자 세션 정보
     */
    function init(userInfo) {
        Object.assign(options.sessionInfo, JSON.parse(userInfo));
        //사용자 정보에 따라 날짜 및 시간 포맷 추출.
        options.timezone = options.sessionInfo.timezone;
        options.lang = options.sessionInfo.lang;
        options.datetimeFormat = options.sessionInfo.timeFormat; // 사용자 날짜 포맷 스트링 원본

        // 임시. (아래 내용 구현 후 .toUpperCase() 삭제예정)
        // 현재 사용자 날짜 포맷은 yyyy-MM-dd와 같은 포맷임.
        // dd처럼 소문자인 경우 요일 정보가 나오게 할 수 있으므로 일단 현재는 모두 대문자 처리
        // 추후 기능 확장을 위해서 사용자 정보에서 현재 포맷은 DD로 넣게 하고
        // 요일 출력 옵션도 추가하려면 그때 소문자 dd를 추가하기.
        let splitDatetimeFormat = options.datetimeFormat.split(' ');
        options.dateFormat = splitDatetimeFormat[0].toUpperCase();
        options.hourFormat = splitDatetimeFormat[1];
        options.datetimeFormat = options.dateFormat + ' ' + options.hourFormat

        if (splitDatetimeFormat.length > 2) {
            options.hourFormat = options.hourFormat + ' ' + splitDatetimeFormat[2];
            options.datetimeFormat = options.datetimeFormat + ' ' + splitDatetimeFormat[2];
        }

        //컴포넌트 기본 속성인 '/assets/js/form/componentAttribute.json' 데이터를 조회하여 저장한다.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/assets/js/form/componentAttribute.json',
            callbackFunc: function(xhr) {
                options.componentAttribute = JSON.parse(xhr.responseText);
            },
            contentType: 'application/json; charset=utf-8'
        });
    }

    exports.init = init;
    exports.options = options;

    Object.defineProperty(exports, '__esModule', { value: true });
})));
