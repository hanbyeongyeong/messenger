package com.kh.messenger.client2;


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

		login.setOnAction(event -> {

			
			System.out.println("로그인버튼클릭!!");
			System.out.println("아이디:" + id.getText());
			System.out.println("비밀번호:" + pw.getText());
			login.setDisable(true);
			loginId = id.getText();
			protocol = new Protocol(this);
			
//			if(protocol.isSocketClosed()) {
//				login.setDisable(false);
//			}
			
			protocol.isMember(id.getText(), pw.getText());
			Platform.setImplicitExit(false);

//			//회원판단
//			//1)회원: 로그인성공 메시지 콘솔출력 
//			if(Member.getInstance().containsKey(id.getText())) {
//				memPw = Member.getInstance().get(id.getText()).getPw();
//				
//				if(memPw.equals(pw.getText())) {
//					System.out.println("Log-in");
//					msg.setText("Log-In");
//	
//					primaryStage.hide();
//					
//					Stage dialog = new Stage(StageStyle.DECORATED);
//					dialog.initModality(Modality.WINDOW_MODAL);
//					dialog.initOwner(primaryStage);
//					dialog.setTitle("메신저 메인");
//					
//					FXMLLoader loader = new FXMLLoader(getClass().getResource("messengerMain.fxml"));
//					try {
//						messengerMainWindow =loader.load();
//					}catch (IOException e) { }
//					
//					MessengerMainController controller = (MessengerMainController)(loader.getController());
//					controller.setDialog(primaryStage,dialog);
//					Scene scene = new Scene(messengerMainWindow);
//					
//					dialog.setScene(scene);
//					dialog.setResizable(false);
//					dialog.show();		
//					
//							
//				}else {
//					//회원이면서 비밀번호가 맞는지 판단
//					// 3)비밀번호 오류: 비밀번호가 맞지않습니다. 콘솔출력
//					System.out.println("false password");
//					msg.setText("False Password");
//					pw.clear(); pw.requestFocus();
//				}
//			}else {
//			//2)비회원: 회원정보가 없습니다 콘솔출력
//				System.out.println("회원정보가없습니다.");
//				msg.setText("Identity cannot found");
//				id.clear(); pw.clear();
//				id.requestFocus();
//			}

		});

		cancle.setOnAction(event -> {
			System.out.println("취소버튼클릭");
			msg.setText("GOOD-BYE");
			id.clear();
			pw.clear();
			id.requestFocus();// 텍스트 필드로 포커스 이동
		});

	}

	public void searchId(Event e) throws IOException {
		Stage dialog = new Stage(StageStyle.UTILITY);
		dialog.initModality(Modality.WINDOW_MODAL);

		dialog.initOwner(primaryStage);
		dialog.setTitle("아이디 조회");


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
		dialog.setTitle("비밀번호 조회");

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
		// case1)부모윈도우 객체 참조하는 방법:
		// 부모윈도우의 컨트롤 또는 컨테이너로 scene정보를 얻어와 부모윈도우 참조
		// dialog.initOwner(login.getScene().getWindow());
		dialog.initOwner(primaryStage);
		// case2)메인 윈도우에서
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
		
		//중복로긴체크
		if(status == -1) {
			String ip = (String)command.getResults().elementAt(0);
			Platform.runLater(()->{
			DialogUtil.dialog(AlertType.ERROR, "중복로그인", "로그인 된 아이디입니다."+"\n [사용중인 ip] : "+ip, null);
			});
			return;
		}
				
		boolean flag = ((Boolean) command.getResults().elementAt(0)).booleanValue();
		// 정상로그인
		if (flag) {
			Platform.runLater(() -> {

				Stage dialog = new Stage(StageStyle.UNIFIED);
//				dialog.initModality(Modality.WINDOW_MODAL);
//				dialog.initOwner(primaryStage);
				dialog.setTitle("메신저 메인2");

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

//				dialog.setOnCloseRequest(e->{
//					boolean answer = false;
//					answer = ConfirmDialog.display("확인", "로그아웃 하시겠습니까?");
//					if(answer) {
//						if(protocol != null) {
//							protocol.stopClient();
//						}
//						dialog.close();
//						primaryStage.show();
//					}else {
//						e.consume();
//					}
//				});					

			});
			// 유효한 로그인이 아닌경우
		} else {
			Platform.runLater(() -> {
				DialogUtil.dialog(AlertType.WARNING, "알림", "계정확인", "등록되지 않은 id혹은 pw입니다.");
				login.setDisable(false);
			});
			protocol.stopClient();
		}
	}

	public void loginBtnDisable(boolean status) {
		login.setDisable(status);
		
	}



}
