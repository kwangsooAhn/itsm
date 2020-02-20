const workflowUtil = {};

/**
 * generate UUID.
 * Public Domain/MIT
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