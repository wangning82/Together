/**
 * Created by Andste on 2016/5/11.
 */
var dynamicLoading = {
    css: function () {
        if (!arguments || arguments.length === 0) {
            throw new Error('argument "path" is required !');
        }
        for (var i = 0, len = arguments.length; i < len; i++) {
            var head = document.getElementsByTagName('head')[0];
            var link = document.createElement('link');
            link.href = arguments[i];
            link.rel = 'stylesheet';
            link.type = 'text/css';
            head.appendChild(link);
        }
    },
    js: function () {
        if (!arguments || arguments.length === 0) {
            throw new Error('argument "path" is required !');
        }
        for (var i = 0, len = arguments.length; i < len; i++) {
            var head = document.getElementsByTagName('head')[0];
            var script = document.createElement('script');
            script.src = arguments[i];
            script.type = 'text/javascript';
            head.appendChild(script);
        }
    }
}
var lteIE8 = false,   //  全局变量，当它为true时，说明浏览器为IE9【不包括IE9】以下
    Sys = {};         //  全局变量，此为对象。
                      // Sys.ie --> IE浏览器版本【IE11检测不到】 Sys.firefox --> 火狐浏览器版本  Sys.chrome  -->  chrome浏览器版本  Sys.opera  -->  opera浏览器版本
var ctx = "/";
(function () {
    var jq183 = '<script type="text/javascript" src="' + ctx + '/jquery/jquery-1.8.3.min.js"></script>',
        jq183_cdn = '<script type="text/javascript" src="//cdn.bootcss.com/jquery/1.8.3/jquery.min.js"></script>';

    var ua = navigator.userAgent.toLowerCase();
    var s;
    (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1]
        : (s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1]
        : (s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1]
            : (s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1]
                : (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;

    if (Object.hasOwnProperty.call(window, "ActiveXObject") && !window.ActiveXObject) {
        Sys.ie = 11.0
    }
    ;
    if (Sys.ie) {
        var ver = parseFloat(Sys.ie);
        ver >= 9 ? heiLoad() : lowLoad();
        if (ver <= 8) {
            lteIE8 = true;
        }
        ;
    } else {
        heiLoad();
    }
    ;

    function lowLoad() {
        // document.write(jq183);
    };

    function heiLoad() {

    };

    (function () {
        // document.write(cookie);

    }())
})();

