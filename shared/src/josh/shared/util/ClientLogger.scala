package josh.shared.util

import com.google.gwt.core.client.GWT

class ClientLogger(name: String) extends Logger {
  def info(str: => String) =
    GWT.log(str)

  def warn(str: => String) =
    GWT.log(str)
}