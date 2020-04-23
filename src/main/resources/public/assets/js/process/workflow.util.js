const workflowUtil = {};

/**
 * generate UUID.
 * Public Domain/MIT
 *
 * <HISTORY>
 * UUID 의 첫 번째가 숫자로 시작할 경우,
 * querySelector 로 id 검색 사용 시 오류 발생하여
 * 첫 번째 글자를 alice 를 의미하는 'a'로 시작하도록 고정함.
 *
 * @returns {string} UUID
 */
workflowUtil.generateUUID = function() {
    let d = new Date().getTime(); //Timestamp
    let d2 = (performance && performance.now && (performance.now() * 1000)) || 0; //Time in microseconds since page-load or 0 if unsupported
    return 'axxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx'.replace(/[x]/g, function(c) {
        let r = Math.random() * 16;//random number between 0 and 16
        if(d > 0){ //Use timestamp until depleted
            r = (d + r) % 16 | 0;
            d = Math.floor(d / 16);
        } else { //Use microseconds since page-load if supported
            r = (d2 + r) % 16 | 0;
            d2 = Math.floor(d2 / 16);
        }
        return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
}

/**
 * 두 개의 json 데이터가 동일한 지 비교한 후 boolean 을 리턴 한다.
 *
 * @param obj1 비교 대상 JSON 데이터 1
 * @param obj2 비교 대상 JSON 데이터 2
 * @return {boolean} 데이터 일치 여부 (true: 일치, false: 불일치)
 */
workflowUtil.compareJson = function(obj1, obj2) {
    if (!Object.keys(obj2).every(function(key) { return obj1.hasOwnProperty(key); })) {
        return false;
    }
    return Object.keys(obj1).every(function(key) {
        if ((typeof obj1[key] === 'object') && (typeof obj2[key] === 'object')) {
            return this.compareJson(obj1[key], obj2[key]);
        } else {
            return obj1[key] === obj2[key];
        }
    });
}

/**
 * polyfill.
 */
workflowUtil.polyfill = function() {
    if (!Math.hypot) {
        Math.hypot = function (x, y) {
            // https://bugzilla.mozilla.org/show_bug.cgi?id=896264#c28
            let max = 0,
                s = 0;
            for (let i = 0; i < arguments.length; i += 1) {
                let arg = Math.abs(Number(arguments[i]));
                if (arg > max) {
                    s *= (max / arg) * (max / arg);
                    max = arg;
                }
                s += arg === 0 && max === 0 ? 0 : (arg / max) * (arg / max);
            }
            return max === 1 / 0 ? 1 / 0 : max * Math.sqrt(s);
        };
    }
}