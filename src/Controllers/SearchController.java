package Controllers;

import java.io.IOException;

import Models.User;
import application.Photos;
import application.UserLauncher;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Pair;

public class SearchController {
	
	User user;
	Stage stage;
	
	@FXML
	public TextField keyInput;
	@FXML
	public TextField valueInput;
	@FXML
	public TableView<Pair<String, String>> tagTable;
	@FXML
    public TableColumn<Pair<String, String>, String> keyCol;
    @FXML
    public TableColumn<Pair<String, String>, String> valueCol;
	
    
    /**
     * Lets the Search interface know which user is logged in
     * 
     * @param user
     * @param stage
     */
    public void init(User user, Stage stage){
    	this.user = user;
    	this.stage = stage;
    }
    
    /*
     * Search Options
     */
	public void addSearchTag(ActionEvent event){
		String key = keyInput.getText();
		String value = valueInput.getText();
		
		// check to see that null tag is not added to the table
		if (key.trim().length() == 0 || value.trim().length() == 0){
		   Photos.showError("Blank entry", "Empty key : value.", "Cannot leave either key or value input blank");
		        return;
		}
		    
		Pair<String, String> tagPair = new Pair<>(key,value);
	  
		// check to see if the search tag is already in the table
		for(int i = 0; i < tagTable.getItems().size(); i++){
			if(tagPair.equals(tagTable.getItems().get(i))){
				Photos.showError("Invalid search tag", "Tag is already in the search options", "Must add unique tags to search options");
				return;
			}
		}
	  
		// add tag to table
		keyCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getKey()));
		valueCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getValue()));
		tagTable.getItems().add(tagPair);
        
	}
	
	public void search(ActionEvent event){
		
		// grab dates if user specified them
		
		// grab tags if user specified them
		
	}
	
	/*
	 * Menu Buttons
	 */
	public void newAlbum(ActionEvent event){
		
	}
	
	public void back(ActionEvent event) throws IOException{
		UserLauncher.start(user, stage);
	}
	
	public void logout(ActionEvent event) throws Exception{
		Photos.logout(event);
	}
	
	
}
