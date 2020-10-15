/***********************************************************************************************************

 Basic Paint

 Written By Tim Hradil

 Written 1/17/20 - 1/30/20

 This is a basic paint program with the following tools (all tools are click and drag):

 Brush: Paints
 Erase: Erases
 Rect: Makes a rectangle
 Circle: Makes a circle
 Oval: Makes an oval
 Line: Creates a line
 Fill: Flood fills the clicked area based on color
 Text: Adds text from text box
 Grayscale: Turns the entire canvas to grayscale
 Invert: Inverts all the colors on the canvas

 The following options are available:

 Size: Changes the size of the brush, erase, and line tools, all other tools are variable sizes
 Color: Changes the color of the tool
 Tool: The tool to be used in the program
 Text: The text to be added using the text tool

 ***********************************************************************************************************/

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Main extends Application {

    private Text text = new Text();
    private WritableImage image;
    private Line line = new Line();
    private Ellipse oval = new Ellipse();
    private Circle circle = new Circle();
    private Rectangle rect = new Rectangle();
    private double initX = 0;
    private double initY = 0;

    public static void main(String[] args) {
        launch(args);
    }

    //Creates scene
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Basic Paint");
        Group root = new Group();
        Canvas canvas = new Canvas(800, 800);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        setup(gc, canvas, root);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    //Sets up scene
    private void setup(GraphicsContext gc, Canvas canvas, Group root) {

        //A Graphics Context Rectangle is drawn on top of the entire canvas so that the empty canvas can be filled
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,800,800);

        //Setting up the brush size slider
        Slider brushSize = new Slider(0, 100, 10);
        brushSize.setShowTickMarks(true);
        brushSize.setShowTickLabels(true);
        brushSize.setPrefWidth(canvas.getWidth()*.8);
        brushSize.setMajorTickUnit(10);
        brushSize.setBlockIncrement(10);
        brushSize.setPrefHeight(canvas.getHeight()*.05);
        brushSize.setLayoutX(canvas.getWidth()*.2);
        root.getChildren().add(brushSize);

        //Adding a label to the brush size slider
        Label brushSizeLabel = new Label("Size");
        brushSizeLabel.setTextAlignment(TextAlignment.CENTER);
        brushSizeLabel.setPrefWidth(canvas.getWidth()*.2);
        brushSizeLabel.setPrefHeight(canvas.getHeight()*.05);
        brushSizeLabel.setFont(Font.font(20));
        brushSizeLabel.setAlignment(Pos.CENTER);
        root.getChildren().add(brushSizeLabel);

        //Setting up the oolor picker
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(Color.BLACK);
        colorPicker.setLayoutY(brushSize.getLayoutY()+brushSize.getPrefHeight());
        colorPicker.setLayoutX(canvas.getWidth()*.2);
        colorPicker.setPrefWidth(canvas.getWidth()*.8);
        colorPicker.setPrefHeight(canvas.getHeight()*.05);
        root.getChildren().add(colorPicker);

        //Adding a label to the color picker
        Label colorLabel = new Label("Color");
        colorLabel.setTextAlignment(TextAlignment.CENTER);
        colorLabel.setLayoutY(colorPicker.getLayoutY());
        colorLabel.setPrefWidth(canvas.getWidth()*.2);
        colorLabel.setPrefHeight(canvas.getHeight()*.05);
        colorLabel.setFont(Font.font(20));
        colorLabel.setAlignment(Pos.CENTER);
        root.getChildren().add(colorLabel);

        //Setting up the tool combo box
        ComboBox tool = new ComboBox();
        tool.getItems().addAll(
                "Draw",
                "Erase",
                "Rectangle",
                "Circle",
                "Oval",
                "Line",
                "Fill",
                "Text",
                "Grayscale",
                "Invert"
        );
        tool.setValue("Draw");
        tool.setLayoutY(colorPicker.getLayoutY()+colorPicker.getPrefHeight());
        tool.setLayoutX(canvas.getWidth()*.2);
        tool.setPrefWidth(canvas.getWidth()*.8);
        tool.setPrefHeight(canvas.getHeight()*.05);
        tool.prefWidth(canvas.getWidth()*.8);
        root.getChildren().add(tool);

        //Adding a label to the tool picker
        Label toolLabel = new Label("Tool");
        toolLabel.setTextAlignment(TextAlignment.CENTER);
        toolLabel.setLayoutY(tool.getLayoutY());
        toolLabel.setPrefWidth(canvas.getWidth()*.2);
        toolLabel.setPrefHeight(canvas.getHeight()*.05);
        toolLabel.setFont(Font.font(20));
        toolLabel.setAlignment(Pos.CENTER);
        root.getChildren().add(toolLabel);

        //Setting the text input text field
        TextField textInput = new TextField();
        textInput.setText("Write Text Here");
        textInput.setLayoutY(tool.getLayoutY()+tool.getPrefHeight());
        textInput.setLayoutX(canvas.getWidth()*.2);
        textInput.setPrefWidth(canvas.getWidth()*.8);
        textInput.setPrefHeight(canvas.getHeight()*.05);
        textInput.prefWidth(canvas.getWidth()*.8);
        root.getChildren().add(textInput);

        //Adding a label to the text input text field
        Label textLabel = new Label("Text");
        textLabel.setTextAlignment(TextAlignment.CENTER);
        textLabel.setLayoutY(textInput.getLayoutY());
        textLabel.setPrefWidth(canvas.getWidth()*.2);
        textLabel.setPrefHeight(canvas.getHeight()*.05);
        textLabel.setFont(Font.font(20));
        textLabel.setAlignment(Pos.CENTER);
        root.getChildren().add(textLabel);

        //Adding mouse event for mouse dragging
        EventHandler<MouseEvent> eventHandlerDrag = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent edrag) {
                dragMouse(gc, edrag, (int)brushSize.getValue(), colorPicker, tool);
            }
        };
        canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, eventHandlerDrag);

        //Adding mouse event for mouse press
        EventHandler<MouseEvent> eventHandlerShapePress = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent eshape) {
                shapeMousePress(eshape,root, tool, canvas, colorPicker, gc, textInput);
            }
        };
        canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandlerShapePress);

        //Adding mouse event for mouse release
        EventHandler<MouseEvent> eventHandlerShapeRelease = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent eshape) {
                shapeMouseRelease(gc, colorPicker, eshape, root, tool);
            }
        };
        canvas.addEventFilter(MouseEvent.MOUSE_RELEASED, eventHandlerShapeRelease);
    }

    //When the mouse is dragged
    private void dragMouse(GraphicsContext gc, MouseEvent e, int r, ColorPicker color, ComboBox tool){

        //Get current x and y of mouse pointer
        double x = e.getX();
        double y = e.getY();

        //When the drawing tool is selected
        //Size is based on slider size
        if(tool.getValue().equals("Draw")){
            gc.setFill(color.getValue());
            gc.fillOval((int)x-r/2, (int)y-r/2, r, r);
        }
        //When the erase tool is selected
        //Size is based on slider size
        else if(tool.getValue().equals("Erase")){
            gc.setFill(Color.WHITE);
            gc.fillOval((int)x-r/2, (int)y-r/2, r, r);
        }
        //When the rectangle tool is selected
        //This will constantly update a rectangle in the canvas based on the current mouse position
        //The initial mouse press is the corner of the rectangle opposite of where the current x and y is where distance from this initial press is the height and width
        else if(tool.getValue().equals("Rectangle")){
            rect.setFill(color.getValue());
            if (x >= initX && y >= initY) {
                rect.setX(initX);
                rect.setY(initY);
            } else if (x < initX && y > initY) {
                rect.setX(initX-Math.abs(initX-x));
                rect.setY(initY);
            } else if (x > initX && y < initY) {
                rect.setX(initX);
                rect.setY(initY-Math.abs(initY-y));
            } else if (x < initX && y < initY) {
                rect.setX(initX-Math.abs(initX-x));
                rect.setY(initY-Math.abs(initY-y));
            }
            rect.setWidth(Math.abs(initX-x));
            rect.setHeight(Math.abs(initY-y));
        }
        //When the circle tool is selected
        //This will constantly update a circle in the canvas based on the current mouse position
        //Initial mouse press is the center and the radius is the distance from the center to the x or y position of the mouse: whichever is larger
        else if(tool.getValue().equals("Circle")){
            circle.setFill(color.getValue());
            if(Math.abs(initX-x)>Math.abs(initY-y)) {
                circle.setRadius(Math.abs(initX-x));
            }
            else {
                circle.setRadius(Math.abs(initY-y));
            }
        }
        //When the oval tool is selected
        //This will constantly update an oval in the canvas based on the current mouse position
        //Initial mouse press is the center and the radii are the distance in the x and y from the center to the current mouse position
        else if(tool.getValue().equals("Oval")){
            oval.setFill(color.getValue());
            oval.setRadiusX(Math.abs(initX-x));
            oval.setRadiusY(Math.abs(initY-y));
        }
        //When the line tool is selected
        //This will constantly update a line in the canvas based on the current mouse position
        //Initial mouse press is the start of the line and the end of the line is at the current mouse position
        //Size is based on slider size
        else if(tool.getValue().equals("Line")){
            line.setStroke(color.getValue());
            line.setEndX(x);
            line.setEndY(y);
            line.setStrokeWidth(r);
        }
        //When the text tool is selected
        //This will constantly update a text in the canvas based on the current mouse position
        //Initial mouse press is where the text starts and distance from initial press to y of mouse is the size of font
        //Text is the text from the text field
        else if(tool.getValue().equals("Text")){
            text.setSelectionFill(color.getValue());
            text.setFont(Font.font(Math.abs(initY-y)));
            text.setFill(color.getValue());
        }
    }
    //When the mouse is pressed
    private void shapeMousePress(MouseEvent e, Group root, ComboBox tool, Canvas canvas, ColorPicker color, GraphicsContext gc,TextField textInput) {

        //Sets initial x and y when mouse is pressed
        initX = e.getX();
        initY = e.getY();

        //When the rectangle tool is selected
        //Creates a rectangle with corner at the initial position with height and width zero, adds the rectangle to the canvas
        if (tool.getValue().equals("Rectangle")){
            rect.setWidth(0);
            rect.setHeight(0);
            root.getChildren().add(rect);
        }
        //When the circle tool is selected
        //Creates a circle centered at the initial position with radius 0, adds the circle to the canvas
        else if (tool.getValue().equals("Circle")){
            circle.setCenterX(initX);
            circle.setCenterY(initY);
            circle.setRadius(0);
            root.getChildren().add(circle);
        }
        //When the oval tool is selected
        //Creates an oval centered at the initial position with radii 0, adds the oval to the canvas
        else if (tool.getValue().equals("Oval")){
            oval.setCenterX(initX);
            oval.setCenterY(initY);
            oval.setRadiusX(0);
            oval.setRadiusY(0);
            root.getChildren().add(oval);
        }
        //When the line tool is selected
        //Creates a line with start position at the initial position with length 0, adds the line to the canvas
        else if(tool.getValue().equals("Line")){
            line.setStartX(initX);
            line.setStartY(initY);
            line.setEndX(initX);
            line.setEndY(initY);
            line.setStrokeWidth(0);
            line.setStrokeLineCap(StrokeLineCap.ROUND);
            root.getChildren().add(line);
        }
        //When the fill tool is selected
        //Saves the canvas as an image, runs flood fill algorithm fill() on this image at the selected position and replacement color, opens the edited image on the graphics context
        else if(tool.getValue().equals("Fill")) {
            WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
            image = canvas.snapshot(null, writableImage);
            Color targetColor = image.getPixelReader().getColor((int) initX, (int) initY);
            Color replacementColor = color.getValue();
            //runs for a 10 by 10 square to avoid filling up the heap causing a stack overflow error
            for (int i = -10; i < 10; i++)
                for (int o = -10; o < 10; o++)
                    fill((int) initX+i, (int) initY+o, targetColor, replacementColor);
            gc.drawImage(image, 0,0);
        }
        //When the text tool is selected
        //Creates a text with left at initial x and y and text from text field,, adds the text to the canvas
        else if(tool.getValue().equals("Text")){
            text.setFont(Font.font(0));
            text.setX(initX);
            text.setY(initY);
            text.setText(textInput.getText());
            root.getChildren().add(text);
        }
        //When the Grayscale tool is selected
        //Saves the canvas as an image, turns all pixels to their gray scale equivalent, opens the edited image on the graphics context
        else if(tool.getValue().equals("Grayscale")){
            WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
            image = canvas.snapshot(null, writableImage);
            for(int i=0;i<800;i++)
                for(int o=0;o<800;o++)
                    image.getPixelWriter().setColor(i,o,image.getPixelReader().getColor(i,o).grayscale());
            gc.drawImage(image, 0,0);
        }
        //When the Invert tool is selected
        //Saves the canvas as an image, turns all pixels to their inverted equivalent, opens the edited image on the graphics context
        else if(tool.getValue().equals("Invert")){
            WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
            image = canvas.snapshot(null, writableImage);
            for(int i=0;i<800;i++)
                for(int o=0;o<800;o++)
                    image.getPixelWriter().setColor(i,o,image.getPixelReader().getColor(i,o).invert());
            gc.drawImage(image, 0,0);
        }
    }
    //When the mouse is released
    private void shapeMouseRelease(GraphicsContext gc, ColorPicker color, MouseEvent e, Group root, ComboBox tool){

        //Gets position when mouse is released
        double x = e.getX();
        double y = e.getY();

        //Sets graphics context color to the chosen color
        gc.setFill(color.getValue());

        //When rectangle tool is selected
        //Draws rectangle on to graphics context, deletes rectangle from canvas
        if(tool.getValue().equals("Rectangle")){
            if (x > initX && y > initX) {
                gc.fillRect(initX, initY, x - initX, y - initY);
            } else if (x < initX && y > initY) {
                gc.fillRect(x, initY, initX - x, y - initY);
            } else if (x > initX && y < initY) {
                gc.fillRect(initX, y, x - initX, initY - y);
            } else if (x < initX && y < initY) {
                gc.fillRect(x, y, initX - x, initY - y);
            }
            root.getChildren().remove(rect);
        }
        //When circle tool is selected
        //Draws circle on to graphics context, deletes circle from canvas
        else if(tool.getValue().equals("Circle")){
            if(Math.abs(initX-x)>Math.abs(initY-y)) {
                gc.fillOval(initX-Math.abs(initX-x), initY-Math.abs(initX-x), 2*Math.abs(initX-x), 2*Math.abs(initX-x));
            }
            else {
                gc.fillOval(initX-Math.abs(initY-y), initY-Math.abs(initY-y), 2*Math.abs(initY-y), 2*Math.abs(initY-y));
            }
            root.getChildren().remove(circle);
        }
        //When oval tool is selected
        //Draws oval on to graphics context, deletes oval from canvas
        else if(tool.getValue().equals("Oval")){
            gc.fillOval(initX-Math.abs(initX-x), initY-Math.abs(initY-y), 2*Math.abs(initX-x), 2*Math.abs(initY-y));
            root.getChildren().remove(oval);
        }
        //When line tool is selected
        //Draws line on to graphics context, deletes line from canvas
        else if(tool.getValue().equals("Line")){
            gc.setLineCap(StrokeLineCap.ROUND);
            gc.setLineWidth(line.getStrokeWidth());
            gc.setStroke(color.getValue());
            gc.strokeLine(initX, initY, x, y);
            root.getChildren().remove(line);
        }
        //When text tool is selected
        //Draws text on to graphics context, deletes text from canvas
        else if(tool.getValue().equals("Text")){
            gc.setFill(color.getValue());
            gc.setFont(text.getFont());
            gc.fillText(text.getText(),text.getX(),text.getY());
            root.getChildren().remove(text);
        }

    }

    //Flood fill algorithm used for fill tool
    private void fill(int x, int y, Color targetColor, Color replacementColor){

        //Fails when trying to replace color with same color
        if(targetColor.equals(replacementColor)){
            return;
        }
        //Fails when pixel is not in bounds of image
        else if( x<=0 || x>=800 || y<=0 || y>=800 ){
            return;
        }
        //Fails when color of pixel is not the target color
        else if(!(image.getPixelReader().getColor(x,y).equals(targetColor))){
            return;
        }
        //Fails when color of pixel is already the target color
        else if(image.getPixelReader().getColor(x,y).equals(replacementColor)){
            return;
        }
        //Replaces pixel with pixel colored replacement color otherwise
        else {
            image.getPixelWriter().setColor(x, y, replacementColor);
        }
        //Calls fill for the pixels around it, 9 pixels away to avoid filling up the heap causing a stack overflow error
        fill(x,y+9,targetColor, replacementColor);
        fill(x,y-9,targetColor, replacementColor);
        fill(x-9,y,targetColor, replacementColor);
        fill(x+9,y,targetColor, replacementColor);
    }
}