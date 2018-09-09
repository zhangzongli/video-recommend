$(function () {
    function getUrlParam (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");//构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);//匹配目标参数
        if (r != null) return decodeURIComponent(r[2]); return null;//返回参数值
    }
    var content = getUrlParam("content");
    ajaxSearch(content);

});

/**
 * 搜索按钮
 */
function search() {
    var content = $(".search").val();
    ajaxSearch(content);
}

/**
 * ajax 请求后台搜索
 */
function ajaxSearch(content) {
    $.ajax({
        url: "/list_page/search?comment="+content,
        type: "get",
        async: true,
        success: function (data) {
            var html = "<ul class=\"video_list list_v_content\">\n";
            $.each(data, function (index, value) {
                html = html + "<li class=\"first_content bg\">\n" +
                    "                            <a href=\"/play_page?videoId="+value["videoId"]+"&url="+value["videoUrl"]+"&name="+value["videoName"]+"\" class=\"pic\" target=\"_blank\">\n" +
                    "                                <img src=\""+value["videoPic"]+"\" width=\"100%\"/>\n" +
                    "                                <span class=\"first_bg\"><i class=\"icon_bf\"></i></span>\n" +
                    "                            </a>\n" +
                    "                            <div class=\"tc\">\n" +
                    "                                <p class=\"tit\">\n" +
                    "                                    <a target=\"_blank\" href=\"/play_page?videoId="+value["videoId"]+"&url="+value["videoUrl"]+"&name="+value["videoName"]+"\">"+value["videoName"]+"</a></p>\n" +
                    "                                <p class=\"des\">"+value["videoSummary"]+"</p>\n" +
                    "                            </div>\n" +
                    "                        </li>";
            });
            html = html + "</ul>";
            $("#movie_list").html(html);
        }
    })
}

/**
 * 父筛选条件点击事件
 * @param obj
 */
function filterClick(obj) {
    $.each(obj.parentElement.children, function (index, ele) {
        ele.removeAttribute("class");
    });
    obj.setAttribute("class", "selected");
    var value = obj.getAttribute("value");
    switch (value) {
        case "all":
            $("#type").css("display", "none");
            $("#area").css("display", "none");
            $("#time").css("display", "none");
            break;
        case "1":
            var movieHtml = "<dt class=\"l_f\">类别：</dt>\n" +
                "                    <dd>\n" +
                "                        <a href=\"javascript:void(0)\" onclick=\"childrenFilterClick(this)\" value=\"all\" attrval=\"全部\" class=\"selected\">全部</a>\n" +
                "                        <a href=\"javascript:void(0)\" onclick=\"childrenFilterClick(this)\" value=\"1_1\" attrval=\"喜剧\">喜剧</a>\n" +
                "                        <a href=\"javascript:void(0)\" onclick=\"childrenFilterClick(this)\" value=\"1_2\" attrval=\"科幻\">科幻</a>\n" +
                "                        <a href=\"javascript:void(0)\" onclick=\"childrenFilterClick(this)\" value=\"1_3\" attrval=\"恐怖\">恐怖</a>\n" +
                "                        <a href=\"javascript:void(0)\" onclick=\"childrenFilterClick(this)\" value=\"1_4\" attrval=\"爱情\">爱情</a>\n" +
                "                    </dd>";
            $("#type").html(movieHtml);
            $("#type").css("display", "block");
            $("#area").css("display", "block");
            $.each($("#area a"), function (index, ele) {
                ele.removeAttribute("class");
            });
            $("#area a")[0].setAttribute("class", "selected");
            $("#time").css("display", "block");
            $.each($("#time a"), function (index, ele) {
                ele.removeAttribute("class");
            });
            $("#time a")[0].setAttribute("class", "selected");
            break;
        case "2":
            var tvHtml = "<dt class=\"l_f\">类别：</dt>\n" +
                "                    <dd>\n" +
                "                        <a href=\"javascript:void(0)\" onclick=\"childrenFilterClick(this)\" value=\"all\" attrval=\"全部\" class=\"selected\">全部</a>\n" +
                "                        <a href=\"javascript:void(0)\" onclick=\"childrenFilterClick(this)\" value=\"2_1\" attrval=\"古装\">古装</a>\n" +
                "                        <a href=\"javascript:void(0)\" onclick=\"childrenFilterClick(this)\" value=\"2_2\" attrval=\"武侠\">武侠</a>\n" +
                "                        <a href=\"javascript:void(0)\" onclick=\"childrenFilterClick(this)\" value=\"2_3\" attrval=\"警匪\">警匪</a>\n" +
                "                        <a href=\"javascript:void(0)\" onclick=\"childrenFilterClick(this)\" value=\"2_4\" attrval=\"军事\">军事</a>\n" +
                "                    </dd>";
            $("#type").html(tvHtml);
            $("#type").css("display", "block");
            $.each($("#area a"), function (index, ele) {
                ele.removeAttribute("class");
            });
            $("#area a")[0].setAttribute("class", "selected");
            $("#time").css("display", "block");
            $.each($("#time a"), function (index, ele) {
                ele.removeAttribute("class");
            });
            $("#time a")[0].setAttribute("class", "selected");
            break;
        case "3":
            var animeHtml = "<dt class=\"l_f\">类别：</dt>\n" +
                "                    <dd>\n" +
                "                        <a href=\"javascript:void(0)\" onclick=\"childrenFilterClick(this)\" value=\"all\" attrval=\"全部\" class=\"selected\">全部</a>\n" +
                "                        <a href=\"javascript:void(0)\" onclick=\"childrenFilterClick(this)\" value=\"3_1\" attrval=\"热血\">热血</a>\n" +
                "                        <a href=\"javascript:void(0)\" onclick=\"childrenFilterClick(this)\" value=\"3_2\" attrval=\"格斗\">格斗</a>\n" +
                "                        <a href=\"javascript:void(0)\" onclick=\"childrenFilterClick(this)\" value=\"3_3\" attrval=\"恋爱\">恋爱</a>\n" +
                "                        <a href=\"javascript:void(0)\" onclick=\"childrenFilterClick(this)\" value=\"3_4\" attrval=\"校园\">校园</a>\n" +
                "                    </dd>";
            $("#type").html(animeHtml);
            $("#type").css("display", "block");
            $.each($("#area a"), function (index, ele) {
                ele.removeAttribute("class");
            });
            $("#area a")[0].setAttribute("class", "selected");
            $("#time").css("display", "block");
            $.each($("#time a"), function (index, ele) {
                ele.removeAttribute("class");
            });
            $("#time a")[0].setAttribute("class", "selected");
            break;
        case "4":
            var varietyHtml = "<dt class=\"l_f\">类别：</dt>\n" +
                "                    <dd>\n" +
                "                        <a href=\"javascript:void(0)\" onclick=\"childrenFilterClick(this)\" value=\"all\" attrval=\"全部\" class=\"selected\">全部</a>\n" +
                "                        <a href=\"javascript:void(0)\" onclick=\"childrenFilterClick(this)\" value=\"4_1\" attrval=\"美食\">美食</a>\n" +
                "                        <a href=\"javascript:void(0)\" onclick=\"childrenFilterClick(this)\" value=\"4_2\" attrval=\"旅游\">旅游</a>\n" +
                "                        <a href=\"javascript:void(0)\" onclick=\"childrenFilterClick(this)\" value=\"4_3\" attrval=\"搞笑\">搞笑</a>\n" +
                "                        <a href=\"javascript:void(0)\" onclick=\"childrenFilterClick(this)\" value=\"4_4\" attrval=\"选秀\">选秀</a>\n" +
                "                    </dd>";
            $("#type").html(varietyHtml);
            $("#type").css("display", "block");
            $.each($("#area a"), function (index, ele) {
                ele.removeAttribute("class");
            });
            $("#area a")[0].setAttribute("class", "selected");
            $("#time").css("display", "block");
            $.each($("#time a"), function (index, ele) {
                ele.removeAttribute("class");
            });
            $("#time a")[0].setAttribute("class", "selected");
            break;
        default:
    }
    commenFilter();
}

