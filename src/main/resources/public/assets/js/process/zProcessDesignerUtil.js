(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.zProcessDesigner = global.zProcessDesigner || {})));
}(this, (function (exports) {
    'use strict';

    let savedData = {};
    let isEdited = false;

    const RESPONSE_SUCCESS = '1';
    const RESPONSE_FAIL = '0';
    const RESPONSE_DUPLICATION = '-1';

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
                return !ZWorkflowUtil.compareJson(d[0], d[1]);
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
            zProcessDesigner.data.elements.sort(function (a, b) {
                return a.id < b.id ? -1 : 1;
            });
            savedData.elements.sort(function (a, b) {
                return a.id < b.id ? -1 : 1;
            });

            isEdited = !ZWorkflowUtil.compareJson(zProcessDesigner.data, savedData);
            changeProcessName();
            setProcessMinimap();
        },
        undo: function () {
            zProcessDesigner.removeElementSelected();
            zProcessDesigner.setElementMenu();
            if (this.undo_list.length) {
                let restoreData = this.undo_list.pop();
                redrawProcess(restoreData, 'undo');
                this.saveHistory(restoreData, this.redo_list, true);
            }
        },
        redo: function () {
            zProcessDesigner.removeElementSelected();
            zProcessDesigner.setElementMenu();
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
        document.getElementById('process-name').textContent = (isEdited ? '*' : '') + zProcessDesigner.data.process.name;
    }

    /**
     * 프로세스를 다시 그리고, 데이터 수정를 수정한다.
     *
     * @param restoreData 데이터
     * @param type 타입(undo, redo)
     */
    function redrawProcess(restoreData, type) {
        const restoreProcess = function (originData, changeData) {
            let links = zProcessDesigner.elements.links;
            if (!Object.keys(originData).length || !Object.keys(changeData).length) {
                if (!Object.keys(changeData).length) { // delete element
                    zProcessDesigner.data.elements.forEach(function (elem, i) {
                        if (originData.id === elem.id) {
                            zProcessDesigner.data.elements.splice(i, 1);
                        }
                    });
                    if (originData.type !== 'arrowConnector') {
                        let element = d3.select(document.getElementById(originData.id));
                        d3.select(element.node().parentNode).remove();
                    } else {
                        for (let i = 0, len = links.length; i < len; i++) {
                            if (links[i].id === originData.id) {
                                zProcessDesigner.elements.links.splice(i, 1);
                                break;
                            }
                        }
                    }
                } else { // add element
                    if (changeData.type !== 'arrowConnector') {
                        let node = zProcessDesigner.addElement(changeData);
                        if (node) {
                            node.nodeElement.attr('id', changeData.id);
                            zProcessDesigner.data.elements.push(changeData);
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
                        zProcessDesigner.data.elements.push(changeData);
                    }
                }
            } else if (typeof changeData.type === 'undefined') { // modify process data
                zProcessDesigner.data.process = changeData;
                if (originData.name !== changeData.name) { // modify name
                    changeProcessName();
                }
                zProcessDesigner.setElementMenu();
            } else { // modify element
                let element = d3.select(document.getElementById(changeData.id));
                if (originData.type !== changeData.type) { // modify type
                    zProcessDesigner.changeElementType(element, changeData.type);
                }
                if (originData.name !== changeData.name) { // modify name
                    zProcessDesigner.changeTextToElement(changeData.id, changeData.name);
                }
                if (changeData.type !== 'arrowConnector') {
                    if (originData.display['position-x'] !== changeData.display['position-x']
                        || originData.display['position-y'] !== changeData.display['position-y']
                        || originData.display.width !== changeData.display.width
                        || originData.display.height !== changeData.display.height
                        || originData.data['line-color'] !== changeData.data['line-color']
                        || originData.data['background-color'] !== changeData.data['background-color']) { // modify position or size or group color
                        let node = zProcessDesigner.addElement(changeData);
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
                for (let i = 0, len = zProcessDesigner.data.elements.length; i < len; i++) {
                    if (zProcessDesigner.data.elements[i].id === changeData.id) {
                        zProcessDesigner.data.elements[i] = changeData;
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
        zProcessDesigner.setConnectors(true);
    }

    /**
     * save process.
     */
    function saveProcess() {
        if(!valdationCheck()) return false;
        zProcessDesigner.resetElementPosition();
        save(function (response) {
            let resultCode = response.responseText;
            switch (resultCode) {
                case RESPONSE_DUPLICATION:
                    aliceAlert.alertWarning(i18n.msg('process.msg.duplicateProcessName'));
                    return;
                case RESPONSE_FAIL:
                    aliceAlert.alertWarning(i18n.msg('common.msg.fail'));
                    return;
                default:
                    aliceAlert.alertSuccess(i18n.msg('common.msg.save'));
                    isEdited = false;
                    savedData = JSON.parse(JSON.stringify(zProcessDesigner.data));
                    if (savedData.process.status === 'process.status.publish' ||
                        savedData.process.status === 'process.status.use') {
                        uploadProcessFile();
                    }
                    changeProcessName();
                    zProcessDesigner.initialStatus = savedData.process.status;
            }
        });
    }

    /**
     * 자동 저장 (현재는 최초 오픈 시 start event 추가 후 저장 기능을 위해서만 사용중)
     */
    function autoSaveProcess() {
        save(function (xhr) {
            if (xhr.responseText === RESPONSE_SUCCESS) {
                isEdited = false;
                savedData = JSON.parse(JSON.stringify(zProcessDesigner.data));
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
            url: '/rest/process/' + zProcessDesigner.data.process.id + '/data',
            contentType: 'application/json; charset=utf-8',
            params: JSON.stringify(zProcessDesigner.data),
            callbackFunc: callbackFunc
        });
    }

    /**
     * 다른 이름으로 저장 content.
     *
     * @return {string} content html
     */
    function createDialogContent() {
        return `
            <div class="save-as-main flex-column">
                <label class="field-label" for="process_name">${i18n.msg('process.label.name')}<span class="required"></span></label>
                <input type="text" id="process_name">
                <label class="field-label" for="process_description">${i18n.msg('process.label.description')}</label>
                <textarea rows="3" class="textarea-scroll-wrapper" id="process_description"></textarea>
            </div>
            `;
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
                nameTextObject.classList.add('error');
                aliceAlert.alertWarning(i18n.msg('common.msg.requiredEnter'), function() {
                    nameTextObject.focus();
                });
                return false;
            }
            nameTextObject.classList.remove('error');
            return true;
        };
        /**
         * 저장처리.
         */
        const saveAs = function () {
            const saveAsProcessData = JSON.parse(JSON.stringify(zProcessDesigner.data));
            let processData = saveAsProcessData.process;
            processData.name = document.getElementById('process_name').value;
            processData.description = document.getElementById('process_description').value;
            aliceJs.sendXhr({
                method: 'POST',
                url: '/rest/processes' + '?saveType=saveas',
                callbackFunc: function (response) {
                    let resultToJson = JSON.parse(response.responseText);
                    let processId = resultToJson.processId;
                    let resultCode = resultToJson.result;
                    switch (resultCode.toString()) {
                        case RESPONSE_DUPLICATION:
                            aliceAlert.alertWarning(i18n.msg('process.msg.duplicateProcessName'));
                            return;
                        case RESPONSE_FAIL:
                            aliceAlert.alertWarning(i18n.msg('common.msg.fail'));
                            return;
                        default:
                            aliceAlert.alertSuccess(i18n.msg('common.msg.save'), function () {
                                opener.location.reload();
                                window.name = 'process_' + processId + '_edit';
                                location.href = '/process/' + processId + '/edit';
                            });

                    }
                },
                contentType: 'application/json; charset=utf-8',
                params: JSON.stringify(saveAsProcessData)
            });
        };

        /**
         * 다른 이름으로 저장하기 모달 저장 CallBack.
         */
        const saveAsCallBack = function() {
            if (checkRequired()) {
                saveAs();
                return true;
            }
            return false;
        };

        /**
         * 다른 이름으로 저장하기 모달.
         */

        const saveAsModal = new modal({
            title: i18n.msg('common.btn.saveAs'),
            body: createDialogContent(),
            classes: 'save-as',
            buttons: [
                {
                    content: i18n.msg('common.btn.save'),
                    classes: 'z-button primary',
                    bindKey: false,
                    callback: function(modal) {
                        if (saveAsCallBack()) {
                            modal.hide();
                        }
                    }
                }, {
                    content: i18n.msg('common.btn.cancel'),
                    classes: 'z-button secondary',
                    bindKey: false,
                    callback: function(modal) {
                        modal.hide();
                    }
                }
            ],
            close: {
                closable: false,
            },
            onCreate: function(modal) {
                OverlayScrollbars(document.getElementById('process_description'), {
                    className: 'scrollbar',
                    resize: 'none',
                    sizeAutoCapable: true,
                    textarea: {
                        dynHeight: false,
                        dynWidth: false,
                        inheritedAttrs: 'class'
                    }
                });
            }
        });
        saveAsModal.show();
    }

    /**
     * 시뮬레이션 점검 동작
     */
    function simulationProcess() {
        aliceJs.sendXhr({
            method: 'put',
            url: '/rest/process/' + zProcessDesigner.data.process.id + '/simulation',
            callbackFunc: function (xhr) {
                if (document.querySelectorAll('.simulation-report-contents-main .details div').length > 0) {
                    document.querySelectorAll('.simulation-report-contents-main .details div').forEach((element) => element.parentElement.removeChild(element));
                    document.querySelector('.simulation-report-contents-main .result').classList.remove('success', 'failed');
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
                document.querySelector('.simulation-report-contents-main .result').classList.add(mainResultClassName);
                document.querySelector('.simulation-report-contents-main .result').textContent = mainResult;

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
                    result.textContent = successOrFailure;
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

                    document.querySelector('.simulation-report-contents-main .details').appendChild(reportDetails);
                }

                if (document.querySelector('.simulation-report').classList.contains('closed')) {
                    document.querySelector('.z-button-simulation-report').click();
                }
                // 스크롤바 생성
                OverlayScrollbars(document.querySelector('.simulation-report-contents-main'), { className: 'scrollbar' });
            },
            contentType: 'application/json; charset=utf-8',
            params: JSON.stringify(zProcessDesigner.data)
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
                a.download = zProcessDesigner.data.process.name + '_' + zProcessDesigner.data.process.id + '.png';
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
            formData.append('file', blob, zProcessDesigner.data.process.id + '.xml');

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
        processNode.setAttribute('id', zProcessDesigner.data.process.id);
        processNode.setAttribute('name', zProcessDesigner.data.process.name);
        processNode.setAttribute('description', zProcessDesigner.data.process.description);

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
        const elements = zProcessDesigner.data.elements.filter(elem => excludeElementTypes.indexOf(elem.type) === -1);
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
        let svg = d3.select(svgNode).html(d3.select('.drawing-board > svg').html());
        svg.attr('width', viewBox[2])
            .attr('height', viewBox[3])
            .attr('viewBox', viewBox.join(' '))
            .classed('drawing-board', true);

        svg.selectAll('.guides-container, .alice-tooltip, .grid, .tick, .pointer, .drag-line, .painted-connector').remove();
        svg.selectAll('.group-artifact-container, .element-container, .connector-container').attr('transform', '');
        svg.selectAll('.node.selected').nodes().forEach(function (node) {
            zProcessDesigner.setDeselectedElement(d3.select(node));
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
        zShortcut.init();

        const shortcuts = [
            {'keys': 'ctrl+s', 'command': 'zProcessDesigner.utils.save();', 'force': true},             // 저장
            {'keys': 'ctrl+shift+s', 'command': 'zProcessDesigner.utils.saveAs();', 'force': true},     // 다른 이름으로 저장
            {'keys': 'ctrl+z', 'command': 'zProcessDesigner.utils.undo();', 'force': false},            // 작업 취소
            {'keys': 'ctrl+shift+z', 'command': 'zProcessDesigner.utils.redo();', 'force': false},      // 작업 재실행
            {'keys': 'ctrl+e', 'command': 'zProcessDesigner.utils.simulation();', 'force': false},      // 미리보기(시뮬레이션)
            {'keys': 'ctrl+d', 'command': 'zProcessDesigner.utils.download();', 'force': false},        // 이미지 다운로드
            {'keys': 'ctrl+x,delete', 'command': 'zProcessDesigner.deleteElements();', 'force': false}, // 엘리먼트 삭제
            {'keys': 'alt+e', 'command': 'zProcessDesigner.utils.focus();', 'force': false}             // 세부 속성 편집: 제일 처음으로 이동
        ];

        for (let i = 0; i < shortcuts.length; i++) {
            zShortcut.add(shortcuts[i].keys, shortcuts[i].command, shortcuts[i].force);
        }
    }

    /**
     * focus properties panel.
     */
    function focusPropertiesPanel() {
        let panel = document.querySelector('.process-properties');
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
        const drawingboardContainer = document.querySelector('.drawing-board');
        let drawingBoard = d3.select(drawingboardContainer).select('svg');
        let content = drawingBoard.html();
        const minimapSvg = d3.select('div.minimap').select('svg');
        minimapSvg.html(content);
        minimapSvg.attr('width', 290).attr('height', 200);
        minimapSvg.selectAll('.guides-container, .alice-tooltip, .grid, .tick, .pointer, .drag-line, .painted-connector, defs').remove();
        minimapSvg.selectAll('text').nodes().forEach(function (node) {
            if (node.textContent === '') {
                d3.select(node).remove();
            }
        });
        minimapSvg.selectAll('.group-artifact-container, .element-container, .connector-container').attr('transform', '');
        minimapSvg.selectAll('.node.selected').nodes().forEach(function (node) {
            zProcessDesigner.setDeselectedElement(d3.select(node));
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
        const drawingBoard = d3.select(document.querySelector('.drawing-board'));
        const minimapSvg = d3.select('div.minimap').select('svg');
        const nodeTopArray = [],
            nodeRightArray = [],
            nodeBottomArray = [],
            nodeLeftArray = [];
        const nodes = minimapSvg.selectAll('g.element, g.connector').nodes();
        nodes.forEach(function (node) {
            let nodeBBox = zProcessDesigner.utils.getBoundingBoxCenter(d3.select(node));
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
        const drawingBoard = document.querySelector('.drawing-board');

        // 미니맵 초기화 설정
        const minimapContainer = document.createElement('div');
        minimapContainer.classList.add('minimap', 'closed');
        drawingBoard.appendChild(minimapContainer);
        d3.select(minimapContainer).append('svg');
        // 미니맵 버튼
        const minimapButton = document.createElement('button');
        minimapButton.type = 'button';
        minimapButton.className = 'z-button-icon secondary z-button-minimap';
        minimapButton.addEventListener('click', function (e) {
            const elem = aliceJs.clickInsideElement(e, 'z-button-minimap');
            elem.classList.toggle('active');
            document.querySelector('div.minimap').classList.toggle('closed');
        }, false);

        const minimapIcon = document.createElement('span');
        minimapIcon.className = 'z-icon i-minimap';
        minimapButton.appendChild(minimapIcon);
        drawingBoard.appendChild(minimapButton);

        setProcessMinimap();

        // 시뮬레이션 레포트 버튼 동작 이벤트 설정
        const simulationToggleEvent = function() {
            document.querySelector('.simulation-report').classList.toggle('closed');
            document.querySelector('.z-button-simulation-report').classList.toggle('active');
        };

        // 시뮬레이션 레포트 초기화 설정
        const simulationContainer = document.createElement('div');
        simulationContainer.classList.add('simulation-report', 'closed');

        const simulationTitle = document.createElement('div');
        simulationTitle.className = 'simulation-report-title';
        simulationTitle.textContent = i18n.msg('process.btn.simulationCheckResult');

        const simulationClose = document.createElement('span');
        simulationClose.className = 'icon-minus';
        simulationTitle.appendChild(simulationClose);
        simulationClose.addEventListener('click', simulationToggleEvent, false);
        simulationContainer.appendChild(simulationTitle);

        const simulationContent = document.createElement('div');
        simulationContent.className = 'simulation-report-contents';

        const simulationMain = document.createElement('div');
        simulationMain.className = 'simulation-report-contents-main';

        const simulationResult = document.createElement('div');
        simulationResult.className = 'result';
        simulationMain.appendChild(simulationResult);

        const simulationDetail = document.createElement('div');
        simulationDetail.className = 'details';
        simulationMain.appendChild(simulationDetail);

        simulationContent.appendChild(simulationMain);
        simulationContainer.appendChild(simulationContent);
        drawingBoard.appendChild(simulationContainer);

        // 시뮬레이션 동작 버튼
        const simulationButton = document.createElement('button');
        simulationButton.type = 'button';
        simulationButton.className = 'z-button-icon secondary z-button-simulation-report';
        simulationButton.addEventListener('click', simulationToggleEvent, false);

        const simulationIcon = document.createElement('span');
        simulationIcon.className = 'z-icon i-simulation-report';
        simulationButton.appendChild(simulationIcon);
        drawingBoard.appendChild(simulationButton);

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
                    simulationReportX = (d3.event.x - simulationReportX);
                    simulationReportY = (d3.event.y - simulationReportY);
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
        savedData = JSON.parse(JSON.stringify(zProcessDesigner.data));
        setShortcut();
        changeProcessName();
    }

    exports.utils = utils;
    exports.initUtil = initUtil;
    exports.autoSave = autoSaveProcess;
    Object.defineProperty(exports, '__esModule', {value: true});
})));

function valdationCheck() {
    let typeList = ['commonStart', `timerStart`, 'signalSend', 'manualTask', 'userTask', 'scriptTask', 'arrowConnector',
        'exclusiveGateway', 'inclusiveGateway', 'parallelGateway', 'groupArtifact', 'annotationArtifact', 'commonEnd'];
    let totalElements = zProcessDesigner.data.elements;
    let requiredList = [];
    let deployableStatus = ['process.status.publish', 'process.status.use'];
    let nowStatus = zProcessDesigner.data.process.status;

    if (deployableStatus.indexOf(zProcessDesigner.initialStatus) >= 0 && deployableStatus.indexOf(nowStatus) >= 0) {
        aliceJs.alertWarning(i18n.msg('common.msg.onlySaveInEdit'));
        return false;
    }
    if (zProcessDesigner.isView) return false;
    if (zProcessDesigner.data.process.name.toString().trim() === '') {
        aliceAlert.alertWarning(i18n.msg('process.msg.enterProcessName'));
        return false;
    }

    if (deployableStatus.indexOf(nowStatus) >= 0) {
        for (let i = 0; i < totalElements.length; i++) {
            if (typeList.indexOf(totalElements[i].type) >= 0) {
                requiredList = totalElements[i].required;
                for (let key in totalElements[i]) {
                    if (requiredList.indexOf(key) >= 0) {
                        if (totalElements[i][key].toString().trim() === '') {
                            const errorElem = document.getElementById(totalElements[i].id);
                            aliceAlert.alertWarning(i18n.msg('process.msg.enterRequired',
                                i18n.msg('process.designer.attribute.' + totalElements[i].type)));
                            zProcessDesigner.setSelectedElement(d3.select(errorElem));
                            zProcessDesigner.setElementMenu(d3.select(errorElem));
                            return false;
                        }
                    }
                }
                if (totalElements[i].data !== undefined) {
                    for (let key in totalElements[i].data) {
                        if (requiredList.indexOf(key) >= 0) {
                            if (totalElements[i].data[key].toString().trim() === '') {
                                const errorElem = document.getElementById(totalElements[i].id);
                                aliceAlert.alertWarning(i18n.msg('process.msg.enterRequired',
                                    i18n.msg('process.designer.attribute.' + totalElements[i].type)));
                                zProcessDesigner.setSelectedElement(d3.select(errorElem));
                                zProcessDesigner.setElementMenu(d3.select(errorElem));
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    return true;
}
