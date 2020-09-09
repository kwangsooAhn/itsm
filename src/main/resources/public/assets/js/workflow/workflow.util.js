const workflowUtil = {};

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
workflowUtil.generateUUID = function() {
    let d = new Date().getTime(); //Timestamp
    let d2 = (performance && performance.now && (performance.now() * 1000)) || 0; //Time in microseconds since page-load or 0 if unsupported
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
workflowUtil.compareJson = function(obj1, obj2) {
    if (obj1 === null && obj2 === null) {
        return true;
    } else if (obj1 === null || obj2 === null) {
        return false;
    }

    if (!Object.keys(obj2).every(function(key) { return obj1.hasOwnProperty(key); })) {
        return false;
    }
    return Object.keys(obj1).every(function(key) {
        if ((typeof obj1[key] === 'object') && (typeof obj2[key] === 'object')) {
            return workflowUtil.compareJson(obj1[key], obj2[key]);
        } else {
            return obj1[key] === obj2[key];
        }
    });
};

/**
 * polyfill.
 */
workflowUtil.polyfill = function() {
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
workflowUtil.ObjectToXML = function(data) {
    let xml = '';
    Object.keys(data).forEach(function(key) {
        xml += Array.isArray(data[key]) ? '' : '<' + key + '>';
        if (data[key] && Array.isArray(data[key])) {
            Object.keys(data[key]).forEach(function (item) {
                xml += '<' + key + '>';
                xml += workflowUtil.ObjectToXML(data[key][item]);
                xml += '</' + key + '>';
            });
        } else if (data[key] && typeof data[key] == 'object') {
            xml += workflowUtil.ObjectToXML(data[key]);
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
workflowUtil.createFormXMLString = function(formData, version) {
    const xmlDoc = document.implementation.createDocument('', '', null);
    let definitions = xmlDoc.createElement('definitions');
    let form = xmlDoc.createElement('form');
    form.setAttribute('workflow-version', version);
    form.setAttribute('id', formData.formId);
    form.setAttribute('name', formData.name);
    form.setAttribute('description', formData.desc);

    for (let i = 0, len = formData.components.length; i < len; i ++) {
        let componentProp = formData.components[i];
        let componentNode = xmlDoc.createElement('component');
        componentNode.setAttribute('id', componentProp.componentId);
        delete componentProp.componentId;
        componentNode.setAttribute('type', componentProp.type);
        delete  componentProp.type;
        if (typeof componentProp == 'object') {
            componentNode.innerHTML = workflowUtil.ObjectToXML(componentProp);
        }
        form.appendChild(componentNode);
    }
    definitions.appendChild(form);
    xmlDoc.appendChild(definitions);
    let serializer = new XMLSerializer();
    return serializer.serializeToString(xmlDoc);
};

/**
 * 프로세스 데이터로 XML 형식의 문자열을 만들어 반환 한다.
 *
 * @param processData 프로세스 데이터
 * @param version workflow version
 * @return {string} XML 문자열
 */
workflowUtil.createProcessXMLString = function(processData, version) {
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
 * download.
 *
 * @param id ID
 * @param suffix 구분자
 * @param xmlString XML string
 */
workflowUtil.downloadXML = function(id, suffix,  xmlString) {
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
 * export form , process.
 * 
 * @param id form/process ID
 * @param type form/process
 */
workflowUtil.export = function(id, type) {
    let exportUrl = '/rest/forms/' + id + '/data';
    if (type === 'process') {
        exportUrl = '/rest/processes/' + id + '/data';
    }
    aliceJs.sendXhr({
        method: 'GET',
        url: exportUrl,
        callbackFunc: function(xhr) {
            const data = JSON.parse(xhr.responseText);
            console.debug(data);
            aliceJs.sendXhr({
                method: 'GET',
                url: '/rest/codes/version.workflow',
                callbackFunc: function(xhr) {
                    let versionInfo = JSON.parse(xhr.responseText);
                    let xmlString = '';
                    if (type === 'form') {
                        xmlString = workflowUtil.createFormXMLString(data, versionInfo.codeValue);
                    } else if (type === 'process') {
                        xmlString = workflowUtil.createProcessXMLString(data, versionInfo.codeValue);
                    }
                    workflowUtil.downloadXML(id, type, xmlString);
                },
                contentType: 'application/json; charset=utf-8',
                showProgressbar: false
            });
        },
        contentType: 'application/json; charset=utf-8'
    });
};

/**
 * XML 파싱오류 확인.
 *
 * @param parsedDocument
 * @return {boolean} 파싱오류 여부
 */
workflowUtil.isParseError = function(parsedDocument) {
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
workflowUtil.addRequiredProcessAttribute = function(processData) {
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
};

/**
 * XML 를 Object 구조로 변경한다.
 *
 * @param data XML 파일 데이터
 * @return string 데이터
 */
workflowUtil.XMLToObject = function(data) {
    let obj = '';
    if (data.nodeType === Node.ELEMENT_NODE) { // element
        obj = {};
        if (data.attributes.length > 0) { // 속성 정보도 object 데이터로 추가한다.
            for (let i = 0, len = data.attributes.length; i < len; i++) {
                let attr = data.attributes.item(i);
                obj[attr.nodeName] = attr.nodeValue;
            }
        }
    } else if (data.nodeType === Node.TEXT_NODE) {
        obj = data.nodeValue;
    }
    if (data.hasChildNodes()) {
        for (let j = 0, cLen = data.childNodes.length; j < cLen; j++) {
            let item = data.childNodes.item(j);
            if (item.nodeType === Node.ELEMENT_NODE) {
                let nodeName = item.nodeName;
                if (typeof (obj[nodeName]) === 'undefined') {
                    obj[nodeName] = workflowUtil.XMLToObject(item);
                } else {
                    if (typeof (obj[nodeName].push) === 'undefined') {
                        let arr = obj[nodeName];
                        obj[nodeName] = [];
                        obj[nodeName].push(arr);
                    }
                    obj[nodeName].push(workflowUtil.XMLToObject(item));
                }
            } else if (item.nodeType === Node.CDATA_SECTION_NODE) {
                if (item.nodeValue === 'false' || item.nodeValue === 'true') { //boolean 값
                    obj = (item.nodeValue === 'true');
                } else {
                    obj = item.nodeValue;
                }
            }
        }
    }
    return obj;
};

/**
 * 파일 데이터로부터 폼 정보를 읽어 json 구조로 만들어 반환 한다.
 * @param data XML 파일 데이터
 * <?xml version="1.0" encoding="UTF-8"?>
 * <definitions>
 *     <form workflow-version="버전" id="폼ID"" name="" description="">
 *         <component id="컴포넌트ID" type="select">
 *             <dataAttribute>
 *                 <displayType/>
 *                 <mappingId/>
 *                 <isTopic>true</isTopic>
 *             </dataAttribute>
 *             <label>...</label>
 *             <display>...</display>
 *             <option>
 *                 <seq>1</seq>
 *                 <name>Option</name>
 *                 <value>Option</value>
 *             </option>
 *             <option></option>
 *         </component>
 *         ...
 *     </form>
 * <definitions>
 * @returns {{components: []}}
 */
workflowUtil.loadFormFromXML = function(data) {
    const parser = new DOMParser();
    const resultType = XPathResult.ANY_UNORDERED_NODE_TYPE;
    const xmlDoc = parser.parseFromString(data,'text/xml');
    if (workflowUtil.isParseError(xmlDoc)) {
        throw new Error('Error parsing XML');
    }
    
    let formData = {components: []};
    let formNode = xmlDoc.evaluate('/definitions/form', xmlDoc, null, resultType, null);
    let formNodeList = formNode.singleNodeValue.childNodes;
    for (let i = 0, len = formNodeList.length; i < len; i++) {
        let componentNode = formNodeList[i];
        if (componentNode.nodeType === Node.ELEMENT_NODE) {
            let componentData = workflowUtil.XMLToObject(componentNode);
            Object.defineProperty(componentData, 'componentId', Object.getOwnPropertyDescriptor(componentData, 'id'));
            delete componentData['id'];
            // 옵션이 배열이 아닐 경우 배열에 넣어준다.
            if (typeof componentData['option'] !== 'undefined' && !Array.isArray(componentData['option'])) {
                componentData['option'] = [componentData['option']];
            }
            formData.components.push(componentData);
        }
    }
    //display order는 Number로 처리해야한다.
    formData.components = formData.components.filter(function(comp) {
        return comp.display.order = Number(comp.display.order);
    });
    return formData;
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
workflowUtil.loadProcessFromXML = function(data) {
    const parser = new DOMParser();
    const resultType = XPathResult.ANY_UNORDERED_NODE_TYPE;
    const xmlDoc = parser.parseFromString(data,'text/xml');
    if (workflowUtil.isParseError(xmlDoc)) {
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
 * import form, process 데이터 저장.
 *
 * @param type form/process
 * @param data form/process 데이터
 */
workflowUtil.saveImportData = function(type, data) {
    let result = false;
    let saveUrl = '/rest/forms-admin' + '?saveType=saveas';
    if (type === 'process') {
        saveUrl = '/rest/processes-admin' + '?saveType=saveas';
    }
    aliceJs.sendXhr({
        method: 'POST',
        async: false,
        url: saveUrl,
        callbackFunc: function(xhr) {
            if (xhr.responseText !== '') {
                result = true;
            }
        },
        contentType: 'application/json; charset=utf-8',
        params: JSON.stringify(data)
    });
    return result;
};

/**
 * import form, process.
 * 
 * @param xmlFile XML 파일
 * @param data IMPORT form/process 정보
 * @param type form/process
 * @param callbackFunc IMPORT 처리 후 호출될 function
 */
workflowUtil.import = function(xmlFile, data, type, callbackFunc) {
    try {
        const reader = new FileReader();
        reader.addEventListener('load', function(e) {
            let saveData = {};
            if (type === xmlFile.name.split('_')[0]) {
                switch (type) {
                    case 'form':
                        saveData = workflowUtil.loadFormFromXML(e.target.result);
                        saveData.name = data.formName;
                        saveData.desc = data.formDesc;
                        break;
                    case 'process':
                        saveData = workflowUtil.loadProcessFromXML(e.target.result);
                        saveData.process = {name: data.processName, description: data.processDesc};
                        console.log(saveData);
                        workflowUtil.addRequiredProcessAttribute(saveData);
                        break;
                    default: //none
                }
                console.debug(saveData);
                let result = workflowUtil.saveImportData(type, saveData);
                if (typeof callbackFunc === 'function') {
                    callbackFunc(result);
                }
            } else {
                aliceJs.alertDanger(i18n.get('validation.msg.checkImportFormat'));
                return false;
            }
        });
        reader.readAsText(xmlFile, 'utf-8');
    } catch (e) {
        console.error(e);
    }
};
