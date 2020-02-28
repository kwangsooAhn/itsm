(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.AliceProcessEditor = global.AliceProcessEditor || {})));
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
            let x = bbox.x,
                y = bbox.y;
            return {
                x: x,
                y: y,
                cx: x + bbox.width / 2,
                cy: y + bbox.height / 2,
                width: bbox.width,
                height: bbox.height
            };
        }
    };

    /**
     * save workflow.
     */
    function saveWorkflow() {
        console.debug(AliceProcessEditor.data);
        aliceJs.sendXhr({
            method: 'POST',
            url: '/rest/processes/data',
            callbackFunc: function(xhr) {
                if (xhr.responseText === '1') { //TODO: return 값은 engine 쪽 개발자와 추후 협의 필요!! 현재는 임시로..
                    alert(i18n('common.msg.save'));
                } else {
                    alert(i18n('common.label.fail'));
                }
            },
            contentType: 'application/json; charset=utf-8',
            params: JSON.stringify(AliceProcessEditor.data)
        });
    }

    /**
     * simulation workflow.
     */
    function simulationWorkflow() {
        //TODO: 처리로직
        console.log('clicked simulation button.');
    }

    /**
     * undo.
     */
    function undoWorkflow() {
        //TODO: 처리로직
        console.log('clicked undo button.');
    }

    /**
     * redo.
     */
    function redoWorkflow() {
        //TODO: 처리로직
        console.log('clicked redo button.');
    }

    /**
     * import workflow.
     */
    function importWorkflow() {
        //TODO: 처리로직
        console.log('clicked import button.');
    }

    /**
     * export workflow.
     */
    function exportWorkflow() {
        //TODO: 처리로직
        console.log('clicked export button.');
    }

    /**
     * download workflow image.
     */
    function downloadWorkflowImage() {
        //TODO: 처리로직
        console.log('clicked image download button.');
    }

    /**
     * init workflow util.
     */
    function initUtil() {
        // add click event listener.
        document.getElementById('btnSave').addEventListener('click', saveWorkflow);
        document.getElementById('btnSimulation').addEventListener('click', simulationWorkflow);
        document.getElementById('btnUndo').addEventListener('click', undoWorkflow);
        document.getElementById('btnRedo').addEventListener('click', redoWorkflow);
        document.getElementById('btnImport').addEventListener('click', importWorkflow);
        document.getElementById('btnExport').addEventListener('click', exportWorkflow);
        document.getElementById('btnDownload').addEventListener('click', downloadWorkflowImage);
    }

    exports.utils = utils;
    exports.initUtil = initUtil;
    Object.defineProperty(exports, '__esModule', {value: true});
})));