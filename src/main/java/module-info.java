module ge.tsu.snippingtool {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.slf4j;
    requires org.apache.logging.log4j;

    opens ge.tsu.snippingtool to javafx.fxml;
    exports ge.tsu.snippingtool;
}