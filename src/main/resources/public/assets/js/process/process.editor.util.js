(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.aliceProcessEditor = global.aliceProcessEditor || {})));
}(this, (function (exports) {
    'use strict';

    let savedData = {};
    let isEdited = false;
    window.addEventListener('beforeunload', function (event) {
        if (isEdited) {
            event.returnValue = '';
        } else {
            delete event.returnValue;
        }
    });

    const history = {
        redo_list: [],
        undo_list: [],
        saveHistory: function (data, list, keep_redo) {
            data = data.filter(function (d) { // data check
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
            aliceProcessEditor.data.elements.sort(function (a, b) {
                return a.id < b.id ? -1 : 1;
            });
            savedData.elements.sort(function (a, b) {
                return a.id < b.id ? -1 : 1;
            });

            isEdited = !workflowUtil.compareJson(aliceProcessEditor.data, savedData);
            changeProcessName();
            setProcessMinimap();
        },
        undo: function () {
            aliceProcessEditor.removeElementSelected();
            aliceProcessEditor.setElementMenu();
            if (this.undo_list.length) {
                let restoreData = this.undo_list.pop();
                redrawProcess(restoreData, 'undo');
                this.saveHistory(restoreData, this.redo_list, true);
            }
        },
        redo: function () {
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
        getBoundingBoxCenter: function (selection) {
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
        calcDist: function (a, b) {
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
        const restoreProcess = function (originData, changeData) {
            let links = aliceProcessEditor.elements.links;
            if (!Object.keys(originData).length || !Object.keys(changeData).length) {
                if (!Object.keys(changeData).length) { // delete element
                    aliceProcessEditor.data.elements.forEach(function (elem, i) {
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
                if (originData.name !== changeData.name) { // modify name
                    aliceProcessEditor.changeTextToElement(changeData.id, changeData.name);
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
                    for (let i = 0, len = links.length; i < len; i++) {
                        if (links[i].id === changeData.id) {
                            links[i].sourceId = changeData.data['start-id'];
                            links[i].targetId = changeData.data['end-id'];
                            // modify connector points.
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
                for (let i = 0, len = aliceProcessEditor.data.elements.length; i < len; i++) {
                    if (aliceProcessEditor.data.elements[i].id === changeData.id) {
                        aliceProcessEditor.data.elements[i] = changeData;
                        break;
                    }
                }
            }
        };
        restoreData.forEach(function (data) {
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
        if (aliceProcessEditor.isView) {
            return false;
        }
        aliceProcessEditor.resetElementPosition();
        save(function (xhr) {
            if (xhr.responseText === 'true') {
                aliceJs.alert(i18n.msg('common.msg.save'));
                isEdited = false;
                savedData = JSON.parse(JSON.stringify(aliceProcessEditor.data));
                if (savedData.process.status === 'process.status.publish') {
                    uploadProcessFile();
                }
                changeProcessName();
            } else {
                aliceJs.alert(i18n.msg('common.label.fail'));
            }
        });
    }

    /**
     * 자동 저장 (현재는 최초 오픈 시 start event 추가 후 저장 기능을 위해서만 사용중)
     */
    function autoSaveProcess() {
        save(function (xhr) {
            if (xhr.responseText === 'true') {
                isEdited = false;
                savedData = JSON.parse(JSON.stringify(aliceProcessEditor.data));
                changeProcessName();
            }
        });
    }

    /**
     * 프로세스 저장 처리.
     *
     * @param callbackFunc 저장 처리 후 실행될 callback function
     */
    function save(callbackFunc) {
        aliceJs.sendXhr({
            method: 'PUT',
            url: '/rest/processes/' + aliceProcessEditor.data.process.id + '/data',
            contentType: 'application/json; charset=utf-8',
            params: JSON.stringify(aliceProcessEditor.data),
            callbackFunc: callbackFunc
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
        nameLabel.textContent = i18n.msg('process.label.name');
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
        descLabel.textContent = i18n.msg('process.label.description');
        descContent.appendChild(descLabel);
        const descTextareaObject = document.createElement('textarea');
        descTextareaObject.className = 'gmodal-input';
        descTextareaObject.rows = 3;
        descTextareaObject.id = 'process_description';
        descContent.appendChild(descTextareaObject);
        container.appendChild(descContent);

        const requiredMsgContent = document.createElement('div');
        requiredMsgContent.className = 'gmodal-required';
        requiredMsgContent.textContent = i18n.msg('common.msg.requiredEnter');
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
        const checkRequired = function () {
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
        const saveAs = function () {
            const saveAsProcessData = JSON.parse(JSON.stringify(aliceProcessEditor.data));
            let processData = saveAsProcessData.process;
            processData.name = document.getElementById('process_name').value;
            processData.description = document.getElementById('process_description').value;
            aliceJs.sendXhr({
                method: 'POST',
                url: '/rest/processes-admin' + '?saveType=saveas',
                callbackFunc: function (xhr) {
                    if (xhr.responseText !== '') {
                        aliceJs.alert(i18n.msg('common.msg.save'), function () {
                            opener.location.reload();
                            window.name = 'process_' + xhr.responseText + '_edit';
                            location.href = '/processes/' + xhr.responseText + '/edit';
                        });
                    } else {
                        aliceJs.alert(i18n.msg('common.label.fail'));
                    }
                },
                contentType: 'application/json; charset=utf-8',
                params: JSON.stringify(saveAsProcessData)
            });
        };

        /**
         * 다른 이름으로 저장하기 모달 저장 CallBack.
         */
        const saveAsCallBack = function () {
            if (checkRequired()) {
                saveAs();
            }
        }

        /**
         * 다른 이름으로 저장하기 모달.
         */
        const saveAsModal = aliceJs.confirm(createDialogContent(), () => saveAsCallBack(), () => '');
    }

    /**
     * 시뮬레이션 점검 동작
     */
    function simulationProcess() {
        aliceJs.sendXhr({
            method: 'put',
            url: '/rest/processes/' + aliceProcessEditor.data.process.id + '/simulation',
            callbackFunc: function (xhr) {
                if (document.querySelectorAll('.simulation-report .contents .details div').length > 0) {
                    document.querySelectorAll('.simulation-report .contents .details div').forEach((element) => element.parentElement.removeChild(element));
                    document.querySelector('.simulation-report .contents .result').classList.remove('success', 'failed');
                }
                const response = JSON.parse(xhr.responseText);

                // 전체 결과
                let mainResult = '';
                let mainResultClassName = '';
                if (response.success === true) {
                    mainResult = i18n.msg('common.label.success');
                    mainResultClassName = 'success';
                } else {
                    mainResult = i18n.msg('common.label.fail');
                    mainResultClassName = 'failed';
                }
                document.querySelector('.simulation-report .contents .result').classList.add(mainResultClassName);
                document.querySelector('.simulation-report .contents .result').textContent = mainResult;

                // 세부 결과
                for (let i = 0; i < response.simulationReport.length; i++) {
                    const report = response.simulationReport[i];

                    let successOrFailure = '';
                    let order = '[' + [i + 1] + '/' + response.simulationReport.length + ']';
                    let elementDescription = '[' + report.elementType + (report.elementName !== '' ? ':' + report.elementName : '') + ']';
                    let message = '';
                    let reportDetailClassName = '';
                    if (report.success === true) {
                        successOrFailure = '[' + i18n.msg('common.label.success') + ']';
                        document.getElementById(report.elementId).classList.remove('selected', 'error');
                    } else if (report.success === false) {
                        successOrFailure = '[' + i18n.msg('common.label.fail') + ']';
                        message = '[' + report.failedMessage + ']';
                        reportDetailClassName = 'failed';
                        document.getElementById(report.elementId).classList.add('selected', 'error');

                    }
                    const count = document.createElement('span');
                    count.className = 'details-number-of';
                    count.textContent = order;
                    const result = document.createElement('span');
                    result.className = 'details-result';
                    result.textContent = successOrFailure
                    const elementInfo = document.createElement('span');
                    elementInfo.className = 'details-element-info';
                    elementInfo.textContent = elementDescription;
                    const failedMessage = document.createElement('span');
                    failedMessage.className = 'details-failed-message';
                    failedMessage.textContent = message;

                    const reportDetails = document.createElement('div');
                    reportDetails.className = reportDetailClassName;
                    reportDetails.appendChild(count);
                    reportDetails.appendChild(result);
                    reportDetails.appendChild(elementInfo);
                    reportDetails.appendChild(failedMessage);

                    document.querySelector('.simulation-report .contents .details').appendChild(reportDetails);
                }

                if (document.querySelector('.simulation-report').classList.contains('closed')) {
                    document.querySelector('.simulation-report-icon').click();
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
        const viewBox = getSvgViewBox();
        let svgNode = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
        const embedImages = loadProcessImage(viewBox, svgNode);
        embedImages.then(() => {
            let svgString = getSVGString(svgNode);
            let canvas = document.createElement('canvas');
            canvas.width = viewBox[2];
            canvas.height = viewBox[3];
            let context = canvas.getContext('2d');
            let image = new Image();
            image.onload = function () {
                context.clearRect(0, 0, canvas.width, canvas.height);
                context.drawImage(image, 0, 0, canvas.width, canvas.height);

                let canvasData = canvas.toDataURL('image/png');
                const a = document.createElement('a');
                a.download = aliceProcessEditor.data.process.name + '_' + aliceProcessEditor.data.process.id + '.png';
                a.href = canvasData;
                a.click();
                svgNode = null;
            };
            image.src = 'data:image/svg+xml;base64,' + btoa(unescape(encodeURIComponent(svgString)));
        });
    }

    /**
     * 발행 시 프로세스 이미지 데이터를 서버로 업로드한다.
     */
    function uploadProcessFile() {
        const viewBox = getSvgViewBox();
        let svgNode = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
        const embedImages = loadProcessImage(viewBox, svgNode);
        embedImages.then(() => {
            let svgString = getSVGString(svgNode);
            let xmlString = createProcessXMLString(viewBox, svgString);
            let formData = new FormData();
            let blob = new Blob(['<?xml version="1.0" encoding="UTF-8"?>' + xmlString], {type: 'text/plain'});
            formData.append('file', blob, aliceProcessEditor.data.process.id + '.xml');

            let xhr = new XMLHttpRequest();
            xhr.open('POST', '/fileupload?target=process');
            xhr.onerror = function () {
                console.error('Process file upload failed!');
            };
            xhr.send(formData);
            svgNode = null;
        });
    }

    /**
     * XML 형식의 문자열을 만들어 반환 한다.(프로세스 상태 표시용)
     *
     * @param viewBox 프로세스이미지 정보
     * @param svgString 프로세스이미지 String
     * @return XML String
     */
    function createProcessXMLString(viewBox, svgString) {
        const xmlDoc = document.implementation.createDocument('', '', null);
        let processNode = xmlDoc.createElement('process');
        processNode.setAttribute('id', aliceProcessEditor.data.process.id);
        processNode.setAttribute('name', aliceProcessEditor.data.process.name);
        processNode.setAttribute('description', aliceProcessEditor.data.process.description);

        let imageNode = xmlDoc.createElement('image');
        imageNode.setAttribute('left', viewBox[0]);
        imageNode.setAttribute('top', viewBox[1]);
        imageNode.setAttribute('width', viewBox[2]);
        imageNode.setAttribute('height', viewBox[3]);
        let cdata = xmlDoc.createCDATASection('data:image/svg+xml;base64,' + btoa(unescape(encodeURIComponent(svgString))));
        imageNode.appendChild(cdata);
        processNode.appendChild(imageNode);

        let displayNode = xmlDoc.createElement('display');
        const excludeElementTypes = ['annotation', 'group', 'arrowConnector'];
        const elements = aliceProcessEditor.data.elements.filter(elem => excludeElementTypes.indexOf(elem.type) === -1);
        elements.forEach(function (element) {
            let elementNode = xmlDoc.createElement('element');
            elementNode.setAttribute('id', element.id);
            elementNode.setAttribute('type', element.type);
            let keys = Object.keys(element.display);
            keys.forEach(function (key) {
                elementNode.setAttribute(key, element.display[key]);
            });
            displayNode.appendChild(elementNode);
        });
        processNode.appendChild(displayNode);
        xmlDoc.appendChild(processNode);

        let serializer = new XMLSerializer();
        return serializer.serializeToString(xmlDoc);
    }

    /**
     * 프포세스 이미지를 추가하여 리턴한다.
     * (image 태그의 url 호출은 data 데이터로 변환한다.)
     *
     * @param viewBox
     * @param svgNode
     * @return {Promise<unknown[]>}
     */
    function loadProcessImage(viewBox, svgNode) {
        let svg = d3.select(svgNode).html(d3.select('.alice-process-drawing-board > svg').html());
        svg.attr('width', viewBox[2])
            .attr('height', viewBox[3])
            .attr('viewBox', viewBox.join(' '))
            .classed('alice-process-drawing-board', true);

        svg.selectAll('.guides-container, .alice-tooltip, .grid, .tick, .pointer, .drag-line, .painted-connector').remove();
        svg.selectAll('.group-artifact-container, .element-container, .connector-container').attr('transform', '');
        svg.selectAll('.node.selected').nodes().forEach(function (node) {
            aliceProcessEditor.setDeselectedElement(d3.select(node));
        });
        svg.selectAll('.selected').classed('selected', false);
        svg.selectAll('.reject-element').classed('reject-element', false);

        function asyncImageLoader(url) {
            return new Promise((resolve, reject) => {
                let image = new Image();
                image.src = url;
                image.onload = () => resolve(image);
                image.onerror = () => reject(console.error('could not load image!'));
            });
        }

        return Promise.all(Array.from(svgNode.querySelectorAll('image')).map(function (image) {
            return new Promise(resolve => {
                let url = image.getAttributeNS('http://www.w3.org/1999/xlink', 'href');
                let promise = asyncImageLoader(url);
                promise.then(img => {
                    let canvas = document.createElement('canvas'),
                        ctx = canvas.getContext('2d');
                    canvas.height = img.naturalHeight;
                    canvas.width = img.naturalWidth;
                    ctx.drawImage(img, 0, 0);
                    let dataURL = canvas.toDataURL('image/png');
                    image.setAttributeNS('http://www.w3.org/1999/xlink', 'href', dataURL);
                    resolve(image);
                });
            });
        }));
    }

    /**
     * svg 이미지를 string 으로 변환하여 반환한다.
     *
     * @param svgNode 다운로드할 SVG Node
     * @return {string} 다운로드 svg string data
     */
    function getSVGString(svgNode) {
        svgNode.setAttribute('xlink', 'http://www.w3.org/1999/xlink');
        let cssStyleText = '';
        for (let i = 0; i < document.styleSheets.length; i++) {
            let s = document.styleSheets[i];
            try {
                if (!s.cssRules) {
                    continue;
                }
            } catch (e) {
                if (e.name !== 'SecurityError') {
                    throw e;
                } // for Firefox
                continue;
            }

            let cssRules = s.cssRules;
            for (let r = 0; r < cssRules.length; r++) {
                if (cssRules[r].selectorText &&
                    cssRules[r].selectorText.startsWith('svg')) {
                    cssStyleText += cssRules[r].cssText;
                }
            }
        }
        let styleElement = document.createElement('style');
        styleElement.setAttribute('type', 'text/css');
        styleElement.innerHTML = cssStyleText;
        let refNode = svgNode.hasChildNodes() ? svgNode.children[0] : null;
        svgNode.insertBefore(styleElement, refNode);

        let serializer = new XMLSerializer();
        let svgString = serializer.serializeToString(svgNode);
        svgString = svgString.replace(/(\w+)?:?xlink=/g, 'xmlns:xlink='); // Fix root xlink without namespace
        svgString = svgString.replace(/NS\d+:href/g, 'xlink:href'); // Safari NS namespace fix

        return svgString;
    }

    /**
     * set shortcut.
     */
    function setShortcut() {
        shortcut.init();

        const shortcuts = [
            {'keys': 'ctrl+s', 'command': 'aliceProcessEditor.utils.save();', 'force': true},             // 저장
            {'keys': 'ctrl+shift+s', 'command': 'aliceProcessEditor.utils.saveAs();', 'force': true},     // 다른 이름으로 저장
            {'keys': 'ctrl+z', 'command': 'aliceProcessEditor.utils.undo();', 'force': false},            // 작업 취소
            {'keys': 'ctrl+shift+z', 'command': 'aliceProcessEditor.utils.redo();', 'force': false},      // 작업 재실행
            {'keys': 'ctrl+e', 'command': 'aliceProcessEditor.utils.simulation();', 'force': false},      // 미리보기(시뮬레이션)
            {'keys': 'ctrl+d', 'command': 'aliceProcessEditor.utils.download();', 'force': false},        // 이미지 다운로드
            {'keys': 'ctrl+x,delete', 'command': 'aliceProcessEditor.deleteElements();', 'force': false}, // 엘리먼트 삭제
            {'keys': 'alt+e', 'command': 'aliceProcessEditor.utils.focus();', 'force': false}             // 세부 속성 편집: 제일 처음으로 이동
        ];

        for (let i = 0; i < shortcuts.length; i++) {
            shortcut.add(shortcuts[i].keys, shortcuts[i].command, shortcuts[i].force);
        }
    }

    /**
     * focus properties panel.
     */
    function focusPropertiesPanel() {
        let panel = document.querySelector('.alice-process-properties-panel');
        let items = panel.querySelectorAll('input:not([readonly]), select');
        if (items.length === 0) {
            return false;
        }
        items[0].focus();
    }

    /**
     * 미니맵을 표시한다.
     */
    function setProcessMinimap() {
        const drawingboardContainer = document.querySelector('.alice-process-drawing-board');
        let drawingBoard = d3.select(drawingboardContainer).select('svg');
        let content = drawingBoard.html();
        const minimapSvg = d3.select('div.minimap').select('svg');
        minimapSvg.html(content);
        minimapSvg.attr('width', 160).attr('height', 110);
        minimapSvg.selectAll('.guides-container, .alice-tooltip, .grid, .tick, .pointer, .drag-line, .painted-connector, defs').remove();
        minimapSvg.selectAll('text').nodes().forEach(function (node) {
            if (node.textContent === '') {
                d3.select(node).remove();
            }
        });
        minimapSvg.selectAll('.group-artifact-container, .element-container, .connector-container').attr('transform', '');
        minimapSvg.selectAll('.node.selected').nodes().forEach(function (node) {
            aliceProcessEditor.setDeselectedElement(d3.select(node));
        });
        minimapSvg.selectAll('.selected').classed('selected', false);
        minimapSvg.selectAll('.reject-element').classed('reject-element', false);
        minimapSvg.append('rect')
            .attr('class', 'minimap-guide')
            .attr('x', 0)
            .attr('y', 0)
            .attr('width', drawingboardContainer.offsetWidth)
            .attr('height', drawingboardContainer.offsetHeight);

        let minimapTranslate = '';
        if (minimapSvg.selectAll('g.element, g.connector').nodes().length > 0) {
            let transform = d3.zoomTransform(drawingBoard.select('.element-container').node());
            minimapTranslate = 'translate(' + -transform.x + ',' + -transform.y + ')';
        }
        minimapSvg.attr('viewBox', getSvgViewBox().join(' '));
        minimapSvg.select('.minimap-guide').attr('transform', minimapTranslate);
    }

    /**
     * 프로세스 viewbox 를 조회하여 반환한다.
     *
     * @return {[number, number, *, *]}
     */
    function getSvgViewBox() {
        let isMinimapClosed = d3.select('div.minimap').classed('closed');
        if (isMinimapClosed) {
            d3.select('div.minimap').classed('closed', false);
        }
        const drawingBoard = d3.select(document.querySelector('.alice-process-drawing-board'));
        const minimapSvg = d3.select('div.minimap').select('svg');
        const nodeTopArray = [],
            nodeRightArray = [],
            nodeBottomArray = [],
            nodeLeftArray = [];
        const nodes = minimapSvg.selectAll('g.element, g.connector').nodes();
        nodes.forEach(function (node) {
            let nodeBBox = aliceProcessEditor.utils.getBoundingBoxCenter(d3.select(node));
            nodeTopArray.push(nodeBBox.cy - (nodeBBox.height / 2));
            nodeRightArray.push(nodeBBox.cx + (nodeBBox.width / 2));
            nodeBottomArray.push(nodeBBox.cy + (nodeBBox.height / 2));
            nodeLeftArray.push(nodeBBox.cx - (nodeBBox.width / 2));
        });
        if (isMinimapClosed) {
            d3.select('div.minimap').classed('closed', true);
        }
        let viewBox = [0, 0, drawingBoard.node().offsetWidth, drawingBoard.node().offsetHeight];
        if (nodes.length > 0) {
            const margin = 100;
            viewBox = [
                d3.min(nodeLeftArray) - margin,
                d3.min(nodeTopArray) - margin,
                Math.abs(d3.max(nodeRightArray) - d3.min(nodeLeftArray)) + (margin * 2),
                Math.abs(d3.max(nodeBottomArray) - d3.min(nodeTopArray)) + (margin * 2)
            ];
        }
        return viewBox;
    }

    /**
     * 드로잉보드 오른쪽 하단 버튼 기능 추가
     */
    function initializeButtonOnDrawingBoard() {
        // 미니맵 초기화 설정
        const drawingBoard = document.querySelector('.alice-process-drawing-board');
        const minimapContainer = document.createElement('div');
        minimapContainer.classList.add('minimap', 'closed');
        drawingBoard.appendChild(minimapContainer);
        d3.select(minimapContainer).append('svg');
        const minimapButtonContainer = document.createElement('div');
        minimapButtonContainer.classList.add('minimap-button');
        minimapButtonContainer.addEventListener('click', function () {
        // document.querySelector('.minimap-button').addEventListener('click', function () {
            document.querySelector('div.minimap').classList.toggle('closed');
            document.querySelector('div.minimap-button').classList.toggle('active');
        }, false);
        const minimapButton = document.createElement('img');
        minimapButton.classList.add('load-svg');
        minimapButton.setAttribute('src', '/assets/media/icons/process/editor/icon-map.svg');
        minimapButtonContainer.appendChild(minimapButton)
        drawingBoard.appendChild(minimapButtonContainer);

        setProcessMinimap();

        // 시뮬레이션 레포트 버튼 동작 이벤트 설정
        let simulationToggleEvent = function() {
            document.querySelector('.simulation-report').classList.toggle('closed');
            document.querySelector('.simulation-report-icon').classList.toggle('active');
        }
        document.querySelector('.simulation-report-icon').addEventListener('click', function () {
            simulationToggleEvent();
        });
        document.querySelector('.simulation-report .title .title-close').addEventListener('click', function () {
            simulationToggleEvent();
        });

        // 시뮬레이션 레포트 화면 drag 설정
        let simulationReportX = 0;
        let simulationReportY = 0;
        d3.select(document.querySelector('.simulation-report')).call(
            d3.drag()
                .on('start', function () {
                    simulationReportX = (d3.event.x - simulationReportX);
                    simulationReportY = (d3.event.y - simulationReportY);
                })
                .on('drag', function () {
                    let x = (d3.event.x - simulationReportX) + 'px';
                    let y = (d3.event.y - simulationReportY) + 'px';
                    this.style.transform = 'translate(' + x + ',' + y + ')';
                })
                .on('end', function () {
                    simulationReportX = (d3.event.x - simulationReportX)
                    simulationReportY = (d3.event.y - simulationReportY)
                })
        );
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

        initializeButtonOnDrawingBoard();
        // start observer
        isEdited = false;
        savedData = JSON.parse(JSON.stringify(aliceProcessEditor.data));
        setShortcut();
        changeProcessName();
        // aliceJs.loadSvg();
    }

    exports.utils = utils;
    exports.initUtil = initUtil;
    exports.autoSave = autoSaveProcess;
    Object.defineProperty(exports, '__esModule', {value: true});
})));
