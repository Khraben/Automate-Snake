package controlador;

import vista.gameFrame;

public class gameThread extends Thread {

    gameFrame frame;
    public boolean gameOver = false;

    public gameThread(gameFrame frame) {
        this.frame = frame;
    }

    public void run() {
        try {
            while (!gameOver) {
                frame.updateTimeLabel();
                frame.updateMovesLabel();
                frame.updateRute();
                frame.paintSnake();
                sleep(30);
            }
            frame.backgroundMusic.stop();
            frame.dispose();
        } catch (Exception e) {
            System.err.println("Error en la ejecucion del juego");
        }
    }
}
