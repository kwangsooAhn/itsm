<%@ page contentType="text/html; charset=euc-kr" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.ksign.access.wrapper.api.*" %>
<%@ page import="com.ksign.access.wrapper.sso.SSOConf" %>
<%@ page import="com.ksign.access.wrapper.sso.sso10.SSO10Conf" %>
<%!
  // -------------------------------------------------------------------------
  //  [유틸] 로그인 오류 발생 시, alert() 출력 후 다음 페이지로 이동하는 메소드
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
  //  <AP.1> SSO 서비스 객체 획득
  // =========================================================================
  SSORspData rspData = null;
  SSOService ssoService = SSOService.getInstance();

  // =========================================================================
  //  <AP.2> SSO 인증토큰 획득
  // =========================================================================

  rspData = ssoService.ssoGetLoginData(request);
  if(rspData == null || rspData.getResultCode() == -1) {
    // TODO : 에러페이지로 or index.jsp 페이지로 이동
    String alertMsg = "사용자 인증토큰 획득에 실패하였습니다. 초기 접속페이지로 이동합니다.";
    String nextURI = "../index.jsp";
    sendAlert(response, alertMsg, nextURI);
    return;
  }
  // end: added


  // =========================================================================
  //  [AP.4] 최초 접속한 리턴 URL로 이동
  // =========================================================================
  String retURL = (String)request.getParameter("returnurl");
  if(retURL == null){
    // TODO : 에러페이지로 or index.jsp 페이지로 이동

    String encReturnURL = (String) request.getParameter("encreturnurl");
    encReturnURL = ssoService.decryptURL(encReturnURL);


    if(encReturnURL == null) {
      String alertMsg = "리턴 URL 획득에 실패하였습니다. 초기 접속페이지로 이동합니다.";
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
