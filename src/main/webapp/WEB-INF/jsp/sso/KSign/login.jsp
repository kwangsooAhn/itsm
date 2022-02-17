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
  String uid = request.getParameter("uid");
  String passwd = request.getParameter("password");

  // =========================================================================
  //  [AP.1] ���̵�/�н����� ����
  // =========================================================================
  if(uid == null || passwd == null){
    String alertMsg = "����� ID�� �Է��Ͻñ� �ٶ��ϴ�.";
    String nextURI = "../index.jsp";
    sendAlert(response, alertMsg, nextURI);
    return;
  }

  // =========================================================================
  //  [AP.2] was ���� ���� ����
  // =========================================================================
  session.setAttribute("uid", uid);
  session.setAttribute("ip", request.getRemoteAddr());

  // ============================= SSO ���� ���� =============================

  // =========================================================================
  //  <SSO.1> SSO ���� ��ü ȹ��
  // =========================================================================
  SSOService ssoService = null;
  ssoService = SSOService.getInstance();
  String reqCtx = request.getContextPath();
  String SSOServer = ssoService.getServerScheme();

  // =========================================================================
  //  <SSO.2> ������ū �߱�: �߰� �Ӽ����� ����
  //    - ����ý��ۿ��� SSO ó�� �� �ʿ���ϴ� �߰� ������ ������ū�� ����
  //      �����ϰ� �ŷ��Ҽ� �ִ� ������� �����ϱ� ���� ���
  //    - eg. �̸�/�μ�/����/����/���� ��
  // =========================================================================
  String avps = "name=���̻���$level=����$";
  // =========================================================================
  //  <SSO.3> ���� ��ū ���� ��û
  //   returnUrl : ���� Ŀ���� ����¡ �ʿ�
  // =========================================================================

  String returnUrl = "http://" + request.getServerName()+":"+ request.getServerPort() + reqCtx + "/sso/index.jsp";
  String agentIp = request.getLocalAddr();

  // case.1. SSO API ������ SSO ������ �����̷�Ʈ ����

  SSORspData rspData = null;
  rspData = ssoService.ssoReqIssueToken(
          request,	               	// ���� ��û ��ü
          response,					// ���� ���� ��ü
          "form-based",				// ���� ���
          uid,						// ���̵�
          avps,						// ������ū : �߰� �Ӽ����� ����
          returnUrl, 				// return url
          agentIp,	// agent ip
          request.getRemoteAddr());			    	// client ip

  if(SSOServer == null) {
    String alertMsg = "SSOServer Down";
    String nextURI = reqCtx + "/sso/index.jsp";
    sendAlert(response, alertMsg, nextURI);
    return;
  }

  // case.2. ���� jsp ����  SSO ������ �����̷�Ʈ ����
	/* String issue =
	ssoService.ssoReqIssueTokenToString(
							  request,	            	// ���� ��û ��ü
        				      response,					// ���� ���� ��ü
                              "form-based",				// ���� ���
                              uid,				    	// ���̵�
                              avps,		            	// ������ū : �߰� �Ӽ����� ����
                              returnUrl,            	// return url
                              request.getRemoteAddr(),	// client ip
                              agentIp);					// agent ip

	if(issue == null ){
		String alertMsg = "����� ������ū ��û���� ������ ���� �Ͽ����ϴ�. �ý��� ��ü �α����� �����մϴ�.";
		String nextURI = "index.jsp";
		sendAlert(response, alertMsg, nextURI);
		return;
	}
    response.sendRedirect(issue); */


  if(true) return;

  // ============================= SSO ���� �� =============================
%>
<html>
<head>
  <title></title>
</head>
<body>
</body>
</html>
