package com.kh.messenger.client2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.kh.messenger.client2.ReceiveChatWindowController;
import com.kh.messenger.common.Command;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClientServer {

	ServerSocket serverSocket; // ��������
	ExecutorService executorService; // ������Ǯ�̿�
	List<Client> connections = new Vector<Client>(); // Ŭ���̾�Ʈ���� ����

	final String HOSTNAME = "localhost";
	final int PORT = 7002;

	ClientServer() {
		System.out.println("ClientServer()");
		startServer();
	}

	// �������� ���� �� Ŭ���̾�Ʈ�� �����ϸ� Ŭ���̾�Ʈ ������ ����
	void startServer() {

		executorService = Executors.newFixedThreadPool(10);

		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(HOSTNAME, PORT));
			System.out.println("startServer() [�������� ������]");
		} catch (IOException e) {
			System.out.println("startServer() [�������� ��������]");
			if (!serverSocket.isClosed()) {
				stopServer();
			}
			return;
		}

		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						// Ŭ���̾�Ʈ�� �����Ҷ����� �����·� �ִٰ� ��û�� ���� ��� socket�� ��ȯ��
						Socket socket = serverSocket.accept();

						String message = "[���� ����: " + socket.getRemoteSocketAddress() + ": " + Thread.currentThread().getName()
								+ "]";
						System.out.println(message);

						Client client = new Client(socket);

						// ������ Ŭ���̾�Ʈ�� vector�� ���� => client�����뵵
						connections.add(client);
						System.out.println("[���� ����: " + connections.size() + "]");
					} catch (IOException e) {
						if (!serverSocket.isClosed()) {
							stopServer();
						}
						break;
					}
				}
			}
		};
		executorService.submit(runnable);

	}

	// ������ Ŭ���̾�Ʈ ������ ��� close �ϰ� ���������� close
	void stopServer() {

		try {
			Iterator<Client> iterator = connections.iterator();
			while (iterator.hasNext()) {
				Client client = iterator.next();
				client.socket.close();
				iterator.remove();
			}
			if (serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			if (executorService != null && !executorService.isShutdown()) {
				executorService.shutdown();
			}

			System.out.println("stopServer()!");
		} catch (Exception e) {
		}
	}

	// ������ ��û�� Ŭ���̾�Ʈ�� ����ϴ� ���
	class Client {

		Socket socket;
		ObjectInputStream ois;
		ObjectOutputStream oos;
		Command command;

		Stage chatWindow;
		ReceiveChatWindowController receiveChatWindowController;

		String message, senderID, receiverID;

		public Client(Socket socket) {
			this.socket = socket;

			try {
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}

			receive();
		}

		void receive() {
			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					try {
						while (true) {

							command = (Command) ois.readObject();
							System.out.println(command.getType().name() + "ȣ���!!");

							switch (command.getType()) {
							case SENDMESSAGE: {
								String[] args = command.getArgs();
								message = args[0];
								senderID = args[1];
								receiverID = args[2];
								System.out.println("args="+message+":"+senderID+":"+receiverID);								
								chattHandle(Client.this, message, senderID, receiverID);
							}
								break;
							default:
								break;
							}
							String message = "[��û ó��: " + socket.getRemoteSocketAddress() + ": " + Thread.currentThread().getName()
									+ "]";
							System.out.println(message);

							for(Client client: connections) {
								client.send(command);
							}
						}
					} catch (Exception e) {
						try {
							connections.remove(Client.this);
							String message = "[Ŭ���̾�Ʈ ��� �ȵ�: " + socket.getRemoteSocketAddress() + ": "
									+ Thread.currentThread().getName() + "]";
							
							 Platform.runLater(()->receiveChatWindowController.display(senderID+">>"  + "���� �����̽��ϴ�."));								
							
							System.out.println("[���� ����: " + connections.size() + "]");

							socket.close();
						} catch (IOException e1) {
						}
					}
				}
			};
			executorService.submit(runnable);
		}

		protected void chattHandle(Client client,String message, String senderID, String receiverID) {

			System.out.println("��ȭâȣ��!");
			Platform.runLater(()->{
				if (chatWindow == null) {
					chatWindow = new Stage(StageStyle.DECORATED);
					FXMLLoader loader = new FXMLLoader(getClass().getResource("chatWindow.fxml"));
	
					Parent p = null;
					
					receiveChatWindowController = new ReceiveChatWindowController();
					loader.setController(receiveChatWindowController);
					
					try {
						p = loader.load();
					} catch (IOException e) {
					}
//					receiveChatWindowController = (ReceiveChatWindowController)loader.getController();
					receiveChatWindowController.setDialog(chatWindow);
	
					Scene scene = new Scene(p);
					chatWindow.setScene(scene);
					chatWindow.setTitle("��ȭâ");
					chatWindow.show();
	
					chatWindow.setOnCloseRequest(event->{
						try {
							client.socket.close();
						} catch (IOException e) {	}
					});					
					receiveChatWindowController.receiveMsg(client, message, senderID, receiverID);
				} else {
					chatWindow.show();
					receiveChatWindowController.receiveMsg(client, message, senderID, receiverID);
				}
			});
		}

		void send(Command command) {
			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					try {
						oos.writeObject(command);
						oos.flush();
						System.out.println(command.getType().name() + ": ��� ���������� ����!");
					} catch (IOException e) {

						try {
							connections.remove(Client.this);
							String message = "[Ŭ���̾�Ʈ ��� �ȵ�: " + socket.getRemoteSocketAddress() + ": "
									+ Thread.currentThread().getName() + "]";
							Platform.runLater(()->receiveChatWindowController.display(senderID+">>"  + "���� �����̽��ϴ�."));							
							
//							mSvrCtr.uiUpdate(UiCommand.SERVER_LOG, message);
//							mSvrCtr.uiUpdate(UiCommand.SERVER_LOG, "[���� ����: " + connections.size() + "]");

							socket.close();
						} catch (IOException e1) {
						}
					}
				}

			};

			executorService.submit(runnable);
		}
	}

}
