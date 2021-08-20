#### 1. 基础操作

1）创建 Student 表，并录入数据

```sql
CREATE TABLE `t_student` (
    `student_id` INT AUTO_INCREMENT,
    `student_name` VARCHAR(32) NOT NULL,
    `english` INT(3) NOT NULL,
    `math` INT(3) NOT NULL,
    `computer` INT(3) NOT NULL,
    PRIMARY KEY (`student_id`)
);

INSERT INTO `t_student` VALUES(null, 'zhangsan', 69, 86, 77);
INSERT INTO `t_student` VALUES(null, 'lisi', 55, 100, 88);
```

2）输出所有记录

```sql
SELECT * FROM `t_student`;
```

3）查询 zhangsan 的 Computer 成绩

```sql
SELECT `computer` FROM `t_student` WHERE `student_name` = 'zhangsan';
```

4）修改 lisi 的 Math 成绩为 95

```sql
UPDATE `t_student` SET `math` = 95 WHERE `student_name` = 'lisi';
```

#### 2. 客户端 API

1）向 Student 添加一条记录：“scofield，45，89，100”

2）获取 scofield 的 English 成绩

引入 `mysql-connector-java` 依赖，代码详见 `TaskMySQL.java` 。

