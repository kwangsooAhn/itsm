(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.wfEditor = global.wfEditor || {})));
}(this, (function (exports) {
    'use strict';

    /* test function */
    function testFunc() {
        console.log('test');
    }

    exports.testFunc = testFunc;
    Object.defineProperty(exports, '__esModule', { value: true });
})));