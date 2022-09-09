package com.github.visual;

import com.github.fx.ScaleEventHandler;
import com.github.fx.SyncProgressBar;
import com.github.global_coefficients.CanvasProperties;
import com.github.types.Complex;
import com.github.types.ViewState;
import com.github.utility.BigDecimalFactory;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.github.global_coefficients.InitialParameters.*;
import static com.github.utility.BigDecimalFactory.halfScaled;
import static javafx.embed.swing.SwingFXUtils.toFXImage;


public class MainWindow {
    //region some fields
    private Stack<ViewState> history;
    private Stack<ViewState> undoHistory;
    private PrintTask task;
    private BufferedImage bi;
    private int imageWidth;
    private int imageHeight;
    private SyncProgressBar syncProgressBar;
    //endregion

    //region FXML fields
    @FXML
    private ImageView imageView;
    @FXML
    private Canvas canvas;
    @FXML
    private TextField textX1;
    @FXML
    private TextField textX2;
    @FXML
    private TextField textY1;
    @FXML
    private TextField textY2;
    @FXML
    private TextField textMaxIter;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label progressLabel;
    @FXML
    private Button buttonUndo;
    @FXML
    private Button buttonRedo;
    //endregion

    public void initialize() {
        syncProgressBar = new SyncProgressBar(progressBar);
        history = new Stack<>();
        undoHistory = new Stack<>();
        task = new PrintTask();
        canvas.setWidth(CanvasProperties.CANVAS_WIDTH);
        canvas.setHeight(CanvasProperties.CANVAS_HEIGHT);
        setCoordsAndPrint(INITIAL_X_1, INITIAL_X_2, INITIAL_Y_1, INITIAL_Y_2, INITIAL_MAX_ITER);
        canvas.getGraphicsContext2D().setStroke(javafx.scene.paint.Color.GREEN);
        ScaleEventHandler eventHandler = new ScaleEventHandler(this::scale, canvas);
        canvas.setOnMousePressed(eventHandler);
        canvas.setOnMouseDragged(eventHandler);
        canvas.setOnMouseReleased(eventHandler);
    }

    /**
     * used to transfer changes in border coordinates on canvas to changes in border coordinates on complex plane
     */
    private void scale(double newX1, double newX2, double newY1, double newY2) {

        ViewState oldViewState = history.peek();
//        x1 = (view.x2 - view.x1) * newX1 / imageWidth + view.x1;
//        x2 = (view.x2 - view.x1) * newX2 / imageWidth + view.x1;
//        y1 = (view.y2 - view.y1) * newY1 / imageHeight + view.y1;
//        y2 = (view.y2 - view.y1) * newY2 / imageHeight + view.y1;
        BigDecimal oldX1 = oldViewState.getX1();
        BigDecimal oldX2 = oldViewState.getX2();
        BigDecimal oldY1 = oldViewState.getY1();
        BigDecimal oldY2 = oldViewState.getY2();
        BigDecimal x1 = (halfScaled(oldX2.subtract(oldX1)).multiply(halfScaled(newX1 / imageWidth))).add(oldX1);
        BigDecimal x2 = (halfScaled(oldX2.subtract(oldX1)).multiply(halfScaled(newX2 / imageWidth))).add(oldX1);
        BigDecimal y1 = (halfScaled(oldY2.subtract(oldY1)).multiply(halfScaled(newY1 / imageHeight))).add(oldY1);
//        BigDecimal y2 = (halfScaled(oldY2.subtract(oldY1)).multiply(halfScaled(newY2 / imageHeight))).add(oldY1);
        // y2 = k*(x2-x1) + y1    <--->   if fixed then y2 is always on the main diagonal of the canvas
        BigDecimal y2 = halfScaled(x2.subtract(x1)).multiply(halfScaled(CanvasProperties.CANVAS_DIAGONAL_FACTOR_BIG_DECIMAL)).add(y1);
        setCoordsAndPrint(x1, x2, y1, y2, oldViewState.getMaxIter());
    }

    // TODO: 09/09/2022 implement scaleOut() to pass to ScaleEventHandler for scaling out


    private void setCoordsAndPrint(BigDecimal x1, BigDecimal x2, BigDecimal y1, BigDecimal y2, int maxIter) {
        ViewState item = new ViewState(x1, x2, y1, y2, maxIter);
        if (history.size() > 0 && history.peek().equals(item)) {
            return;
        }
        history.push(item);
        if (history.size() > 1) {
            buttonUndo.setDisable(false);
        }
        undoHistory.clear();
        buttonRedo.setDisable(true);
        printSet();
    }

    private void printSet() {
        task.cancel();
        progressLabel.setText("processing...");
        ViewState viewState = history.peek();
        textX1.setText(viewState.getX1().toString());
        textX2.setText(viewState.getX2().toString());
        textY1.setText(viewState.getY1().toString());
        textY2.setText(viewState.getY2().toString());
        textMaxIter.setText(String.valueOf(viewState.getMaxIter()));
        task = new PrintTask();
        task.setOnSucceeded(event -> {
            imageView.setImage(toFXImage(bi, null));
            progressLabel.setText("success in " + syncProgressBar.getTime() + " ms");
        });
        task.setOnFailed(event -> {
            throw new RuntimeException(task.getException());
        });
        task.setOnCancelled(event -> progressLabel.setText("cancelled"));
        new Thread(task).start();
    }

