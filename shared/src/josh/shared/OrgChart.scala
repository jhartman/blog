//package josh.shared
//
//object OrgChart {
//  val JEFF_ID = 22330283
//  val random = new scala.util.Random(1)
//
//  def apply(map: Map[Int, Member]): OrgChart = {
////    val defaultNumGroups = map.values.count (_.seniority >= Seniority.Manager)
//    val filteredMap = filterLikelySpam(map)
//    val members = filteredMap.values.toSet.toArray
//
//    val groups = members.foldLeft(Set.empty[Group]) { (groups, member) =>
//      if(member.seniority >= Seniority.Manager) {
//        groups + Group(member, Set.empty)
//      } else groups
//    }
//
//    val groupArray = groups.toArray
//
//    println("Num members = " + members.size)
//    println("Num groups = " + groups.size)
//    println("Members per group = " + members.size.toDouble / groups.size)
//
//    val initialGrouping = members.foreach{ member =>
//     if (member.id != JEFF_ID) {
//        // pick a group at random (make sure the member also isn't the leader) and assign. Managers should be in 2 groups
//        var assigned = false
//        var index = -1
//        while(!assigned) {
//          index = random.nextInt(groupArray.length)
//          val assignedGroup = groupArray(index)
//          if(assignedGroup.leader.id != member.id) {
//            assigned = true
//          }
//        }
//
//        val assignedGroup = groupArray(index)
//        val newGroup = Group(assignedGroup.leader, assignedGroup.members + member)
//        groupArray(index) = newGroup
//      }
//    }
//    new OrgChart(groupArray.toSet)
//  }
//
//  def filterLikelySpam(map: Map[Int, Member]): Map[Int, Member] = {
//    map.filter { case (id, member) =>
//      // People at linkedin must have at least 10 connections at LinkedIn to be considered to actually work at LinkedIn...
//      val numConnectionsAtLinkedIn = member.edges.count { edge => map.contains(edge.dest) }
//      numConnectionsAtLinkedIn >= 10
//    }
//  }
//}
//
//case class OrgChart(val groups: Set[Group]) {
//  lazy val score = groups.foldLeft(0.0)(_ + _.score)
//
//  def members = groups.flatMap(_.members)
//
//  def print: Iterator[String] = {
//    val orderedGroups = groups.toSeq.sortWith { (group1, group2) =>
//      if(group1.leader.seniority < group2.leader.seniority) true
//      else if (group1.leader.seniority == group2.leader.seniority) group1.leader.id < group2.leader.id
//      else false
//    }.reverse
//
//    orderedGroups.iterator.map { _.toString }
//  }
//
////  def memberIterator: Iterator[Member] = new Iterator[Member] {
////    val groupIterator = groups.iterator
////    var memberIterator: Iterator[Member] = null
////    var c: Option[Member] = None
////
////    def hasNext = {
////      if (!c.isDefined) {
////        c = fetchNext
////        c.isDefined
////      } else true
////    }
////
////    def next = {
////      val res = c.get
////      c = None
////      res
////    }
////
////    def fetchNext: Option[Member] = {
////      if (memberIterator == null || !memberIterator.hasNext) {
////        if (groupIterator.hasNext) {
////          val nextGroup = groupIterator.next
////          memberIterator = nextGroup.members.iterator
////          fetchNext
////        } else None
////      } else {
////        Some(memberIterator.next)
////      }
////    }
////  }
//}