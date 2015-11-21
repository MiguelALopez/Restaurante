package Vista;

import Modelo.Mesa;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MapaMesas extends JPanel {
    
    private ArrayList<Mesa> mesas;

    int tamanoCuadricula;

    //Variable encargada de escalar el juego para que se tenga el tamaño correcto
    double factEscGrid;
    double factEscSquare;
    double factEscImage;
    double factEscSpace;
    double sizeGrid;
    double sizeSquare;
    double sizeImage;
    double sizeSpace;

    public MapaMesas(int tamanoCuadricula, ArrayList<Mesa> mesas) {
        this.tamanoCuadricula = tamanoCuadricula;
        this.mesas = mesas;
    }

    @Override
    public void paint(Graphics g) {
        Image imageBuffer = createImage(getWidth(), getHeight());
        Graphics graphicsBuffer = imageBuffer.getGraphics();
        //escalas(0.037, 0.9259, 0.7407, 0.0925);
        escalas(0.037, 0.9259, 0.0925, 0.4166);
        drawGrids(graphicsBuffer);
        drawText(graphicsBuffer);

        g.drawImage(imageBuffer, 0, 0, Color.cyan, null);

    }

    public void escalas(double factGrid, double factSquare, double factImage, double factSpace) {
        int n = (int) (Math.pow(mesas.size(), 0.5) +1);
        int tamanoRelativo = getHeight();//Tamano relativo usado para verificar cual eje es menor y usarlo para escalar con respecto a ese
        if (getWidth() < getHeight()) {//Se Comprueba que eje es menor
            tamanoRelativo = getWidth();
        }
        factEscGrid = tamanoRelativo * factGrid;
        factEscSquare = tamanoRelativo * factSquare;
        factEscImage = tamanoRelativo * factImage;
        factEscSpace = tamanoRelativo * factSpace;
        sizeGrid = factEscGrid / n;
        sizeSquare = (factEscSquare + (2 * sizeGrid)) / n;
        sizeImage = factEscImage / n;
        sizeSpace = factEscSpace / n;
    }

    public void drawGrids(Graphics g) {
        int n = (int) (Math.pow(mesas.size(), 0.5) +1);
        for (int x = 0; x <= n * sizeSquare; x += sizeSquare) {
            g.fillRect(x, 0, (int) (2 * sizeGrid), (int) (n * sizeSquare));
        }
        for (int y = 0; y <= n * sizeSquare; y += sizeSquare) {
            g.fillRect(0, y, (int) (n * sizeSquare) + 1, (int) (2 * sizeGrid));
        }
    }
    
    public void drawText(Graphics g){
        int n = (int) (Math.pow(mesas.size(), 0.5) +1);
        double y = sizeGrid + sizeImage + sizeSpace;
        g.setFont(new Font("TimesRoman", Font.PLAIN, (int)sizeImage));
        int k = 0;
        for (int i = 0; i < n; i++, y+= sizeSquare) {
            double x = sizeGrid+ 5;
            for (int j = 0; j < n && k < mesas.size(); j++, k++, x+= sizeSquare) {
                String f = "NF";
                if(mesas.get(k).isFumador()){
                    f = "F";
                }
                g.drawString("No. " +mesas.get(k).getNumero() + " - "+ f+ " - Cap. " + mesas.get(k).getCapacidad(),
                        (int)x, (int)y);
            }
        }
    }
    
    public static void main(String[] args) {
        ArrayList<Mesa> mesas = new ArrayList<>();
        mesas.add(new Mesa(1, 20, true, "Libre", "RESTAURANTE 1"));
        mesas.add(new Mesa(2, 15, true, "Libre", "RESTAURANTE 1"));
        mesas.add(new Mesa(3, 10, true, "Libre", "RESTAURANTE 1"));
        mesas.add(new Mesa(4, 5, true, "Libre", "RESTAURANTE 1"));
        mesas.add(new Mesa(5, 2, true, "Libre", "RESTAURANTE 1"));
        mesas.add(new Mesa(6, 14, true, "Libre", "RESTAURANTE 1"));
        mesas.add(new Mesa(7, 13, true, "Libre", "RESTAURANTE 1"));
        
        
        MapaMesas mapaMesas = new MapaMesas(5, mesas);
        JFrame frame = new JFrame();
        frame.add(mapaMesas);
        frame.setVisible(true);
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }
}
