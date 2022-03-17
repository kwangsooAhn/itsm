const ZWorkflowUtil = {};

/**
 * generate UUID.
 * Public Domain/MIT
 *
 * <HISTORY>
 * UUID 의 첫 번째가 숫자로 시작할 경우,
 * querySelector 로 id 검색 사용 시 오류 발생하여
 * 첫 번째 글자를 alice 를 의미하는 'a'로 시작하도록 고정함.
 *
 * @returns {string} UUID
 */
ZWorkflowUtil.generateUUID = function() {
    let d = new Date().getTime(); //Timestamp
    //Time in microseconds since page-load or 0 if unsupported
    let d2 = (performance && performance.now && (performance.now() * 1000)) || 0;
    return 'axxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx'.replace(/[x]/g, function(c) {
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
};

/**
 * 두 개의 json 데이터가 동일한 지 비교한 후 boolean 을 리턴 한다.
 *
 * @param obj1 비교 대상 JSON 데이터 1
 * @param obj2 비교 대상 JSON 데이터 2
 * @return {boolean} 데이터 일치 여부 (true: 일치, false: 불일치)
 */
ZWorkflowUtil.compareJson = function(obj1, obj2) {
    if (obj1 === null && obj2 === null) {
        return true;
    } else if (obj1 === null || obj2 === null) {
        return false;
    } else if (typeof obj1 === 'boolean' && typeof obj2 === 'boolean') {
        return (obj1 === obj2);
    }
    if (!Object.keys(obj2).every(function(key) { return Object.prototype.hasOwnProperty.call(obj1, key); })) {
        return false;
    }

    return Object.keys(obj1).every(function(key) {
        if ((typeof obj1[key] === 'object') && (typeof obj2[key] === 'object')) {
            return ZWorkflowUtil.compareJson(obj1[key], obj2[key]);
        } else {
            return obj1[key] === obj2[key];
        }
    });
};

/**
 * polyfill.
 */
ZWorkflowUtil.polyfill = function() {
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
};

/**
 * Object 를 XML 구조로 변경한다.
 *
 * @param data 데이터
 * @return {string} XML 문자열
 */
ZWorkflowUtil.NULL_CHECK_TYPE = ['group', 'tags'];
ZWorkflowUtil.objectToXML = function(data) {
    let xml = '';
    Object.keys(data).forEach(function(key) {
        // id는 속성으로 처리하고 나머지는 엘리먼트로 처리한다.
        if (key === 'id') { return; }
        xml += Array.isArray(data[key]) ? '' : '<' + key + '>';

        if (data[key] && Array.isArray(data[key])) {
            Object.keys(data[key]).forEach(function (item) {
                // id가 존재하면 id를 속성으로 처리한다.
                if (Object.prototype.hasOwnProperty.call(data[key][item], 'id')) {
                    xml += '<' + key + ' id="' + data[key][item]['id'] + '">';
                } else {
                    xml += '<' + key + '>';
                }
                xml += ZWorkflowUtil.objectToXML(data[key][item]);
                xml += '</' + key + '>';
            });
            // 빈 배열일 경우에도 태그 추가
            if (data[key].length === 0) {
                if (ZWorkflowUtil.NULL_CHECK_TYPE.includes(key)) {
                    xml += '<' + key + '></' + key + '>';
                } else {
                    xml += '<' + key + '><![CDATA[]]></' + key + '>';
                }
            }
        } else if (data[key] && typeof data[key] === 'object') {
            xml += ZWorkflowUtil.objectToXML(data[key]);
        } else if (data[key] === null) {
            xml += 'null';
        } else {
            xml += '<![CDATA[' + data[key] + ']]>';
        }

        xml += Array.isArray(data[key]) ? '' : '</' + key + '>';
    });
    return xml;
};

/**
 * 폼 데이터로 XML 형식의 문자열을 만들어 반환 한다.
 *
 * @param formData 폼 데이터
 * @param version workflow version
 * @return {string} XML 문자열
 */
ZWorkflowUtil.createFormXMLString = function(formData, version) {
    const serializer = new XMLSerializer();
    const xmlDoc = document.implementation.createDocument('', '', null);
    // 세부 정보
    const definitions = xmlDoc.createElement('definitions');
    const form = xmlDoc.createElement('form');
    form.setAttribute('version', version); // 버전
    form.setAttribute('id', formData.id);
    form.innerHTML = ZWorkflowUtil.objectToXML(formData);
    definitions.appendChild(form);

    xmlDoc.appendChild(definitions);
    return serializer.serializeToString(xmlDoc);
};

/**
 * 프로세스 데이터로 XML 형식의 문자열을 만들어 반환 한다.
 *
 * @param processData 프로세스 데이터
 * @param version workflow version
 * @return {string} XML 문자열
 */
ZWorkflowUtil.createProcessXMLString = function(processData, version) {
    const xmlDoc = document.implementation.createDocument('', '', null);
    let definitions = xmlDoc.createElement('definitions');
    let process = xmlDoc.createElement('process');
    process.setAttribute('workflow-version', version);
    process.setAttribute('id', processData.process.id);
    process.setAttribute('name', processData.process.name);
    process.setAttribute('description', processData.process.description);

    const elements = processData.elements;
    elements.forEach(function(element) {
        let elementNode = xmlDoc.createElement(element.type);
        elementNode.setAttribute('id', element.id);
        elementNode.setAttribute('name', element.name);
        elementNode.setAttribute('notification', element.notification);
        elementNode.setAttribute('description', element.description);
        let keys = Object.keys(element.data);
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
        let elementTagName = 'shape';
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
};

/**
 * 업무흐름 데이터로 XML 형식의 문자열을 만들어 반환 한다.
 * @param workflowData 업무흐름 데이터
 * @param version workflow version
 * @return {string} XML 문자열
 */
ZWorkflowUtil.createDocumentXMLString = function (workflowData, version) {
    const serializer = new XMLSerializer();
    const xmlDoc = document.implementation.createDocument('', '', null);
    // 세부 정보
    const definitions = xmlDoc.createElement('definitions');
    const workflow = xmlDoc.createElement('workflow');
    workflow.setAttribute('version', version); // 버전
    workflow.setAttribute('id', workflowData.documentId);
    definitions.appendChild(workflow);

    // 폼 : ZWorkflowUtil.createFormXMLString 와 구조 동일
    const form = xmlDoc.createElement('form');
    form.setAttribute('id', workflowData.form.id);
    form.innerHTML = ZWorkflowUtil.objectToXML(workflowData.form);
    workflow.appendChild(form);

    // 프로세스 : ZWorkflowUtil.createProcessXMLString 와 구조 동일
    const process = xmlDoc.createElement('process');
    process.setAttribute('id', workflowData.process.id);
    process.setAttribute('name', workflowData.process.name);
    process.setAttribute('description', workflowData.process.description);
    const diagram = xmlDoc.createElement('diagram');
    workflowData.elements.forEach(function(element) {
        // 프로세스 엘리먼트
        const elementNode = xmlDoc.createElement(element.type);
        elementNode.setAttribute('id', element.id);
        elementNode.setAttribute('name', element.name);
        elementNode.setAttribute('notification', element.notification);
        elementNode.setAttribute('description', element.description);
        const keys = Object.keys(element.data);
        keys.forEach(function(key) {
            const attributeNode = xmlDoc.createElement(key);
            if (element.data[key]) {
                if (Array.isArray(element.data[key])) {
                    const data = xmlDoc.createTextNode(element.data[key]);
                    attributeNode.appendChild(data);
                } else {
                    const cdata = xmlDoc.createCDATASection(element.data[key]);
                    attributeNode.appendChild(cdata);
                }
            }
            elementNode.appendChild(attributeNode);
        });
        process.appendChild(elementNode);
        // 프로세스 다이어그램
        const diagramTagName = (element.type === 'arrowConnector') ? 'connector' : 'shape';
        const diagramNode = xmlDoc.createElement(diagramTagName);
        diagramNode.setAttribute('id', element.id);
        const diagramKeys = Object.keys(element.display);
        diagramKeys.forEach(function(key) {
            diagramNode.setAttribute(key, element.display[key]);
        });
        diagram.appendChild(diagramNode);
    });
    workflow.appendChild(process);
    workflow.appendChild(diagram);

    // 신청서 편집 양식
    const docDisplay = xmlDoc.createElement('documentDisplay');
    workflowData.displays.forEach(function (display) {
        const progressNode = xmlDoc.createElement('progress');
        progressNode.innerHTML = ZWorkflowUtil.objectToXML(display);
        docDisplay.appendChild(progressNode);
    });
    workflow.appendChild(docDisplay);

    definitions.appendChild(workflow);
    xmlDoc.appendChild(definitions);
    return serializer.serializeToString(xmlDoc);
};

/**
 * download.
 *
 * @param id ID
 * @param suffix 구분자
 * @param xmlString XML string
 */
ZWorkflowUtil.downloadXML = function(id, suffix, xmlString) {
    const fileName = suffix + '_' + id + '.xml';
    let pom = document.createElement('a');
    let bb = new Blob(['<?xml version="1.0" encoding="UTF-8"?>' + xmlString], {type: 'text/plain'});
    pom.setAttribute('href', window.URL.createObjectURL(bb));
    pom.setAttribute('download', fileName);
    pom.dataset.downloadurl = ['text/plain', pom.download, pom.href].join(':');
    pom.draggable = true;
    pom.click();
};

/**
 * export form, process, document
 *
 * @param id form/process/document ID
 * @param url 경로
 * @param type form/process/workflow
 */
ZWorkflowUtil.export = async function(id, url, type) {
    // 버전 정보
    const version = await aliceJs.fetchJson('/rest/codes/version.workflow', { method: 'GET' });
    
    // XML 추출
    aliceJs.fetchJson(url, {
        method: 'GET'
    }).then((data) => {
        if (Object.prototype.hasOwnProperty.call(data, 'error')) {
            zAlert.danger(i18n.msg('form.msg.failedExport'));
            return false;
        }
        let xmlString = '';
        switch (type) {
            case 'form':
                xmlString = ZWorkflowUtil.createFormXMLString(data, version.codeValue);
                break;
            case 'process':
                xmlString = ZWorkflowUtil.createProcessXMLString(data, version.codeValue);
                break;
            case 'workflow':
                xmlString = ZWorkflowUtil.createDocumentXMLString(data, version.codeValue);
                break;
        }
        ZWorkflowUtil.downloadXML(id, type, xmlString);
    });
};

/**
 * XML 파싱오류 확인.
 *
 * @param parsedDocument
 * @return {boolean} 파싱오류 여부
 */
ZWorkflowUtil.isParseError = function(parsedDocument) {
    let parser = new DOMParser(),
        errorneousParse = parser.parseFromString('<', 'text/xml'),
        parsererrorNS = errorneousParse.getElementsByTagName('parsererror')[0].namespaceURI;
    if (parsererrorNS === 'http://www.w3.org/1999/xhtml') {
        return parsedDocument.getElementsByTagName('parsererror').length > 0;
    }
    return parsedDocument.getElementsByTagNameNS(parsererrorNS, 'parsererror').length > 0;
};

/**
 * 필수 속성값 확인 후 속성으로 추가.
 *
 * @param processData 프로세스 데이터
 */
ZWorkflowUtil.addRequiredProcessAttribute = function(processData) {
    aliceJs.fetchJson('/assets/js/process/elementAttribute.json', {
        method: 'GET'
    }).then((elementAttributes) => {
        const elementsKeys = Object.getOwnPropertyNames(elementAttributes),
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
    });
};

/**
 * XML 를 Object 구조로 변경한다.
 *
 * @param data XML 파일 데이터
 * @return string 데이터
 */
ZWorkflowUtil.FORM_ARRARY_TYPE = ['group', 'row', 'component', 'tags', 'options', 'columns'];
ZWorkflowUtil.XMLToObject = function(data) {
    if (data.nodeType === Node.ELEMENT_NODE) { // element
        let obj = {};
        if (data.attributes.length > 0) { // 속성 정보도 object 데이터로 추가한다.
            for (let i = 0, len = data.attributes.length; i < len; i++) {
                let attr = data.attributes.item(i);
                if (attr.nodeName === 'version') { continue; } // xml 버전은 제외
                obj[attr.nodeName] = attr.nodeValue;
            }
        }
        if (data.hasChildNodes()) {
            for (let j = 0, cLen = data.childNodes.length; j < cLen; j++) {
                let item = data.childNodes.item(j);
                if (item.nodeType === Node.ELEMENT_NODE) {
                    let nodeName = item.nodeName;
                    if (typeof (obj[nodeName]) === 'undefined') {
                        // group, row, component, columns 등 복수 처리가 필요한 경우 배열로 넘긴다.
                        if (ZWorkflowUtil.FORM_ARRARY_TYPE.includes(nodeName) && !Array.isArray(obj[nodeName])) {
                            let arr = ZWorkflowUtil.XMLToObject(item);
                            obj[nodeName] = [];
                            obj[nodeName].push(arr);
                        } else {
                            obj[nodeName] = ZWorkflowUtil.XMLToObject(item);
                        }
                    } else {
                        if (typeof (obj[nodeName].push) === 'undefined') {
                            let arr = obj[nodeName];
                            obj[nodeName] = [];
                            obj[nodeName].push(arr);
                        }
                        obj[nodeName].push(ZWorkflowUtil.XMLToObject(item));
                    }
                } else if (item.nodeType === Node.CDATA_SECTION_NODE) { // <!CDATA[[ … ]]>.
                    obj = ['true', 'false'].includes(item.nodeValue) ? (item.nodeValue === 'true') : item.nodeValue;
                } else if (item.nodeType === Node.TEXT_NODE && item.nodeValue.trim() === 'null') {
                    obj = null;
                }
            }
        }
        return obj;
    } else if (data.nodeType === Node.TEXT_NODE) {
        return data.nodeValue;
    } else {
        return '';
    }
};

/**
 * 파일 데이터로부터 폼 정보를 읽어 json 구조로 만들어 반환 한다.
 * @param data XML 파일 데이터
 * @returns json
 */
ZWorkflowUtil.loadFormFromXML = function (data) {
    const parser = new DOMParser();
    const resultType = XPathResult.ANY_UNORDERED_NODE_TYPE;
    const xmlDoc = parser.parseFromString(data, 'application/xml');
    if (ZWorkflowUtil.isParseError(xmlDoc)) {
        throw new Error('Error parsing XML');
    }

    const form = xmlDoc.evaluate('/definitions/form', xmlDoc, null, resultType, null);
    return ZWorkflowUtil.XMLToObject(form.singleNodeValue);
};

/**
 * 파일 데이터로부터 프로세스 정보를 읽어 json 구조로 만들어 반환 한다.
 *
 * @param data XML 파일 데이터
 * <?xml version="1.0" encoding="UTF-8"?>
 * <definitions>
 *     <process workflow-version="버전" id="프로세스ID"" name="" description="">
 *         <commonStart id="엘리먼트ID" name="" notification="" description=""/>
 *         ...
 *     </process>
 *     <diagram>
 *         <shape id="엘리먼트ID" width="40" height="40" position-x="80" position-y="230"/>
 *         ...
 *     </diagram>
 * <definitions>
 * @return {{elements: []}}
 */
ZWorkflowUtil.loadProcessFromXML = function(data) {
    const parser = new DOMParser();
    const resultType = XPathResult.ANY_UNORDERED_NODE_TYPE;
    const xmlDoc = parser.parseFromString(data, 'text/xml');
    if (ZWorkflowUtil.isParseError(xmlDoc)) {
        throw new Error('Error parsing XML');
    }

    let processData = {elements: []};
    //프로세스의 XML 구조는 데이터를 담당하는 process와 element 위치 정보를 담는 diagram으로 나뉜다.
    let processNode = xmlDoc.evaluate('/definitions/process', xmlDoc, null, resultType, null);
    let diagramNode = xmlDoc.evaluate('/definitions/diagram', xmlDoc, null, resultType, null);
    let processNodeList = processNode.singleNodeValue.childNodes;
    let diagramNodeList = diagramNode.singleNodeValue.childNodes;
    for (let i = 0, len = processNodeList.length; i < len; i++) {
        let processNode = processNodeList[i];
        if (processNode.nodeType === Node.ELEMENT_NODE) {
            let elementData = {type: processNode.nodeName, data: {}, display: {}};

            let processNodeAttributes = processNode.attributes;
            for (let j = 0, attrLen = processNodeAttributes.length; j < attrLen; j++) {
                let attr = processNodeAttributes[j];
                elementData[attr.nodeName] = attr.nodeValue;
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
};

/**
 * import form, process.
 *
 * @param xmlFile XML 파일
 * @param data IMPORT form/process 정보
 * @param type form/process
 * @param callbackFunc IMPORT 처리 후 호출될 function
 */
ZWorkflowUtil.import = function(xmlFile, data, type, callbackFunc) {
    try {
        const reader = new FileReader();
        reader.addEventListener('load', function(e) {
            let saveData = {};
            let saveUrl = '';
            let result = false;
            if (type === xmlFile.name.split('_')[0]) {
                switch (type) {
                    case 'form':
                        saveData = ZWorkflowUtil.loadFormFromXML(e.target.result);
                        saveData.name = data.formName;
                        saveData.desc = data.formDesc;
                        saveUrl = '/rest/forms?saveType=saveas';
                        break;
                    case 'process':
                        saveData = ZWorkflowUtil.loadProcessFromXML(e.target.result);
                        saveData.process = {name: data.processName, description: data.processDesc};
                        ZWorkflowUtil.addRequiredProcessAttribute(saveData);
                        saveUrl = '/rest/processes?saveType=saveas';
                        break;
                    default: //none
                }
                console.debug(saveData);
                aliceJs.fetchText(saveUrl, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(saveData)
                }).then((response) => {
                    if (type === 'process') {
                        let resultToJson = JSON.parse(response);
                        result = resultToJson.result;
                    } else {
                        result = response;
                    }

                    if (typeof callbackFunc === 'function') {
                        callbackFunc(result);
                    }
                });
                return result;
            } else {
                zAlert.danger(i18n.msg('validation.msg.checkImportFormat'));
                return false;
            }
        });
        reader.readAsText(xmlFile, 'utf-8');
    } catch (e) {
        console.error(e);
    }
};
