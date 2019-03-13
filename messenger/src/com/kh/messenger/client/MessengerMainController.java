package com.kh.messenger.client;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.kh.messenger.common.Command;
import com.kh.messenger.common.CommonUtil;
import com.kh.messenger.client.DialogUtil;
import com.kh.messenger.common.MemberDTO;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeTableView.TreeTableViewSelectionModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MessengerMainController implements Initializable {

	@FXML
	private TreeTableView<Friend> ttr;

	@FXML
	private TreeTableColumn<Friend, String> ttcNickName;

	@FXML
	private TreeTableColumn<Friend, String> ttcEmail;

	@FXML
	private TreeTableColumn<Friend, String> ttcGender;

	@FXML
	private TreeTableColumn<Friend, String> ttcAge;

	@FXML
	private TreeTableColumn<Friend, String> ttcRegion;

	@FXML
	private TreeTableColumn<Friend, String> ttcTel;
	
	@FXML 
	private Label lbLoginId;

	private Stage primaryStage, dialog;
	private String loginId;
	private Protocol protocol;
	private SendChatWindowController sendChatWindowController;
	private ClientServer clientServer;

	private class Friend {

		private SimpleStringProperty nickNameProperty;
		private SimpleStringProperty emailProperty;
		private SimpleStringProperty genderProperty;
		private SimpleStringProperty ageProperty;
		private SimpleStringProperty regionProperty;
		private SimpleStringProperty telProperty;

		Friend(String nickName, String email, String gender, String age, String region, String tel) {
			this.nickNameProperty = new SimpleStringProperty(nickName);
			this.emailProperty = new SimpleStringProperty(email);
			this.genderProperty = new SimpleStringProperty(gender);
			this.ageProperty = new SimpleStringProperty(age);
			this.regionProperty = new SimpleStringProperty(region);
			this.telProperty = new SimpleStringProperty(tel);
		}

		public SimpleStringProperty getNickNameProperty() {
			return nickNameProperty;
		}

		public SimpleStringProperty getEmailProperty() {
			return emailProperty;
		}

		public SimpleStringProperty getGenderProperty() {
			return genderProperty;
		}

		public SimpleStringProperty getAgeProperty() {
			return ageProperty;
		}

		public SimpleStringProperty getRegionProperty() {
			return regionProperty;
		}

		public SimpleStringProperty getTelProperty() {
			return telProperty;
		}
	}

	List<TreeItem<Friend>> friendList = new ArrayList<>();

	TreeItem<Friend> root = new TreeItem<>(new Friend("", "", "", "", "", ""));
	
	private TreeTableViewSelectionModel<Friend> sm;
	private int rowIndex;  //친구삭제 및 추가시 ttv컨트롤 선택행 정보
	private int ttrRowCount = -1;

	private Map<String,String> connectedFriendIPList = new Hashtable<>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//loadTreeItems();
				//클릭하면 친구1친구2친구3 처럼 이벤트뜨게하는 코딩
		ttr.getSelectionModel()
		.selectedItemProperty()
		.addListener(new ChangeListener<TreeItem<Friend>>() {

			@Override
			public void changed(ObservableValue<? extends TreeItem<Friend>> observable,
					TreeItem<Friend> oldValue,
					TreeItem<Friend> newValue) {
				
				if ( newValue != null) {
					System.out.println("���� :" + newValue.getValue());
				}
			}
		});
		ttr.setOnMouseClicked(event -> doChat(event));
		lbLoginId.setText(loginId);
	}


	//채팅창
	private void doChat(MouseEvent event) {
		if(event.getClickCount() == 2) {
			
			TreeItem<Friend> item = ttr.getSelectionModel().getSelectedItem();
			String receiverID = item.getValue().getEmailProperty().getValue();
			if (!connectedFriendIPList.containsKey(receiverID)) {
				return;
			}
			String receiverIP = connectedFriendIPList.get(receiverID);
			System.out.println("selected text: " + receiverID + ":" + receiverIP);
			
			Parent chatWindow = null;
			Stage dialog = new Stage(StageStyle.DECORATED);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("chatWindow.fxml"));
			sendChatWindowController = new SendChatWindowController();
			loader.setController(sendChatWindowController);
			
			try {
				chatWindow = loader.load();
			} catch (IOException e) {		}
//			sendChatWindowController = loader.getController();
			sendChatWindowController.setDialog(dialog);

			sendChatWindowController.init(loginId,receiverID,receiverIP);			
			Scene scene = new Scene(chatWindow);
			dialog.setScene(scene);
			dialog.setTitle("대화창");
			dialog.show();
			
			dialog.setOnCloseRequest(e->{
				dialog.close();
				sendChatWindowController.client.stopClient();
			});
		}
	}

	private void loadTreeItems() {

		root.setExpanded(true); //클릭할때마다 접혔다 폈다하는 부분
		root.getChildren().setAll(friendList);

		ttcNickName.setCellValueFactory(param -> param.getValue().getValue().getNickNameProperty());
		ttcEmail.setCellValueFactory(param -> param.getValue().getValue().getEmailProperty());
		ttcGender.setCellValueFactory(param -> param.getValue().getValue().getGenderProperty());
		ttcAge.setCellValueFactory(param -> param.getValue().getValue().getAgeProperty());
		ttcRegion.setCellValueFactory(param -> param.getValue().getValue().getRegionProperty());
		ttcTel.setCellValueFactory(param -> param.getValue().getValue().getTelProperty());

		ttr.setRoot(root);
		ttr.setShowRoot(false);
		ttr.setTableMenuButtonVisible(true);

	}

	public void setDialog(Stage primaryStage, Stage dialog) {
		this.primaryStage = primaryStage;
		this.dialog = dialog;

	}
	//-------------------------------------------   ---------------------------------------------------//

	//로그인아이디 표시
	
	public void setInitial(String loginId, Protocol protocol) {
		this.loginId = loginId;
		this.protocol = protocol;
		
		clientServer =new ClientServer();
		protocol.registUser(loginId);
		lbLoginId.setText(loginId);

	}
	//초기 친구 로그인 목록
		
	public void getFriendLoginList(Command command) {
			// 친구목록
			List<MemberDTO> fList =   
					(List<MemberDTO>) command.getResults().elementAt(0);
			// 접속한 친구목록
			Map<String,String> cfList = 
					(Map<String,String>) command.getResults().elementAt(1);
			
			updateConnectedFriendIp(cfList,'+');
			
			System.out.println("fList"+fList);
			System.out.println("cfList"+cfList);
			
			Platform.runLater(()->{
				
				fList.stream().forEach(member->{
					Image image = null;	
					
					if(cfList.containsKey(member.getId())){
					 image = 
							new Image(MessengerMainController.class.getResource("images/login.png").toString());
					}else {
						image = 
							new Image(MessengerMainController.class.getResource("images/logout.png").toString());
					}
					int age = CommonUtil.getSeAge(member.getBirth()); // 만나이 계산
					
					friendList.add(new TreeItem<Friend>(new Friend(
								member.getNickname(),
								member.getId(),
								member.getGender(),
								String.valueOf(age),
								member.getRegion(),
								member.getTel()
							), new ImageView(image)
					));
				});
				
			
				loadTreeItems();
			});
		}

		// 친구접속 정보 갱신
		private void updateConnectedFriendIp(Map<String, String> cfList, char ch) {
			switch(ch) {
			case '+':
				connectedFriendIPList.putAll(cfList);
				break;
			case '-':
				cfList.keySet().stream().forEach(id->{
					if(connectedFriendIPList.containsKey(id)) {
						connectedFriendIPList.remove(id);
					}
				});
				break;
			default :
				break;
			}
			System.out.println("connectedFriendIPList :" + connectedFriendIPList);
		}
		//친구목록 결과
		public void getFriends(Command command) {
			Platform.runLater(()->{
				List<MemberDTO> list = 
						(List<MemberDTO>) command.getResults().elementAt(0);
				
				System.out.println(list);
				list.stream().forEach(member->{
					
					Image loginImage = 
							new Image(MessengerMainController.class.getResource("images/login.png").toString());
					Image logoutImage = 
							new Image(MessengerMainController.class.getResource("images/logout.png").toString());
					int age = CommonUtil.getSeAge(member.getBirth()); //������ ���
					friendList.add(new TreeItem<Friend>(new Friend(
								member.getNickname(),
								member.getId(),
								member.getGender(),
								String.valueOf(age),
								member.getRegion(),
								member.getTel()
							), new ImageView(loginImage)
					));
				});
				
			
				loadTreeItems();
			});
		}
