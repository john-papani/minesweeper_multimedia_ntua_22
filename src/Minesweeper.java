
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.module.InvalidModuleDescriptorException;
import java.util.List;
import java.util.logging.Handler;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.plaf.metal.MetalBorders.PaletteBorder;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

/**
 * Java Minesweeper Game
 *
 * Author: Jan Bodnar
 * Website: http://zetcode.com
 */

class InvalidDescriptionException extends Exception {
    public InvalidDescriptionException(String errorMessage) {
        super(errorMessage);
    }
}

class InvalidValueException extends Exception {
    public InvalidValueException(String errorMessage) {
        super(errorMessage);
    }
}

public class Minesweeper extends Application {
    public static int difficulty;
    public static int numMines;
    public static int max_time;
    public static int hasSuperMine;

    private static int MIN_TIME_EASY = 120;
    private static int MAX_TIME_EASY = 180;
    private static int MIN_TIME_HARD = 240;
    private static int MAX_TIME_HARD = 360;
    private static int MIN_MINES_EASY = 9;
    private static int MAX_MINES_EASY = 11;
    private static int MIN_MINES_HARD = 35;
    private static int MAX_MINES_HARD = 45;

    private Scene scene;

    public Parent Minesweeper1(Stage stage) {
        BorderPane rootPane = new BorderPane();
        Pane pane1 = new Pane();

        Label labelTop = new Label("HERE WILL BE THE PLAY AND START BOTTOMS");
        HBox hBoxTop = new HBox();
        HBox.setMargin(labelTop, new Insets(20, 20, 20, 20));
        hBoxTop.getChildren().add(labelTop);

        Label labelBottom = new Label("NUMBER OF BOMBS");
        HBox hBoxBottom = new HBox();
        HBox.setMargin(labelBottom, new Insets(20, 20, 20, 20));
        hBoxBottom.getChildren().add(labelBottom);

        labelTop.setStyle("-fx-font-weight: bold");
        labelBottom.setStyle("-fx-font-weight: bold");

        Board board = new Board(pane1, labelBottom, stage);

        rootPane.setTop(hBoxTop);
        rootPane.setCenter(pane1);
        rootPane.setBottom(hBoxBottom);

        return rootPane;
    }

    public static void readScenarioFile() throws Exception {
        BufferedReader reader;
        Integer[] varIntegers = { difficulty, numMines, max_time, hasSuperMine };
        try {
            reader = new BufferedReader(new FileReader(
                    "src/medialab/SCENARIO-4.txt"));
            int reader_len = 0;
            String line = reader.readLine();

            while (line != null) {
                varIntegers[reader_len] = Integer.parseInt(line);
                reader_len++;
                System.out.println(line);
                line = reader.readLine();
            }
            reader.close();
            System.out.println("len " + reader_len);
            if (reader_len != 4)
                throw new InvalidDescriptionException("Incorrect lenght file : " +
                        reader_len);
            // easy and superbomb == that is incorrect
            if (difficulty == 1 && hasSuperMine != 0)
                throw new InvalidValueException("Incorect compination, because with easy can't have superbomb");
            // number of mines out of limits
            if ((difficulty == 1 && !(numMines > MIN_MINES_EASY && numMines < MAX_MINES_EASY))
                    || (difficulty == 2 && !(numMines > MIN_MINES_HARD && numMines < MAX_MINES_HARD)))
                throw new InvalidValueException("Incorect compination, because Mines out of limits");
            // number of time out of limits
            if ((difficulty == 1 && !(max_time > MIN_TIME_EASY && max_time < MAX_TIME_EASY))
                    || (difficulty == 2 && !(max_time > MIN_TIME_HARD && max_time < MAX_TIME_HARD)))
                throw new InvalidValueException("Incorect compination, because TIME out of     limits");
        } catch (InvalidDescriptionException err) {
            err.printStackTrace();
            // throw;
        } catch (InvalidValueException err) {
            err.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            // } finally {
            // System.out.print("malakiaa egine");
        }
    }

    public static void main(String[] args) throws Exception {
        // readScenarioFile();
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        scene = new Scene(Minesweeper1(stage));

        stage.setTitle("My Minesweeper_ioannis-papani_multimedia_2022");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

}

// public class Minesweeper {

// // private EventHandler<ActionEvent> startTheGame(Stage stage) {
// // stage.close();
// // }
// Label statusBar = new Label("This will be the counter for flags");
// BorderPane bPane = new BorderPane();
// Scene scene = new Scene(bPane, 400, 400);

// Board Boardd = new Board(statusBar,bPane);
// // Boardd.N_COLS = 15;
// // Boardd.N_ROWS = 15;
// // Boardd.N_MINES = 3;
// // bPane.setBottom(Boardd);

// }

// @Override
// public void start(Stage stage) {ψ
// Label mainGameHere = new Label("epp kaleeeppp kalispera, edo tha mpei to
// game");
// Button playEasyButton = new Button("Play Easy");
// Button playHardButton = new Button("Play Hard");

// playEasyButton.setOnAction(this::clicledButton);
// playHardButton.setOnAction(this::clicledhardButton);

// HBox hBox = new HBox();
// hBox.setSpacing(10);

// HBox.setMargin(playEasyButton, new Insets(20, 20, 20, 20));
// HBox.setMargin(playHardButton, new Insets(20, 20, 20, 20));

// ObservableList list = hBox.getChildren();
// list.addAll(playEasyButton, playHardButton);

// statusBar.setStyle("-fx-font-weight: bold");

// bPane.setTop(hBox);
// bPane.setCenter(mainGameHere);
// // bPane.setBottom(statusBar);
// BorderPane.setMargin(hBox, new Insets(10, 20, 10, 80));
// BorderPane.setMargin(mainGameHere, new Insets(20, 20, 20, 20));
// BorderPane.setMargin(statusBar, new Insets(20, 20, 5, 5));

// // BorderPane.setAlignment(hBox, Pos.BOTTOM_LEFT);

// stage.setTitle("O Narkalieutis Mou");
// stage.setScene(scene);

// initUI();

// stage.show();

// }

// private void clicledButton(ActionEvent e) {
// statusBar.setText("YESSS WE DID IT");
// }

// private void clicledhardButton(ActionEvent e) {
// statusBar.setText("hard nowwwwwwwwwwwwwwwww");
// }
