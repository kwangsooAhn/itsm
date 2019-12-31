(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.formEditor = global.formEditor || {})));
}(this, (function (exports) {
    'use strict';
    
    function init() {
        console.info('form editor initialization.');
    }
    
    exports.init = init;
    Object.defineProperty(exports, '__esModule', { value: true });
})));