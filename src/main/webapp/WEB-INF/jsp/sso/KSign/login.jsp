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

    String ACC_DE = rspData.getAttribute("ACC_DE");
    String ACC_DE_SYS = "ITS";
    String rspDataUid = rspData.getAttribute("UID");

    ArrayList<String> accDeList = new ArrayList<String>(Arrays.asList(ACC_DE.split("_")));
    if (!accDeList.contains(ACC_DE_SYS)) {
        try {
            session.invalidate();
        } catch (Exception e) {
            System.out.println("Session already invalidate.");
            System.out.println("[ACCESS_DENIED] 해당 사용자는 접근할 수 없는 시스템입니다. 시스템 코드 : " + ACC_DE_SYS);
        }
%>
<script type="text/javascript">
    alert("[ACCESS_DENIED] 해당 사용자는 접근할 수 없는 시스템입니다. \n시스템 코드 : <%=ACC_DE_SYS%>");
    window.close();
</script>
<%
        return;
    }
%>
<script type="text/javascript">
    let keyModule = '<%=keyModulus%>';
    let keyExponent = '<%=keyExponent%>';
    let rspDataUid = '<%=rspDataUid%>';

    const rsa = new RSAKey();

    window.onload = function () {
        rsa.setPublic(keyModule, keyExponent);

        if (rspDataUid !== '') {
            document.getElementById('userId').value = rsa.encrypt(rspDataUid);
            document.ssoLogin.submit();
        } else {
            window.location.href = '/portals/main';
        }
    }
</script>
<body>
<form name='ssoLogin' method='post' action='/itsm/ssoLogin'>
    <input type="hidden" id="userId" name="userId" value=""/>
</form>
</body>
</html>
