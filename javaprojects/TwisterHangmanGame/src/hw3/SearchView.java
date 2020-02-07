//Akanksha Joshi-akankshj
package hw3;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class SearchView{

	ComboBox<String> gameComboBox = new ComboBox<>(); //shows drop down for filtering the tableView data
	TextField searchTextField = new TextField();  //for entering search letters
	TableView<Score> searchTableView = new TableView<>();  //displays data from scores.csv
	Callback<CellDataFeatures<Score, String>, ObservableValue<String>> gameNameCallBack;
	Callback<CellDataFeatures<Score, String>,ObservableValue<String>> scoreCallBack;
	
	/**setupView() sets up the GUI components
	 * for Search functionality
	 */
	void setupView() {
		
		VBox searchVBox = new VBox();  //searchVBox contains searchLabel and searchHBox
		Text searchLabel = new Text("Search");
		searchVBox.getChildren().add(searchLabel);

		HBox searchHBox = new HBox();  //searchHBox contain gameComboBox and searchTextField
		searchHBox.getChildren().add(gameComboBox);
		searchHBox.getChildren().add(new Text("Search letters"));
		searchHBox.getChildren().add(searchTextField);
		searchVBox.getChildren().add(searchHBox);
		
		searchLabel.setStyle( "-fx-font: 30px Tahoma;" + 
				" -fx-fill: linear-gradient(from 0% 50% to 50% 100%, repeat, lightgreen 0%, lightblue 50%);" +
				" -fx-stroke: gray;" +
				" -fx-background-color: gray;" +
				" -fx-stroke-width: 1;") ;
		searchHBox.setPrefSize(WordNerd.GAME_SCENE_WIDTH, WordNerd.GAME_SCENE_HEIGHT / 3);
		gameComboBox.setPrefWidth(200);
		searchTextField.setPrefWidth(300);
		searchHBox.setAlignment(Pos.CENTER);
		searchVBox.setAlignment(Pos.CENTER);
		searchHBox.setSpacing(10);
		
		setupSearchTableView();
		
		WordNerd.root.setPadding(new Insets(10, 10, 10, 10));
		WordNerd.root.setTop(searchVBox);
		WordNerd.root.setCenter(searchTableView);
		WordNerd.root.setBottom(WordNerd.exitButton);
	}

	void setupSearchTableView() {
		
		//adding column headings to the searchTable
		TableColumn<Score,String> gCol = new TableColumn<>("Game");
		
		TableColumn<Score,String> wCol = new TableColumn<>("Word");
		wCol.setCellValueFactory(new PropertyValueFactory<Score,String>("puzzleWord"));
		TableColumn<Score,Integer> tCol = new TableColumn<>("Time(sec)");
		tCol.setCellValueFactory(new PropertyValueFactory<Score,Integer>("timeStamp"));
		TableColumn<Score,String> sCol = new TableColumn<>("Score");
		
		searchTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		searchTableView.setPrefSize(300, 100);
        searchTableView.getColumns().setAll(gCol,wCol,tCol,sCol);
        //call back to replace gameid's with game names
        gameNameCallBack = new Callback<CellDataFeatures<Score, String>,ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(CellDataFeatures<Score, String> arg0) {
				// TODO Auto-generated method stub
				if(arg0.getValue().getGameId()==0) {
					return (ObservableValue<String>) new SimpleStringProperty("Hangman");
				}else {
					return (ObservableValue<String>) new SimpleStringProperty("Twister");	
				}
			}
        	
        };

        gCol.setCellValueFactory(gameNameCallBack);
        //call back to replace score with formatting of two decimal places
        scoreCallBack = new Callback<CellDataFeatures<Score, String>,ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(CellDataFeatures<Score, String> arg0) {
				// TODO Auto-generated method stub
				Float currentScore = arg0.getValue().getScore();
				String formattedString = String.format("%.02f", currentScore);
				return (ObservableValue<String>) new SimpleStringProperty(formattedString);
				
			}

			
        	
        };
        sCol.setCellValueFactory(scoreCallBack);
	
}
}



