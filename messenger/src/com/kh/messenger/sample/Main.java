package com.kh.messenger.sample;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;import oracle.net.aso.b;

public class Main extends Application {

	Stage window;
	Button[] button = new Button[10];
	List<Button> list = new ArrayList<>();
	
	private boolean answer ;
	public static void main(String[] args) {
		Application.launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		window = primaryStage;
	
			button[0]= new Button("alert()");
			button[1]= new Button("alert2()");
			button[2]= new Button("textinput()");
			button[3]= new Button("choice()");
			button[4]= new Button("confirm()");
			button[5]= new Button("alert()");
			button[6]= new Button("");
			button[7]= new Button("");
			button[8]= new Button("");
			button[9]= new Button("");
			
			for(int i =0;i<button.length;i++) {
				list.add(button[i]);
	}
		button[0].setOnAction(e->
		{Optional<ButtonType> optional =
			DialogUtil.dialog(AlertType.CONFIRMATION, "정보","친구등록","친구등록하시겠습니까?");
			if(optional.get() ==ButtonType.OK) {
				System.out.println("of clk");
			}else if(optional.get()==ButtonType.CANCEL) {
				System.out.println("cancle clk");
			}
				
		});
		button[1].setOnAction(e->
		{DialogUtil.dialog(AlertType.INFORMATION, "정보", "친구등록", "새친구등록됨");});
		
		button[2].setOnAction(e->
		{String value =
			DialogUtil.textInputDialog("친구아이디를 입력하세요", "친구등록", "새로 등록할 친구의 이메일정보를 입력해주세요" ,null);
		System.out.println("입력값-->"+value);});
		
		button[3].setOnAction(e->
		{List<String> list = new ArrayList<>();
		list.add("친구등록");
		list.add("친구삭제");
		list.add("친구조회");
		String value = DialogUtil.choiceDialog(list, "친구조회","친구관리","친구보기","친구머리");
		System.out.println("선택값: "+value);});
		
		button[4].setOnAction(e->
		{List<ButtonType> types = new ArrayList<>();
		types.add(ButtonType.YES);
		types.add(ButtonType.NO);
		types.add(ButtonType.CLOSE);
		
		ButtonType bt1 = new ButtonType("button1");
		ButtonType bt2 = new ButtonType("button2");
		ButtonType bt3 = new ButtonType("button3");
		types.add(bt1);
		types.add(bt2);
		types.add(bt3);
		
		Optional<ButtonType> optional =
			DialogUtil.dialog("정보","친구삭제","친구목록에서 삭제하시겠습니까?", types);
		if(optional.get() == ButtonType.YES) {
			System.out.println("yes clk");
		}else if(optional.get() == ButtonType.NO){
			System.out.println("no clk");
		}else if(optional.get() == ButtonType.CLOSE){
			System.out.println("cls clk");
		}else if(optional.get() == bt1){
			System.out.println("bt1 clk");
		}else if(optional.get() == bt2){
			System.out.println("bt2 clk");
		}else if(optional.get() == bt3){
			System.out.println("bt3 clk");
		}
			
		});
		
		button[5].setOnAction(e->
		{			
			ButtonType bt1 = new ButtonType("button1");
			ButtonType bt2 = new ButtonType("button2");
			ButtonType bt3 = new ButtonType("button3");
			
			
			Optional<ButtonType> optional =
				DialogUtil.dialog("정보","친구삭제","친구목록에서 삭제하시겠습니까?",
							bt1,bt2,bt3,
							ButtonType.YES,ButtonType.NO,ButtonType.CLOSE);
			
			if(optional.get() == ButtonType.YES) {
				System.out.println("yes clk");
			}else if(optional.get() == ButtonType.NO){
				System.out.println("no clk");
			}else if(optional.get() == ButtonType.CLOSE){
				System.out.println("cls clk");
			}else if(optional.get() == bt1){
				System.out.println("bt1 clk");
			}else if(optional.get() == bt2){
				System.out.println("bt2 clk");
			}else if(optional.get() == bt3){
				System.out.println("bt3 clk");
			}});
//		button[6].setOnAction(e->{DialogUtil.textInputDialog();});
//		button[7].setOnAction(e->{DialogUtil.choiceDialog();});

			
						
		VBox stack = new VBox();

		stack.setAlignment(Pos.CENTER);
		stack.getChildren().addAll(list);

		Scene scene =new Scene(stack,300,250);
		
		window.setScene(scene);
		window.setTitle("dialog test");
		window.show();
		
	}

}
