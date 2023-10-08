package kr.airout.opengltutorial;

public class SquareMatrix {
    private int rows;
    private int cols;
    private Square[][] squares;
    private int screenWidth;
    private int screenHeight;

    public SquareMatrix(int rows, int cols, int screenWidth, int screenHeight, int[][] initialColors) {
        this.rows = rows;
        this.cols = cols;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.squares = new Square[rows][cols];
        initializeSquares(initialColors);
    }

    private void initializeSquares(int[][] initialColors) {
        float sideLength = 1.0f; // 사각형의 한 변의 길이
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                float xPixel = j * sideLength;
                float yPixel = i * sideLength;
                squares[i][j] = new Square(sideLength, xPixel, yPixel, 1.0f, 1.0f, screenWidth, screenHeight, initialColors[i][j]);
            }
        }
    }

    public void draw() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                squares[i][j].draw();
            }
        }
    }

    public void updateColors(int[][] newColors) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                squares[i][j].updateColor(newColors[i][j]);
            }
        }
    }
}
