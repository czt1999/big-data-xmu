object Common {
  def toKV(line: String): (String, String) = {
    val blankIndex = line.indexOf(" ")
    val k = line.substring(0, blankIndex)
    val v = line.substring(blankIndex + 1).trim()
    (k, v)
  }

  def toLine(e: (String, Iterable[String])): List[String] = {
    e._2.map(v => "%s %s".format(e._1, v)).toList
  }

  def avg(itr: Iterable[String]): String = {
    "%.2f".format(itr.map(i => i.toDouble).sum / itr.size)
  }

}
