import javax.swing.*;
import java.awt.*;

public class Graficos extends JPanel {
    private boolean pintando = true;
    private Motor instMotor;

    public Graficos(Motor m, int ancho, int alto) {
        instMotor = m;
        this.setPreferredSize(new Dimension(ancho, alto));
        this.addMouseListener(instMotor);
        this.addMouseMotionListener(instMotor);
        iniciarThread();
    }

    private void iniciarThread(){
        new Thread(() -> {
            while(pintando){
                this.repaint();
            }
        }).start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        pintarCells(g);
    }

    private void pintarCells(Graphics g){
        g.setColor(Color.GREEN);
        int[][] mat = instMotor.getMatriz();
        int tamCuad = Motor.TAM_CUAD;
        for(int i=0; i<mat.length; i++){
            for(int j=0; j<mat[0].length; j++){
                if(mat[i][j]==1) g.fillRect(j*tamCuad, i*tamCuad, tamCuad, tamCuad);
            }
        }
    }

    private void pintarCuadricula(Graphics g){

    }
}
