package issFX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("iss_checker.fxml"));
        primaryStage.setTitle("ISS Current Status");
        primaryStage.setScene(new Scene(root, 900, 375));
        primaryStage.show();
        primaryStage.getIcons().add(new Image("https://t00.deviantart.net/e12ENEOD01wJxFzTLiDspuExKFo=/300x200/filters:fixed_height(100,100):origin()/pre00/9e58/th/pre/f/2018/162/c/4/devil_may_cry_5_icon_by_kiramaru_kun-dce3hw4.png")); //Not my Image
    }


    public static void main(String[] args) {
        launch(args);
    }
}
