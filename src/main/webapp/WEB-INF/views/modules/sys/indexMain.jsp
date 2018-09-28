<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>首页</title>
    <meta name="decorator" content="default"/>
    <style type="text/css">
        .index-main {
            margin: 0 0 0 0;
            position: static;
        }
        .index-inner {
            position: relative;
            width: 100vw;
            height: 100vh;
            background-position: center center;
            background: url(${ctxStatic}/images/index-bg.png) !important;
            background-size: cover !important;
        }
    </style>
</head>
<body>
<div class="index-main">
    <div class="index-inner"></div>
</div>
</body>
</html>
