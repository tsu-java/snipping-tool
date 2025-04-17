module ge.tsu.snippingtool {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens ge.tsu.snippingtool to javafx.fxml;
    exports ge.tsu.snippingtool;
}