    /**
     * @param complex - the complex number that is fed into an iterative function
     * @param maxIter - number of final iteration, after which the drawing process is finished
     * @return number of "jumps" that it takes for the module of a complex number after iterating over equation to get equal to 2.0 or go beyond
     * <p>
     * As for the Mandelbrot Set, (Zn+1=Zn^2+C, where C=x + i* y   is a random select point from the part of the complex plane
     * which belongs to the fractal, of course)
     * <p>
     * THE FRACTAL RECTANGULAR BOUNDARIES CAN BE DESCRIBED BY A FOLLOWING PAIR OF POINTS (bot-left and top-right)
     * { {-2;-1*i} ; {1;i} } )
     * The resulting color of the point depends on number of jumps that it takes for THE MODULE of this point
     * (after iterating its coordinates) to get more than or equal to 4
     */
    private int jumpCount(Complex complex, int maxIter) {
        Complex initialComplex = complex.clone();
        int count = 0;
        BigDecimal module;
        do {
            module = complex.module_square();
            complex.multiply(complex).add(initialComplex);
            count++;
        } while ((module.compareTo(BigDecimal.valueOf(4)) <= 0) && (count <= maxIter));
        return count;
    }

    @FXML
    private void onClickPrint() {
        setCoordsAndPrint(BigDecimalFactory.scaled(textX1.getText()),
                BigDecimalFactory.scaled(textX2.getText()),
                BigDecimalFactory.scaled(textY1.getText()),
                BigDecimalFactory.scaled(textY2.getText()), Integer.parseInt(textMaxIter.getText()));
    }

    @FXML
    private void onClickReset() {
        textX1.setText(INITIAL_X_1.toString());
        textX2.setText(INITIAL_X_2.toString());
        textY1.setText(INITIAL_Y_1.toString());
        textY2.setText(INITIAL_Y_2.toString());
        textMaxIter.setText(String.valueOf(INITIAL_MAX_ITER));
        onClickPrint();
    }

    @FXML
    private void onClickUndo() {
        undoHistory.push(history.pop());
        buttonRedo.setDisable(false);
        if (history.size() == 1) {
            buttonUndo.setDisable(true);
        }
        printSet();
    }

    @FXML
    private void onClickRedo() {
        history.push(undoHistory.pop());
        if (undoHistory.size() == 0) {
            buttonRedo.setDisable(true);
        }
        buttonUndo.setDisable(false);
        printSet();
    }


    private class PrintTask extends Task<Void> {
        @Override
        protected Void call() {
            ViewState viewState = history.peek();

            imageWidth = CanvasProperties.CANVAS_WIDTH;
            imageHeight = CanvasProperties.CANVAS_HEIGHT;

            bi = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
            syncProgressBar.setZero();
            syncProgressBar.setMaxVal(imageWidth * imageHeight);
            int maxIter = viewState.getMaxIter();

            ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
            BigDecimal math_max_x = viewState.getX2();
            BigDecimal math_max_y = viewState.getY2();
            BigDecimal math_min_x = viewState.getX1();
            BigDecimal math_min_y = viewState.getY1();
            BigDecimal x_step = math_max_x.subtract(math_min_x).divide(new BigDecimal(imageWidth), BIG_DECIMAL_ROUNDING_MODE);
            BigDecimal y_step = math_max_y.subtract(math_min_y).divide(new BigDecimal(imageHeight), BIG_DECIMAL_ROUNDING_MODE);
            for (int x = 0; x < imageWidth; x++) {
                BigDecimal mathX = math_min_x.add(x_step.multiply(new BigDecimal(x)));
                int finalX = x;
                threadPool.execute(() -> {
                    for (int y = 0; y < imageHeight; y++) {
                        BigDecimal mathY = math_min_y.add(y_step.multiply(new BigDecimal(y)));
                        Complex temporaryComplex;
                        Color color;
                        int jumps;
                        temporaryComplex = new Complex(mathX, mathY);
                        jumps = jumpCount(temporaryComplex, maxIter);
                        if (jumps < viewState.getMaxIter()) {
                            //TODO: variable color with a broad spectrum
                            color = new Color((jumps * 255 / viewState.getMaxIter()), 70, 255);
                        } else color = Color.black;
                        try {
                            bi.setRGB(finalX, y, color.getRGB());
                        } catch (ArrayIndexOutOfBoundsException e) {
                            //FixMe: when crossing a border of the picture with a selection frame, some points are mis-drawn
                        }
                        syncProgressBar.incrementValue();
                        if (isCancelled()) {
                            break;
                        }
                    }
                    syncProgressBar.displayProgress();
                });
            }
            threadPool.shutdown();
            try {
                threadPool.awaitTermination(10L, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}






