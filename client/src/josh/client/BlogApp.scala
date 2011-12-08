package josh
package client

import com.google.gwt.core.client.EntryPoint
import com.google.gwt.core.client.GWT
import Handlers._
import shared.util.Utils._
import com.google.gwt.event.dom.client.{ClickEvent, LoadEvent}
import widgets.LoadingWidget
import scala.util.Random
import shared._
import com.google.gwt.user.client.Window
import com.google.gwt.user.client.ui.{HorizontalPanel, VerticalPanel, Panel, Label, Image => GwtImage}

class BlogApp {
  val resources: BlogResources = GWT.create(classOf[BlogResources])

  def interact(panel: Panel) {
    val img = new GwtImage(resources.lena256().getURL);
    val imagePanel = new HorizontalPanel
    imagePanel.add(img)

    val controls = new KMeansView(KMeansModel(0.15, 8))

    val outerPanel = new VerticalPanel
    outerPanel.add(imagePanel)
    outerPanel.add(controls)

    panel.add(outerPanel)
    EventQueue.register((event: StartKMeansEvent) => {
      runApp(imagePanel, img, event.model)
    })
  }

  def runApp(p: Panel, img: GwtImage, model: KMeansModel) {
    val lena = LoadImageClient(p, img)
    runKMeans(lena, p, model)
  }

  var newView: MyImageView = null

  def runKMeans(img: Image, p: Panel, model: KMeansModel) {
    val imgData = img.toArray
    val startingVectors = KMeans2.createStartingVectors(imgData, model.k, new Random)

    val initial = KMeans2.cluster(imgData, startingVectors, ImageKMeans.buildDistanceFn(img.width, img.height, model.cdWeight))
    var clustering = (startingVectors, initial)

    def draw() {
      var newImg = new Array[MyPixel](imgData.length)
      var i = 0
      while(i < imgData.length) {
        val pixel = imgData(i)
        val color = clustering._1(clustering._2(i)).color
        newImg(i) = new MyPixel(pixel.row, pixel.col, color)
        i += 1
      }
      if(newView != null) p.remove(newView)
      newView = new MyImageView(img.width, img.height, newImg)
      p.add(newView)
    }

    draw()

    def runProcess(iter: Int) {
      if(iter != 0) {
        defer {
          clustering = ImageKMeans(imgData, clustering, model.k, model.cdWeight, img.width, img.height)
          runProcess(iter - 1)
        }
        draw()
      } else {
        draw()
      }
    }

    runProcess(9)
  }
}