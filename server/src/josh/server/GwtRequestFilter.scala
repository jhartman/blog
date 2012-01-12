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

    val now = System.currentTimeMillis()
    if (uri.contains("nocache")) {
      httpResponse.setDateHeader("Date", now)
      httpResponse.setDateHeader("Expires", now)
      httpResponse.setHeader("Pragma", "no-cache")
      httpResponse.setHeader("Cache-control", "no-cache,no-store,must-revalidate")
    } else if(uri.contains("cache")) {
      httpResponse.setDateHeader("Expires", now + 30L * 24L * 60L * 60L * 1000L)
      httpResponse.addHeader("Cache-control", "max-age=108000")
    } else {
      httpResponse.setDateHeader("Expires", now + 60L * 60L * 1000L)
      httpResponse.addHeader("Cache-control", "max-age=3600")
    }
    filterChain.doFilter(httpRequest, httpResponse)
  }

  def destroy() {}
}