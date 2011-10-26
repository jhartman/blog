package josh.shared

//object Group {
//  def apply(leader: Member, members: Set[Member]): Group = new Group(leader, members)
//}

case class Group(val leader: Member, val members: Set[Member]) {
  val SENIORITY_K = 20.0
  val GROUP_SIZE_K = 4.0
  val EDGE_SCORE_K = 1.0
  val INVITE_SCORE_K = 2.0

  private var score: (ScoreParams, Double) = _

  def score(params: ScoreParams): Double = {
    // lazy cache
    if (score == null || score._1 != params)
      score = (params, score0(params))

    score._2
  }

  private def score0(scoring: ScoreParams) = {
    scoring.seniority * computeSeniorityScore +
    scoring.groupSize * computeGroupSizeScore +
    scoring.edgeScore * computeConnectionScore +
    scoring.invitationScore * computeInvitationScore
  }

  def computeSeniorityScore = {
    val leaderSeniority = leader.seniority
    members.map(_.seniority).foldLeft(0) { (sum, seniority) =>
      val score = leaderSeniority score seniority
      sum + score * score
    }
  }

  def computeGroupSizeScore = {
    val s = members.size + 1 - 5
    s * s
  }

  def computeConnectionScore = computeMemberScore( (m1: Member,  m2: Member) => m1.edgeScore(m2))
  def computeInvitationScore = computeMemberScore( (m1: Member,  m2: Member) => m1.inviteScore(m2))

  def computeMemberScore(scoreFn: (Member,  Member) => Double) = {
    val allMembers = members + leader
    // sum the score^2 for all members

    allMembers.foldLeft(0.0) { (sum, src) =>
      val memberScore = allMembers.foldLeft(0.0) { (sum, dest) =>
        if (src == dest)
          sum
        else {
          val score = scoreFn(src, dest)
          sum + (if (dest == leader) 4.0 else 1.0) * (score * score)
        }
      }
      memberScore
    }
  }


  override def hashCode() = leader.hashCode()

  override def equals(that: Any) = {
    if(that.isInstanceOf[Group]) {
      this.leader == that.asInstanceOf[Group].leader
    } else false
  }

  override def toString = "Group{leader = %s, members = [%s]}".format(leader.id, members.map(_.id).mkString(","))
}