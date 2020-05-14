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
        /*const saveAsProcessData = {};

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
        });*/
    }

    /**
     * export process.
     *
     * @param processId 프로세스ID
     */
    function exportProcess(processId) {
        let attributeData = {};
        /**
         * load element attribute data.
         */
        const loadElementData = function() {
            aliceJs.sendXhr({
                method: 'GET',
                async: false,
                url: '/assets/js/process/elementAttribute.json',
                callbackFunc: function(xhr) {
                    attributeData = JSON.parse(xhr.responseText);
                },
                contentType: 'application/json; charset=utf-8',
                showProgressbar: false
            });
        };

        /**
         * load process data.
         */
        aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/processes/' + processId + '/data',
            callbackFunc: function(xhr) {
                loadElementData();
                const processData = JSON.parse(xhr.responseText);
                console.debug(attributeData);
                console.debug(processData);

                const xmlDoc = document.implementation.createDocument('', '', null);
                let definitions = xmlDoc.createElement('definitions', );
                let process = xmlDoc.createElement('process');
                process.setAttribute('id', processData.process.id);
                process.setAttribute('name', processData.process.name);
                process.setAttribute('description', processData.process.description);
                definitions.append(process);

                let diagram = xmlDoc.createElement('diagram');
                definitions.append(diagram);

                xmlDoc.append(definitions);

                let serializer = new XMLSerializer();
                let xmlString = serializer.serializeToString(xmlDoc);
                console.log(xmlString);
            },
            contentType: 'application/json; charset=utf-8',
            showProgressbar: false
        });
    }

    exports.import = importProcess;
    exports.export = exportProcess;
    Object.defineProperty(exports, '__esModule',{value: true});
})));