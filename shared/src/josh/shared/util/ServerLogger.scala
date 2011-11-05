package josh.shared.util

class ServerLogger(name: String) extends Logger {
  def info(str: => String) = System.out.println(str)

  def warn(str: => String) = System.err.println(str)
}