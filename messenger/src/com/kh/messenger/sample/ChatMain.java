package com.kh.messenger.sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ChatMain extends Application {


	@Override
	public void start(Stage primaryStage) throws Exception {
		
		
		setUserAgentStylesheet(STYLESHEET_CASPIAN);
//		System.out.println(getClass().getResource("chat.fxml"));
		FXMLLoader loader = new FXMLLoader(getClass().getResource("chat.fxml"));
		ChatController chatController = new ChatController("¥Î»≠√¢");
		loader.setController(chatController);
		
		Parent p =loader.load();
		Scene scene = new Scene(p);
		scene.setFill(Color.BLACK);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		Application.launch(args);
		
	}

}
