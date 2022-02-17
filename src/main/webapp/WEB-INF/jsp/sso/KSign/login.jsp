<%@ page contentType="text/html; charset=euc-kr" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.ksign.access.wrapper.api.*" %>
<%@ page import="com.ksign.access.wrapper.sso.SSOConf" %>
<%@ page import="com.ksign.access.wrapper.sso.sso10.SSO10Conf" %>
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
  // =========================================================================
  //  <AP.1> SSO ���� ��ü ȹ��
  // =========================================================================
  SSORspData rspData = null;
  SSOService ssoService = SSOService.getInstance();

  // =========================================================================
  //  <AP.2> SSO ������ū ȹ��
  // =========================================================================

  rspData = ssoService.ssoGetLoginData(request);
  if(rspData == null || rspData.getResultCode() == -1) {
    // TODO : ������������ or index.jsp �������� �̵�
    String alertMsg = "����� ������ū ȹ�濡 �����Ͽ����ϴ�. �ʱ� ������������ �̵��մϴ�.";
    String nextURI = "../index.jsp";
    sendAlert(response, alertMsg, nextURI);
    return;
  }
  // end: added


  // =========================================================================
  //  [AP.4] ���� ������ ���� URL�� �̵�
  // =========================================================================
  String retURL = (String)request.getParameter("returnurl");
  if(retURL == null){
    // TODO : ������������ or index.jsp �������� �̵�

    String encReturnURL = (String) request.getParameter("encreturnurl");
    encReturnURL = ssoService.decryptURL(encReturnURL);


    if(encReturnURL == null) {
      String alertMsg = "���� URL ȹ�濡 �����Ͽ����ϴ�. �ʱ� ������������ �̵��մϴ�.";
      String nextURI = "../index.jsp";
      sendAlert(response, alertMsg, nextURI);
      return;
    } else {
      retURL = encReturnURL;
    }
  }
  response.sendRedirect(retURL);
  if(true) return;
%>
<html>
<head>
  <title></title>
</head>
<body>
</body>
</html>
