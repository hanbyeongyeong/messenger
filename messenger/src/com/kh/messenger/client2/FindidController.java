package com.kh.messenger.client2;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.kh.messenger.common.Command;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class FindidController implements Initializable {

	@FXML	private Button findIdBtn, findIdCloseBtn;
	@FXML	private TextField tel;
	@FXML	private PasswordField pw;
	@FXML	private Label msg2;
	@FXML	private DatePicker birth;
	
	private Protocol protocol;



	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	
	
	public void handleFindid(ActionEvent e) {
	
		System.out.println("핸들파인드아이디");	
		System.out.println("전화번호:" + tel.getText() );
		System.out.println("생년월일:"+birth.getValue().toString());
		
		protocol = new Protocol(this);
		protocol.findId(tel.getText(),birth.getValue().toString());
		
		if(tel.getText().trim().equals("")) {
			msg2.setText("전화번호를 입력바랍니다.");
			tel.requestFocus();
			return;
		}
//		boolean isID = Pattern.matches("\\w+@\\w+\\.\\w+(\\.\\w+)?", id.getText());
//		if(!isID) {
//			msg.setText("아이디가 이메일 형식과 맞질않습니다.");
//			id.requestFocus();
//			return;
//		}	
		
	}
		
	public void findId(Command command) {
		
		String id =(String)command.getResults().elementAt(0);
			
		System.out.println(command.getResults().elementAt(0));
		
		Platform.runLater(()->{
			
			if(id != null) {
				System.out.println("아이디찾기 성공");
				msg2.setText("[I D] :" + id);
				protocol.stopClient();
				
			}else {
				msg2.setText("찾고자하는 정보가 없음");
				protocol.stopClient();
				
			}
				
			
		});
		
	}
}
