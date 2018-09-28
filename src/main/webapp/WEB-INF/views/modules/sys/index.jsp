<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>${fns:getConfig('productName')}</title>
    <meta name="decorator" content="blank"/>
    <c:set var="tabmode" value="1"/>
    <c:if test="${tabmode eq '1'}">
        <link rel="Stylesheet" href="${ctxStatic}/jerichotab/css/jquery.jerichotab.css"/>
        <script type="text/javascript" src="${ctxStatic}/jerichotab/js/jquery.jerichotab.js"></script>
    </c:if>
    <style type="text/css">
        #main {
            padding: 0;
            margin: 0;
        }

        #main .container-fluid {
            padding: 0 4px 0 6px;
        }

        #header {
            margin: 0 0 8px;
            position: static;

        }

        .navbar-inner {
            background: url(${ctxStatic}/images/title-bg.png) !important;
            background-size: cover;
            width: 100%;
        }

        #header li {
            font-size: 14px;
            _font-size: 12px;
        }

        #productName {
            font-family: Helvetica, Georgia, Arial, sans-serif, 黑体;
            font-size: 45px;
            font-weight: bold;
            padding-left: 33px;
            color: #0d0d0d;
            vertical-align: middle;
        }

        #footer {
            font-size: 11px;
            text-align: center;
            border-top: 2px solid #0663A2;
            background: url(${ctxStatic}/images/footer-bg.png) !important;
            background-size: cover;
            width: 100%;
            height: 20px;
            position: absolute;
            left: 0px;
            bottom: 0px;
            margin-top: 0px;
            vertical: middle;
        }

        #footer, #footer a {
            color: #999;
        }

        #left {
            overflow-x: hidden;
            overflow-y: auto;
            background-color: #157ab5;
        }

        #left .collapse {
            position: static;
        }

        #userControl > li > a { /*color:#fff;*/
            text-shadow: none;
        }

        #userControl > li > a:hover, #user #userControl > li.open > a {
            background: transparent;
        }

        .tabs {
            background-color: #157ab5;
            border: 1px solid #157ab5 !important;
        }

        .tab_pages div ul li {
            font-size: 18px !important;
        }

        .jericho_tabs {
            vertical-align: middle;
        }

    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            // <c:if test="${tabmode eq '1'}"> 初始化页签
            $.fn.initJerichoTab({
                renderTo: '#right', uniqueId: 'jerichotab',
                contentCss: {'height': $('#right').height() - tabTitleHeight},
                tabs: [], loadOnce: true, tabWidth: 110, titleHeight: tabTitleHeight
            });//</c:if>
            // 绑定菜单单击事件
            $("#menu a.menu").click(function () {
                // 一级菜单焦点
                $("#menu li.menu").removeClass("active");
                $(this).parent().addClass("active");
                // 左侧区域隐藏
                if ($(this).attr("target") == "mainFrame") {
                    $("#left,#openClose").hide();
                    wSizeWidth();
                    // <c:if test="${tabmode eq '1'}"> 隐藏页签
                    $(".jericho_tab").hide();
                    $("#mainFrame").show();//</c:if>
                    return true;
                }
                // 左侧区域显示
                $("#left,#openClose").show();
                if (!$("#openClose").hasClass("close")) {
                    $("#openClose").click();
                }
                // 显示二级菜单
                var menuId = "#menu-" + $(this).attr("data-id");
                if ($(menuId).length > 0) {
                    $("#left .accordion").hide();
                    $(menuId).show();
                    // 初始化点击第一个二级菜单
                    if (!$(menuId + " .accordion-body:first").hasClass('in')) {
                        $(menuId + " .accordion-heading:first a").click();
                    }
                    if (!$(menuId + " .accordion-body li:first ul:first").is(":visible")) {
                        $(menuId + " .accordion-body a:first i").click();
                    }
                    // 初始化点击第一个三级菜单
                    $(menuId + " .accordion-body li:first li:first a:first i").click();
                } else {
                    // 获取二级菜单数据
                    $.get($(this).attr("data-href"), function (data) {
                        if (data.indexOf("id=\"loginForm\"") != -1) {
                            alert('未登录或登录超时。请重新登录，谢谢！');
                            top.location = "${ctx}";
                            return false;
                        }
                        $("#left .accordion").hide();
                        $("#left").append(data);
                        // 链接去掉虚框
                        $(menuId + " a").bind("focus", function () {
                            if (this.blur) {
                                this.blur()
                            }
                            ;
                        });
                        // 二级标题
                        $(menuId + " .accordion-heading a").click(function () {
                            $(menuId + " .accordion-toggle i").removeClass('icon-chevron-down').addClass('icon-chevron-right');
                            if (!$($(this).attr('data-href')).hasClass('in')) {
                                $(this).children("i").removeClass('icon-chevron-right').addClass('icon-chevron-down');
                            }
                        });
                        // 二级内容
                        $(menuId + " .accordion-body a").click(function () {
                            $(menuId + " li").removeClass("active");
                            $(menuId + " li i").removeClass("icon-white");
                            // 不要这个属性，否则字体会变成白色
                            //$(this).parent().addClass("active");
                            // 不要白色，跟背景色重叠了，add by HL
                            //$(this).children("i").addClass("icon-white");
                        });
                        // 展现三级
                        $(menuId + " .accordion-inner a").click(function () {
                            var href = $(this).attr("data-href");
                            if ($(href).length > 0) {
                                $(href).toggle().parent().toggle();
                                return false;
                            }
                            // <c:if test="${tabmode eq '1'}"> 打开显示页签
                            return addTab($(this)); // </c:if>
                        });
                        // 默认选中第一个菜单,默认不选中，add by HL
                        //$(menuId + " .accordion-body a:first i").click();
                        //$(menuId + " .accordion-body li:first li:first a:first i").click();
                    });
                }
                // 大小宽度调整
                wSizeWidth();
                return false;
            });
            // 初始化点击第一个一级菜单，
            $("#menu a.menu:first span").click();
            // <c:if test="${tabmode eq '1'}"> 下拉菜单以选项卡方式打开
            $("#userInfo .dropdown-menu a").mouseup(function () {
                return addTab($(this), true);
            });
            // </c:if>
            // 鼠标移动到边界自动弹出左侧菜单
            $("#openClose").mouseover(function () {
                if ($(this).hasClass("open")) {
                    $(this).click();
                }
            });
            // 获取通知数目  <c:set var="oaNotifyRemindInterval" value="${fns:getConfig('oa.notify.remind.interval')}"/>

            function getNotifyNum() {
                $.get("${ctx}/oa/oaNotify/self/count?updateSession=0&t=" + new Date().getTime(), function (data) {
                    var num = parseFloat(data);
                    if (num > 0) {
                        $("#notifyNum,#notifyNum2").show().html("(" + num + ")");
                    } else {
                        $("#notifyNum,#notifyNum2").hide()
                    }
                });
            }

            getNotifyNum();
            //<c:if test="${oaNotifyRemindInterval ne '' && oaNotifyRemindInterval ne '0'}">
            setInterval(getNotifyNum, ${oaNotifyRemindInterval});
            //</c:if>

            // 登录后跳转到首页
            //$.ajax({url: "${ctx}/sys/menu/index", async: false});
            //$('#btn-index').trigger('click');
            var _this = $('#btn-index');
            addTab($(_this));
        });
        // <c:if test="${tabmode eq '1'}"> 添加一个页签
        function addTab($this, refresh) {
            $(".jericho_tab").show();
            $("#mainFrame").hide();
            if ($this.text() == "首页") {
                $.fn.jerichoTab.addTab({
                    tabFirer: $this,
                    title: $this.text().replace(/[\r\n]/g, "").replace(/[ ]/g, ""), // 去掉换行符、空格
                    closeable: false,
                    data: {
                        dataType: 'iframe',
                        dataLink: $this.attr('href')
                    }
                }).loadData(refresh);
            } else {
                $.fn.jerichoTab.addTab({
                    tabFirer: $this,
                    title: $this.text().replace(/[\r\n]/g, "").replace(/[ ]/g, ""), // 去掉换行符、空格
                    closeable: true,
                    data: {
                        dataType: 'iframe',
                        dataLink: $this.attr('href')
                    }
                }).loadData(refresh);
            }

            return false;
        }// </c:if>
    </script>
