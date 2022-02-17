<%@ page contentType="text/html; charset=euc-kr"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.IOException" %>
<%--<%@ page import="com.ksign.access.wrapper.sso.conf.SSOConfManager" %>--%>
<%--<%@ page import="com.ksign.access.wrapper.api.*"%>--%>
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
//  String uid = request.getParameter("uid");
//  String passwd = request.getParameter("password");
//
//  // =========================================================================
//  //  [AP.1] 아이디/패스워드 검증
//  // =========================================================================
//  if(uid == null || passwd == null){
//    String alertMsg = "사용자 ID를 입력하시기 바랍니다.";
//    String nextURI = "../index.jsp";
//    sendAlert(response, alertMsg, nextURI);
//    return;
//  }
//
//  // =========================================================================
//  //  [AP.2] was 인증 세션 설정
//  // =========================================================================
//  session.setAttribute("uid", uid);
//  session.setAttribute("ip", request.getRemoteAddr());
//
//  // ============================= SSO 설정 시작 =============================
//
//  // =========================================================================
//  //  <SSO.1> SSO 서비스 객체 획득
//  // =========================================================================
//  SSOService ssoService = null;
//  ssoService = SSOService.getInstance();
//  String reqCtx = request.getContextPath();
//  String SSOServer = ssoService.getServerScheme();
//
//  // =========================================================================
//  //  <SSO.2> 인증토큰 발급: 추가 속성정보 설정
//  //    - 응용시스템에서 SSO 처리 시 필요로하는 추가 정보를 인증토큰을 통해
//  //      안전하고 신뢰할수 있는 방식으로 전달하기 위해 사용
//  //    - eg. 이름/부서/직급/권한/역할 등
//  // =========================================================================
//  String avps = "name=케이사인$level=만렙$";
//  // =========================================================================
//  //  <SSO.3> 인증 토큰 생성 요청
//  //   returnUrl : 응용 커스터 마이징 필요
//  // =========================================================================
//
//  String returnUrl = "http://" + request.getServerName()+":"+ request.getServerPort() + reqCtx + "/sso/index.jsp";
//  String agentIp = request.getLocalAddr();
//
//  // case.1. SSO API 내에서 SSO 서버로 리다이렉트 수행
//
//  SSORspData rspData = null;
//  rspData = ssoService.ssoReqIssueToken(
//          request,	               	// 서블릿 요청 객체
//          response,					// 서블릿 응답 객체
//          "form-based",				// 인증 방법
//          uid,						// 아이디
//          avps,						// 인증토큰 : 추가 속성정보 설정
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
//  // case.2. 응용 jsp 에서  SSO 서버로 리다이렉트 수행
//	/* String issue =
//	ssoService.ssoReqIssueTokenToString(
//							  request,	            	// 서블릿 요청 객체
//        				      response,					// 서블릿 응답 객체
//                              "form-based",				// 인증 방법
//                              uid,				    	// 아이디
//                              avps,		            	// 인증토큰 : 추가 속성정보 설정
//                              returnUrl,            	// return url
//                              request.getRemoteAddr(),	// client ip
//                              agentIp);					// agent ip
//
//	if(issue == null ){
//		String alertMsg = "사용자 인증토큰 요청정보 생성에 실패 하였습니다. 시스템 자체 로그인을 수행합니다.";
//		String nextURI = "index.jsp";
//		sendAlert(response, alertMsg, nextURI);
//		return;
//	}
//    response.sendRedirect(issue); */
//
//
//  if(true) return;
//
//  // ============================= SSO 설정 끝 =============================
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
