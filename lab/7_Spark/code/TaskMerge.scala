import org.apache.spark.{SparkConf, SparkContext}

object TaskMerge {
  def main(args: Array[String]) {
    val pathA = "hdfs://192.168.3.9:9000/user/hadoop/input/a.txt"
    val pathB = "hdfs://192.168.3.9:9000/user/hadoop/input/b.txt"
    val pathC = "hdfs://192.168.3.9:9000/user/hadoop/output/merge/"
    val conf = new SparkConf().setAppName("Spark-Test-Merge").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val lines = sc.textFile(pathA) ++ sc.textFile(pathB)
    lines.map(toKV)
      .groupByKey()
      .mapValues(v => v.toSet[String])
      .flatMap(toLine)
      .sortBy(s => s)
      .repartition(1)
      .saveAsTextFile(pathC)
  }

  def toKV(line: String): (String, String) = {
    val blankIndex = line.indexOf(" ")
    val k = line.substring(0, blankIndex)
    val v = line.substring(blankIndex + 1).trim()
    (k, v)
  }

  def toLine(e: (String, Iterable[String])): List[String] = {
    e._2.map(v => "%s %s".format(e._1, v)).toList
  }
}
