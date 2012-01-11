package josh.server

import org.eclipse.jetty.webapp.WebAppContext
import org.eclipse.jetty.server.handler.{RequestLogHandler, ContextHandlerCollection, HandlerCollection}
import java.util.logging.{SimpleFormatter, Level, Logger, FileHandler}
import org.eclipse.jetty.servlet.FilterHolder
import org.eclipse.jetty.server.{DispatcherType, NCSARequestLog, Server}
import java.util.EnumSet
import org.eclipse.jetty.servlets.GzipFilter

object LaunchJetty {
  def main(args: Array[String]): Unit = {

    val fh = new FileHandler("/tmp/logs/debug.log", true)
    fh setFormatter(new SimpleFormatter)

    val rootLogger = Logger getLogger ""
    rootLogger addHandler fh
    rootLogger setLevel Level.FINEST

    val handlers = new HandlerCollection();

    val requestLog = new NCSARequestLog("/tmp/logs/jetty-yyyy_mm_dd.request.log")
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
    
    val all = EnumSet.of(DispatcherType.ASYNC, DispatcherType.ERROR, DispatcherType.FORWARD,
      DispatcherType.INCLUDE, DispatcherType.REQUEST)

    context.addFilter(new FilterHolder(new GwtRequestFilter), "/*", all)

    val gzipFilter = new FilterHolder(new GzipFilter())
    gzipFilter.setInitParameter("mimeTypes", "text/javascript,text/html,text/css,application/javascript,undefined");
    gzipFilter.setInitParameter("minGzipSize", "0");

    context.addFilter(gzipFilter, "/*", all)

    handlers.setHandlers(Array(context, requestLogHandler))

    val server = new Server(80)
    server.setHandler(handlers);
    server.start
    server.join
  }
}
