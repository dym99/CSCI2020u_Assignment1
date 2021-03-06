import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;




public class Question1 extends Application {

	public static void main(String args[]) {
		launch(args);
	}
	
	public int RandomCardIndex() {
		//Generate random double from 0-1
		double rand = Math.random();
		
		//Turn random into a range of integers.
		rand*=52.0d;
		
		//Round up
		rand+=1.0f;
		
		int randInt = (int)rand;
		
		return randInt;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//HObx for horizontal layout.
		HBox hbox = new HBox();

		int cardAIndex = RandomCardIndex();
		int cardBIndex = RandomCardIndex();
		int cardCIndex = RandomCardIndex();
		
		Image cardAImage = new Image("Cards/"+Integer.toString(cardAIndex)+".png");
		Image cardBImage = new Image("Cards/"+Integer.toString(cardBIndex)+".png");
		Image cardCImage = new Image("Cards/"+Integer.toString(cardCIndex)+".png");
		
		ImageView cardA = new ImageView(cardAImage);
		ImageView cardB = new ImageView(cardBImage);
		ImageView cardC = new ImageView(cardCImage);
		
		hbox.getChildren().add(cardA);
		hbox.getChildren().add(cardB);
		hbox.getChildren().add(cardC);
		
		Scene scene = new Scene(hbox);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
