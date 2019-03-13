package com.kh.messenger.client2;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.kh.messenger.common.Command;
import com.kh.messenger.common.MemberDTO;
import com.kh.messenger.sample.DialogUtil;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.*;


public class MemberJoinController implements Initializable {

	@FXML private TextField id;
	@FXML private PasswordField pw;
	@FXML private PasswordField pwchk;
	@FXML private TextField tel;
	@FXML private TextField nickName;
	@FXML private ToggleGroup sex;
	@FXML private RadioButton sex1;
	@FXML private RadioButton sex2;
	@FXML private ComboBox<String> region;
	@FXML private DatePicker birth;
	@FXML private Label msg;
	
	//Map<String, MemberDTO> map = new HashMap<>();
	MemberDTO memberDTO = new MemberDTO();
	Stage stage = null;
	Protocol protocol;
	
	void setDialog(Stage stage) {
		this.stage = stage;
	}
	
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	sex.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
		
		@Override
		public void changed(ObservableValue<? extends Toggle> observable,
				Toggle oldValue, Toggle newValue) {
			memberDTO.setGender(newValue.getUserData().toString());
		}
	});
	
	}
	
		public void memberjoin(Event e) {
			
			//��ȿ��üũ
			
			//�ʼ��Է°�
					
			if(id.getText().trim().equals("")) {
				msg.setText("���̵� �Է¹ٶ��ϴ�.");
				id.requestFocus();
				return;
			}
			boolean isID = Pattern.matches("\\w+@\\w+\\.\\w+(\\.\\w+)?", id.getText());
			if(!isID) {
				msg.setText("���̵� �̸��� ���İ� �����ʽ��ϴ�.");
				id.requestFocus();
				return;
			}	
			if(Member.getInstance().containsKey(id.getText())) {
				msg.setText("�ߺ��� ���̵� �Դϴ�");
				id.requestFocus();
				return;
			}
			if(pw.getText().trim().equals("")||
					pw.getText().trim().length()<4) {
				msg.setText("��й�ȣ�� �Է¹ٶ��ϴ�.(4�ڸ��̻�)");
				pw.requestFocus();
				return;
			}
			if(pwchk.getText().trim().equals("")) {
				msg.setText("��й�ȣȮ�� �Է¹ٶ��ϴ�.");
				pwchk.requestFocus();
				return;
			}
			if(!pw.getText().trim().equals(pwchk.getText().trim())){
				msg.setText("��й�ȣ�� ����ġ�մϴ�");
				pwchk.requestFocus();
				return;
			}
			
			if(tel.getText().trim().equals("")) {
				msg.setText("�ڵ�����ȣ�� �Է¹ٶ��ϴ� ");
				tel.requestFocus();
				return;
			}
							
			boolean istel = 
					Pattern.matches("^01([0|1|6|7|8|9]?)\\d{8,9}", tel.getText());
			if(!istel) {
				msg.setText("�ùٸ� ��ȣ�� �Է��� �ֽʽÿ�");
				tel.requestFocus();
				return;
			}
			
			
			if(nickName.getText().trim().equals("")||
					nickName.getText().trim().length()<2) {
				msg.setText("�г����� �Է¹ٶ��ϴ�.(2�����̻�)");
				nickName.requestFocus();
				return;
			}
			if(birth.getValue()==null) {
				msg.setText("��������� �Է¹ٶ��ϴ�.");
				birth.requestFocus();
				return;
			}
			
			
			

			String key =id.getText();		
			memberDTO.setId(key);
			memberDTO.setPw(pw.getText());
			memberDTO.setTel(tel.getText());
			memberDTO.setNickname(nickName.getText());
			memberDTO.setRegion((String)region.getValue());
			memberDTO.setBirth(birth.getValue().toString());
			
			
			

			
			System.out.println(memberDTO);
			protocol=new Protocol(this);
			protocol.memberJoin(memberDTO);
			stage.close();
			
			
		}

		
		public void memberCancel(Event e) {
			
		stage.close();
		protocol.stopClient();
			
			
		}


			//ȸ�����԰��
			public void memberJoin(Command command) {
						
							boolean flag = ((Boolean) command.getResults().elementAt(0)).booleanValue();
							if (flag) {
								Platform.runLater(() -> {
									DialogUtil.dialog(AlertType.INFORMATION, "�˸�", "ȸ������", "ȸ�����Լ���");
									protocol.stopClient();
								});

							} else {
								Platform.runLater(() -> {
									DialogUtil.dialog(AlertType.ERROR, "�˸�", "ȸ������", "���Խ��� \n (�̹� �����ϴ� ���̵��Դϴ�.)");
									protocol.stopClient();
								});
							}
				
			}

		}
		
	

	

