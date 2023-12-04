# 使用jdk8 镜像作为基础镜像
FROM cs2ag/docker-solrintegernet:latest

# 设置工作目录
WORKDIR /app

# 复制可执行 JAR 文件到容器中
COPY 2.jar /app/app.jar

# 定义容器启动命令
CMD ["/app/app.jar"]