//-------------------------------------------   ---------------------------------------------------//
		//친구추가
	public void handleAddFriend(ActionEvent e) {

		System.out.println("친구추가 클릭!!" + e.getTarget());

		boolean result = false;
		String answer = null;

		sm = ttr.getSelectionModel();
		ttrRowCount = sm.getTreeTableView().getExpandedItemCount();
		
		if (ttrRowCount == 0) {
			rowIndex = 0;
		} else {
			rowIndex = sm.getSelectedIndex();
			
		}

		if (rowIndex < 0) {
			return;
		}

		while (!result) {
			answer = DialogUtil.textInputDialog(null, "친구추가", "친구 이메일(Email)정보를 입력하세요.", null);

			if (!answer.trim().equals("")) {

				boolean isID = Pattern.matches("\\w+@\\w+\\.\\w+(\\.\\w+)?", answer);
				if (!isID) {
					DialogUtil.dialog(AlertType.WARNING, "친구추가", "이메일(Email) 형식이 잘못되었습니다. ex)aaa@bbb.com", null);
				} else {
					if (loginId.equalsIgnoreCase(answer)) {
						DialogUtil.dialog(AlertType.INFORMATION, "알림", "자신을 친구추가 할 수 없습니다.", null);
						break;
					}
					protocol.addFriends(loginId, answer);
					break;
				}
			} else {
				break;

			}
		}
	}

	//친구추가 결과
	public void addFriends(Command command) {
		boolean flag = ((Boolean) command.getResults().elementAt(0)).booleanValue();
		
		int loginStatus =  command.getResults().getStatus();
		
		if (flag) {
			Platform.runLater(() -> {
				switch (loginStatus) {
				case 1:{
					MemberDTO memberDTO = (MemberDTO)command.getResults().elementAt(1);	
					Map<String,String> loginFriend = 
							(Map<String,String>) command.getResults().elementAt(2);						
					updateConnectedFriendIp(loginFriend,'+');					
					addFriendTreeTableView(memberDTO,loginStatus);
				}
					break;
				case 2: {
					MemberDTO memberDTO = (MemberDTO)command.getResults().elementAt(1);										
					addFriendTreeTableView(memberDTO,loginStatus);
				}
					break;
				default:
					break;
				}

				DialogUtil.dialog(AlertType.INFORMATION, "알림", 
						"친구추가", "친구추가 성공.");		
			});
		} else {
			Platform.runLater(() -> {
				if(command.getResults().getStatus() == -1) {
					DialogUtil.dialog(AlertType.ERROR, "알림", 
							"친구추가", "가입된 회원이 아닙니다!");	
					
				}else {
					DialogUtil.dialog(AlertType.ERROR, "알림", 
							"친구추가", "친구추가 실패.");
					}
			});
		}

	}

	//친구TreeItem추가 
	private void addFriendTreeTableView(MemberDTO memberDTO, int loginStatus) {
		TreeItem<Friend> item = null;
		Image loginImage = new Image
				(MessengerMainController.class.getResource("images/login.png").toString());
		Image logoutImage = new Image
				(MessengerMainController.class.getResource("images/logout.png").toString());
		
		switch(loginStatus) {
		case 1:
			item = new TreeItem<>(new Friend(
					memberDTO.getNickname(),
					memberDTO.getId(),
					memberDTO.getGender(),
					String.valueOf(CommonUtil.getSeAge(memberDTO.getBirth())),
					memberDTO.getRegion(),
					memberDTO.getTel()),new ImageView(loginImage));
			break;
		case 2:
			item = new TreeItem<>(new Friend(
					memberDTO.getNickname(),
					memberDTO.getId(),
					memberDTO.getGender(),
					String.valueOf(CommonUtil.getSeAge(memberDTO.getBirth())),
					memberDTO.getRegion(),
					memberDTO.getTel()),new ImageView(logoutImage));		
			break;
		}

		if (ttrRowCount == 0) {
			root.getChildren().add(0, item);
		} else {

			TreeItem<Friend> selectedItem = sm.getModelItem(rowIndex);
			selectedItem.getParent().getChildren().add(rowIndex + 1, item);
			selectedItem.setExpanded(true);
		}

		editItem(item);
		sm = null;
	}

	//친구 TreeItem추가 후 ttv갱신 (첫번째 컬럼의 추가행으로 포커스 이동 )
	private void editItem(TreeItem<Friend> item) {
		int newRowIndex = ttr.getRow(item);
		ttr.scrollTo(newRowIndex);

		TreeTableColumn<Friend, ?> firstCol = ttr.getColumns().get(0);
		ttr.getSelectionModel().select(item);
		ttr.getFocusModel().focus(newRowIndex, firstCol);
		ttr.edit(newRowIndex, firstCol);
	}

	// -------------------------------------------	 ---------------------------------------------------//

	//친구삭제
	public void handleDelFriend(ActionEvent e) {

		sm = ttr.getSelectionModel();
		String friendId = sm.getSelectedItem().getValue().getEmailProperty().getValue();
		if (sm.isEmpty()) {
			return;
		}

		Optional<ButtonType> optional = 
				DialogUtil.dialog(AlertType.CONFIRMATION, "친구삭제", "선택된친구를 삭제하시겠습니까?", friendId);
				
		if (optional.get() == ButtonType.OK) {
			protocol.delFriends(loginId, friendId);
		}
	}

	//친구 삭제 결과
	public void delFriends(Command command) {
		boolean flag = ((Boolean) command.getResults().elementAt(0)).booleanValue();

		if(flag) {
			Platform.runLater(()->{
		
		if(command.getResults().getStatus() == 1) {
			Map<String,String> cfList = 
					(Map<String,String>) command.getResults().elementAt(1);
			
			updateConnectedFriendIp(cfList,'-');	
		}
		delFriendTreeTableView();


		DialogUtil.dialog(AlertType.INFORMATION, "알림", "친구삭제", "친구식제 성공.");				
				
	});	
	
		} else {
			Platform.runLater(() -> {

				DialogUtil.dialog(AlertType.ERROR, "알림","친구삭제", "친구삭제 실패.");		
			});

		}
	}

	//친구 TreeItem 삭제
	private void delFriendTreeTableView() {
		TreeItem<Friend> selectedForDeletion = sm.getSelectedItem();
		TreeItem<Friend> parent = selectedForDeletion.getParent();
		if (parent != null) {
			parent.getChildren().remove(selectedForDeletion);
		}
		sm = null;
	}
	
	//-------------------------------------------   ---------------------------------------------------//
	// 회원탈퇴
	public void handlememberout(ActionEvent e) {
		System.out.println("회원탈퇴");
		String answer = 
				DialogUtil.textInputDialog(null, "회원탈퇴", "회원탈퇴하시겠습니까?", "비밀번호를 입력하세요");
		System.out.println("answer:" + answer);
		if (!answer.trim().equals("")) {
			protocol.memberOut(loginId, answer);

		}
	}

	//회원탈퇴 결과
	public void memberOut(Command command) {

		boolean flag = ((Boolean) command.getResults().elementAt(0)).booleanValue();
		if (flag) {
			Platform.runLater(() -> {
				Optional<ButtonType> optional = 
						DialogUtil.dialog(AlertType.INFORMATION, "알림", "회원탈퇴", "회원탈퇴 성공.");
				
				if (optional.get() == ButtonType.OK) {
				
					DialogUtil.dialog(AlertType.INFORMATION, "�˸�", "ȸ��Ż��", "ȸ��Ż�𼺰�");
				
					dialog.close();}
			});

			protocol.stopClient();

		} else {
			Platform.runLater(() -> {
				DialogUtil.dialog(AlertType.ERROR, "알림", 
						"회원탈퇴", "회원탈퇴 실패.");		
			});
		}
	}
	//-------------------------------------------   ---------------------------------------------------//
	//로그인정보 수신
	public void login_notify(Command command) {
		Map<String,String> connectId = (Map<String,String>)command.getResults().elementAt(0);
		System.out.println("connectId"+connectId);
		Platform.runLater(()->{
			ttr.getRoot().getChildren().stream().forEach(row->{
				if(row.getValue().getEmailProperty().getValue().equalsIgnoreCase(
						connectId.keySet().stream().findFirst().get())) {
					System.out.println(connectId+":로긴이미지 변경해야함");
					row.setGraphic(
							new ImageView(
									new Image(MessengerMainController.class.getResource("images/login.png").toString())));
				}
			});
			updateConnectedFriendIp(connectId, '+');			
			DialogUtil.dialog(AlertType.INFORMATION, "알림", 
					"친구접속", connectId+"님이 로그인 하셨습니다!");
		});	
		
	}
	//로그아웃정보 수신
	public void logout_notify(Command command) {
		Map<String,String> closeId = (Map<String,String>)command.getResults().elementAt(0);
		System.out.println("closeId"+closeId);
		Platform.runLater(()->{
			ttr.getRoot().getChildren().stream().forEach(row->{
				if(row.getValue().getEmailProperty().getValue().equalsIgnoreCase(
						closeId.keySet().stream().findFirst().get())) {
					System.out.println(closeId+":로그아웃 변경해야함");
					row.setGraphic(
							new ImageView(
									new Image(MessengerMainController.class.getResource("images/logout.png").toString())));
				}
			});
			updateConnectedFriendIp(closeId, '-');
			
			
			DialogUtil.dialog(AlertType.INFORMATION, "알림", 
					"친구접속해제", closeId+"님이 로그아웃 하셨습니다!");
			
			
			
			
		});
		
	}	
	
	
	//로그아웃
	public void handleLogOut(ActionEvent e) {
		System.out.println("로그아웃");

		Optional<ButtonType> answer = 
				DialogUtil.dialog(AlertType.CONFIRMATION, "로그아웃", "로그아웃하시겠습니까?", null);

		if (answer.get() == ButtonType.OK) {

			dialog.close();
			primaryStage.show();
			protocol.stopClient();
			
		}
	}

}
