package josh.client

import com.kiouri.sliderbar.client.view.SliderBar
import com.kiouri.sliderbar.client.solution.simplehorizontal.SliderBarSimpleHorizontal
import com.google.gwt.user.client.ui._
import Handlers._
import com.google.gwt.user.client.Window
import josh.shared.util.Logging

case class KMeansModel(cdWeight: Double, k: Int)

class KMeansView(model: KMeansModel) extends Composite with Logging {
  val kBox = new TextBox
  kBox.setText(model.k.toString)

  val slider = new SliderBarSimpleHorizontal(10, "50", false)

  val kText = new Label("K")
  val sliderText = new Label("Color vs Distance")

  val table = new Grid(4, 4)
  table.setWidget(0, 0, kText)
  table.setWidget(0, 1, kBox)
  table.setWidget(1, 0, sliderText)
  table.setWidget(1, 1, slider)

  val vp = new VerticalPanel
  vp.add(table)

  val button = new Button("Run", apply())
  vp.add(button)

  slider.addAttachHandler(slider.setDragPosition((model.cdWeight * slider.getAbsMaxLength).toInt))
  initWidget(vp)

  def apply() {
    log.info( "Slider drag position = " + slider.getDragPosition +
              " Slider max value = " + slider.getMaxValue +
              " Slider absolute max length " + slider.getAbsMaxLength)
    val model = KMeansModel(slider.getDragPosition.toDouble / slider.getAbsMaxLength, kBox.getText.toInt)
    EventQueue.fire(StartKMeansEvent(model))
  }
}