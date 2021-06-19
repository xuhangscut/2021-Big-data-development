import scala.beans.BeanProperty

case class buy_record(@BeanProperty var buy_time:String,
                      @BeanProperty var buy_address:String,
                      @BeanProperty var origin:String,
                      @BeanProperty var destination:String,
                      @BeanProperty var username:String) {

  def this() = {
    this(null, null, null, null,null)
  }

}