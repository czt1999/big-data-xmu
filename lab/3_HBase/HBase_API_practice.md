启动 HBase 后，执行 `hbase shell` 可开启会话。

进行 Java 编程需要引入的 jar 包可以在 `$HABASE_HOME​/lib` 及其子目录中找到。

#### 1. 基础API

参考：

- [Java API](https://hbase.apache.org/2.3/apidocs/index.html)
- [Shell 命令](https://hbase.apache.org/book.html#shell)

#### 2. 根据所给数据建表

给出关系数据库中的表。

1）Student

| S_No    | S_Name   | S_Sex  | S_Age |
| ------- | -------- | ------ | ----- |
| 2015001 | Zhangsan | male   | 23    |
| 2015002 | Mary     | female | 22    |
| 2015003 | Lisi     | male   | 24    |

2）Course

| C_No   | C_Name           | C_Credit |
| ------ | ---------------- | -------- |
| 123001 | Math             | 2.0      |
| 123002 | Computer Science | 5.0      |
| 123003 | English          | 3.0      |

3）SC

| SC_Sno  | SC_Cno | SC_Score |
| ------- | ------ | -------- |
| 2015001 | 123001 | 86       |
| 2015001 | 123003 | 69       |
| 2015002 | 123002 | 77       |
| 2015002 | 123003 | 99       |
| 2015003 | 123001 | 98       |
| 2015003 | 123002 | 95       |

教材官方的方法就是照搬三张表，不知意义何在？

我的思路是 1 和 2 可以照搬，3 利用列族将同一学生的成绩放在同一行。

```shell
create 'Student','Name','Sex','Age'
put 'Student','2015001','Name','Zhangsan'
put 'Student','2015001','Sex','male'
put 'Student','2015001','Age','23'
put 'Student','2015001','Name','Mary'
put 'Student','2015002','Sex','female'
put 'Student','2015002','Age','22'
put 'Student','2015001','Name','Lisi'
put 'Student','2015003','Sex','male'
put 'Student','2015003','Age','24'

create 'Course','Name','Credit'
put 'Course','123001','Name','Math'
put 'Course','123001','Credit','2.0'
put 'Course','123002','Name','Computer Science'
put 'Course','123002','Credit','5.0'
put 'Course','123003','Name','English'
put 'Course','123003','Credit','3.0'

create 'SC','Score'
put 'SC','2015001','Score:123001','86'
put 'SC','2015001','Score:123003','69'
put 'SC','2015002','Score:123002','77'
put 'SC','2015002','Score:123003','99'
put 'SC','2015003','Score:123001','98'
put 'SC','2015003','Score:123002','95'
```

