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
     * save workflow.
     */
    function saveWorkflow() {
        const xhr = createXmlHttpRequestObject('POST', '/rest/processes/data');
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    if (xhr.responseText === '1') { //TODO: return 값은 engine 쪽 개발자와 추후 협의 필요!! 현재는 임시로..
                        alert('저장되었습니다.');
                    } else {
                        alert('저장실패');
                    }
                } else if (xhr.status === 400) {
                    console.error('There was an error 400');
                } else {
                    console.debug(xhr);
                    console.error('something else other than 200 was returned. ' + xhr.status);
                }
            }
        };
        xhr.setRequestHeader('Content-Type', 'application/json; charset=utf-8');
        xhr.send(JSON.stringify(wfEditor.data));
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
    function initWorkflowUtil() {
        // add click event listener.
        document.querySelector('#btnSave').addEventListener('click', saveWorkflow);
        document.querySelector('#btnSimulation').addEventListener('click', simulationWorkflow);
        document.querySelector('#btnUndo').addEventListener('click', undoWorkflow);
        document.querySelector('#btnRedo').addEventListener('click', redoWorkflow);
        document.querySelector('#btnImport').addEventListener('click', importWorkflow);
        document.querySelector('#btnExport').addEventListener('click', exportWorkflow);
        document.querySelector('#btnDownload').addEventListener('click', downloadWorkflowImage);
    }

    exports.utils = utils;
    exports.initWorkflowUtil = initWorkflowUtil;
    Object.defineProperty(exports, '__esModule', {value: true});
})));