/**
 * 공통 Util
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
/**
 * 비동기 통신 후 Promise 형태로 반환되는 함수
 *
 * @param option 옵션
 * @return 비동기 통신 객체 = Promise 객체
 */

export function doFetch(option) {
    // TODO: Progressbar 추가/삭제
    const fetchParam = { method: option.method.toUpperCase() || 'POST' };
    if (fetchParam.method !== 'GET') {
        fetchParam.headers = {
            'Content-Type': option.contentType || 'application/x-www-form-urlencoded'
        };
    }
    if (option.params) {
        fetchParam.body = option.params;
    }
    return fetch(option.url, fetchParam);
}

/**
 * 비동기 통신 후 Promise 형태로 Json 데이터를 반환하는 함수 
 * @param option 옵션
 * @returns Promise 객체 반환값
 */
export function fetchJson(option) {
    return doFetch(option)
        .then(response => response.json());
}

/**
 * 믹스인을 추가하는 함수
 *
 * @param target 믹스인을 추가할 대상 객체의 prototype
 * @param source 믹스인
 * @return target 믹스인이 추가된 대상
 */
export function importMixin(target, source) {
    for (const key in source) {
        if (source.hasOwnProperty(key)) {
            target[key] = source[key];
        }
    }
    return target;
}

/**
 * 동적으로 디자인 setter 추가하는 함수
 *
 * @param properties 속성
 * @param target 대상 객체의 prototype
 * @return target 대상 객체
 */
export function importDesignedSetter(properties, target) {
    properties.forEach( property => {
        const method = 'set' + property.substr( 0, 1 ).toUpperCase() +
            property.substr( 1, property.length );
        target[method] = function () {
            this.setStyle(property, arguments);
            return this;
        };
    });
}
