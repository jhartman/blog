package josh
package client
package widgets

import shared.util.Utils._
import com.google.gwt.user.client.ui.Composite._
import com.google.gwt.user.client.ui._
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant
import com.google.gwt.user.client.Window

object LoadingWidget {
//  def apply(fn: => Widget): Widget = {
//    val gif = new Image(BlogResources.resources.loading())
//    val panel = new VerticalPanel
//    panel.add(gif)
//    defer {
//      val trueWidget = fn
//      panel.remove(gif)
//      panel.add(trueWidget)
//    }
//    new LoadingWidget(panel)
//  }

  def apply(fn: => Widget, width: Int, height: Int): Widget = {
    val gif = new Image(BlogResources.resources.loading())
//    val panel = new VerticalPanel
//
//    panel.add(gif)
    defer {
      Window.alert("About to run fn")
      val trueWidget = fn
      Window.alert("Finished fn")
      val parent = gif.getParent
      gif.removeFromParent()
      if(parent.isInstanceOf[Panel]) {
        (parent.asInstanceOf[Panel]).add(trueWidget)
      }

//      panel.remove(gif)
//      panel.add(trueWidget)
    }
    gif
  }
}