#### 1. 基础操作

1）用 HBase Shell 命令创建 Student 表并录入数据

```shell
create 'Student', 'Score'
put 'Student', 'zhangsan', 'Score:English', '69'
put 'Student', 'zhangsan', 'Score:Math', '86'
put 'Student', 'zhangsan', 'Score:Computer', '77'
put 'Student', 'lisi', 'Score:English', '55'
put 'Student', 'lisi', 'Score:Math', '100'
put 'Student', 'lisi', 'Score:Computer', '88'
```

2）用 scan 命令浏览 Student 表

```shell
scan 'Student'
```

输出：

```shell
ROW                   COLUMN+CELL
 lisi                 column=Score:Computer, timestamp=1629472491693, value=88
 lisi                 column=Score:English, timestamp=1629472490087, value=55
 lisi                 column=Score:Math, timestamp=1629472490106, value=100
 zhangsan             column=Score:Computer, timestamp=1629472490064, value=77
 zhangsan             column=Score:English, timestamp=1629472483175, value=69
 zhangsan             column=Score:Math, timestamp=1629472490019, value=86
2 row(s)
Took 0.0301 seconds
```

3）查询 zhangsan 的 Computer 成绩

```shell
get 'Student', 'zhangsan', 'Score:Computer'
```

输出：

```shell
COLUMN                CELL
 Score:Computer       timestamp=1629472490064, value=77
1 row(s)
Took 0.0340 seconds
```

4）修改 lisi 的 Math 成绩为 95

```shell
put 'Student', 'lisi', 'Score:Math', '95'
```

#### 2. 客户端 API

使用 Maven 引入 `hbase-client` 和 Hadoop 相关依赖，详见 `TaskHBase.java` 。

