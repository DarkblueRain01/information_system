//显示验证结果
function show_validate_msg(ele, status, msg) {
    if ("success" == status) {
        // 让父容器变色
        $(ele).parent().addClass("has-success");
        // 给sapn赋值正确信息
        $(ele).next("span").text(msg);
    } else if ("error" == status) {
        // 让父容器变色
        $(ele).parent().addClass("has-error");
        // 给sapn赋值错误信息
        $(ele).next("span").text(msg);
    }
}
