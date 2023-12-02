package application;
	
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	private double eixoX = 0;
	private double eixoY = 0;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("PlayerMusica.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.show();
			
			
			root.setOnMousePressed(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent evento) {
					eixoX = evento.getSceneX();
					eixoY = evento.getSceneY();
					
				}
				
			});
			
			root.setOnMouseDragged(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent evento) {
					primaryStage.setX(evento.getScreenX() - eixoX);
					primaryStage.setY(evento.getScreenY() - eixoY);
				}
			});
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
