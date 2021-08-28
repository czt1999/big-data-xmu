import org.apache.spark.{SparkConf, SparkContext}

object TaskMerge {
  def main(args: Array[String]) {
    val inputPaths = Array(
      "hdfs://192.168.3.9:9000/user/hadoop/input/a.txt",
      "hdfs://192.168.3.9:9000/user/hadoop/input/b.txt"
    )
    val outputPath = "hdfs://192.168.3.9:9000/user/hadoop/output/merge/"
    val conf = new SparkConf().setAppName("Spark-Test-Merge").setMaster("local[2]")
    val sc = new SparkContext(conf)
    inputPaths.map(path => sc.textFile(path))
      .reduce((r1, r2) => r1 ++ r2)
      .map(Common.toKV)
      .groupByKey()
      .mapValues(v => v.toSet[String])
      .sortByKey()
      .flatMap(Common.toLine)
      .repartition(1)
      .saveAsTextFile(outputPath)
  }
}