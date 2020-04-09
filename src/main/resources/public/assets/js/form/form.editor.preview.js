/**
* @projectDescription Form Desigener Preview Library
*
* @author phc
* @version 1.0
*/
(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.preview = global.preview || {})));
}(this, (function (exports) {
    'use strict';

    function init(data) {
        let editorData = JSON.parse(data);
        console.info('formEditor preview initialization. [FORM ID: ' + editorData.form.id + ']');
        aliceDocument.initContainer('document-container');
        aliceDocument.drawDocument(editorData);
    }

    exports.init = init;
    Object.defineProperty(exports, '__esModule', {value: true});
})));
