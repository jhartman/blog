package josh.shared.util

import java.util.logging.{Logger => JLogger}
import java.util.logging.{Level => JLogLevel}

class ClientLogger(name: String) extends Logger {
  val inner = JLogger.getLogger(name)

  def info(str: => String) =
    inner.log(JLogLevel.INFO, str)

  def warn(str: => String) =
    inner.log(JLogLevel.WARNING, str)

  def warn(ex: Exception, str: => String) =
    inner.log(JLogLevel.WARNING, str, ex)
}