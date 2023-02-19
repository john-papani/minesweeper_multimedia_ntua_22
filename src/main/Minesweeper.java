package main;
// !!! FAINETAI SAN NA MHN KANEI CLEAN 

// !!! OTAN APO 16x16, PAEI STO 9x9

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

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

    public static int scenarioID;
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

    private GameTimer gameTimer;
    public Scene scene;
    private Scene scenePopUp;
    private GridPane topPane;
    private MenuBar mb;

    public Board board;
    private Label totalminesLabel;
    private Label flagsLabel;
    private Label remainingTimeLabel;
    private BorderPane rootPane;
    private Pane pane1;

    public Parent Minesweeper1(Stage stage) {
        rootPane = new BorderPane();
        pane1 = new Pane();
        topPane = new GridPane();
        VBox vBoxMenu = createMenuBar(stage);
        HBox hBoxTop = createUpPanel();
        vBoxMenu.prefWidthProperty().bind(pane1.widthProperty());

        topPane.add(vBoxMenu, 0, 0);
        topPane.add(hBoxTop, 0, 1);

        // gameTimer = new GameTimer(8, remainingTimeLabel, board, stage);
        // aZuregiapanta.gr
        rootPane.setTop(topPane);
        rootPane.setBottom(pane1);
        // timeTextTop.setText("03:00");
        return rootPane;
    }

    public VBox createMenuBar(Stage stage) {
        // create a menu
        VBox vBoxMenu = new VBox();

        Menu menu1 = new Menu("Application");
        // create menuitems
        MenuItem menu1Item1 = new MenuItem("Create");
        MenuItem menu1Item2 = new MenuItem("Load/Start");
        // MenuItem menu1Item3 = new MenuItem("Start");
        MenuItem menu1Item4 = new MenuItem("Exit");
        // add menu items to menu1

        menu1Item1.setOnAction(e -> openCreatePopUpScenario(stage));
        menu1Item2.setOnAction(e -> loadPopUpScenario(stage));
        // menu1Item3.setOnAction(e -> startGameCreatePane_MenuItem(stage));
        menu1Item4.setOnAction(e -> exitFromMenuItem(stage));

        menu1.getItems().add(menu1Item1);
        menu1.getItems().add(menu1Item2);
        // menu1.getItems().add(menu1Item3);
        menu1.getItems().add(menu1Item4);

        Menu menu2 = new Menu("Details");
        // create menu1items
        MenuItem menu2Item1 = new MenuItem("Rounds");
        MenuItem menu2Item2 = new MenuItem("Solution");

        menu2Item1.setOnAction(e -> statisticsRounds(stage));
        menu2Item2.setOnAction(e -> solutionGameEnd(stage));
        // add menu items to menu2
        menu2.getItems().add(menu2Item1);
        menu2.getItems().add(menu2Item2);

        // create a menubar
        mb = new MenuBar(menu1, menu2);

        vBoxMenu.getChildren().add(mb);
        return vBoxMenu;
    }

    public void openCreatePopUpScenario(Stage stage) {
        if (gameTimer != null)
            gameTimer.endTimer(); // stop the previous running timer
        scenePopUp = new Scene(createPopUpNewScenario(stage));
        stage.setScene(scenePopUp);
        stage.show();
    }

    public void loadPopUpScenario(Stage stage) {
        if (gameTimer != null)
            gameTimer.endTimer(); // stop the previous running timer
        scenePopUp = new Scene(loadNewScenario(stage));
        stage.setScene(scenePopUp);
        stage.show();
    }

    public void startGameCreatePane_MenuItem(Stage stage) {
        // with start will start the timer

    }

    public void exitFromMenuItem(Stage stage) {
        stage.close();
    }

    public void statisticsRounds(Stage stage) {
        Alert alert = new Alert(AlertType.WARNING,
                "Oups! You found a bug.",
                ButtonType.CLOSE);
        alert.showAndWait();
        stage.close();
    }

    public void solutionGameEnd(Stage stage) {
        if (gameTimer != null && board != null) {
            gameTimer.endTimer();
            board.solutionGameEndRevealAll();
            Alert alert = new Alert(AlertType.WARNING, "You Lose. \nAgain  ?",
                    ButtonType.YES,
                    ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                board.newGame();
                // repaint();
            } else if (alert.getResult() == ButtonType.NO) {
                stage.close();
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING, "Start a game first",
                    ButtonType.OK);
            alert.showAndWait();
        }
    }

    public HBox createUpPanel() {
        HBox hBoxTop = new HBox();

        totalminesLabel = new Label("START");
        flagsLabel = new Label("NEW");
        remainingTimeLabel = new Label("GAME");

        totalminesLabel.setAlignment(Pos.CENTER);
        flagsLabel.setAlignment(Pos.CENTER);
        remainingTimeLabel.setAlignment(Pos.CENTER);

        totalminesLabel.setPadding(new Insets(20, 20, 20, 20));
        flagsLabel.setPadding(new Insets(20, 20, 20, 20));
        remainingTimeLabel.setPadding(new Insets(20, 20, 20, 20));

        hBoxTop.getChildren().add(totalminesLabel);
        hBoxTop.getChildren().add(flagsLabel);
        hBoxTop.getChildren().add(remainingTimeLabel);

        totalminesLabel.setStyle("-fx-font-weight: bold");
        flagsLabel.setStyle("-fx-font-weight: bold");
        remainingTimeLabel.setStyle("-fx-font-weight: bold");
        return hBoxTop;
    }

    public static void readScenarioFile(int id, Stage stage) throws Exception {
        BufferedReader reader;

        Integer[] varIntegers = { difficulty, numMines, max_time, hasSuperMine };
        try {
            reader = new BufferedReader(new FileReader(
                    "./src/medialab/SCENARIO-" + id + ".txt"));
            int reader_len = 0;
            String line = reader.readLine();

            while (line != null) {
                varIntegers[reader_len] = Integer.parseInt(line);
                reader_len++;
                line = reader.readLine();
            }
            reader.close();

            if (reader_len != 4)
                throw new InvalidDescriptionException("Incorrect lenght file : " + reader_len);

            difficulty = varIntegers[0];
            numMines = varIntegers[1];
            max_time = varIntegers[2];
            hasSuperMine = varIntegers[3];
            // easy and superbomb == that is incorrect
            if (difficulty == 1 && hasSuperMine != 0)
                throw new InvalidValueException("Incorect compination, because with easy can't have superbomb");
            // number of mines out of limits
            if ((difficulty == 1 && !(numMines >= MIN_MINES_EASY && numMines <= MAX_MINES_EASY))
                    || (difficulty == 2 && !(numMines >= MIN_MINES_HARD && numMines <= MAX_MINES_HARD)))
                throw new InvalidValueException("Incorect compination, because Mines(" + numMines + ") out of limits");
            // number of time out of limits
            if ((difficulty == 1 && !(max_time >= MIN_TIME_EASY && max_time <= MAX_TIME_EASY))
                    || (difficulty == 2 && !(max_time >= MIN_TIME_HARD && max_time <= MAX_TIME_HARD)))
                throw new InvalidValueException("Incorect compination, because TIME(" + max_time + ") out of limits");
        } catch (InvalidDescriptionException err) {
            err.printStackTrace();
            Alert alert = new Alert(AlertType.WARNING,
                    "Exception !!!\n" + err.getMessage() + "\n(This window will close)",
                    ButtonType.OK);
            alert.showAndWait();
            stage.close();
            // throw;
        } catch (InvalidValueException err) {
            err.printStackTrace();
            Alert alert = new Alert(AlertType.WARNING,
                    "Exception !!!\n" + err.getMessage() + "\n(This window will close)",
                    ButtonType.OK);
            alert.showAndWait();
            stage.close();
        } catch (IOException err) {
            err.printStackTrace();
            Alert alert = new Alert(AlertType.WARNING,
                    "Exception !!!\n" + err.getMessage() + "\n(This window will close)",
                    ButtonType.OK);
            alert.showAndWait();
            stage.close();
        }
        System.out.println("readfilescenario " + numMines);
    }

    public GridPane createPopUpNewScenario(Stage stage) {
        GridPane popupGridPane = new GridPane();
        Label scenarioIdLabel = new Label("SCENARIO-ID: ");
        TextField scenarioIdTextField = new TextField();

        Label difficultyLabel = new Label("Difficulty Level: ");
        // 1 = easy , 2 = hard
        String st[] = { "Easy", "Hard" };
        ChoiceBox difficultyChoiceBox = new ChoiceBox(FXCollections.observableArrayList(st));
        difficultyChoiceBox.setValue(st[0]);

        Label numMinesLabel = new Label("Number of Mines: ");
        TextField numMinesTextField = new TextField();

        Label superBombLabel = new Label("SuperBomb: ");

        String st1[] = { "No", "Yes" };
        ChoiceBox superBombchoiceBox = new ChoiceBox(FXCollections.observableArrayList(st1));
        superBombchoiceBox.setValue(st1[0]);

        Label timeLabel = new Label("Time(sec): ");
        Spinner<Integer> timeSpinner = new Spinner<>(120, 360, 120, 10);
        Button saveNewScenarioButton = new Button("Save");
        saveNewScenarioButton.setPadding(new Insets(10));
        Button goBackButton = new Button("Back");
        goBackButton.setPadding(new Insets(7));

        popupGridPane
                .setBackground(new Background(new BackgroundFill(Color.GAINSBORO, new CornerRadii(0), new Insets(0))));
        popupGridPane.setPadding(new Insets(10, 10, 10, 10));
        popupGridPane.setVgap(10);
        popupGridPane.setHgap(10);

        popupGridPane.add(scenarioIdLabel, 0, 0);
        popupGridPane.add(scenarioIdTextField, 1, 0);

        popupGridPane.add(difficultyLabel, 0, 1);
        popupGridPane.add(difficultyChoiceBox, 1, 1);

        popupGridPane.add(numMinesLabel, 0, 2);
        popupGridPane.add(numMinesTextField, 1, 2);

        popupGridPane.add(superBombLabel, 0, 3);
        popupGridPane.add(superBombchoiceBox, 1, 3);

        popupGridPane.add(timeLabel, 0, 4);
        popupGridPane.add(timeSpinner, 1, 4);
        popupGridPane.add(saveNewScenarioButton, 1, 5);
        popupGridPane.add(goBackButton, 0, 5);
        goBackButton.setOnAction(e -> stage.setScene(scene));

        saveNewScenarioButton.setOnAction(e -> {
            int difficultyValue = difficultyChoiceBox.getSelectionModel().selectedIndexProperty().getValue() + 1;
            int superBombValue = superBombchoiceBox.getSelectionModel().selectedIndexProperty().getValue();
            saveNewScenarioLocally(scenarioIdTextField.getText(), difficultyValue,
                    numMinesTextField.getText(), superBombValue, timeSpinner.getValue());
            stage.setScene(scene);
        });
        return popupGridPane;
    }

    public void saveNewScenarioLocally(String scenarioID, int difficultyValue, String numMines, int superBombValue,
            Integer time) {
        String newScenario = String.format("%d\n%d\n%d\n%d", difficultyValue, Integer.parseInt(numMines), time,
                superBombValue);
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("./src/medialab/SCENARIO-" + scenarioID + ".txt", true))) {
            writer.write(newScenario);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GridPane loadNewScenario(Stage stage) {
        GridPane popupGridPane = new GridPane();
        Label scenarioIdLabel = new Label("Please select scenario(ID): ");
        TextField scenarioIdTextField = new TextField();
        Button saveNewScenarioButton = new Button("Load");
        saveNewScenarioButton.setPadding(new Insets(8));
        Button goBackButton = new Button("Back");
        goBackButton.setPadding(new Insets(7));
        popupGridPane
                .setBackground(new Background(new BackgroundFill(Color.GAINSBORO, new CornerRadii(0), new Insets(0))));
        popupGridPane.setPadding(new Insets(10, 10, 10, 10));
        popupGridPane.setVgap(10);
        popupGridPane.setHgap(10);

        popupGridPane.add(scenarioIdLabel, 0, 0);
        popupGridPane.add(scenarioIdTextField, 1, 0);
        popupGridPane.add(saveNewScenarioButton, 1, 1);
        popupGridPane.add(goBackButton, 0, 1);
        goBackButton.setOnAction(e -> stage.setScene(scene));
        saveNewScenarioButton.setOnAction(e -> {
            try {
                // if scenario id is wrong (not number) the program will exit!
                if (scenarioIdTextField.getText() == null | scenarioIdTextField.getText().equals("")) {
                    Alert alert = new Alert(AlertType.WARNING, "Choose a valid Scenario Id next time",
                            ButtonType.OK);
                    alert.showAndWait();
                }
                readScenarioFile(Integer.parseInt(scenarioIdTextField.getText()), stage);
            } catch (Exception e1) {
                e1.printStackTrace();
                stage.close();
            }
            gameTimer = new GameTimer(max_time + 1, remainingTimeLabel, stage); // +1 DELETE IT (MAYBE)
            board = new Board(difficulty, numMines, pane1, flagsLabel,
                    totalminesLabel, stage, gameTimer);
            gameTimer.addBoardToTimer(board);
            stage.setScene(scene);
            System.out.println("loadnewscenario " + numMines);
        });
        return popupGridPane;
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Minesweeper a = new Minesweeper();
        scene = new Scene(Minesweeper1(stage));
        // stage.setTitle("Minesweeper_ioannis-papani_multimedia_2022");
        stage.setTitle("MediaLab Minesweeper");
        stage.setScene(scene);
        // stage.setResizable(false);
        stage.show();
    }

}