</head>
<body>
<div id="main">
    <div id="header" class="navbar navbar-fixed-top">
        <div class="navbar-inner">
            <div class="brand"><img style="width: 240px; height: 60px;" src="${ctxStatic}/images/logo-red.png"></div>
            <div class="brand" id="productName" style="vertical-align: middle;">
                <span style="display: inline-block; line-height: 60px;
                      cursor: default;">${fns:getConfig('productName')}</span>
            </div>
            <ul id="userControl" class="nav pull-right">
                <%--暂时注释掉
                <li>
                    <a href="${pageContext.request.contextPath}${fns:getFrontPath()}/index-${fnc:getCurrentSiteId()}.html"
                       target="_blank" title="访问网站主页"><i class="icon-home"></i></a></li>
                <li id="themeSwitch" class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#" title="主题切换"><i
                            class="icon-th-large"></i></a>
                    <ul class="dropdown-menu">
                        <c:forEach items="${fns:getDictList('theme')}" var="dict">
                            <li><a href="#"
                                   onclick="location='${pageContext.request.contextPath}/theme/${dict.value}?url='+location.href">${dict.label}</a>
                            </li>
                        </c:forEach>
                        <li>
                            <a href="javascript:cookie('tabmode','${tabmode eq '1' ? '0' : '1'}');location=location.href">${tabmode eq '1' ? '关闭' : '开启'}页签模式</a>
                        </li>
                    </ul>
                    <!--[if lte IE 6]>
                    <script type="text/javascript">$('#themeSwitch').hide();</script><![endif]-->
                </li>

                <li id="userInfo" class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#" title="个人信息">您好, ${fns:getUser().name}&nbsp;<span
                            id="notifyNum" class="label label-info hide"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="${ctx}/sys/user/info" target="mainFrame"><i class="icon-user"></i>&nbsp; 个人信息</a>
                        </li>
                        <li><a href="${ctx}/sys/user/modifyPwd" target="mainFrame"><i class="icon-lock"></i>&nbsp; 修改密码</a>
                        </li>
                        <li><a href="${ctx}/oa/oaNotify/self" target="mainFrame"><i class="icon-bell"></i>&nbsp; 我的通知
                            <span id="notifyNum2" class="label label-info hide"></span></a></li>
                    </ul>
                </li>
                --%>
                <li>
                    <a href="${ctx}/logout" title="退出登录">
                        <img id="btn-exit" style="width: 173px; height: 55px;" src="${ctxStatic}/images/exit-bg.png">
                    </a>
                </li>
                <li style="margin-top: 17px; margin-right: 10px;">
                    <img id="header-qrcode" style="width: 55px; height: 55px;z-index: 10;"
                         src="${ctxStatic}/images/QRDownApp1.png"/>
                </li>

                <li style="display: none;">
                    <a href="${ctx}/sys/menu/index" target="mainFrame" text="首页">
                        <span id="btn-index" href="${ctx}/sys/menu/index" target="mainFrame" text="首页">首页</span>
                    </a>
                </li>
                <li>&nbsp;</li>
            </ul>
            <div id="qrcode-dropdown" style="width: 100%; position: absolute; float: right;
                margin-top: 90px; z-index: 10; display: none; text-align: center;">
                <div style="text-align: center;">
                    <div style="margin-top: 20px; text-align: center;">
                        <img style="width: 120px; height: 120px;" src="${ctxStatic}/images/QRDownApp1.png"/>
                    </div>
                    <div style="margin-bottom: 20px;text-align: center;">
                        <h5 style="color: black;">安卓移动端</h5>
                    </div>
                </div>
            </div>
            <%-- <c:if test="${cookie.theme.value eq 'cerulean'}">
                <div id="user" style="position:absolute;top:0;right:0;"></div>
                <div id="logo" style="background:url(${ctxStatic}/images/logo_bg.jpg) right repeat-x;width:100%;">
                    <div style="background:url(${ctxStatic}/images/logo.jpg) left no-repeat;width:100%;height:70px;"></div>
                </div>
                <script type="text/javascript">
                    $("#productName").hide();$("#user").html($("#userControl"));$("#header").prepend($("#user, #logo"));
                </script>
            </c:if> --%>
            <div class="nav-collapse" style="display: none;">
                <ul id="menu" class="nav" style="*white-space:nowrap;float:none;">
                    <c:set var="firstMenu" value="true"/>
                    <c:forEach items="${fns:getMenuList()}" var="menu" varStatus="idxStatus">
                        <c:if test="${menu.parent.id eq '1'&&menu.isShow eq '1'}">
                            <li class="menu ${not empty firstMenu && firstMenu ? ' active' : ''}">
                                <c:if test="${empty menu.href}">
                                    <a class="menu" href="javascript:"
                                       data-href="${ctx}/sys/menu/tree?parentId=${menu.id}"
                                       data-id="${menu.id}"><span>${menu.name}</span>
                                    </a>
                                </c:if>
                                <c:if test="${not empty menu.href}">
                                    <a class="menu"
                                       href="${fn:indexOf(menu.href, '://') eq -1 ? ctx : ''}${menu.href}"
                                       data-id="${menu.id}" target="mainFrame"><span>${menu.name}</span>
                                    </a>
                                </c:if>
                            </li>
                            <c:if test="${firstMenu}">
                                <c:set var="firstMenuId" value="${menu.id}"/>
                            </c:if>
                            <c:set var="firstMenu" value="false"/>
                        </c:if>
                    </c:forEach><%--
						<shiro:hasPermission name="cms:site:select">
						<li class="dropdown">
							<a class="dropdown-toggle" data-toggle="dropdown" href="#">${fnc:getSite(fnc:getCurrentSiteId()).name}<b class="caret"></b></a>
							<ul class="dropdown-menu">
								<c:forEach items="${fnc:getSiteList()}" var="site"><li><a href="${ctx}/cms/site/select?id=${site.id}&flag=1">${site.name}</a></li></c:forEach>
							</ul>
						</li>
						</shiro:hasPermission> --%>
                </ul>
            </div><!--/.nav-collapse -->
        </div>
    </div>
    <div class="container-fluid">
        <div id="content" class="row-fluid">
            <div id="left"><%--
					<iframe id="menuFrame" name="menuFrame" src="" style="overflow:visible;" scrolling="yes" frameborder="no" width="100%" height="650"></iframe> --%>
            </div>
            <div id="openClose" class="close">&nbsp;</div>
            <div id="right">
                <iframe id="mainFrame" name="mainFrame" src="" style="overflow:visible;"
                        scrolling="yes" frameborder="no" width="100%" height="650">
                </iframe>
            </div>
        </div>
        <div id="footer" class="row-fluid">
            Copyright &copy; 2012-${fns:getConfig('copyrightYear')} ${fns:getConfig('productName')} - Powered By <a
                href="http://www.xa7m.com" target="_blank">七米科技</a> ${fns:getConfig('version')}
        </div>
    </div>
