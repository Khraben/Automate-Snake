package vista;

import controlador.gameThread;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class gameFrame extends javax.swing.JFrame {

    public Clip backgroundMusic;

    gameThread gameThread;
    public long tiempoInicial;
    public int cantMov = 0;
    int[][] snakePosition = {{0, 0}, {0, 1}, {0, 2}};

    public gameFrame() {
        initComponents();
        this.setLocationRelativeTo(null);
        gameThread = new gameThread(this);
        gameThread.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        timeLabel = new javax.swing.JLabel();
        movesLabel = new javax.swing.JLabel();
        startButton = new javax.swing.JLabel();
        buttonsPane = new vista.buttonsPane();
        backgroundLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("SnakeIO");
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        timeLabel.setFont(new java.awt.Font("Bahnschrift", 1, 30)); // NOI18N
        timeLabel.setForeground(new java.awt.Color(255, 255, 255));
        timeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(timeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 65, 600, 33));

        movesLabel.setFont(new java.awt.Font("Bahnschrift", 1, 30)); // NOI18N
        movesLabel.setForeground(new java.awt.Color(255, 255, 255));
        movesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(movesLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 645, 600, 33));

        startButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/startButton.png"))); // NOI18N
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                startButtonMouseClicked(evt);
            }
        });
        getContentPane().add(startButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 670, -1, -1));
        getContentPane().add(buttonsPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(43, 101, -1, -1));

        backgroundLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/backgroundLabel.png"))); // NOI18N
        getContentPane().add(backgroundLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void startMusic() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    getClass().getResource("/sound/backgroundLoop.wav"));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startButtonMouseClicked
        startMusic();
        buttonsPane.state = true;
        startButton.setEnabled(false);
        startButton.setVisible(false);
        tiempoInicial = System.currentTimeMillis();
    }//GEN-LAST:event_startButtonMouseClicked

    public void moveSnake() {
        if (!buttonsPane.ruteList.isEmpty()) {
            System.out.println("" + buttonsPane.ruteList.get(0).get(0));
            snakeNextMove(buttonsPane.ruteList.get(0).get(0), buttonsPane.ruteList.get(0).get(1));
            buttonsPane.ruteList.remove(0);
        }
    }

    public void snakeNextMove(int nextX, int nextY) {
        //ESTAS SE ACTUALIZAN A LA POSICION ANTERIOR DE LA UBICACION DEL SIG NODO DE LA SERPIENTE
        snakePosition[0][0] = snakePosition[1][0];
        snakePosition[0][1] = snakePosition[1][1];
        snakePosition[1][0] = snakePosition[2][0];
        snakePosition[1][1] = snakePosition[2][1];
        if (buttonsPane.buttonsTable[nextX][nextY].getBackground() == Color.RED) {
            buttonsPane.objetiveList.remove(0);
        }
        snakePosition[2][0] = nextX;
        snakePosition[2][1] = nextY;
        cantMov += 1;

    }

    public void paintSnake() {
        if (buttonsPane.objetiveList.isEmpty()) {//solo si hay objetivos
        } else {
            buttonsPane.buttonsTable[snakePosition[0][0]][snakePosition[0][1]].setBackground(Color.BLACK);
            moveSnake();
            buttonsPane.buttonsTable[snakePosition[0][0]][snakePosition[0][1]].setBackground(Color.GREEN);
            buttonsPane.buttonsTable[snakePosition[1][0]][snakePosition[1][1]].setBackground(Color.GREEN);
            buttonsPane.buttonsTable[snakePosition[2][0]][snakePosition[2][1]].setBackground(Color.GREEN);
        }
        //DELAY PARA PODER APRECIAR EL MOVIMIENTO VISUALMENTE
        try {
            TimeUnit.MILLISECONDS.sleep(700);
        } catch (InterruptedException ex) {
            Logger.getLogger(gameFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateTimeLabel() {
        if (tiempoInicial != 0) {
            long tiempoActual = System.currentTimeMillis();
            long tiempoTranscurrido = (tiempoActual - tiempoInicial) / 1000; // Convertir a segundos
            long minutos = tiempoTranscurrido / 60;
            long segundos = tiempoTranscurrido % 60;
            String tiempoFormateado = String.format("%02d:%02d", minutos, segundos);
            timeLabel.setText(tiempoFormateado);

            //POR EL MOMENTO PARA PROBAR
            if (tiempoFormateado.equals("01:00")) {
                gameThread.gameOver = true;
            }
        }
    }

    public void updateMovesLabel() {
        if (tiempoInicial != 0) {
            movesLabel.setText("Cantidad de Movimientos: " + cantMov);
        }
    }

    public void updateRute() {
        if (buttonsPane.ruteList.isEmpty()) {
            if (!buttonsPane.objetiveList.isEmpty()) {
                List<Integer> origen = Arrays.asList(snakePosition[2][0], snakePosition[2][1]);
                List<Integer> destino = Arrays.asList(buttonsPane.objetiveList.get(0).get(0), buttonsPane.objetiveList.get(0).get(1));
                buttonsPane.ruteList = buttonsPane.buscarRuta(origen, destino, buttonsPane.tableGame);
                if (buttonsPane.ruteList.isEmpty()) {
                    gameThread.gameOver = true;
                }
            }

        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel backgroundLabel;
    private vista.buttonsPane buttonsPane;
    private javax.swing.JLabel movesLabel;
    private javax.swing.JLabel startButton;
    private javax.swing.JLabel timeLabel;
    // End of variables declaration//GEN-END:variables
}
