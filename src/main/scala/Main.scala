

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.linalg._
import org.apache.spark.mllib.stat.{MultivariateStatisticalSummary, Statistics}

import scala.math._
import org.apache.spark.rdd._



object Main {

  def main(args: Array[String]): Unit = {

    val sc = new SparkContext("local[4]", "ML.Stats Test", new SparkConf())
    //turn down the amount of output in the console so we can see what's happening
    val rootLogger = Logger.getRootLogger()
    rootLogger.setLevel(Level.ERROR)

    val observations = sc.textFile("data/data.txt").
      map(x => x.split(" ")).
      map(x => x.map(y => y.toDouble)).
      map(x => Vectors.dense(x)).
      cache

    val summary: MultivariateStatisticalSummary = Statistics.colStats(observations)

    println("The mean of each column follows:")
    println(summary.mean)

    println("The Standard Deviation of each column follows:")
    println(summary.variance.toArray.map(x => scala.math.sqrt(x)).toList)
    println("")

    val corrMatrix = Statistics.corr(observations)

    println("The correlation matrix of the data follows:")
    println(corrMatrix)
    println("")

    val transposedObservations = observations.map(x => x.toArray).
      flatMap(x => x.zipWithIndex).
      map(x => x.swap).
      groupByKey.
      map(x => (x._1, Vectors.dense(x._2.toArray))).
      cache

    observations.unpersist()
    val arrayOfResults = transposedObservations.collect.map(x => (x._1, sc.makeRDD(x._2.toArray))).
      map(x => (x._1, new DoubleRDDFunctions(x._2))).
      map(x => (x._1, x._2.histogram(20))).
      map(x => (x._1, x._2._2.map(y => y.toDouble))).
      map(x => (x._1, Vectors.dense(x._2))).
      map(x => (x._1, Statistics.chiSqTest(x._2)))

    def printChiResults(ColumnAndResult: (Int, org.apache.spark.mllib.stat.test.ChiSqTestResult)): Unit = {
      println(s"column: ${ColumnAndResult._1}")
      val testResult = ColumnAndResult._2
      println(s"Chi^2/ndof=${testResult.statistic / testResult.degreesOfFreedom}")
      println(s"p-value=${testResult.pValue}")
    }

    println("Chi-Square results follow:")
    arrayOfResults.sortBy(_._1).foreach(printChiResults)

   }
}
