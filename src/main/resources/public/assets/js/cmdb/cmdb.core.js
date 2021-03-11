/**
 * @projectDescription CMDB Core Library
 *
 * @author phc
 * @version 1.0
 *
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.cmdb = global.cmdb || {})));
}(this, (function (exports) {
    'use strict';

    let sessionInfo = {};

    function init(callback) {

    }

    /**
     * initSession.
     *
     * @param {String} userInfo 사용자 세션 정보
     */
    function initSession(userInfo) {
        let user = JSON.parse(userInfo);
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
        Object.assign(sessionInfo, user);
    }

    exports.init = init;
    exports.initSession = initSession;

    exports.session = sessionInfo;

    Object.defineProperty(exports, '__esModule', { value: true });
})));