</div>
<script type="text/javascript">
    var leftWidth = 160; // 左侧窗口大小
    var tabTitleHeight = 35; // 页签的高度
    var htmlObj = $("html"), mainObj = $("#main");
    var headerObj = $("#header"), footerObj = $("#footer");
    var frameObj = $("#left, #openClose, #right, #right iframe");

    function wSize() {
        var minHeight = 500, minWidth = 980;
        var strs = getWindowSize().toString().split(",");
        htmlObj.css({
            "overflow-x": strs[1] < minWidth ? "auto" : "hidden",
            "overflow-y": strs[0] < minHeight ? "auto" : "hidden"
        });
        mainObj.css("width", strs[1] < minWidth ? minWidth - 10 : "auto");
        frameObj.height((strs[0] < minHeight ? minHeight : strs[0]) - headerObj.height() - footerObj.height() - (strs[1] < minWidth ? 42 : 28));
        $("#openClose").height($("#openClose").height() - 5);// <c:if test="${tabmode eq '1'}">
        $(".jericho_tab iframe").height($("#right").height() - tabTitleHeight); // </c:if>
        wSizeWidth();
    }

    function wSizeWidth() {
        if (!$("#openClose").is(":hidden")) {
            var leftWidth = ($("#left").width() < 0 ? 0 : $("#left").width());
            $("#right").width($("#content").width() - leftWidth - $("#openClose").width() - 5);
        } else {
            $("#right").width("100%");
        }
    }// <c:if test="${tabmode eq '1'}">

    function openCloseClickCallBack(b) {
        $.fn.jerichoTab.resize();
    } // </c:if>
</script>
<script src="${ctxStatic}/common/wsize.min.js" type="text/javascript"></script>
<script>
    $(function () {

        //  鼠标移入移出事件
        $('#header-qrcode').on(
            'mouseover', function () {
                $('#qrcode-dropdown').show();
            });
        // 鼠标移出事件
        $('#header-qrcode').on(
            'mouseout', function () {
                $('#qrcode-dropdown').hide();
            });

    });
</script>
</body>
</html>