package josh.server

import javax.servlet._
import http.{HttpServletResponse, HttpServletRequest}
import java.util.logging.Logger


class GwtRequestFilter extends Filter {
  val logger = Logger getLogger getClass.getName

  def init(filterConfig: FilterConfig) {}

  def doFilter(request: ServletRequest, response: ServletResponse, filterChain: FilterChain) {
    val httpRequest = request.asInstanceOf[HttpServletRequest]
    val httpResponse = response.asInstanceOf[HttpServletResponse]
    val uri = httpRequest.getRequestURI
    if (uri.contains("nocache")) {
      val now = System.currentTimeMillis()
      httpResponse.setDateHeader("Date", now)
      httpResponse.setDateHeader("Expires", now)
      httpResponse.setHeader("Pragma", "no-cache")
      httpResponse.setHeader("Cache-control", "no-cache,no-store,must-revalidate")
    } else {
      logger.info("Setting cache control for " + uri)
//      httpResponse.setHeader("Cache-control", "max-age=31536000")
      httpResponse.addHeader("Cache-control", "max-age=3600")
    }
    filterChain.doFilter(httpRequest, httpResponse)
  }

  def destroy() {}
}