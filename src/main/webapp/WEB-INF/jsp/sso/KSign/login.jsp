<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.ksign.access.wrapper.api.*" %>
<%@ page import="com.ksign.access.wrapper.sso.SSOConf" %>
<%@ page import="com.ksign.access.wrapper.sso.sso10.SSO10Conf" %>
<html>
<head>
<%
  // =========================================================================
  //  <AP.1> SSO 서비스 객체 획득
  // =========================================================================
  SSORspData rspData = null;
  SSOService ssoService = SSOService.getInstance();

  // =========================================================================
  //  <AP.2> SSO 인증토큰 획득
  // =========================================================================

  rspData = ssoService.ssoGetLoginData(request);
  out.print("1. rspData --> " +rspData);
%>
</head>
<body>
>> <%=rspData%>
</body>
</html>
