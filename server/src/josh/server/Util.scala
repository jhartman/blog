package josh.server

object Util {
  def innerJoin[K, V1, V2](map1: Map[K,  V1], map2: Map[K,  V2]): Map[K,  (V1,  V2)] = {
    val keys = map1.keySet ++ map2.keySet
    keys.flatMap { key =>
      if (map1.contains(key) && map2.contains(key)) Some(key -> ( (map1(key), map2(key)) ) )
      else None
    }.toMap
  }
}