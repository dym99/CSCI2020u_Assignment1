import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Question2 extends Application {
	public static void main(String args[]) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//Grid pane for layout
		GridPane gPane = new GridPane();
		
		//Create labels
		Label amountLabel = new Label("Investment Amount");
		Label yearsLabel = new Label("Years");
		Label rateLabel = new Label("Annual Interest Rate");
		Label valueLabel = new Label("Future Value");
		
		//Add to grid pane
		gPane.add(amountLabel, 0, 0);
		gPane.add(yearsLabel, 0, 1);
		gPane.add(rateLabel, 0, 2);
		gPane.add(valueLabel, 0, 3);
		
		//Set up text fields
		TextField amountField = new TextField();
		TextField yearsField = new TextField();
		TextField rateField = new TextField();
		TextField valueField = new TextField();
		
		//Disable value field (don't want to have a user type in the value field)
		valueField.setEditable(false);
		
		//Add to grid pane
		gPane.add(amountField, 1, 0);
		gPane.add(yearsField, 1, 1);
		gPane.add(rateField, 1, 2);
		gPane.add(valueField, 1, 3);
		
		Button calcBtn = new Button("Calculate");
		
		calcBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				//Obtain the user input
				double amount = Double.parseDouble(amountField.getText());
				double years = Double.parseDouble(yearsField.getText());
				double rate = Double.parseDouble(rateField.getText());
				
				//Get monthly interest rate from annual, and turn into percent
				double monthlyRate = rate/12.0d;
				monthlyRate /= 100.0d;
				
				//Calculate future value and set the value field to display the result.
				double futureValue = amount * Math.pow((1 + monthlyRate), years*12);
				valueField.setText(String.format("%.2f",futureValue));
			}
			
		});
		
		//Add button to grid pane
		gPane.add(calcBtn, 1, 4);
		
		//Set the scene up
		Scene scene = new Scene(gPane);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}

