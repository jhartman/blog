package josh.client

sealed trait Event

case class StartKMeansEvent(model: KMeansModel) extends Event

object EventQueue {
  private val listeners = collection.mutable.Map.empty[ClassManifest[Event], collection.mutable.Set[Event => Unit]]

  def fire[EventT <: Event](event: EventT)(implicit cm: ClassManifest[EventT]) {
    listeners.get(cm.asInstanceOf[ClassManifest[Event]]).foreach { listeners =>
      listeners.foreach { listener =>
        listener.apply(event)
      }
    }
  }

  def register[EventT <: Event](fn: EventT => Unit)(implicit cm: ClassManifest[EventT]) {
    val genFn = fn.asInstanceOf[Event => Unit]
    val genCm = cm.asInstanceOf[ClassManifest[Event]]
    listeners.get(genCm) match {
      case Some(l) => l += genFn
      case None => listeners.put(genCm, collection.mutable.Set(genFn))
    }
  }
}