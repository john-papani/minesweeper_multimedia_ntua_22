//! https://zetcode.com/javagames/minesweeper/

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.swing.JLabel;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

public class Board {

    private final int NUM_IMAGES = 16;
    public final int CELL_SIZE = 30;

    private final int COVER_FOR_CELL = 10;
    private final int FOR_FLAG = 10;
    private final int EMPTY_CELL = 0;
    private final int MINE_CELL = 9;
    private final int FOR_SUPERBOMB_CELL = 21;
    private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    private final int FLAGGED_MINE_CELL = COVERED_MINE_CELL + FOR_FLAG;
    private final int SUPER_BOMB_CELL = MINE_CELL + FOR_SUPERBOMB_CELL;
    private final int COVERED_SUPER_BOMB_CELL = SUPER_BOMB_CELL + COVER_FOR_CELL;
    private final int FLAGGED_SUPER_BOMB = COVERED_SUPER_BOMB_CELL + FOR_FLAG;
    private final int MINE_COL_LINE_OFSUPER = SUPER_BOMB_CELL + MINE_CELL;

    private final int DRAW_MINE = 9;
    private final int DRAW_COVER = 10;
    private final int DRAW_FLAG = 11;
    private final int DRAW_WRONG_FLAG = 12;
    private final int DRAW_MINE_SUPERBOMB_LINE_COL = 13;
    private final int DRAW_SUPERBOMB = 14;
    private final int DRAW_FLAG_SUPERBOMB = 15;

    public final int N_MINES = 5;
    public final int N_ROWS = 15;
    public final int N_COLS = 15;

    private final int BOARD_WIDTH = N_COLS * CELL_SIZE + 10;
    private final int BOARD_HEIGHT = N_ROWS * CELL_SIZE + 10;

    private int[] field;
    private boolean inGame;
    private boolean flagSuperBomb = false;
    private int flags;
    public Image[] img;

    private int successfulClicks = 0;

    private int allCells;
    private String fileForMineTXT = "";
    private final Label statusbar;
    private Stage wholeStage;
    private Pane boardPane;
    private int uncoverCells;

    public Board(Pane bPane, Label lableBottom, Stage stage) {
        boardPane = bPane;
        statusbar = lableBottom;
        wholeStage = stage;
        initBoard();
    }

    private void loadImages() {
        img = new Image[NUM_IMAGES];
        for (int i = 0; i < NUM_IMAGES; i++) {
            var path = "./img/" + i + ".png";
            img[i] = new Image(path);
        }
    }

    private void initBoard() {
        boardPane.setPrefSize(BOARD_WIDTH, BOARD_HEIGHT);
        loadImages();
        newGame();
    }

