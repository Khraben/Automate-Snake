package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class buttonsPane extends JPanel {

    public JButton[][] buttonsTable;
    //FALSE: Colocando Obstaculos      //TRUE: Jugando-Colocando Manzanas
    public boolean state = false;

    public buttonsPane() {
        setLayout(new GridLayout(18, 20));
        setBackground(Color.BLACK);
        buttonsTable = new JButton[18][20];
        setTableGame();

        // Crear los botones y agregarlos al panel
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 20; j++) {
                buttonsTable[i][j] = new JButton();
                buttonsTable[i][j].setBackground(Color.BLACK); // Color de fondo negro
                buttonsTable[i][j].setPreferredSize(new Dimension(30, 30)); // Tamaño de los botones
                buttonsTable[i][j].addActionListener(new ButtonListener()); // Agregar el listener de eventos
                add(buttonsTable[i][j]); // Agregar el botón al panel
            }
        }
        //posicion inicial serpiente
        buttonsTable[0][0].setBackground(Color.GREEN);
        buttonsTable[0][1].setBackground(Color.GREEN);
        buttonsTable[0][2].setBackground(Color.GREEN);
    }

    // Clase para saber qué botón se está pulsando
    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            for (int i = 0; i < 18; i++) {
                for (int j = 0; j < 20; j++) {
                    if (button == buttonsTable[i][j]) {
                        if (!((button.getBackground() == Color.GREEN) || (button.getBackground() == Color.RED))) {
                            if (!state) {
                                button.setEnabled(false);
                                tableGame = disableCell(tableGame, i, j);
                            } else {
                                button.setBackground(Color.RED);
                                List<Integer> nextObjetive = new ArrayList<>();
                                nextObjetive.add(i);
                                nextObjetive.add(j);
                                objetiveList.add(nextObjetive);
                            }
                        }
                        return;
                    }
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////CODIGO  FUNCIONAL://///////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static List<List<Integer>> tableGame = new ArrayList<>();
    public List<List<Integer>> objetiveList = new ArrayList<>();
    public List<List<Integer>> ruteList = new ArrayList<>();

    public static void setTableGame() {
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 20; j++) {
                tableGame.add(Arrays.asList(i, j));
            }
        }

    }

    public static List<List<Integer>> disableCell(List<List<Integer>> tableGame, int row, int column) {
        java.util.List<java.util.List<Integer>> nuevoTablero = new ArrayList<>(tableGame);
        nuevoTablero.remove(Arrays.asList(row, column));
        return nuevoTablero;
    }

    public static boolean validPosition(List<Integer> pos, List<List<Integer>> tablero) {
        if (tablero.isEmpty()) {
            return false;
        }

        if (pos.equals(tablero.get(0))) {
            return true;
        }

        return validPosition(pos, tablero.subList(1, tablero.size()));
    }

    public static List<List<Integer>> neighbors(List<Integer> posicion, List<List<Integer>> tablero) {
        int fila = posicion.get(0);
        int columna = posicion.get(1);

        Predicate<List<Integer>> validPositionPredicate = pos -> validPosition(pos, tablero);

        List<List<Integer>> neighbors = new ArrayList<>();
        neighbors.add(Arrays.asList(fila - 1, columna));
        neighbors.add(Arrays.asList(fila + 1, columna));
        neighbors.add(Arrays.asList(fila, columna - 1));
        neighbors.add(Arrays.asList(fila, columna + 1));

        return neighbors.stream()
                .filter(validPositionPredicate)
                .collect(Collectors.toList());
    }

    public static List<List<Integer>> ordererNeighbors(List<Integer> posicion, List<List<Integer>> tablero, List<Integer> destino) {
        Function<List<Integer>, Integer> distanciaCalculator = pos -> distanceTo(pos, destino);
        List<List<Integer>> vecinos = neighbors(posicion, tablero);

        return vecinos.stream()
                .sorted(Comparator.comparingInt(pos -> distanciaCalculator.apply(pos)))
                .collect(Collectors.toList());
    }

    public static int distanceTo(List<Integer> pos1, List<Integer> pos2) {
        int x1 = pos1.get(0);
        int y1 = pos1.get(1);
        int x2 = pos2.get(0);
        int y2 = pos2.get(1);

        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    public static List<List<Integer>> buscarRuta(List<Integer> origen, List<Integer> destino, List<List<Integer>> tablero) {
        List<List<Integer>> ruta = new ArrayList<>();
        Set<List<Integer>> visitados = new HashSet<>();
        ruta.add(origen); // Agregar el origen a la ruta inicialmente
        if (buscarRutaRecursiva(origen, destino, tablero, ruta, visitados)) {
            return ruta;
        }
        return Collections.emptyList(); // Si no se encuentra una ruta, retornar una lista vacía
    }

    public static boolean buscarRutaRecursiva(List<Integer> posicion, List<Integer> destino, List<List<Integer>> tablero, List<List<Integer>> ruta, Set<List<Integer>> visitados) {
        if (posicion.equals(destino)) {
            return true;
        }

        if (visitados.contains(posicion)) {
            return false;
        }

        visitados.add(posicion);

        List<List<Integer>> vecinos = ordererNeighbors(posicion, tablero, destino);
        for (List<Integer> vecino : vecinos) {
            ruta.add(vecino); // Agregar el vecino a la ruta
            if (buscarRutaRecursiva(vecino, destino, tablero, ruta, visitados)) {
                return true;
            }
            ruta.remove(ruta.size() - 1); // Eliminar el vecino de la ruta si no lleva a una solución
        }

        return false;
    }
}
