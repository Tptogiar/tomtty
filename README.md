# tomtty  基于NIO使用主从Reactor模型编写的HTTP服务器
<hr/>

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


<hr>


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

