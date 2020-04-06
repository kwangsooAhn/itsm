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
        dateFormat: 'YYYY-MM-DD',
        timeFormat: 'hh:mm',
        hourType: '24',
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
        let formatArr = options.sessionInfo.timeFormat.split(' ');
        options.dateFormat = formatArr[0].toUpperCase();
        if (formatArr.length === 3) {
            options.hourType = '12';
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
