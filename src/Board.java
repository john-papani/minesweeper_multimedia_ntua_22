//! https://zetcode.com/javagames/minesweeper/

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FilenameFilter;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.StyleSheet;

public class Board extends JPanel {

    private final int NUM_IMAGES = 13;
    private final int CELL_SIZE = 15;

    private final int COVER_FOR_CELL = 10;
    private final int FOR_FLAG = 10;
    private final int EMPTY_CELL = 0;
    private final int MINE_CELL = 9;
    private final int FOR_SUPERBOMB_CELL = 11;
    private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    private final int FLAGGED_MINE_CELL = COVERED_MINE_CELL + FOR_FLAG;
    private final int SUPER_BOMB_CELL = COVERED_MINE_CELL + FOR_SUPERBOMB_CELL;

    private final int DRAW_MINE = 9;
    private final int DRAW_COVER = 10;
    private final int DRAW_FLAG = 11;
    private final int DRAW_WRONG_FLAG = 12;
    private final int DRAW_MINE_SUPERBOMB_LINE_COL = 13;
    private final int DRAW_SUPERBOMB = 14;

    private final int N_MINES = 10;
    private final int N_ROWS = 15;
    private final int N_COLS = 15;

    private final int BOARD_WIDTH = N_COLS * CELL_SIZE + 5;
    private final int BOARD_HEIGHT = N_ROWS * CELL_SIZE + 5;

    private int[] field;
    private boolean inGame;
    private int flags;
    private Image[] img;

    private int successfulClicks = 0;

    private int allCells;
    private final JLabel statusbar;

    public Board(JLabel statusbar) {

        this.statusbar = statusbar;
        initBoard();
    }

    private void loadImages() {
        img = new Image[NUM_IMAGES];

        for (int i = 0; i < NUM_IMAGES; i++) {

            var path = "src/img/" + i + ".png";
            img[i] = (new ImageIcon(path)).getImage();
        }

    }

