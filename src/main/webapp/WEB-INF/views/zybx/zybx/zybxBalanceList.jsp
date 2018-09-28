<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>中银保险客户余额管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#btnExport").click(function () {
                top.$.jBox.confirm("确认要导出客户余额数据吗？", "系统提示", function (v, h, f) {
                    if (v == "ok") {
                        $("#searchForm").attr("action", "${ctx}/zybx/zybxBalance/export");
                        $("#searchForm").submit();
                    }
                }, {buttonsFocus: 1});
                top.$('.jbox-body .jbox-icon').css('top', '55px');
            });
            $("#btnImport").click(function () {
                $.jBox($("#importBox").html(), {
                    title: "导入数据", buttons: {"关闭": true},
                    bottomText: "导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"
                });
            });
        });

        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
    </script>
</head>
<body>
<div id="importBox" class="hide">
    <form id="importForm" action="${ctx}/zybx/zybxBalance/import" method="post" enctype="multipart/form-data"
          class="form-search" style="padding-left:20px;text-align:center;" onsubmit="loading('正在导入，请稍等...');"><br/>
        <input id="uploadFile" name="file" type="file" style="width:330px"/><br/><br/>　　
        <input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>
        <a href="${ctx}/zybx/zybxBalance/import/template">下载模板</a>
    </form>
</div>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/zybx/zybxBalance/">中银保险客户余额列表</a></li>
    <%--<shiro:hasPermission name="zybx:zybxBalance:edit">--%>
    <%--<li><a href="${ctx}/zybx/zybxBalance/form">中银保险客户余额添加</a></li>--%>
    <%--</shiro:hasPermission>--%>
</ul>
<form:form id="searchForm" modelAttribute="zybxBalance" action="${ctx}/zybx/zybxBalance/" method="post"
           class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <ul class="ul-form">
        <li><label>姓名：</label>
            <form:input path="name" htmlEscape="false" maxlength="64" class="input-medium"/>
        </li>
        <li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
                <%--
                <input id="btnExport" class="btn btn-primary" type="button" value="导出"/>
                --%>
            <input id="btnImport" class="btn btn-primary" type="button" value="导入"/>
        </li>
        <li class="clearfix"></li>
    </ul>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>序号</th>
        <th>身份证号码</th>
        <th>姓名</th>
        <%--<th>手机号码</th>--%>
        <th>账户余额</th>
        <%--<th>2018年账户余额</th>--%>
        <th>截止日期</th>
        <shiro:hasPermission name="zybx:zybxBalance:edit">
            <th>操作</th>
        </shiro:hasPermission>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="zybxBalance" varStatus="idx">
        <tr>
            <td>${idx.index + 1}</td>
            <td><a href="${ctx}/zybx/zybxBalance/form?id=${zybxBalance.id}">
                    ${zybxBalance.identityCard}
            </a></td>
            <td><a href="${ctx}/zybx/zybxBalance/form?id=${zybxBalance.id}">
                    ${zybxBalance.name}
            </a></td>
                <%--<td>--%>
                <%--${zybxBalance.mobile}--%>
                <%--</td>--%>
            <td>
                    ${zybxBalance.formerYearsBalance}
            </td>
                <%--<td>--%>
                <%--${zybxBalance.year2018Balance}--%>
                <%--</td>--%>
            <td>
                <fmt:formatDate value="${zybxBalance.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </td>
            <shiro:hasPermission name="zybx:zybxBalance:edit">
                <td>
                    <a href="${ctx}/zybx/zybxBalance/form?id=${zybxBalance.id}">修改</a>
                    <a href="${ctx}/zybx/zybxBalance/delete?id=${zybxBalance.id}"
                       onclick="return confirmx('确认要删除该中银保险客户余额吗？', this.href)">删除</a>
                </td>
            </shiro:hasPermission>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>