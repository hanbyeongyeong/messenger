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
		//���̾ƿ� ������ �о� �� �±׸� ��üȭ �ϰ� �ֻ��� ��Ʈ �����̳ʸ� ���������� ��ȯ �Ѵ�.
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
			answer =ConfirmDialog.display("Ȯ��", "�޽����� �����Ͻðڽ��ϱ�?");
			
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
