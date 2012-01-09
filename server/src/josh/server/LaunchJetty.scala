package josh.server

import org.eclipse.jetty.webapp.WebAppContext
import org.eclipse.jetty.server.{NCSARequestLog, Server}
import org.eclipse.jetty.server.handler.{RequestLogHandler, ContextHandlerCollection, HandlerCollection}
import java.util.logging.{SimpleFormatter, Level, Logger, FileHandler}

object LaunchJetty {
  def main(args: Array[String]): Unit = {

    val fh = new FileHandler("../logs/debug.log", true)
    fh setFormatter(new SimpleFormatter)

    val rootLogger = Logger getLogger ""
    rootLogger addHandler fh
    rootLogger setLevel Level.FINEST

    val handlers = new HandlerCollection();

    val requestLog = new NCSARequestLog("../logs/jetty-yyyy_mm_dd.request.log")
    requestLog setRetainDays(90)
    requestLog setAppend(true)
    requestLog setExtended(false)
    requestLog setLogTimeZone("GMT")
    val requestLogHandler = new RequestLogHandler
    requestLogHandler setRequestLog requestLog

    val context = new WebAppContext
    context.setDescriptor("war/WEB-INF/web.xml")
    context.setResourceBase("war")
    context.setContextPath("/")
    context.setParentLoaderPriority(true)

    handlers.setHandlers(Array(context, requestLogHandler))


    val server = new Server(80)
    server.setHandler(handlers);
    server.start
    server.join
  }
}
