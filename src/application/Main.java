package application;

	
import java.sql.Connection;
import java.sql.PreparedStatement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Main extends Application {
	Connection conn;
	PreparedStatement pst = null;
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("Sample.fxml"));        
	        Scene scene = new Scene(root,1400,770);
	        
	            Text diagnosisCategoryText = new Text("Diagnosis Category");
	            diagnosisCategoryText.setX(14);
	            diagnosisCategoryText.setY(22);
	            root.getChildren().add(diagnosisCategoryText);
	        
	            Text alternativeLanguageText = new Text("Alternative Language");
	            alternativeLanguageText.setX(195);
	            alternativeLanguageText.setY(22);
	            root.getChildren().add(alternativeLanguageText);
	            
	            Text insranceText = new Text("Insurance");
	            insranceText.setX(377);
	            insranceText.setY(22);
	            root.getChildren().add(insranceText);
	            
	            Text zipCodeText = new Text("Zip Code");
	            zipCodeText.setX(559);
	            zipCodeText.setY(22);
	            root.getChildren().add(zipCodeText);
	        
	        
			primaryStage.setScene(scene);
			primaryStage.setTitle("Provider Search Engine v3");
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	public static void main(String[] args) {
	    
		launch(args);
	}
}
