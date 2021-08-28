1）创建一个内部表 stocks

```sql
CREATE TABLE IF NOT EXISTS stocks(
  `exchange` string,
  `symbol` string,
  `ymd` string,
  `price_open` float,
  `price_high` float,
  `price_low` float,
  `price_close` float,
  `volume` int,
  `price_adj_close` float
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';
```

内部表又称托管表（managed table）。配置文件 `hive-default.xml` 指定了内部表的默认存放位置，一般是 HDFS 上的 `/user/hive/warehouse` ，在建表时可以用 `LOCATION` 关键字进行自定义。

删除内部表时，所有内容都会从 HDFS 中移除。

2）创建一个外部分区表 dividends，分区字段为 exchange 和 symbol

```sql
CREATE EXTERNAL TABLE IF NOT EXISTS dividends(
  `ymd` string,
  `dividend` float
) 
PARTITIONED BY(
  `exchange` string,
  `symbol` string
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';
```

删除外部表时，只有相关的元数据被删除，其内容不受影响。

3）向 stocks 表导入外部数据

```sql
LOAD DATA INPATH 'file:///usr/local/hadoop/input/data/stocks/stocks.csv' 
INTO TABLE stocks;
# 共 2075394 条记录
```

4）创建一个未分区的外部表 dividends_unpartitioned，并导入数据

```sql
CREATE EXTERNAL TABLE IF NOT EXISTS dividends_unpartitioned(
  `exchange` string,
  `symbol` string,
  `ymd` string,
  `dividend` float
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';
# 为了与待导入的 csv 文件保持一致顺序
# 这里的字段顺序跟教材给出的表结构有所不同 

LOAD DATA INPATH 'file:///usr/local/hadoop/input/data/dividends/dividends.csv' 
INTO TABLE dividends_unpartitioned;
# 共 15208 条记录
```

5）以针对 dividends_unpartitioned 的查询为基础，利用 Hive 自动分区特性向分区表 dividends 各个分区中插入数据

```sql
SET hive.exec.dynamic.partition.mode=nonstrick;
SET hive.exec.max.dynamic.partitions.pernode=1500;

INSERT OVERWRITE TABLE dividends partition(`exchange`,`symbol`)
SELECT du.`ymd`, du.`dividend`, du.`exchange`, du.`symbol`
FROM dividends_unpartitioned du;
# 注意 SELECT 的字段顺序
# 用于分区的 exchange 和 symbol 必须放在最后
```

这里的自动分区特性即“动态分区”，需设置非严格模式。

此外，对于这份数据，还需要调整每个节点的最大分区数，默认的 100 不够用。

若执行失败，请查看日志文件（默认位置在 `/tmp/$USERNAME` 中）。如果发生内存溢出，可以通过设置 `hive-site.xml` 中的 `mapred.map.child.java.opts` 和 `mapred.reduce.child.java.opts` 属性来解决。

6）查询 IBM（symbol=IBM）从 2000 年起所有支付股息的交易日（dividends 表中有对应记录）的收盘价

```sql
SELECT s.`ymd`, s.`price_close`
FROM stocks s, dividends d
WHERE d.`symbol` = 'IBM' AND s.`symbol` = 'IBM' 
AND d.`ymd` >= '2000-01-01' AND d.`ymd` = s.`ymd`;
# 共 41 条记录
```

7）查询 AAPL 在 2008 年 10 月每个交易日的涨跌情况，涨显示 rise，跌显示 fall，不变显示 unchange

```sql
# 这个问题对“涨跌”的定义...算了
SELECT `ymd`, 
    CASE 
        WHEN price_close - price_open > 0 THEN 'rise'
        WHEN price_close - price_open < 0 THEN 'fall'
        ELSE 'unchange'
    END AS `rise_or_fall`
FROM stocks
WHERE `symbol` = 'AAPL' AND `ymd` >= '2008-10-01' AND `ymd` < '2008-11-01';
```

8）查询 stocks 表中收盘价比开盘价高得最多的那条记录的交易所、股票代码、日期、收盘价、开盘价及二者差价

```sql
SELECT s.`exchange`, s.`symbol`, s.`ymd`, s.`price_close`, 
    s.`price_open`, s.`price_close` - s.`price_open`
FROM stocks s, (
    SELECT MAX(dif) max_dif
    FROM (
        SELECT `price_close` - `price_open` dif FROM stocks
    ) t1
) t2
WHERE s.`price_close` - s.`price_open` = t2.max_dif;
```

9）查询 AAPL 的年平均调整后收盘价大于 50 的年份及当年的平均调整后收盘价

```sql
SELECT YEAR(`ymd`) yr, AVG(price_adj_close)
FROM stocks
WHERE `symbol` = 'AAPL'
GROUP BY YEAR(`ymd`)
HAVING AVG(price_adj_close) > 50
ORDER BY yr DESC;
```

10）查询每年年平均调整后收盘价前三名的公司的股票代码及年平均调整后收盘价

```sql
SELECT yr y, symbol s, avg_pac p
FROM (
    SELECT *, ROW_NUMBER() OVER (PARTITION BY yr ORDER BY avg_pac DESC) rank
    FROM (
        SELECT YEAR(`ymd`) yr, symbol, AVG(price_adj_close) avg_pac
        FROM stocks
        GROUP BY YEAR(`ymd`), symbol
    ) t1
) t2
WHERE rank <= 3;
```

Hive 特有的分组（分区）排序方法：

```sql
ROW_NUMBER() OVER (PARTITION BY 分区项A ORDER BY 排序项B [DESC]) rank
# 以 rank 的值作为排序依据
```



