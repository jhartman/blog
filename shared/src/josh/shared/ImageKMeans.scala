package josh.shared

object Color{
  def apply(colors: Seq[Int]): Color = {
    Color(colors(0), colors(1), colors(2))
  }
}
case class Color(R: Int, G: Int, B: Int) {
  def toSeq = Seq(R, G, B)
}
case class MyPixel(row: Int, col: Int, color: Color)

case class Image(pixels: Array[Array[Color]]) extends Iterable[MyPixel] {
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

object ImageKMeans {
  def apply(image: Image, K: Int) {
    val data = image.pixels.zipWithIndex.flatMap { case (row, r) =>
      row.zipWithIndex.map { case (col, c) =>
        MyPixel(r, c, col)
      }
    }

    def colorDistance(p1: MyPixel, p2: MyPixel): Double = {
      val inner = ((p1.color.toSeq).zip(p2.color.toSeq)).foldLeft(0.0) { case (sum, (c1, c2)) =>
        val diff = colorToFloat(c1) - colorToFloat(c2)
        diff * diff
      }

      math.sqrt(inner)
    }

    def euclideanDistance(p1: MyPixel, p2: MyPixel): Double = {
      val x = rowToFloat(p1.row) - rowToFloat(p2.row)
      val y = colToFloat(p1.col) - colToFloat(p2.col)
      math.sqrt(x * x + y * y)
    }

    def average(colors: Iterable[MyPixel]): MyPixel = {
      val newColors = colors.map(_.color.toSeq).foldLeft(Seq(0, 0, 0)) { case (total, c) =>
        total.zip(c).map { case (a, b) => a + b }
      }.map(c => c / colors.size)
      MyPixel(0, 0, Color(newColors))
    }


    // Normalize everything to 0-1
    def colorToFloat(color: Int) = color / 255.0
    def rowToFloat(row: Int) = row.toDouble / image.pixels.size
    def colToFloat(col: Int) = col.toDouble / image.pixels(0).size


    KMeans(data, K)(colorDistance)(average)
  }
}