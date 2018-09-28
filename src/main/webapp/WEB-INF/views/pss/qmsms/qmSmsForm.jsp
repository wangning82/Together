<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>短信设置管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {
            //$("#name").focus();
            $("#inputForm").validate({
                submitHandler: function (form) {
                    // 禁用按钮,防止连击， add by HL
                    $('#btnSubmit').attr('disabled', 'disabled');
                    loading('正在提交，请稍等...');
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
        });
    </script>
</head>
<body>
<br/>
<form:form id="inputForm" modelAttribute="qmSms" action="${ctx}/qmsms/qmSms/update" method="post"
           class="form-horizontal">
    <form:hidden path="id"/>
    <sys:message content="${message}"/>
    <div class="control-group" style="display: none;">
        <label class="control-label">短信编号：</label>
        <div class="controls">
            <form:input path="smsId" htmlEscape="false" maxlength="50" class="input-xlarge "/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">短信标题：</label>
        <div class="controls">
            <form:input path="smsTitle" htmlEscape="false" maxlength="50" class="input-xlarge "/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">提交url：</label>
        <div class="controls">
            <form:input path="smsUrl" htmlEscape="false" maxlength="100" class="input-xlarge "/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">用户名：</label>
        <div class="controls">
            <form:input path="smsUsername" htmlEscape="false" maxlength="50" class="input-xlarge "/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">密码：</label>
        <div class="controls">
            <form:input path="smsPassword" htmlEscape="false" maxlength="50" class="input-xlarge "/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">产品id：</label>
        <div class="controls">
            <form:input path="smsProductid" htmlEscape="false" maxlength="50" class="input-xlarge "/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">扩展小号：</label>
        <div class="controls">
            <form:input path="smsXh" htmlEscape="false" maxlength="50" class="input-xlarge "/>
        </div>
    </div>
    <div class="form-actions">
        <input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
        <a href="${ctx}/qmsms/qmSms/doShot?smsContext=11&smsMobiles=18600670003,13820255924"><input
                class="btn btn-primary" type="button" value="测试"/></a>
    </div>
</form:form>
</body>
</html>