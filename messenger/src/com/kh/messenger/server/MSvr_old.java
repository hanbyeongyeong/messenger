package com.kh.messenger.server;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.print.DocFlavor.INPUT_STREAM;

public class MSvr_old {

	ServerSocket serverSocket; // ��������
	ExecutorService executorService; // ������Ǯ�̿�
	List<Client> connections = new Vector<Client>(); // Ŭ���̾�Ʈ ��������

	final String HOSTNAME = "127.0.0.1";
	final int PORT = 6001;

	private MSvrCtr mSvrCtr;

	MSvr_old(MSvrCtr mSvrCtr) {
		this.mSvrCtr = mSvrCtr;
	}

	// �������ϻ����� Ŭ���̾�Ʈ�� �����ϸ� Ŭ���̾�Ʈ ������ ����
	void startServer() {

		executorService = Executors.newFixedThreadPool(20);

		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(HOSTNAME, PORT));
			mSvrCtr.uiUpdate(UiCommand.SERVER_LOG, "[�������� ������]");

		} catch (IOException e) {
			if (!serverSocket.isClosed()) {
				stopServer();
			}
			return;
		}

		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				mSvrCtr.uiUpdate(UiCommand.SERVER_START, null);
				while(true) {
				try {
					// Ŭ���̾�Ʈ�� ������ ������ �����·� �ִٰ� ��û�� ���� ���socket�� ��ȯ��
					Socket socket = serverSocket.accept();
					String message = "[�������: " + socket.getRemoteSocketAddress() + ":"
							+ Thread.currentThread().getName() + "]";

					mSvrCtr.uiUpdate(UiCommand.SERVER_LOG, message);

					Client client = new Client(socket);

					// ������ Ŭ���̾�Ʈ�� vector�� ����=> client �����뵵
					connections.add(client);
					mSvrCtr.uiUpdate(UiCommand.SERVER_LOG, "[���ᰳ��: " + connections.size() + "]");
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

			mSvrCtr.uiUpdate(UiCommand.SERVER_STOP,null);
		} catch (Exception e) {
		}

	}

	// ������ Ŭ���̾�Ʈ�� ����ϴ� ���
	class Client {

		Socket socket;

		public Client(Socket socket) {
			this.socket = socket;
			recieve();
		}

		void recieve() {
			Runnable runnable = new Runnable() {

				@Override
				public void run() {

					try {
						while (true) {

							byte[] byteArr = new byte[100];
							InputStream inputStream = socket.getInputStream();

							int readBytecount = inputStream.read(byteArr);
					
							//Ŭ���̾�Ʈ�� ����������socket�� close()�� ȣ�� ���� ���
							if(readBytecount ==-1) {
								throw new IOException();
							}

							String message = "[��ûó�� :" + socket.getRemoteSocketAddress() + ": "
									+ Thread.currentThread().getName() + "]";

							mSvrCtr.uiUpdate(UiCommand.SERVER_LOG, message);

							String data = new String(byteArr, 0, readBytecount, "UTF-8");
							for (Client client : connections) {

								client.send(data);
							}
						}
					} catch (IOException e) {
						
						try {
						 connections.remove(Client.this);
						 String message = "[Ŭ������Ʈ ��� �ȵ� :"
								 	+socket.getRemoteSocketAddress()
								 	+";"
								 	+Thread.currentThread().getName()+"]";
						 mSvrCtr.uiUpdate(UiCommand.SERVER_LOG,message);
						 mSvrCtr.uiUpdate(UiCommand.SERVER_LOG,"[���ᰳ��:"+ connections.size()+"]");
						 
						 socket.close();
						}catch(IOException e1){}
						
					}
				}
			};
			executorService.submit(runnable);
		}

		void send(String data) {
			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					try {
						byte[] byteArr = data.getBytes("UTF-8");
						OutputStream outputStream = socket.getOutputStream();
						outputStream.write(byteArr);
						outputStream.flush();
					} catch (IOException e) {

						try {
							connections.remove(Client.this);
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
