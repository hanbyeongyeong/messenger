package com.kh.messenger.common;

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

	public static void alert() {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("정보");
		alert.setHeaderText("친구정보");
		alert.setContentText("부산/남/23");
		;
		alert.showAndWait();

	}

	public static void alert2() {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("정보");
		alert.setHeaderText("친구정보");
		alert.setContentText("남산/남/22");
		;
		alert.showAndWait();

	}

	public static void alert3() {

		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("정보");
		alert.setHeaderText("친구정보");
		alert.setContentText("양산/남/21");
		;
		alert.showAndWait();

	}

	public static void alert4() {

		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("정보");
		alert.setHeaderText("친구정보");
		alert.setContentText("울산/남/20");
		;
		alert.showAndWait();

	}

	public static void alert5() {

		Alert alert = new Alert(AlertType.NONE,"test??",
								ButtonType.YES,ButtonType.NO,ButtonType.CLOSE,
								ButtonType.APPLY,ButtonType.CANCEL,
								ButtonType.FINISH,ButtonType.NEXT,ButtonType.PREVIOUS);
								
		alert.setTitle("정보");
		alert.setHeaderText("친구정보");
		alert.setContentText("울산/여/20");
		
		Optional<ButtonType> result = alert.showAndWait();
		
		if(result.get() == ButtonType.YES) {
			System.out.println("yes");
		}else if(result.get()==ButtonType.NO){
			System.out.println("no");
		}else if(result.get()==ButtonType.CLOSE){
			alert.close();
		}
		
	}

	public static void alert6() {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("정보");
		alert.setHeaderText("친구정보");
		alert.setContentText("울산/남/20");
		;

		ButtonType buttonType1 = new ButtonType("1");
		ButtonType buttonType2 = new ButtonType("2");
		ButtonType buttonType3 = new ButtonType("3");
		ButtonType buttonType4 = new ButtonType("4");

		alert.getButtonTypes().
		setAll(buttonType1, buttonType2, buttonType3, buttonType4);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonType1) {
			System.out.println("bt1 click");
		} else if (result.get() == buttonType2) {
			System.out.println("bt2 click");
		} else if (result.get() == buttonType3) {
			System.out.println("bt3 click");
		} else if (result.get() == buttonType4) {
			System.out.println("bt4 click");
		} else {
			System.err.println("error");
		}
	}
	
	public static String textInputDialog
		(String defaultText, String title ,String headerText, String contentText){
		TextInputDialog dialog = new TextInputDialog("친구아이디입력!");
		dialog.setTitle("정보");
		dialog.setHeaderText("친구정보");
		dialog.setContentText("울산/남/20");
		dialog.getDialogPane().setStyle(
							"-fx-background-color: grey;"
							+"-fx-max-width:250;-fx-max-height:200;"
							+"-fx-pref-width:250;-fx-pref-height:200;"
				
							
				);
		
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {
			return result.get();	
		}else {
			return "";
		}
	}
	public static void choiceDialog() {
		
		List<String> choices = new ArrayList<>();
		choices.add("친구1");
		choices.add("친구2");
		choices.add("친구3");
		choices.add("친구4");
		
		ChoiceDialog<String> dialog =
				 new ChoiceDialog<String>("친구3",choices);
		
		dialog.setTitle("정보");
		dialog.setHeaderText("친구정보");
		dialog.setContentText("울산/남/20");
		dialog.getDialogPane().setStyle(
							"-fx-background-color: grey;"
							+"-fx-max-width:250;-fx-max-height:200;"
							+"-fx-pref-width:250;-fx-pref-height:200;"
							);
		
		Stage stage = (Stage)dialog.getDialogPane()
									.getScene()
									.getWindow();
			stage.getIcons().add(
					new Image(DialogUtil.class
							.getResource("images/icon.jpg")
							.toString())
					);
			
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {
			System.out.println("친구아이디:"+ result.get());
		}
	}
}
