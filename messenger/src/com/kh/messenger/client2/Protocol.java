package com.kh.messenger.client2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.kh.messenger.common.Command;
import com.kh.messenger.common.MemberDTO;


public class Protocol {

	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Command command;
	
	//private boolean socketStatus;
	


	private final String HOSTNAME = "192.168.0.130";
	private final int PORT = 6001;
	
	RootController rootController;
	MessengerMainController messengerMainController;
	MemberJoinController memberJoinController;
	
	FindidController findidController;
	FindpwController findpwController;
	
	Protocol(RootController rootController){
		this.rootController = rootController;
		startClient();
		
		if(socket==null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			
			}
		}
		System.out.println("socket2"+socket);
	}
	
	Protocol(MemberJoinController memberJoinController){
		this.memberJoinController = memberJoinController;
		startClient();
		if(socket==null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			
			}
		}
		System.out.println("socket2"+socket);
	}
	
	Protocol(FindidController findidController) {
		this.findidController= findidController;
		startClient();
		if(socket==null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			
			}
		}
		System.out.println("socket2"+socket);
	}
	
	Protocol(FindpwController findpwController) {
		this.findpwController= findpwController;
		startClient();
		if(socket==null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			
			}
		}
		System.out.println("socket2"+socket);
	}
	
	public void setMessengerMainController(MessengerMainController controller) {
		this.messengerMainController = controller;
		
	}

	public void setMemberJoinController(MemberJoinController controller) {
		this.memberJoinController = controller;
	}

	
	// ���� ����
	void startClient() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					socket = new Socket();
					socket.connect(new InetSocketAddress(HOSTNAME, PORT));
					oos = new ObjectOutputStream(socket.getOutputStream());
					ois = new ObjectInputStream(socket.getInputStream());
					
				} catch (IOException e) {
					System.out.println("������žȵ�!");
					if(!socket.isClosed()) {
						stopClient();
					}
//					socketClosed=true;
				}
				receive();
			}
		};
		thread.start();
	}
	// ���� ���� ����
	void stopClient() {
		try {
			rootController.loginBtnDisable(false);
			
			if(socket !=null && !socket.isClosed()) {
				System.out.println("�������!");
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// ������ ����
	void receive() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				while(true) {
					try {
						command = (Command) ois.readObject();
						System.out.println(command.getType().name() + ": ������ŵ�!!");

						switch (command.getType()) {
						case ISLOGIN: {
								rootController.doLogin(command);
						}break;
						
						case REGISTUSER: {
							messengerMainController.getFriendLoginList(command);
						}break;
						
						case GETFRIENDS:{
							
							messengerMainController.getFriends(command);
						}break;
						
						case ADDFRIEND:{
							messengerMainController.addFriends(command);
						}break;
						
						case DELFRIEND:{
							messengerMainController.delFriends(command);
						}break;
						
						case MEMBERJOIN:{
							memberJoinController.memberJoin(command);
						}break;
						
						case MEMBEROUT:{
							messengerMainController.memberOut(command);
						}break;
						
						case LOGIN_NOTIFY: {
							messengerMainController.login_notify(command);
						}
							break;
						case LOGOUT_NOTIFY: {
							messengerMainController.logout_notify(command);
						}
							break;
						
						case FINDID:{
							findidController.findId(command);
						}break;
						
						case FINDPW:{
							findpwController.findPw(command);
						}break;
						
						default:
	
						break;			
													
						}
					} catch (Exception e) {
						 e.printStackTrace();
						System.out.println("������žȵ�!");
						stopClient();
						break;
					}
				}
			}
		};
		thread.start();
	}
		//�α���
	void isMember(String id, String pw) {
		command = new Command(Command.CommandType.ISLOGIN);
		String args[] = { id, pw };
		command.setArgs(args);
		writeCommand(command);
	}
		// �������ӵ��
		public void registUser(String loginId) {
			command = new Command(Command.CommandType.REGISTUSER);
			String args[] = { loginId };
			command.setArgs(args);
			writeCommand(command);
		}
	
	
		//ģ�����
	void getFriends(String id){
		command = new Command(Command.CommandType.GETFRIENDS);
		String args[] = {id};
		command.setArgs(args);
		writeCommand(command);
		}
		//ģ���߰�
	void addFriends(String myId,String friendId){
		command = new Command(Command.CommandType.ADDFRIEND);
		String args[] = {myId,friendId};
		command.setArgs(args);
		writeCommand(command);
		}
		//ģ������
	void delFriends(String myId,String friendId){
		command = new Command(Command.CommandType.DELFRIEND);
		String args[] = {myId,friendId};
		command.setArgs(args);
		writeCommand(command);
		}
		//ȸ������
	public void memberJoin(MemberDTO memberDTO) {
		command = new Command(Command.CommandType.MEMBERJOIN);
		command.getRequests().addElement(memberDTO);
		writeCommand(command);
		
	}
		//ȸ��Ż��
	public void memberOut(String id, String pw){
		command = new Command(Command.CommandType.MEMBEROUT);
		String args[]= { id, pw};
		command.setArgs(args);
		writeCommand(command);
		
	}
	//���̵���ȸ
	public void findId(String tel, String birth) {

		command = new Command(Command.CommandType.FINDID);
		String args[] = { tel, birth };
		command.setArgs(args);
		writeCommand(command);

	}
	
	public void findPw(String id, String tel, String birth) {
		command = new Command(Command.CommandType.FINDPW);
		String args[] = { id, tel, birth };
		command.setArgs(args);
		writeCommand(command);
		
	}
			
	// ������ command����
	public void writeCommand(Command command) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					oos.writeObject(command);
					oos.flush();
					System.out.println(command.getType().name()+":writecommȣ��");
				} catch (IOException e) {
				}
			}
		};
		thread.start();
	}
	
//	private boolean socketStatus;
//	public boolean isSocketStatus() {
//		return socketStatus;
//	}
	
}











