package com.kh.messenger.sample;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectInputOutputStream {

	public static void main(String[] args) throws Exception {

		FileOutputStream fos = new FileOutputStream("d:/temp/Login.dat");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setId("t1@t.com");
		loginInfo.setPw("t1");

		oos.writeObject(loginInfo);
		oos.flush();
		
		oos.close();
		
		FileInputStream fis = new FileInputStream("d:/temp/Login.dat");
		ObjectInputStream ois = new ObjectInputStream(fis);
		
		LoginInfo info = (LoginInfo)ois.readObject();
		System.out.println(info.getId());
		System.out.println(info.getPw());
	}

}
