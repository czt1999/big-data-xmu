## 1 Hadoop 安装和伪分布式配置

参考：http://dblab.xmu.edu.cn/blog/2441-2/

#### 1.1 环境准备

1）安装 Ubuntu 16.04

2）更新软件源

```bash
sudo apt-get update
```

3）安装文本编辑软件 Vim

```bash
sudo apt-get install vim
```

4）安装 SSH

Ubuntu 已默认安装 openssh-client，还需要手动安装 openssh-server。

```bash
sudo apt-get install openssh-server
```

设置本地免密登陆。

按照教程的步骤来，如果不能实现免密登录，请检查 authorized_keys 的文件权限和 ssh 配置文件：

1. 设置 authorized_keys 的文件权限为 700，`chmod 700 authorized_keys`
2. 将 `/etc/ssh/sshd_config` 中的 `StrictModes` 设置为 no，将 `AuthorizedKeysFile` 的注释放开
3. 重启 ssh，`service sshd restart`

如果还不成功，请检查防火墙和 SELinux 状态。

5）安装 JDK 并设置好环境变量（`/etc/profile`）

#### 1.2 安装 Hadoop

1）下载

前往 Hadoop 官网下载，使用 [3.1.3](https://hadoop.apache.org/release/3.1.3.html) 版本。

解压至`/usr/local/hadoop`。

2）设置环境变量

```bash
export HADOOP_HOME=/usr/local/hadoop
export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native

export PATH=...:$HADOOP_HOME/bin
```

执行`source /etc/profile` 使配置生效（这里的环境变量只在当前终端有效，请保持终端会话，或者重启使该配置在系统层次生效）

#### 1.3 伪分布式配置

Hadoop 的配置文件位于 `/usr/local/hadoop/etc/hadoop/` 中，需要修改2个配置文件： core-site.xml、hdfs-site.xml，具体内容参考教程。

#### 1.4 运行 Hadoop

1）格式化 NameNode

```bash
hdfs namenode -format
```

NameNode 负责保存 HDFS 的元数据信息，比如命名空间信息、块信息等。

2）启动 NameNode 和 DataNode

```bash
cd /usr/local/hadoop
sbin/start-dfs.sh
```

如果出现如下错误：

```
sign_and_send_pubkey: signing failed: agent refused operation
```

请检查是否遵照上述步骤配置 ssh，并执行：

```bash
ssh-add
```

用 `jps` 查看进程，除了 NameNode 和 DataNode，还有一个 SecondaryNameNode，它的职责是帮助 NameNode 保存文件系统的快照。

顺利启动后，可以用浏览器访问 [http://localhost:9870](http://localhost:9870/) 查看 NameNode 和 Datanode 信息。

#### 1.5 运行 example

1）在 HDFS 中创建用户目录

```bash
hdfs dfs -mkdir -p /user/{username}
```

之后可以在 hdfs 命令中使用相对路径，会转换成当前用户对应的目录。

2）运行 grep 示例

不妨以 hadoop 配置文件作为输入：

```bash
hdfs dfs -mkdir input
hdfs dfs -put ./etc/*.xml input
```

使用 mapreduce 框架运行 grep，查找以 "dfs" 为前缀的内容：

```bash
hadoop jar \
./share/hadoop/mapreduce/hadoop-mapreduce-examples-3.1.3.jar \ 
grep input output 'dfs[a-z.]+'
```

查看输出：

```bash
hdfs dfs -cat output/*
```

#### 1.6 关闭 Hadoop

```bash
./sbin/stop-dfs.sh
```