/**
 * 子筛选条件点击事件
 * @param obj
 */
function childrenFilterClick(obj) {
    $.each(obj.parentElement.children, function (index, ele) {
        ele.removeAttribute("class");
    })
    obj.setAttribute("class", "selected");
    commenFilter();
}

/**
 * 共通查询
 */
function commenFilter() {
    //获取module
    var module = getValue("module");
    //获取type
    var type;
    if ("block" == $("#type").css("display")) {
        type = getValue("type")
    }
    //获取area
    var area;
    if ("block" == $("#area").css("display")) {
        area = getValue("area")
    }
    //获取time
    var time;
    if ("block" == $("#time").css("display")) {
        time = getValue("time")
    }
    $.ajax({
        url: "/list_page/filter?module="+module+"&area="+area+"&type="+type+"&time="+time,
        type: "get",
        async: true,
        success: function (data) {
            var html = "<ul class=\"video_list list_v_content\">\n";
            $.each(data, function (index, value) {
                html = html + "<li class=\"first_content bg\">\n" +
                    "                            <a href=\"/play_page?videoId="+value["videoId"]+"&url="+value["videoUrl"]+"&name="+value["videoName"]+"\" class=\"pic\" target=\"_blank\">\n" +
                    "                                <img src=\""+value["videoPic"]+"\" width=\"100%\"/>\n" +
                    "                                <span class=\"first_bg\"><i class=\"icon_bf\"></i></span>\n" +
                    "                            </a>\n" +
                    "                            <div class=\"tc\">\n" +
                    "                                <p class=\"tit\">\n" +
                    "                                    <a target=\"_blank\" href=\"/play_page?videoId="+value["videoId"]+"&url="+value["videoUrl"]+"&name="+value["videoName"]+"\">"+value["videoName"]+"</a></p>\n" +
                    "                                <p class=\"des\">"+value["videoSummary"]+"</p>\n" +
                    "                            </div>\n" +
                    "                        </li>";
            });
            html = html + "</ul>";
            $("#movie_list").html(html);
        }
    })
}

/**
 * 获取不同的筛选条件
 */
function getValue(classId) {
    var result = '';
    $("#"+classId).find("a").each(function (index, ele) {
        if ("selected" == ele.getAttribute("class")) {
            result = ele.getAttribute("value");
        }
    })
    return result;
}