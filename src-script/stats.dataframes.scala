import org.apache.spark.sql._

val df = sqlContext.read
  .format("com.databricks.spark.csv")
  .option("header", "false")
  .option("inferSchema", "true")
  .option("delimiter", " ")
  .load("data/data.txt")
  .cache


//this will print out count, mean, stddev, min, max
println("Here are some statistics about the data using dataframe.describe")
df.describe().show

//an alternate way to get the means
println("The mean of each column follows:")
df.agg(mean("c0"), mean("c1"), mean("c2"), mean("c3")).show

//an alternate way to get the standard deviations
println("The Standard Deviation of each column follows:")
df.agg(stddev_samp("c0"), stddev_samp("c1"), stddev_samp("c2"), stddev_samp("c3")).show


//function to create matrix
def corrMatrix(data:DataFrame) : Array[Array[String]] = {
  def getCorr(col1: String, col2: String) :String = {
    if (col1 == col2) "1.0"
    else data.agg(corr(col1, col2)).take(1)(0)(0).toString
  }
  val colNames = data.columns
  val colNameMatrix = colNames.map(x => colNames.map(y => List(y, x).sortBy(z => z)))
  val setOfNames = colNameMatrix.flatten.toSet

  val mapOfCorrelations = setOfNames.map(x => (x.mkString(""), getCorr(x(0), x(1)))).toMap

  colNameMatrix.map(x => x.map(y => mapOfCorrelations.getOrElse(y.mkString(""),"0")))
}

//correlation matrix:
println("The correlation matrix of the data follows:")
corrMatrix(df).foreach(z => println(z.toList))
