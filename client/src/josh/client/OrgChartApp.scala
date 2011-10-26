//package josh.client
//
//import com.google.gwt.user.client.Window
//import com.google.gwt.core.client.{GWT, EntryPoint}
//import com.google.gwt.user.client.ui._
//import com.google.gwt.event.dom.client.ClickEvent
//import josh.shared.{OrgChart, ScoreParams, OrgChartRpc, OrgChartRpcAsync}
//
//class OrgChartApp extends EntryPoint {
//  def onModuleLoad() {
//    val rpc: OrgChartRpcAsync = GWT.create(classOf[OrgChartRpc])
//    val parent = RootPanel.get
//
//    val inputs = List("seniority", "group size", "edge score", "invitation score").map { name =>
//      (new Label(name), new TextArea)
//    }
//
//    inputs.foreach { case (label, textArea) =>
//      val hp = new HorizontalPanel
//      hp.add(label)
//      hp.add(textArea)
//      parent.add(hp)
//    }
//
//    parent.add(new Button("submit", (ce: ClickEvent) => {
//      val params = inputs.map(_._2).map(_.getText.toDouble).toArray
//      val sParams = ScoreParams(params(0), params(1), params(2), params(3))
//      rpc.getOrgChart(1337, sParams, (orgChart: OrgChart) => {
//        GWT.log("Received org chart!!!!")
//        orgChart.groups.foreach(g => GWT.log(g.toString))
//      })
//    }))
//
//  }
//}