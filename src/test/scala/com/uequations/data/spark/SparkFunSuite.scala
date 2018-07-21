package com.uequations.data.spark

import java.io.File

import com.uequations.data.spark.util.AccumulatorContext
import org.apache.spark.internal.Logging
import org.scalatest.{BeforeAndAfterAll, FunSuite, Outcome}

class UEQTestSuite extends SparkFunSuite {

}

abstract class SparkFunSuite extends FunSuite with BeforeAndAfterAll with Logging with ThreadAudit {

  protected val enableAutoThreadAudit = true

  protected override def beforeAll(): Unit = {
    System.setProperty("spark.testing", "true")
    if (enableAutoThreadAudit) {
      doThreadPostAudit()
    }
    super.beforeAll()
  }

  protected override def afterAll(): Unit = {
    try {
      AccumulatorContext.clear()
    } finally {
      super.afterAll()
      if (enableAutoThreadAudit) {
        doThreadPostAudit()
      }
    }
  }

  protected final def getTestResourceFile(file: String): File = {
    new File(getClass.getClassLoader.getResource(file).getFile)
  }

  protected final def getTestResourcePath(file: String): String = {
    getTestResourceFile(file).getCanonicalPath
  }
  
  final protected override def withFixture(test: NoArgTest): Outcome = {
    val testName = test.text
    val suiteName = this.getClass.getName
    val shortSuiteName = suiteName.replaceAll("org.apache.spark", "o.a.s")
    try {
      logInfo(s"\n\n===== TEST OUTPUT FOR $shortSuiteName: '$testName' =====\n")
      test()
    } finally {
      logInfo(s"\n\n===== FINISHED $shortSuiteName: '$testName' =====\n")
    }
  }
}
