package josh.shared

import util.Logging

object Color{
  def apply(colors: Seq[Int]): Color = {
    Color(colors(0), colors(1), colors(2))
  }
}
case class Color(R: Int, G: Int, B: Int) {
  def toSeq = Seq(R, G, B)
}

object MyPixel {
  def apply(row: Int, col: Int, color: Color) = new MyPixel(row, col, color)
}
class MyPixel(val row: Int, val col: Int, val color: Color) {
  override def hashCode() = {
    val hash0 = ((color.B & 0xFF) << 24) | ((color.G & 0xFF) << 16) | ((row & 0xFF) << 8) | (col & 8)
    hash0 ^ (((color.R & 0xFF) << 16) * 43)
  }
  override def equals(other: Any) = {
    if(other != null && other.isInstanceOf[MyPixel]) {
      val that = other.asInstanceOf[MyPixel]
      this.row == that.row && this.col == that.col && this.color.R == that.color.R && this.color.B == that.color.B && this.color.G == that.color.G
    } else false
  }
}

case class Image(pixels: Array[Array[Color]]) extends Iterable[MyPixel] {
  def width: Int = pixels.head.size
  def height: Int = pixels.size

  def iterator: Iterator[MyPixel] = new Iterator[MyPixel] {
    var row = 0
    var col = 0

    def hasNext = {
      row < pixels.size - 1 || (row == pixels.size - 1 && col < pixels(0).size)
    }

    def next() = {

      val res = MyPixel(row, col, pixels(row)(col))

      col += 1
      if (col == pixels(0).size) {
        row += 1
        col = 0
      }

      res
    }
  }
}

object ImageKMeans extends Logging {
  private def colorDistance(p1: MyPixel, p2: MyPixel): Double = {
    try {
      val rd = colorToFloat(p1.color.R) - colorToFloat(p2.color.R)
      val bd = colorToFloat(p1.color.B) - colorToFloat(p2.color.B)
      val gd = colorToFloat(p1.color.G) - colorToFloat(p2.color.G)
      val inner = rd*rd + bd*bd + gd*gd

      inner
    } catch {
      case ex: Exception =>
        log.warn(ex, "Exception getting the color at " + p1 + " or " + p2)
        throw ex
    }
  }

  private def euclideanDistance(rowToFloat: Int => Double)(colToFloat: Int => Double)(p1: MyPixel, p2: MyPixel): Double = {
    val x = rowToFloat(p1.row) - rowToFloat(p2.row)
    val y = colToFloat(p1.col) - colToFloat(p2.col)
    x * x + y * y
  }

  def overallDistance(rowToFloat: Int => Double)
                     (colToFloat: Int => Double)
                     (cdWeight: Double)
                     (p1: MyPixel, p2: MyPixel): Double = {
    (1.0 - cdWeight) * colorDistance(p1, p2) +
    cdWeight * euclideanDistance(rowToFloat)(colToFloat)(p1, p2)
  }

  def average(pixels: Array[MyPixel]): MyPixel = {
    var accum = Seq(0, 0, 0)
    var i = 0
    var nr = 0
    var nc = 0
    while(i < pixels.size) {
      accum = accum.zip(pixels(i).color.toSeq).map { case (a, b) => a + b }
      nr += pixels(i).row
      nc += pixels(i).col
      i += 1
    }

    nr = nr / pixels.size
    nc = nc / pixels.size
    accum = accum.map(_ / pixels.size)

    MyPixel(nr, nc, Color(accum))
  }

  def colorToFloat(color: Int) = color / 255.0

  def buildDistanceFn(width: Int, height: Int, cdWeight: Double): (MyPixel, MyPixel) => Double = {
    def rowToFloat(row: Int) = row.toDouble / height
    def colToFloat(col: Int) = col.toDouble / width

    overallDistance(rowToFloat)(colToFloat)(cdWeight)
  }

  def apply(pixels: Array[MyPixel], clusters: (Array[MyPixel], Array[Int]), K: Int, cdWeight: Double,  width: Int, height: Int): (Array[MyPixel], Array[Int]) = {
    KMeans2(pixels, clusters)(buildDistanceFn(width, height, cdWeight))(average)
  }
}