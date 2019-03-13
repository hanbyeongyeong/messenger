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
	
		System.out.println("�ڵ����ε���̵�");	
		System.out.println("��ȭ��ȣ:" + tel.getText() );
		System.out.println("�������:"+birth.getValue().toString());
		
		protocol = new Protocol(this);
		protocol.findId(tel.getText(),birth.getValue().toString());
		
		if(tel.getText().trim().equals("")) {
			msg2.setText("��ȭ��ȣ�� �Է¹ٶ��ϴ�.");
			tel.requestFocus();
			return;
		}
//		boolean isID = Pattern.matches("\\w+@\\w+\\.\\w+(\\.\\w+)?", id.getText());
//		if(!isID) {
//			msg.setText("���̵� �̸��� ���İ� �����ʽ��ϴ�.");
//			id.requestFocus();
//			return;
//		}	
		
	}
		
	public void findId(Command command) {
		
		String id =(String)command.getResults().elementAt(0);
			
		System.out.println(command.getResults().elementAt(0));
		
		Platform.runLater(()->{
			
			if(id != null) {
				System.out.println("���̵�ã�� ����");
				msg2.setText("[I D] :" + id);
				protocol.stopClient();
				
			}else {
				msg2.setText("ã�����ϴ� ������ ����");
				protocol.stopClient();
				
			}
				
			
		});
		
	}
}
