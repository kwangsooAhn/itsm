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
        },
        /**
         * generate UUID.
         * Public Domain/MIT
         *
         * @returns {string} UUID
         */
        generateUUID: function() {
            let d = new Date().getTime(); //Timestamp
            let d2 = (performance && performance.now && (performance.now() * 1000)) || 0; //Time in microseconds since page-load or 0 if unsupported
            return 'axxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx'.replace(/[x]/g, function(c) {
                let r = Math.random() * 16;//random number between 0 and 16
                if(d > 0){ //Use timestamp until depleted
                    r = (d + r) % 16 | 0;
                    d = Math.floor(d / 16);
                } else { //Use microseconds since page-load if supported
                    r = (d2 + r) % 16 | 0;
                    d2 = Math.floor(d2 / 16);
                }
                return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
            });
        }

    };

    /**
     * Polyfill.
     */
    function polyfill() {
        if (!Math.hypot) {
            Math.hypot = function (x, y) {
                // https://bugzilla.mozilla.org/show_bug.cgi?id=896264#c28
                let max = 0,
                    s = 0;
                for (let i = 0; i < arguments.length; i += 1) {
                    let arg = Math.abs(Number(arguments[i]));
                    if (arg > max) {
                        s *= (max / arg) * (max / arg);
                        max = arg;
                    }
                    s += arg === 0 && max === 0 ? 0 : (arg / max) * (arg / max);
                }
                return max === 1 / 0 ? 1 / 0 : max * Math.sqrt(s);
            };
        }
    }

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
                    alert('There was an error 400');
                } else {
                    console.log(xhr);
                    alert('something else other than 200 was returned. ' + xhr.status);
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
        polyfill(); // Polyfill.
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