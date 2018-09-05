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
 * 筛选条件点击事件
 * @param obj
 */
function filterClick(obj) {
    $.each(obj.parentElement.children, function (index, ele) {
        ele.removeAttribute("class");
    })
    obj.setAttribute("class", "selected");

    //获取module
    var module = getValue("module");
    //获取area
    var area = getValue("area");
    //获取type
    var type = getValue("type");
    //获取time
    var time = getValue("time");
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