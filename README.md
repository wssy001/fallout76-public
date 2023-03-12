<br>
<div align="center" style="font-size: 40px">
Fallout 76小助手
</div>

***
## 写在开头
>
> ###   本项目 不强制用户安装使用JDK
> ####  本项目 不强制用户安装使用JDK
> ##### 本项目 不强制用户安装使用JDK
> 
***
## 项目简介
### 这是一个使用 Quarkus + Project Loom + GraalVM 开发的项目
### 本项目使用Open JDK 19，并提供了无需JVM环境使用的解决方案
### 目前 辐射76小助手 可以为 QQ、QQ频道以及Kook（原开黑了）的用户提供服务
### 其中 QQ、QQ频道的支持是通过 [Go-CQHttp](https://github.com/Mrs4s/go-cqhttp) 实现的
### 如果你有想在其他平台上使用本项目，欢迎[点此提交issue](https://github.com/wssy001/fallout76-public/issues/new?assignees=&labels=feature+request&template=feature.md&title=)
***
## 使用帮助
> #### 初次运行程序，会在项目同级目录下生成 <code>config</code> 文件夹，内部有 `application.yml`（项目配置文件）、`nukaCode.json`（核弹密码，会自动检测有效期并自动更新）和`photos.json`（图片，指令所需图片的Url）
> #### 接入 `Kook` 、`Go-CQHttp` 时，与程序的连接方式选 `webhook`
## 如何运行
### 运行须知
- Windows
> 在Windows 10 专业工作站版 64位 22H2 下测试运行通过， 需要额外安装 [VC++ 2015-2022 Redistributable工具](https://learn.microsoft.com/en-US/cpp/windows/latest-supported-vc-redist?view=msvc-170#visual-studio-2015-2017-2019-and-2022)
- Linux
> 在 Ubuntu 20.04.5 LTS WSL2、Docker镜像 中测试运行通过
### Native
>#### （推荐）运行中不需要JVM，不强制用户安装使用JDK
> #### 进入 [Release](https://github.com/wssy001/fallout76-public/releases) 页面，选择最新版本以及相应平台，下载相应的压缩包并解压
> #### 执行压缩包内的 <code>run.sh</code> 即可启动程序
### Docker
> ####  （推荐）运行中不需要JVM，不强制用户安装使用JDK
> ####   前往[本项目Docker仓库](https://hub.docker.com/r/wssy001/fallout76-public)，选择合适的镜像，使用<code>docker run -itd --name fallout76-assistant -p 35701:35701 -v /path/to/config:/docker/image/path/to/config fallout76:latest</code>
### Jar包
>#### （不推荐此类方式）运行中需要JVM，强制用户安装使用JDK
>####  下载前请先确保你安装了Open JDK 19及以上
>####  下载最新版本的.jar文件，使用<code>java -server --enable-preview -Xmx512m -Xms128m -jar xxx.jar</code> 即可运行
***
### 如有疑问，欢迎交流
