在 Ubuntu 上安装 MongoDB 同样可以使用 apt，请先更新软件源。

参考：https://docs.mongodb.com/manual/tutorial/install-mongodb-on-ubuntu/

#### 1. 基础 API

1）用 MongoDB Shell 设计出 Student 集合

```shell
use test
# 创建集合
db.createCollection("student")
# 插入数据
db.student.insertMany([
	{ name: "zhangsan", score: { English: 69, Math: 86, Computer: 77 } },
	{ name: "lisi", score: { English: 55, Math: 100, Computer: 88 } }
])
```

2）用 find() 方法输出学生信息

```she
db.student.find()
```

3）用 find() 方法查询 zhangsan 的所有成绩（只显示 score 列）

```shell
db.student.find({ name: "zhangsan" }, { score: 1, _id: 0 })
```

4）修改 lisi 的 Math 成绩为 95

```shell
db.student.updateOne({ name: "lisi" }, { $set: { "score.Math": 95 } })
```

#### 2. 客户端 API

引入 `mongo-java-driver` 依赖，代码详见 `TaskMongoDB.java` 。

