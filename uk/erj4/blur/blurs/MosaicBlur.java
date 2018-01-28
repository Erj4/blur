package uk.erj4.blur.blurs;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class MosaicBlur extends Blur {
  public Image blur(Image old){
    final int X_BLOCKS = 10;
    final int Y_BLOCKS = 10;
    return blur(old, X_BLOCKS, Y_BLOCKS);
  }

  Image blur(Image old, int xBlocks, int yBlocks){
    if(xBlocks<1||yBlocks<1) return old;
    else if(xBlocks>=old.widthProperty().get()&&yBlocks>=old.heightProperty().get()) return old;

    class Block{
      int startX;
      int startY;
      public Block(int startX, int startY){
        this.startX=startX;
        this.startY=startY;
      }
    }

    PixelReader pr = old.getPixelReader();
    WritableImage img = new WritableImage(pr, (int) old.widthProperty().get(), (int) old.heightProperty().get());
    PixelWriter pw = img.getPixelWriter();

    Block[] blocks = new Block[xBlocks*yBlocks];
    double blockWidth = old.widthProperty().get()/xBlocks;
    double blockHeight = old.heightProperty().get()/yBlocks;

    for(int x=0;x<xBlocks;x++){
      for(int y=0; y<yBlocks;y++){
        blocks[x*xBlocks+y]=new Block((int)Math.floor(x*blockWidth), (int)Math.floor(y*blockHeight));
      }
    }

    for(Block block:blocks){
      double red=0;
      double green=0;
      double blue=0;
      int count=0;

      for(int x=block.startX;x<block.startX+blockWidth;x++){
        for(int y=block.startY;y<block.startY+blockHeight;y++){
          Color pixelColor = pr.getColor(x, y);
          red+=pixelColor.getRed();
          green+=pixelColor.getGreen();
          blue+=pixelColor.getBlue();
          count++;
        }
      }
      red/=count;
      green/=count;
      blue/=count;
      for(int x=block.startX;x<block.startX+blockWidth;x++){
        for(int y=block.startY;y<block.startY+blockHeight;y++){
          pw.setColor(x, y, Color.color(red, green, blue));
        }
      }
    }
    return img;
  }
}
