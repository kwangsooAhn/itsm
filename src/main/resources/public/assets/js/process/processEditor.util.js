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
     * save process.
     */
    function saveProcess() {
        console.debug(AliceProcessEditor.data);
        AliceProcessEditor.resetElementPosition();
        aliceJs.sendXhr({
            method: 'PUT',
            url: '/rest/processes/' + AliceProcessEditor.data.process.id,
            callbackFunc: function(xhr) {
                if (xhr.responseText === 'true') {
                    aliceJs.alert(i18n.get('common.msg.save'));
                } else {
                    aliceJs.alert(i18n.get('common.label.fail'));
                }
            },
            contentType: 'application/json; charset=utf-8',
            params: JSON.stringify(AliceProcessEditor.data)
        });
    }

    /**
     * save as process.
     */
    function saveAsProcess() {
        aliceJs.sendXhr({
            method: 'POST',
            url: '/rest/processes' + '?saveType=copy',
            callbackFunc: function(xhr) {
                if (xhr.responseText !== '') {
                    aliceJs.alert(i18n.get('common.msg.save'), function() {
                        opener.location.reload();
                        location.href = '/processes/' + xhr.responseText + '/edit';
                    });
                } else {
                    aliceJs.alert(i18n.get('common.label.fail'));
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
    function undoProcess() {
        //TODO: 처리로직
        console.log('clicked undo button.');
    }

    /**
     * redo.
     */
    function redoProcess() {
        //TODO: 처리로직
        console.log('clicked redo button.');
    }

    /**
     * import process.
     */
    function importProcess() {
        //TODO: 처리로직
        console.log('clicked import button.');
    }

    /**
     * export process.
     */
    function exportProcess() {
        //TODO: 처리로직
        console.log('clicked export button.');
    }

    /**
     * download process image.
     */
    function downloadProcessImage() {
        //TODO: 처리로직
        console.log('clicked image download button.');
    }

    /**
     * init workflow util.
     */
    function initUtil() {
        // add click event listener.
        if (document.getElementById('btnSave') != null) {
            document.getElementById('btnSave').addEventListener('click', saveProcess);
        }
        if (document.getElementById('btnSaveAs') != null) {
            document.getElementById("btnSaveAs").addEventListener('click', saveAsProcess);
        }
        if (document.getElementById('btnSimulation') != null) {
            document.getElementById('btnSimulation').addEventListener('click', simulationWorkflow);
        }
        if (document.getElementById('btnUndo') != null) {
            document.getElementById('btnUndo').addEventListener('click', undoProcess);
        }
        if (document.getElementById('btnRedo') != null) {
            document.getElementById('btnRedo').addEventListener('click', redoProcess);
        }
        if (document.getElementById('btnImport') != null) {
            document.getElementById('btnImport').addEventListener('click', importProcess);
        }
        if (document.getElementById('btnExport') != null) {
            document.getElementById('btnExport').addEventListener('click', exportProcess);
        }
        if (document.getElementById('btnDownload') != null) {
            document.getElementById('btnDownload').addEventListener('click', downloadProcessImage);
        }
    }

    exports.utils = utils;
    exports.initUtil = initUtil;
    Object.defineProperty(exports, '__esModule', {value: true});
})));
