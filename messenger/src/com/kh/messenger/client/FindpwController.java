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
	
		
		System.out.println("�ڵ����ε���̵�");	
		System.out.println("��ȭ��ȣ:" + tel.getText() );
		System.out.println("�������:"+birth.getValue().toString());
		System.out.println("���̵�:"+id.getText());
		protocol = new Protocol(this);
		protocol.findPw(id.getText(),tel.getText(),birth.getValue().toString());
		
	}

	public void findPw(Command command) {
		String pw =(String)command.getResults().elementAt(0);
		
		System.out.println(command.getResults().elementAt(0));
		
		Platform.runLater(()->{
			if(pw != null) {
				System.out.println("pwã�� ����");
				msg2.setText("[password] :" + pw);
				protocol.stopClient();
			}else {
				msg2.setText("ã�����ϴ� ������ ����");
				protocol.stopClient();
			}
				
			
		});
		
	}
		
	}

