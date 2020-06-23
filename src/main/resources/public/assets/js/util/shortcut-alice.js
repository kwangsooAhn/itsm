/**
 * @projectDescription Shortcut Library
 *
 * @author woodajung
 * @version 1.0
 */
(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.shortcut = global.shortcut || {})));
}(this, (function (exports) {
    'use strict';

    const metaKeyCode = {
        13: 'enter',
        27: 'esc',
        33: 'pageup',
        34: 'pagedown',
        35: 'end',
        36: 'home',
        37: 'left',
        38: 'up',
        39: 'right',
        40: 'down',
        45: 'insert',
        46: 'delete'
    };
    const keySeparator = '+';
    let eventTarget = document,
        keyMap = {},
        shortcutExists = [],
        keyDown = {};

    function checkIsInput(target) {
        let name = target.tagName.toLowerCase();
        let type = target.type;
        return (name === 'input' && ['text', 'password', 'file', 'search'].indexOf(type) > -1) || name === 'textarea';
    }

    function run(type, e) {

        let pressKey = {
            'shift': e.shiftKey,
            'ctrl': e.ctrlKey,
            'alt': e.altKey
        };
        let keyValue = String.fromCharCode(e.which).toLowerCase();
        if (metaKeyCode[e.which]) {
            keyValue = metaKeyCode[e.which];
        }
        Object.keys(keyMap).forEach(function(key) {
            let splitKey = keyMap[key].splitKey;
            let maskKey = {
                'shift'      : false,
                'ctrl'       : false,
                'alt'        : false
            };
            let matches = 0;
            for (let i = 0; i < splitKey.length; i++) {
                if (splitKey[i] === 'ctrl' || splitKey[i] === 'shift' || splitKey[i] === 'alt') {
                    maskKey[splitKey[i]] = true;
                    matches++;
                } else if (splitKey[i] === keyValue) {
                    matches++;
                }
            }
            const target = e.target;
            if (type === 'down' && splitKey.length === matches &&
                    pressKey.ctrl === maskKey.ctrl &&
                    pressKey.shift === maskKey.shift &&
                    pressKey.alt === maskKey.alt &&
                    (keyMap[key].force || !checkIsInput(target)) &&
                    keyMap[key].command !== '') {
                e.preventDefault();
                if (checkIsInput(target)) { target.blur(); } // focus out 처리
                ( new Function('return ' + keyMap[key].command) )();
            }
        });
    }

    function onKeyDownShortcutHandler(e) {
        if (!keyDown[e.which]) {
            run('down', e);
        }
        keyDown[e.which] = true;
        run('hold', e);
    }

    function onKeyUpShortcutHandler(e) {
        keyDown[e.which] = false;
        run('up', e);
    }

    /**
     * 단축키 등록
     * @param keys 단축키
     * @param callback 단축키 실행시 호출할 함수
     * @param force 속성창에서 단축키 실행 여부
     */
    function add(keys, callback, force) {
        if (shortcutExists[keys] === true) { return false; } //중복키 방지

        let keyList = keys.toLowerCase().replace(/\s+/g, '').split(','); //여러개 입력된 경우
        for (let i = 0; i < keyList.length; i++) {
            keyMap[keyList[i]] = {
                splitKey: keyList[i].split(keySeparator),
                command: callback,
                force: force
            };
        }
        shortcutExists[keys] = true;
    }

    /**
     * 단축키 초기화
     * @param elem 이벤트를 등록할 대상 (default document)
     */
    function init(elem) {
        if (typeof elem !== 'undefined') { eventTarget = elem; }

        eventTarget.addEventListener('keydown', onKeyDownShortcutHandler, false);
        eventTarget.addEventListener('keyup', onKeyUpShortcutHandler, false);
    }

    exports.init = init;
    exports.add = add;

    Object.defineProperty(exports, '__esModule', { value: true });
})));
