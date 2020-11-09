/**
 * 공통 모달 라이브러리
 *
 * @author woodajung
 * @version 1.0
 */
(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.modal = global.modal || {})));
}(this, (function (exports) {
    'use strict';

    let defaults = {
        title: '',
        body: '',
        width: 1000,         // 모달 너비
        height: 775,         // 모달 높이
        buttons: [{
            content: "Accept",
            classes: "default-line",
            bindKey: 13, //Enter. See https://keycode.info/
            callback: function(modal) {
                modal.hide();
            }
        }, {
            content: "Cancel",
            classes: "default-line",
            bindKey: false,
            callback: function(modal) {
                modal.hide();
            }
        }],
        close: {
            closable: true,
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
            var classes = getClasses(el);
            classes.splice(classes.indexOf(className), 1);
            el.className = classes.join(' ');
        }
    }
   /**
     * 모달 생성 (gModal.js 참고)
     * https://github.com/jsanahuja/thumbnail-modal
     *
     * @param options 옵션
     */
    function Modal(options) {
        this.id = options.id || Math.random().toString(36).substr(2);
        this.options = options;
        this.display = false;
        this.bindings = {};

        this.bind = function(key, callback) {
            if (typeof this.bindings[key] !== 'undefined') {
                console.warn('modal: Tried to bind the key ' + key + ' twice. Overriding...');
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
            if (
                typeof window.currentModal !== 'undefined' &&
                window.currentModal instanceof gModal
            ) {
                var _that = window.currentModal;
                if (!_that.display) return;
                var keyCode = e.keyCode || e.which;
                var keys = Object.keys(_that.bindings);
                for (var i = 0; i < keys.length; i++) {
                    if (parseInt(keys[i]) === keyCode) {
                        e.preventDefault();
                        e.stopPropagation();
                        _that.bindings[keys[i]](_that);
                        return false;
                    }
                }
            }
        };

        this.show = function() {
            if (typeof this.wrapper !== 'undefined') {
                addClass(this.wrapper, 'modal-active');
                addClass(document.body, 'modal-active');
                this.display = true;
                this.options.onShow(this);
            }
        };

        this.hide = function() {
            if (typeof this.wrapper !== 'undefined') {
                removeClass(this.wrapper, 'modal-active');
                removeClass(document.body, 'modal-active');
                this.display = false;
                this.options.onHide(this);
            }
        };

        this.create = function() {
            if (typeof this.wrapper !== 'undefined') { return; }

            let backdrop, dialog;

            this.wrapper = document.createElement('div');
            this.wrapper.className = 'modal-wrapper';
            this.wrapper.id = 'modal-wrapper-' + this.id;

            backdrop = document.createElement('div');
            backdrop.className = 'modal-backdrop';

            dialog = document.createElement('div');
            dialog.className = 'modal-dialog';
            // 너비 높이 지정
            dialog.style.width = this.options.width;
            dialog.style.height = this.options.height;
            // 닫기 버튼
            if (typeof this.options.close.closable !== 'undefined' && this.options.close.closable) {
                let close = document.createElement('a');
                close.setAttribute('href', 'javascript:void(0);');

                if (typeof this.options.close.callback === 'undefined') {
                    this.options.close.callback = function() {};
                }

                // close button click
                close.modal = this;
                close.callback = this.options.close.callback;
                close.onclick = function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                    this.callback(this.modal);
                    return false;
                };

                // close key binding
                if (typeof this.options.close.bindKey === 'number') {
                    this.bind(this.options.close.bindKey, this.options.close.callback);
                }

                if (typeof this.options.close.location === 'undefined' ||
                    this.options.close.location == 'in') {
                    close.className = 'modal-close-in';
                    dialog.appendChild(close);
                } else {
                    close.className = 'modal-close-out';
                    backdrop.appendChild(close);
                }
            }
            // 제목
            if (this.options.title instanceof Element ||
                (typeof this.options.title === "string" && this.options.title != '')) {
                let title = document.createElement('div');
                title.className = 'modal-title';
                if (this.options.title instanceof Element) {
                    title.appendChild(this.options.title);
                } else {
                    title.innerHTML = this.options.title;
                }
                dialog.appendChild(title);
            }

            if (this.options.body instanceof Element ||
                (typeof this.options.body === "string" && this.options.body != '')) {
                let body = document.createElement('div');
                body.className = 'modal-body';
                if (this.options.body instanceof Element) {
                    body.appendChild(this.options.body);
                } else {
                    body.innerHTML = this.options.body;
                }
                dialog.appendChild(body);
            }
            // 버튼
            if (this.options.buttons.length > 0) {
                let buttons = document.createElement('div');
                buttons.className = 'modal-buttons flex-row float-right';

                for (let i = 0, len = this.options.buttons.length; i < len; i++) {
                    let button = document.createElement('button');
                    button.type = 'button';

                    button.className = 'modal-button';
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

        this.destroy = function() {
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
     * 모달 초기화.
     *
     * @param option 옵션
     */
    function init(options) {
      // 옵션 재할당
      let defaultOptions = JSON.parse(JSON.stringify(defaults));
      let mergeOptions = aliceJs.mergeObject(options, defaultOptions);
      console.log(mergeOptions);

      // 모달 생성
      let modal = new Modal(mergeOptions);
      modal.show();
    }

    exports.init = init;
    Object.defineProperty(exports, '__esModule', {value: true});
})));