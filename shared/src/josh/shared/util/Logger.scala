package josh.shared.util

trait Logger {
  def info(str: => String)

  def warn(str: => String)

  def warn(ex: Exception, str: => String)
}