# tomtty  基于NIO使用主从Reactor模型编写的HTTP服务器



目录

<hr/>

- [项目名字由来](#项目名字由来)
- [Bio Nio](#Bio-Nio)
  * [Bio模式下请求处理流程示意图](#Bio模式下请求处理流程示意图)
  * [Nio下的主从Reactor模型](#Nio下的主从Reactor模型)
- [配置文件](#配置文件)
- [Quick Start](#Quick-Start)
  * [环境版本说明](#环境版本说明)
  * [配置编译输出路径](#配置编译输出路径)
  * [调整配置文件](#调整配置文件)
  * [启动tomtty-example模块下的TomttyStarter](#启动tomtty-example模块下的TomttyStarter)
  * [启动后日志输出如下](#启动后日志输出如下)
- [使用](#使用)
  * [servlet容器的使用](#servlet容器的使用)
    + [添加自定义servlet程序](#添加自定义servlet程序
    + [配置servlet](#配置servlet)
  * [映射静态资源到服务器](#映射静态资源到服务器)
  * [配置长连接](#配置长连接)
  
<hr>


# 项目名字由来

> Bio模式下参考了tomcat的一些设计及部分源码，nio模式下的线程模型参考了netty的一些设计，所以两个合起来称为tomtty 😀



# Bio Nio
## Bio模式下请求处理流程示意图
![servlet逻辑](https://user-images.githubusercontent.com/79641956/169803890-118191be-aa30-4b07-88f8-a70b14ade969.png)
## Nio下的主从Reactor模型
![image](https://user-images.githubusercontent.com/79641956/172405455-ea0be3b2-9ec9-4773-a081-fc95cf5b8898.png)

# 配置文件
示例配置如下
```
# 服务器地址,及端口
serverHostname=0.0.0.0
serverPort=8848


# 以指定的IO模式启动，支持 bio 和 nio
ioModel=nio


# 从反应器数量
nioSubReactorCount=4


# 线程池配置
threadPoolCorePoolSize=8
threadPoolMaximumPoolSize=200
threadPoolKeepAliveTime=5
thteadPoolBlockingQueueSize=20


# HTTP读取缓存区大小,及Server内OutputStream缓存区大小
httpReadBufferSize=8096
servletOutPutStreamBufferSize=8096


# http keep-alice长连接保留时长（单位为秒），及长连接最大数量
httpKeepAliveTime=60
httpKeepAliveMaxConnection=150


# web.xml路径
webConfigXmlFilePath=/web.xml


# 默认页面路径
notFoundPagePath=/default/pages/html/404.html
internalServerErrorPagePath=/default/pages/html/500.html


# 静态资源根路径，可以以数组的方式配置多个(优先级依次递减)，默认最多8个
staticResourceRootPath[0]=C:\\xxxx
staticResourceRootPath[1]=C:\\xxx
staticResourceRootPath[2]=/xxx
```

# Quick Start
## 环境版本说明
<img src="https://user-images.githubusercontent.com/79641956/172396809-946760f8-4516-43f2-9f91-1e080a770446.png" alt="image" style="zoom:50%;" />
<img src="https://user-images.githubusercontent.com/79641956/172397186-0c7a2672-fcbf-4c22-b1e2-82af0b5d1fc0.png" alt="image" style="zoom:50%;" />

## 配置编译输出路径
将这两个子模块的编译输出路径设置在同一个地方
<img src="https://user-images.githubusercontent.com/79641956/172397771-5988b74e-f946-4e44-92d2-94a56985ec31.png" alt="image" style="zoom:50%;" />

## 调整配置文件
示例配置参考这里[配置文件](#配置文件)


## 启动tomtty-example模块下的TomttyStarter

![image](https://user-images.githubusercontent.com/79641956/172398935-8fb3d72f-8c0a-4e9f-aab8-9a84d0643d5c.png)

## 启动后日志输出如下

<img src="https://user-images.githubusercontent.com/79641956/172399136-d5f50741-48e5-4b26-a523-96961e741fc5.png" alt="image" style="zoom:50%;" />

# 使用
## servlet容器的使用
### 添加自定义servlet程序
- 创建一个Java类并继承自com.tptogiar.servlet.HttpServlet
- 重写父类的service方法
- 在service方法内进行业务处理即可
### 配置servlet
- 创建web.xml文件，并为刚刚的servlet程序配置访问的类路径以及URL
- 下面是示例

![image](https://user-images.githubusercontent.com/79641956/172555965-06583575-8d4b-4bd5-836c-761171d429e2.png)
![image](https://user-images.githubusercontent.com/79641956/172556634-c16f996a-ab2c-4fac-b238-d48117cb5771.png)

## 映射静态资源到服务器
- 将静态资源放置到webapp资源文件夹下即可，也可在配置文件中指定静态资源根路径

![image](https://user-images.githubusercontent.com/79641956/172557031-751dfcb8-605e-4bca-9c0c-b852f13237fe.png)

## 配置长连接

![image](https://user-images.githubusercontent.com/79641956/177781874-5c2ef0cd-49dd-4706-b366-f56805a1f47b.png)

![image](https://user-images.githubusercontent.com/79641956/177781901-b21a291c-f325-4070-b0b4-bc0b02893447.png)



