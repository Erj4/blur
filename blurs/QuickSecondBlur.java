import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

class QuickSecondBlur extends Blur {
  Image blur(Image old){
    final int ITERATIONS=1;
    final double DEFINITION=2; //Can be changed to double without issue
    final int RANGE_LIMIT=10;
    return blur(old, ITERATIONS, DEFINITION, RANGE_LIMIT);
  }

  Image blur(Image old, int iterations, double definition, int rangeLimit){
    System.out.print("Completion: 0%");
    int completion=0;

    if(rangeLimit<-1||rangeLimit==0) return old;
    if(rangeLimit==-1) return new SecondBlur().blur(old, iterations, definition);
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
          for(int x2=Math.max(x-rangeLimit,0);x2<Math.min(x+rangeLimit,old.widthProperty().get());x2++){
            for(int y2=Math.max(y-rangeLimit,0);y2<Math.min(y+rangeLimit,old.heightProperty().get());y2++){
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

        if(completion>=10) System.out.print("\b");
        System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
        completion=(int)(100*(i*old.widthProperty().get()*old.heightProperty().get()+x*old.heightProperty().get())/(iterations*old.widthProperty().get()*old.heightProperty().get()));
        System.out.print("Completion: "+completion+"%");

      }
      old=img;
    }
    System.out.println();
    return img;
  }
}
