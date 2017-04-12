package application;

import java.io.IOException;

import Controllers.SearchController;
import Models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * @author Calin Gilan
 * @author Arturo Corro
 */

public class SearchLauncher {
	/**
	 * Start Search Results panel
	 * @param u user that is passed in when logged in
	 * @param s stage which will displayed to the user
	 * @throws IOException 
	 */
	public static void start(User u, Stage s) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(UserLauncher.class.getResource("/view/Search.fxml"));
        AnchorPane root = loader.load();
       
        SearchController sCont = loader.getController();
        sCont.init(u, s);

        s.setTitle("Search");
        s.setScene(new Scene(root));
        s.setResizable(false);
        s.show();

	}
}
