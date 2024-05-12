package MainPackage;

import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainProgram extends Application {
	
	public double X = 0;
	public double Y = 0;
	public double dK = 10;
	private boolean Exting = false;
	private Text TextXY = new Text();
	private Label Helper = new Label();
	private TextField FieldX = new TextField("0");
	private TextField FieldY = new TextField("0");
	private PlanetEarth Planet = new PlanetEarth();
	
	private void MatrixRotatePlanet(double gam, double bet) {
		double A11 = Math.cos(gam);
		double A12 = -Math.sin(bet)*Math.sin(gam);
		double A13 = Math.cos(bet)*Math.sin(gam);
		double A21 = 0;
		double A22 = Math.cos(bet);
		double A23 = -Math.sin(bet);
		double A31 = -Math.sin(gam);
		double A32 = Math.cos(gam)*Math.sin(bet);
		double A33 = Math.cos(bet)*Math.cos(gam);
		double d = Math.acos((A11+A22+A33-1)/2);
		if(d != 0) {
			double den = 2*Math.sin(d);
			Point3D place = new Point3D((A32-A23)/den, (A13-A31)/den, (A21-A12)/den);
			Planet.setRotationAxis(place);
			Planet.setRotate(Math.toDegrees(d));
		}
		else
	    	Planet.setRotate(0);
	}
	
	private class PlanetEarth extends Sphere {
		
		private PhongMaterial EarthMaterial = new PhongMaterial();
		
		PlanetEarth() {
			this.setRadius(300);
			this.setTranslateX(500);
			this.setTranslateY(500);
			EarthMaterial.setDiffuseMap(new Image("Images/DefaultMap.jpg"));
			EarthMaterial.setBumpMap(new Image("Images/ReliefMap.jpg"));
			EarthMaterial.setSpecularMap(new Image("Images/SpecularMap.jpg"));
			this.setMaterial(EarthMaterial);
			RotateAnimation PlanetRotate = new RotateAnimation();
			PlanetRotate.start();
		}
		
		public void setLines() {
			this.EarthMaterial.setDiffuseMap(new Image("Images/LinedMap.jpg"));
		}
		
		public void setDefault() {
			this.EarthMaterial.setDiffuseMap(new Image("Images/DefaultMap.jpg"));
		}
		
		class RotateAnimation extends Service<Void> {
			protected Task<Void> createTask() {
				return new Task<Void>() {
					protected Void call() throws Exception {
						double ThreadX = 0;
						double ThreadY = 0;
						double Reverse = 1;
						double tnXY = 1;
						while(!Exting) {
							double CheckX = Math.abs(X-ThreadX);
							double CheckY = Math.abs(Y-ThreadY);
							if(CheckX != 0 || CheckY != 0) {
								double Right = Math.abs(X-ThreadX);
								double Left = 360-Right;
								if(Right < Left)							
									Reverse = 1;
								else
									Reverse = -1;
								if(CheckX > dK*1.5/10) {
									if(ThreadX < X)
										ThreadX += dK*Reverse/10;
									else
										ThreadX -= dK*Reverse/10;
									if(ThreadX > 180)
										ThreadX -= 360;
									else if(ThreadX < -180)
										ThreadX += 360;
								}
								else if(CheckX != 0)
									ThreadX = X;			
								if(CheckY > dK*1.5/10) {
									if (CheckX == 0 || ThreadY == 0 || X-ThreadX == 0)
										tnXY = 1;
									else 
										if(Math.abs(X-ThreadX) > 180)
											tnXY = Math.abs((Y-ThreadY)/(360-Math.abs(X-ThreadX)));	
										else
											tnXY = Math.abs((Y-ThreadY)/(X-ThreadX));
									if(ThreadY < Y)
										ThreadY += dK*tnXY/10;
									else
										ThreadY -= dK*tnXY/10;
								}
								else if(CheckY != 0)
									ThreadY = Y;
								MatrixRotatePlanet(Math.toRadians(ThreadX), Math.toRadians(ThreadY));
			    				TextXY.setText("Поточні координати:\n" + 
			    						"Широта: " + Math.round(ThreadX * 10)/(double)10 + "\n" + 
			    						"Висота: " + Math.round(ThreadY * 10)/(double)10);
							}
							Thread.sleep(25);
						}
						return null;
					}			
				};
			}			
		}
	}
	
	private Group TextSet() {
		Label Head = new Label("Спостерігач Землі");
		Head.setFont(new Font("TimesNewRoman", 40));
		Head.setTranslateX(600);
		Head.setTranslateY(15);
		
		Label Panel = new Label("Панель керування");
		Panel.setFont(new Font("TimesNewRoman", 45));
		Panel.setTranslateX(1010);
		Panel.setTranslateY(130);
		
		Label Control = new Label("Ручне керування");
		Control.setFont(new Font("TimesNewRoman", 45));
		Control.setTranslateX(1010);
		Control.setTranslateY(825);
		
		TextXY.setText("Поточні координати:\n" + "Широта: " + X + "\n" + "Висота: " + Y);
		TextXY.setFont(new Font("TimesNewRoman", 33));
		TextXY.setTranslateX(1010);
		TextXY.setTranslateY(255);
		
		Label TextX = new Label("Ш:");
		TextX.setFont(new Font("TimesNewRoman", 33));
		TextX.setTranslateX(1240);
		TextX.setTranslateY(405);
		
		Label TextY = new Label("В:");
		TextY.setFont(new Font("TimesNewRoman", 33));
		TextY.setTranslateX(1240);
		TextY.setTranslateY(505);
		
		Helper.setText("Тут будуть коментуватися ваші помилки.");
		Helper.setFont(new Font("TimesNewRoman", 33));
		Helper.setTextFill(Color.RED);
		Helper.setTranslateX(130);
		Helper.setTranslateY(930);
		
		FieldX.setFont(new Font("TimesNewRoman", 25));
		FieldX.setPrefWidth(110);
		FieldX.setTranslateX(1280);
		FieldX.setTranslateY(400);
		
		FieldY.setFont(new Font("TimesNewRoman", 25));
		FieldY.setPrefWidth(110);
		FieldY.setTranslateX(1280);
		FieldY.setTranslateY(500);
	
		return new Group(Head, Panel, Control, TextXY, TextX, TextY, Helper, FieldX, FieldY);
	}
		
	private Group ButtonsSet() {		
		ImageView Button_200x180 = new ImageView(new Image("Images/Button(200x180).png"));	
		Button_200x180.setX(988);
		Button_200x180.setY(385);
		Button_200x180.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Button_200x180.setImage(new Image("Images/Button(200x180)_pres.png"));	
			}		
		});  
		Button_200x180.setOnMouseReleased(new  GoAction(Button_200x180)); 
		
		ImageView Button_Up = new ImageView(new Image("Images/Button(arrow).png"));
		Button_Up.setX(1070);
		Button_Up.setY(600);
		Button_Up.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Button_Up.setTranslateY(-10);	
			}		
		});  
		Button_Up.setOnMouseReleased(new  UpAction(Button_Up)); 
		
		ImageView Button_Right = new ImageView(new Image("Images/Button(arrow).png"));
		Button_Right.setRotate(90);
		Button_Right.setX(1120);
		Button_Right.setY(652);
		Button_Right.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Button_Right.setTranslateX(10);	
			}		
		});  
		Button_Right.setOnMouseReleased(new RightAction(Button_Right)); 
			
		ImageView Button_Down = new ImageView(new Image("Images/Button(arrow).png"));
		Button_Down.setRotate(180);
		Button_Down.setX(1068);
		Button_Down.setY(700);
		Button_Down.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Button_Down.setTranslateY(10);	
			}		
		});  
		Button_Down.setOnMouseReleased(new  DownAction(Button_Down)); 
				
		ImageView Button_Left = new ImageView(new Image("Images/Button(arrow).png"));
		Button_Left.setRotate(270);
		Button_Left.setX(1018);
		Button_Left.setY(650);
		Button_Left.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Button_Left.setTranslateX(-10);	
			}		
		});  
		Button_Left.setOnMouseReleased(new  LeftAction(Button_Left)); 
				
		ImageView Button_Plus = new ImageView(new Image("Images/Button(plus).png"));
		Button_Plus.setX(1230);
		Button_Plus.setY(605);
		Button_Plus.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Button_Plus.setImage(new Image("Images/Button(plus)_pres.png"));	
			}		
		});  
		Button_Plus.setOnMouseReleased(new PlusAction(Button_Plus));  
		
		ImageView Button_Minus = new ImageView(new Image("Images/Button(minus).png"));
		Button_Minus.setX(1230);
		Button_Minus.setY(705);
		Button_Minus.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
					Button_Minus.setImage(new Image("Images/Button(minus)_pres.png"));	
			}		
		});  
		Button_Minus.setOnMouseReleased(new MinusAction(Button_Minus)); 
		
		ImageView ModeButton = new ImageView(new Image("Images/Switcher_off.png"));
		boolean isSwitch = false;				
		ModeButton.setX(1330);
		ModeButton.setY(605);
		ModeButton.setOnMouseReleased(new SwitcherAction(ModeButton, isSwitch)); 
			
		return new Group(Button_200x180, Button_Up, Button_Right, Button_Down, Button_Left, Button_Plus, Button_Minus, ModeButton);
	}

	public class GoAction implements EventHandler<MouseEvent> {	//Подія для запису координат з текстового поля;
		
		ImageView ButtonPicture;
		
		GoAction(ImageView Original) {
			ButtonPicture = Original;
		}
		
		public void handle(MouseEvent event) {
			double TestX;
			double TestY;
			ButtonPicture.setImage(new Image("Images/Button(200x180).png"));
			try {
				TestX = Double.parseDouble(FieldX.getText());	    		    	
				TestY = Double.parseDouble(FieldY.getText());	
			}
			catch(NumberFormatException e) {
				Helper.setText("Неправильний формат вводу.");
				return;
			}			
			if(TestX <= 180 && TestX >= -180 && TestY <= 90 && TestY >= -90) {
    			Helper.setText("Координати були введені правильно.");
    			X = TestX;
    			Y = TestY;
			}
			else
				Helper.setText("Координати були введені неправильно - перехід не відбувся.");
		}	
	}	
	
	public class UpAction implements EventHandler<MouseEvent> {
		
		ImageView ButtonPicture;
		
		UpAction(ImageView Original) {
			ButtonPicture = Original;
		}
		
		public void handle(MouseEvent event) {
			Helper.setText("Викорисовується ручне керування.");
			ButtonPicture.setTranslateY(0);
	    	if(Y+dK < 90)
	    		Y += dK;  
	    	else
	    		Y = 90;
		}	
	}	
	
	public class RightAction implements EventHandler<MouseEvent> {
		
		ImageView ButtonPicture;
		
		RightAction(ImageView Original) {
			ButtonPicture = Original;
		}
		
		public void handle(MouseEvent event) {
			Helper.setText("Викорисовується ручне керування.");
			ButtonPicture.setTranslateX(0);
	    	if(X+dK > 180)
	    		X = -360+X+dK;
	    	else 
	    		X += dK;	 
		}	
	}	
	
	public class DownAction implements EventHandler<MouseEvent> {
		
		ImageView ButtonPicture;
		
		DownAction(ImageView Original) {
			ButtonPicture = Original;
		}
		
		public void handle(MouseEvent event) {
			Helper.setText("Викорисовується ручне керування.");
			ButtonPicture.setTranslateY(0);
	    	if(Y-dK > -90)
	    		Y -= dK;	
	    	else
	    		Y = -90;
		}	
	}	
	
	public class LeftAction implements EventHandler<MouseEvent> {
		
		ImageView ButtonPicture;
		
		LeftAction(ImageView Original) {
			ButtonPicture = Original;
		}
		
		public void handle(MouseEvent event) {
			Helper.setText("Викорисовується ручне керування.");
			ButtonPicture.setTranslateX(0);
	    	if(X-dK < -180)
	    		X = 360+X-dK;
	    	else 
	    		X -= dK; 		 
		}	
	}
	
	public class PlusAction implements EventHandler<MouseEvent> {
		
		ImageView ButtonPicture;
		
		PlusAction(ImageView Original) {
			ButtonPicture = Original;
		}
		
		public void handle(MouseEvent event) {
			ButtonPicture.setImage(new Image("Images/Button(plus).png"));
			if(Planet.getRadius() < 350) {
				Helper.setText("Наближаю.");
				dK /= 2;
				Planet.setRadius(Planet.getRadius()+50);
			}
			else if (Planet.getRadius() < 385) {
				Helper.setText("Наближаю.");  
				dK /= 2;
				Planet.setRadius(385);
			}
			else
				Helper.setText("Більше наближати неможливо.");
		}	
	}
	
	public class MinusAction implements EventHandler<MouseEvent> {
		ImageView ButtonPicture;
		MinusAction(ImageView Original) {
			ButtonPicture = Original;
		}
		public void handle(MouseEvent event) {
			ButtonPicture.setImage(new Image("Images/Button(minus).png"));
			if(Planet.getRadius() == 385) {
				Helper.setText("Віддаляю.");
				dK *= 2;
				Planet.setRadius(350);
			}
			else if(Planet.getRadius() > 250) {
				Helper.setText("Віддаляю.");
				dK *= 2;
				Planet.setRadius(Planet.getRadius()-50);
			}
			else
				Helper.setText("Більше віддаляти неможливо.");	 
		}	
	}
		
	public class SwitcherAction implements EventHandler<MouseEvent> {
		
		ImageView ButtonPicture;
		boolean Check;
		
		SwitcherAction(ImageView Original, boolean SwitcherMode) {
			ButtonPicture = Original;
			Check = SwitcherMode;
		}
		
		public void handle(MouseEvent event) {
			if(Check) {
				ButtonPicture.setImage(new Image("Images/Switcher_off.png"));
				Planet.setDefault();
				Helper.setText("Режим \"Сітка\" вимкнено.");
			}
			else {
				ButtonPicture.setImage(new Image("Images/Switcher_on.png"));
				Planet.setLines();
				Helper.setText("Режим \"Сітка\" увімкнено.");
			}
			Check = !Check;				
		}
	}
		
	private Group CreateGroup() {
		Rectangle Background = new Rectangle(0, 0, 1500, 1000);
		
		Background.setFill(Color.BLACK);
		
		ImageView Interface = new ImageView(new Image("Images/Interface.png"));
		
		return new Group(Background, Planet, Interface, ButtonsSet(), TextSet());
	}
	
	public void start(Stage MainStage) {	//Створення вікна та налаштування його змісту;
		Group MainGroup = CreateGroup();
		Scene MainScene = new Scene(new Pane(MainGroup), 1500, 1030);
		MainStage.setScene(MainScene);
		MainStage.setTitle("Спостерігач Землі.exe");
		MainStage.setResizable(false);
		MainStage.getIcons().add(new Image("Images/PlanetIcon.png"));
		MainStage.setHeight(1030);
		MainStage.setWidth(1500);
		MainStage.show();
	}
	
	public void exit(Stage MainStage) {
		Exting = true;
	}
	
	public static void main(String[] args) {
	    launch();
	}
}