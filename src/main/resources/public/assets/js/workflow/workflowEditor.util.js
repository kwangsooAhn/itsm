(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.wfEditor = global.wfEditor || {})));
}(this, (function (exports) {
    'use strict';

    /**
     * import workflow.
     */
    function importWorkflow() {
        //TODO: 처리로직
    }

    /**
     * export workflow.
     */
    function exportWorkflow() {
        //TODO: 처리로직
    }

    /**
     * download workflow image.
     */
    function downloadWorkflowImage() {
        //TODO: 처리로직
    }

    exports.importWorkflow = importWorkflow;
    exports.exportWorkflow = exportWorkflow;
    exports.downloadWorkflowImage = downloadWorkflowImage;
    Object.defineProperty(exports, '__esModule', {value: true});
})));