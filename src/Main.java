import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application
{

    public static ObservableList<Node> child;
    //
    private static final String title = "JellyBeanci";
    public static final int width = 800;
    public static final int height = 800;
    private static Color backcolor = Color.rgb(51, 51, 51);
    private static GraphicsContext gc;
    private static Timeline update;
    private static Point2D center;
    private static final double k = 2;
    private static int piCount = 0;
    private static double hue = 0;

    private static Point2D[] possitions = new Point2D[]{
            new Point2D(1, 0),            //   0
            new Point2D(0.809, 0.588),    //   36
            new Point2D(0.309, 0.951),    //   72
            new Point2D(-0.309, 0.951),   //   108
            new Point2D(-0.809, 0.588),   //   144
            new Point2D(-1, 0),           //   180
            new Point2D(-0.809, -0.588),  //   216
            new Point2D(-0.309, -0.951),  //   252
            new Point2D(0.309, -0.951),   //   288
            new Point2D(0.809, -0.588),   //   324
    };
    private static Color[] colors = new Color[]{
            Color.hsb(0, 1, 1),
            Color.hsb(36, 1, 1),
            Color.hsb(72, 1, 1),
            Color.hsb(108, 1, 1),
            Color.hsb(144, 1, 1),
            Color.hsb(180, 1, 1),
            Color.hsb(216, 1, 1),
            Color.hsb(252, 1, 1),
            Color.hsb(288, 1, 1),
            Color.hsb(324, 1, 1)
    };

    private static ArrayList<Byte> pi_pos = new ArrayList<>();

    @Override
    public void start(Stage stage) throws Exception
    {

        File text = new File("pi_neo.txt");
        //Creating Scanner instnace to read File in Java
        Scanner scanner = new Scanner(text);

        //Reading each line of file using Scanner class
        while (scanner.hasNextLine())
        {
            String line = scanner.nextLine();
            for (int i = 0; i < line.length(); i++)
            {
                pi_pos.add((byte) Character.getNumericValue(line.charAt(i)));
            }
        }

        Pane root = new Pane();
        child = root.getChildren();
        Canvas canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(2.05);
        Color snow = Color.SNOW;
        gc.setStroke(Color.color(snow.getRed(), snow.getGreen(), snow.getBlue(), 0.5));
        center = new Point2D(width / 2, height / 2);
        //
        canvas.setLayoutX(0);
        canvas.setLayoutY(0);
        child.addAll(canvas);
        clearFrame();
        //
        root.setOnKeyPressed(e -> {
            switch (e.getCode())
            {
                case F1:
                {
                    //PLAY
                    update.play();
                    break;
                }
                case F2:
                {
                    //PAUSE
                    update.pause();
                    break;
                }
                case F3:
                {
                    //Show Child Count
                    System.out.println("Child Count: " + child.size());
                    break;
                }
            }
        });
        update = new Timeline(new KeyFrame(Duration.millis(16), e -> {
            //60 fps
            //System.out.println("loop test");
            for (int i = 0; i < 50; i++)
            {
                gc.setStroke(Color.hsb(hue, 1, 1, 0.5));
                center = drawLine(center, pi_pos.get(piCount++)); // 8
                hue += 0.02;
            }
        }));
        update.setCycleCount(Timeline.INDEFINITE);
        update.setRate(1);
        update.setAutoReverse(false);
        //update.play(); //uncomment for play when start
        //
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(new Scene(root, width - 10, height - 10, backcolor));
        stage.show();
        root.requestFocus();
    }

    private static void clearFrame()
    {
        gc.clearRect(0, 0, width, height);
    }

    private static void drawDot(double x, double y, Color color)
    {
        gc.setStroke(color);
        gc.setLineWidth(1.0);
        gc.strokeRect(x + 0.5, y + 0.5, 0.5, 0.5);
    }

    private static Point2D drawLine(Point2D startPoint, byte pos)
    {
        //gc.setStroke(colors[pos]);
        gc.strokeLine(startPoint.getX(), startPoint.getY(),
                startPoint.getX() + k * possitions[pos].getX(),
                startPoint.getY() + k * possitions[pos].getY());
        return startPoint.add(new Point2D(k * possitions[pos].getX(), k * possitions[pos].getY()));
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}