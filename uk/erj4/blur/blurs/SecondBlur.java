package uk.erj4.blur.blurs;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class SecondBlur extends Blur {
  public Image blur(Image old){
    final int ITERATIONS=1;
    final int DEFINITION=2; //Can be changed to double without issue
    return blur(old, ITERATIONS, DEFINITION);
  }

  Image blur(Image old, int iterations, final double definition){
    System.out.print("Completion: 0%");
    PixelReader pr = old.getPixelReader();
    WritableImage img = new WritableImage(pr, (int) old.widthProperty().get(), (int) old.heightProperty().get());
    for(int i=0;i<iterations;i++){
      pr = old.getPixelReader();
      img = new WritableImage(pr, (int) old.widthProperty().get(), (int) old.heightProperty().get());
      PixelWriter pw = img.getPixelWriter();
      for(int x=0;x<old.widthProperty().get();x++){
        for(int y=0;y<old.heightProperty().get();y++){
          double red   = 0;
          double green = 0;
          double blue  = 0;
          double prioritysum = 0;
          for(int x2=0;x2<old.widthProperty().get();x2++){
            for(int y2=0;y2<old.heightProperty().get();y2++){
              if(x!=x2||y!=y2){
                double dx = Math.abs(x2-x);
                double dy = Math.abs(y2-y);
                red   += pr.getColor(x2, y2).getRed()/Math.pow(dx+dy,definition);
                green += pr.getColor(x2, y2).getGreen()/Math.pow(dx+dy,definition);
                blue  += pr.getColor(x2, y2).getBlue()/Math.pow(dx+dy,definition);
                prioritysum+=1/Math.pow(dx+dy,definition);
              }
            }
          }
          int wh=(int)old.widthProperty().get()*(int)old.heightProperty().get();
          Color newColor = Color.color(
              red/prioritysum,
              green/prioritysum,
              blue/prioritysum
            );
          pw.setColor(x, y, newColor);
        }
        if((int)(100*(i*old.widthProperty().get()*old.heightProperty().get()+x*old.heightProperty().get())/(iterations*old.widthProperty().get()*old.heightProperty().get()))>=10) System.out.print("\b");
        System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
        System.out.print("Completion: "+(int)(100*(i*old.widthProperty().get()*old.heightProperty().get()+x*old.heightProperty().get())/(iterations*old.widthProperty().get()*old.heightProperty().get()))+"%");
      }
      old=img;
    }
    System.out.println();
    return img;
  }
}
