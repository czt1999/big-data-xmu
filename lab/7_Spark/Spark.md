#### 1. 本地模式

安装 Spark 请参考：http://dblab.xmu.edu.cn/blog/2501-2/

使用本地模式，只需要在 `spark-env.sh` 中添加以下配置：

```bash
# 将这里的 <HADOOP_HOME> 替换成你本机的 HADOOP_HOME 实际值
export SPARK_DIST_CLASSPATH=$(<HADOOP_HOME>/bin/hadoop classpath)
```

#### 2. Spark Shell

在 Spark 的安装目录下，使用 `bin/spark-shell` 可启动 Spark Shell。

https://spark.apache.org/docs/latest/quick-start.html

1）读取本地文件"/home/hadoop/test.txt"并统计文件行数

```shell
# textFile 是一个 RDD（Resilient Distributed Dataset），数据集由各行文本构成
var textFile = sc.textFile("file:///home/hadoop/test.txt")
testFile.count()
```

2）读取 HDFS 文件"/user/hadoop/test.txt"并统计文件行数

```shell
var textFile = sc.textFile("hdfs://user/hadoop/test.txt")
testFile.count()
```

3）用 Scala 编写独立应用程序，读取 HDFS 文件"/user/hadoop/test.txt"并统计文件行数

Scala 3 已经发布了稳定版，不过对于初学者来说，从 Scala 2 开始是一个比较好的选择。

前往 https://www.scala-sbt.org/download.html 下载 sbt，作为 Scala 项目构建工具。

sbt 会自动拉取指定的 Scala 版本。你也可以前往 https://scala-lang.org/download ，选择合适的方式自行安装 Scala。

代码请参考 `code/TaskSpark.scala` 。

4）通过 sbt 将 3 的应用程序打包成 JAR，并通过 spark-submit 提交到 Spark 中运行

```shell
sbt package
cd $SPARK_HOME
bin/spark-submit --class "TaskSpark" <jar_path>
```

