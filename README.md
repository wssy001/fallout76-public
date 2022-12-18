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
### 基于 [Go CQHttp](https://github.com/Mrs4s/go-cqhttp) 开发
### 这是一个使用 Quarkus + Project Loom + GraalVM 开发的项目
### 本项目使用Open JDK 19，并提供了无需JVM环境使用的解决方案
### 目前 辐射76小助手 可以为 QQ、QQ频道以及Kook（原开黑了）的用户提供服务
### 如果你有想在其他平台上使用本项目，欢迎[点此提交issue](https://github.com/wssy001/fallout76-public/issues/new?assignees=&labels=feature+request&template=feature.md&title=)
***

## 如何运行
### Native
>#### （推荐）运行中不需要JVM，不强制用户安装使用JDK
> #### 进入 [Release](https://github.com/wssy001/fallout76-public/releases) 页面，选择最新版本以及相应平台的可执行文件，点击下载即可
### Docker
> ####  （推荐）运行中不需要JVM，不强制用户安装使用JDK
> ####   前往[本项目Docker仓库](https://hub.docker.com/r/wssy001/fallout76-public)，选择合适的镜像，使用<code>docker run -itd --name fallout76-assistant -p 35701:35701 -v /path/to/config:/docker/image/path/to/config fallout76:latest</code>
### Jar包
>#### （不推荐此类方式）运行中需要JVM，强制用户安装使用JDK
>####  下载前请先确保你安装了Open JDK 19及以上
>####  下载最新版本的.jar文件，使用<code>java -server --enable-preview -Xmx512m -Xms128m -jar xxx.jar</code> 即可运行
***
### 如有疑问，欢迎交流
