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
const QUILL_DEFAULT_OPTIONS = {
    toolbarVisible: true,
    placeholder: '',
    readOnly: false,
    content: ''
};

function zQuill(target, options) {
    this.options = Object.assign({}, QUILL_DEFAULT_OPTIONS, options);

    // html template
    this.getTemplate = function () {
        return `<div id="textEditorToolbar" class="${this.options.toolbarVisible ? '' : 'ql-toolbar-hidden'}">` +
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
                    // [{ 'align': '' }, { 'align': 'center' }, { 'align': 'right' }],
                    `<span class="ql-formats">` +
                        `<button class="ql-align" value=""></button>` +
                        `<button class="ql-align" value="center"></button>` +
                        `<button class="ql-align" value="right"></button>` +
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

    // wrapper 생성
    target.innerHTML = '';
    target.classList.add('ql-wrapper-container');
    if (!this.options.readOnly) {
        target.tabIndex = 0; // 선택가능
    }
    target.insertAdjacentHTML('beforeend', this.getTemplate());
    this.containerEl = target;
    
    // quill editor 호출
    const quill = new Quill(target.querySelector('#textEditorContainer'), {
        modules: {
            toolbar: this.options.toolbarVisible ? target.querySelector('#textEditorToolbar') : false
        },
        placeholder: this.options.placeholder,
        theme: 'snow',
        readOnly: this.options.readOnly
    });
    // set value
    if (this.options.content !== '') {
        quill.setContents(this.options.content);
    }
    return quill;
}