IDE 选择 IDEA Intellij（教材用的是 Eclipse）。

为了与 HDFS 交互，需要添加的依赖为以下目录中的 jar 包：

1. `${HADOOP_HOME}/share/hadoop/common`
2. `${HADOOP_HOME}/share/hadoop/common/lib`
3. `${HADOOP_HOME}/share/hadoop/hdfs` 
4. `${HADOOP_HOME}/share/hadoop/hdfs/lib`

#### 1. 基础 API

参考：

- [Java API](https://hadoop.apache.org/docs/r3.1.3/api/)
- [Shell 命令](http://hadoop.apache.org/docs/r3.1.3/hadoop-project-dist/hadoop-hdfs/HDFSCommands.html)

Java 代码请参考 code 目录中的文件。

（1）向 HDFS 上传任意文本文件，若指定文件已存在，由用户判断执行“追加”还是“覆盖”。

（2）从 HDFS 下载指定文件，如果本地文件同名，则自动对下载的文件重命名。

（3）将 HDFS 中指定文件的内容输出到终端。

（4）显示 HDFS 中指定文件的读写权限、大小、创建时间、路径等信息。

（5）给定 HDFS 中某个目录，对该目录下所有文件执行（4）操作，若是目录则以递归方式执行。

（6）在 HDFS 中根据指定路径创建或删除文件，创建文件时，若所在目录不存在则自动创建目录。

（7）在 HDFS 中根据指定路径创建或删除目录，若父目录不存在则自动创建，若目录不为空则询问用户是否继续。

（8）向 HDFS 中指定文件追加内容，由用户指定将内容追加到原有文件的开头或结尾。

（9）删除 HDFS 中指定文件。

（10）在 HDFS 中将文件从源路径移动到目的路径。

#### 2. 继承 FSDataInputStream

略。（继承的意义是什么？流如何缓存？想说缓冲？）

#### 3. 模拟远程输出

使用 `URL.setUrlStreamHandlerFactory()` 注册由 HDFS 提供的 URL 协议，之后就可以通过 `new URL(hdfsDir)` 来定位 HDFS 中的文件。