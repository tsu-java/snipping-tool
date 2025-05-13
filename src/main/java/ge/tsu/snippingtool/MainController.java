package ge.tsu.snippingtool;

import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends WindowController implements Initializable {

    private FileChooser fileChooser;
    private ObjectProperty<BufferedImage> imageProperty = new SimpleObjectProperty<>();

    public VBox vBoxCenter;
    public Button btnTakeSnapshot;
    public Button btnGrayscale;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle); // Always call this before anything else!

        fileChooser = new FileChooser();
        fileChooser.setTitle("Save Captured Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        log.debug("Initialized MainController");

        setResizeHandler(event -> onClear());

        // Bindings
        btnTakeSnapshot.disableProperty().bind(imageProperty.isNotNull());
        btnGrayscale.disableProperty().bind(imageProperty.isNull());

        imageProperty.addListener((observable, oldBufferedImage, newBufferedImage) -> {
            destroyCenterChildren();
            if (newBufferedImage == null) {
                return;
            }

            // Grab image bytes
            byte[] capturedImageBytes;
            try (var byteArrayOutputStream = new ByteArrayOutputStream()) {
                ImageIO.write(newBufferedImage, "png", byteArrayOutputStream);
                capturedImageBytes = byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Render captured image
            try (var byteArrayInputStream = new ByteArrayInputStream(capturedImageBytes)) {
                ImageView imageView = new ImageView(new Image(byteArrayInputStream));
                vBoxCenter.getChildren().add(imageView);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void onTakeSnapshot() throws AWTException {
        log.info("Called onTakeSnapshot(..) action");
        Bounds boundsInScreen = vBoxCenter.localToScreen(vBoxCenter.getBoundsInLocal());

        Robot robot = new Robot();
        Rectangle rect = new Rectangle(
                (int) boundsInScreen.getMinX(), (int) boundsInScreen.getMinY(),
                (int) boundsInScreen.getWidth(), (int) boundsInScreen.getHeight()
        );
        imageProperty.set(robot.createScreenCapture(rect));
    }

    public void onSave() throws IOException {
        log.info("Called onSave(..) action");
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            ImageIO.write(imageProperty.get(), "png", file);
        } else {
            log.warn("Abruptly canceled save operation");
        }
    }

    public void onGrayscale() {
        log.info("Called onGrayscale(..) action");
        GrayU8 image = new GrayU8();
        ConvertBufferedImage.convertFrom(imageProperty.get(), image);
        BufferedImage bufferedImage = ConvertBufferedImage.convertTo(image, null);
        imageProperty.set(bufferedImage);
    }

    public void onClear() {
        log.info("Called onClear(..) action");
        imageProperty.set(null);
    }

    public void destroyCenterChildren() {
        vBoxCenter.getChildren().clear();
    }
}