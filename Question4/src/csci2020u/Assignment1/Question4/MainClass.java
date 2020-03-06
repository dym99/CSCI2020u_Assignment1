package csci2020u.Assignment1.Question4;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class MainClass extends Application {

	public static void main(String args[]) {
		launch(args);
	}
	
	//Reference to canvas graphics context
	GraphicsContext m_gc;
	
	HashMap<Character, Integer> m_occurenceMap;
	

	//Set a proportional height for the bars
	//(a height of 400 reaches the top of the canvas with no extra room), so 380 seems nice.
	static final double OCC_BAR_MAX_HEIGHT = 380;
	
	//Dimensions of the canvas.
	static final double CANVAS_WIDTH = 640;
	static final double CANVAS_HEIGHT = 480;
	
	
	void sampleText(String file) {
		m_occurenceMap.clear();
		for (char c = 'A'; c <= 'Z'; ++c) {
			m_occurenceMap.put(c, 0);
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			//Read the file line by line and count the letters, storing the values in the occurence map.
			try {
				String line;
				for(;;) {
					//Read the line.
					line = reader.readLine();
					if (line==null) {
						//Reached the end of the file, break the loop.
						break;
					}
					
					//Only capital variants of letters are in the map, so force upper case, and iterate through the characters.
					for (char c : line.toUpperCase().toCharArray()) {
						//Increment the character's count if it is in the map.
						if (m_occurenceMap.containsKey(c)) {
							m_occurenceMap.replace(c, m_occurenceMap.get(c)+1);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File doesn't exist!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void repaintHistogram() {
		//Clear the canvas.
		m_gc.clearRect(0, 0, 640, 480);
		
		//Draw the x-axis
		m_gc.setStroke(Color.BLACK);
		m_gc.setFill(Color.BLACK);
		m_gc.strokeLine(10,  400,  630,  400);

		//Find the highest occurrence value (allows to size the bars relatively to the highest value)
		int maxOccurenceValue = 0;
		for (char c = 'A'; c <= 'Z'; ++c) {
			if (m_occurenceMap.get(c)>maxOccurenceValue) {
				maxOccurenceValue = m_occurenceMap.get(c);
			}
		}
		
		//Iterate through and draw letters
		char ch = 'A';
		//Iterate 26 times
		for (int i = 0; i < 26; ++i) {
			//Print letters at an X coordinate based on the number of iterations (i)
			m_gc.setTextAlign(TextAlignment.CENTER);
			m_gc.setFont(new Font("Arial", 12));
			m_gc.fillText(""+(ch), 24+((640.0d-32.0d)/26.0d)*i, 424);
			
			//Draw the occurence bars too (centered on the same x-value as the letters.
			//Scale the height of the bars based on the highest occurence value.
			double occBarHeight = OCC_BAR_MAX_HEIGHT * (((double)m_occurenceMap.get(ch))/(double)maxOccurenceValue);
			
			//Draw the bar such that the bottom of it is on the axis line. 
			m_gc.strokeRect(
					24+((640.0d-32.0d)/26.0d)*i-8, 400-occBarHeight,
					16, occBarHeight);
			
			//Go to next letter.
			ch++;
		}
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Initialise the map with empty values.
		m_occurenceMap = new HashMap<Character, Integer>();
		sampleText("");
		
		//VBox for layout
		VBox vbox = new VBox();
		HBox bottomControls = new HBox();
		
		//Make a canvas and store a reference to the graphics context.
		Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		m_gc = canvas.getGraphicsContext2D();
		
		Label label = new Label("Filename:");

		TextField fileField = new TextField();
		fileField.setMinWidth(400);
		
		//Event handler for when enter is pressed.
		//Update the occurence map and repaint the canvas.
		fileField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sampleText(fileField.getText());
				repaintHistogram();
			}
			
		});
		
		//Button that does the same thing as pressing enter 
		//because there's a button in the example picture.
		Button viewBtn = new Button("View");
		viewBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sampleText(fileField.getText());
				repaintHistogram();
			}
			
		});

		//Add the controls along the bottom to the HBox.
		bottomControls.getChildren().add(label);
		bottomControls.getChildren().add(fileField);
		bottomControls.getChildren().add(viewBtn);

		//Add the canvas and HBox to the VBox.
		vbox.getChildren().add(canvas);
		vbox.getChildren().add(bottomControls);
		
		//Paint the (currently empty) histogram.
		repaintHistogram();
		
		//Set up scene
		Scene scene = new Scene(vbox);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
