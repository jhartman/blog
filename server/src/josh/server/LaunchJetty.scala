package josh.server

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext

object LaunchJetty {
  def main(args: Array[String]): Unit = {
    val server = new Server(8080)
    val context = new WebAppContext
    context.setDescriptor("war/WEB-INF/web.xml")
    context.setResourceBase("war")
    context.setContextPath("/")
    context.setParentLoaderPriority(true)
    server.setHandler(context)
    server.start
    server.join
  }
}
