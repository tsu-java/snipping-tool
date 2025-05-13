package ge.tsu.snippingtool;

import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
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
import java.nio.file.Files;
import java.util.ResourceBundle;

public class MainController extends WindowController implements Initializable {

    private FileChooser fileChooser;
    private byte[] capturedImageBytes;

    public VBox vBoxCenter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle); // Always call this before anything else!

        fileChooser = new FileChooser();
        fileChooser.setTitle("Save Captured Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        log.debug("Initialized MainController");
    }

    public void onTakeSnapshot() throws AWTException, IOException {
        log.info("Called onTakeSnapshot(..) action");
        Bounds boundsInScreen = vBoxCenter.localToScreen(vBoxCenter.getBoundsInLocal());

        Robot robot = new Robot();
        Rectangle rect = new Rectangle(
                (int) boundsInScreen.getMinX(), (int) boundsInScreen.getMinY(),
                (int) boundsInScreen.getWidth(), (int) boundsInScreen.getHeight()
        );
        BufferedImage bufferedImage = robot.createScreenCapture(
                rect
        );

        // Clear previous image
        onClear();

        // Grap image bytes
        try (var byteArrayOutputStream = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            capturedImageBytes = byteArrayOutputStream.toByteArray();
        }

        // Render captured image
        try (var byteArrayInputStream = new ByteArrayInputStream(capturedImageBytes)) {
            ImageView imageView = new ImageView(new Image(byteArrayInputStream));
            vBoxCenter.getChildren().add(imageView);
        }
    }

    public void onSave() throws IOException {
        log.info("Called onSave(..) action");
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            Files.write(file.toPath(), capturedImageBytes);
        } else {
            log.warn("Abruptly canceled save operation");
        }
    }

    public void onClear() {
        log.info("Called onClear(..) action");
        vBoxCenter.getChildren().clear();
    }
}