# tomtty是一个HTTP网络框架,使用一主多从Reactor线程模型编写而成，支持NIO的同时，也支持使用传统的BIO模式

<hr/>
<br/><br/>

# 目录

- [目录](#目录)
- [Feature](#Feature)
- [关于项目中Bio与Nio](#关于项目中Bio与Nio)
  * [Bio模式下请求处理流程示意图](#Bio模式下请求处理流程示意图)
  * [Nio下的主从Reactor模型](#Nio下的主从Reactor模型)
- [目录结构](#目录结构)
- [配置文件](#配置文件)
- [使用本服务器](#使用本服务器)
  * [servlet容器的使用](#servlet容器的使用)
    + [添加自定义servlet程序](#添加自定义servlet程序)
    + [配置servlet](#配置servlet)
  * [映射静态资源到服务器](#映射静态资源到服务器)
  * [配置长连接](#配置长连接)
- [Quick Start](#Quick-Start)
  * [环境版本说明](#环境版本说明)
  * [配置编译输出路径](#配置编译输出路径)
  * [调整配置文件](#调整配置文件)
  * [启动tomtty-example模块下的TomttyStarter](#启动tomtty-example模块下的TomttyStarter)
  * [启动后日志输出如下](#启动后日志输出如下)

<hr><br/><br/>

# Feature

- 为减低并发时对资源的消耗，提高服务器并发性能，将 IO 模型从阻塞式 IO 替换为 IO 多路复用中的 epoll 模型，在同等的并发条件下，性能提升 20%左右
- 采用主从 Reactor+线程池并发模型，主 Reactor 轮询方式将连接分到从 Reactor，实现负载均衡
- 将静态资源回传客户端时，使用 sendfile 零拷贝进行传输，同等条件下传输性能提升 50%以上
- 主 Reactor 通过从 Reactor 上的任务队列完成事件注册，而不是使用锁，减少了使用锁的开销，锁的争用只会出现在任务队列的读写上
- 实现双缓存队列替换原来的阻塞队列，减少锁的竞争，提高从 Reactor 内任务队列的并发度，进一步提升主从 Reactor 对任务队列的读写性能
- 使用基于小根堆的定时器关闭超时的非活动连接，减少对服务器资源的占用
- 将 HTTP 请求解析后封装成 Request 对象传给用户定义的 servlet 程序，实现简易的 servlet 容器



# 关于项目中Bio与Nio

[回到目录👆](#目录)

<details open="open">

  <summary><h6>Bio模式下请求处理流程示意图</h6></summary>
  流程：接受连接 -> 读取数据 -> 解析HTTP抱文 -> 处理请求 -> 封装响应 -> 发送响应
 
![servlet逻辑](https://user-images.githubusercontent.com/79641956/169803890-118191be-aa30-4b07-88f8-a70b14ade969.png)
 
</details>

<details open="open">
  <summary><h6>Nio模式下的一主多从Reactor模型示意图</h6></summary>
  
![image](https://user-images.githubusercontent.com/79641956/172405455-ea0be3b2-9ec9-4773-a081-fc95cf5b8898.png)
 
</details>


# 目录结构


[回到目录👆](#目录)

<details open="open"><summary><h6>目录结构</h6></summary>

```
├── tomtty-core
│  ├── pom.xml
│  └── src
│      ├── main
│      │  ├── java.com.tptogiar
│      │  │            ├── component            相关组件
│      │  │            │  ├── connection        连接管理
│      │  │            │  │  └── ...
│      │  │            │  ├── dispatch          分发器
│      │  │            │  │  ├── ...
│      │  │            │  ├── pool              连接池
│      │  │            │  │  ├── ...
│      │  │            │  └── queue             任务队列
│      │  │            │      └── ...
│      │  │            ├── config               相关配置
│      │  │            │  └── ...
│      │  │            ├── constant             静态常量
│      │  │            │  ├── ...  
│      │  │            │  └── http
│      │  │            │      ├── ...
│      │  │            ├── exception
│      │  │            │  ├── ...
│      │  │            ├── holder
│      │  │            │  └── ...
│      │  │            ├── info
│      │  │            │  ├── ...
│      │  │            ├── network              网络模块
│      │  │            │  ├── bio               bio模块
│      │  │            │  │  ├── accept         请求接收
│      │  │            │  │  │  └── ...
│      │  │            │  │  ├── builder        相依构建器
│      │  │            │  │  │  └── ...
│      │  │            │  │  ├── endpoint       端到端控制
│      │  │            │  │  │  └── ...
│      │  │            │  │  ├── handler        处理器
│      │  │            │  │  │  ├── ...
│      │  │            │  │  └── parser         请求解析器
│      │  │            │  │      └── ...
│      │  │            │  ├── ...
│      │  │            │  └── nio                nio模块
│      │  │            │      ├── connection    
│      │  │            │      │  └── ...
│      │  │            │      ├── eventloop      事件循环
│      │  │            │      │  ├── ...
│      │  │            │      ├── handler        处理器
│      │  │            │      │  ├── http
│      │  │            │      │  │  └── ...
│      │  │            │      │  └── tcp
│      │  │            │      │      └── ...
│      │  │            │      ├── ...
│      │  │            │      ├── poller        轮询器
│      │  │            │      │  ├── ...
│      │  │            │      └── task
│      │  │            │          └── ...
│      │  │            ├── servlet              servlet容器相关
│      │  │            │  ├── ...
│      │  │            │  ├── component
│      │  │            │  │  ├── ...
│      │  │            │  ├── context
│      │  │            │  │  ├── impl
│      │  │            │  │  │  ├── ...
│      │  │            │  │  └── ...
│      │  │            │  ├── defaultServlet
│      │  │            │  │  └── ...
│      │  │            │  └── wrapper
│      │  │            │      ├── ...
│      │  │            └── util
│      │  │                ├── ...
│      │  └── resources
│      │      ├── ...
│      │      └── tomtty.config.properties
│      └── test
├── tomtty-example                              框架使用示例
│  ├── pom.xml
│  └── src
│      └── main
│          ├── java
│          │  ├── dao
│          │  ├── service
│          │  ├── servlet
│          │  └── TomttyStarter.java
│          └── webapp
│              ├── ...
├── pom.xml
├── README.md
└── 部分压测数据.md

95 directories, 119 files

```

</details>


# 配置文件

[回到目录👆](#目录)

<details open="open"><summary><h6>示例配置如下</h6></summary>

```
# 服务器地址,及端口
serverHostname=0.0.0.0
serverPort=8848


# 以指定的IO模式启动，支持 bio 和 nio
ioModel=nio


# 从反应器数量
nioSubReactorCount=6


# 线程池配置
threadPoolCorePoolSize=8
threadPoolMaximumPoolSize=200
threadPoolKeepAliveTime=5
thteadPoolBlockingQueueSize=20


# HTTP读取缓存区大小,及Server内OutputStream缓存区大小
httpReadBufferSize=8096
servletOutPutStreamBufferSize=8096


# http keep-alice长连接保留时长（单位为秒），及长连接最大数量
httpKeepAliveTime=100
httpKeepAliveMaxConnection=150

# 连接管理器的检查时间间隔，单位毫秒
connectionMgrCheckInterval=1000


# web.xml路径
webConfigXmlFilePath=/web.xml


# 默认页面路径
notFoundPagePath=/default/pages/html/404.html
internalServerErrorPagePath=/default/pages/html/500.html


# 静态资源根路径，可以以数组的方式配置多个(优先级依次递减)，默认最多8个
staticResourceRootPath[0]=C:\\MyFiles\\CodeFlies\\Project\\tomtty\\tomtty\\tomtty-core\\src\\main\\resources
staticResourceRootPath[1]=C:\\MyFiles\\CodeFlies\\Project\\tomtty\\tomtty\\tomtty-example\\src\\main\\webapp
staticResourceRootPath[2]=/myFiles/test/tomtty
```

</details>

# 使用本服务器

[回到目录👆](#目录)


<details open="open">
  <summary><h6>添加自定义servlet程序</h6></summary>

- 创建一个Java类并继承自com.tptogiar.servlet.HttpServlet
- 重写父类的service方法
- 在service方法内进行业务处理即可，还原javaWeb使用体验

![image](https://user-images.githubusercontent.com/79641956/177810666-1ac9dcb9-f182-4e89-8150-f47958d45458.png)
  
  </details>



<details open="open">
  <summary><h6>配置servlet</h6></summary>

- 创建web.xml文件，将web.xml文件配置至配置文件tomtty.config.properties中，再为刚刚的servlet程序配置访问的类路径以及URL
- 下面是示例
- ![image](https://user-images.githubusercontent.com/79641956/172556634-c16f996a-ab2c-4fac-b238-d48117cb5771.png)
  ![image](https://user-images.githubusercontent.com/79641956/177811120-2e5713c9-0d29-4d37-83a4-7dc34b8ca8e3.png)
  
  </details>


<details open="open">
  <summary><h6>映射静态资源到服务器</h6></summary>

[回到目录👆](#目录)

- 将静态资源放置到webapp资源文件夹下即可，也可在配置文件中指定静态资源根路径
  ![image](https://user-images.githubusercontent.com/79641956/172557031-751dfcb8-605e-4bca-9c0c-b852f13237fe.png)
  
  </details>

<details open="open">
  <summary><h6>配置长连接</h6></summary>

[回到目录👆](#目录)

![image](https://user-images.githubusercontent.com/79641956/177781874-5c2ef0cd-49dd-4706-b366-f56805a1f47b.png)
 
使用wireshark验证长连接的建立和关闭
 
![image](https://user-images.githubusercontent.com/79641956/177781901-b21a291c-f325-4070-b0b4-bc0b02893447.png)

</details>

# Quick Start

[回到目录👆](#目录)

<details open="open">
  <summary><h6>环境版本说明</h6></summary>
  采用1.8版本的编译器，语言级别也使用8版本

<img src="https://user-images.githubusercontent.com/79641956/177809071-d51680de-ad1f-41df-a862-0db422c8dbfe.png" alt="image" style="zoom:50%;" />
<img src="https://user-images.githubusercontent.com/79641956/177808894-3ad43507-4022-4d6d-857c-7037c3eb73d1.png" alt="image" style="zoom:50%;" />

 
</details>

<details open="open">
  <summary><h6>配置编译输出路径</h6></summary>
  (有报错的情况下)将这两个子模块的编译输出路径设置在同一个地方
<img src="https://user-images.githubusercontent.com/79641956/172397771-5988b74e-f946-4e44-92d2-94a56985ec31.png" alt="image" style="zoom:50%;" />

 
</details>


<details open="open">
  <summary><h6>调整配置文件</h6></summary>
  示例配置参考这里[配置文件](#配置文件)
</details>

<details open="open">
  <summary><h6>启动tomtty-example模块下的TomttyStarter</h6></summary>
  
![image](https://user-images.githubusercontent.com/79641956/177809960-782e36a0-2a16-41ec-90f7-c5101f5c8081.png)
 
</details>


<details open="open">
  <summary><h6>启动后日志输出如下</h6></summary>
  
<img src="https://user-images.githubusercontent.com/79641956/177810275-9d875327-096a-4777-8067-de5483363987.png" alt="image" style="zoom:50%;" />
 
</details>



<br/><br/>
[回到目录👆](#目录)
