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

	ServerSocket serverSocket; // 서버소켓
	ExecutorService executorService; // 스레드풀이용
	List<Client> connections = new Vector<Client>(); // 클라이언트 정보저장

	final String HOSTNAME = "127.0.0.1";
	final int PORT = 6001;

	private MSvrCtr mSvrCtr;

	MSvr_old(MSvrCtr mSvrCtr) {
		this.mSvrCtr = mSvrCtr;
	}

	// 서버소켓생성후 클라이언트가 접속하면 클라이언트 소켓을 생성
	void startServer() {

		executorService = Executors.newFixedThreadPool(20);

		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(HOSTNAME, PORT));
			mSvrCtr.uiUpdate(UiCommand.SERVER_LOG, "[서버소켓 생성됨]");

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
					// 클라이언트가 접속할 때까지 대기상태로 있다가 요청이 오면 통신socket을 반환함
					Socket socket = serverSocket.accept();
					String message = "[연결수락: " + socket.getRemoteSocketAddress() + ":"
							+ Thread.currentThread().getName() + "]";

					mSvrCtr.uiUpdate(UiCommand.SERVER_LOG, message);

					Client client = new Client(socket);

					// 접속한 클라이언트를 vector에 저장=> client 관리용도
					connections.add(client);
					mSvrCtr.uiUpdate(UiCommand.SERVER_LOG, "[연결개수: " + connections.size() + "]");
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

	// 접속한 클라이언트 소켓을 모두 close 하고 서버소켓을 close
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

	// 접속한 클라이언트와 통신하는 기능
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
					
							//클라이언트가 정상적으로socket의 close()를 호출 했을 경우
							if(readBytecount ==-1) {
								throw new IOException();
							}

							String message = "[요청처리 :" + socket.getRemoteSocketAddress() + ": "
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
						 String message = "[클라이턴트 통신 안됨 :"
								 	+socket.getRemoteSocketAddress()
								 	+";"
								 	+Thread.currentThread().getName()+"]";
						 mSvrCtr.uiUpdate(UiCommand.SERVER_LOG,message);
						 mSvrCtr.uiUpdate(UiCommand.SERVER_LOG,"[연결개수:"+ connections.size()+"]");
						 
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
