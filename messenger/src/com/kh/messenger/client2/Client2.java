package com.kh.messenger.client2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javafx.application.Platform;

public class Client2 {
	
	ReceiveChatWindowController chatWindowController;
	Socket socket;
	
	Client2(ReceiveChatWindowController chatWindowController){
		this.chatWindowController = chatWindowController;
		System.out.println("Ŭ���̾�Ʈ ������");
		startClient();
	}
	
	void startClient() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					socket = new Socket();
					socket.connect(new InetSocketAddress("192.168.0.121", 6001));
					Platform.runLater(()->{
						try {
							chatWindowController.display("[���� �Ϸ�: "  + socket.getRemoteSocketAddress() + "]");
							chatWindowController.btnSendDisable(false);
						} catch (Exception e) {}
					});
				} catch(Exception e) {
					Platform.runLater(()->chatWindowController.display("[���� ��� �ȵ�]"));
					if(!socket.isClosed()) { stopClient(); }
					return;
				}
				receive();
			}
		};
		thread.start();
	}
	
	void stopClient() {
		try {
			Platform.runLater(()->{
				chatWindowController.display("[���� ����]");
				chatWindowController.btnSendDisable(true);
			});
			if(socket!=null && !socket.isClosed()) {
				socket.close();
			}
		} catch (IOException e) {}
	}		
	
	void receive() {
		while(true) {
			try {
				byte[] byteArr = new byte[100];
				InputStream inputStream = socket.getInputStream();
				
				//������ ������������ �������� ��� IOException �߻�
				int readByteCount = inputStream.read(byteArr);
				
				//������ ���������� Socket�� close()�� ȣ������ ���
				if(readByteCount == -1) { throw new IOException(); }
				
				String data = new String(byteArr, 0, readByteCount, "UTF-8");
				
				Platform.runLater(()->chatWindowController.display("[�ޱ� �Ϸ�] "  + data));
			} catch (Exception e) {
				Platform.runLater(()->chatWindowController.display("[���� ��� �ȵ�]"));
				stopClient();
				break;
			}
		}
	}
	
	void send(String data) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {		
					byte[] byteArr = data.getBytes("UTF-8");
					OutputStream outputStream = socket.getOutputStream();
					outputStream.write(byteArr);
					outputStream.flush();
					Platform.runLater(()->chatWindowController.display("[������ �Ϸ�]"));
				} catch(Exception e) {
					Platform.runLater(()->chatWindowController.display("[���� ��� �ȵ�]"));
					stopClient();
				}				
			}
		};
		thread.start();
	}
}
