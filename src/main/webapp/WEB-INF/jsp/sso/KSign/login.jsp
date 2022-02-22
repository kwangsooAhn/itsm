<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.ksign.access.wrapper.api.*" %>
<%@ page import="com.ksign.access.wrapper.sso.SSOConf" %>
<%@ page import="com.ksign.access.wrapper.sso.sso10.SSO10Conf" %>
<html>
<head>
    <script src="/assets/vendors/rsa/rsa.js"></script>
    <script src="/assets/vendors/rsa/jsbn.js"></script>
    <script src="/assets/vendors/rsa/prng4.js"></script>
    <script src="/assets/vendors/rsa/rng.js"></script>
</head>
<%
    Object keyModulus = request.getAttribute("_publicKeyModulus");
    Object keyExponent = request.getAttribute("_publicKeyExponent");

    SSORspData rspData = null;
    SSOService ssoService = SSOService.getInstance();
    rspData = ssoService.ssoGetLoginData(request);

    String rspDataUid = rspData.getAttribute("UID");
%>
<script type="text/javascript">
    let keyModule = '<%=keyModulus%>'
    let keyExponent = '<%=keyExponent%>'
    let rspDataUid = '<%=rspDataUid%>'

    const rsa = new RSAKey();

    window.onload = function () {
        rsa.setPublic(keyModule, keyExponent);

        if (rspDataUid !== '') {
            document.getElementById('userId').value = rsa.encrypt(rspDataUid);
            document.ssoLogin.submit();
        } else {
            window.location.href='/portals/main';
        }
    }
</script>
<body>
<form name='ssoLogin' method='post' action='/itsm/ssoLogin'>
    <input type="hidden" id="userId" name="userId" value=""/>
</form>
</body>
</html>
