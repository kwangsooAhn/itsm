/**
 * 썸네일 모달 라이브러리
 * 
 * @author woodajung
 * @version 1.0
 */

(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.thumbnail = global.thumbnail || {})));
}(this, (function (exports) {
    'use strict';

    let defaults = {
        title: 'Image',
        files: [],
        type: 'image',         // 타입 : image, icon
        isThumbnailInfo: true, // 하단 정보 출력 여부
        isFilePrefix: true,    // 파일 선택시 파일명 앞에 'file:///' 추가 여부
        thumbnailDoubleClickUse: false, // 더블클릭으로 이미지 선택기능 여부
        buttons: [{
            content: 'Confirm',
            classes: 'thumbnail-modal-button-default',
            bindKey: false,
            callback: function(modal) {
                modal.save();
            }
        }, {
            content: 'Cancel',
            classes: 'thumbnail-modal-button-default',
            bindKey: false,
            callback: function(modal) {
                modal.hide();
            }
        }],
        close: {
            location: 'in',
            bindKey: 27,
            callback: function(modal) {
                modal.hide();
            }
        },
        onShow: function(modal) {},
        onHide: function(modal) {
            modal.destroy();
        },
        onCreate: function(modal) {},
        onDestroy: function(modal) {}
    };

    /**
     * 특정 클래스 목록 조회
     * @param el 대상객체
     */
    function getClasses(el) {
        return el.className.split(' ').filter(function(c) {
            return c.length > 0;
        });
    }

    /**
     * 특정 클래스 존재 여부 체크
     * @param el 대상객체
     * @param className 클래스명
     */
    function hasClass(el, className) {
        return getClasses(el).indexOf(className) >= 0;
    }

    /**
     * 특정 클래스 추가
     * @param el 대상객체
     * @param className 클래스명
     */
    function addClass(el, className) {
        if (!hasClass(el, className)) {
            el.className += ' ' + className;
        }
    }

    /**
     * 특정 클래스 삭제
     * @param el 대상객체
     * @param className 클래스명
     */
    function removeClass(el, className) {
        if (hasClass(el, className)) {
            let classes = getClasses(el);
            classes.splice(classes.indexOf(className), 1);
            el.className = classes.join(' ');
        }
    }

    /**
     * 특정 클래스 이름을 가진 요소 내부를 클릭했는지 확인
     * @param e 이벤트객체
     * @param className 클래스명
     * @return 존재하면 객제 반환
     */
    function clickInsideElement(e, className) {
        let el = e.srcElement || e.target;
        if (el.classList.contains(className)) {
            return el;
        } else {
            while (el = el.parentNode) {
                if (el.classList && el.classList.contains(className)) {
                    return el;
                }
            }
        }
        return null;
    }

    /**
     * 모달 생성 (gModal.js 참고)
     * https://github.com/jsanahuja/thumbnail-modal
     * 
     * @param options 옵션
     */
    function Modal(options) {
        this.id = Math.random().toString(36).substr(2);
        this.options = options;
        this.display = false;
        this.bindings = {};

        this.bind = function(key, callback) {
            if (typeof this.bindings[key] !== 'undefined') {
                console.warn('thumbnail-modal: Tried to bind the key ' + key + ' twice. Overriding...');
            }
            this.bindings[key] = callback;
        };

        this.addKeyListener = function() {
            window.currentModal = this;
            window.addEventListener('keydown', this.onKeyPress, false);
        };

        this.removeKeyListener = function() {
            window.currentModal = undefined;
            window.removeEventListener('keydown', this.onKeyPress, false);
        };

        this.onKeyPress = function(e) {
            if ( typeof window.currentModal !== 'undefined' &&  window.currentModal instanceof Modal) {
                let _that = window.currentModal;
                if (!_that.display) return;
                let keyCode = e.keyCode || e.which;
                let keys = Object.keys(_that.bindings);
                for (let i = 0; i < keys.length; i++) {
                    if (parseInt(keys[i]) === keyCode) {
                        e.preventDefault();
                        e.stopPropagation();
                        _that.bindings[keys[i]](_that);
                        return false;
                    }
                }
            }
        };

        this.show = function() { // 모달 표시
            if (typeof this.wrapper !== 'undefined') {
                addClass(this.wrapper, 'thumbnail-modal-active');
                addClass(document.body, 'thumbnail-modal-active');
                this.display = true;
                this.options.onShow(this);
            }
        };

        this.hide = function() { // 모달 숨김
            if (typeof this.wrapper !== 'undefined') {
                removeClass(this.wrapper, 'thumbnail-modal-active');
                removeClass(document.body, 'thumbnail-modal-active');
                this.display = false;
                this.options.onHide(this);
            }
        };

        this.save = function() { // 확인 버튼 클릭 > 저장
            // image 미선택 시 알림창 출력
            const selectedFile = this.wrapper.querySelector('.thumbnail.selected');
            if (!selectedFile) {
                aliceJs.alert(i18n.msg('image.msg.fileSelect'));
                return false;
            }
            const targetElem = document.getElementById(this.options.targetId);
            if (targetElem) {
                if (options.isFilePrefix) {
                    targetElem.value = 'file:///' + selectedFile.dataset.name;
                } else {
                    targetElem.value = selectedFile.dataset.name;
                }
                targetElem.dispatchEvent(new Event('focusout'));
            }
            this.hide();
        };

        this.select = function(e) { // 썸네일 선택
            const elem = clickInsideElement(e, 'thumbnail');
            if (elem) {
                const parentElem = elem.parentNode;
                const isSelected = elem.classList.contains('selected');
                if (!isSelected) {
                    for (let i = 0, len = parentElem.childNodes.length ; i< len; i++) {
                        let child = parentElem.childNodes[i];
                        if (child.classList.contains('selected')) {
                            child.classList.remove('selected');
                        }
                    }
                    elem.classList.add('selected');
                }
            }
        };

        this.create = function() { // 모달 draw
            if (typeof this.wrapper !== 'undefined') { return; }
            let backdrop, dialog;

            this.wrapper = document.createElement('div');
            this.wrapper.className = 'thumbnail-modal-wrapper';
            this.wrapper.id = 'thumbnail-modal-wrapper-' + this.id;

            backdrop = document.createElement('div');
            backdrop.className = 'thumbnail-modal-backdrop';

            dialog = document.createElement('div');
            dialog.className = 'thumbnail-modal-dialog';

            // 제목
            if (this.options.title instanceof Element || (typeof this.options.title === "string" && this.options.title != '')) {
                const title = document.createElement('div');
                title.className = 'thumbnail-modal-title';
                if (this.options.title instanceof Element) {
                    title.appendChild(this.options.title);
                } else {
                    title.innerHTML = this.options.title;
                }
                dialog.appendChild(title);
            }

            // 썸네일 이미지 파일 표시
            const body = document.createElement('div');
            body.className = 'thumbnail-modal-body';

            const container = document.createElement('div');
            container.className = 'thumbnail-container';
            body.appendChild(container);
            if (this.options.files.length > 0) {
                for (let i = 0, len = this.options.files.length; i < len; i++) {
                    let file = this.options.files[i];

                    const thumbnail = document.createElement('div');
                    thumbnail.className = 'thumbnail';
                    if (typeof this.options.selectedPath !== 'undefined' &&  this.options.selectedPath.indexOf(file.name) > -1) {
                        thumbnail.classList.add('selected');
                    }
                    thumbnail.setAttribute('data-name', file.name);
                    thumbnail.addEventListener('click', this.select, false);
                    if (this.options.thumbnailDoubleClickUse) {
                        thumbnail.addEventListener('dblclick', this.save.bind(this), false);
                    }

                    container.appendChild(thumbnail);

                    const thumbnailImg = document.createElement('div');
                    if(this.options.type === 'image') {
                        thumbnailImg.className = 'thumbnail-img';
                    } else if (this.options.type === 'icon') {
                        thumbnailImg.className = 'thumbnail-icon';
                        thumbnailImg.style.backgroundSize = '50%';
                    }
                    thumbnailImg.style.backgroundImage = 'url("data:image/' + file.extension +';base64,' + file.data + '")';
                    thumbnail.appendChild(thumbnailImg);

                    if (this.options.isThumbnailInfo) {
                        const thumbnailInfo = document.createElement('div');
                        thumbnailInfo.className = 'thumbnail-info';
                        thumbnail.appendChild(thumbnailInfo);

                        const thumbnailName = document.createElement('p');
                        thumbnailName.className = 'thumbnail-info-text';
                        thumbnailName.innerHTML = `<label>${file.name}</label>`;
                        thumbnailInfo.appendChild(thumbnailName);

                        const thumbnailSize = document.createElement('p');
                        thumbnailSize.className = 'thumbnail-info-text';
                        thumbnailSize.innerHTML = `<label>${file.width} X ${file.height} ${file.size}</label>`;
                        thumbnailInfo.appendChild(thumbnailSize);

                        const thumbnailBottom = document.createElement('div');
                        thumbnailBottom.className = 'thumbnail-bottom';
                        thumbnailBottom.innerHTML = `<label>${i18n.userDateTime(file.updateDt)}</label>`;
                        thumbnail.appendChild(thumbnailBottom);
                    }
                }
            } else { // 썸네일이 존재하지 않을 경우 안내 문구 표시
                let thumbnailNodataTemplate = `
                    <div class="thumbnail-nodata">
                        <label>${i18n.msg('common.msg.noData')}</label>
                    </div>
                `;
                container.insertAdjacentHTML('beforeend', thumbnailNodataTemplate);
            }
            dialog.appendChild(body);
            
            // 하단 버튼
            if (this.options.buttons.length > 0) {
                const buttons = document.createElement('div');
                buttons.className = 'thumbnail-modal-buttons';

                for (let i = 0; i < this.options.buttons.length; i++) {
                    const button = document.createElement('a');
                    button.setAttribute('href', 'javascript:void(0);');

                    button.className = 'thumbnail-modal-button';
                    if (typeof this.options.buttons[i].classes !== 'undefined') {
                        button.className += ' ' + this.options.buttons[i].classes;
                    }

                    if (typeof this.options.buttons[i].content !== 'undefined') {
                        button.innerHTML = this.options.buttons[i].content;
                    }

                    if (typeof this.options.buttons[i].callback === 'undefined') {
                        this.options.buttons[i].callback = function(modal) {
                            modal.hide();
                        };
                    }

                    // button click
                    button.modal = this;
                    button.callback = this.options.buttons[i].callback;
                    button.onclick = function(e) {
                        e.preventDefault();
                        e.stopPropagation();
                        this.callback(this.modal);
                        return false;
                    };

                    // button key binding
                    if (typeof this.options.buttons[i].bindKey === 'number') {
                        this.bind(
                            this.options.buttons[i].bindKey,
                            this.options.buttons[i].callback
                        );
                    }

                    buttons.appendChild(button);
                }
                dialog.appendChild(buttons);
            }
            this.wrapper.appendChild(backdrop);
            this.wrapper.appendChild(dialog);
            document.body.appendChild(this.wrapper);
            this.addKeyListener();
            this.options.onCreate(this);
        };

        this.destroy = function() { // 모달 제거
            if (typeof this.wrapper !== 'undefined') {
                document.body.removeChild(this.wrapper);
                this.wrapper = undefined;
                this.removeKeyListener();
                this.options.onDestroy(this);
            }
        };

        this.create();
    }

    /**
     * 썸네일 모달 초기화.
     *
     * @param userOptions 옵션
     * targetId = 이미지를 선택한 후 확인을 누를 경우 변경되는 input box의 Id
     * selectedPath = 기존 선택된 이미지 파일 경로
     */
    function init(userOptions) {
        // 버튼 다국어 처리
        defaults.buttons[0].content = i18n.msg('common.btn.check');
        defaults.buttons[1].content = i18n.msg('common.btn.close');

        let options = Object.assign({}, defaults, userOptions);
        // 이미지 파일 로드
        aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/images/list?type=' + options.type,
            callbackFunc: function(xhr) {
                const files = JSON.parse(xhr.responseText);
                options.files = files;

                let thumbnailModal = new Modal(options);
                thumbnailModal.show();
            },
            contentType: 'application/json; charset=utf-8'
        });
    }
    exports.init = init;
    Object.defineProperty(exports, '__esModule', {value: true});
})));
