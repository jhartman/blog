package josh.client

import com.google.gwt.core.client.EntryPoint
import com.google.gwt.core.client.GWT
import com.google.gwt.user.client.ui.{Image, Panel, Label}
import com.google.gwt.event.dom.client.LoadEvent
import Handlers._

class BlogApp {
  val resources: BlogResources = GWT.create(classOf[BlogResources])

  def interact(panel: Panel) {

    val img = new Image(resources.lena().getURL);
    img.addLoadHandler( (e: LoadEvent) => {
      val lena = LoadImageClient(panel, img)
      panel.add(new Label("Lena is " + lena.pixels.size + " pixels high and " + lena.pixels(0).size + " pixels wide"))
      panel.add(new MyImageView(lena))
      panel.add(new Label("Done"))
    })
    panel.add(img)
  }
}