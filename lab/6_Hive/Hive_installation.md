请参考：http://dblab.xmu.edu.cn/blog/2440-2/

注意事项：

1. 需要 JDK 8，用新版本容易出现问题，比如 JDK 11 因为改变了 ClassLoader 的继承结构，会出现类转换错误
2. JDBC 驱动包建议上 Maven 找与 MySQL 版本相匹配的，如果驱动包是 8.0 以后的，在 `hive-site.xml` 中指定的驱动类应为 `com.mysql.cj.jdbc.Driver`
3. 安装后，直接通过 hive 指令进入会话，这个方式只启动了 client 服务，使用本地的 metastore，一般只用于做一些简单测试