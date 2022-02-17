<%@ page contentType="text/html; charset=euc-kr"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.IOException" %>
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
//    SSORspData rspData = null;
//    SSOService ssoService = SSOService.getInstance();

  // =========================================================================
  //  <AP.2> SSO 인증토큰 획득
  // =========================================================================

//    rspData = ssoService.ssoGetLoginData(request);
//    if(rspData == null || rspData.getResultCode() == -1) {
//        // TODO : 에러페이지로 or index.jsp 페이지로 이동
//        String alertMsg = "사용자 인증토큰 획득에 실패하였습니다. 초기 접속페이지로 이동합니다.";
//        String nextURI = "/login";
//        sendAlert(response, alertMsg, nextURI);
//        return;
//    }
  // end: added

  // =========================================================================
  //  [AP.3] WAS 인증세션 설정 - 응용 커스터마이징 필요
  // =========================================================================
  //String uid = rspData.getAttribute("KSIGN_FED_USER_ID");
//    String uid = rspData.getAttribute(SSO10Conf.UIDKey);
//    if(uid == null) {
//        uid = rspData.getAttribute("UID");
//    }
//
//    String simpyoungwon = SeedCrypto.encrypt(rspData.getAttribute("UID"), null);
//    SeedCrypto.decrypt(simpyoungwon, null);
//    if(uid != null) {
//        session.setAttribute("uid", uid);
//    }
//
//    String expireMsg = request.getParameter("expire");
//    if(expireMsg != null) {
//        session.setAttribute("expire.date", expireMsg);
//    }
//
//    if (true) return;
%>
<html>
<head>
  <title>Zenius ITSM : Zenius ITSM : 로그인</title>
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
    <!-- 상단 메뉴 -->
    <!-- 메인 -->
    <div class="z-main">
      <div class="z-main-content flex-row">
        <div class="z-vertical-split-left z-login-background">
          <img class="z-img i-logo-white" src="/assets/media/icons/logo/icon_logo_white.svg" width="234" height="40" alt="Zenius ITSM">
          <a href="/portals/main" class="z-button light-secondary main-return float-right">포탈 화면으로 이동</a>
          <span class="z-copyright"><!-- TODO: ? Copyright 2022 Brainzcompany. All rights reserved. --></span>
        </div>
        <div class="z-vertical-split-right z-login-form">
          <h1 class="align-center">로그인</h1>
          <div class="z-login-base">
            <form id="loginForm" name="loginForm" method="post" action="/login"><input type="hidden" name="_csrf" value="4906853b-b19e-4eab-8cf4-5a9ff01514fe">
              <input type="hidden" id="userId" name="userId" value="">
              <input type="hidden" id="password" name="password">
              <input type="hidden" id="email" value="">
            </form>
            <div class="login-base-form flex-column">
              <label>아이디</label>
              <input type="text" id="uid" name="uid" class="z-input text-ellipsis" maxlength="100" required="required">
              <label tabindex="-1">비밀번호</label>
              <input type="password" id="upasswd" name="upasswd" class="z-input text-ellipsis" maxlength="100" required="required">
            </div>
            <button class="z-button primary col-pct-12" id="sendLogin" onclick="onLogin()">로그인</button>
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
