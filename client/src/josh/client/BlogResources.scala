package josh.client

import com.google.gwt.core.client.GWT
import com.google.gwt.resources.client.{ImageResource, ClientBundle}
import com.google.gwt.resources.client.ClientBundle.Source

trait BlogResources extends ClientBundle {
  @Source(Array("lena.png"))
  def lena(): ImageResource

  @Source(Array("lena256.png"))
  def lena256(): ImageResource

  @Source(Array("loading2.gif"))
  def loading(): ImageResource
}

object BlogResources {
  val resources: BlogResources = GWT.create(classOf[BlogResources])
}