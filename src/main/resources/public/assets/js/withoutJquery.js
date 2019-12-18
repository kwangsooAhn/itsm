/**
 * 
 */


function createXmlHttpRequestObject(method,url,async) {
    // will store the reference to the XMLHttpRequest object
    var xmlHttp;
    var token;
    var metas = document.getElementsByTagName('meta');
    
    for (var i = 0; i < metas.length; i++) {
		if (metas[i].getAttribute('name') === '_csrf') {
			token = metas[i].getAttribute('content');
		}
	}
    
    // if running Internet Explorer
    if (window.ActiveXObject) {
        try {
            xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
        } catch (e) {
            xmlHttp = false;
        }
    }
    // if running Mozilla or other browsers
    else {
        try {
            xmlHttp = new XMLHttpRequest();
        } catch (e) {
            xmlHttp = false;
        }
    }
    // return the created object or display an error message
    if (!xmlHttp)
        alert("Error creating the XMLHttpRequest object.");
    else
    	alert(method);
    alert(url);
    alert(async);
    	xmlHttp.open(method,url,async);
        xmlHttp.setRequestHeader('X-CSRF-TOKEN',token);
        return xmlHttp;
}