package josh.client

import josh.shared.Image
import com.google.gwt.user.client.ui.Composite
import com.google.gwt.canvas.client.Canvas

class MyImageView(image: Image) extends Composite {
  val canvas = Canvas.createIfSupported()
  val height = image.pixels.size
  val width = image.pixels(0).size
  canvas.setCoordinateSpaceHeight(height)
  canvas.setCoordinateSpaceWidth(width)
  val context = canvas.getContext2d
  val imageData = context.getImageData(0, 0, width, height)
  image.foreach { pixel =>
    imageData.setRedAt(pixel.color.R, pixel.col, pixel.row)
    imageData.setGreenAt(pixel.color.G, pixel.col, pixel.row)
    imageData.setBlueAt(pixel.color.B, pixel.col, pixel.row)
    imageData.setAlphaAt(0xFF, pixel.col, pixel.row)
  }
  context.putImageData(imageData, 0, 0)
  initWidget(canvas)
}