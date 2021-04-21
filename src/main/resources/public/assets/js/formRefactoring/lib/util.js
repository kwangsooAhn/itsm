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
 * Object 객체이며 true, 아니면 false를 반환
 * @param target 대상
 * @returns {Boolean} boolean
 */
export function isObject(target) {
    return (target && typeof target === 'object' && !Array.isArray(target));
}

/**
 * Object 객체가 빈 객체인지 체크
 * @param target 대상
 * @returns {Boolean} boolean
 */
export function isEmptyObject(target) {
    return Object.keys(target).length === 0 && target.constructor === Object;
}

/**
 * Merge a `source` object to a `target` recursively
 * @param target target 객체
 * @param source source 객체
 */
export function mergeObject(target, source) {
    if (isObject(target) && isObject(source)) {
        Object.keys(source).forEach(function(key) {
            if (isObject(source[key])) {
                if (!target[key]) { Object.assign(target, { [key]: {} }); }
                mergeObject(target[key], source[key]);
            } else {
                Object.assign(target, JSON.parse(JSON.stringify({ [key]: source[key] })));
            }
        });
    }
    return target;
}
/**
 * Swap object 1 and object2
 * @param index1 인덱스
 * @param index2 인덱스
 */
export function swapObject(array, index1, index2) {
    let temp = array[index1];
    array[index1] = array[index2];
    array[index2] = temp;
}
/**
 * Move object
 * @param index1 old index
 * @param index2 new index
 */
export function moveObject(array, index1, index2) {
    while (index1 < 0) {
        index1 += array.length;
    }
    while (index2 < 0) {
        index2 += array.length;
    }
    if (index2 >= array.length) {
        let k = index2 - array.length + 1;
        while (k--) {
            array.push(undefined);
        }
    }
    array.splice(index2, 0, array.splice(index1, 1)[0]);
}
