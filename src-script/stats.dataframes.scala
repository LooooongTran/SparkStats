import org.apache.spark.sql._

case class statobject(one: Double, two: Double, three:Double, four:Double)

def toDouble(s: String, default: Double = -1): Double = try {
  val s2 = s.trim
  if (s2.length == 0 || s2 == "NA") default
  else s2.toDouble
} catch {
  case nfe: NumberFormatException =>
    Console.err.println(s"""NFE: "$s".toDouble""")
    default
}


def statobjectParse(in:Array[String]):statobject = {
  statobject(toDouble(in(0)), toDouble(in(1)), toDouble(in(2)), toDouble(in(3)))}
//try this:  https://github.com/databricks/spark-csv
val rawdata = sc.textFile("data/data.txt").
  map(x => x.split(" ")).
  map(x => statobjectParse(x)).
  toDF.
  cache

//try some of these: https://databricks.com/blog/2015/06/02/statistical-and-mathematical-functions-with-dataframes-in-spark.html
//df.describe().show()  will show mean and stddev
//df.stat.corr('id', 'id') instead of df.agg(corr(col1, col2))

println("the average of each column is as follows:")
//todo - get all columns and pass 'em
rawdata.agg(mean("one"), mean("two"), mean("three"), mean("four")).show

println("The standard deviation for each column is as follows:")
//todo - get all columns and pass 'em
rawdata.agg(stddev_samp("one"), stddev_samp("two"), stddev_samp("three"), stddev_samp("four")).show


def corrMatrix(data:DataFrame) : Array[Array[Any]] = {
  def getCorr(col1: String, col2: String) :String = {
    rawdata.agg(corr(col1, col2)).take(1)(0)(0).toString
  }
  val colNames = data.columns
  val colNameMatrix = colNames.map(x => colNames.map(y => List(y, x).sortBy(z => z)))
  val setOfNames = colNameMatrix.
    flatten.
    toSet
  val mapOfCorrelations = setOfNames.map(x => (x.mkString(""), getCorr(x(0), x(1)))).
    toMap

  colNameMatrix.map(x => x.map(y => abc.getOrElse(y.mkString(""),0)))
}

//correlation matrix:
corrMatrix(rawdata).foreach(z => println(z.toList))