package ge.tsu.snippingtool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class SnippingTool extends Application {

    @Override
    public void start(Stage stage) throws IOException {
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
        launch();
    }
}