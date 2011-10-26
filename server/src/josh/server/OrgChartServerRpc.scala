package josh
package server

import com.google.gwt.user.server.rpc.RemoteServiceServlet
import shared.{OrgChart, ScoreParams, OrgChartRpc}

class OrgChartServerRpc extends RemoteServiceServlet with OrgChartRpc {
  def getOrgChart(companyId: Int, params: ScoreParams) = {
    if(companyId == 1337) {
      val map = LoadOrgChart("/Users/jhartman/data/orgchartdata3")
      val orgChart = OrgChart(map)
      OptimizeOrgChart.optimize(orgChart, params, 10)
    } else throw new IllegalArgumentException
  }
}