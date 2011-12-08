package josh.shared

import scala.util.Random
import util.Logging
import util.Utils._

object KMeans extends Logging {
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

  def cluster[T: ClassManifest](data: Iterable[T], clusters: Array[T], distanceFn: (T, T) => Double): Map[T, T] = {
    var i = 0
    val mapping = data.map { dataVector =>
      if (i % 1000 == 0) log.info(i + " points clustered")
      val (dist, closest) = clusters.foldLeft((Double.MaxValue, clusters.head)) { case ((min, c), cluster) =>
        val test = distanceFn(dataVector, cluster)
        if(test < min) (test, cluster) else (min, c)
      }
      i += 1
      (dataVector, closest)
    }
    mapping.toMap
  }

  def apply[T : ClassManifest](data: Array[T], K: Int)
                              (distanceFn: (T, T) => Double)
                              (buildMean: Iterable[T] => T): Map[T, T] = {
    val initial = cluster(data, createStartingVectors(data, K, new Random), distanceFn)

    (0 until numIters).foldLeft(initial) { (clusters, iter) =>
      apply(clusters)(distanceFn)(buildMean)
    }
  }

  def apply[T : ClassManifest](data: Map[T, T])
                              (distanceFn: (T, T) => Double)
                              (buildMean: Iterable[T] => T): Map[T, T] = {

    val clustering = cluster(data.keys, data.values.toArray.toSet.toArray, distanceFn)
    val inversion = invert(clustering)

    inversion.flatMap { case (oldMean, vectors) =>
      val newMean = buildMean(vectors)
      vectors.map { v => (v, newMean) }
    }.toMap

  }
}