package josh.shared.util

import com.google.gwt.core.client.GWT

trait Logging {
  val log = buildLog

  private def buildLog: Logger = {
    val name = getClass.getName
    new ClientLogger(name)
//    else new ServerLogger(name)
  }
}