### HBase 安装与启动

参考：http://dblab.xmu.edu.cn/blog/2442-2/

#### 1. 下载

http://archive.apache.org/dist/hbase/

该实验选择 2.2.2 版本，直接下载 bin。

解压至 `/usr/local` 目录下。

```bash
sudo tar -zxf ~/下载/hbase-2.2.2-bin.tar.gz -C /usr/local
sudo mv /usr/local/hbase-2.2.2-bin.tar.gz /usr/local/hbase
```

#### 2. 配置环境变量

配置过程不做赘述。

在终端输入 `hbase version` 检查是否配置成功。

若出现如下错误：

```bash
java.lang.ClassNotFoundException: org.apache.hadoop.hbase.util.GetJavaProperty
```

请参考：https://reviews.apache.org/r/69299/diff/2/?expand=1，修改 `$HBASE_HOME/bin/hbase` 。

#### 3. 单机模式配置

1) hbase-env.sh

```bash
# $HBASE_HOME/bin/hbase/conf/hbase-env.sh
export JAVA_HOME=...
export HBASE_MANAGES_ZK=true 
```

2) hbase-site.xml

由于 `hbase.rootdir` 默认是在 /tmp 下，重启系统会丢失数据，最好重新配置。

```xml
<configuration>
	<property>
		<name>hbase.rootdir</name>
		<value>file:///usr/local/hbase/hbase-tmp</value>
	</property>
</configuration>
```

配置完成后，启动 HBase：

```bash
start-hbase.sh
```

使用 `jps` 可以看到一个名为 HMaster 的进程。

#### 4. 伪分布式模式配置

同样地，只需修改 hbase-env.sh 和 hbase-site.xml，具体请参考教程官网。

先启动 Hadoop，再启动 HBase。

#### 5. 关闭

```bash
stop-hbase.sh
```

若一直显示 `stopping hbase ...` ，则先退出当前进程，换成以下命令：

```bash
hbase-daemon.sh stop master
```

再执行 `stop-hbase.sh` 。
