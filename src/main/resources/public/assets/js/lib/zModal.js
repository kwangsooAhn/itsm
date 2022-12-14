/**
 * 공통 모달 라이브러리
 *
 * @author woodajung
 * @version 1.0
 */
(function (root, factory) {
    if (typeof define === 'function' && define.amd) {
        define([], factory);
    } else if (typeof exports === 'object') {
        module.exports = factory();
    } else {
        root.modal = factory();
    }
}(this, function () {
    'use strict';

    let defaults = {
        title: '',
        body: '',
        classes: '', // modal class
        /*buttons: [{
            content: "Accept",
            classes: "btn__text--box primary",
            bindKey: 13, //Enter. See https://keycode.info/
            callback: function(modal) {
                modal.hide();
            }
        }, {
            content: "Cancel",
            classes: "btn__text--box secondary",
            bindKey: false,
            callback: function(modal) {
                modal.hide();
            }
        }],*/
        close: {
            closable: false,
            location: 'in',
            bindKey: 27,
            callback: function (modal) {
                modal.hide();
            }
        },
        onShow: function () {},
        onHide: function (modal) {
            modal.destroy();
        },
        onCreate: function () {},
        onDestroy: function () {}
    };

    /**
     * 특정 클래스 목록 조회
     * @param el 대상객체
     */
    function getClasses(el) {
        return el.className.split(' ').filter(function (c) {
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
     * 모달 생성 (gModal.js 참고)
     * https://github.com/jsanahuja/thumbnail-modal
     *
     * @param options 옵션
     */
    return function (options) {
        this.options = Object.assign({}, defaults, options);
        this.id = options.id || Math.random().toString(36).substr(2);
        this.display = false;
        this.bindings = {};

        this.bind = function (key, callback) {
            if (typeof this.bindings[key] !== 'undefined') {
                console.warn('modal: Tried to bind the key ' + key + ' twice. Overriding...');
            }
            this.bindings[key] = callback;
        };

        this.addKeyListener = function () {
            window.currentModal = this;
            window.addEventListener('keydown', this.onKeyPress, false);
        };

        this.removeKeyListener = function () {
            window.currentModal = undefined;
            window.removeEventListener('keydown', this.onKeyPress, false);
        };

        this.onKeyPress = function (e) {
            if (typeof window.currentModal !== 'undefined') {
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

        this.show = function () {
            if (typeof this.wrapper !== 'undefined') {
                const _this = this;
                const modalWrapper = _this.wrapper;
                addClass(modalWrapper, 'block');
                addClass(document.body, 'active');
                const modalDialog = modalWrapper.querySelector('.modal__dialog');
                modalDialog.classList.remove('active');
                _this.animation = setTimeout(function () {
                    clearTimeout(_this.animation);
                    modalDialog.classList.add('active');
                    _this.options.onShow(_this);
                    _this.display = true;
                }, 30);
            }
        };

        this.hide = function () {
            if (typeof this.wrapper !== 'undefined') {
                const _this = this;
                const modalWrapper = _this.wrapper;
                modalWrapper.querySelector('.modal__dialog').classList.remove('active');
                setTimeout(function () {
                    removeClass(modalWrapper, 'block');
                    removeClass(document.body, 'active');
                    _this.options.onHide(_this);
                    _this.display = false;
                }, 150);
            }
        };

        this.create = function () {
            if (typeof this.wrapper !== 'undefined') { return; }

            let backdrop, dialog;

            this.wrapper = document.createElement('div');
            this.wrapper.className = 'modal';
            this.wrapper.id = 'modal-' + this.id;

            backdrop = document.createElement('div');
            backdrop.className = 'modal__backdrop';

            dialog = document.createElement('div');
            dialog.className = 'modal__dialog';
            if (typeof this.options.classes !== 'undefined' && this.options.classes !== '') {
                dialog.className += ' ' + this.options.classes;
            }
            setTimeout(function () {
                dialog.classList.add('active');
            }, 30);

            // 닫기 버튼
            if (typeof this.options.close.closable !== 'undefined' && this.options.close.closable) {
                let close = document.createElement('a');
                close.setAttribute('href', 'javascript:void(0);');

                if (typeof this.options.close.callback === 'undefined') {
                    this.options.close.callback = function () {};
                }

                // close button click
                close.modal = this;
                close.callback = this.options.close.callback;
                close.onclick = function (e) {
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
                    this.options.close.location === 'in') {
                    close.className = 'modal-close-in';
                    dialog.appendChild(close);
                } else {
                    close.className = 'modal-close-out';
                    backdrop.appendChild(close);
                }
            }
            // 제목
            if (this.options.title instanceof Element ||
                (typeof this.options.title === 'string' && this.options.title !== '')) {
                let title = document.createElement('div');
                title.className = 'modal__dialog__header';
                if (this.options.title instanceof Element) {
                    title.appendChild(this.options.title);
                } else {
                    title.innerHTML = this.options.title;
                }
                dialog.appendChild(title);
            }

            let body = document.createElement('div');
            body.className = 'modal__dialog__body';
            if (this.options.body instanceof Element || (this.options.body instanceof Object &&
                this.options.body instanceof DocumentFragment)) {
                body.appendChild(this.options.body);
            } else if (typeof this.options.body === 'string' && this.options.body !== '') {
                body.innerHTML = this.options.body;
            }
            dialog.appendChild(body);

            // 버튼
            if (this.options.buttons.length > 0) {
                let buttons = document.createElement('div');
                buttons.className = 'modal__dialog__footer btn__list flex-row float-right align-items-end';

                for (let i = 0, len = this.options.buttons.length; i < len; i++) {
                    let button = document.createElement('button');
                    button.type = 'button';

                    button.className = 'modal-button';

                    if (typeof this.options.buttons[i].id !== 'undefined') {
                        button.id = this.options.buttons[i].id;
                    }

                    if (typeof this.options.buttons[i].classes !== 'undefined') {
                        button.className += ' ' + this.options.buttons[i].classes;
                    }

                    if (typeof this.options.buttons[i].content !== 'undefined') {
                        button.innerHTML = this.options.buttons[i].content;
                    }

                    if (typeof this.options.buttons[i].callback === 'undefined') {
                        this.options.buttons[i].callback = function (modal) {
                            modal.hide();
                        };
                    }

                    // button click
                    button.modal = this;
                    button.callback = this.options.buttons[i].callback;
                    button.onclick = function (e) {
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

        this.destroy = function () {
            if (typeof this.wrapper !== 'undefined') {
                const _this = this;
                const modalWrapper = this.wrapper;
                this.wrapper = undefined;
                setTimeout(function () {
                    document.body.removeChild(modalWrapper);
                    _this.removeKeyListener();
                    _this.options.onDestroy(_this);
                }, 150);
            }
        };

        this.create();
    };
}));
