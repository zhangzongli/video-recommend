var userName = "zhangsan";
var passWord = "123456";
var userId = null;
layui.use(['layer'], function(){
    var layer = layui.layer;
});
$(function () {
    initIndex();
    isLogin();

});

/**
 * 跳转login登录modal
 */
function toLogin() {
    //modal
    $('#loginModal').modal({
        backdrop: false
    });
    $('#loginModal').modal('hide');

    $("#loginModal").modal("show");

}

/**
 * 登录
 */
var loginIndexSumbit;
function login() {
    var dataArr = {};
    dataArr["userName"] = $("#user_name").val();
    dataArr["passWord"] = $("#password").val();
    $.ajax({
        url: "/login",
        type: "post",
        data: JSON.stringify(dataArr),
        async: false,
        contentType: "application/json",
        beforeSend:function()
        {
            //layer loading
            loginIndexSumbit = layer.load(1, {
                shade: [0.1,'#fff'] //0.1透明度的白色背景
            });
        },
        success: function (data) {
            //layer 关闭loading
            layer.close(loginIndexSumbit);
            if ("success" == data) {
                layer.msg('登录成功');
                $('#loginModal').modal('hide');
                $(".login").css("display", "none");
                $(".login_out").css("display", "block");
                initIndex();
            }else if("failed" == data) {
                layer.msg('登录失败');
            }
        }
    })
}

/**
 * 登出
 */
function loginOut() {
    $.ajax({
        url: "/login_out",
        type: "get",
        success: function (data) {
            initIndex();
            $("#loginUser").html("");
            $(".login").css("display", "block");
            $(".login_out").css("display", "none");
        }
    });
}

/**
 * 是否登录
 */
function isLogin() {
    $.ajax({
        url: "/is_login",
        type: "get",
        success: function (data) {
            if ("" != data) {
                $("#loginUser").html(data);
                $(".login").css("display", "none");
                $(".login_out").css("display", "block");
            }
        }
    });
}

/**
 * index 初始化
 */
function initIndex() {
    $.ajax({
        url: "/initIndex",
        type: "get",
        async: false,
        success: function (data) {
            //猜你喜欢
            var likeHtml = spliceHtml(data["like"]);
            $("#like").html(likeHtml);
            //电影
            var movieHtml = spliceHtml(data["movie"]);
            $("#movie").html(movieHtml);
            //电视剧
            var tvHtml = spliceHtml(data["tv"]);
            $("#tv").html(tvHtml);
            //动漫
            var animeHtml = spliceHtml(data["anime"]);
            $("#anime").html(animeHtml);
            //综艺
            var varietyHtml = spliceHtml(data["variety"]);
            $("#variety").html(varietyHtml);
        }
    })
}

/**
 * 拼接index video html
 * @param data
 */
function spliceHtml(data) {
    var html = "<ul class=\"video_list list_v_content\">\n";
    if (undefined != data) {
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
    }
    html = html + "</ul>";
    return html;
}

/**
 * index界面 查询
 */
function search() {
    var content = $(".search").val();
    // location.href = "/list_page";
    window.open("/list_page?content=" + content);
}

function getLike(module) {
    $.ajax({
        url: "/like?module=" + module,
        type: "get",
        async: false,
        success: function (data) {
            //猜你喜欢
            var likeHtml = spliceHtml(data);
            $("#like").html(likeHtml);
        }
    })
}