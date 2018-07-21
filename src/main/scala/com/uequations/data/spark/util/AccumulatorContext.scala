package com.uequations.data.spark.util

import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import java.{lang => jl}

import org.apache.spark.internal.Logging
import org.apache.spark.util.AccumulatorV2

object AccumulatorContext extends Logging {

  private val originals = new ConcurrentHashMap[Long, jl.ref.WeakReference[AccumulatorV2[_, _]]]

  private[this] val nextId = new AtomicLong(0L)

  def newId(): Long = nextId.getAndIncrement()

  def numAccums: Int = originals.size()

  def register(a: AccumulatorV2[_, _]): Unit = {
    originals.putIfAbsent(a.id, new WeakReference[AccumulatorV2[_, _]](a))
  }

  def remove(id: Long): Unit = {
    originals.remove(id)
  }

  def get(id: Long): Option[AccumulatorV2[_, _]] = {
    val ref = originals.get(id)

    if (ref eq null) {
      None
    } else {
      val acc = ref.get()
      if (acc eq null) {
        logWarning(s"Attempted to access garbage collected accumulator $id")
      }
      Option(acc)
    }
  }

  def clear(): Unit = {
    originals.clear()
  }

  private[spark] val SQL_ACCUM_IDENTIFIER = "sql"
}
