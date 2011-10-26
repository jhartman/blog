package josh
package shared


sealed trait Seniority extends Ordered[Seniority] {
  def id: Int

  def diff(that: Seniority): Int = {
    this.id - that.id
  }

  def score(that: Seniority): Int = {
    val d = this diff that
    val score = d - 1

    if (this == Seniority.Manager && that == Seniority.Entry) 0 // entry level employees can report straight to a manager without penalty
    else score
  }

  def compare(that: Seniority) = this.id - that.id
}

object Seniority {
  private val mapping = Map(
    "none" -> None,
    "None" -> None,
    "Unpaid" -> Unpaid,
    "Training" -> Training,
    "Entry" -> Entry,
    "Senior" -> Senior,
    "Manager" -> Manager,
    "Director" -> Director,
    "VP" -> VP,
    "CXO" -> CXO,
    "Owner" -> Owner,
    "Partner" -> Partner,
    "Volunteer" -> Volunteer)

  def fromString(string: String): Seniority = mapping(string)

  case object None extends Seniority {
    def id = 0
  }

  case object Unpaid extends Seniority {
    def id = 1
  }

  case object Volunteer extends Seniority {
    def id = 2
  }

  case object Training extends Seniority {
    def id = 3
  }

  case object Entry extends Seniority {
    def id = 4
  }

  case object Senior extends Seniority {
    def id = 5
  }

  case object Manager extends Seniority {
    def id = 6
  }

  case object Director extends Seniority {
    def id = 7
  }

  case object VP extends Seniority {
    def id = 8
  }

  case object CXO extends Seniority {
    def id = 9
  }

  case object Partner extends Seniority {
    def id = 10
  }

  case object Owner extends Seniority {
    def id = 11
  }


}
