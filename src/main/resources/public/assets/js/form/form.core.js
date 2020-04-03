/**
 * @projectDescription Form Designer Core Library
 *
 * @author woodajung
 * @version 1.0
 */
(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.AliceForm = global.AliceForm || {})));
}(this, (function (exports) {
    'use strict';

    let options = {
        lang: 'en',
        dateFormat: 'YYYY-MM-DD',
        timeFormat: 'hh:mm',
        hourType: '24',
        columnWidth: 8.33,  //폼 양식을 12등분 하였을 때, 1개의 너비
    };
    /**
     * Init.
     *
     * @param {String} userInfo 사용자 세션 정보
     */
    function init(userInfo) {
        Object.assign(options, JSON.parse(userInfo));
        //사용자 정보에 따라 날짜 및 시간 포맷 추출.
        let formatArr = options.timeFormat.split(' ');
        options.dateFormat = formatArr[0].toUpperCase();
        if (formatArr.length === 3) {
            options.hourType = '12';
        }
        console.log(options);
        //컴포넌트 초기화.
        component.init();
    }

    exports.init = init;
    exports.options = options;

    Object.defineProperty(exports, '__esModule', { value: true });
})));
