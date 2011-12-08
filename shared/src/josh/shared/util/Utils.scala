package josh.shared.util

import com.google.gwt.core.client.{Scheduler, RunAsyncCallback, GWT}
import com.google.gwt.core.client.Scheduler.{ScheduledCommand, RepeatingCommand}
import com.google.gwt.user.client.Window

object Utils extends Logging {
  def invert[A, B](map: Map[A, B]): Map[B, Iterable[A]] = {
    map.foldLeft(Map.empty[B, Seq[A]]) { case (accum, (key, value)) =>
      val newSeq = key +: accum.getOrElse(value, Seq.empty[A])
      accum + (value -> (newSeq))
    }
  }

//  def runAsync(fn: => Any) { GWT.runAsync(new RunAsyncCallback {
//      def onFailure(throwable: Throwable) {
//        log.warn(throwable.asInstanceOf[Exception], "Error running async")
//      }
//
//      def onSuccess() {
//        Scheduler.get().scheduleDeferred(new ScheduledCommand {
//          def execute() {
//            fn
//          }
//        })
//      }
//    })
//  }

  def repeat(n: Int)(fn: => Unit) {
    var i = 0
    Scheduler.get().scheduleFixedDelay(new RepeatingCommand {
      def execute() = {
        fn
        i += 1
        i < n
      }
    }, 1000)
  }

  def defer(fn: => Unit) {
    Scheduler.get().scheduleFixedDelay(new RepeatingCommand {
      def execute() = {
        fn
        false
      }
    }, 1000)
  }

  private val memo = collection.mutable.Map.empty[Any, Any]

  def memoize[T, V](fn: T => V)(data: T): V = {
    memo.get(data) match {
      case Some(v) => v.asInstanceOf[V]
      case None =>
        memo.put(data, fn)
        memoize(fn)(data)
    }
  }

//    Scheduler.get().scheduleDeferred(new ScheduledCommand {
//      def execute() {
//        fn
//      }
//    })

//  def runAsync[T](fn: => T): T = {
//    var res: Option[T] = None
//    var th: Exception = new IllegalStateException
//    GWT.runAsync(new RunAsyncCallback {
//      def onFailure(throwable: Throwable) {
//        th = new Exception(throwable)
//        log.warn(th, "Error running async")
//      }
//
//      def onSuccess() {
//        res = Some(fn)
//      }
//    })
//
//    if (res.isDefined) res.get
//    else throw th
//  }

}

//class SuperFunction[T](fn: => T) {
//  def | [V](fn2: T => V): Function[V] = {
//    Utils.runAsync(fn)
//    fn2(fn)
//  }
//}
