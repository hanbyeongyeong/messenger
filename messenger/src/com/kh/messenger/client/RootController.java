package com.kh.messenger.client;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.kh.messenger.common.Command;
import com.kh.messenger.sample.ConfirmDialog;
import com.kh.messenger.common.MemberDTO;
import com.kh.messenger.sample.DialogUtil;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RootController implements Initializable {

	@FXML
	private Button cancle, login;
	@FXML
	private TextField id;
	@FXML
	private PasswordField pw;
	@FXML
	private Label msg;


	Stage primaryStage;

	Parent memberJoinWindow;
	
	Parent messengerMainWindow;
	
	Protocol protocol;
	String loginId;

	public RootController() {
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		//로그인 버튼 이벤트
		login.setOnAction(event -> {

			
			System.out.println("로그인버튼출력!!");
			System.out.println("아이디:" + id.getText());
			System.out.println("비밀번호:" + pw.getText());
			login.setDisable(true);
			loginId = id.getText();
			protocol = new Protocol(this);

			
			protocol.isMember(id.getText(), pw.getText());
			Platform.setImplicitExit(false);

		});

		cancle.setOnAction(event -> {
			System.out.println("취소버튼클릭");
			msg.setText("GOOD-BYE");
			id.clear();
			pw.clear();
			id.requestFocus();// �ؽ�Ʈ �ʵ�� ��Ŀ�� �̵�
		});

	}

	public void searchId(Event e) throws IOException {
		Stage dialog = new Stage(StageStyle.UTILITY);
		dialog.initModality(Modality.WINDOW_MODAL);

		dialog.initOwner(primaryStage);
		dialog.setTitle("아이디찾기");


		Parent parent = FXMLLoader.load(getClass().getResource("findid.fxml"));

//		Button findIdBtn = (Button)parent.lookup("#findIdBtn");
//		findIdBtn.setOnAction(event->dialog.close());

		Button findIdCloseBtn = (Button) parent.lookup("#findIdCloseBtn");
		findIdCloseBtn.setOnAction(event -> dialog.close());
		
		Scene scene = new Scene(parent);
		dialog.setScene(scene);
		dialog.setResizable(false);
		dialog.show();
		
		
	}

	public void searchPw(Event e) throws IOException {
		Stage dialog = new Stage(StageStyle.DECORATED);
		dialog.initModality(Modality.WINDOW_MODAL);

		dialog.initOwner(primaryStage);
		dialog.setTitle("비밀번호찾기");

		Parent parent = FXMLLoader.load(getClass().getResource("findpw.fxml"));

//		Button findPwBtn =(Button)parent.lookup("#findPwBtn");
//		findPwBtn.setOnAction(event->dialog.close());

		Button findPwCloseBtn = (Button) parent.lookup("#findPwCloseBtn");
		findPwCloseBtn.setOnAction(event -> dialog.close());

		Scene scene = new Scene(parent);
		dialog.setScene(scene);
		dialog.setResizable(false);
		dialog.show();
	}

	public void doMemjoin(Event e) throws IOException {
		Stage dialog = new Stage(StageStyle.DECORATED);
		dialog.initModality(Modality.WINDOW_MODAL);
		// case1)�θ������� ��ü �����ϴ� ���:
		// �θ��������� ��Ʈ�� �Ǵ� �����̳ʷ� scene������ ���� �θ������� ����
		// dialog.initOwner(login.getScene().getWindow());
		dialog.initOwner(primaryStage);
		// case2)���� �����쿡��
		dialog.setTitle("회원가입");

		FXMLLoader loader = new FXMLLoader(getClass().getResource("memjoin.fxml"));
		memberJoinWindow = loader.load();

		MemberJoinController controller = (MemberJoinController) (loader.getController());
		controller.setDialog(dialog);

		Scene scene = new Scene(memberJoinWindow);

		dialog.setScene(scene);
		dialog.setResizable(false);
		dialog.show();
		
		
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;

	}

	public void doLogin(Command command) {
		
		int status = command.getResults().getStatus();
		//로그인 버튼 활성화
		Platform.runLater(()->{login.setDisable(false);});
		
		//중복로긴
		if(status == -1) {
			String ip = (String)command.getResults().elementAt(0);
			Platform.runLater(()->{
			DialogUtil.dialog(AlertType.ERROR, "�ߺ��α���", "�α��� �� ���̵��Դϴ�."+"\n [������� ip] : "+ip, null);
			});
			return;
		}
				
		boolean flag = ((Boolean) command.getResults().elementAt(0)).booleanValue();
		//정상 로그인
		if (flag) {
			Platform.runLater(() -> {

				Stage dialog = new Stage(StageStyle.UNIFIED);
//				dialog.initModality(Modality.WINDOW_MODAL);
//				dialog.initOwner(primaryStage);
				dialog.setTitle("�޽��� ����");

				FXMLLoader loader = new FXMLLoader(getClass().getResource("messengerMain.fxml"));
				
				try {
					messengerMainWindow = loader.load();
//					primaryStage.hide();
					
				} catch (IOException e) {
					
				}

				MessengerMainController controller = (MessengerMainController) (loader.getController());
				controller.setDialog(primaryStage, dialog);
				controller.setInitial(loginId,protocol);
				protocol.setMessengerMainController(controller);
				Scene scene = new Scene(messengerMainWindow);

				dialog.setScene(scene);
				dialog.setResizable(false);
				dialog.show();
				login.setDisable(false);
				primaryStage.close();

			});
			// 유효한 로그인 아닌경우	
		} else {
			Platform.runLater(() -> {
				DialogUtil.dialog(AlertType.ERROR, "알림", 
						"계정확인", "등록되지않은 ID 혹은 \n Password가 올바르지않습니다.");	
				login.setDisable(false);
			});
			protocol.stopClient();
		}
	}

	public void loginBtnDisable(boolean status) {
		login.setDisable(status);
		
	}



}
