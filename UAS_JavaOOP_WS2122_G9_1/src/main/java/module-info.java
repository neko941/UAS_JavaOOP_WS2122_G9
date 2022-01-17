module timescheduler.uas_javaoop_ws2122_g9 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.apache.commons.lang3;
    requires java.sql;
    requires java.mail;
    requires json.simple;

    opens TimeScheduler.uas_javaoop_ws2122_g9 to javafx.fxml;
    opens Controllers to javafx.fxml;
    exports TimeScheduler.uas_javaoop_ws2122_g9;
    exports Controllers;
}