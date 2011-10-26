package josh.shared

case class Member(id: Int, name: String, seniority: Seniority, companyJoin: Long,  edges: Set[Edge], invitations: Set[Invitation] = Set.empty) {
  val edgeMap = edges.foldLeft(Map.empty[Int, Edge]) { (map, edge) => map + (edge.dest -> edge) }
  val invitationMap = edges.foldLeft(Map.empty[Int, Edge]) { (map, edge) => map + (edge.dest -> edge) }

  def edgeScore(that: Member) = {
    val score = getScoreFor(that, edgeMap)
    1.0 - score
  }

  def inviteScore(that: Member) = {
    val score = getScoreFor(that, invitationMap)
    val millisSinceJoin = System.currentTimeMillis() - companyJoin
    if (millisSinceJoin < 2 * 365 * 24 * 60 * 60 * 1000L)
      1.0 - score
    else
      0.0
  }

  private def getScoreFor(that: Member, map: Map[Int, Edge]) = {
     map.get(that.id) match {
      case Some(edge) => edge.score
      case None => 0.0
    }
  }

  override def hashCode() = id.hashCode()

  override def equals(that: Any) = {
    if (that.isInstanceOf[Member]) {
      this.id == that.asInstanceOf[Member].id
    } else false
  }

  override def toString = "Member: {id=%d, edges=[%s])".format(id, edges.mkString(","))
}