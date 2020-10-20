# kk-project for demo
springboot + mybatis-plus + redis

application.yml -> server.port # 端口号
                -> spring.profiles.active # 激活环境配置文件
                
application-xxx.yml # 对应环境配置文件
application-xxx.yml -> spring.datasource.url # 数据库连接串

0.创建数据库
1.启动CodeGenerator -> 生成curd代码
2.启动ApiApplication -> 启动项目
