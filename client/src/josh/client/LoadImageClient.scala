package josh.client

import com.google.gwt.canvas.client.Canvas
import com.google.gwt.dom.client.ImageElement
import josh.shared.{Color, Image}
import com.google.gwt.user.client.ui.{Panel, Image => GwtImage}
import com.google.gwt.core.client.GWT

object LoadImageClient {
  def apply(parent: Panel, gwtImage: GwtImage): Image = {
    val height = gwtImage.getHeight
    val width = gwtImage.getWidth
    val canvas = Canvas.createIfSupported()
    canvas.setCoordinateSpaceHeight(512)
    canvas.setCoordinateSpaceWidth(512)
    val context = canvas.getContext2d
    val imageElement = ImageElement.as(gwtImage.getElement)

    context.beginPath
    context.moveTo(100,50)
    context.lineTo(100,100)
    context.arc(100, 100, 50, 0, math.Pi / 2, false)
    context.stroke

    context.drawImage(imageElement, 0.0, 0.0, width, height)

    val imageData = canvas.getContext2d.getImageData(0, 0, width - 1, height - 1) // width - 1 & height - 1

    val buffer = new Array[Array[Color]](imageData.getHeight)

    GWT.log("width = " + imageData.getWidth + " height = " + imageData.getHeight)

    (0 until imageData.getHeight).foreach { row =>
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