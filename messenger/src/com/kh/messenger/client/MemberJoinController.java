package com.kh.messenger.client;

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
			
			//필수입력값 : 아이디,비밀번호,닉네임,성별,생년월일,전화번호
			
			//아이디: 체크/아이디형식/db조회
			if(id.getText().trim().equals("")) {
				msg.setText("아이디를 입력바랍니다!");
				id.requestFocus();
				return;
			}
			boolean isID = Pattern.matches("\\w+@\\w+\\.\\w+(\\.\\w+)?", id.getText());
			if(!isID) {
				msg.setText("아이디의 이메일 형식이 올바르지 않습니다.");
				id.requestFocus();
				return;
			}	
			if(Member.getInstance().containsKey(id.getText())) {
				msg.setText("회원목록에 없습니다.");
				id.requestFocus();
				return;
			}
			
			//pw:비밀번호,비밀번호확인
			if(pw.getText().trim().equals("")||
					pw.getText().trim().length()<4) {
				msg.setText("비밀번호를 입력바랍니다 (4자리 이상)");
				pw.requestFocus();
				return;
			}
			if(pwchk.getText().trim().equals("")) {
				msg.setText("비밀번호를 입력바랍니다 (4자리 이상).");
				pwchk.requestFocus();
				return;
			}
			if(!pw.getText().trim().equals(pwchk.getText().trim())){
				msg.setText("비밀번호와 일치하지 않습니다.");
				pwchk.requestFocus();
				return;
			}
			
			//전화번호,형식
			if(tel.getText().trim().equals("")) {
				msg.setText("전화번호를 입력바랍니다.");
				tel.requestFocus();
				return;
			}
							
			boolean istel = 
					Pattern.matches("^01([0|1|6|7|8|9]?)\\d{8,9}", tel.getText());
			if(!istel) {
				msg.setText("휴대폰번호가 형식과 맞질 않습니다. 010xxxxxxxx(-생략)");
				tel.requestFocus();
				return;
			}
			
			//닉네임,생일
			if(nickName.getText().trim().equals("")||
					nickName.getText().trim().length()<2) {
				msg.setText("닉네임을 입력해주십시오 (2자리이상)");
				nickName.requestFocus();
				return;
			}
			if(birth.getValue()==null) {
				msg.setText("생일을 입력해주십시오.");
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

		//취소시 경로
		public void memberCancel(Event e) {
			
		stage.close();
		protocol.stopClient();
			
			
		}


			// 회원가입 결과
			public void memberJoin(Command command) {
						
							boolean flag = ((Boolean) command.getResults().elementAt(0)).booleanValue();
							if (flag) {
								Platform.runLater(() -> {
									DialogUtil.dialog(AlertType.INFORMATION, "알림", "회원가입", "회원가입 성공");
									protocol.stopClient();
								});

							} else {
								Platform.runLater(() -> {
									DialogUtil.dialog(AlertType.ERROR, "알림", "회원가입", "회원가입 실패");
									protocol.stopClient();
								});
							}
				
			}

		}
		
	

	

