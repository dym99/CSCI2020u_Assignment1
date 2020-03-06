import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Question3 extends Application {
	public static void main(String args) {
		launch(args);
	}

	public static final int CANVAS_WIDTH = 640;
	public static final int CANVAS_HEIGHT = 480;
	
	public static final double CIRCLE_DIAMETER = 256.0d;
	public static final double CIRCLE_X = CANVAS_WIDTH*0.5d;
	public static final double CIRCLE_Y = CANVAS_HEIGHT*0.5d;
	
	public static final double DOT_RADIUS = 5.0d;
	
	//Snaps a point to the circle using simple vector math
	public Point2D SnapToCircle(Point2D _point, double diameter) {
		Point2D temp = _point;
		Point2D circleCenter = new Point2D(CANVAS_WIDTH*0.5d, CANVAS_HEIGHT*0.5d);
		
		//Move to origin
		temp = temp.subtract(circleCenter);
		
		//Normalise
		temp = temp.normalize();
		
		//Scale normalised vector
		temp = temp.multiply(diameter*0.5d);
		
		//Add the circle center back in
		temp = temp.add(circleCenter);
		
		//Return the result
		return temp;
	}
	
	public void UpdateAngles() {
		//Update line AB
		lineC.setStartX(dotA.getCenterX());
		lineC.setStartY(dotA.getCenterY());
		lineC.setEndX(dotB.getCenterX());
		lineC.setEndY(dotB.getCenterY());

		//Update line BC
		lineA.setStartX(dotB.getCenterX());
		lineA.setStartY(dotB.getCenterY());
		lineA.setEndX(dotC.getCenterX());
		lineA.setEndY(dotC.getCenterY());

		//Update line AB
		lineB.setStartX(dotC.getCenterX());
		lineB.setStartY(dotC.getCenterY());
		lineB.setEndX(dotA.getCenterX());
		lineB.setEndY(dotA.getCenterY());
		
		//Get points as vectors for angle calculation and placement for the angle text 
		Point2D pA = new Point2D(dotA.getCenterX(), dotA.getCenterY());
		Point2D pB = new Point2D(dotB.getCenterX(), dotB.getCenterY());
		Point2D pC = new Point2D(dotC.getCenterX(), dotC.getCenterY());
		
		//Get side lengths from points
		double lA = (pC.subtract(pB)).magnitude();
		double lB = (pA.subtract(pC)).magnitude();
		double lC = (pB.subtract(pA)).magnitude();

		System.out.println(String.format("%f,%f,%f\n", lA, lB, lC));
		
		//Update angle text values
		angleC.setText(String.format("%.0f", 
				Math.toDegrees(Math.acos((lA*lA - lB*lB - lC*lC) / (-2 * lB * lC)))
				));
		angleB.setText(String.format("%.0f", 
				Math.toDegrees(Math.acos((lB*lB - lA*lA - lC*lC) / (-2 * lA * lC)))
				));
		angleA.setText(String.format("%.0f", 
				Math.toDegrees(Math.acos((lC*lC - lB*lB - lA*lA) / (-2 * lA * lB)))
				));

		//Place the angle text at the dot, but inside the circle. (snap to a smaller invisible circle)
		Point2D pAngleA = SnapToCircle(pA, CIRCLE_DIAMETER*0.8d);
		Point2D pAngleB = SnapToCircle(pB, CIRCLE_DIAMETER*0.8d);
		Point2D pAngleC = SnapToCircle(pC, CIRCLE_DIAMETER*0.8d);

		//Actually set the text position
		angleA.setX(pAngleA.getX());
		angleA.setY(pAngleA.getY());
		angleB.setX(pAngleB.getX());
		angleB.setY(pAngleB.getY());
		angleC.setX(pAngleC.getX());
		angleC.setY(pAngleC.getY());
		
	}
	
	Circle circle, dotA, dotB, dotC;
	Line lineC, lineA, lineB;
	Text angleA, angleB, angleC;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Group as a container
		Group group = new Group();
		
		//Canvas for drawing (and also setting screen bounds.
		Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		group.getChildren().add(canvas);

		circle = new Circle();
		circle.setRadius(CIRCLE_DIAMETER*0.5d);
		circle.setFill(Color.TRANSPARENT);
		circle.setStroke(Color.BLACK);
		circle.setCenterX(0);
		circle.setCenterY(0);
		circle.setCenterX(CIRCLE_X);
		circle.setCenterY(CIRCLE_Y);
		
		
		//Completely random points
		Point2D pointA = new Point2D(Math.random()*CANVAS_WIDTH,Math.random()*CANVAS_HEIGHT);
		Point2D pointB = new Point2D(Math.random()*CANVAS_WIDTH,Math.random()*CANVAS_HEIGHT);
		Point2D pointC = new Point2D(Math.random()*CANVAS_WIDTH,Math.random()*CANVAS_HEIGHT);
		
		//Snap them to the circle
		pointA = SnapToCircle(pointA, CIRCLE_DIAMETER);
		pointB = SnapToCircle(pointB, CIRCLE_DIAMETER);
		pointC = SnapToCircle(pointC, CIRCLE_DIAMETER);



		//Set up dot A
		dotA = new Circle();
		dotA.setRadius(DOT_RADIUS);
		dotA.setFill(Color.RED);
		dotA.setStroke(Color.BLACK);
		dotA.setCenterX(pointA.getX());
		dotA.setCenterY(pointA.getY());
		
		//Dot A on drag event
		dotA.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//Get mouse position and snap it to the circle.
				Point2D mousePosition = new Point2D(event.getSceneX(), event.getSceneY());
				Point2D snapped = SnapToCircle(mousePosition, CIRCLE_DIAMETER);
				
				//Update dotA position
				dotA.setCenterX(snapped.getX());
				dotA.setCenterY(snapped.getY());
				
				//Update everything else dynamically
				UpdateAngles();
			}
		});

		//Set up dot B
		dotB = new Circle();
		dotB.setRadius(DOT_RADIUS);
		dotB.setFill(Color.RED);
		dotB.setStroke(Color.BLACK);
		dotB.setCenterX(pointB.getX());
		dotB.setCenterY(pointB.getY());
		
		//Dot B on drag event
		dotB.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//Get mouse position and snap it to the circle.
				Point2D mousePosition = new Point2D(event.getSceneX(), event.getSceneY());
				Point2D snapped = SnapToCircle(mousePosition, CIRCLE_DIAMETER);
				
				//Update dot B position
				dotB.setCenterX(snapped.getX());
				dotB.setCenterY(snapped.getY());
				
				//Update everything else dynamically
				UpdateAngles();
			}
		});


		//Set up dot C
		dotC = new Circle();
		dotC.setRadius(DOT_RADIUS);
		dotC.setFill(Color.RED);
		dotC.setStroke(Color.BLACK);
		dotC.setCenterX(pointC.getX());
		dotC.setCenterY(pointC.getY());
		
		//Dot C on drag event
		dotC.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//Get mouse position and snap it to the circle.
				Point2D mousePosition = new Point2D(event.getSceneX(), event.getSceneY());
				Point2D snapped = SnapToCircle(mousePosition, CIRCLE_DIAMETER);
				
				//Update dot C position
				dotC.setCenterX(snapped.getX());
				dotC.setCenterY(snapped.getY());
				
				//Update everything else dynamically
				UpdateAngles();
			}
		});

		//Set up lines
		lineC = new Line();
		lineC.setStroke(Color.BLACK);
		lineA = new Line();
		lineA.setStroke(Color.BLACK);
		lineB = new Line();
		lineB.setStroke(Color.BLACK);
		
		//Set up angle text
		angleA = new Text("");
		angleB = new Text("");
		angleC = new Text("");
		angleA.setTextAlignment(TextAlignment.CENTER);
		angleB.setTextAlignment(TextAlignment.CENTER);
		angleC.setTextAlignment(TextAlignment.CENTER);

		
		//Add all the shapes
		group.getChildren().add(circle);
		group.getChildren().add(lineC);
		group.getChildren().add(lineA);
		group.getChildren().add(lineB);
		
		//Add the dots after the shapes so they aren't obscured.
		group.getChildren().add(dotA);
		group.getChildren().add(dotB);
		group.getChildren().add(dotC);
		
		//Add the labels last so they show up on top
		group.getChildren().add(angleA);
		group.getChildren().add(angleB);
		group.getChildren().add(angleC);
		
		//Calculate the initial angles and set up the lines/text
		UpdateAngles();
		
		//Set up the scene
		Scene scene = new Scene(group);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}

