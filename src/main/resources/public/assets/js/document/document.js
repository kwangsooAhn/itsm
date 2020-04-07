(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.aliceDocument = global.aliceDocument || {})));
}(this, (function (exports) {
    'use strict';

    let documentContainer = null;
    let buttonContainer = null;
    const numIncludeRegular = /[0-9]/gi;
    const numRegular = /^[0-9]*$/;
    const emailRegular = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
    const defaultAssigneeTypeForSave = 'assignee';

    /**
     * alert message.
     *
     * @param element element
     * @param msg 메시지
     */
    function alertMsg(element, msg) {
        aliceJs.alert(msg, function() {
            element.focus();
        });
    }

    /**
     * Validation Check.
     *
     * @param element element
     * @param validateData validate 데이터
     */
    function validateCheck(element, validateData) {
        const chkVal = element.value.trim();
        if (chkVal.length !== 0) {
            for (let key in validateData) {
                const value = validateData[key];
                if (value !== '') {
                    if (key === 'regexp') {
                        switch (value) {
                            case 'char':
                                if (numIncludeRegular.test(chkVal)) {
                                    alertMsg(element, validateData['regexp-msg']);
                                    return true;
                                }
                                break;
                            case 'num':
                                if (!numRegular.test(chkVal)) {
                                    alertMsg(element, validateData['regexp-msg']);
                                    return true;
                                }
                                break;
                            case 'email':
                                if (!emailRegular.test(chkVal)) {
                                    alertMsg(element, validateData['regexp-msg']);
                                    return true;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    if (key === 'length-min' && value > chkVal.length) {
                        alertMsg(element, i18n.get('document.msg.lengthMin', value));
                        return true;
                    }
                    if (key === 'length-max' && value < chkVal.length) {
                        alertMsg(element, i18n.get('document.msg.lengthMax', value));
                        return true;
                    }
                    if (key === 'date-min') {
                        let dateMinValueArray = value.split("|");
                        let dateMinValuePlaceholder = aliceForm.options.dateFormat + ' ' + aliceForm.options.timeFormat + ' ' + aliceForm.options.hourType;
                        let dateMinValue = aliceJs.changeDateFormat(dateMinValueArray[1], dateMinValuePlaceholder, dateMinValueArray[0], aliceForm.options.lang);
                        if (dateMinValue > chkVal) {
                            alertMsg(element, i18n.get('document.msg.dateMin').replace('{0}', dateMinValue));
                            return true;
                        }
                    }
                    if (key === 'date-max') {
                        let dateMaxValueArray = value.split("|");
                        let dateMaxValuePlaceholder = aliceForm.options.dateFormat + ' ' + aliceForm.options.timeFormat + ' ' + aliceForm.options.hourType;
                        let dateMaxValue = aliceJs.changeDateFormat(dateMaxValueArray[1], dateMaxValuePlaceholder, dateMaxValueArray[0], aliceForm.options.lang);
                        if (dateMaxValue < chkVal) {
                            alertMsg(element, i18n.get('document.msg.dateMax').replace('{0}', dateMaxValue));
                            return true;
                        }
                    }
                }
            }
        }
    }

    /**
     * add Component.
     *
     * @param compData 컴포넌트 데이터
     */
    function addComponent(compData) {
        const comp = document.createElement('div');
        comp.id = compData.componentId;
        comp.className = 'component';
        let comp_type = '';
        if (compData.attributes === undefined) {
            comp_type = compData.type;
        } else {
            comp_type = compData.attributes.type;
        }
        comp.setAttribute('data-type', comp_type);

        const fieldFirstEle = document.createElement('div');
        const fieldLastEle = document.createElement('div');
        fieldFirstEle.className = 'field';
        fieldLastEle.className = 'field';

        let comp_label = '';
        let comp_display = '';
        let comp_validate = '';
        if (compData.attributes === undefined) {
            comp_label = compData.label;
            comp_display = compData.display;
            comp_validate = compData.validate;
        } else {
            comp_label = compData.attributes.label;
            comp_display = compData.attributes.display;
            comp_validate = compData.attributes.validate;
        }
        const lblData = comp_label;
        const displayData = comp_display;
        const validateData = comp_validate;

        if (typeof lblData !== 'undefined' && lblData.position !== 'hidden') {
            const lblEle = document.createElement('div');
            lblEle.className = 'label';
            lblEle.style.fontSize = lblData.size + 'px';
            lblEle.style.color = lblData.color;
            lblEle.style.fontWeight = (lblData.bold === 'Y') ? 'bold' : '';
            lblEle.style.fontStyle = (lblData.italic === 'Y') ? 'italic' : '';
            lblEle.style.textDecoration = (lblData.underline === 'Y') ? 'underline' : '';
            lblEle.innerHTML = lblData.text;
            fieldFirstEle.style.textAlign = lblData.align;
            fieldFirstEle.appendChild(lblEle);

            if (typeof validateData !== 'undefined' && validateData.required === 'Y') {
                const requiredEle = document.createElement('span');
                requiredEle.className = 'required';
                requiredEle.innerText = '*';
                fieldFirstEle.append(requiredEle);
            }
            if (lblData.position === 'left') {
                comp.style.display = 'flex';
                fieldFirstEle.style.flexBasis = (aliceForm.options.columnWidth * Number(lblData.column)) + '%';
                fieldLastEle.style.flexBasis = (aliceForm.options.columnWidth * Number(displayData.column)) + '%';
            }
            comp.appendChild(fieldFirstEle);
        }
        comp.appendChild(fieldLastEle);
        documentContainer.appendChild(comp);

        switch (comp_type) {
            case 'text':
                const textEle = document.createElement('input');
                textEle.type = 'text';
                textEle.placeholder = displayData.placeholder;
                if (displayData['default']) {
                    let defaultTextValueArr = displayData['default'].split('|');
                    let defaultTextValue = '';
                    if (defaultTextValueArr[0] === 'none') {
                        defaultTextValue = defaultTextValueArr[1];
                    } else {
                        defaultTextValue = userData[defaultTextValueArr[1]];
                    }
                    textEle.value = defaultTextValue;
                }
                textEle.style.outlineWidth = displayData['outline-width'] + 'px';
                textEle.style.outlineColor = displayData['outline-color'];
                textEle.minLength = validateData['length-min'];
                textEle.maxLength = validateData['length-max'];
                textEle.required = (validateData.required === 'Y');
                textEle.addEventListener('focusout', function() {
                    validateCheck(this, validateData);
                });
                if (compData.values != undefined && compData.values.length > 0) {
                    textEle.value = compData.values[0].value;
                }
                fieldLastEle.appendChild(textEle);
                break;
            case 'textarea':
                const textEditorUseYn = displayData['editor-useYn'] === 'Y' ? true : false;
                if (textEditorUseYn) {
                    const defaultRowHeight = 26;
                    const textEditorGroupEle = document.createElement('div');
                    textEditorGroupEle.style.width = '100%';
                    fieldLastEle.appendChild(textEditorGroupEle);

                    const textEditorEle = document.createElement('div');
                    textEditorEle.className = 'editor-container';
                    let textEditorEleHeight = displayData.rows !== '' ? Number(displayData.rows) * defaultRowHeight : defaultRowHeight;
                    textEditorEle.style.height = textEditorEleHeight + 'px';
                    textEditorEle.style.borderWidth = displayData['outline-width'] + 'px';
                    textEditorEle.style.borderColor = displayData['outline-color'];
                    textEditorEle.setAttribute('data-required', (validateData.required === 'Y'));
                    textEditorGroupEle.appendChild(textEditorEle);

                    let textEditorOptions = {
                        modules: {
                            toolbar: [
                                [{'header': [1, 2, 3, 4, 5, 6, false]}],
                                ['bold', 'italic', 'underline'],
                                [{'color': []}, {'background': []}],
                                [{'align': []}, {'list': 'bullet'}, 'image']
                            ]
                        },
                        placeholder: displayData.placeholder,
                        theme: 'snow'
                    };
                    let textEditor = new Quill(textEditorEle, textEditorOptions);
                    //유효성 검사
                    textEditor.on('selection-change', function (range, oldRange, source) {
                        if (range === null && oldRange !== null) {
                            if (validateData['length-min'] !== '' && textEditor.getLength() < Number(validateData['length-min'])) {
                                alertMsg(textEditor, i18n.get('document.msg.lengthMin', validateData['length-min']));
                            }
                            if (validateData['length-max'] !== '' && textEditor.getLength() > Number(validateData['length-max'])) {
                                textEditor.deleteText(Number(validateData['length-max']) - 1, textEditor.getLength());
                                alertMsg(textEditor, i18n.get('document.msg.lengthMax', validateData['length-max']));
                            }
                        }
                    });
                    let textEditorToolbar = textEditorGroupEle.querySelector('.ql-toolbar');
                    if (textEditor !== null && textEditorToolbar) {
                        textEditorToolbar.style.borderWidth = displayData['outline-width'] + 'px';
                        textEditorToolbar.style.borderColor = displayData['outline-color'];
                    }
                    if (compData.values != undefined && compData.values.length > 0) {
                        textEditor.setContents(JSON.parse(compData.values[0].value));
                    }
                } else {
                    const textareaEle = document.createElement('textarea');
                    textareaEle.placeholder = displayData.placeholder;
                    textareaEle.style.outlineWidth = displayData['outline-width'] + 'px';
                    textareaEle.style.outlineColor = displayData['outline-color'];
                    textareaEle.minLength = validateData['length-min'];
                    textareaEle.maxLength = validateData['length-max'];
                    textareaEle.required = (validateData.required === 'Y');
                    textareaEle.addEventListener('focusout', function() {
                        validateCheck(this, validateData);
                    });
                    if (compData.values != undefined && compData.values.length > 0) {
                        textareaEle.value = compData.values[0].value;
                    }
                    fieldLastEle.appendChild(textareaEle);
                }
                break;
            case 'select':
                const selectEle = document.createElement('select');
                selectEle.required = (validateData.required === 'Y');
                let optData;
                if (compData.attributes !== undefined) {
                    optData = compData.attributes.option;
                } else {
                    optData = compData.option;
                }
                optData.sort(function (a, b) {
                    return a.seq - b.seq;
                });
                for (let i = 0; i < optData.length; i++) {
                    const optEle = document.createElement('option');
                    optEle.text = optData[i].name;
                    optEle.value = optData[i].value;
                    if (compData.values !== undefined && compData.values.length > 0) {
                        if (optEle.value === compData.values[0].value) {
                            optEle.selected = true;
                        }
                    }
                    selectEle.appendChild(optEle);
                }
                fieldLastEle.appendChild(selectEle);

                break;
            case 'radio':
                let radioOptData;
                if (compData.attributes !== undefined) {
                    radioOptData = compData.attributes.option;
                } else {
                    radioOptData = compData.option;
                }
                radioOptData.sort(function (a, b) {
                    return a.seq - b.seq;
                });

                for (let i = 0; i < radioOptData.length; i++) {
                    const divEle = document.createElement('div');
                    if (displayData.direction === 'horizontal') { divEle.style.display = 'inline-block'}
                    const radioEle = document.createElement('input');
                    radioEle.type = 'radio';
                    radioEle.name = 'radio-' + compData.componentId;
                    radioEle.id = radioOptData[i].value;
                    radioEle.value = radioOptData[i].value;
                    if (compData.values != undefined && compData.values.length > 0) {
                        if (radioEle.value === compData.values[0].value) {
                            radioEle.checked = true;
                        }
                    } else {
                        radioEle.checked = (i === 0);
                    }
                    radioEle.required = (i === 0 && validateData.required === 'Y');

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
                let checkOptData;
                if (compData.attributes !== undefined) {
                    checkOptData = compData.attributes.option;
                } else {
                    checkOptData = compData.option;
                }
                checkOptData.sort(function(a, b) {
                    return a.seq - b.seq;
                });

                for (let i = 0; i < checkOptData.length; i++) {
                    const divEle = document.createElement('div');
                    if (displayData.direction === 'horizontal') { divEle.style.display = 'inline-block'}
                    const checkEle = document.createElement('input');
                    checkEle.type = 'checkbox';
                    checkEle.name = 'check-' + compData.componentId;
                    checkEle.id = checkOptData[i].value;
                    checkEle.value = checkOptData[i].value;
                    if (compData.values != undefined && compData.values.length > 0) {
                        const checkboxValue = compData.values[0].value.split(',');
                        checkboxValue.forEach(function (element) {
                            if (checkEle.value === element) {
                                checkEle.checked = true;
                            }
                        });
                    }
                    checkEle.required = (i === 0 && validateData.required === 'Y');

                    const lblEle = document.createElement('label');
                    lblEle.innerText = checkOptData[i].name;
                    lblEle.setAttribute('for', checkOptData[i].value);
                    if (displayData.position === 'left') {
                        divEle.appendChild(checkEle);
                        divEle.appendChild(lblEle);
                    } else {
                        divEle.appendChild(lblEle);
                        divEle.appendChild(checkEle);
                    }
                    fieldLastEle.appendChild(divEle);
                }
                break;
            case 'label':
                const lblEle = document.createElement('div');
                lblEle.style.fontSize = displayData.size + 'px';
                lblEle.style.color = displayData.color;
                lblEle.style.fontWeight = (displayData.bold === 'Y') ? 'bold' : '';
                lblEle.style.fontStyle = (displayData.italic === 'Y') ? 'italic' : '';
                lblEle.style.textDecoration = (displayData.underline === 'Y') ? 'underline' : '';
                lblEle.style.textAlign = displayData.align;
                lblEle.innerHTML = displayData.text;
                fieldLastEle.appendChild(lblEle);
                break;
            case 'image':
                const imgEle = document.createElement('img');
                imgEle.src = displayData.path;
                imgEle.width = displayData.width;
                imgEle.height = displayData.height;
                fieldLastEle.style.textAlign = displayData.align;
                fieldLastEle.appendChild(imgEle);
                break;
            case 'divider':
                const lineEle = document.createElement('hr');
                lineEle.style.borderTopWidth  = displayData.thickness + 'px';
                lineEle.style.borderBottomWidth = '0px';
                lineEle.style.borderTopStyle = displayData.type;
                lineEle.style.borderTopColor = displayData.color;
                fieldLastEle.appendChild(lineEle);
                break;
            case 'date':
                let dateDefaultArr = displayData.default.split('|');
                let dateDefault = '';
                let dateplaceholder = aliceForm.options.dateFormat + ' ' + aliceForm.options.timeFormat;

                if (compData.values != undefined && compData.values.length > 0 ) {
                    let dateValue = compData.values[0].value.split('|');
                    if (dateValue[0] !== '') {
                        dateDefault = aliceJs.changeDateFormat(dateValue[1], dateplaceholder, dateValue[0], aliceForm.options.lang);
                    }
                } else {
                    if (dateDefaultArr[0] === 'now') {
                        dateDefault = aliceJs.getTimeStamp(aliceForm.options.dateFormat);
                        dateDefault = dateDefault.split(' ')[0];
                    } else if (dateDefaultArr[0] === 'datepicker') {
                        dateDefault = aliceJs.changeDateFormat(dateDefaultArr[2], dateplaceholder, dateDefaultArr[1], aliceForm.options.lang);
                    } else if (dateDefaultArr[0] === 'date') {
                        dateDefault = aliceJs.getTimeStamp(aliceForm.options.dateFormat, dateDefaultArr[1]);
                        dateDefault = dateDefault.split(' ')[0];
                    }
                }
                const dateEle = document.createElement('input');
                dateEle.id = 'date-' + compData.componentId;
                dateEle.type = 'text';
                dateEle.placeholder = dateplaceholder;
                dateEle.value = dateDefault;
                dateEle.required = (validateData.required === 'Y');
                dateEle.readOnly = true;
                dateEle.addEventListener('focusout', function() {
                    validateCheck(this, validateData);
                });
                fieldLastEle.appendChild(dateEle);
                dateTimePicker.initDatePicker('date-' + compData.componentId, aliceForm.options.dateFormat, aliceForm.options.lang);
                break;
            case 'time':
                let timeDefaultArr = displayData.default.split('|');
                let timeDefault = '';

                if (compData.values != undefined && compData.values.length > 0 ) {
                    //저장한 날짜와 포맷
                    let timeValue = compData.values[0].value.split('|');
                    //저장한 가상 날짜 및 시간
                    let dummyDateTime = aliceJs.getTimeStamp(timeValue[1]);
                    //저장한 가상 날짜
                    let dummyDate = dummyDateTime.split(' ');
                    let timeFormat = aliceForm.options.dateFormat + ' ' + aliceForm.options.timeFormat + ' ' + aliceForm.options.hourType;
                    timeDefault = aliceJs.changeDateFormat(timeValue[1], timeFormat, dummyDate[0] +' '+ timeValue[0], aliceForm.options.lang);
                    let time = timeDefault.split(' ');
                    if (time.length > 2) {
                        timeDefault = time[1] +' '+time[2];
                    } else {
                        timeDefault = time[1];
                    }
                } else {
                    if (timeDefaultArr[0] === 'now') {
                        timeDefault = aliceJs.getTimeStamp(aliceForm.options.timeFormat);
                    } else if (timeDefaultArr[0] === 'timepicker') {
                        timeDefault = timeDefaultArr[1];
                    } else if (timeDefaultArr[0] === 'time') {
                        timeDefault = aliceJs.getTimeStamp(aliceForm.options.timeFormat, '', timeDefaultArr[1]);
                    }
                }
                const timeEle = document.createElement('input');
                timeEle.id = 'time-' + compData.componentId;
                timeEle.type = 'text';
                timeEle.placeholder = aliceForm.options.timeFormat;
                timeEle.value = timeDefault;
                timeEle.required = (validateData.required === 'Y');
                timeEle.readOnly = true;
                fieldLastEle.appendChild(timeEle);
                dateTimePicker.initTimePicker('time-' + compData.componentId, aliceForm.options.hourType, aliceForm.options.lang);
                break;

            case 'datetime':
                let datetimeDefaultArr = displayData.default.split('|');
                let datetimeDefault = '';
                let datetimeplaceholder = aliceForm.options.dateFormat + ' ' + aliceForm.options.timeFormat + ' ' + aliceForm.options.hourType;

                if (compData.values != undefined && compData.values.length > 0 ) {
                    let dateValue = compData.values[0].value.split('|');
                    if (dateValue[0] !== '') {
                        datetimeDefault = aliceJs.changeDateFormat(dateValue[1], datetimeplaceholder, dateValue[0], aliceForm.options.lang);
                    }
                } else {
                    if (datetimeDefaultArr[0] === 'now') {
                        datetimeDefault = aliceJs.getTimeStamp(aliceForm.options.dateFormat + ' ' + aliceForm.options.timeFormat);
                    } else if (datetimeDefaultArr[0] === 'datetimepicker') {
                        datetimeDefault = aliceJs.changeDateFormat(datetimeDefaultArr[2], datetimeplaceholder, datetimeDefaultArr[1], aliceForm.options.lang);
                    } else if (datetimeDefaultArr[0] === 'datetime') {
                        datetimeDefault = aliceJs.getTimeStamp(aliceForm.options.dateFormat + ' ' + aliceForm.options.timeFormat, datetimeDefaultArr[1], datetimeDefaultArr[2]);
                    }
                }
                const datetimeEle = document.createElement('input');
                datetimeEle.id = 'datetime-' + compData.componentId;
                datetimeEle.type = 'text';
                datetimeEle.placeholder = datetimeplaceholder;
                datetimeEle.value = datetimeDefault;
                datetimeEle.required = (validateData.required === 'Y');
                datetimeEle.readOnly = true;
                datetimeEle.addEventListener('focusout', function() {
                    validateCheck(this, validateData);
                });
                fieldLastEle.appendChild(datetimeEle);
                dateTimePicker.initDateTimePicker('datetime-' + compData.componentId, aliceForm.options.dateFormat, aliceForm.options.hourType, aliceForm.options.lang);
                break;
            case 'fileupload':
                const fileEle = document.createElement('div');
                const fileEleId = 'dropZoneFiles-' + compData.componentId;
                const fileUploadedEleId = 'dropZoneUploadedFiles-' + compData.componentId;
                fileEle.id = fileEleId;
                fieldLastEle.appendChild(fileEle);
                const fileUploadedEle = document.createElement('div');
                fileUploadedEle.id = fileUploadedEleId;
                fieldLastEle.appendChild(fileUploadedEle);
                if (compData.values != undefined && compData.values.length > 0) {
                    fileUploader.init({extra: {formId: 'frm', ownId: '',
                            dropZoneFilesId: fileEleId, dropZoneUploadedFilesId: fileUploadedEleId, fileDataIds: compData.values[0].value}});
                } else {
                    fileUploader.init({extra: {formId: 'frm', ownId: '',
                            dropZoneFilesId: fileEleId, dropZoneUploadedFilesId: fileUploadedEleId}});
                }
                break;
            default :
                break;
        }
    }

    /**
     * button를 만든다.
     * 저장과 취소 버튼은 기본적으로 생성된다.
     * @param  buttonData : button 정보 값
     */
    function addButton(buttonData) {
        const buttonEle = document.createElement('div');
        buttonEle.style.marginTop = '10px';
        buttonEle.style.textAlign = 'center';
        if (buttonData !== undefined && buttonData !== '') {
            buttonData.forEach(function(element) {
                if (element.name !== '') {
                    let buttonProcessEle = document.createElement('button');
                    buttonProcessEle.type = 'button';
                    buttonProcessEle.innerText = element.name;
                    buttonProcessEle.addEventListener('click', function () {
                        aliceDocument.save(element.value);
                    });
                    buttonEle.appendChild(buttonProcessEle);
                }
            });
        } else {
            //20200331 kimsungmin 다음 스프린트에서는 해당 버튼은 삭제가 되어야 한다.
            //token Id 가 없고 버튼에 대한 정보 없다는 것은 처음 문서 생성 이라고 판단한다.
            if (document.getElementById('tokenId') === null) {
                const buttonSaveEle = document.createElement('button');
                buttonSaveEle.type = 'button';
                buttonSaveEle.innerText = i18n.get('common.btn.save');
                buttonSaveEle.addEventListener('click', function () {
                    aliceDocument.save('save');
                });
                buttonEle.appendChild(buttonSaveEle);
            }
        }

        //20200331 kimsungmin 다음 스프린트에서는 해당 버튼은 삭제가 되어야 한다.
        const buttonCancelEle = document.createElement('button');
        buttonCancelEle.type = 'button';
        buttonCancelEle.innerText = i18n.get('common.btn.cancel');
        buttonCancelEle.addEventListener('click', function() {
            window.close();
        });
        buttonEle.appendChild(buttonCancelEle);
        if (buttonContainer !== null) {
            buttonContainer.appendChild(buttonEle);
        }
    }

    /**
     * radio, checkbox 선택 확인.
     *
     * @param element element
     */
    function selectCheck(element) {
        let elements = document.getElementsByName(element.name);
        for (let j = 0; j < elements.length; j++) {
            if (elements[j].checked) { return true; }
        }
        return false;
    }

    /**
     * required Check.
     *
     */
    function requiredCheck() {
        const requiredObjs = document.querySelectorAll(':required');
        for (let i = 0; i < requiredObjs.length; i++) {
            let requiredObj = requiredObjs[i];
            if (requiredObj.type === 'radio' || requiredObj.type === 'checkbox') {
                if (!selectCheck(requiredObj)) {
                    alertMsg(requiredObj, i18n.get('document.msg.requiredSelect'));
                    return true;
                }
            } else if (requiredObj.value === '') {
                alertMsg(requiredObj, i18n.get('document.msg.requiredEnter'));
                return true;
            }
        }
        //textarea Editor 필수 체크
        const textEditorElems = document.querySelectorAll('.editor-container');
        for (let i = 0; i < textEditorElems.length; i++) {
            let textEditorElem = textEditorElems[i];
            if (textEditorElem.getAttribute('data-required') === 'true') {
                let textEditor = new Quill(textEditorElem);
                if (textEditor.getLength() === 1) {
                    alertMsg(textEditor, i18n.get('document.msg.requiredEnter'));
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * id Component를 만든다. (document, token)
     *
     * @param v_kind 분류, data : id 값
     */
    function addIdComponent(v_kind, v_data) {
        const comp = document.createElement('div');
        comp.id = v_kind;
        comp.setAttribute('data-id', v_data);
        documentContainer.appendChild(comp);
    }

    /**
     * save document.
     */
    function save(v_kind) {
        // validation check
        if (v_kind !== 'save') {
            if (requiredCheck()) {
                return false;
            }
        }

        let tokenObject = {};
        let componentArrayList = [];
        let actionArrayList = [];
        let fileDataIds = '';

        //documentId 값을 구한다.
        const documentElements = document.getElementById('documentId');
        if (documentElements !== null && documentElements !== undefined) {
            tokenObject.documentId = documentElements.getAttribute('data-id');
        } else {
            tokenObject.documentId = "";
        }

        //ComponentsInfo
        const componentElements = documentContainer.getElementsByClassName('component');
        for (let eIndex = 0; eIndex < componentElements.length; eIndex++) {
            let componentDataType = componentElements[eIndex].getAttribute('data-type');
            if (componentDataType === 'text' || componentDataType === 'date' || componentDataType === 'time' || componentDataType === 'datetime' ||
                componentDataType === 'textarea' || componentDataType === 'select' || componentDataType === 'radio' || componentDataType === 'checkbox' ||
                componentDataType === 'fileupload') {
                let componentId = componentElements[eIndex].getAttribute('id');
                let componentValue = '';
                let componentChildObject = {};
                let componentChild = '';

                switch (componentDataType) {
                    case 'text':
                        componentChild = componentElements[eIndex].getElementsByTagName('input');
                        componentValue = componentChild.item(0).value;
                        break;
                    case 'date':
                        componentChild = componentElements[eIndex].getElementsByTagName('input');
                        let dateFormat = componentChild.item(0).placeholder;
                        componentValue = componentChild.item(0).value+'|'+dateFormat;
                        break;
                    case 'time':
                        let timeFormat = aliceForm.options.dateFormat + ' ' + aliceForm.options.timeFormat + ' ' + aliceForm.options.hourType;
                        componentChild = componentElements[eIndex].getElementsByTagName('input');
                        componentValue = componentChild.item(0).value+'|'+timeFormat;
                        break;
                    case 'datetime':
                        componentChild = componentElements[eIndex].getElementsByTagName('input');
                        let datetimeFormat = componentChild.item(0).placeholder;
                        componentValue = componentChild.item(0).value+'|'+datetimeFormat;
                        break;
                    case 'textarea':
                        componentChild = componentElements[eIndex].querySelector('.editor-container');
                        if (componentChild) {
                            let textEditor = new Quill(componentChild); //Quill.find(componentChild) === textEditor : true
                            componentValue = JSON.stringify(textEditor.getContents());
                        } else {
                            componentChild = componentElements[eIndex].getElementsByTagName('textarea');
                            componentValue = componentChild.item(0).value;
                        }
                        break;
                    case 'select':
                        componentChild = componentElements[eIndex].getElementsByTagName('select');
                        componentValue = componentChild.item(0).options[componentChild.item(0).selectedIndex].value;
                        break;
                    case 'radio':
                        componentChild = componentElements[eIndex].getElementsByTagName('input');
                        for (let radioIndex = 0; radioIndex < componentChild.length; radioIndex++) {
                            if (componentChild[radioIndex].checked) {
                                componentValue = componentChild[radioIndex].value;
                            }
                        }
                        break;
                    case 'checkbox':
                        componentChild = componentElements[eIndex].getElementsByTagName('input');
                        for (let checkBoxIndex = 0; checkBoxIndex < componentChild.length; checkBoxIndex++) {
                            if (componentChild[checkBoxIndex].checked) {
                                if (componentValue === '' && componentValue.indexOf(",") === -1) {
                                    componentValue = componentChild[checkBoxIndex].value;
                                } else {
                                    componentValue = componentValue + ',' + componentChild[checkBoxIndex].value;
                                }
                            }
                        }
                        break;
                    case 'fileupload' :
                        componentChild = componentElements[eIndex].getElementsByTagName('input');
                        for (let fileuploadIndex = 0; fileuploadIndex < componentChild.length; fileuploadIndex++) {
                            if (componentChild[fileuploadIndex].name !== 'delFileSeq') {
                                if (componentValue === '' && componentValue.indexOf(",") === -1) {
                                    componentValue = componentChild[fileuploadIndex].value;
                                } else {
                                    componentValue = componentValue + ',' + componentChild[fileuploadIndex].value;
                                }
                            }
                            //신규 추가된 첨부파일만 임시폴더에서 일반폴더로 옮기기 위해서
                            if (componentChild[fileuploadIndex].name === 'fileSeq') {
                                if (fileDataIds === '' && fileDataIds.indexOf(",") === -1) {
                                    fileDataIds = componentChild[fileuploadIndex].value;
                                } else {
                                    fileDataIds = fileDataIds + ',' + componentChild[fileuploadIndex].value;
                                }
                            }
                        }
                        break;
                }
                componentChildObject.componentId = componentId;
                componentChildObject.value = componentValue;
                componentArrayList.push(componentChildObject);
            }
        }

        //tokenObject init (RestTemplateTokenDto)
        const tokenElements = document.getElementById('tokenId');
        if (tokenElements !== null && tokenElements !== undefined) {
            tokenObject.tokenId = tokenElements.getAttribute('data-id');
        } else {
            tokenObject.tokenId = '';
        }
        if (v_kind === 'save') {
            tokenObject.isComplete = false; //해당 값이 false라면 저장이다.
            tokenObject.assigneeId = aliceForm.options.sessionInfo.userKey;
            tokenObject.assigneeType = defaultAssigneeTypeForSave;
        } else {
            tokenObject.isComplete = true; //해당 값이 true라면 처리이다.
            tokenObject.assigneeId = '';
            tokenObject.assigneeType = '';
        }

        if (componentArrayList.length > 0) {
            tokenObject.data = componentArrayList;
        } else {
            tokenObject.data = '';
        }

        actionArrayList = v_kind;
        tokenObject.actions = actionArrayList;

        let method = '';
        if (tokenObject.tokenId === '') {
            method = 'post';
        } else {
            method = 'put';
        }

        if (fileDataIds !== '') {
            tokenObject.fileDataIds = fileDataIds;
        }

        const opt = {
            method: method,
            url: '/rest/tokens/data',
            params: JSON.stringify(tokenObject),
            contentType: 'application/json',
            callbackFunc: function(xhr) {
                if (xhr.responseText === 'true') {
                    aliceJs.alert(i18n.get('common.msg.save'), function () {
                        window.close();
                    });
                } else {
                    aliceJs.alert(i18n.get('common.msg.fail'));
                }
            }
        };
        aliceJs.sendXhr(opt);
    }

        /**
     * 조회된 데이터 draw.
     *
     * @param data 문서 데이터.
     */
    function drawDocument(data) {
        console.log(data);
        let components = (data.token === undefined) ? data.components : data.token.components;
        if (components.length > 0) {
            if (components.length > 2) {
                components.sort(function (a, b) {
                    if (a.attributes === undefined) {
                        return a.display.order - b.display.order;
                    } else {
                        return a.attributes.display.order - b.attributes.display.order;
                    }
                });
            }
            for (let i = 0, len = components.length; i < len; i++) {
                //데이터로 전달받은 컴포넌트 속성과 기본 속성을 merge한 후 컴포넌트 draw
                let componentAttr = components[i];
                let compType = (componentAttr.attributes === undefined) ? componentAttr.type : componentAttr.attributes.type;
                let defaultComponentAttr = component.getData(compType);
                let mergeComponentAttr = null;
                if (componentAttr.attributes === undefined) { //신청서
                    mergeComponentAttr = Object.assign({}, defaultComponentAttr, componentAttr);
                    componentAttr = mergeComponentAttr;
                } else { //처리할 문서
                    mergeComponentAttr = Object.assign({}, defaultComponentAttr, componentAttr.attributes);
                    componentAttr.attributes = mergeComponentAttr;
                }
                component.draw(compType, documentContainer, componentAttr);
            }
        }
        if (data.documentId !== undefined) {
            addIdComponent('documentId', data.documentId);
        }

        if (data.tokenId !== undefined) {
            addIdComponent('tokenId', data.tokenId);
        }

        if (data.components != undefined) {
            addButton(data.action);
        } else if (data.token.components != undefined) {
            addButton(data.token.action);
        }
    }

    /**
     * init document.
     *
     * @param documentId 문서 id
     */
    function init(documentId) {
        console.info('document editor initialization. [DOCUMENT ID: ' + documentId + ']');
        documentContainer = document.getElementById('document-container');
        buttonContainer = document.getElementById('button-container');
        
        // document data search.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/documents/data/' + documentId,
            callbackFunc: function(xhr) {
                let jsonData = JSON.parse(xhr.responseText);
                jsonData.documentId = documentId;
                drawDocument(jsonData);
            },
            contentType: 'application/json; charset=utf-8'
        });
    }

    /**
     * init Token.
     *
     * @param tokenId 문서 id
     */
    function initToken(tokenId) {
        console.info('document editor initialization. [Token ID: ' + tokenId + ']');
        documentContainer = document.getElementById('document-container');
        buttonContainer = document.getElementById('button-container');

        // token data search.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/tokens/data/' + tokenId,
            callbackFunc: function(xhr) {
                let jsonData = JSON.parse(xhr.responseText);
                jsonData.tokenId = tokenId;
                drawDocument(jsonData);
            },
            contentType: 'application/json; charset=utf-8'
        });
    }

    /**
     * Init Container.
     *
     * @param elementId
     */
    function initContainer(elementId) {
        documentContainer = document.getElementById(elementId);
    }

    exports.init = init;
    exports.initToken = initToken;
    exports.save = save;
    exports.initContainer = initContainer;
    exports.drawDocument = drawDocument;

    Object.defineProperty(exports, '__esModule', {value: true});
})));
