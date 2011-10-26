package josh.shared

object Edge {
//  def apply(dest: Int, score: Double): Edge = new Edge(dest, score)
  def apply(dest: Int): Edge = new Edge(dest, 0)
}

case class Edge(val dest: Int, val score: Double) extends Ordered[Edge] {
  override def hashCode() = dest.hashCode()

  override def equals(obj: Any) = {
    if (obj.isInstanceOf[Edge]) {
      dest == obj.asInstanceOf[Edge].dest
    } else false
  }

  def compare(that: Edge) = this.dest - that.dest
}

object Invitation {
//  def apply(dest: Int, score: Double, edgeCreation: Long): Invitation = {
//    new Invitation(dest, score, edgeCreation)
//  }


  def apply(dest: Int): Edge = new Edge(dest, 0)
}

case class Invitation(val dest: Int, val score: Double, val edgeCreation: Long) extends Ordered[Invitation] {
  override def hashCode() = dest.hashCode()

  override def equals(obj: Any) = {
    if (obj.isInstanceOf[Invitation]) {
      dest == obj.asInstanceOf[Invitation].dest
    } else false
  }

  def compare(that: Invitation) = this.dest - that.dest
}