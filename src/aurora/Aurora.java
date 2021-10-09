package aurora;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class Aurora extends Application {
    public static void main(String[] args) {
	launch (args);
    }
    
    @Override
    public void start (Stage primaryStage) throws Exception {
	FXMLLoader loader = new FXMLLoader (getClass().getResource ("MainScene.fxml"));
	Parent root = loader.load();
	
	Image icon = new Image(getClass().getResourceAsStream("../images/icon.png"));
	primaryStage.getIcons().add(icon);
	
	Scene primaryScene = new Scene (root);
	primaryStage.setResizable(false);
	primaryStage.setTitle("Aurora");
	primaryStage.setScene(primaryScene);
	primaryStage.show ();
    }
}
