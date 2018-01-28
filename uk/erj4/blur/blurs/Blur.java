package uk.erj4.blur.blurs;
import javafx.scene.image.Image;
public abstract class Blur {

  public abstract Image blur(Image old);

  public String toString(){
    return this.getClass().getName();
  }
}
