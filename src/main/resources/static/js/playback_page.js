layui.use(['layer'], function(){
    var layer = layui.layer;
});
$(function () {

    function getUrlParam (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");//构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);//匹配目标参数
        if (r != null) return decodeURIComponent(r[2]); return null;//返回参数值
    }
    var videoId = getUrlParam("videoId");
    var videoUrl = getUrlParam("url");
    var videoName = getUrlParam("name");

    //拼接视频名称
    var nameHtml = "<div class=\"playback_title\">"+videoName+"</div>";
    //拼接优酷接口
    var embedHtml = nameHtml + "<embed src=\""+videoUrl+"\" allowfullscreen=\"true\" " +
        " quality=\"high\" width=\"885\" height=\"480\" align=\"middle\" allowscriptaccess=\"always\" type=\"application/x-shockwave-flash\">";

    $(".Video_playback").html(embedHtml);

    //浏览打分
    setScore(videoId, "browse");

    //用户行为按钮 点击事情初始化
    $("#mark").click(function () {
        setScore(videoId, "mark");
        layer.msg("评分成功");
    });
    $("#comment").click(function () {
        setScore(videoId, "comment");
        layer.msg("评论成功");
    });
    $("#collection").click(function () {
        setScore(videoId, "collection");
        layer.msg("收藏成功");
    });
    $("#share").click(function () {
        setScore(videoId, "share");
        layer.msg("分享成功");
    });

});

/**
 * 用户行为为视频评分
 * @param videoId
 * @param userBehavior
 */
function setScore(videoId, userBehavior) {
    $.ajax({
        url: "/score?userBehavior=" + userBehavior + "&videoId="+videoId,
        type: "get",
        async: true,
        success: function (data) {
            console.log("评分成功");
        }
    })
}