import org.apache.spark.{SparkConf, SparkContext}

object TaskCount {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Spark-Test-Count")
    val sc = new SparkContext(conf)
    for (path <- args) {
      val lines = sc.textFile(path).cache()
      println("Lines of \"%s\": %s".format(path, lines.count()))
    }
  }
}