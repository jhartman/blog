package josh.client

import com.google.gwt.core.client.GWT
import com.google.gwt.resources.client.{ImageResource, ClientBundle}
import com.google.gwt.resources.client.ClientBundle.Source

trait BlogResources extends ClientBundle {
  @Source(Array("lena.png"))
  def lena(): ImageResource
}