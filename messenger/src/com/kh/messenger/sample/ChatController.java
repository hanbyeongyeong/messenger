package com.kh.messenger.sample;



import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class ChatController implements Initializable {

    @FXML private TextFlow tfDisplay;
    @FXML private ColorPicker cpColor;
    @FXML private ColorPicker cpColor1;
    @FXML private TextArea taMsg;
    @FXML private Button btnSend;
    @FXML private ScrollPane spRoll;
    
    private Color textColor;
	
	
    ChatController(String msg){
		System.out.println("생성자 호출됨: "+ msg);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		spRoll.vvalueProperty().bind(tfDisplay.heightProperty());
		
		textColor =Color.BLACK;
		cpColor.setValue(Color.BLACK);
		
		cpColor.setOnAction(e->{
			textColor = cpColor.getValue();
		});
		
//		cpColor1.setOnAction(e->{
//			TextFlow = cpColor1.getValue();
//		});
		
		tfDisplay.setLineSpacing(2);//줄간격
		tfDisplay.setTextAlignment(TextAlignment.RIGHT);//텍스트 정렬
		
		btnSend.setOnAction(e->{
			Text text= new Text();
			String msg =  taMsg.getText()+"\n";
			text.setText(msg);
			text.setFont(Font.font("Verdana",FontWeight.BOLD,14));
			text.setFill(textColor);
		tfDisplay.getChildren().add(text);	
		taMsg.clear();
		taMsg.requestFocus();
						
		});
		
		taMsg.setOnKeyPressed(event->{
			
			if(event.getCode().equals(KeyCode.ENTER)) {
				btnSend.fire();
				
			}
		});		
		
		taMsg.setOnKeyReleased(event->{
			
			if(event.getCode().equals(KeyCode.ENTER)) {
				taMsg.clear();
				taMsg.requestFocus();
			}
		});		
		
	}

}
