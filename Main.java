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
import javafx.stage.FileChooser;
import java.io.File;
import java.net.MalformedURLException;

import java.lang.Math;
import java.util.ArrayList;
import javafx.scene.control.ChoiceDialog;
import java.util.Optional;
import javafx.stage.FileChooser.ExtensionFilter;
import blurs.*;

public class Main extends Application {

  static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    // Set up scene
    StackPane root = new StackPane();
    HBox hBox = new HBox();
    root.getChildren().add(hBox);
    Scene scene = new Scene(root, 1204, 600);
    stage.setScene(scene);
    stage.setTitle("Blur");

    // Choose image
    Image image = selectImage(stage);
    ImageView originalImageView;
    try{
      originalImageView = new ImageView();
    } catch (Exception e) {
      return;
    }
    originalImageView.setImage(image);
    hBox.getChildren().add(originalImageView);
    if(image==null) return;

    // Choose blur
    Blur blur = selectBlur();
    if(blur==null) return;

    ImageView blurredImageView = new ImageView();
    blurredImageView.setImage(blur.blur(image));
    hBox.getChildren().add(blurredImageView);

    // Show stage
    stage.sizeToScene();
    stage.show();
  }

  static Blur selectBlur() {
    // List blur options
    ArrayList<Blur> blurs = new ArrayList<>();
    blurs.add(new SimpleBlur());
    blurs.add(new SecondBlur());
    blurs.add(new QuickSecondBlur());
    blurs.add(new MosaicBlur());

    // Create dialog to ask which to use
    ChoiceDialog<Blur> dialog = new ChoiceDialog<>(blurs.get(0), blurs);
    dialog.setTitle("Select blur");
    dialog.setHeaderText("Select blur");
    dialog.setContentText("Select the type of blur to use:");

    Optional<Blur> result = dialog.showAndWait(); // Show dialog and get response
    if(result.isPresent()){
      return result.get(); // Return chosen blur
    }
    return null; // If cancel pressed
  }

  static Image selectImage(Stage stage) {
    // To find image to blur
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Image File");
    fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", ".bmp"));
    File imageFile = fileChooser.showOpenDialog(stage);
    String filePath = "";
    try {
      filePath = (imageFile!=null)?imageFile.toURI().toURL().toExternalForm():"cat.png";
    }catch(MalformedURLException e){}
    return new Image(filePath);
  }

}
