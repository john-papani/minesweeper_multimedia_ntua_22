package main;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
// import main.Board;

import java.util.Timer;

//sample class
public class GameTimer {
    // declare timer t
    Timer t;
    private int seconds;
    private Label timeTextTop;
    private Board board;
    private Stage stage;

    // constructor of the class

    public GameTimer(int seconds, Label timeTextTop, Stage stage) {
        this.seconds = seconds;
        this.timeTextTop = timeTextTop;
        this.stage = stage;

        // schedule the timer
        t = new Timer();
    }

    public void startTimer() {
        this.t = new Timer();
        t.schedule(new rt(), 0, 1000);
    }

    
    /** 
     * @param seconds
     */
    public void startTimerWithSeconds(int seconds) {
        this.seconds = seconds;
        this.t = new Timer();
        t.schedule(new rt(), 0, 1000);
    }

    public void endTimer() {
        t.cancel();
    }

    
    /** 
     * @param board
     */
    public void addBoardToTimer(Board board) {
        this.board = board;
    }

    // sub class that extends TimerTask
    class rt extends TimerTask {
        // task to perform on executing the program
        int i = 0;

        @Override
        public void run() {
            Platform.runLater(() -> { // platform.runLater === solve the problem with
                // threads
                i++;
                if (i % seconds == 0) {
                    t.cancel();
                    endTime_GameLost_rt();
                } else {
                    setTimeLabelText_rt();
                }
            });
        }

        public void setTimeLabelText_rt() {
            timeTextTop.setText(String.valueOf(seconds - (i % seconds)));
        }

        public void endTime_GameLost_rt() {
            Alert alert = new Alert(AlertType.ERROR, "You are out of time.\nDo you want to play again  ?",
                    ButtonType.YES,
                    ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                board.newGame();
            } else if (alert.getResult() == ButtonType.NO) {
                stage.close();
            }
        }

    }
    
    /** 
     * @return int
     */
    public int GameTimerGetSeconds() {
        return this.seconds;
    }

    
    /** 
     * @return Label
     */
    public Label GameTimerGetLabel() {
        return this.timeTextTop;
    }

    
    /** 
     * @return Stage
     */
    public Stage GameTimerGetStage() {
        return this.stage;
    }

}
