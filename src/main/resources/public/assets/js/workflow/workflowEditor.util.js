(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.wfEditor = global.wfEditor || {})));
}(this, (function (exports) {
    'use strict';

    const utils = {
        /**
         * 해당 element의 중앙 x,y 좌표와 넓이,높이를 리턴한다.
         *
         * @param selection
         * @returns {{x: number, width: number, y: number, height: number}}
         */
        getBoundingBoxCenter: function(selection) {
            const element = selection.node();
            const bbox = element.getBBox();
            return {x: bbox.x, y: bbox.y, cx: bbox.x + bbox.width / 2, cy: bbox.y + bbox.height / 2, width: bbox.width, height: bbox.height};
        }
    };

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

    exports.utils = utils;
    exports.importWorkflow = importWorkflow;
    exports.exportWorkflow = exportWorkflow;
    exports.downloadWorkflowImage = downloadWorkflowImage;
    Object.defineProperty(exports, '__esModule', {value: true});
})));