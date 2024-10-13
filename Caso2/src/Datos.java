import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Datos {

    // Atributos de la clase
    private int tamanoPagina;
    private int filasMatriz;
    private int columnasMatriz;
    private int numReferencias;
    private int numPaginasVirtuales;

    // Método para generar las referencias
    public void generarReferencias(int tamanoPagina, String nombreArchivoImagen) {
        this.tamanoPagina = tamanoPagina;
        
        try {
            // Abrir la imagen para leer sus dimensiones
            Imagen imagen = new Imagen(nombreArchivoImagen);
            this.filasMatriz = imagen.getAlto();
            this.columnasMatriz = imagen.getAncho();

            // Calcular las referencias de la imagen y el vector del mensaje
            generarArchivoReferencias(imagen);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para calcular y escribir las referencias a un archivo
    private void generarArchivoReferencias(Imagen imagen) throws IOException {
        List<String> referencias = new ArrayList<>();
        
        // Asumiendo row-major order, generamos las referencias fila por fila
        int numBytesPorFila = columnasMatriz * 3; // Cada fila tiene ancho columnas * 3 (RGB)
        numReferencias = filasMatriz * columnasMatriz * 3; // Cada pixel tiene 3 referencias (RGB)
        numPaginasVirtuales = (numReferencias * numBytesPorFila + tamanoPagina - 1) / tamanoPagina; // Redondeo para páginas virtuales

        // Crear archivo de salida
        File archivoReferencias = new File("referencias_" + tamanoPagina + ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(archivoReferencias));

        // Escribir los datos generales
        writer.write("TP: " + tamanoPagina + "\n");
        writer.write("NF: " + filasMatriz + "\n");
        writer.write("NC: " + columnasMatriz + "\n");
        writer.write("NR: " + numReferencias + "\n");
        writer.write("NP: " + numPaginasVirtuales + "\n");
        writer.write("\n");

        // Generar las referencias
        for (int i = 0; i < filasMatriz; i++) {
            for (int j = 0; j < columnasMatriz; j++) {
                // Referencias de R, G, B
                referencias.add("Imagen[" + i + "][" + j + "].R");
                referencias.add("Imagen[" + i + "][" + j + "].G");
                referencias.add("Imagen[" + i + "][" + j + "].B");
            }
        }

        // Escribir las referencias en el archivo
        for (String referencia : referencias) {
            writer.write(referencia + "\n");
        }

        writer.close();
        System.out.println("Archivo de referencias generado: " + archivoReferencias.getAbsolutePath());
    }

    // Getters y Setters si se necesitan más adelante
    public int getTamanoPagina() {
        return tamanoPagina;
    }

    public int getFilasMatriz() {
        return filasMatriz;
    }

    public int getColumnasMatriz() {
        return columnasMatriz;
    }

    public int getNumReferencias() {
        return numReferencias;
    }

    public int getNumPaginasVirtuales() {
        return numPaginasVirtuales;
    }
}