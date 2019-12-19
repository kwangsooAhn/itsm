const aliceJs = {};

/**
 * XMLHttpReqeust 응답시 에러 발생하는 경우 호출
 *
 * @param elementId 에러를 출력할 장소 element id
 * @param text response text
 * @returns {HTMLTableElement}
 */
aliceJs.xhrErrorResponse = function (elementId, text) {
    let elmNode = document.getElementById(elementId);
    while (elmNode.hasChildNodes()) {
        elmNode.removeChild(elmNode.firstChild);
    }
    const data = JSON.parse(text);
    let messages = [
        {key: '제목', text: data.error + '(' + data.status + ')'},
        {key: '메시지', text: data.message},
        {key: '호출 URL', text: data.path}
    ];
    if (data.hasOwnProperty('exceptionType')) {
        messages.push({key: '예외 종류', text: data.exceptionType});
    }
    if (data.hasOwnProperty('knownError')) {
        messages.push({key: '정의된 에러', text: data.knownError});
    }
    const table = document.createElement('table');
    messages.forEach(function(obj) {
        const tr = table.insertRow();
        const keyTd = tr.insertCell();
        const valueTd = tr.insertCell();
        keyTd.innerText = obj.key;
        valueTd.innerText = obj.text;
    });
    elmNode.appendChild(table);
};

