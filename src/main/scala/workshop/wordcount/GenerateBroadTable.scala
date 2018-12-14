package workshop.wordcount

import java.time.Clock

import com.typesafe.config.ConfigFactory
import org.apache.log4j.{Level, LogManager, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions.col

object GenerateBroadTable {
  val log: Logger = LogManager.getRootLogger
  implicit val clock: Clock = Clock.systemDefaultZone()

  def main(args: Array[String]): Unit = {
    val conf = ConfigFactory.load
    log.setLevel(Level.INFO)
    val spark = SparkSession.builder.appName("Generate broad table").getOrCreate()
    log.info("Application Initialized: " + spark.sparkContext.appName)

    val orderPath = if (!args.isEmpty) args(0) else conf.getString("apps.WordCount.orderPath")
    val productPath = if (!args.isEmpty) args(1) else conf.getString("apps.WordCount.productPath")
    val outputPath = if (args.length > 1) args(2) else conf.getString("apps.WordCount.output")

    run(spark, orderPath, productPath, outputPath)

    spark.stop()
  }

  def run(spark: SparkSession, orderInputPath: String, productInputPath: String, outputPath: String): Unit = {
    log.info("Reading data: " + orderInputPath)
    log.info("Writing data: " + outputPath)

    val orderCustomSchema = StructType(Array(
      StructField("MONTH", IntegerType, nullable = true),
      StructField("ORDER_ID", StringType, nullable = true),
      StructField("SKU_ID", StringType, nullable = true),
      StructField("SALES", IntegerType, nullable = true)))


    val products = spark.read.option("header", value = true)
      .option("inferSchema", value = true)
      .csv(productInputPath) // Read file

    val orders = spark.read.option("header", "false")
      .schema(orderCustomSchema)
      .csv(orderInputPath) // Read file

    //    val ordersDf = orders.as("orders")
    //
    //    val productsDf = products.as("products")

    val joinedAll = orders.join(products, "SKU_ID")
    joinedAll
      .groupBy("NAME", "MONTH", "SIZE")
      .sum("SALES")
      .show()
    //      .write
    //      .option("quoteAll", value = false)
    //      .option("quote", " ")
    //      .option("header", value = true)
    //      .csv(outputPath)

    log.info("Application Done: " + spark.sparkContext.appName)
  }
}
