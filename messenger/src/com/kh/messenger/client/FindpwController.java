package com.kh.messenger.client;


import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

import com.kh.messenger.common.Command;
import com.kh.messenger.common.MemberDTO;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class FindpwController implements Initializable {

	@FXML	private Button findPwBtn, findPwCloseBtn;
	@FXML	private TextField tel;
	@FXML	private TextField id;
	@FXML	private DatePicker birth;
	@FXML	private PasswordField pw;
	@FXML	private Label msg2;

	private Protocol protocol;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

			}

	public void handleFindpw(ActionEvent e) {
	
		
		System.out.println("핸들파인드아이디");	
		System.out.println("전화번호:" + tel.getText() );
		System.out.println("생년월일:"+birth.getValue().toString());
		System.out.println("아이디:"+id.getText());
		protocol = new Protocol(this);
		protocol.findPw(id.getText(),tel.getText(),birth.getValue().toString());
		
	}

	public void findPw(Command command) {
		String pw =(String)command.getResults().elementAt(0);
		
		System.out.println(command.getResults().elementAt(0));
		
		Platform.runLater(()->{
			if(pw != null) {
				System.out.println("pw찾기 성공");
				msg2.setText("[password] :" + pw);
				protocol.stopClient();
			}else {
				msg2.setText("찾고자하는 정보가 없음");
				protocol.stopClient();
			}
				
			
		});
		
	}
		
	}

