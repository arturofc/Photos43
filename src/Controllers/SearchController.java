package Controllers;

import static application.Photos.showError;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

import Models.Album;
import Models.Photo;
import Models.User;
import application.Photos;
import application.UserLauncher;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

public class SearchController {
	
	User user;
	Stage stage;
	ArrayList<Photo> searchResults = new ArrayList<>();
	
	
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
    @FXML
    public DatePicker startDate;
    @FXML
    public DatePicker endDate;
    @FXML
    public TilePane searchResultsPane;
	
    
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
	
	public void removeSearchTag(ActionEvent event){
		Pair<String, String> tagPair = tagTable.getSelectionModel().getSelectedItem();
		
		if( tagPair == null){
			Photos.showError("No selection", "No key : value selected", "Please selected a key : value to remove");
			return;
		}
		
		tagTable.getItems().remove(tagPair);
	}
	
	public void search(ActionEvent event){
		
		searchResults.clear();
		/*
		 *  get all of a user's photos for comparison
		 */
		ArrayList<Photo> userPhotos = new ArrayList<>();
		ArrayList<Album> albums = Album.getAlbumList(user); // gets all of the user's album information
		
		HashSet<Photo> tmp = new HashSet<>();
		for (Album a: albums){
			tmp = a.getPhotos();
			for(Photo p: tmp){
				userPhotos.add(p);
			}
		}
		
		/*
		 * grab all of the Photo dates
		 */
		ArrayList<LocalDate> dates = new ArrayList<>();
		for(Photo p: userPhotos){
			dates.add(p.getDate());
		}
		
		// grab date range if user specified it, if not set the min and max dates as the range
		LocalDate start = startDate.getValue() != null? startDate.getValue() : Collections.min(dates);
		LocalDate end = endDate.getValue() != null? endDate.getValue() : Collections.max(dates);
		
		// make sure the format of the date range is correct
		if(start.isAfter(end)){
			Photos.showError("Invalid Dates", "Invalid Date Range", "Please select a date range in which the start date is before the end date");
			return;
		}
		
		/*
		 * get all the photos that match the search criteria
		 */
		for(Photo p: userPhotos){
			Boolean matched = true;
			if((p.getDate().isAfter(start) || p.getDate().equals(start)) && (p.getDate().isBefore(end) || p.getDate().isEqual(end))){
				HashMap<String,HashSet<String>> tags = p.getTags();
				for(int i = 0; i < tagTable.getItems().size(); i++){
					Pair<String, String> searchTag = tagTable.getItems().get(i);
					HashSet<String> value = tags.get(searchTag.getKey());
					if(value == null){
						// the key of the search tag does not exist in the photo
						matched = false;
						break;
					}
					System.out.println(value);
					System.out.println(searchTag.getValue());
					if(!value.contains(searchTag.getValue())){
						// the value of the search tag does not match with the corresponding key in the photo
						matched = false;
						break;
					}
						
				} // if the table is blank (size 0), the user has not specified a tag to search by and thus tags should be ignored
			}else{
				matched = false;
			}
			
			if(matched){
				// only add the photo to the search results if it matches all the specified criteria
				searchResults.add(p);
			}
		}
		
		// display the search results
		searchResultsPane.getChildren().clear();
		for(Photo p: searchResults){
			searchResultsPane.getChildren().add(createTile(p));
		}
		
	}
	
    /**
     * Creates the tile for the photo and handles the click event and selection
     *
     * @param p the photo to pass in to create the tile
     * @return the create vbox tile
     */
    private VBox createTile(Photo p)
    {
        VBox v = new VBox();

        try
        {
            /*
            Image File
             */

            Image i;

            /* check if image file actually still exists */
            if (p.getPhotoFile().exists())
                i = new Image(new FileInputStream(p.getPhotoFile()), 500, 500, true, true);
            else
            {
                /* get current album and all the photos */
                return null;
            }


            /*
            Photo label
             */
            Label l = new Label(p.getName());

            /*
            Label max width
             */
            l.maxWidth(10);

            /*
            Create imageview from image i, add a dropshadow and change the viewport a bit
             */
            ImageView img = new ImageView(i);
            img.setViewport(new Rectangle2D(150, 125, 150, 100));
            img.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 2, 0, 0, 0);");

            /*
            Add to Vbox
             */
            v.setSpacing(6);
            v.getChildren().add(img);
            v.getChildren().add(l);

            /*
            The border
             */
            PseudoClass border = PseudoClass.getPseudoClass("b");

            v.getStyleClass().add("img-border");

            BooleanProperty borderActive = new SimpleBooleanProperty()
            {
                @Override
                protected void invalidated()
                {
                    v.pseudoClassStateChanged(border, get());
                }
            };

            v.setOnMouseEntered(t -> v.setStyle("-fx-background-color:#eeeeee;"));
            v.setOnMouseExited(t -> v.setStyle("-fx-background-color:transparent;"));

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("Error creating image view");
        }
        return v;
    }
	
	
	/*
	 * Menu Buttons
	 */
	public void newAlbum(ActionEvent event){
		if(searchResults.isEmpty()){
			Photos.showError("No Results","Cannot Create the Album", "There must be search results to create a new album with");
			return;
		}
		
        /*
        Create a text input dialog
         */
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Album");
        dialog.setHeaderText("Create a Album");
        dialog.setContentText("Enter the new album's name");
        /*
        Wait for results
         */
        Optional<String> result = dialog.showAndWait();
        
        HashSet<Photo> photos = new HashSet<>();
        photos.addAll(searchResults);
        
        Album a = new Album(result.get(), photos, user);
        
        /*
        if there is a result and the trimmed result length is 0
         */
        if (result.isPresent() && result.get().trim().length() == 0)
        {
            showError("Blank name", "Blank name", "Cannot have a blank name or name with only spaces.");
            return;
        }

        else if (result.isPresent() && !Album.doesAlbumNameExist(result.get(), user))
            Album.commitAlbum(a);
        /*
        otherwise, if theres a result and album name does exist throw an error
         */
        else if (result.isPresent() && Album.doesAlbumNameExist(result.get(), user))
        {
            showError("Duplicate Album", "Album already exists", "Cannot create an album with the same name as an exsisting album");
            return;
        }
        
	}
	
	public void back(ActionEvent event) throws IOException{
		UserLauncher.start(user, stage);
	}
	
	public void logout(ActionEvent event) throws Exception{
		Photos.logout(event);
	}
	
	
}
