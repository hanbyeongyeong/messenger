package com.kh.messenger.client2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import oracle.jdbc.proxy.annotation.GetCreator;

public class DialogUtil {

	public static Optional<ButtonType> dialog(AlertType type, String title, String headerText, String contentText) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.getDialogPane().setStyle("-fx-background-color: grey;"
				+ "-fx-max-width:250;-fx-max-height:200;"
				+ "-fx-pref-width:250;-fx-pref-height:200;");

		Optional<ButtonType> optional = alert.showAndWait();
		return optional;
	}
	//alerttype.none 사용자정의 추가용
	public static Optional<ButtonType> dialog
	(String title, String headerText, String contentText
			,ButtonType...btype) {
		
		Alert alert = new Alert(AlertType.NONE,contentText,btype);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);

		Optional<ButtonType> optional = alert.showAndWait();
		return optional;
	}
	//	b[4] confirmation 사용자 정의 버튼 추가용
	public static Optional<ButtonType> dialog
	( String title, String headerText,
			String contentText,List<ButtonType> list) {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);

		alert.getButtonTypes().setAll(list);
		Optional<ButtonType> optional = alert.showAndWait();
		return optional;
	}
	

	// b[2] textinputdialog 사용자정의
	public static String textInputDialog
	(String defaultText, String title ,String headerText, String contentText) {
		TextInputDialog dialog = new TextInputDialog(defaultText);
		dialog.setTitle(title);
		dialog.setHeaderText(headerText);
		dialog.setContentText(contentText);
		dialog.getDialogPane().setStyle("-fx-background-color: grey;"
				+ "-fx-max-width:250;-fx-max-height:200;"
				+ "-fx-pref-width:250;-fx-pref-height:200;"

		);

		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {
			return result.get();	
		}else {
			return "";
		}
	}
	
	// b[3] choiceDialog 사용자정의
	public static String choiceDialog
	(List<String> choices, String defaultText,
			String title,String headerText, String contentText) {

		

		ChoiceDialog<String> dialog = new ChoiceDialog<String>(defaultText, choices);

		dialog.setTitle(title);
		dialog.setHeaderText(headerText);
		dialog.setContentText(contentText);
		dialog.getDialogPane().setStyle("-fx-background-color: grey;" + "-fx-max-width:250;-fx-max-height:200;"
				+ "-fx-pref-width:250;-fx-pref-height:200;");

		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(DialogUtil.class.getResource("images/icon.jpg").toString()));

		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {
		return result.get();
		}else {
			return null;
		}
		}
	}


