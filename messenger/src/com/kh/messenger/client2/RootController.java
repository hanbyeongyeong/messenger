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

			
			System.out.println("�α��ι�ưŬ��!!");
			System.out.println("���̵�:" + id.getText());
			System.out.println("��й�ȣ:" + pw.getText());
			login.setDisable(true);
			loginId = id.getText();
			protocol = new Protocol(this);
			
//			if(protocol.isSocketClosed()) {
//				login.setDisable(false);
//			}
			
			protocol.isMember(id.getText(), pw.getText());
			Platform.setImplicitExit(false);

//			//ȸ���Ǵ�
//			//1)ȸ��: �α��μ��� �޽��� �ܼ���� 
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
//					dialog.setTitle("�޽��� ����");
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
//					//ȸ���̸鼭 ��й�ȣ�� �´��� �Ǵ�
//					// 3)��й�ȣ ����: ��й�ȣ�� �����ʽ��ϴ�. �ܼ����
//					System.out.println("false password");
//					msg.setText("False Password");
//					pw.clear(); pw.requestFocus();
//				}
//			}else {
//			//2)��ȸ��: ȸ�������� �����ϴ� �ܼ����
//				System.out.println("ȸ�������������ϴ�.");
//				msg.setText("Identity cannot found");
//				id.clear(); pw.clear();
//				id.requestFocus();
//			}

		});

		cancle.setOnAction(event -> {
			System.out.println("��ҹ�ưŬ��");
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
		dialog.setTitle("���̵� ��ȸ");


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
		dialog.setTitle("��й�ȣ ��ȸ");

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
		dialog.setTitle("ȸ������");

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
		//�α��� ��ư Ȱ��ȭ
		Platform.runLater(()->{login.setDisable(false);});
		
		//�ߺ��α�üũ
		if(status == -1) {
			String ip = (String)command.getResults().elementAt(0);
			Platform.runLater(()->{
			DialogUtil.dialog(AlertType.ERROR, "�ߺ��α���", "�α��� �� ���̵��Դϴ�."+"\n [������� ip] : "+ip, null);
			});
			return;
		}
				
		boolean flag = ((Boolean) command.getResults().elementAt(0)).booleanValue();
		// ����α���
		if (flag) {
			Platform.runLater(() -> {

				Stage dialog = new Stage(StageStyle.UNIFIED);
//				dialog.initModality(Modality.WINDOW_MODAL);
//				dialog.initOwner(primaryStage);
				dialog.setTitle("�޽��� ����2");

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
//					answer = ConfirmDialog.display("Ȯ��", "�α׾ƿ� �Ͻðڽ��ϱ�?");
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
			// ��ȿ�� �α����� �ƴѰ��
		} else {
			Platform.runLater(() -> {
				DialogUtil.dialog(AlertType.WARNING, "�˸�", "����Ȯ��", "��ϵ��� ���� idȤ�� pw�Դϴ�.");
				login.setDisable(false);
			});
			protocol.stopClient();
		}
	}

	public void loginBtnDisable(boolean status) {
		login.setDisable(status);
		
	}



}
