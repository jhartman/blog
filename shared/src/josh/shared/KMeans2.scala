package josh.shared

import scala.util.Random
import util.Logging
import util.Utils._
import collection.mutable.ArrayBuffer

object KMeans2 extends Logging {
  val numIters = 10

  def createStartingVectors[T : ClassManifest](data: Array[T], K: Int, random: Random): Array[T] = {
    if (K < data.size) {
      val indexes = scala.collection.mutable.Set.empty[Int]
      while(indexes.size < K) {
        val next = random.nextInt(data.size - 1)
        indexes += next
      }

      indexes.map { idx => data(idx) }.toArray
    } else throw new IllegalArgumentException
  }

  def cluster[T: ClassManifest](data: Array[T], clusters: Array[T], distanceFn: (T, T) => Double): Array[Int] = {
    val clustering = new Array[Int](data.length)
    var i = 0
    while(i < data.length) {
      val dataVector = data(i)
      
      var j = 0
      var min = Double.MaxValue
      var minIndex = 0
      while(j < clusters.length) {
        val distance = distanceFn(dataVector, clusters(j))
        if (distance < min) {
          min = distance
          minIndex = j
        }
        j += 1
      }
      clustering(i) = minIndex
      i += 1
    }
    clustering
  }

//  def apply[T : ClassManifest](data: Array[T], K: Int)
//                              (distanceFn: (T, T) => Double)
//                              (buildMean: Iterable[T] => T): Array[Int] = {
//    val initial = cluster(data, createStartingVectors(data, K, new Random), distanceFn)
//
//    (0 until numIters).foldLeft(initial) { (clusters, iter) =>
//      apply(clusters)(distanceFn)(buildMean)
//    }
//  }

  def apply[T : ClassManifest](data: Array[T], clustering: (Array[T], Array[Int]))
                              (distanceFn: (T, T) => Double)
                              (buildMean: Iterable[T] => T): (Array[T], Array[Int]) = {

    val newClustering = cluster(data, clustering._1, distanceFn)
    var i = 0
    val grouped = collection.mutable.Map.empty[Int, ArrayBuffer[T]]

    data.foreach { item =>
      val key = newClustering(i)
      grouped.get(key) match {
        case Some(buff) => buff += (item)
        case None => grouped += (key -> (ArrayBuffer(item)))
      }

      i += 1
    }

    val newClusters = grouped.map { case (idx, vectors) =>
      val newMean = buildMean(vectors)
      (idx, newMean)
    }.toMap

    val newClusterArray = new Array(newClusters.keys.max)
    newClusters.foreach { case (idx, newMean) => newClusterArray(idx) = newMean}

    log.info("Prev num clusters = " + clustering._1.length + ". New num clusters = " + newClusterArray.length + ".")

    (newClusterArray, newClustering)
  }
  
}