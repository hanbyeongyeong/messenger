package com.kh.messenger.common;



import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmDialog {

	static boolean answer;
	public static boolean display(String title, String message) {
		
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMaxWidth(250);
		
		Label label = new Label();
		label.setText(message);
		
		Button yesButton = new Button("예");
		Button noButton = new Button("아니오");
		
		
		yesButton.setOnAction(e->{
			answer = true;
			window.close();
		});
		
		noButton.setOnAction(e->{
			answer = false;
			window.close();
		});
		
		VBox layout = new VBox(20);
		HBox hBox = new HBox(10);
		hBox.getChildren().addAll(label,yesButton,noButton);
		layout.getChildren().addAll(label,hBox);
		
		
		layout.setAlignment(Pos.CENTER);
		hBox.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout,200,150);
		window.setScene(scene);
		window.showAndWait();
		return answer;
		
	}
}
