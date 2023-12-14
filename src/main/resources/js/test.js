function fadeInAndOut() {
    var content = document.getElementById('content');

    // 显示内容
    content.style.opacity = 1;

    // 设置一个定时器，2秒后将内容透明度逐渐恢复为0
    setTimeout(function () {
        content.style.opacity = 0;
    }, 2000);
}
