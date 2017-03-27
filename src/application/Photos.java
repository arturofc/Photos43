package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.loginController;



import java.io.IOException;

import static javafx.application.Application.launch;

public class Photos extends Application
{

		public void start(Stage primaryStage) throws IOException
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/Login.fxml"));
			AnchorPane root = (AnchorPane)loader.load();

			loginController lCont = loader.getController();
			lCont.launch();

			primaryStage.setTitle("Song Library");
			primaryStage.setScene(new Scene(root));
			primaryStage.setResizable(false);
			primaryStage.show();


			//unitTest.testAll();

		}

		public static void main(String[] args)
		{
			launch(args);
		}

}
