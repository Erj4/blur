import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

class SimpleBlur extends Blur {
  Image blur(Image old){
    final int ITERATIONS=1;
    return blur(old, ITERATIONS);
  }

  Image blur(Image old, int iterations){
    PixelReader pr = old.getPixelReader();
    WritableImage img = new WritableImage(pr, (int) old.widthProperty().get(), (int) old.heightProperty().get());
    for(int i=0;i<iterations;i++){
      pr = old.getPixelReader();
      img = new WritableImage(pr, (int) old.widthProperty().get(), (int) old.heightProperty().get());
      PixelWriter pw = img.getPixelWriter();
      for(int x=1;x<old.widthProperty().get()-1;x++){
        for(int y=1;y<old.heightProperty().get()-1;y++){
          Color[] nearby = new Color[4];
          nearby[0]=pr.getColor(x-1, y-1);
          nearby[1]=pr.getColor(x+1, y-1);
          nearby[2]=pr.getColor(x-1, y+1);
          nearby[3]=pr.getColor(x-1, y+1);
          Color newColor = Color.color(
              (nearby[0].getRed()+nearby[1].getRed()+nearby[2].getRed()+nearby[3].getRed())/4,
              (nearby[0].getGreen()+nearby[1].getGreen()+nearby[2].getGreen()+nearby[3].getGreen())/4,
              (nearby[0].getBlue()+nearby[1].getBlue()+nearby[2].getBlue()+nearby[3].getBlue())/4
            );
          pw.setColor(x, y, newColor);
        }
      }
      old=img;
    }
    return img;
  }
}
