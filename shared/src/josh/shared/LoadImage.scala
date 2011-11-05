//package josh.shared
//
//import javax.imageio.ImageIO
//import java.io.File
//
//object LoadImage {
//  def apply(name: String): Image = {
//    val stream = getClass.getClassLoader.getResourceAsStream(name)
//    val bufferedImage = ImageIO.read(stream)
//    val raster = bufferedImage.getData
//    val width = raster.getWidth
//    val height = raster.getHeight
//
//    val pixels = new Array[Array[Color]](height)
//    (0 until height).foreach { row =>
//      pixels(row) = new Array[Color](width)
//      (0 until width).foreach { col =>
//        val rgb = bufferedImage.getRGB(col, row)
//
//        val red = (rgb >> 16) & 0xFF
//        val green = (rgb >> 8) & 0xFF
//        val blue = (rgb) & 0xFF
//
//        pixels(row)(col) = new Color(red, green, blue)
//      }
//    }
//    Image(pixels)
//  }
//}