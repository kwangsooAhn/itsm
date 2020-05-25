(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.aliceProcessEditor = global.aliceProcessEditor || {})));
}(this, (function (exports) {
    'use strict';

    let savedData = {};
    let isEdited = false;
    window.addEventListener('beforeunload', function(event) {
        if (isEdited) {
            event.returnValue = '';
        } else {
            delete event.returnValue;
        }
    });

    const history = {
        redo_list: [],
        undo_list: [],
        saveHistory: function(data, list, keep_redo) {
            data = data.filter(function(d) { // data check
                return !workflowUtil.compareJson(d[0], d[1]);
            });
            if (data.length === 0) {
                return;
            }
            keep_redo = keep_redo || false;
            if (!keep_redo) {
                this.redo_list = [];
            }
            (list || this.undo_list).push(data);

            // 엘리먼트 정렬
            aliceProcessEditor.data.elements.sort(function(a, b) {
                return a.id < b.id ? -1 : 1;
            });
            savedData.elements.sort(function(a, b) {
                return a.id < b.id ? -1 : 1;
            });

            isEdited = !workflowUtil.compareJson(aliceProcessEditor.data, savedData);
            changeProcessName();
            setProcessInformation();
        },
        undo: function() {
            aliceProcessEditor.removeElementSelected();
            aliceProcessEditor.setElementMenu();
            if (this.undo_list.length) {
                let restoreData = this.undo_list.pop();
                redrawProcess(restoreData, 'undo');
                this.saveHistory(restoreData, this.redo_list, true);
            }
        },
        redo: function() {
            aliceProcessEditor.removeElementSelected();
            aliceProcessEditor.setElementMenu();
            if (this.redo_list.length) {
                let restoreData = this.redo_list.pop();
                redrawProcess(restoreData, 'redo');
                this.saveHistory(restoreData, this.undo_list, true);
            }
        }
    };

    const utils = {
        /**
         * 해당 element 의 중앙 x,y 좌표와 넓이,높이를 리턴 한다.
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
         * 두 개의 좌표 사이의 거리를 구한다.
         *
         * @param a 시작좌표
         * @param b 종료좌표
         * @return {number} 좌표 사이 거리
         */
        calcDist: function(a, b) {
            let dist = Math.sqrt(
                Math.pow(a[0] - b[0], 2) + Math.pow(a[1] - b[1], 2)
            );
            if (dist === 0) {
                dist = 1;
            }
            return dist;
        },
        save: saveProcess,
        saveAs: saveAsProcess,
        undo: undoProcess,
        redo: redoProcess,
        simulation: simulationProcess,
        download: downloadProcessImage,
        focus: focusPropertiesPanel,
        history: history
    };

    /**
     * 프로세스명 변경
     */
    function changeProcessName() {
        document.querySelector('.process-name').textContent = (isEdited ? '*' : '') + aliceProcessEditor.data.process.name;
    }

    /**
     * 프로세스를 다시 그리고, 데이터 수정를 수정한다.
     *
     * @param restoreData 데이터
     * @param type 타입(undo, redo)
     */
    function redrawProcess(restoreData, type) {
        const restoreProcess = function(originData, changeData) {
            let links = aliceProcessEditor.elements.links;
            if (!Object.keys(originData).length || !Object.keys(changeData).length) {
                if (!Object.keys(changeData).length) { // delete element
                    aliceProcessEditor.data.elements.forEach(function(elem, i) {
                        if (originData.id === elem.id) {
                            aliceProcessEditor.data.elements.splice(i, 1);
                        }
                    });
                    if (originData.type !== 'arrowConnector') {
                        let element = d3.select(document.getElementById(originData.id));
                        d3.select(element.node().parentNode).remove();
                    } else {
                        for (let i = 0, len = links.length; i < len; i++) {
                            if (links[i].id === originData.id) {
                                aliceProcessEditor.elements.links.splice(i, 1);
                                break;
                            }
                        }
                    }
                } else { // add element
                    if (changeData.type !== 'arrowConnector') {
                        let node = aliceProcessEditor.addElement(changeData);
                        if (node) {
                            node.nodeElement.attr('id', changeData.id);
                            aliceProcessEditor.data.elements.push(changeData);
                        }
                    } else {
                        let link = {
                            id: changeData.id,
                            sourceId: changeData.data['start-id'],
                            targetId: changeData.data['end-id']
                        };
                        if (changeData.display && changeData.display['mid-point']) {
                            link.midPoint = changeData.display['mid-point'];
                            if (changeData.display['source-point']) {
                                link.sourcePoint = changeData.display['source-point'];
                            }
                            if (changeData.display['target-point']) {
                                link.targetPoint = changeData.display['target-point'];
                            }
                        }
                        if (changeData.display && changeData.display['text-point']) {
                            link.textPoint = changeData.display['text-point'];
                        }
                        links.push(link);
                        aliceProcessEditor.data.elements.push(changeData);
                    }
                }
            } else if (typeof changeData.type === 'undefined') { // modify process data
                aliceProcessEditor.data.process = changeData;
                if (originData.name !== changeData.name) { // modify name
                    changeProcessName();
                }
                aliceProcessEditor.setElementMenu();
            } else { // modify element
                let element = d3.select(document.getElementById(changeData.id));
                if (originData.type !== changeData.type) { // modify type
                    aliceProcessEditor.changeElementType(element, changeData.type);
                }
                if (originData.data.name !== changeData.data.name) { // modify name
                    aliceProcessEditor.changeTextToElement(changeData.id, changeData.data.name);
                }
                if (changeData.type !== 'arrowConnector') {
                    if (originData.display['position-x'] !== changeData.display['position-x']
                        || originData.display['position-y'] !== changeData.display['position-y']
                        || originData.display.width !== changeData.display.width
                        || originData.display.height !== changeData.display.height
                        || originData.data['line-color'] !== changeData.data['line-color']
                        || originData.data['background-color'] !== changeData.data['background-color']) { // modify position or size or group color
                        let node = aliceProcessEditor.addElement(changeData);
                        if (node) {
                            d3.select(element.node().parentNode).remove();
                            node.nodeElement.attr('id', changeData.id);
                        }
                    }
                } else {
                    if (changeData.display && (originData.display['mid-point'] !== changeData.display['mid-point']
                        || originData.display['source-point'] !== changeData.display['source-point']
                        || originData.display['target-point'] !== changeData.display['target-point']
                        || originData.display['text-point'] !== changeData.display['text-point'])) {
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
                                if (changeData.display && changeData.display['text-point']) {
                                    links[i].textPoint = changeData.display['text-point'];
                                } else {
                                    delete links[i].textPoint;
                                }
                                break;
                            }
                        }
                    }
                }
                for (let i = 0, len = aliceProcessEditor.data.elements.length; i < len; i++) {
                    if (aliceProcessEditor.data.elements[i].id === changeData.id) {
                        aliceProcessEditor.data.elements[i] = changeData;
                        break;
                    }
                }
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
        aliceProcessEditor.setConnectors(true);
    }

    /**
     * save process.
     */
    function saveProcess() {
        if (aliceProcessEditor.isView) { return false; }
        console.debug(aliceProcessEditor.data);
        aliceProcessEditor.resetElementPosition();
        aliceJs.sendXhr({
            method: 'PUT',
            url: '/rest/processes/' + aliceProcessEditor.data.process.id + '/data',
            callbackFunc: function(xhr) {
                if (xhr.responseText === 'true') {
                    aliceJs.alert(i18n.get('common.msg.save'));
                    isEdited = false;
                    savedData = JSON.parse(JSON.stringify(aliceProcessEditor.data));
                    changeProcessName();
                } else {
                    aliceJs.alert(i18n.get('common.label.fail'));
                }
            },
            contentType: 'application/json; charset=utf-8',
            params: JSON.stringify(aliceProcessEditor.data)
        });
    }

    /**
     * 다른 이름으로 저장 content.
     *
     * @return {string} content html
     */
    function createDialogContent() {
        const container = document.createElement('div');

        const nameContent = document.createElement('div');
        const nameLabel = document.createElement('label');
        nameLabel.className = 'gmodal-input-label';
        nameLabel.htmlFor = 'process_name';
        nameLabel.textContent = i18n.get('process.label.name');
        nameContent.appendChild(nameLabel);
        const nameTextObject = document.createElement('input');
        nameTextObject.className = 'gmodal-input';
        nameTextObject.id = 'process_name';
        nameTextObject.textContent = aliceProcessEditor.data.process.name;
        nameContent.appendChild(nameTextObject);
        container.appendChild(nameContent);

        const descContent = document.createElement('div');
        const descLabel = document.createElement('label');
        descLabel.className = 'gmodal-input-label';
        descLabel.htmlFor = 'process_description';
        descLabel.textContent = i18n.get('process.label.description');
        descContent.appendChild(descLabel);
        const descTextareaObject = document.createElement('textarea');
        descTextareaObject.className = 'gmodal-input';
        descTextareaObject.rows = 3;
        descTextareaObject.id = 'process_description';
        descContent.appendChild(descTextareaObject);
        container.appendChild(descContent);

        const requiredMsgContent = document.createElement('div');
        requiredMsgContent.className = 'gmodal-required';
        requiredMsgContent.textContent = i18n.get('common.msg.requiredEnter');
        container.appendChild(requiredMsgContent);

        return container.outerHTML;
    }

    /**
     * save as process.
     */
    function saveAsProcess() {
        /**
         * 필수체크.
         *
         * @return {boolean} 체크성공여부
         */
        const checkRequired = function() {
            let nameTextObject = document.getElementById('process_name');
            if (nameTextObject.value.trim() === '') {
                nameTextObject.style.backgroundColor = '#ff000040';
                document.querySelector('.gmodal-required').style.display = 'block';
                return false;
            }
            nameTextObject.style.backgroundColor = '';
            document.querySelector('.gmodal-required').style.display = 'none';
            return true;
        };
        /**
         * 저장처리.
         */
        const saveAs = function() {
            const saveAsProcessData = JSON.parse(JSON.stringify(aliceProcessEditor.data));
            let processData = saveAsProcessData.process;
            processData.name = document.getElementById('process_name').value;
            processData.description = document.getElementById('process_description').value;
            aliceJs.sendXhr({
                method: 'POST',
                url: '/rest/processes' + '?saveType=saveas',
                callbackFunc: function(xhr) {
                    if (xhr.responseText !== '') {
                        aliceJs.alert(i18n.get('common.msg.save'), function() {
                            opener.location.reload();
                            window.name = 'process_' + xhr.responseText + '_edit';
                            location.href = '/processes/' + xhr.responseText + '/edit';
                        });
                    } else {
                        aliceJs.alert(i18n.get('common.label.fail'));
                    }
                },
                contentType: 'application/json; charset=utf-8',
                params: JSON.stringify(saveAsProcessData)
            });
        };

        const saveAsModal = new gModal({
            title: i18n.get('common.btn.saveAs'),
            body: createDialogContent(),
            buttons: [
                {
                    content: i18n.get('common.btn.cancel'),
                    classes: 'gmodal-button-red',
                    bindKey: false, /* no key! */
                    callback: function(modal) {
                        modal.hide();
                    }
                }, {
                    content: i18n.get('common.btn.save'),
                    classes: 'gmodal-button-green',
                    bindKey: false, /* no key! */
                    callback: function(modal) {
                        if (checkRequired()) {
                            saveAs();
                            modal.hide();
                        }
                    }
                }
            ],
            close: {
                closable: false,
            }
        });
        saveAsModal.show();
    }

    /**
     * simulation process.
     */
    function simulationProcess() {
        aliceProcessEditor.resetElementPosition();
        aliceJs.sendXhr({
            method: 'put',
            url: '/rest/processes/' + aliceProcessEditor.data.process.id + '/simulation',
            callbackFunc: function(xhr) {
                if (xhr.responseText === 'true') {
                    aliceJs.alert(i18n.get('process.msg.simulation'));
                } else {
                    aliceJs.alert(i18n.get('common.label.fail'));
                }
            },
            contentType: 'application/json; charset=utf-8',
            params: JSON.stringify(aliceProcessEditor.data)
        });
    }

    /**
     * undo.
     */
    function undoProcess() {
        history.undo();
    }

    /**
     * redo.
     */
    function redoProcess() {
        history.redo();
    }

    /**
     * download process image.
     */
    function downloadProcessImage() {
        //TODO: 처리로직
        console.log('clicked image download button.');
    }

    /**
     * set shortcut.
     */
    function setShortcut() {
        shortcut.init();

        const shortcuts = [
            { 'keys': 'ctrl+s', 'command': 'aliceProcessEditor.utils.save();' },               // 저장
            { 'keys': 'ctrl+shift+s', 'command': 'aliceProcessEditor.utils.saveAs();' },       // 다른 이름으로 저장
            { 'keys': 'ctrl+z', 'command': 'aliceProcessEditor.utils.undo();' },               // 작업 취소
            { 'keys': 'ctrl+shift+z', 'command': 'aliceProcessEditor.utils.redo();' },         // 작업 재실행
            { 'keys': 'ctrl+e', 'command': 'aliceProcessEditor.utils.simulation();' },         // 미리보기(시뮬레이션)
            { 'keys': 'ctrl+d', 'command': 'aliceProcessEditor.utils.download();' },           // 이미지 다운로드
            { 'keys': 'ctrl+x,delete', 'command': 'aliceProcessEditor.deleteElements();' },    // 엘리먼트 삭제
            { 'keys': 'alt+e', 'command': 'aliceProcessEditor.utils.focus();' }                // 세부 속성 편집: 제일 처음으로 이동
        ];

        for (let i = 0; i < shortcuts.length; i++) {
            shortcut.add(shortcuts[i].keys, shortcuts[i].command);
        }
    }

    /**
     * focus properties panel.
     */
    function focusPropertiesPanel() {
        let panel = document.querySelector('.alice-process-properties-panel');
        let items = panel.querySelectorAll('input:not([readonly]), select');
        if (items.length === 0) { return false; }
        items[0].focus();
    }

    /**
     * 오른쪽 하단에 프로세스 정보를 표시 한다.
     */
    function setProcessInformation() {
        let drawingBoard = d3.select(document.querySelector('.alice-process-drawing-board'));
        let content = drawingBoard.html();
        d3.select('.minimap').html(content);
        const minimapSvg = d3.select('.minimap').select('svg');
        minimapSvg.attr('width', 288).attr('height', 178);
        minimapSvg.selectAll('.guides-container, .alice-tooltip, .grid, .tick, .pointer, .drag-line, .painted-connector, defs').remove();
        minimapSvg.selectAll('text').nodes().forEach(function(node) {
            if (node.textContent === '') { d3.select(node).remove(); }
        });
        minimapSvg.selectAll('.group-artifact-container, .element-container, .connector-container').attr('transform', '');
        minimapSvg.selectAll('.selected').classed('selected', false);
        minimapSvg.append('rect')
            .attr('class', 'minimap-guide')
            .attr('x', 0)
            .attr('y', 0)
            .attr('width', drawingBoard.node().offsetWidth)
            .attr('height', drawingBoard.node().offsetHeight);

        const nodeTopArray = [],
              nodeRightArray = [],
              nodeBottomArray = [],
              nodeLeftArray = [];
        const nodes = minimapSvg.selectAll('g.element, g.connector').nodes();
        nodes.forEach(function(node) {
            let nodeBBox = aliceProcessEditor.utils.getBoundingBoxCenter(d3.select(node));
            nodeTopArray.push(nodeBBox.cy - (nodeBBox.height / 2));
            nodeRightArray.push(nodeBBox.cx + (nodeBBox.width / 2));
            nodeBottomArray.push(nodeBBox.cy + (nodeBBox.height / 2));
            nodeLeftArray.push(nodeBBox.cx - (nodeBBox.width / 2));
        });
        let viewBox = [0, 0, drawingBoard.node().offsetWidth, drawingBoard.node().offsetHeight];
        let minimapTranslate = '';
        if (nodes.length > 0) {
            const margin = 100;
            viewBox = [
                d3.min(nodeLeftArray) - margin,
                d3.min(nodeTopArray) - margin,
                Math.abs(d3.max(nodeRightArray) - d3.min(nodeLeftArray)) + (margin * 2),
                Math.abs(d3.max(nodeBottomArray) - d3.min(nodeTopArray)) + (margin * 2)
            ];
            let transform = d3.zoomTransform(drawingBoard.select('.element-container').node());
            minimapTranslate = 'translate(' + -transform.x + ',' + -transform.y + ')';
        }
        minimapSvg.attr('viewBox', viewBox.join(' '));
        minimapSvg.select('.minimap-guide').attr('transform', minimapTranslate);

        const elements = aliceProcessEditor.data.elements;
        let categories = [];
        elements.forEach(function(elem) {
            categories.push(aliceProcessEditor.getElementCategory(elem.type));
        });

        let uniqList =  categories.reduce(function(a, b) {
            if (a.indexOf(b) < 0 ) { a.push(b); }
            return a;
        },[]);
        const countList = [];
        uniqList.forEach(function(item) {
            let count = 0;
            categories.forEach(function(category) {
                if (item === category) {
                    count++;
                }
            });
            countList.push({category: item, count: count});
        });
        let infoContainer = document.querySelector('.alice-process-properties-panel .info');
        infoContainer.querySelectorAll('label').forEach(function(label) {
            label.textContent = '0';
        });
        countList.forEach(function(countInfo) {
            infoContainer.querySelector('#' + countInfo.category + '_count').textContent = countInfo.count;
        });
        infoContainer.querySelector('#element_count').textContent = elements.length;
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
            document.getElementById('btnSaveAs').addEventListener('click', saveAsProcess);
        }
        if (document.getElementById('btnSimulation') !== null) {
            document.getElementById('btnSimulation').addEventListener('click', simulationProcess);
        }
        if (document.getElementById('btnUndo') !== null) {
            document.getElementById('btnUndo').addEventListener('click', undoProcess);
        }
        if (document.getElementById('btnRedo') !== null) {
            document.getElementById('btnRedo').addEventListener('click', redoProcess);
        }
        if (document.getElementById('btnDownload') !== null) {
            document.getElementById('btnDownload').addEventListener('click', downloadProcessImage);
        }

        // start observer
        isEdited = false;
        savedData = JSON.parse(JSON.stringify(aliceProcessEditor.data));
        setShortcut();
        changeProcessName();
        setProcessInformation();
    }

    exports.utils = utils;
    exports.initUtil = initUtil;
    Object.defineProperty(exports, '__esModule',{value: true});
})));
