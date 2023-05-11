# 问书

## 本项目的安装过程大致如下：

### 1. 下载项目源码

从项目代码托管平台（如 Github、GitLab 等）上下载项目源代码并解压，或者使用 git 工具 进行克隆（git clone 命令）。

### 2. 安装前端依赖

进入前端工程目录，使用 npm 或 yarn 安装项目依赖：

`cd your-proejct-front npm install npm -i`

### 3. 安装后端依赖

进入后端工程目录，使用 Maven 安装项目依赖：
`   cd your-proejct-back mvn install`

### 4. 配置数据库

在 mysql 数据库中创建相应的数据库和数据表，修改后端工程中 resources/application.yml 文件中的数据库连接信息。

### 5. 配置 Redis

在 Redis 缓存数据库中创建相应的缓存数据，修改后端工程中 resources/application.yml 文 件中的 Redis 配置信息。

### 6. 启动项目

在前端工程根目录下执行以下命令启动前端工程：
`   npm run dev`
在后端工程目录下执行以下命令启动后端工程：
`   mvn spring-boot:run`
至此，您已经完成本项目的安装过程，可以访问相应的网站（http://localhost:6060/）进行测试和体验。

# Tip
sql文件在resource文件下
需要修改的内容有：
* mysql数据库账号密码
* redis数据库账号密码
* 修改application.yml文件中腾讯云短信和对象存储服务的secretId和secretKey，当然buckerName、region、url也需要修改。使用者需自行申请阿里云短信和对象存储服务并填充application.yml文件中的密钥等信息。

