package com.ecommunicator.client;

import com.ecommunicator.client.net.EcpClient;
import com.ecommunicator.client.ui.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * JavaFX entry point for the eCommunicator client.
 *
 * Run:  mvn javafx:run  (inside ecommunicator-client/)
 *   or: java --module-path $JAVAFX_LIB --add-modules javafx.controls,javafx.fxml \
 *            -jar ecommunicator-client.jar
 */
public class ClientApplication extends Application {

    private static final Logger log = LoggerFactory.getLogger(ClientApplication.class);

    /** Global ECP client — created once, passed around via controllers. */
    private EcpClient ecpClient;

    @Override
    public void start(Stage primaryStage) throws Exception {
        log.info("eCommunicator client starting");

        ecpClient = new EcpClient();

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/ecommunicator/client/login.fxml"));
        Scene loginScene = new Scene(loader.load(), 520, 620);
        loginScene.getStylesheets().add(
                getClass().getResource("/com/ecommunicator/client/styles/dark.css").toExternalForm());

        LoginController loginController = loader.getController();
        loginController.init(primaryStage, ecpClient);

        primaryStage.setTitle("eCommunicator");
        primaryStage.setScene(loginScene);
        primaryStage.setResizable(false);

        // Set app icon if available
        InputStream iconStream = getClass().getResourceAsStream("/com/ecommunicator/client/images/icon.png");
        if (iconStream != null) {
            primaryStage.getIcons().add(new Image(iconStream));
        }

        primaryStage.show();
        log.info("Login screen displayed");
    }

    @Override
    public void stop() {
        log.info("Application stopping");
        if (ecpClient != null) {
            ecpClient.disconnect();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
