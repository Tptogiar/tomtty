# Tomdog
简易版Tomcat

[toc]

- [Tomdog](#tomdog)
- [运行流程](#----)
  * [环境版本说明](#------)
  * [配置编译输出路径](#--------)
  * [启动example模块下的TomdogStarter](#--example----tomdogstarter)
  * [启动后日志输出如下](#---------)
- [Bio模式下请求处理流程示意图](#bio------------)

<small><i><a href='http://ecotrust-canada.github.io/markdown-toc/'>Table of contents generated with markdown-toc</a></i></small>




# 运行流程
## 环境版本说明
<img src="https://user-images.githubusercontent.com/79641956/172396809-946760f8-4516-43f2-9f91-1e080a770446.png" alt="image" style="zoom:50%;" />
<img src="https://user-images.githubusercontent.com/79641956/172397186-0c7a2672-fcbf-4c22-b1e2-82af0b5d1fc0.png" alt="image" style="zoom:50%;" />

## 配置编译输出路径
将这两个子模块的编译输出路径设置在同一个地方
<img src="https://user-images.githubusercontent.com/79641956/172397771-5988b74e-f946-4e44-92d2-94a56985ec31.png" alt="image" style="zoom:50%;" />

## 启动example模块下的TomdogStarter
![image](https://user-images.githubusercontent.com/79641956/172398935-8fb3d72f-8c0a-4e9f-aab8-9a84d0643d5c.png)

## 启动后日志输出如下
<img src="https://user-images.githubusercontent.com/79641956/172399136-d5f50741-48e5-4b26-a523-96961e741fc5.png" alt="image" style="zoom:50%;" />






# Bio模式下请求处理流程示意图
![servlet逻辑](https://user-images.githubusercontent.com/79641956/169803890-118191be-aa30-4b07-88f8-a70b14ade969.png)
