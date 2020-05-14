(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.aliceProcessUtil = global.aliceProcessUtil || {})));
}(this, (function (exports) {
    'use strict';

    /**
     * import process.
     */
    function importProcess() {
        console.log('import');
        /**
         * load element attribute data.
         */

        aliceJs.sendXhr({
            method: 'GET',
            async: false,
            url: '/assets/js/process/elementAttribute.json',
            callbackFunc: function(xhr) {
                JSON.parse(xhr.responseText);
            },
            contentType: 'application/json; charset=utf-8',
            showProgressbar: false
        });
        const saveAsProcessData = {};

        const elements = saveAsProcessData.elements;
        elements.forEach(function(element) {
            // const category = getElementCategory(element.type);
            // element['required'] = getAttributeRequired(category, element.type);
        });

        aliceJs.sendXhr({
            method: 'POST',
            url: '/rest/processes' + '?saveType=saveas',
            callbackFunc: function(xhr) {
                if (xhr.responseText !== '') {
                    aliceJs.alert(i18n.get('common.msg.save'));
                } else {
                    aliceJs.alert(i18n.get('common.label.fail'));
                }
            },
            contentType: 'application/json; charset=utf-8',
            params: JSON.stringify(saveAsProcessData)
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
                let xmlString = createProcessXMLString(processData);

                // download
                let filename = 'process_' + processId + '.xml';
                let pom = document.createElement('a');
                let bb = new Blob(['<?xml version="1.0" encoding="UTF-8"?>' + xmlString], {type: 'text/plain'});
                pom.setAttribute('href', window.URL.createObjectURL(bb));
                pom.setAttribute('download', filename);
                pom.dataset.downloadurl = ['text/plain', pom.download, pom.href].join(':');
                pom.draggable = true;

                pom.click();
            },
            contentType: 'application/json; charset=utf-8'
        });
    }

    /**
     * 프로세스 데이터로 XML 형식의 문자열을 만들어 반환한다.
     *
     * @param processData 프로세스 데이터
     * @return {string} XML 문자열
     */
    function createProcessXMLString(processData) {
        const xmlDoc = document.implementation.createDocument('', '', null);
        let definitions = xmlDoc.createElement('definitions', );
        let process = xmlDoc.createElement('process');
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
                    let cdata = xmlDoc.createCDATASection(element.data[key]);
                    attributeNode.appendChild(cdata);
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

    exports.import = importProcess;
    exports.export = exportProcess;
    Object.defineProperty(exports, '__esModule',{value: true});
})));