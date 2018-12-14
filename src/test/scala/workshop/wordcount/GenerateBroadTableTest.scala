package workshop.wordcount

import java.nio.file.{Files, Paths, StandardOpenOption}

import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter._
import workshop.DefaultFeatureSpecWithSpark

import scala.io.Source


class GenerateBroadTableTest extends DefaultFeatureSpecWithSpark {
  feature("Word Count application") {
    scenario("Acceptance test for basic use") {
      Given("A simple input file, a Spark context, and a known output file")

      val rootDirectory = Files.createTempDirectory(this.getClass.getName)


      import scala.collection.JavaConverters._

      When("I trigger the application")

      val outputFilePath = "/Users/zxwang/dataworkshop/data/output2"
      GenerateBroadTable.run(spark,
        "/Users/zxwang/dataworkshop/data/orders.csv",
        "/Users/zxwang/dataworkshop/data/products.csv",
        outputFilePath)

      Then("It outputs files containing the expected data")

      //      val files = FileUtils
      //        .listFiles(Files.readoutputFilePath,
      //          new AndFileFilter(EmptyFileFilter.NOT_EMPTY,
      //            new SuffixFileFilter(".csv")),
      //          TrueFileFilter.TRUE)
      //        .asScala
      //
      //      val allLines = files
      //        .foldRight(Set[String]())((file, lineSet) =>
      //          lineSet ++ FileUtils.readLines(file).asScala)
      //        .map(_.trim)
      //      val expectedLines = Set("worst,1",
      //        "times,2",
      //        "was,4",
      //        "age,2",
      //        "it,4",
      //        "foolishness,1",
      //        "of,4",
      //        "wisdom,1",
      //        "best,1",
      //        "the,4")

//      val allLines = Files.readAllLines(Paths.get(outputFilePath))
      //      allLines should contain theSameElementsAs expectedLines
    }
  }
}