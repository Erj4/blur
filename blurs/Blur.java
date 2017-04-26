import javafx.scene.image.Image;
abstract class Blur {

  abstract Image blur(Image old);

  public String toString(){
    return this.getClass().getName();
  }
}
