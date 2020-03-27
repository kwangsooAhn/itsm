(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.AliceProcessEditor = global.AliceProcessEditor || {})));
}(this, (function (exports) {
    'use strict';

    let isEdited = false;
    let observer = new MutationObserver(function(mutations) {
        mutations.forEach(function(mutation) {
            //console.log(mutation);
            isEdited = true;
        });
    });

    let observerConfig = {
        childList: true,
        characterData: true,
        subtree: true
    };

    window.addEventListener('beforeunload', function(event) {
        if (isEdited) {
            event.returnValue = '';
        } else {
            delete event['returnValue'];
        }
    });

    const utils = {
        /**
         * 해당 element 의 중앙 x,y 좌표와 넓이,높이를 리턴한다.
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
        },
        /**
         *
         * @param obj1
         * @param obj2
         * @return {boolean}
         */
        compareJson: function(obj1, obj2) {
            if (!Object.keys(obj2).every(function(key) { return obj1.hasOwnProperty(key); })) {
                return false;
            }
            return Object.keys(obj1).every(function(key) {
                if ((typeof obj1[key] === 'object') && (typeof obj2[key] === 'object')) {
                    return utils.compareJson(obj1[key], obj2[key]);
                } else {
                    return obj1[key] === obj2[key];
                }
            });
        }
    };

    const history = {
        redo_list: [],
        undo_list: [],
        saveHistory: function(data, list, keep_redo) {
            if (data.length === 1 && utils.compareJson(data[0][0], data[0][1])) { // data check
                console.debug('These two json data are the same.');
                console.debug('0: ' + JSON.stringify(data[0][0]));
                console.debug('1: ' + JSON.stringify(data[0][1]));
                return;
            }
            keep_redo = keep_redo || false;
            if (!keep_redo) {
                this.redo_list = [];
            }
            (list || this.undo_list).push(data);
            console.debug(this.undo_list);
        },
        undo: function() {
            if (this.undo_list.length) {
                let restoreData = this.undo_list.pop();
                this.saveHistory(restoreData, this.redo_list, true);
                redrawProcess(restoreData, 'undo');
            }
        },
        redo: function() {
            if (this.redo_list.length) {
                let restoreData = this.redo_list.pop();
                this.saveHistory(restoreData, this.undo_list, true);
                redrawProcess(restoreData, 'redo');
            }
        }
    };

    /**
     * 프로세스를 다시 그리고, 데이터 수정를 수정한다.
     *
     * @param restoreData 데이터
     * @param type 타입(undo, redo)
     */
    function redrawProcess(restoreData, type) {
        const restoreProcess = function(originData, changeData) {
            let links = AliceProcessEditor.elements.links;
            if (!Object.keys(originData).length || !Object.keys(changeData).length) {
                if (!Object.keys(changeData).length) { // delete element
                    AliceProcessEditor.data.elements.forEach(function(elem, i) {
                        if (originData.id === elem.id) {
                            AliceProcessEditor.data.elements.splice(i, 1);
                        }
                    });
                    if (originData.type !== 'arrowConnector') {
                        let element = d3.select(document.getElementById(originData.id));
                        d3.select(element.node().parentNode).remove();
                    } else {
                        for (let i = 0, len = links.length; i < len; i++) {
                            if (links[i].id === originData.id) {
                                AliceProcessEditor.elements.links.splice(i, 1);
                                AliceProcessEditor.setConnectors(true);
                                break;
                            }
                        }
                    }
                } else { // add element
                    if (changeData.type !== 'arrowConnector') {
                        let node = AliceProcessEditor.addElement(changeData);
                        if (node) {
                            node.nodeElement.attr('id', changeData.id);
                            AliceProcessEditor.data.elements.push(changeData);
                        }
                    } else {
                        let link = {
                            id: changeData.id,
                            source: d3.select(document.getElementById(changeData.data['start-id'])),
                            target: d3.select(document.getElementById(changeData.data['end-id']))
                        };
                        if (changeData.display && changeData.display['mid-point']) {
                            link.midPoint = changeData.display['mid-point'];
                        }
                        if (changeData.display && changeData.display['source-point']) {
                            link.sourcePoint = changeData.display['source-point'];
                        }
                        if (changeData.display && changeData.display['target-point']) {
                            link.targetPoint = changeData.display['target-point'];
                        }
                        links.push(link);
                        AliceProcessEditor.data.elements.push(changeData);
                        AliceProcessEditor.setConnectors(true);
                    }
                }
            } else if (typeof changeData.type === 'undefined') { // modify process data
                AliceProcessEditor.data.process = changeData;
                if (originData.name !== changeData.type) { // modify type
                    document.querySelector('.process-name').textContent = changeData.name;
                }
            } else { // modify element
                let element = d3.select(document.getElementById(changeData.id));
                if (originData.type !== changeData.type) { // modify type
                    AliceProcessEditor.changeElementType(element, changeData.type);
                }
                if (originData.data.name !== changeData.data.name) { // modify name
                    AliceProcessEditor.changeTextToElement(changeData.id, changeData.data.name);
                }
                if (changeData.type !== 'arrowConnector') {
                    if (originData.display['position-x'] !== changeData.display['position-x']
                        || originData.display['position-y'] !== changeData.display['position-y']
                        || originData.display.width !== changeData.display.width
                        || originData.display.height !== changeData.display.height) { // modify position or size
                        let node = AliceProcessEditor.addElement(changeData);
                        if (node) {
                            d3.select(element.node().parentNode).remove();
                            node.nodeElement.attr('id', changeData.id);
                            AliceProcessEditor.setConnectors(true);
                        }
                    }
                } else {
                    if (changeData.display && (originData.display['mid-point'] !== changeData.display['mid-point']
                        || originData.display['source-point'] !== changeData.display['source-point']
                        || originData.display['target-point'] !== changeData.display['target-point'])) {
                        // modify connector points.
                        for (let i = 0, len = links.length; i < len; i++) {
                            if (links[i].id === changeData.id) {
                                if (changeData.display && changeData.display['mid-point']) {
                                    links[i].midPoint = changeData.display['mid-point'];
                                } else {
                                    delete links[i].midPoint;
                                }
                                if (changeData.display && changeData.display['source-point']) {
                                    links[i].sourcePoint = changeData.display['source-point'];
                                } else {
                                    delete links[i].sourcePoint;
                                }
                                if (changeData.display && changeData.display['target-point']) {
                                    links[i].targetPoint = changeData.display['target-point'];
                                } else {
                                    delete links[i].targetPoint;
                                }
                                AliceProcessEditor.setConnectors(true);
                                break;
                            }
                        }
                    }
                }
                for (let i = 0, len = AliceProcessEditor.data.elements.length; i < len; i++) {
                    if (AliceProcessEditor.data.elements[i].id === changeData.id) {
                        AliceProcessEditor.data.elements[i] = changeData;
                        break;
                    }
                }
            }
            let selected = document.querySelector('.alice-process-drawing-board').querySelector('.selected');
            if (selected) { // element is selected.
                AliceProcessEditor.setElementMenu(d3.select(selected));
            } else { // otherwise, display process properties.
                AliceProcessEditor.setElementMenu();
            }
        };
        restoreData.forEach(function(data) {
            let originData = data[1],
                changeData = data[0];
            if (type === 'redo') {
                originData = data[0];
                changeData = data[1];
            }
            restoreProcess(originData, changeData);
        });
    }

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
                    isEdited = false;
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
            url: '/rest/processes' + '?saveType=saveas',
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
        d3.select('g.alice-tooltip').remove();
        history.undo();
    }

    /**
     * redo.
     */
    function redoProcess() {
        d3.select('g.alice-tooltip').remove();
        history.redo();
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
        if (document.getElementById('btnSave') !== null) {
            document.getElementById('btnSave').addEventListener('click', saveProcess);
        }
        if (document.getElementById('btnSaveAs') !== null) {
            document.getElementById("btnSaveAs").addEventListener('click', saveAsProcess);
        }
        if (document.getElementById('btnSimulation') !== null) {
            document.getElementById('btnSimulation').addEventListener('click', simulationWorkflow);
        }
        if (document.getElementById('btnUndo') !== null) {
            document.getElementById('btnUndo').addEventListener('click', undoProcess);
        }
        if (document.getElementById('btnRedo') !== null) {
            document.getElementById('btnRedo').addEventListener('click', redoProcess);
        }
        if (document.getElementById('btnImport') !== null) {
            document.getElementById('btnImport').addEventListener('click', importProcess);
        }
        if (document.getElementById('btnExport') !== null) {
            document.getElementById('btnExport').addEventListener('click', exportProcess);
        }
        if (document.getElementById('btnDownload') !== null) {
            document.getElementById('btnDownload').addEventListener('click', downloadProcessImage);
        }
        // start observer
        isEdited = false;
        observer.observe(document.querySelector('.alice-process-drawing-board'), observerConfig);
    }

    exports.utils = utils;
    exports.history = history;
    exports.initUtil = initUtil;
    Object.defineProperty(exports, '__esModule',{value: true});
})));
