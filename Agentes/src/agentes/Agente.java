
package agentes;

import java.util.Random;
import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author macario
 */
public class Agente extends Thread{
    private final String nombre;
    private int i;
    private int j;
    private ImageIcon icon;
    private ImageIcon icon2;
    private ImageIcon nave;
    private int[][] matrix;
    private JLabel tablero[][];
    private Set<int[]> naves;
    private int naveX;
    private int naveY;
    private int ocupado;
    private int[] dx= {1,0,-1,0}, dy= {0,1,0,-1};
    
    private JLabel casillaAnterior;
    Random aleatorio = new Random(System.currentTimeMillis());
    
    public Agente(String nombre, ImageIcon icon, ImageIcon icon2, int[][] matrix, JLabel tablero[][], Set<int[]> naves, ImageIcon nav){
        this.nombre = nombre;
        this.icon = icon;
        this.matrix = matrix;
        this.tablero = tablero;
        this.naves = naves;
        this.icon2 = icon2;
        this.nave = nav;
        ocupado = 0;
        
        this.i = aleatorio.nextInt(matrix.length);
        this.j = aleatorio.nextInt(matrix.length);
        tablero[i][j].setIcon(icon);     
    }
    
    @Override
    public void run(){
        int dir;
        naveX = naves.iterator().next()[0];
        naveY = naves.iterator().next()[1];

        while(true){
            System.out.println("Ocupado" + " " + ocupado);
            casillaAnterior = tablero[i][j];
            
            
            if(ocupado == 0){
                dir = aleatorio.nextInt(4);  
                while(ok(i+dx[dir],j+dy[dir])==0){
                    dir = aleatorio.nextInt(4);
                }
                i+=dx[dir];
                j+=dy[dir];
                if(matrix[i][j] == 3){
                    ocupado^=1;
                    matrix[i][j] = 0;
                    swap();
                }
                actualizarPosicion();
                
            }
            else{
                
                if(naveX == i){
                    if(naveY>j){
                        dir = 1;
                    }
                    else{
                        dir = 3;
                    }
                }
                else if(naveY==j){
                    if(naveX>i){
                        dir = 0;
                    }
                    else{
                        dir = 2;
                    }
                }
                else{
                    dir = aleatorio.nextInt(4);
                }
                
                
                while(ok2(i+dx[dir],j+dy[dir])==0){
                    dir = aleatorio.nextInt(4);
                }
                
                i+=dx[dir];
                j+=dy[dir];
                if(matrix[i][j] == 2){
                    casillaAnterior.setIcon(null);
                    tablero[i][j].setIcon(icon);
                    ocupado^=1;
                    swap();
                    try{
                        sleep(100+aleatorio.nextInt(100));
                    }
                    catch (InterruptedException ex){
                        ex.printStackTrace(System.out);
                    }
                    casillaAnterior.setIcon(icon);
                    tablero[i][j].setIcon(nave);
                    i-=dx[dir];
                    j-=dy[dir];
                }
                else{
                    actualizarPosicion();
                }
            }
                
            try{
               sleep(100+aleatorio.nextInt(100));
            }
            catch (InterruptedException ex){
                ex.printStackTrace(System.out);
            }
        }

                      
    }
    
    public int[] pos(){
        int[] res = {i,j};
        return res;
    }
    
    public void actMat(int[][] m){
        for(int i=0; i<15; i++){
            for(int j=0; j<15; j++){
                matrix[i][j] = m[i][j];
            }
        }
    }
    
    public void imp(){
        for(int i=0; i<15; i++){
            for(int j=0; j<15; j++){
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println("");
        }
    }
    
    //0: vacio, 1:robot, 2:nave, 3:muestra, 4: obstaculo
    private int ok(int x, int y){
        if(x>=0 && y>=0 && x<matrix.length && y<matrix.length){
            if(matrix[x][y]==4)  return 0;
            if(matrix[x][y]==2)  return 0;
            return 1;
        }
        return 0;
    }
    
    private int ok2(int x, int y){
        if(x>=0 && y>=0 && x<matrix.length && y<matrix.length){
            if(matrix[x][y]==4)  return 0;
            if(matrix[x][y]==3)  return 0;
            //if(matrix[x][y]==2)  return 0;
            return 1;
        }
        return 0;
    }
    
    private void swap(){
        ImageIcon aux = icon;
        icon = icon2;
        icon2 = aux;
    }
    
    public synchronized void actualizarPosicion(){
        casillaAnterior.setIcon(null); // Elimina su figura de la casilla anterior
        tablero[i][j].setIcon(icon); // Pone su figura en la nueva casilla
        //System.out.println(nombre + " in -> Row: " + i + " Col:"+ j);              
    }
    
    public synchronized void actualizarPosicionConNave(){
        casillaAnterior.setIcon(null);
        tablero[i][j].setIcon(icon);
    }
    
}
