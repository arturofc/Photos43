package Controllers;

import Models.User;
import application.Photos;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Pair;

public class SearchController {
	
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
	
	public void addSearchTag(ActionEvent event){
		String key = keyInput.getText();
		String value = valueInput.getText();
		
		if (key.trim().length() == 0 || value.trim().length() == 0){
		   Photos.showError("Blank entry", "Empty key : value.", "Cannot leave either key or value input blank");
		        return;
		}
		    
		Pair<String, String> tagPair = new Pair<>(key,value);
	  
		// check to see if the search tag is already in the table
		for(int i = 0; i < tagTable.getItems().size(); i++){
			if(tagPair.equals(tagTable.getItems().get(i))){
				Photos.showError("Invalid search tag", "Tag is already in the search options", "Must add unique tags to search options");
			}
		}
	  
		keyCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getKey()));
		valueCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getValue()));
		tagTable.getItems().add(tagPair);
        
	}
	
	public void search(ActionEvent event){
		
	}
}
