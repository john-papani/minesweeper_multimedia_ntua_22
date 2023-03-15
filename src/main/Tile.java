package main;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Tile extends StackPane {
    public Board boardD;
    public int columnTile, rowTile, value, position;

    public Rectangle border;
    public Text text = new Text();

    public Tile(int position, int value, Board boardD) {
        this.position = position;
        this.boardD = boardD;
        columnTile = position % boardD.N_COLS; // column
        rowTile = (position - columnTile) / boardD.N_ROWS; // row
        this.value = value;

        border = new Rectangle(boardD.CELL_SIZE, boardD.CELL_SIZE);
        border.setStroke(Color.LIGHTGRAY);
        border.setFill(new ImagePattern(boardD.img[10]));
        text.setFont(Font.font(15));
        text.setText(Integer.toString(value));
        text.setVisible(false);
        getChildren().addAll(border, text);

        setTranslateX(columnTile * boardD.CELL_SIZE);
        setTranslateY(rowTile * boardD.CELL_SIZE);
        setOnMouseClicked(e -> open(e, this.position));
    }

    public void open(javafx.scene.input.MouseEvent ee, int position) {
        boardD.mousePressed(position, ee, this);
    }

    public void paintingTile(int numberImg) {
        border.setFill(new ImagePattern(boardD.img[numberImg]));
    }

}
