package josh.shared

import scala.util.Random

object KMeans {
  val numIters = 10

  private def createStartingVectors[T : ClassManifest](data: Array[T], K: Int, random: Random): Array[T] = {
    if (K < data.size) {
      val indexes = scala.collection.mutable.Set.empty[Int]
      while(indexes.size < K) {
        val next = random.nextInt(data.size - 1)
        indexes += next
      }

      indexes.map { idx => data(idx) }.toArray
    } else throw new IllegalArgumentException
  }

  // Input: A data vector and the number of clusters
  def cluster[T: ClassManifest](data: Array[T], indexedClusters: Array[(T, Int)], distanceFn: (T, T) => Double) = {
    data.zipWithIndex.map {
      case (dataVector, i) =>
        val closestCluster = indexedClusters.map {
          case (clusterVector, j) =>
            (distanceFn(dataVector, clusterVector), j)
        }.sorted.head._2

        (i, closestCluster)
    }.toMap
  }

  def apply[T : ClassManifest](data: Array[T], K: Int)
                              (distanceFn: (T, T) => Double)
                              (buildMean: Iterable[T] => T): Map[Int, Int] = {
    val res = (0 until numIters).foldLeft(createStartingVectors(data, K, new Random)) { (clusters, iter) =>
      val indexedClusters = clusters.zipWithIndex

      val clustering = cluster(data, indexedClusters, distanceFn)

      invert(clustering).mapValues { dataPoints =>
        buildMean(dataPoints.map(i => data(i)))
      }.values.toArray
    }

    cluster(data, res.zipWithIndex, distanceFn)
  }

  private def invert[A, B](map: Map[A, B]): Map[B, Iterable[A]] = {
    map.foldLeft(Map.empty[B, Seq[A]]) { case (accum, (key, value)) =>
      val newSeq = key +: accum.getOrElse(value, Seq.empty[A])
      accum + (value -> (newSeq))
    }
  }

//  private def cross[T : ClassManifest](a1: Array[T], a2: Array[T]): Array[(Array[T], Array[T])] = {
//    a1.foldLeft(List.empty[(Array[T], Array[T])]) { (res, elem1) =>
//      a2.foldLeft(res) { (res2, elem2) => ((elem1, elem2)) :: res2 }
//    }.reverse.toArray
//  }
}


//package josh.shared
//
//import util.Random
//
//object KMeans {
//  val numIters = 10
//
//  private def createStartingVectors[T : ClassManifest](data: Array[Array[T]], K: Int, random: Random): Array[Array[T]] = {
//    if (K < data.size) {
//      val indexes = scala.collection.mutable.Set.empty[Int]
//      while(indexes.size < K) {
//        val next = random.nextInt(data.size - 1)
//        indexes += next
//      }
//
//      indexes.map { idx => data(idx) }.toArray
//    } else throw new IllegalArgumentException
//  }
//
//  // Input: A data vector and the number of clusters
//  def cluster[T: ClassManifest](data: Array[Array[T]], indexedClusters: Array[(Array[T], Int)], distanceFn: (Array[T], Array[T]) => Double) = {
//    data.zipWithIndex.map {
//      case (dataVector, i) =>
//        val closestCluster = indexedClusters.map {
//          case (clusterVector, j) =>
//            (distanceFn(dataVector, clusterVector), j)
//        }.sorted.head._2
//
//        (i, closestCluster)
//    }.toMap
//  }
//
//  def apply[T : ClassManifest](data: Array[Array[T]], K: Int)
//                              (distanceFn: (Array[T], Array[T]) => Double)
//                              (buildMean: Iterable[Array[T]] => Array[T]): Map[Int, Int] = {
//    cluster((0 until numIters).foldLeft(createStartingVectors(data, K, new Random)) { (clusters, iter) =>
//      val indexedClusters = clusters.zipWithIndex
//
//      val clustering = cluster(data, indexedClusters, distanceFn)
//
//      invert(clustering).mapValues { dataPoints =>
//        buildMean(dataPoints.map(i => data(i)))
//      }.values.toArray
//    })
//  }
//
//  private def invert[A, B](map: Map[A, B]): Map[B, Iterable[A]] = {
//    map.foldLeft(Map.empty[B, Seq[A]]) { case (accum, (key, value)) =>
//      val newSeq = key +: accum.getOrElse(value, Seq.empty[A])
//      accum + (value -> (newSeq))
//    }
//  }
//
////  private def cross[T : ClassManifest](a1: Array[T], a2: Array[T]): Array[(Array[T], Array[T])] = {
////    a1.foldLeft(List.empty[(Array[T], Array[T])]) { (res, elem1) =>
////      a2.foldLeft(res) { (res2, elem2) => ((elem1, elem2)) :: res2 }
////    }.reverse.toArray
////  }
//}