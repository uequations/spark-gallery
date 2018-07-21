package com.uequations.data.spark

import org.apache.spark.internal.Logging

import scala.collection.JavaConverters._

trait ThreadAudit extends Logging {

  val threadWhiteList = Set(
    "netty.*",
    "globalEventExecutor.*",
    "threadDeathWatcher.*",
    "rpc-client.*",
    "rpc-server.*",
    "shuffle-client.*",
    "shuffle-server.*"
  )

  private var threadNamesSnapshot: Set[String] = Set.empty

  protected def doThreadPreAudit(): Unit = {
    threadNamesSnapshot = runningThreadNames()
  }

  protected def doThreadPostAudit(): Unit = {
    val shortSuiteName = this.getClass.getName.replaceAll("org.apache.spark", "o.a.s")

    if (threadNamesSnapshot.nonEmpty) {
      val remainingThreadNames = runningThreadNames().diff(threadNamesSnapshot)
        .filterNot { s => threadWhiteList.exists(s.matches(_)) }
      if (remainingThreadNames.nonEmpty) {
        logWarning(s"\n\n===== POSSIBLE THREAD LEAK IN SUITE $shortSuiteName, " +
          s"thread names: ${remainingThreadNames.mkString(", )")} ====\n")
      }
    } else {
      logWarning("\n\n==== THREAD AUDIT POST ACTION CALLED " +
        s"WITHOUT PRE ACTION IN SUITE $shortSuiteName ====\n")
    }
  }

  private def runningThreadNames(): Set[String] = {
    Thread.getAllStackTraces.keySet().asScala.map(_.getName).toSet
  }
}
