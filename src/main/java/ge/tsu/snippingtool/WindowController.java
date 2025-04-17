package ge.tsu.snippingtool;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class WindowController extends AbstractController implements Initializable {

    protected double xOffset = 0;
    protected double yOffset = 0;

    // Debug mode flag
    // TODO Extract from configuration file or somewhere else!
    public static final boolean DEBUG = false;

    // Window Edges (North, East, South, West)
    public Pane edgeNW;
    public Pane edgeN;
    public Pane edgeNE;
    public Pane edgeW;
    public Pane edgeE;
    public Pane edgeSW;
    public Pane edgeS;
    public Pane edgeSE;

    public HBox titleBox; // Title bar container (draggable)

    @FXML
    private Label titleLabel;

    // Title bar buttons
    public Button btnIconify; // 🗕
    public Button btnFullScreen; // 🗗︎🗖
    public Button btnClose; // 🗙

    public WindowController() {
        super();
        setAfterStageInit((stage) -> {
            // Automatically binds to stage's title
            titleLabel.textProperty().bind(stage.titleProperty());
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // In case of debug mode, we'll show thick and colourful borders
        if (DEBUG) {
            edgeN.setStyle("-fx-background-color: green;");
            edgeN.setPrefHeight(10);

            edgeS.setStyle("-fx-background-color: green;");
            edgeS.setPrefHeight(10);

            edgeE.setStyle("-fx-background-color: blue;");
            edgeE.setPrefWidth(10);
            edgeW.setStyle("-fx-background-color: blue;");
            edgeW.setPrefWidth(10);

            edgeNE.setStyle("-fx-background-color: red;");
            edgeNE.setPrefWidth(10);
            edgeNE.setPrefHeight(10);

            edgeNW.setStyle("-fx-background-color: red;");
            edgeNW.setPrefWidth(10);
            edgeNW.setPrefHeight(10);

            edgeSE.setStyle("-fx-background-color: red;");
            edgeSE.setPrefWidth(10);
            edgeSE.setPrefHeight(10);

            edgeSW.setStyle("-fx-background-color: red;");
            edgeSW.setPrefWidth(10);
            edgeSW.setPrefHeight(10);
        }

        btnIconify.setText("\uD83D\uDDD5");
        btnFullScreen.setText("\uD83D\uDDD6");
        btnClose.setText("\uD83D\uDDD9");

        // Register window edges' handles
        addResizeHandler(edgeN, ResizeDirection.NORTH);
        addResizeHandler(edgeS, ResizeDirection.SOUTH);
        addResizeHandler(edgeE, ResizeDirection.EAST);
        addResizeHandler(edgeW, ResizeDirection.WEST);
        addResizeHandler(edgeNE, ResizeDirection.NORTH_EAST);
        addResizeHandler(edgeNW, ResizeDirection.NORTH_WEST);
        addResizeHandler(edgeSE, ResizeDirection.SOUTH_EAST);
        addResizeHandler(edgeSW, ResizeDirection.SOUTH_WEST);

        // Register title bar double click event
        titleBox.setOnMousePressed(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    toggleFullScreen();
                }
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });

        // Register title bar drag event
        titleBox.setOnMouseDragged(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)
                    && !stage.isFullScreen()) {
                if (event.getClickCount() < 2) { // Ignores double clicks
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            }
        });
    }

    public void toggleIconify() {
        stage.setIconified(!stage.isIconified());
    }

    public void toggleFullScreen() {
        stage.setFullScreen(!stage.isFullScreen());
        if (stage.isFullScreen()) {
            btnFullScreen.setText("\uD83D\uDDD7");
        } else {
            btnFullScreen.setText("\uD83D\uDDD6");
        }
    }

    public void exit() {
        Platform.exit();
    }

    /**
     * Adds reside handler to window edge panes.
     *
     * @param pane
     * @param direction
     */
    protected void addResizeHandler(Pane pane, ResizeDirection direction) {
        pane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        pane.setOnMouseDragged(event -> {
            double dx = event.getScreenX() - stage.getX();
            double dy = event.getScreenY() - stage.getY();

            switch (direction) {
                case EAST:
                    double newWidth = dx;
                    if (newWidth >= stage.getMinWidth()) {
                        stage.setWidth(newWidth);
                    }
                    break;
                case WEST:
                    double deltaX = event.getScreenX() - stage.getX();
                    double width = stage.getWidth() - deltaX;
                    if (width >= stage.getMinWidth()) {
                        stage.setX(event.getScreenX());
                        stage.setWidth(width);
                    }
                    break;
                case SOUTH:
                    double newHeight = dy;
                    if (newHeight >= stage.getMinHeight()) {
                        stage.setHeight(newHeight);
                    }
                    break;
                case NORTH:
                    double deltaY = event.getScreenY() - stage.getY();
                    double height = stage.getHeight() - deltaY;
                    if (height >= stage.getMinHeight()) {
                        stage.setY(event.getScreenY());
                        stage.setHeight(height);
                    }
                    break;
                case NORTH_EAST:
                    // Combine NORTH and EAST
                    resizeNorth(event);
                    resizeEast(event);
                    break;
                case NORTH_WEST:
                    // Combine NORTH and WEST
                    resizeNorth(event);
                    resizeWest(event);
                    break;
                case SOUTH_EAST:
                    // Combine SOUTH and EAST
                    resizeSouth(event);
                    resizeEast(event);
                    break;
                case SOUTH_WEST:
                    // Combine SOUTH and WEST
                    resizeSouth(event);
                    resizeWest(event);
                    break;
            }
        });
    }

    protected void resizeNorth(MouseEvent event) {
        double deltaY = event.getScreenY() - stage.getY();
        double height = stage.getHeight() - deltaY;
        if (height >= stage.getMinHeight()) {
            stage.setY(event.getScreenY());
            stage.setHeight(height);
        }
    }

    protected void resizeSouth(MouseEvent event) {
        double newHeight = event.getScreenY() - stage.getY();
        if (newHeight >= stage.getMinHeight()) {
            stage.setHeight(newHeight);
        }
    }

    protected void resizeEast(MouseEvent event) {
        double newWidth = event.getScreenX() - stage.getX();
        if (newWidth >= stage.getMinHeight()) {
            stage.setWidth(newWidth);
        }
    }

    protected void resizeWest(MouseEvent event) {
        double deltaX = event.getScreenX() - stage.getX();
        double width = stage.getWidth() - deltaX;
        if (width >= stage.getMinWidth()) {
            stage.setX(event.getScreenX());
            stage.setWidth(width);
        }
    }

    /**
     * Window edge directions.
     */
    public enum ResizeDirection {
        EAST, WEST, NORTH, SOUTH, NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST
    }
}
