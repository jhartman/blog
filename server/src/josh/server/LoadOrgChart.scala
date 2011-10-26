package josh
package server

import shared._
import io.Source
import java.io.{DataInputStream, BufferedInputStream, FileInputStream}

object LoadOrgChart {
//  def apply(file: String): Map[Int, Member] = {
//    val regex = "\\{(.*)\\},(\\d*),(\\d*),(\\d*),(\\w*)".r
//    val edgesRegex = "\\((.*,\\d)\\)".r
//    val res = Source.fromFile(file).getLines.map { line =>
//      val regex(edgesString, id, id2, company, seniority) = line
//      val edges = edgesRegex.findAllIn(edgesString)
//
//      val properEdges = edges.map { str =>
//        val split = str.split(",")
//        Edge(split(1).toInt, split(0).toDouble)
//      }
//
//      (id.toInt, Member(id.toInt, Seniority.withName(seniority), properEdges.toSet))
//    }.toMap
//
//    System.out.println("Loaded chart")
//    res
//  }

//  def apply(file: String): Map[Int, Member] = {
//    val dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))
//    var map = Map.empty[Int, Member]
//    try {
//      while (true) {
//        val id = dis.readInt
//        val seniorityId = dis.readInt
//        val numEdges = dis.readInt
//
//        val ids = (0 until numEdges) map { i => dis.readInt() }
//        val scores = (0 until numEdges) map { i => dis.readDouble() }
//        val edges = ids.zip(scores). map { case (id, score) => Edge(id, score) }
//
//        map += id -> Member(id, Seniority(seniorityId), edges.toSet)
//      }
//    } catch {
//      case ex: Exception =>
//    }
//
//    System.out.println("Loaded chart")
////    println(map)
//    map
//  }

  def apply(file: String): Map[Int, Member] = {
    val data = Source.fromFile(file).getLines().map { line =>
      val tokens = line.split(";")

      val memberId = tokens(0).toInt
      val name = tokens(1)
      val seniority = try {
        Seniority.withName(tokens(2))
      } catch {
        case ex: Exception =>
        println(tokens(2))
        Seniority.None
      }
      val joinAt = tokens(3).toLong
      val endAt = tokens(4).toLong

      val edges = tokens(5).replace("),(", ";").replace("(", "").replace(")", "").replace("{", "").replace("}", "").split(";").map{ connectionStrength: String =>
        val s = connectionStrength.split(",")
        Edge(s(1).toInt, s(0).toDouble)
      }.toSet

      val invitations = tokens(6).replace("),(", ";").replace("(", "").replace(")", "").replace("{", "").replace("}", "").split(";").flatMap { invitation: String =>
        val s = invitation.split(",")
        try {
          if (s.size == 2) Some((s(0).toInt, s(1).toLong))
          else None
        } catch {
          case ex: Exception => None
        }
      }

      (memberId, (Member(memberId, name, seniority, joinAt, edges), invitations))
    }.toMap

    // Invitations are directional. Let's crudely make them bidirectional and rank the first 9 that happen at
    // the company on a linear scale from 1 - 0.1

    // Step 1 - bidirectional invitations
    val invitations = data.flatMap { case (memberId, (memberData, invitationGraph)) =>
      invitationGraph.flatMap { case (dest, join) =>
        List((memberId, dest, join), (dest, memberId, join))
      }
    }.groupBy { case (src, dest, join) => src }

    // Step 2 - Rejoin with the member data
    val joined = innerJoin(data, invitations).map { case (src, ((member, inv), invites)) =>
      (src, (member, invites.map { case (src, dest, join) => (dest, join) }))
    }

    joined.map { case (src, (member, invites)) =>
      val rankedInvites = invites.map { case (dest, time) =>
        (dest, time, time - member.companyJoin)
      }.filter { case (dest, time, diff) =>
        diff > 0
      }.take(10)

      val invitationEdges = rankedInvites.zipWithIndex.map { case ((dest, time, diff), idx) => Invitation(dest, (rankedInvites.size - idx).toDouble / rankedInvites.size, time) }

      (src, member.copy(invitations = invitationEdges.toSet))
    }
  }

}