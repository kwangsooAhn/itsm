/**
 * 이벤트 Class.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

export class EventHandler {
    constructor() {
        this.events = {};
    }
    /**
     * 이벤트 추가
     * @param type 이벤트 타입
     * @param callback 콜백함수
     * @param scope 이벤트 실행 컨텍스트
     */
    on(type, callback, scope, ...args) {
        if (typeof this.events[type] === 'undefined') {
            this.events[type] = [];
        }
        this.events[type].push({scope, callback, args});
    }
    /**
     * 이벤트 삭제
     * @param type 이벤트 타입
     * @param callback 콜백함수
     * @param scope 이벤트 실행 컨텍스트
     */
    off(type, callback, scope) {
        if (typeof this.events[type] === 'undefined') { return false; }
        // 전달 된 이벤트와 동일하지 않은 모든 이벤트는 유지된다.
        const filterFn = (event) => event.scope !== scope || event.callback !== callback;
        this.events[type] = this.events[type].filter(filterFn);
    }
    /**
     * 모든 이벤트 삭제
     * @param type 이벤트 타입
     * @param callback 콜백함수
     * @param scope 이벤트 실행 컨텍스트
     */
    clear() {
        for (const type of Object.getOwnPropertyNames(this.events)) {
            delete this.events[type];
        }
    }
    /**
     * 등록된 이벤트 인지 체크
     * @param type 이벤트 타입
     * @param callback 콜백함수
     * @param scope 이벤트 실행 컨텍스트
     */
    has(type, callback, scope) {
        if (typeof this.events[type] === 'undefined') { return false; }
        
        let numOfCallbacks = this.events[type].length;
        if (callback === undefined && scope === undefined) {
            return numOfCallbacks > 0;
        }

        const conditionFn = (event) => {
            const scopeIsSame = scope ? event.scope === scope : true;
            const callbackIsSame = event.callback === callback;
            if (scopeIsSame && callbackIsSame) {
                return true;
            }
        };
        return this.events[type].some(conditionFn);
    }
    /**
     * 이벤트 실행 (trigger)
     * @param type 이벤트 타입
     * @param target 호출 대상
     */
    emit(type, target, ...args) {
        if (typeof this.events[type] === 'undefined') { return false; }

        let bag = {type, target};
        const events = this.events[type].slice();

        for (const event of events) {
            if (event && event.callback) {
                event.callback.apply(event.scope, [bag, ...args, ...event.args]);
            }
        }
    }
}

export const eventHandler = new EventHandler();