package josh
package server

import shared._

import io.Source
import java.io.{FileOutputStream, BufferedOutputStream, DataOutputStream}

object FormatData {
  def main(args: Array[String]) {
    val regex = "\\{(.*)\\},(\\d*),(\\d*),(\\d*),(\\w*)".r
    val data = Source.fromFile(args(0)).getLines.map { line =>
      val regex(edgesString, id, id2, company, seniority) = line
      val fEdges = edgesString replace("(", "") replace(")", "") split(",")
      val slidingEdges = fEdges.sliding(2, 2).toArray
      println(slidingEdges.length)

      val properEdges = slidingEdges.map { str =>
        Edge(str(1).toInt, str(0).toDouble)
      }.toArray.sorted

      (id.toInt, Seniority.fromString(seniority), properEdges)
    }

    val outputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(args(1))))
    data.foreach { case (id, seniority, edges) =>
      outputStream.writeInt(id)
      outputStream.writeInt(seniority.id)
      val ids = edges.map(_.dest)
      val scores = edges.map(_.score)
      val numEdges = edges.length
      outputStream.writeInt(numEdges)
      ids.foreach(outputStream.writeInt)
      scores.foreach(outputStream.writeDouble)
    }
    outputStream.close
  }
}