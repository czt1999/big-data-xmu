import org.apache.spark.{SparkConf, SparkContext}

object TaskAverage {
  def main(args: Array[String]) {
    val inputPaths = Array(
      "hdfs://192.168.3.9:9000/user/hadoop/input/score-1.txt",
      "hdfs://192.168.3.9:9000/user/hadoop/input/score-2.txt",
      "hdfs://192.168.3.9:9000/user/hadoop/input/score-3.txt"
    )
    val outputPath = "hdfs://192.168.3.9:9000/user/hadoop/output/average"
    val conf = new SparkConf().setAppName("Spark-Test-Average").setMaster("local[2]")
    val sc = new SparkContext(conf)
    inputPaths.map(path => sc.textFile(path))
      .reduce((r1, r2) => r1 ++ r2)
      .map(Common.toKV)
      .groupByKey()
      .mapValues(Common.avg)
      .sortByKey()
      .repartition(1)
      .saveAsTextFile(outputPath)
  }
}
