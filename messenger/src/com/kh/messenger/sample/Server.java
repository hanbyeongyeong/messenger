package com.kh.messenger.sample;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;



public class Server {

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		Socket socket =null;
		InputStream is =null;
		OutputStream os =null;
		
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress("192.168.0.130", 9000));
			while(true) {
				System.out.println("[�����ٸ�]");
				 socket =serverSocket.accept();
				InetSocketAddress isa = (InetSocketAddress)socket.getRemoteSocketAddress();
				System.out.println("[���������]"+isa.getHostName());
				System.out.println("[���������]"+isa.getAddress());
				System.out.println("[���������]"+isa.getPort());
				System.out.println("[���������]"+isa.getHostString());
				
				byte[] bytes = new byte[100];
				 is = socket.getInputStream();
				int readByteCount = is.read(bytes);
				String msg = new  String(bytes, 0, readByteCount,"UTF-8");
				System.out.println("[�����ͼ���]:"+ msg);
				
				 os = socket.getOutputStream();
				msg=";_;";
				bytes = msg.getBytes("UTF-8");
				os.write(bytes);
				os.flush();
				System.out.println("[�����ͼ۽ſϷ�]");

				
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				os.close();
				socket.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

}