    private void createMineTXT() {

        SimpleDateFormat sf = new SimpleDateFormat("yyyy_MM_dd-HH_mm_ss");
        Date date = new Date(System.currentTimeMillis());
        var datetimenow = sf.format(date);
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("./" + "mines" + datetimenow + ".txt", true))) {
            writer.write(fileForMineTXT);
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void newGame() {

        int cell;

        var random = new Random();
        inGame = true;
        flags = N_MINES;

        allCells = N_ROWS * N_COLS;
        field = new int[allCells];

        for (int i = 0; i < allCells; i++) {
            field[i] = COVER_FOR_CELL;
        }

        statusbar.setText(Integer.toString(flags));

        int i = 0;
        while (i < N_MINES) {

            int position = (int) (allCells * random.nextDouble());

            if ((position < allCells) && (field[position] != COVERED_MINE_CELL)) {

                int current_col = position % N_COLS;
                if (N_MINES > 34 && i == 0) {
                    // the first one is the super-bomb
                    field[position] = COVERED_SUPER_BOMB_CELL;
                } else {
                    field[position] = COVERED_MINE_CELL;
                }
                int mineCol = position % N_COLS;
                int mineRow = (position - mineCol) / N_ROWS;
                int hasSuperBomb = i == 0 ? 1 : 0; // if is super-bomb, hasSuperBomb will be 1

                // up + left is the 0,0 for the starting-location and the start is from 1 ( not
                // zero)
                String mineLocation = String.format("%d, %d, %d\n", mineRow + 1, mineCol + 1, hasSuperBomb);

                fileForMineTXT += mineLocation;
                i++;
                if (current_col > 0) {
                    cell = position - 1 - N_COLS;

                    if (cell >= 0 && field[cell] != COVERED_MINE_CELL && field[cell] != COVERED_SUPER_BOMB_CELL)
                        field[cell] += 1;

                    cell = position - 1;
                    if (cell >= 0 && field[cell] != COVERED_MINE_CELL && field[cell] != COVERED_SUPER_BOMB_CELL)
                        field[cell] += 1;

                    cell = position + N_COLS - 1;
                    if (cell < allCells && field[cell] != COVERED_MINE_CELL && field[cell] != COVERED_SUPER_BOMB_CELL)
                        field[cell] += 1;
                }

                cell = position - N_COLS;
                if (cell >= 0 && field[cell] != COVERED_MINE_CELL && field[cell] != COVERED_SUPER_BOMB_CELL)
                    field[cell] += 1;

                cell = position + N_COLS;
                if (cell < allCells && field[cell] != COVERED_MINE_CELL && field[cell] != COVERED_SUPER_BOMB_CELL)
                    field[cell] += 1;

                if (current_col < (N_COLS - 1)) {
                    cell = position - N_COLS + 1;
                    if (cell >= 0 && field[cell] != COVERED_MINE_CELL && field[cell] != COVERED_SUPER_BOMB_CELL)
                        field[cell] += 1;

                    cell = position + N_COLS + 1;
                    if (cell < allCells && field[cell] != COVERED_MINE_CELL && field[cell] != COVERED_SUPER_BOMB_CELL)
                        field[cell] += 1;

                    cell = position + 1;
                    if (cell < allCells && field[cell] != COVERED_MINE_CELL && field[cell] != COVERED_SUPER_BOMB_CELL)
                        field[cell] += 1;
                }
            }
        }
        createMineTXT();
        for (int j = 0; j < allCells; j++) {
            Tile tile = new Tile(j, field[j]);
            boardPane.getChildren().add(tile);

            System.out.print(field[j] + " ");
            if ((j + 1) % 15 == 0)
                System.out.print("\n");
        }
        System.out.print("\n");

    }

    private void find_empty_cells(int j) {

        int current_col = j % N_COLS;
        int cell;
        if (current_col > 0) {
            // up + left
            cell = j - N_COLS - 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
            // left
            cell = j - 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
            // down + left
            cell = j + N_COLS - 1;
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
        }

        // up
        cell = j - N_COLS;
        if (cell >= 0) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
            }
        }
        // down
        cell = j + N_COLS;
        if (cell < allCells) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
            }
        }

        if (current_col < (N_COLS - 1)) {
            // up + right
            cell = j - N_COLS + 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
            // down + right
            cell = j + N_COLS + 1;
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
            // right
            cell = j + 1;
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
        }

    }

    private void open_up(int cell) {
        int new_cell = cell - N_COLS;
        if (new_cell < allCells) {
            if (new_cell >= 0) {
                if (field[new_cell] > MINE_CELL)
                    field[new_cell] -= COVER_FOR_CELL;
                if (field[new_cell] == MINE_CELL)
                    field[new_cell] = MINE_COL_LINE_OFSUPER;
                open_up(new_cell);
            }
        }
    }

    private void open_left(int cell) {
        int current_col = cell % N_COLS;
        if (current_col > 0) {
            int new_cell = cell - 1;
            if (new_cell >= 0) {
                if (field[new_cell] > MINE_CELL)
                    field[new_cell] -= COVER_FOR_CELL;
                if (field[new_cell] == MINE_CELL)
                    field[new_cell] = MINE_COL_LINE_OFSUPER;
                open_left(new_cell);
            }
        }
    }

    private void open_down(int cell) {

        int new_cell = cell + N_COLS;
        if (new_cell < allCells) {
            if (field[new_cell] > MINE_CELL)
                field[new_cell] -= COVER_FOR_CELL;
            if (field[new_cell] == MINE_CELL)
                field[new_cell] = MINE_COL_LINE_OFSUPER;
            open_down(new_cell);
        }
    }

    private void open_right(int cell) {

        int new_cell = cell + 1;
        int current_col = new_cell % N_COLS;

        if (current_col <= (N_COLS - 1)) {
            if (new_cell < allCells) {
                if (field[new_cell] > MINE_CELL) {
                    field[new_cell] -= COVER_FOR_CELL;
                }
                if (field[new_cell] == MINE_CELL)
                    field[new_cell] = MINE_COL_LINE_OFSUPER;
            }
        }
        if (current_col < (N_COLS - 1)) {
            open_right(new_cell);
        }
    }

    private void open_one_line_one_col(int j) {
        open_left(j);
        open_up(j);
        open_down(j);
        open_right(j);
    }

    public int returnImageNumber(Tile tile) {
        int cell = tile.value;

        if (inGame && cell == MINE_CELL) {
            inGame = false;
        }
        if (!inGame) {
            if (cell == COVERED_MINE_CELL) {
                cell = DRAW_MINE;
            } else if (cell == FLAGGED_MINE_CELL || cell == FLAGGED_SUPER_BOMB) {
                cell = DRAW_FLAG;
            } else if (cell == COVERED_SUPER_BOMB_CELL) {
                cell = DRAW_SUPERBOMB;
                // } else if (cell == SUPER_BOMB_CELL) {
                // cell = DRAW_SUPERBOMB;
            } else if (cell == MINE_COL_LINE_OFSUPER) {
                cell = DRAW_MINE_SUPERBOMB_LINE_COL;
            } else if (cell > COVERED_MINE_CELL) {
                cell = DRAW_WRONG_FLAG;
            } else if (cell > MINE_CELL) {
                cell = DRAW_COVER;
            }
        } else {

            if (cell == SUPER_BOMB_CELL) {
                cell = DRAW_SUPERBOMB;
            } else if (cell == FLAGGED_MINE_CELL) {
                cell = DRAW_FLAG;
            } else if (cell == FLAGGED_SUPER_BOMB) {
                cell = DRAW_FLAG_SUPERBOMB;
            } else if (cell == MINE_COL_LINE_OFSUPER) {
                cell = DRAW_MINE_SUPERBOMB_LINE_COL;
            } else if (cell > COVERED_MINE_CELL && cell != COVERED_SUPER_BOMB_CELL) {
                cell = DRAW_FLAG;
            } else if (cell > MINE_CELL) {
                cell = DRAW_COVER;
                uncoverCells++;
            }
        }

        // if (uncover == 0 && inGame) {

        // inGame = false;
        // // statusbar.setText("Game won");
        // return -1;

        // } else if (!inGame) {
        // statusbar.setText("Game lost");
        // return -2;
        // }
        // System.out.println("position after " + cell + "\n\n");

        return cell;
    }

    public void mousePressed(int position, MouseEvent ee, Tile tile) {

        int cCol = tile.columnTile;
        int cRow = tile.rowTile;

        int x = (int) ee.getX() + CELL_SIZE * cCol;
        int y = (int) ee.getY() + CELL_SIZE + cRow;

        System.out.println("x " + x + " y " + y + " CCol " + cCol + " Row " + cRow);

        if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {
            // ! BUTTON3 == RIGHT
            // THIS IS FOR FLAGS
            if (ee.getButton() == MouseButton.SECONDARY) {
                successfulClicks++;
                if (field[(cRow * N_COLS) + cCol] > MINE_CELL
                        && field[(cRow * N_COLS) + cCol] != MINE_COL_LINE_OFSUPER
                        && field[(cRow * N_COLS) + cCol] != FLAGGED_SUPER_BOMB) {

                    if (successfulClicks <= 4 && field[(cRow * N_COLS) + cCol] == COVERED_SUPER_BOMB_CELL
                            && !flagSuperBomb) {
                        flagSuperBomb = true;
                        open_one_line_one_col((cRow * N_COLS) + cCol);
                    }

                    // open_one_line_one_col((cRow * N_COLS) + cCol);

                    if ((field[(cRow * N_COLS) + cCol] <= COVERED_MINE_CELL
                            || ((field[(cRow * N_COLS) + cCol] > SUPER_BOMB_CELL)
                                    && field[(cRow * N_COLS) + cCol] <= COVERED_SUPER_BOMB_CELL))) {

                        if (flags > 0) {
                            field[(cRow * N_COLS) + cCol] += FOR_FLAG;
                            flags--;
                            String msg = Integer.toString(flags);
                            statusbar.setText(msg);
                        } else {
                            statusbar.setText("No marks left");
                        }
                    } else {

                        field[(cRow * N_COLS) + cCol] -= FOR_FLAG;
                        flags++;
                        String msg = Integer.toString(flags);
                        statusbar.setText(msg);
                    }
                }
            }
            // NOT A FLAG
            // * this if is for testing
            else if (ee.getButton() == MouseButton.MIDDLE) {
                // doRepaint = true;
                flagSuperBomb = true;
                System.out.print(flagSuperBomb);
                field[(cRow * N_COLS) + cCol] -= COVER_FOR_CELL;
                open_one_line_one_col((cRow * N_COLS) + cCol);
                flagSuperBomb = false;
                System.out.print(flagSuperBomb);
            }
            // NOT A FLAG
            else {

                if (field[(cRow * N_COLS) + cCol] > COVERED_MINE_CELL)
                    return;

                if ((field[(cRow * N_COLS) + cCol] > MINE_CELL
                        && field[(cRow * N_COLS) + cCol] < FLAGGED_MINE_CELL)
                        || (field[(cRow * N_COLS) + cCol] > SUPER_BOMB_CELL
                                && field[(cRow * N_COLS) + cCol] < FLAGGED_SUPER_BOMB)) {

                    field[(cRow * N_COLS) + cCol] -= COVER_FOR_CELL;
                    // doRepaint = true;

                    if (field[(cRow * N_COLS) + cCol] == MINE_CELL
                            || field[(cRow * N_COLS) + cCol] == SUPER_BOMB_CELL) {
                        inGame = false;
                    }

                    if (field[(cRow * N_COLS) + cCol] == EMPTY_CELL) {
                        find_empty_cells((cRow * N_COLS) + cCol);
                    }
                }

            }
            uncoverCells = 0;
            for (int j = 0; j < N_ROWS * N_COLS; j++) {
                Tile newtile = new Tile(j, field[j]);
                int as = returnImageNumber(newtile);
                newtile.paintingTile(as);
                boardPane.getChildren().add(newtile);
            }

            if (uncoverCells == 0 && flags == 0) {
                Alert alert = new Alert(AlertType.CONFIRMATION, "END OF GAME   YOU WIN",
                        ButtonType.YES,
                        ButtonType.NO);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    newGame();
                }
            }

            if (!inGame) {
                Alert alert = new Alert(AlertType.WARNING, "Yoy Lose🤨. Again  ?",
                        ButtonType.YES,
                        ButtonType.NO);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    newGame();
                    // repaint();
                } else if (alert.getResult() == ButtonType.NO) {
                    wholeStage.close();
                }
            }
        }
    }

    private class Tile extends StackPane {
        private int columnTile, rowTile, value, position;

        private Rectangle border = new Rectangle(CELL_SIZE, CELL_SIZE);
        private Text text = new Text();

        public Tile(int position, int value) {
            this.position = position;
            columnTile = position % N_COLS;
            rowTile = (position - columnTile) / N_ROWS;
            this.value = value;

            border.setStroke(Color.LIGHTGRAY);

            border.setFill(new ImagePattern(img[10]));
            text.setFont(Font.font(15));
            text.setText(Integer.toString(value));
            text.setVisible(false);
            getChildren().addAll(border, text);

            setTranslateX(columnTile * CELL_SIZE);
            setTranslateY(rowTile * CELL_SIZE);

            setOnMouseClicked(e -> open(e, this.position));
        }

        public void open(javafx.scene.input.MouseEvent ee, int position) {
            mousePressed(position, ee, this);
        }

        public void paintingTile(int numberImg) {
            border.setFill(new ImagePattern(img[numberImg]));
        }

    }
}
