在 Ubuntu 上安装 Redis 可以直接使用 apt 包管理工具，先通过 `add-apt-repository  ppa:chris-lea/redis-server` 将 redis 仓库加入到软件源，再 upadte、install。

安装完毕后 `redis-server` 会自动启动，在终端输入 `redis-cli` 进入会话。

#### 1.基础操作

指令文档：https://redis.io/commands

给出 Student 的键值对：

```json
zhangsan: {
    English:  69,
    Math:     86,
    Computer: 77
}
list: {
    English:  55,
    Math:     100,
    Computer: 88   
}
```

1）用 Redis 的哈希结构设计出 Student 表（可以用 student.zhangsan 和 student.lisi 来表示两个键值属于同一个表）

```shell
# hset => https://redis.io/commands/hset
hset student.zhangsan English 69
hset student.zhangsan Math 86
hset student.zhangsan Computer 77
hset student.lisi English 55
hset student.lisi Math 100
hset student.lisi Computer 88
```

2）用 hgetall 命令分别输出 zhangsan 和 lisi 的成绩信息

```shell
# hgetall => https://redis.io/commands/hgetall
hgetall student.zhangsan
hgetall student.lisi
```

3）用 hget 命令查询 zhangsan 的 Computer 成绩

```shell
# https://redis.io/commands/hget
hget student.zhangsan Computer
```

4）修改 lisi 的 Math 成绩为95

```shell
# https://redis.io/commands/hset
hset student.lisi Math 95
```

#### 2. 客户端 API

引入 `redis-client:jedis` 依赖，代码详见 `TaskRedis.java` 。