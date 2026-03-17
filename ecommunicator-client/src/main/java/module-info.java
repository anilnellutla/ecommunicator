module com.ecommunicator.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.media;

    requires org.java_websocket;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires org.slf4j;

    requires java.desktop;   // For javax.sound.sampled and java.awt.Robot

    opens com.ecommunicator.client             to javafx.fxml;
    opens com.ecommunicator.client.ui          to javafx.fxml;
    opens com.ecommunicator.client.whiteboard  to javafx.fxml;

    exports com.ecommunicator.client;
    exports com.ecommunicator.client.net;
    exports com.ecommunicator.client.model;
    exports com.ecommunicator.client.whiteboard;
    exports com.ecommunicator.client.whiteboard.tool;
    exports com.ecommunicator.client.audio;
    exports com.ecommunicator.client.video;
    exports com.ecommunicator.client.ui;
}
