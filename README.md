# chat-fron

#### 项目介绍
该项目是chat聊天IM app的服务端项目,架构为springBoot + SpringMVC + Mybatis + netty构建而成。
实现聊天功能方式有很多种,可以是轮训式,可以是服务端转发式。
本项目采用服务端转发式,用户A要想于用户B通信,需要发送给服务器,通过服务器转发推送给用户B。

#### 安装教程

1. git克隆项目到本地
2. 通过目录中的sql文件创建数据库
3. 用你喜欢的ide打开并运行
4. 因为本项目文件存储采用fdfs,所以部署该服务还需要部署fdfs服务。可以参考[fdfs](https://github.com/happyfish100/fastdfs)

#### 使用说明

1. 修改配置文件
```
fdfs:
  so-timeout: 1501
  connect-timeout: 601
  thumb-image:             #缩略图生成参数
    width: 80
    height: 80
  tracker-list:            #TrackerList参数,支持多个
    - fdfs服务器地址:22122
```
