<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE >
<html>
<head>
    <meta charset="utf-8">
    <title>${fns:getConfig('productName')}</title>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <link rel="stylesheet" href="${ctxStatic}/jingle/css/Jingle.css">
    <link rel="stylesheet" href="${ctxStatic}/jingle/css/app.css">
    <link rel="stylesheet" href="${ctxStatic}/jingle/css/wx.css">
</head>
<body>
<div id="aside_container">
</div>
<div id="section_container">
    <section id="result_section" class="active">
        <header>
            <h1 class="title">${fns:getConfig('productName')}</h1>
        </header>
        <article data-scroll="true" id="result_article">
            <div class="indented">
                <form id="resultForm">
                    <div>&nbsp;</div>

                        <c:choose>
                            <c:when test="${empty zybxBalanceList}">
                                <b class="noDataTitle">&nbsp;&nbsp;&nbsp;&nbsp;暂时没有数据</b>
                                <b class="noDataTitle">&nbsp;&nbsp;&nbsp;&nbsp;或是您输入有误，请重新输入</b>
                            </c:when>
                            <c:otherwise>
                                 <div class="input-group">
                                <c:forEach items="${zybxBalanceList}" var="zybxBalance" varStatus="idx">
                                    <div class="input-row">
                                        <label>序号</label>
                                        <label>${idx.index + 1}</label>
                                    </div>
                                    <div class="input-row">
                                        <label>身份证号</label>
                                        <label>${zybxBalance.identityCard}</label>
                                    </div>
                                    <div class="input-row">
                                        <label>姓名</label>
                                        <label>${zybxBalance.name}</label>
                                    </div>
                                    <div class="input-row">
                                        <label>账户余额</label>
                                        <label>${zybxBalance.formerYearsBalance}</label>
                                    </div>
                                    <div class="input-row">
                                        <label>截止日期</label>
                                        <label><fmt:formatDate value="${zybxBalance.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/></label>
                                    </div>
                                </c:forEach>
                                 </div>
                            </c:otherwise>
                        </c:choose>

                    <div>&nbsp;</div>
                    <input type="hidden" name="mobileResult" value="true">
                </form>
            </div>
        </article>
    </section>
</div>
<!--<script type="text/javascript" src="${ctxStatic}/jingle/js/lib/cordova.js"></script>-->
<!-- lib -->
<script type="text/javascript" src="${ctxStatic}/jingle/js/lib/zepto.js"></script>
<script type="text/javascript" src="${ctxStatic}/jingle/js/lib/iscroll.js"></script>
<%-- <script type="text/javascript" src="${ctxStatic}/jingle/js/lib/template.min.js"></script> --%>
<script type="text/javascript" src="${ctxStatic}/jingle/js/lib/Jingle.debug.js"></script>
<script type="text/javascript" src="${ctxStatic}/jingle/js/lib/zepto.touch2mouse.js"></script>
<%-- <script type="text/javascript" src="${ctxStatic}/jingle/js/lib/JChart.debug.js"></script> --%>
<!--- app --->
<script type="text/javascript">var ctx = '${ctx}';</script>
<script type="text/javascript" src="${ctxStatic}/jingle/js/app/app.js"></script>
<!--<script src="http://192.168.2.153:8080/target/target-script-min.js#anonymous"></script>-->
<script type="text/javascript">
    var sessionid = '${not empty fns:getPrincipal() ? fns:getPrincipal().sessionid : ""}';
    $('body').delegate('#result_section', 'pageshow', function () {
        if (sessionid != '') {
            var targetHash = location.hash;
            if (targetHash == '#result_section') {
                J.Router.goTo('#index_section?index');
            }
        } else {
            $('#result_article').addClass('active');
        }
    });
</script>
</body>
</html>