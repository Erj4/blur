import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.lang.Math;

public class Main extends Application {

  final String IMAGEFILE="catbig.png";

  static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage){
    StackPane root = new StackPane();
    HBox hBox = new HBox();
    root.getChildren().add(hBox);
    Scene scene = new Scene(root, 1204, 600);
    stage.setScene(scene);
    stage.sizeToScene();
    stage.show();


    Image img = new Image(IMAGEFILE);
    ImageView imageView = new ImageView();
    imageView.setImage(img);
    hBox.getChildren().add(imageView);

    ImageView imageView2 = new ImageView();
    imageView2.setImage(quickSecondBlur(img));
    hBox.getChildren().add(imageView2);
  }

  static Image simpleBlur(Image old, int iterations){
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

  static Image secondBlur(Image old){
    final int ITERATIONS=1;
    final int DEFINITION=2; //Can be changed to double without issue
    return secondBlur(old, ITERATIONS, DEFINITION);
  }

  static Image secondBlur(Image old, int iterations, final int DEFINITION){
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
                red   += pr.getColor(x2, y2).getRed()/Math.pow(dx+dy,DEFINITION);
                green += pr.getColor(x2, y2).getGreen()/Math.pow(dx+dy,DEFINITION);
                blue  += pr.getColor(x2, y2).getBlue()/Math.pow(dx+dy,DEFINITION);
                prioritysum+=1/Math.pow(dx+dy,DEFINITION);
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

  static Image quickSecondBlur(Image old){
    final int ITERATIONS=1;
    final int DEFINITION=2; //Can be changed to double without issue
    final int RANGE_LIMIT=50;
    return quickSecondBlur(old, ITERATIONS, DEFINITION, RANGE_LIMIT);
  }

  static Image quickSecondBlur(Image old, int iterations, int definition, int rangeLimit){
    System.out.print("Completion: 0%");
    int completion=0;

    if(rangeLimit<-1||rangeLimit==0) return old;
    if(rangeLimit==-1) return secondBlur(old, iterations, definition);
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
