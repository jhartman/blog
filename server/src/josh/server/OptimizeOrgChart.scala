package josh
package server

import shared._

/**
 * Rules:
 * 1. Individual contributers should be in 1 group
 * 2. Jeff Weiner should be the leader of 1 group
 * 3. Manager - CXO should be in 2 groups. They must lead 1 group and be the member of another
 *
 * Possible actions for optimization for this implementation
 * 1. Move a member from 1 group to another
 * 2. Swap members in 2 groups
 * 3. Merge groups (TODO later)
 * 4. Split groups (TODO later)
 *
 * Features to consider
 * 1. Who are the first people the member invites with?
 * 2. Consider my subordinates connections when evaluating connection strength to properly allocate directors
 */

object OptimizeOrgChart {
  val random = new scala.util.Random(1)
//  def main(args: Array[String]) {
//    val orgChart = OrgChart(LoadOrgChart.apply("/Users/jhartman/code/orgchart/linkedinSeniorityAndConnections.dat"))
//    var joshsGroup = orgChart.groups.filter(_.members.map(_.id).contains(46079344))
//    println(joshsGroup)
//
//    val optimized = optimize(orgChart, 10)
//
//    optimized.groups.foreach(println)
//  }

  def optimize(orgChart: OrgChart, params: ScoreParams, numIterations: Int): OrgChart = {
    var chart = orgChart
    var iter = 0
    while(iter < numIterations) {
      System.out.println("Score: " + chart.score)
      chart = optimize(chart, params)
      iter += 1
    }
    chart
  }

  val knuthRandom = new scala.util.Random(1)
  def knuthShuffle[T](array: Array[T]) {
    var i = array.length - 1
    while(i > 0) {
      val j = knuthRandom.nextInt(i + 1)
      swap(array, i, j)
      i -= 1
    }
  }

  def swap[T](array: Array[T], idx1: Int, idx2: Int) {
    val tmp = array(idx1)
    array(idx1) = array(idx2)
    array(idx2) = tmp
  }

  def optimize(orgChart: OrgChart, params: ScoreParams): OrgChart = {
    val workers = orgChart.members.toArray
    knuthShuffle(workers)

    var groups = orgChart.groups

    // we want to move the worker to the group that reduces the overall score
    val scoring = workers.foreach { worker =>

      val memberToGroup = groups.flatMap { group =>
        group.members.map(m => (m, group))
      }.toMap

      val workersOldGroup = memberToGroup(worker)
      val oldBefore = workersOldGroup.score(params)
      val oldGroupMinusWorker = new Group(workersOldGroup.leader, workersOldGroup.members - worker)
      val oldAfter = oldGroupMinusWorker.score(params)
      val oldDiff = oldAfter - oldBefore

//      println("Scoring member " + worker.id)
      val scoredGroups = groups.map { group =>
        if (group.leader == worker) (group, Double.MaxValue)
        else if (group == oldGroupMinusWorker)
          (group, 0.0)
        else {
          val newBefore = group.score(params)
          val newGroup = new Group(group.leader, group.members + worker)
          val newAfter = newGroup.score(params)

          val newDiff = newAfter - newBefore
          (group, oldDiff + newDiff)
        }
      }

      val bestGroup = scoredGroups.foldLeft(null: Group, Double.MaxValue) { case ((bestGroup, bestScore), (group, score)) =>
        if (score <= bestScore) (group, score)
        else (bestGroup, bestScore)
      }

      groups = ((((groups - workersOldGroup) + oldGroupMinusWorker) - bestGroup._1) + new Group(bestGroup._1.leader, bestGroup._1.members + worker))// should overwrite previous groupings

    }

    new OrgChart(groups)
  }


//  def optimize(orgChart: OrgChart): OrgChart = {
//    val workers = orgChart.members.toArray
//    knuthShuffle(workers)
//
//    val groups = orgChart.groups.toArray
//
//    // we want to move the worker to the group that reduces the overall score
//    val scoring = workers.map { worker =>
////      println("Scoring member " + worker.id)
//      val scoredGroups = groups.map { group =>
//        if (group.leader == worker) (group, Double.MaxValue)
//        else {
//          val before = group.score
//          val newGroup = new Group(group.leader, group.members + worker)
//          val after = newGroup.score
//          (group, after - before)
//        }
//      }.toMap
//      (worker, scoredGroups)
//    }.toMap
//
//    // TODO: Check for swapping rather than adding and removing since it's technically different from a score perspective
//
//    // we have a mapping from workers => groups. Need worker => best group and then to invert it.
////    println("Beginning match phase")
//    val bestGroups = scoring.map { case (worker, groupMap) =>
//      val bestMatch = groupMap.foldLeft((null: Group, Double.MaxValue)) { case ((bestGroup, bestScore), (group, score)) =>
//        if (score <= bestScore) (group, score)
//        else (bestGroup, bestScore)
//      }
//      (worker, bestMatch._1)
//    }.toMap
//
//    val finalMapping = bestGroups.foldLeft(Map.empty[Group, Set[Member]]) { case (map, (member, group)) =>
//      map + (group -> (map.get(group) match {
//        case None => Set(member)
//        case Some(members) => members + member
//      }))
//    }
//
//    new OrgChart(finalMapping.map { case (group, members) => new Group(group.leader, members) }.toSet)
//  }


//  def gradient
}