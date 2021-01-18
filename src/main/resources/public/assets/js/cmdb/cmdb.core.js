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

    function init(callback) {

    }

    exports.init = init;

    Object.defineProperty(exports, '__esModule', { value: true });
})));
