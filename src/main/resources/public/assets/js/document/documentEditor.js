(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.documentEditor = global.documentEditor || {})));
}(this, (function (exports) {
    'use strict';

    let documentContainer = null;
    const defaultColWidth = 8.33; //폼 패널을 12등분하였을때, 1개의 너비

    /**
     * add Component.
     *
     * @param compData 컴포넌트.
     */
    function addComponent(compData) {
        const comp = document.createElement('div');
        comp.id = compData.id;
        comp.className = 'component';
        comp.setAttribute('data-type', compData.type);

        const fieldFirstEle = document.createElement('div');
        const fieldLastEle = document.createElement('div');
        fieldFirstEle.className = 'field';
        fieldLastEle.className = 'field';

        const lblData = compData.label;
        const displayData = compData.display;
        const validateData = compData.validate;

        if (lblData !== 'undefined' && lblData.position !== 'hidden') {
            const lblEle = document.createElement('div');
            lblEle.className = 'label';
            lblEle.style.fontSize = lblData.size + 'px';
            lblEle.style.color = lblData.color;
            lblEle.style.fontWeight = (lblData.bold === 'Y') ? 'bold' : '';
            lblEle.style.fontStyle = (lblData.italic === 'Y') ? 'italic' : '';
            lblEle.style.textDecoration = (lblData.underline === 'Y') ? 'underline' : '';
            lblEle.style.textAlign = lblData.align;
            lblEle.innerHTML = lblData.text;

            if (lblData.position === 'left') {
                comp.style.display = 'flex';
                fieldFirstEle.style.flexBasis = (defaultColWidth * Number(lblData.column)) + '%';
                fieldLastEle.style.flexBasis = (defaultColWidth * Number(displayData.column)) + '%';
            }
            fieldFirstEle.appendChild(lblEle);
        }

        //구현 완료 시 삭제.
        console.log('=================================================== ' + compData.type + ' ===================================================');
        console.log(JSON.stringify(displayData));
        console.log(JSON.stringify(validateData));

        //TODO: common[mapping-id] 구현 필요.
        //TODO: validate 처리 필요.
        switch (compData.type) {
            case 'text':
                const textEle = document.createElement('input');
                textEle.type = 'text';
                textEle.placeholder = displayData.placeholder;
                textEle.style.outlineWidth = displayData['outline-width'] + 'px';
                textEle.style.outlineColor = displayData['outline-color'];
                textEle.minLength = validateData['length-min'];
                textEle.maxLength = validateData['length-max'];
                fieldLastEle.appendChild(textEle);
                break;
            case 'textarea':
                const textareaEle = document.createElement('textarea');
                textareaEle.placeholder = displayData.placeholder;
                textareaEle.style.outlineWidth = displayData['outline-width'] + 'px';
                textareaEle.style.outlineColor = displayData['outline-color'];
                textareaEle.minLength = validateData['length-min'];
                textareaEle.maxLength = validateData['length-max'];
                fieldLastEle.appendChild(textareaEle);
                break;
            case 'select':
                const selectEle = document.createElement('select');
                const optData = compData.option;
                optData.sort(function(a, b) {
                    return a.seq - b.seq;
                });
                for (let i = 0; i < optData.length; i++) {
                    const optEle = document.createElement('option');
                    optEle.text = optData[i].name;
                    optEle.value = optData[i].value;
                    selectEle.appendChild(optEle);
                }
                fieldLastEle.appendChild(selectEle);
                break;
            case 'radio':
                const radioOptData = compData.option;
                radioOptData.sort(function(a, b) {
                    return a.seq - b.seq;
                });
                for (let i = 0; i < radioOptData.length; i++) {
                    const divEle = document.createElement('div');
                    if (displayData.direction === 'horizontal') { divEle.style.display = 'inline-block'}
                    const radioEle = document.createElement('input');
                    radioEle.type = 'radio';
                    radioEle.name = 'radio-' + displayData.order; //TODO: 임시. 별도의 속성 필요.
                    radioEle.id = radioOptData[i].value;
                    radioEle.value = radioOptData[i].value;
                    radioEle.checked = (i === 0);

                    const lblEle = document.createElement('label');
                    lblEle.innerText = radioOptData[i].name;
                    lblEle.setAttribute('for', radioOptData[i].value);
                    if (displayData.position === 'left') {
                        divEle.appendChild(radioEle);
                        divEle.appendChild(lblEle);
                    } else {
                        divEle.appendChild(lblEle);
                        divEle.appendChild(radioEle);
                    }
                    fieldLastEle.appendChild(divEle);
                }
                break;
            case 'checkbox':
                //TODO: checkbox

                break;
            default :
                break;
        }

        comp.appendChild(fieldFirstEle);
        comp.appendChild(fieldLastEle);
        documentContainer.appendChild(comp);
    }

    /**
     * draw document.
     *
     * @param data 문서 데이터.
     */
    function drawDocument(data) {
        if (data.components.length > 0) {
            data.components.sort(function(a, b) {
                return a.display.order - b.display.order;
            });
            for (let i = 0; i < data.components.length; i++) {
                addComponent(data.components[i]);
            }
        }
    }

    /**
     * save document.
     */
    function save() {
        //TODO: 구현 필요. TicketRestController 호출해야함.
        alert("저장되었습니다.");
    }

    /**
     * init document.
     *
     * @param documentId 문서 id
     */
    function init(documentId) {
        console.info('document editor initialization. [DOCUMENT ID: ' + documentId + ']');
        documentContainer = document.getElementById('document-container');

        // 신청서의 문서 데이터 조회.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/documents/data/' + documentId,
            callbackFunc: function(xhr) {
                drawDocument(JSON.parse(xhr.responseText))
            },
            contentType: 'application/json; charset=utf-8'
        });
    }

    exports.init = init;
    exports.save = save;

    Object.defineProperty(exports, '__esModule', {value: true});
})));