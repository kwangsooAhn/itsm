<%@ page contentType="text/html; charset=euc-kr" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.ksign.access.wrapper.api.*" %>
<%@ page import="com.ksign.access.wrapper.sso.SSOConf" %>
<%@ page import="com.ksign.access.wrapper.sso.sso10.SSO10Conf" %>
<%@ page import="com.ksign.access.wrapper.api.SSOService" %>
<%!
  // -------------------------------------------------------------------------
  //  [유틸] 로그인 오류 발생 시, alert() 출력 후 다음 페이지로 이동하는 메소드
  // -------------------------------------------------------------------------
//  public void sendAlert(HttpServletResponse resp, String alertMsg, String nextURI) throws IOException {
//
//    alertMsg = alertMsg.replaceAll("\"", "\\\"");
//    alertMsg = alertMsg.replaceAll("\r", "\\r");
//    alertMsg = alertMsg.replaceAll("\n", "\\n");
//
//    String msg =
//            "<script language=\"javascript\">\r\n" +
//                    "  alert(\"" + alertMsg + "\");\r\n" +
//                    "  top.location.href = \"" + nextURI + "\";\r\n" +
//                    "</script>\r\n";
//
//    resp.setCharacterEncoding("MS949");
//    // resp.setContentLength();
//
//    PrintWriter out = resp.getWriter();
//    out.println(msg);
//    out.flush();
//  }
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
  out.print("1. rspData --> " +rspData);
  out.print("2. rspData.getResultCode() --> " +rspData.getResultCode());

//  if(rspData == null || rspData.getResultCode() == -1) {
//    // TODO : 에러페이지로 or index.jsp 페이지로 이동
//    String alertMsg = "사용자 인증토큰 획득에 실패하였습니다. 초기 접속페이지로 이동합니다.";
//    String nextURI = "../index.jsp";
//    sendAlert(response, alertMsg, nextURI);
//    return;
//  }
  // end: added

  // =========================================================================
  //  [AP.3] WAS 인증세션 설정 - 응용 커스터마이징 필요
  // =========================================================================
  //String uid = rspData.getAttribute("KSIGN_FED_USER_ID");
  String uid = rspData.getAttribute(SSO10Conf.UIDKey);
//  if(uid == null) {
//    uid = rspData.getAttribute("UID");
//  }
  out.print("3. rspData.getAttribute(\"KSIGN_FED_USER_ID\") --> " +rspData.getAttribute("KSIGN_FED_USER_ID"));
  out.print("4. rspData.getAttribute(SSO10Conf.UIDKey) --> " +rspData.getAttribute(SSO10Conf.UIDKey));


  String simpyoungwon = SeedCrypto.encrypt(rspData.getAttribute("UID"), null);
  SeedCrypto.decrypt(simpyoungwon, null);
  if(uid != null) {
    session.setAttribute("uid", uid);
  }

  String expireMsg = request.getParameter("expire");
  if(expireMsg != null) {
    session.setAttribute("expire.date", expireMsg);
  }
  //...
  if(true) return;
%>
<html>
<head>
  <title></title>
</head>
<body>
</body>
</html>
