import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Motor implements ActionListener, MouseListener, MouseMotionListener {
    int[][] matriz = new int[100][200];
    public static int TAM_CUAD = 7;
    public static int PAUSA_GENERACION = 100;
    Graficos instGraficos;
    JFrame instVentana;
    private boolean hacerCiclo = true;

    public static void main(String[] args) {
        new Motor();
    }

    public Motor() {
        crearVentana();
        iniciarThread();
    }

    private void crearVentana() {
        instVentana = new JFrame("Tuki");
        SwingUtilities.invokeLater(() -> {
            instVentana.setLayout(new BorderLayout());
            instGraficos = new Graficos(this, TAM_CUAD * matriz[0].length, TAM_CUAD * matriz.length);
            instVentana.add(instGraficos, BorderLayout.CENTER);
            crearBarraMenu();
            instVentana.setResizable(false);
            instVentana.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            instVentana.pack();
            instVentana.setLocationRelativeTo(null);
            instVentana.setVisible(true);
        });
    }

    private void crearBarraMenu() {
        JMenuBar barraMenu = new JMenuBar();
        JMenu menuJuego = new JMenu("Juego");

        JMenuItem itemStop = new JMenuItem("Stop");
        itemStop.setActionCommand("ITEM_STOP");
        itemStop.addActionListener(this);
        menuJuego.add(itemStop);

        JMenuItem itemPlay = new JMenuItem("Play");
        itemPlay.setActionCommand("ITEM_PLAY");
        itemPlay.addActionListener(this);
        menuJuego.add(itemPlay);

        JMenuItem itemClear = new JMenuItem("Clear");
        itemClear.setActionCommand("ITEM_CLEAR");
        itemClear.addActionListener(this);
        menuJuego.add(itemClear);

        barraMenu.add(menuJuego);
        instVentana.add(barraMenu, BorderLayout.NORTH);
    }

    public int[][] getMatriz() {
        return matriz;
    }

    private void iniciarThread() {
        new Thread(() -> {
            while (hacerCiclo) {
                try {
                    Thread.sleep(PAUSA_GENERACION);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                nuevaGeneracion();
            }
        }).start();
    }

    private void clearMatriz() {
        matriz = new int[matriz.length][matriz[0].length];
    }

    private void nuevaGeneracion() {
        int[][] nuevaMatriz = new int[matriz.length][matriz[0].length];
        for (int i = 0; i < nuevaMatriz.length; i++) {
            for (int j = 0; j < nuevaMatriz[0].length; j++) {
                nuevaMatriz[i][j] = cellVive(i, j) ? 1 : 0;
            }
        }
        matriz = nuevaMatriz;
    }

    private boolean cellVive(int x, int y) {
        int cantVec = cantVecinos(x, y);
        if (matriz[x][y] == 1) {
            return cantVec == 2 || cantVec == 3;
        } else {
            return cantVec == 3;
        }
    }

    private int cantVecinos(int x, int y) {
        int cant = 0;
        int[][] listaVec = {{x - 1, y - 1}, {x, y - 1}, {x + 1, y - 1}, {x + 1, y}, {x + 1, y + 1}, {x, y + 1}, {x - 1, y + 1}, {x - 1, y}};
        int xVec, yVec;
        for (int[] par : listaVec) {
            xVec = Math.floorMod(par[0], matriz.length);
            yVec = Math.floorMod(par[1], matriz[0].length);
            cant += matriz[xVec][yVec];
        }
        return cant;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "ITEM_STOP":
                hacerCiclo = false;
                break;
            case "ITEM_PLAY":
                if (!hacerCiclo) {
                    hacerCiclo = true;
                    iniciarThread();
                }
                break;
            case "ITEM_CLEAR":
                clearMatriz();
                break;
        }
    }

    private void agregarCell(int x, int y) {
        int cellX = x / TAM_CUAD;
        int cellY = y / TAM_CUAD;
        matriz[cellY][cellX] = 1;
    }

    private void borrarCuad(int x, int y, int tamGoma) {
        int maxX = matriz.length;
        int maxY = matriz[0].length;
        int cellX = x / TAM_CUAD;
        int cellY = y / TAM_CUAD;
        int xAct;
        int yAct;
        for (xAct = cellX - tamGoma; xAct < cellX + tamGoma; xAct++) {
            for (yAct = cellY - tamGoma; yAct < cellY + tamGoma; yAct++) {
                if (xAct >= 0 && yAct >= 0 && xAct < maxX && yAct < maxY)
                    matriz[yAct][xAct] = 0;
            }
        }
    }

    private void borrarCell(int x, int y) {
        int cellX = x / TAM_CUAD;
        int cellY = y / TAM_CUAD;
        matriz[cellY][cellX] = 0;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    int modo = 0; //0 = pintar, 1 = goma
    int tamGoma = 3;
    boolean mouseEnVentana = false;

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1) {
            modo = 0;
            agregarCell(e.getX(), e.getY());
        } else if (e.getButton() == 3) {
            modo = 1;
            borrarCuad(e.getX(), e.getY(), tamGoma);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        mouseEnVentana = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseEnVentana = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (modo == 0 && mouseEnVentana) {
            agregarCell(e.getX(), e.getY());
        } else if (modo == 1 && mouseEnVentana) {
            borrarCuad(e.getX(), e.getY(), tamGoma);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
