/**
 * 텍스트 에디터 공통 Wrapper 함수
 *
 * 기존 quill.js (https://quilljs.com/) 라이브러리를 디자인에 맞게 커스텀.
 * 사용시 vendors/quill/quill.js 파일과 함께 import 한다.
 *
 * https://quilljs.com/
 *
 * @author Woo Da Jung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

const DEFAULT_OPTIONS = {
    placeholder: '',
    readOnly: false
};

function zQuill(target, options) {
    this.options = Object.assign({}, DEFAULT_OPTIONS, options);
    // html template
    this.getTemplate = function () {
        return `<div id="textEditorToolbar">` +
                    // {'header': [1, 2, 3, 4, false]}
                    `<span class="ql-formats">` +
                        `<select class="ql-header">` +
                            `<option value="1">Heading 1</option>` +
                            `<option value="2">Heading 2</option>` +
                            `<option value="3">Heading 3</option>` +
                            `<option value="4">Heading 4</option>` +
                            `<option selected="selected">Normal</option>` +
                        `</select>` +
                    `</span>` +
                    // ['bold', 'italic', 'underline']
                    `<span class="ql-formats">` +
                        `<button class="ql-bold"></button>` +
                        `<button class="ql-italic"></button>` +
                        `<button class="ql-underline"></button>` +
                    `</span>` +
                    // [{'color': []}, {'background': []}]
                    `<span class="ql-formats">` +
                        `<select class="ql-color"></select>` +
                        `<select class="ql-background"></select>` +
                    `</span>` +
                    // align - left, center, right
                    `<span class="ql-formats">` +
                        `<button class="ql-align-left" id="textEditorAlignLeft" value="left"></button>` +
                        `<button class="ql-align-center" id="textEditorAlignCenter" value="center"></button>` +
                        `<button class="ql-align-right" id="textEditorAlignRight" value="right"></button>` +
                    `</span>` +
                    // [{ 'list': 'bullet' }]
                    `<span class="ql-formats">` +
                        `<button class="ql-list" value="bullet"></button>` +
                    `</span>` +
                    // ['image']
                    `<span class="ql-formats">` +
                        `<button class="ql-image"></button>` +
                    `</span>` +
            `</div>` +
            `<div id="textEditorContainer"></div>`;
    };
    // toolbar 초기화
    target.innerHTML = '';
    target.insertAdjacentHTML('beforeend', this.getTemplate());
    this.containerEl = target;

    const quill = new Quill('#textEditorContainer', {
        modules: { toolbar: '#textEditorToolbar' },
        placeholder: this.options.placeholder,
        theme: 'snow',
        readOnly: this.options.readOnly
    });

    console.log(quill);
}