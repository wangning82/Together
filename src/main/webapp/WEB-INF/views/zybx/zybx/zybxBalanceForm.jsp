<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>中银保险客户余额管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {
            //$("#name").focus();
            $("#inputForm").validate({
                submitHandler: function (form) {
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
<ul class="nav nav-tabs">
    <li><a href="${ctx}/zybx/zybxBalance/">中银保险客户余额列表</a></li>
    <li class="active"><a href="${ctx}/zybx/zybxBalance/form?id=${zybxBalance.id}">中银保险客户余额<shiro:hasPermission
            name="zybx:zybxBalance:edit">${not empty zybxBalance.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission
            name="zybx:zybxBalance:edit">查看</shiro:lacksPermission></a></li>
</ul>
<br/>
<form:form id="inputForm" modelAttribute="zybxBalance" action="${ctx}/zybx/zybxBalance/save" method="post"
           class="form-horizontal">
    <form:hidden path="id"/>
    <sys:message content="${message}"/>
    <div class="control-group">
        <label class="control-label">身份证号码：</label>
        <div class="controls">
            <form:input path="identityCard" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">姓名：</label>
        <div class="controls">
            <form:input path="name" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <%--<div class="control-group">--%>
    <%--<label class="control-label">手机号码：</label>--%>
    <%--<div class="controls">--%>
    <%--<form:input path="mobile" htmlEscape="false" maxlength="64" class="input-xlarge "/>--%>
    <%--</div>--%>
    <%--</div>--%>
    <div class="control-group">
        <label class="control-label">账户余额：</label>
        <div class="controls">
            <form:input path="formerYearsBalance" htmlEscape="false" maxlength="255" class="input-xlarge "/>
        </div>
    </div>
    <%--<div class="control-group">--%>
    <%--<label class="control-label">2018年余额：</label>--%>
    <%--<div class="controls">--%>
    <%--<form:input path="year2018Balance" htmlEscape="false" maxlength="255" class="input-xlarge "/>--%>
    <%--</div>--%>
    <%--</div>--%>
    <div class="control-group">
        <label class="control-label">截止日期：</label>
        <div class="controls">
            <input name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
                   value="<fmt:formatDate value="${zybxBalance.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">备注信息：</label>
        <div class="controls">
            <form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
        </div>
    </div>
    <div class="form-actions">
        <shiro:hasPermission name="zybx:zybxBalance:edit"><input id="btnSubmit" class="btn btn-primary" type="submit"
                                                                 value="保 存"/>&nbsp;</shiro:hasPermission>
        <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
    </div>
</form:form>
</body>
</html>