    private void initBoard() {

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        loadImages();
        addMouseListener(new MinesAdapter());
        newGame();
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
                if (i == 0) {
                    field[position] = SUPER_BOMB_CELL;
                } else {
                    field[position] = COVERED_MINE_CELL;
                }
                i++;
                if (current_col > 0) {
                    cell = position - 1 - N_COLS;

                    if (cell >= 0 && field[cell] != COVERED_MINE_CELL && field[cell] != SUPER_BOMB_CELL)
                        field[cell] += 1;

                    cell = position - 1;
                    if (cell >= 0 && field[cell] != COVERED_MINE_CELL && field[cell] != SUPER_BOMB_CELL)
                        field[cell] += 1;

                    cell = position + N_COLS - 1;
                    if (cell < allCells && field[cell] != COVERED_MINE_CELL && field[cell] != SUPER_BOMB_CELL)
                        field[cell] += 1;
                }

                cell = position - N_COLS;
                if (cell >= 0 && field[cell] != COVERED_MINE_CELL && field[cell] != SUPER_BOMB_CELL)
                    field[cell] += 1;

                cell = position + N_COLS;
                if (cell < allCells && field[cell] != COVERED_MINE_CELL && field[cell] != SUPER_BOMB_CELL)
                    field[cell] += 1;

                if (current_col < (N_COLS - 1)) {
                    cell = position - N_COLS + 1;
                    if (cell >= 0 && field[cell] != COVERED_MINE_CELL && field[cell] != SUPER_BOMB_CELL)
                        field[cell] += 1;

                    cell = position + N_COLS + 1;
                    if (cell < allCells && field[cell] != COVERED_MINE_CELL && field[cell] != SUPER_BOMB_CELL)
                        field[cell] += 1;

                    cell = position + 1;
                    if (cell < allCells && field[cell] != COVERED_MINE_CELL && field[cell] != SUPER_BOMB_CELL)
                        field[cell] += 1;
                }
            }
        }
        for (int j = 0; j < allCells; j++) {
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
                if (field[new_cell] > MINE_CELL) {
                    field[new_cell] -= COVER_FOR_CELL;
                }
                open_up(new_cell);
            }
        }
    }

    private void open_left(int cell) {
        int current_col = cell % N_COLS;
        if (current_col > 0) {
            int new_cell = cell - 1;
            if (new_cell >= 0) {
                if (field[new_cell] > MINE_CELL) {
                    field[new_cell] -= COVER_FOR_CELL;
                }
                open_left(new_cell);
            }
        }
    }

    private void open_down(int cell) {
        
        int new_cell = cell + N_COLS;
        if (new_cell < allCells) {
            if (field[new_cell] > MINE_CELL) {
                field[new_cell] -= COVER_FOR_CELL;
            }
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

    @Override
    public void paintComponent(Graphics g) {

        int uncover = 0;

        for (int i = 0; i < N_ROWS; i++) {

            for (int j = 0; j < N_COLS; j++) {

                int cell = field[(i * N_COLS) + j];

                if (inGame && cell == MINE_CELL) {

                    inGame = false;
                }

                if (!inGame) {

                    if (cell == COVERED_MINE_CELL) {
                        cell = DRAW_MINE;
                    } else if (cell == FLAGGED_MINE_CELL) {
                        cell = DRAW_FLAG;
                    } else if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_WRONG_FLAG;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                    }

                } else {

                    if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_FLAG;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                        uncover++;
                    }
                }

                g.drawImage(img[cell], (j * CELL_SIZE),
                        (i * CELL_SIZE), this);
            }
        }

        if (uncover == 0 && inGame) {

            inGame = false;
            statusbar.setText("Game won");

        } else if (!inGame) {
            statusbar.setText("Game lost");
        }
    }

    private class MinesAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();
            int cCol = x / CELL_SIZE;
            int cRow = y / CELL_SIZE;

            System.out.println("x " + x + " y " + y + " CCol " + cCol + "  Row " + cRow);
            boolean doRepaint = false;

            // auto einai gia thn ennallagh anamesa sta paixnidia
            // tha prepei na to ftiaxo etsi oste na rotaei h na petaei ena munhma
            if (!inGame) {
                newGame();
                repaint();
            }
            for (int j = 0; j < allCells; j++) {
                System.out.print(j + " ");
                if ((j + 1) % N_COLS == 0)
                    System.out.print("\n");
            }
            System.out.print("\n");
            for (int j = 0; j < allCells; j++) {
                System.out.print(field[j] + " ");
                if ((j + 1) % N_COLS == 0)
                    System.out.print("\n");
            }
            System.out.print("\n");

            if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {
                // ! BUTTON3 == RIGHT
                // THIS IS FOR FLAGS
                if (e.getButton() == MouseEvent.BUTTON3) {
                    successfulClicks++;
                    if (field[(cRow * N_COLS) + cCol] > MINE_CELL) {

                        doRepaint = true;

                        if (field[(cRow * N_COLS) + cCol] <= COVERED_MINE_CELL) {

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
                // ! this if is for testing
                else if (e.getButton() == MouseEvent.BUTTON2) {
                    doRepaint = true;
                    field[(cRow * N_COLS) + cCol] -= COVER_FOR_CELL;
                    open_one_line_one_col((cRow * N_COLS) + cCol);

                } else {

                    if (field[(cRow * N_COLS) + cCol] > COVERED_MINE_CELL)
                        return;

                    if (field[(cRow * N_COLS) + cCol] > MINE_CELL
                            && field[(cRow * N_COLS) + cCol] < FLAGGED_MINE_CELL) {

                        field[(cRow * N_COLS) + cCol] -= COVER_FOR_CELL;
                        doRepaint = true;

                        if (field[(cRow * N_COLS) + cCol] == MINE_CELL) {
                            inGame = false;
                        }

                        if (field[(cRow * N_COLS) + cCol] == EMPTY_CELL) {
                            if (successfulClicks <= 4 && field[(cRow * N_COLS) + cCol] == SUPER_BOMB_CELL)
                                find_empty_cells((cRow * N_COLS) + cCol);
                            // open_one_line_one_col((cRow * N_COLS) + cCol);
                            else
                                find_empty_cells((cRow * N_COLS) + cCol);

                        }
                    }
                }

                if (doRepaint) {
                    repaint();
                }
            }
        }
    }
}