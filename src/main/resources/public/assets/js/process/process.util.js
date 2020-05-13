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
        const saveAsProcessData = {};

        const elements = saveAsProcessData.elements;
        elements.forEach(function(element) {
            //const category = getElementCategory(element.type);
            //element['required'] = getAttributeRequired(category, element.type);
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
        aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/processes/' + processId + '/data',
            callbackFunc: function(xhr) {
                JSON.parse(xhr.responseText);
            },
            contentType: 'application/json; charset=utf-8'
        });
    }

    exports.import = importProcess;
    exports.export = exportProcess;
    Object.defineProperty(exports, '__esModule',{value: true});
})));