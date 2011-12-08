package josh
package client

import com.google.gwt.canvas.client.Canvas
import com.google.gwt.dom.client.ImageElement
import shared.{Color, Image}
import shared.util.Logging
import com.google.gwt.user.client.ui.{Panel, Image => GwtImage}
import com.google.gwt.core.client.GWT

object LoadImageClient extends Logging with Function2[Panel, GwtImage, Image] {
  def apply(parent: Panel, gwtImage: GwtImage): Image = {
    val height = gwtImage.getHeight
    val width = gwtImage.getWidth
    val canvas = Canvas.createIfSupported()
    canvas.setCoordinateSpaceHeight(512)
    canvas.setCoordinateSpaceWidth(512)
//    canvas.setVisible(false)
    val context = canvas.getContext2d
    val imageElement = ImageElement.as(gwtImage.getElement)

    context.drawImage(imageElement, 0.0, 0.0, width, height)

    val imageData = canvas.getContext2d.getImageData(0, 0, width - 1, height - 1) // width - 1 & height - 1

    val buffer = new Array[Array[Color]](imageData.getHeight)

    log.info("width = " + imageData.getWidth + " height = " + imageData.getHeight)

    (0 until imageData.getHeight).foreach { row =>
      if(row % 10 == 0) log.info("loaded row " + row)
      buffer(row) = new Array[Color](imageData.getWidth)
      (0 until imageData.getWidth).foreach { col =>
        val red = imageData.getRedAt(col, row)
        val green = imageData.getGreenAt(col, row)
        val blue = imageData.getBlueAt(col, row)

        buffer(row)(col) = Color(red, green, blue)
      }
    }
    Image(buffer)
  }
}