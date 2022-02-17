<%@ page contentType="text/html; charset=euc-kr"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.IOException" %>
<%--<%@ page import="com.ksign.access.wrapper.sso.conf.SSOConfManager" %>--%>
<%--<%@ page import="com.ksign.access.wrapper.api.*"%>--%>
<%!
  // -------------------------------------------------------------------------
  //  [��ƿ] �α��� ���� �߻� ��, alert() ��� �� ���� �������� �̵��ϴ� �޼ҵ�
  // -------------------------------------------------------------------------
  public void sendAlert(HttpServletResponse resp, String alertMsg, String nextURI) throws IOException {

    alertMsg = alertMsg.replaceAll("\"", "\\\"");
    alertMsg = alertMsg.replaceAll("\r", "\\r");
    alertMsg = alertMsg.replaceAll("\n", "\\n");

    String msg =
            "<script language=\"javascript\">\r\n" +
                    "  alert(\"" + alertMsg + "\");\r\n" +
                    "  top.location.href = \"" + nextURI + "\";\r\n" +
                    "</script>\r\n";

    resp.setCharacterEncoding("MS949");
    // resp.setContentLength();

    PrintWriter out = resp.getWriter();
    out.println(msg);
    out.flush();
  }
%>
<%
//  String uid = request.getParameter("uid");
//  String passwd = request.getParameter("password");
//
//  // =========================================================================
//  //  [AP.1] ���̵�/�н����� ����
//  // =========================================================================
//  if(uid == null || passwd == null){
//    String alertMsg = "����� ID�� �Է��Ͻñ� �ٶ��ϴ�.";
//    String nextURI = "../index.jsp";
//    sendAlert(response, alertMsg, nextURI);
//    return;
//  }
//
//  // =========================================================================
//  //  [AP.2] was ���� ���� ����
//  // =========================================================================
//  session.setAttribute("uid", uid);
//  session.setAttribute("ip", request.getRemoteAddr());
//
//  // ============================= SSO ���� ���� =============================
//
//  // =========================================================================
//  //  <SSO.1> SSO ���� ��ü ȹ��
//  // =========================================================================
//  SSOService ssoService = null;
//  ssoService = SSOService.getInstance();
//  String reqCtx = request.getContextPath();
//  String SSOServer = ssoService.getServerScheme();
//
//  // =========================================================================
//  //  <SSO.2> ������ū �߱�: �߰� �Ӽ����� ����
//  //    - ����ý��ۿ��� SSO ó�� �� �ʿ���ϴ� �߰� ������ ������ū�� ����
//  //      �����ϰ� �ŷ��Ҽ� �ִ� ������� �����ϱ� ���� ���
//  //    - eg. �̸�/�μ�/����/����/���� ��
//  // =========================================================================
//  String avps = "name=���̻���$level=����$";
//  // =========================================================================
//  //  <SSO.3> ���� ��ū ���� ��û
//  //   returnUrl : ���� Ŀ���� ����¡ �ʿ�
//  // =========================================================================
//
//  String returnUrl = "http://" + request.getServerName()+":"+ request.getServerPort() + reqCtx + "/sso/index.jsp";
//  String agentIp = request.getLocalAddr();
//
//  // case.1. SSO API ������ SSO ������ �����̷�Ʈ ����
//
//  SSORspData rspData = null;
//  rspData = ssoService.ssoReqIssueToken(
//          request,	               	// ���� ��û ��ü
//          response,					// ���� ���� ��ü
//          "form-based",				// ���� ���
//          uid,						// ���̵�
//          avps,						// ������ū : �߰� �Ӽ����� ����
//          returnUrl, 				// return url
//          agentIp,	// agent ip
//          request.getRemoteAddr());			    	// client ip
//
//  if(SSOServer == null) {
//    String alertMsg = "SSOServer Down";
//    String nextURI = reqCtx + "/sso/index.jsp";
//    sendAlert(response, alertMsg, nextURI);
//    return;
//  }
//
//  // case.2. ���� jsp ����  SSO ������ �����̷�Ʈ ����
//	/* String issue =
//	ssoService.ssoReqIssueTokenToString(
//							  request,	            	// ���� ��û ��ü
//        				      response,					// ���� ���� ��ü
//                              "form-based",				// ���� ���
//                              uid,				    	// ���̵�
//                              avps,		            	// ������ū : �߰� �Ӽ����� ����
//                              returnUrl,            	// return url
//                              request.getRemoteAddr(),	// client ip
//                              agentIp);					// agent ip
//
//	if(issue == null ){
//		String alertMsg = "����� ������ū ��û���� ������ ���� �Ͽ����ϴ�. �ý��� ��ü �α����� �����մϴ�.";
//		String nextURI = "index.jsp";
//		sendAlert(response, alertMsg, nextURI);
//		return;
//	}
//    response.sendRedirect(issue); */
//
//
//  if(true) return;
//
//  // ============================= SSO ���� �� =============================
%>
<html>
<head>
  <title>Zenius ITSM : Zenius ITSM : �α���</title>
  <meta name="_csrf" content="76196e3a-e06e-4ae2-b482-a5925deeb141">
  <meta name="_csrf_header" content="X-CSRF-TOKEN">
  <!-- meta tags -->
  <link rel="shortcut icon" href="/assets/media/images/zenius.ico" type="image/x-icon">
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <link href="/assets/css/portal.css" rel="stylesheet">
  <link href="/assets/vendors/quill/quill.snow.css" rel="stylesheet">
  <link href="/assets/vendors/toastify/toastify.css" rel="stylesheet">
  <link href="/assets/vendors/overlayScrollbars/OverlayScrollbars.css" rel="stylesheet">
</head>
<body>
<div class="z-wrapper">
  <div class="z-portal-container">
    <!-- ��� �޴� -->
    <!-- ���� -->
    <div class="z-main">
      <div class="z-main-content flex-row">
        <div class="z-vertical-split-left z-login-background">
          <img class="z-img i-logo-white" src="/assets/media/icons/logo/icon_logo_white.svg" width="234" height="40" alt="Zenius ITSM">
          <a href="/portals/main" class="z-button light-secondary main-return float-right">��Ż ȭ������ �̵�</a>
          <span class="z-copyright"><!-- TODO: ? Copyright 2022 Brainzcompany. All rights reserved. --></span>
        </div>
        <div class="z-vertical-split-right z-login-form">
          <h1 class="align-center">�α���</h1>
          <div class="z-login-base">
            <form id="loginForm" name="loginForm" method="post" action="/login"><input type="hidden" name="_csrf" value="4906853b-b19e-4eab-8cf4-5a9ff01514fe">
              <input type="hidden" id="userId" name="userId" value="">
              <input type="hidden" id="password" name="password">
              <input type="hidden" id="email" value="">
            </form>
            <div class="login-base-form flex-column">
              <label>���̵�</label>
              <input type="text" id="uid" name="uid" class="z-input text-ellipsis" maxlength="100" required="required">
              <label tabindex="-1">��й�ȣ</label>
              <input type="password" id="upasswd" name="upasswd" class="z-input text-ellipsis" maxlength="100" required="required">
            </div>
            <button class="z-button primary col-pct-12" id="sendLogin" onclick="onLogin()">�α���</button>
          </div>
          <div class="z-login-other flex-row justify-content-center align-items-center">
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
