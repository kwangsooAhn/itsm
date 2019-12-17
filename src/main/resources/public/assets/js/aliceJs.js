const aliceJs = {};
/**
 * XMLHttpReqeust 응답시 에러 발생하는 경우 호출
 *
 * @param element 에러를 출력할 장소 element id
 * @param text response text
 * @returns {HTMLTableElement}
 */
aliceJs.xhrErrorResponse = function (element, text) {
    /*<![CDATA[*/
    let elmNode = document.getElementById(element);
    while (elmNode.hasChildNodes()) {
        elmNode.removeChild(elmNode.firstChild)
    }
    const obj = JSON.parse(text);
    const data = {
        // TODO message prop은 못담니?? 확인필요
        // [[#{label.status}]]: obj['status'],
        // [[#{label.error}]]: obj['error'],
        // [[#{label.exceptionType}]]: obj['exceptionType'],
        // [[#{label.message}]]: obj['message'],
        // [[#{label.path}]]: obj['path']
        '제목': obj['error'] + '(' + obj['status'] + ')',
        '에러종류': obj['exceptionType'],
        '메시지': obj['message'],
        '호출 URL': obj['path']
    };
    /*]]>*/
    const table = document.createElement('table');
    for (const [key, value] of Object.entries(data)) {
        const tr = table.insertRow();
        const keyTd = tr.insertCell();
        const valueTd = tr.insertCell();
        keyTd.innerText = key;
        valueTd.innerText = value.toString();
    }
    elmNode.appendChild(table);
};

