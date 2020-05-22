(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.aliceProcessUtil = global.aliceProcessUtil || {})));
}(this, (function (exports) {
    'use strict';

    let fileList = [];
    /**
     * IMPORT.
     *
     * @param fileElemId 파일 선택 Element ID
     */
    function initImport(fileElemId) {
        const fileSelector = document.getElementById(fileElemId);
        fileSelector.addEventListener('change', function(event) {
            fileList = event.target.files;
        });
    }

    /**
     * import process.
     *
     * @param processInfo IMPORT process 정보
     * @param callbackFunc IMPORT 처리 후 호출될 function
     */
    function importProcess(processInfo, callbackFunc) {
        if (fileList.length > 0) {
            try {
                const reader = new FileReader();
                reader.addEventListener('load', function(e) {
                    let processData = loadProcessFromXML(e.target.result);
                    processData.process = {name: processInfo.processName, description: processInfo.processDesc};
                    addRequiredAttribute(processData);
                    console.debug(processData);
                    let result = saveProcess(processData);
                    if (typeof callbackFunc === 'function') {
                        callbackFunc(result);
                    }
                });
                reader.readAsText(fileList[0], 'utf-8');
            } catch (e) {
                console.error(e);
            }
        } else {
            console.error('No file selected.');
        }
    }

    /**
     * 파일 데이터로부터 프로세스 정보를 읽어 json 구조로 만들어 반환 한다.
     *
     * @param data XML 파일 데이터
     * @return {{elements: []}}
     */
    function loadProcessFromXML(data) {
        const processData = {elements: []};

        const parser = new DOMParser();
        const xmlDoc = parser.parseFromString(data,'text/xml');
        if (isParseError(xmlDoc)) {
            throw new Error('Error parsing XML');
        }

        const resultType = XPathResult.ANY_UNORDERED_NODE_TYPE;
        let processNode = xmlDoc.evaluate('/definitions/process', xmlDoc, null, resultType, null);
        let diagramNode = xmlDoc.evaluate('/definitions/diagram', xmlDoc, null, resultType, null);
        let processNodeList = processNode.singleNodeValue.childNodes;
        let diagramNodeList = diagramNode.singleNodeValue.childNodes
        for (let i = 0, len = processNodeList.length; i < len; i++) {
            let processNode = processNodeList[i];
            if (processNode.nodeType === Node.ELEMENT_NODE) {
                let elementData = {type: processNode.nodeName, data: {}, display: {}};

                let processNodeAttributes = processNode.attributes;
                for (let j = 0, attrLen = processNodeAttributes.length; j < attrLen; j++) {
                    let attr = processNodeAttributes[j];
                    if (attr.nodeName === 'id') {
                        elementData.id = attr.nodeValue;
                    }
                    elementData.data[attr.nodeName] = attr.nodeValue;
                }

                let processNodeChildList = processNode.childNodes;
                for (let j = 0, childLen = processNodeChildList.length; j < childLen; j++) {
                    let childNode = processNodeChildList[j];
                    if (childNode.nodeType === Node.ELEMENT_NODE) {
                        elementData.data[childNode.nodeName] = childNode.nodeValue;
                        let nodeValue = '';
                        if (childNode.childNodes && childNode.childNodes[0]) {
                            if (childNode.childNodes[0].nodeType === Node.CDATA_SECTION_NODE) {
                                nodeValue = childNode.childNodes[0].nodeValue;
                            } else {
                                nodeValue = childNode.childNodes[0].nodeValue.split(',');
                            }
                        }
                        elementData.data[childNode.nodeName] = nodeValue;
                    }
                }

                let isMatched = false;
                for (let j = 0, diagramNodeLen = diagramNodeList.length; j < diagramNodeLen; j++) {
                    let diagramNode = diagramNodeList[i];
                    if (diagramNode.nodeType === Node.ELEMENT_NODE) {
                        let diagramNodeAttributes = diagramNode.attributes;
                        for (let idx = 0, attrLen = diagramNodeAttributes.length; idx < attrLen; idx++) {
                            let attr = diagramNodeAttributes[idx];
                            if (attr.nodeName === 'id' && elementData.id === attr.nodeValue) {
                                isMatched = true;
                                break;
                            }
                        }
                        if (isMatched) {
                            for (let idx = 0, attrLen = diagramNodeAttributes.length; idx < attrLen; idx++) {
                                let attr = diagramNodeAttributes[idx];
                                if (attr.nodeName !== 'id') {
                                    let valueArr = attr.nodeValue.split(',');
                                    if (valueArr.length > 1) {
                                        let valueNumberArr = [];
                                        valueArr.forEach(function(val, index) {
                                            valueNumberArr[index] = Number(val);
                                        });
                                        elementData.display[attr.nodeName] = valueNumberArr;
                                    } else {
                                        elementData.display[attr.nodeName] = Number(attr.nodeValue);
                                    }

                                }
                            }
                            break;
                        }
                    }
                }
                processData.elements.push(elementData);
            }
        }
        return processData;
    }

    /**
     * XML 파싱오류 확인.
     *
     * @param parsedDocument
     * @return {boolean} 파싱오류 여부
     */
    function isParseError(parsedDocument) {
        let parser = new DOMParser(),
            errorneousParse = parser.parseFromString('<', 'text/xml'),
            parsererrorNS = errorneousParse.getElementsByTagName('parsererror')[0].namespaceURI;
        if (parsererrorNS === 'http://www.w3.org/1999/xhtml') {
            return parsedDocument.getElementsByTagName('parsererror').length > 0;
        }
        return parsedDocument.getElementsByTagNameNS(parsererrorNS, 'parsererror').length > 0;
    }

    /**
     * 프로세스 저장처리.
     *
     * @param processData 프로세스 데이터
     */
    function saveProcess(processData) {
        let result = false;
        aliceJs.sendXhr({
            method: 'POST',
            async: false,
            url: '/rest/processes' + '?saveType=saveas',
            callbackFunc: function(xhr) {
                if (xhr.responseText !== '') {
                    result = true;
                }
            },
            contentType: 'application/json; charset=utf-8',
            params: JSON.stringify(processData)
        });
        return result;
    }

    /**
     * 필수 속성값 확인 후 속성으로 추가.
     *
     * @param processData 프로세스 데이터
     */
    function addRequiredAttribute(processData) {
        /**
         * load element attribute data.
         */
        aliceJs.sendXhr({
            method: 'GET',
            async: false,
            url: '/assets/js/process/elementAttribute.json',
            callbackFunc: function(xhr) {
                const elementAttributes = JSON.parse(xhr.responseText),
                      elementsKeys = Object.getOwnPropertyNames(elementAttributes),
                      elements = processData.elements;
                elements.forEach(function(element) {
                    let requiredDataArr = [];
                    for (let j = 0, keyLen = elementsKeys.length; j < keyLen; j++) {
                        let elementTypeData = elementAttributes[elementsKeys[j]].filter(function(e) {
                            return e.type === element.type;
                        });
                        if (elementTypeData.length) {
                            let attributeList = elementTypeData[0].attribute;
                            attributeList.forEach(function(attr) {
                                let items = attr.items;
                                items.forEach(function(item) {
                                    if (item.required === 'Y') { requiredDataArr.push(item.id); }
                                });
                            });
                        }
                        if (requiredDataArr.length) {
                            break;
                        }
                    }
                    element.required = requiredDataArr;
                });
            },
            contentType: 'application/json; charset=utf-8',
            showProgressbar: false
        });
    }

    /**
     * export process.
     *
     * @param processId 프로세스ID
     */
    function exportProcess(processId) {
        /**
         * load process data.
         */
        aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/processes/' + processId + '/data',
            callbackFunc: function(xhr) {
                const processData = JSON.parse(xhr.responseText);
                console.debug(processData);
                aliceJs.sendXhr({
                    method: 'GET',
                    url: '/rest/codes/version.workflow',
                    callbackFunc: function(xhr) {
                        let versionInfo = JSON.parse(xhr.responseText);
                        let xmlString = createProcessXMLString(processData, versionInfo.codeValue);
                        downloadXML(processId, xmlString);
                    },
                    contentType: 'application/json; charset=utf-8',
                    showProgressbar: false
                });
            },
            contentType: 'application/json; charset=utf-8'
        });
    }

    /**
     * 프로세스 데이터로 XML 형식의 문자열을 만들어 반환 한다.
     *
     * @param processData 프로세스 데이터
     * @param version workflow version
     * @return {string} XML 문자열
     */
    function createProcessXMLString(processData, version) {
        const xmlDoc = document.implementation.createDocument('', '', null);
        let definitions = xmlDoc.createElement('definitions', );
        let process = xmlDoc.createElement('process');
        process.setAttribute('workflow-version', version);
        process.setAttribute('id', processData.process.id);
        process.setAttribute('name', processData.process.name);
        process.setAttribute('description', processData.process.description);

        const elements = processData.elements;
        elements.forEach(function(element) {
            let elementNode = xmlDoc.createElement(element.type);
            elementNode.setAttribute('id', element.id);
            if (typeof element.data.name !== 'undefined') {
                elementNode.setAttribute('name', element.data.name);
            }
            if (typeof element.data.description !== 'undefined') {
                elementNode.setAttribute('description', element.data.description);
            }
            let keys = Object.keys(element.data);
            keys = keys.filter(function(key) { return key !== 'name' && key !== 'description'; });
            keys.forEach(function(key) {
                let attributeNode = xmlDoc.createElement(key);
                if (element.data[key]) {
                    if (Array.isArray(element.data[key])) {
                        let data = xmlDoc.createTextNode(element.data[key]);
                        attributeNode.appendChild(data);
                    } else {
                        let cdata = xmlDoc.createCDATASection(element.data[key]);
                        attributeNode.appendChild(cdata);
                    }
                }
                elementNode.appendChild(attributeNode);
            });
            process.appendChild(elementNode);
        });
        definitions.appendChild(process);

        let diagram = xmlDoc.createElement('diagram');
        elements.forEach(function(element) {
            let elementTagName = 'shape'
            if (element.type === 'arrowConnector') {
                elementTagName = 'connector';
            }
            let elementNode = xmlDoc.createElement(elementTagName);
            elementNode.setAttribute('id', element.id);
            let keys = Object.keys(element.display);
            keys.forEach(function(key) {
                elementNode.setAttribute(key, element.display[key]);
            });
            diagram.appendChild(elementNode);
        });
        definitions.appendChild(diagram);

        xmlDoc.appendChild(definitions);

        let serializer = new XMLSerializer();
        return serializer.serializeToString(xmlDoc);
    }

    /**
     * download.
     *
     * @param processId 프로세스ID
     * @param xmlString XML string
     */
    function downloadXML(processId, xmlString) {
        let filename = 'process_' + processId + '.xml';
        let pom = document.createElement('a');
        let bb = new Blob(['<?xml version="1.0" encoding="UTF-8"?>' + xmlString], {type: 'text/plain'});
        pom.setAttribute('href', window.URL.createObjectURL(bb));
        pom.setAttribute('download', filename);
        pom.dataset.downloadurl = ['text/plain', pom.download, pom.href].join(':');
        pom.draggable = true;
        pom.click();
    }

    exports.initImport = initImport;
    exports.import = importProcess;
    exports.export = exportProcess;
    Object.defineProperty(exports, '__esModule',{value: true});
})));