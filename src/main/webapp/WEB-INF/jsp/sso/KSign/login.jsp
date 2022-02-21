<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.ksign.access.wrapper.api.*" %>
<%@ page import="com.ksign.access.wrapper.sso.SSOConf" %>
<%@ page import="com.ksign.access.wrapper.sso.sso10.SSO10Conf" %>
<html>
<head>
<%
  SSORspData rspData = null;
  SSOService ssoService = SSOService.getInstance();
  rspData = ssoService.ssoGetLoginData(request);
  out.print("1. rspData --> " +rspData);
  out.print("2. rspData --> " +rspData.getAttribute("UID"));
%>
</head>
<body>
</body>
</html>
