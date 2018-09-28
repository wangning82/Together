<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
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
</head>
<body>
<div id="aside_container">
</div>
<div id="section_container">
    <section id="login_section" class="active">
        <header>
            <h1 class="title">${fns:getConfig('productName')}</h1>
            <!-- <nav class="right">
                <a data-target="section" data-icon="info" href="#about_section"></a>
            </nav> -->
        </header>
        <article data-scroll="true" id="login_article">
            <div class="indented">
                <form id="loginForm" action="${pageContext.request.contextPath}/mobile/zybx/zybxBalance/list"
                      method="post">
                    <div>&nbsp;</div>
                    <div class="input-group">
                        <div class="input-row">
                            <label for="identityCard">身份证号码</label>
                            <input type="text" name="identityCard" id="identityCard" placeholder="请填写身份证号码">
                        </div>
                        <div class="input-row">
                            <label for="name">姓名</label>
                            <input type="text" name="name" id="name" placeholder="请填写姓名">
                        </div>
                        <div class="input-row">
                            <label for="mobile">手机号码</label>
                            <input type="text" name="mobile" id="mobile" placeholder="请填写手机号码"
                                   onkeyup="value=value.replace(/[^\d]/g,'')">
                        </div>
                        <div class="register-item-area" style="margin-top: 10px;">
                            <input autocomplete="off" id="msgCode" name="msgCode" placeholder="请输入短信验证码"
                                   onkeyup="value=value.replace(/[^\d]/g,'')"
                                   style="width: auto; height: 38px;" type="text">
                            <input id="btn_code" value="获取验证码" type="button" class="btn"
                                   style="width: 100px; height: 38px; border: 1px solid #ccc; color: dodgerblue;
                           background-color: transparent; margin-bottom: 10px;">
                        </div>
                    </div>
                    <div>&nbsp;</div>
                    <input type="hidden" name="mobileLogin" value="true">
                    <button id="btn" class="submit block" data-icon="key">登录</button>
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
    $('body').delegate('#login_section', 'pageinit', function () {
        $("#loginForm").submit(function () {
            if ($('#identityCard').val() == '') {
                J.showToast('请填写身份证号码', 'info');
            } else if ($('#name').val() == '') {
                J.showToast('请填写姓名', 'info');
            } else if ($('#mobile').val() == '') {
                J.showToast('请填写手机号码', 'info');
            } else if ($('#msgCode').val() == '') {
                J.showToast('请填写短信验证码', 'info');
            } else {
                var loginForm = $("#loginForm");
                if (!loginForm.valid()) {
                    J.showToast('请输入正确的验证码', 'info');
                } else {
                    var options = {
                        url: loginForm.attr('action'),
                        type: 'POST',
                        async: false,
                        success: function (data) {
                            loginForm.attr('action');
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {

                        }
                    }
                    $('#loginForm').ajaxSubmit(options);
                }

            }
//            return false;
        });
    });
    $('body').delegate('#login_section', 'pageshow', function () {
        if (sessionid != '') {
            var targetHash = location.hash;
            if (targetHash == '#login_section') {
                //J.showToast('你已经登录！', 'success');
                J.Router.goTo('#index_section?index');
            }
        } else {
            $('#login_article').addClass('active');
        }
    });

    $(function () {
        $('#btn_code').click(function () {
            var _this = $(this);
            var mobile = $('#mobile').val();
            if (mobile != null && mobile != undefined && mobile != '') {

                $.ajax({
                    type: "GET",
//                dataType: "json",
//                contentType: "application/json; charset=utf-8",
                    url: "${pageContext.request.contextPath}/servlet/sendMsgCodeServlet",
                    data: {'mobile': mobile},
                    async: true,
                    success: function (data) {
                        var jsonObj = eval("(" + data + ")");
                        if (jsonObj.code == 0) {
                            J.showToast(jsonObj.message, 'info');
                            $.countDown(_this);
                        } else if (jsonObj.code == 1) {
                            J.showToast(jsonObj.message, 'success');
                        } else if (jsonObj.code == 2) {
                            J.showToast(jsonObj.message, 'info');
                        }

//                    alertx(data);
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        J.showToast("短信验证码发送失败，请稍后重试", 'info');
//                    alertx(XMLHttpRequest);
//                    alertx(textStatus);
//                    alertx(errorThrown);
                    }
                });
            } else {
                J.showToast("请输入正确的手机号码！", 'info');
            }
        })
    });
    // 登录验证
    $(document).ready(function () {
        $('#loginForm').validate({
            rules: {
                // 手机号码
                mobile: {
                    required: true,
                    minlength: 11,
                    maxlength: 11,
                    isphoneNum: true,
                    remote: "${pageContext.request.contextPath}/servlet/validateMobileServlet"
                },
                // 短信验证码
                msgCode: {
                    required: true,
                    rangelength: [6, 6],
                    remote: {
                        url: "${pageContext.request.contextPath}/servlet/validateMsgCodeServlet",
                        data: {
                            mobile: function () {
                                return $('#mobile').val();
                            },
                            msgCode: function () {
                                return $('#msgCode').val();
                            }
                        }
                    },
                }
            },
            messages: {
                mobile: {
                    required: "*请输入您的手机号码",
                    maxlength: "*请填写11位的手机号",
                    minlength: "*请填写11位的手机号",
                    isphoneNum: "请填写正确的手机号码",
                    remote: "*该手机号码已经被注册过了！"
                },
                msgCode: {
                    required: "请输入正确的短信验证码",
                    rangelength: "*请填写6位的短信验证码",
                    remote: "*短信验证码不正确或已超时"
                }
            },
            errorLabelContainer: "#messageBox",
            errorPlacement: function (error, element) {
                error.appendTo($("#loginError").parent());
            }


        });

    });
    //自定义手机号验证
    jQuery.validator.addMethod("isphoneNum", function (value, element) {
        var length = value.length;
        var mobile = /^1[1-9]{1}[0-9]{9}$/;
        return this.optional(element) || (length == 11 && mobile.test(value));
    }, "请正确填写您的手机号码");

</script>
</body>
</html>