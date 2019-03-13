package com.kh.messenger.client2;

import com.kh.messenger.sample.ConfirmDialog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppMain2 extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		//레이아웃 파일을 읽어 각 태그를 객체화 하고 최상위 루트 컨테이너를 참조값으로 변환 한다.
//		Parent patent= FXMLLoader(getClass().getResource("root.fxml"));
		FXMLLoader loader =
		  new FXMLLoader(getClass().getResource("root.fxml"));
		Parent parent = loader.load();

		RootController controller = loader.getController();
		controller.setPrimaryStage(primaryStage);
		
		Member.getInstance();
		Scene scene = new Scene(parent);
		//
		scene.getStylesheets().add(getClass().getResource("messenger.css").toString());
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("MESSENGER2");
		primaryStage.show();
		
		primaryStage.setOnCloseRequest(e->{
			boolean answer =false;
			answer =ConfirmDialog.display("확인", "메신저를 종료하시겠습니까?");
			
			if(answer) {
				primaryStage.close();
			}else {
				e.consume();
			}
			
		});
	}
	
	public static void main (String[] args) {
		Application.launch(args);
		
		
	}

}
