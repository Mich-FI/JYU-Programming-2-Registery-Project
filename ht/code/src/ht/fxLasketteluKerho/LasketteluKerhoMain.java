package ht.fxLasketteluKerho;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import kerho.Kerho;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author Michal
 * @version 19.2.2025
 *
 */
public class LasketteluKerhoMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            final FXMLLoader ldr = new FXMLLoader(getClass().getResource("LasketteluKerhoGUIView.fxml"));
            final Pane root = ldr.load();
            
            final LasketteluKerhoGUIController laskettelukerhoCtrl = (LasketteluKerhoGUIController) ldr.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("laskettelukerho.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("LasketteluKerho");
            
            Kerho kerho = new Kerho();
            laskettelukerhoCtrl.setKerho(kerho);
            
            //primaryStage.setOnCloseRequest((event)) -> {
            //    if ( !laskettelukerhoCtrl.voikoSulkea() ) event.consume;
            //  }
            primaryStage.show();
            if ( !laskettelukerhoCtrl.avaa() ) Platform.exit();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args Ei kaytossa
     */
    public static void main(String[] args) {
        launch(args);
    }
}