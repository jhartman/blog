package josh.shared.util

import com.google.gwt.core.client.GWT

trait Logging {
  private def buildLog: Logger = {
    val name = getClass.getCanonicalName
    if (GWT.isClient) new ClientLogger(name)
    else new ServerLogger(name)
  }
}