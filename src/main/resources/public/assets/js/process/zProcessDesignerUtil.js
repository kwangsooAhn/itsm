(function(global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.zProcessDesigner = global.zProcessDesigner || {})));
}(this, (function(exports) {
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
                return !ZWorkflowUtil.compareJson(d[0], d[1]);
            });
            if (!data.length) {
                return;
            }
            keep_redo = keep_redo || false;
            if (!keep_redo) {
                this.redo_list = [];
            }
            (list || this.undo_list).push(data);

            // 엘리먼트 정렬
            zProcessDesigner.data.elements.sort(function(a, b) {
                return a.id < b.id ? -1 : 1;
            });
            savedData.elements.sort(function(a, b) {
                return a.id < b.id ? -1 : 1;
            });

            isEdited = !ZWorkflowUtil.compareJson(zProcessDesigner.data, savedData);
            changeProcessName();
            setProcessMinimap();
        },
        undo: function() {
            zProcessDesigner.removeElementSelected();
            zProcessDesigner.setElementMenu();
            if (this.undo_list.length) {
                let restoreData = this.undo_list.pop();
                redrawProcess(restoreData, 'undo');
                this.saveHistory(restoreData, this.redo_list, true);
            }
        },
        redo: function() {
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
        document.getElementById('processName').textContent = (isEdited ? '*' : '') + zProcessDesigner.data.process.name;
    }

    /**
     * 프로세스를 다시 그리고, 데이터 수정를 수정한다.
     *
     * @param restoreData 데이터
     * @param type 타입(undo, redo)
     */
    function redrawProcess(restoreData, type) {
        const restoreProcess = function(originData, changeData) {
            let links = zProcessDesigner.elements.links;
            if (!Object.keys(originData).length || !Object.keys(changeData).length) {
                if (!Object.keys(changeData).length) { // delete element
                    zProcessDesigner.data.elements.forEach(function(elem, i) {
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
        restoreData.forEach(function(data) {
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
        if (!validationCheck()) return false;
        zProcessDesigner.resetElementPosition();
        save(function(response) {
            switch (response.status) {
                case aliceJs.response.success:
                    zAlert.success(i18n.msg('common.msg.save'));
                    isEdited = false;
                    savedData = JSON.parse(JSON.stringify(zProcessDesigner.data));
                    if (savedData.process.status === 'process.status.publish' ||
                        savedData.process.status === 'process.status.use') {
                        uploadProcessFile();
                    }
                    changeProcessName();
                    zProcessDesigner.initialStatus = savedData.process.status;
                    break;
                case aliceJs.response.duplicate:
                    zAlert.warning(i18n.msg('process.msg.duplicateName'));
                    break;
                case aliceJs.response.error:
                    zAlert.danger(i18n.msg('common.msg.fail'));
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * 자동 저장 (현재는 최초 오픈 시 start event 추가 후 저장 기능을 위해서만 사용중)
     */
    function autoSaveProcess() {
        save(function(response) {
            if (response.status === aliceJs.response.success) {
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
        aliceJs.fetchJson('/rest/process/' + zProcessDesigner.data.process.id + '/data', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(zProcessDesigner.data),
            showProgressbar: true
        }).then((response) => {
            if (typeof callbackFunc === 'function') {
                callbackFunc(response);
            }
        });
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
            let nameTextObject = document.getElementById('newProcessName');
            if (nameTextObject.value.trim() === '') {
                nameTextObject.classList.add('error');
                zAlert.warning(i18n.msg('common.msg.requiredEnter'), function() {
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
        const saveAs = function() {
            const saveAsProcessData = JSON.parse(JSON.stringify(zProcessDesigner.data));
            let processData = saveAsProcessData.process;
            processData.name = document.getElementById('newProcessName').value;
            processData.description = document.getElementById('newProcessDescription').value;
            aliceJs.fetchJson('/rest/processes' + '?saveType=saveas', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(saveAsProcessData),
                showProgressbar: true
            }).then((response) => {
                switch (response.status) {
                    case aliceJs.response.success:
                        const processId = response.data.processId;
                        zAlert.success(i18n.msg('common.msg.save'), function() {
                            opener.location.reload();

                            window.name = 'process_' + processId + '_edit';
                            location.href = '/process/' + processId + '/edit';
                        });
                        break;
                    case aliceJs.response.duplicate:
                        zAlert.warning(i18n.msg('process.msg.duplicateName'));
                        break;
                    case aliceJs.response.error:
                        zAlert.danger(i18n.msg('common.msg.fail'));
                        break;
                    default:
                        break;
                }
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
        const saveAsModalTemplate = document.getElementById('saveAsModalTemplate');
        const saveAsModal = new modal({
            title: i18n.msg('common.btn.saveAs'),
            body: saveAsModalTemplate.content.cloneNode(true),
            classes: 'save-as',
            buttons: [
                {
                    content: i18n.msg('common.btn.save'),
                    classes: 'btn__text--box primary',
                    bindKey: false,
                    callback: function(modal) {
                        if (saveAsCallBack()) {
                            modal.hide();
                        }
                    }
                }, {
                    content: i18n.msg('common.btn.cancel'),
                    classes: 'btn__text--box secondary',
                    bindKey: false,
                    callback: function(modal) {
                        modal.hide();
                    }
                }
            ],
            close: { closable: false },
            onCreate: function(modal) {
                OverlayScrollbars(document.getElementById('newProcessDescription'), {
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
        aliceJs.fetchJson('/rest/process/' + zProcessDesigner.data.process.id + '/simulation', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(zProcessDesigner.data)
        }).then((response) => {
            // 기존 데이터 삭제
            const prevReportList = document.querySelectorAll('.simulation-report-contents-main .details div');
            const prevReportResult = document.querySelector('.simulation-report-contents-main .result');
            if (prevReportList.length > 0) {
                prevReportList.forEach((element) => element.parentElement.removeChild(element));
                if (prevReportResult) {
                    prevReportResult.classList.remove('success', 'failed');
                }
            }

            // 전체 결과
            let mainResult = response.data.success ? i18n.msg('common.label.success') : i18n.msg('common.label.fail');
            let mainResultClassName = response.data.success ? 'success' : 'failed';

            prevReportResult.classList.add(mainResultClassName);
            prevReportResult.textContent = mainResult;

            // 세부 결과
            const curReportList = (response.status === aliceJs.response.success) ? response.data.simulationReport : [];
            for (let i = 0; i < curReportList.length; i++) {
                const report = curReportList[i];

                const successOrFailure = '[' + (report.success ? i18n.msg('common.label.success') : i18n.msg('common.label.fail')) + ']';
                const order = '[' + [i + 1] + '/' + curReportList.length + ']';
                const elementDescription = '[' + report.elementType + (report.elementName !== '' ? ':' + report.elementName : '') + ']';
                const message = response.data.success ? '' : '[' + report.failedMessage + ']';
                const reportDetailClassName = report.success ? '' : 'failed';

                if (response.data.success) {
                    document.getElementById(report.elementId).classList.remove('selected', 'error');
                } else {
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
                document.querySelector('.button-simulation-report').click();
            }
            // 스크롤바 생성
            OverlayScrollbars(document.querySelector('.simulation-report-contents-main'), { className: 'scrollbar' });
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
            image.onload = function() {
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
            let blob = new Blob(['<?xml version="1.0" encoding="UTF-8"?>' + xmlString], { type: 'text/plain' });
            formData.append('file', blob, zProcessDesigner.data.process.id + '.xml');

            let xhr = new XMLHttpRequest();
            xhr.open('POST', '/rest/resources/file/upload?type=process');
            xhr.onerror = function() {
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
        elements.forEach(function(element) {
            let elementNode = xmlDoc.createElement('element');
            elementNode.setAttribute('id', element.id);
            elementNode.setAttribute('type', element.type);
            let keys = Object.keys(element.display);
            keys.forEach(function(key) {
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
        svg.selectAll('.node.selected').nodes().forEach(function(node) {
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

        return Promise.all(Array.from(svgNode.querySelectorAll('image')).map(function(image) {
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
            { 'keys': 'ctrl+s', 'command': 'zProcessDesigner.utils.save();', 'force': true },             // 저장
            { 'keys': 'ctrl+shift+s', 'command': 'zProcessDesigner.utils.saveAs();', 'force': true },     // 다른 이름으로 저장
            { 'keys': 'ctrl+z', 'command': 'zProcessDesigner.utils.undo();', 'force': false },            // 작업 취소
            { 'keys': 'ctrl+shift+z', 'command': 'zProcessDesigner.utils.redo();', 'force': false },      // 작업 재실행
            { 'keys': 'ctrl+e', 'command': 'zProcessDesigner.utils.simulation();', 'force': false },      // 미리보기(시뮬레이션)
            { 'keys': 'ctrl+d', 'command': 'zProcessDesigner.utils.download();', 'force': false },        // 이미지 다운로드
            { 'keys': 'ctrl+x,delete', 'command': 'zProcessDesigner.deleteElements();', 'force': false }, // 엘리먼트 삭제
            { 'keys': 'alt+e', 'command': 'zProcessDesigner.utils.focus();', 'force': false }             // 세부 속성 편집: 제일 처음으로 이동
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
        if (!items.length) {
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
        minimapSvg.selectAll('text').nodes().forEach(function(node) {
            if (node.textContent === '') {
                d3.select(node).remove();
            }
        });
        minimapSvg.selectAll('.group-artifact-container, .element-container, .connector-container').attr('transform', '');
        minimapSvg.selectAll('.node.selected').nodes().forEach(function(node) {
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
        nodes.forEach(function(node) {
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
        minimapContainer.className = 'minimap closed';
        drawingBoard.appendChild(minimapContainer);
        d3.select(minimapContainer).append('svg');
        // 미니맵 버튼
        const minimapButton = document.createElement('button');
        minimapButton.type = 'button';
        minimapButton.className = 'btn__ic secondary button-minimap palette-tooltip';
        minimapButton.addEventListener('click', function(e) {
            const elem = aliceJs.clickInsideElement(e, 'button-minimap');
            elem.classList.toggle('active');
            document.querySelector('div.minimap').classList.toggle('closed');
        }, false);

        const minimapIcon = document.createElement('span');
        minimapIcon.className = 'ic-minimap';

        const minimapTooltip = document.createElement('div');
        minimapTooltip.className = 'palette-tooltip-contents right-bottom'
        const minimapTooltipText = document.createElement('span');
        minimapTooltipText.className = 'palette-tooltip-text'
        minimapTooltipText.textContent = i18n.msg('tooltip.label.minimap');

        minimapTooltip.appendChild(minimapTooltipText);
        minimapButton.appendChild(minimapIcon);
        minimapButton.appendChild(minimapTooltip);
        drawingBoard.appendChild(minimapButton);

        setProcessMinimap();

        // 시뮬레이션 레포트 버튼 동작 이벤트 설정
        const simulationToggleEvent = function() {
            document.querySelector('.simulation-report').classList.toggle('closed');
            document.querySelector('.button-simulation-report').classList.toggle('active');
        };

        // 시뮬레이션 레포트 초기화 설정
        const simulationContainer = document.createElement('div');
        simulationContainer.className = 'simulation-report closed';

        const simulationTitle = document.createElement('div');
        simulationTitle.className = 'simulation-report-title';
        simulationTitle.textContent = i18n.msg('process.btn.simulationCheckResult');

        const simulationClose = document.createElement('span');
        simulationClose.className = 'ic-minus';
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
        simulationButton.className = 'btn__ic secondary button-simulation-report palette-tooltip';
        simulationButton.addEventListener('click', simulationToggleEvent, false);

        const simulationIcon = document.createElement('span');
        simulationIcon.className = 'ic-simulation-report';

        const simulationTooltip = document.createElement('div');
        simulationTooltip.className = 'palette-tooltip-contents'
        const simulationTooltipText = document.createElement('span');
        simulationTooltipText.className = 'palette-tooltip-text'
        simulationTooltipText.textContent = i18n.msg('tooltip.label.simulation');

        simulationTooltip.appendChild(simulationTooltipText);
        simulationButton.appendChild(simulationIcon);
        simulationButton.appendChild(simulationTooltip);
        drawingBoard.appendChild(simulationButton);

        // 시뮬레이션 레포트 화면 drag 설정
        let simulationReportX = 0;
        let simulationReportY = 0;
        d3.select(document.querySelector('.simulation-report')).call(
            d3.drag()
                .on('start', function() {
                    simulationReportX = (d3.event.x - simulationReportX);
                    simulationReportY = (d3.event.y - simulationReportY);
                })
                .on('drag', function() {
                    let x = (d3.event.x - simulationReportX) + 'px';
                    let y = (d3.event.y - simulationReportY) + 'px';
                    this.style.transform = 'translate(' + x + ',' + y + ')';
                })
                .on('end', function() {
                    simulationReportX = (d3.event.x - simulationReportX);
                    simulationReportY = (d3.event.y - simulationReportY);
                })
        );
    }

    /**
     * 상단 드롭다운 이벤트 핸들러
     */
    function onDropdownClickHandler(e) {
        const target = e.target || e;
        const targetId = target.getAttribute('data-targetId');
        const changeTarget = document.getElementById(targetId);

        const actionType = target.getAttribute('data-actionType');
        if (changeTarget.firstElementChild.getAttribute('data-actionType') !== actionType) {
            // 기존 버튼 삭제
            changeTarget.removeChild(changeTarget.firstElementChild);
            // 버튼 추가
            const buttonTemplate = document.getElementById(actionType + 'ButtonTemplate');
            changeTarget.appendChild(buttonTemplate.content.cloneNode(true));
            // 이벤트 할당
            changeTarget.firstElementChild.addEventListener('click', onDropdownClickHandler);
        }

        // 이벤트 실행
        switch (actionType) {
            case 'undo':
                undoProcess();
                break;
            case 'redo':
                redoProcess();
                break;
            case 'save':
                saveProcess();
                break;
            case 'saveAs':
                saveAsProcess();
                break;
            default:
                break;
        }
    }

    /**
     * init workflow util.
     */
    function initUtil() {
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
    exports.saveProcess = saveProcess;
    exports.saveAsProcess = saveAsProcess;
    exports.simulationProcess = simulationProcess;
    exports.downloadProcessImage = downloadProcessImage;
    exports.onDropdownClickHandler = onDropdownClickHandler;
    Object.defineProperty(exports, '__esModule', { value: true });
})));

function validationCheck() {
    let typeList = ['commonStart', `timerStart`, 'signalSend', 'manualTask', 'userTask', 'scriptTask', 'arrowConnector',
        'exclusiveGateway', 'inclusiveGateway', 'parallelGateway', 'groupArtifact', 'annotationArtifact', 'commonEnd', 'subprocess'];
    let totalElements = zProcessDesigner.data.elements;
    let requiredList = [];
    let deployableStatus = ['process.status.publish', 'process.status.use'];
    let nowStatus = zProcessDesigner.data.process.status;
    let commonStartCount = 0;
    let commonStartId = '';

    // '발행' or '사용' 상태의 문서를 저장하려는 경우 경고 표시 (#8969 일감 참조)
    if (zProcessDesigner.isView && deployableStatus.includes(zProcessDesigner.initialStatus)) {
        zAlert.warning(i18n.msg('common.msg.notSaveAfterPublishAndUse'));
        return false;
    }

    // 폐기상태일 경우 저장 불가능
    if (zProcessDesigner.isView && zProcessDesigner.initialStatus === 'process.status.destroy') {
        zAlert.warning(i18n.msg('common.msg.notSaveAfterDestroy'));
        return false;
    }

    // '편집' 상태에서 '발행' or '사용' 상태로 저장하려는 경우 유효성 검증
    if (zProcessDesigner.initialStatus === 'process.status.edit' && deployableStatus.includes(nowStatus)) {
        if (zProcessDesigner.data.process.name.toString().trim() === '') {
            zAlert.warning(i18n.msg('process.msg.enterProcessName'));
            return false;
        }

        for (let i = 0; i < totalElements.length; i++) {
            if (totalElements[i].type === 'commonStart') {
                commonStartCount++;
                commonStartId = totalElements[i].id;
            }
            if (typeList.indexOf(totalElements[i].type) >= 0) {
                requiredList = totalElements[i].required;
                for (let key in totalElements[i]) {
                    if (requiredList.indexOf(key) > -1) {
                        if (totalElements[i][key].toString().trim() === '') {
                            const errorElem = document.getElementById(totalElements[i].id);
                            zAlert.warning(i18n.msg('process.msg.enterRequired',
                                i18n.msg('process.designer.attribute.' + totalElements[i].type)));
                            zProcessDesigner.setSelectedElement(d3.select(errorElem));
                            zProcessDesigner.setElementMenu(d3.select(errorElem));
                            return false;
                        }
                    }
                }
                if (totalElements[i].data !== undefined) {
                    for (let key in totalElements[i].data) {
                        if (requiredList.indexOf(key) > -1) {
                            if (totalElements[i].data[key].toString().trim() === '') {
                                const errorElem = document.getElementById(totalElements[i].id);
                                zAlert.warning(i18n.msg('process.msg.enterRequired',
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

        if (commonStartCount !== 1) {
            zAlert.warning(i18n.msg('process.msg.startElementMustOne'));
            return false;
        }

        for (let i = 0; i < totalElements.length; i++) { //첫번째 엘리먼트에 회수 기능 설정시 회수 기능 설정불가 알림
            if (totalElements[i].type === 'arrowConnector' && totalElements[i].data['start-id'] == commonStartId) {
                for (let j = 0; j < totalElements.length; j++) {
                    if (totalElements[i].data['end-id'] == totalElements[j].id && totalElements[j].data['withdraw'] == 'Y') {
                        const errorElem = document.getElementById(totalElements[j].id);
                        zProcessDesigner.setSelectedElement(d3.select(errorElem));
                        zProcessDesigner.setElementMenu(d3.select(errorElem));
                        zAlert.warning(i18n.msg('process.msg.uncheckWithdraw', totalElements[j].name));
                        return false;
                    }
                }
            }
        }
        return true;
    }
    return true;
}
