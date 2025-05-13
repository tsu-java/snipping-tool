package ge.tsu.snippingtool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SnippingTool extends Application {
    private static final Logger log = LoggerFactory.getLogger(SnippingTool.class);

    @Override
    public void start(Stage stage) throws IOException {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> log.error(throwable.getMessage(), throwable));

        FXMLLoader fxmlLoader = new FXMLLoader(SnippingTool.class.getResource("main.fxml"));
        Parent parent = fxmlLoader.load();
        MainController controller = fxmlLoader.getController();
        controller.initStage(stage);

        Scene scene = new Scene(parent);
        scene.setFill(Color.TRANSPARENT);

        stage.setTitle("Snipping Tool");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        // Minimal dimension values are important for custom resizability
        stage.setMinWidth(400);
        stage.setMinHeight(300);
        stage.show();
    }

    public static void main(String[] args) {
        log.info("Application started");
        launch();
        log.info("Application finished");
    }
}