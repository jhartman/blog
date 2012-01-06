package josh.client

import com.google.gwt.user.client.ui.Composite
import com.google.gwt.canvas.client.Canvas
import josh.shared.{MyPixel, Image}
import josh.shared.util.Logging

class MyImageView(width: Int, height: Int, pixels: Iterable[MyPixel]) extends Composite with Logging {
  def this(image: Image) = this(image.width, image.height, image)

  val canvas = load(width, height, pixels)
  initWidget(canvas)

  private def load(width: Int, height: Int, pixels: Iterable[MyPixel]): Canvas = {
    val canvas = Canvas.createIfSupported()
    canvas.setStyleName("kmeansImage")
    canvas.setCoordinateSpaceHeight(height)
    canvas.setCoordinateSpaceWidth(width)
    val context = canvas.getContext2d
    val imageData = context.getImageData(0, 0, width, height)

    log.info("About to set pixels.")
    pixels.foreach { pixel =>
      imageData.setRedAt(pixel.color.R, pixel.col, pixel.row)
      imageData.setGreenAt(pixel.color.G, pixel.col, pixel.row)
      imageData.setBlueAt(pixel.color.B, pixel.col, pixel.row)
      imageData.setAlphaAt(0xFF, pixel.col, pixel.row)
    }
    log.info("Finished setting pixels.")
    context.putImageData(imageData, 0, 0)
    canvas
  }
}