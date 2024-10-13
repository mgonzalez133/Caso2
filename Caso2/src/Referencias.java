import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Referencias {

    // Atributos de la clase
    private int tamanoPagina;
    private int filasMatriz;
    private int columnasMatriz;
    private int numReferencias;
    private int numPaginasVirtuales;
    private int longitudMensaje;
    private String nombreArchivoImagen;

    // Método para generar las referencias
    public void generarReferencias(int tamanoPagina, String nombreArchivoImagen) {
        this.tamanoPagina = tamanoPagina;
        this.nombreArchivoImagen = nombreArchivoImagen;
        try {
            // Abrir la imagen para leer sus dimensiones y el mensaje escondido
            Imagen imagen = new Imagen(nombreArchivoImagen);
            this.filasMatriz = imagen.getAlto();
            this.columnasMatriz = imagen.getAncho();
            this.longitudMensaje = imagen.leerLongitud(); // Leer la longitud del mensaje escondido

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
        numReferencias = filasMatriz * columnasMatriz * 3 + longitudMensaje; // Cada pixel tiene 3 referencias (RGB) más las del mensaje
        numPaginasVirtuales = (numReferencias + tamanoPagina - 1) / tamanoPagina; // Redondeo para páginas virtuales

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

        // Generar las referencias de la imagen
        int contadorBytes = 0;
        for (int i = 0; i < filasMatriz; i++) {
            for (int j = 0; j < columnasMatriz; j++) {
                // Referencias de R, G, B (lectura de la imagen)
                referencias.add(generarReferencia("Imagen", i, j, "R", contadorBytes++, tamanoPagina));
                referencias.add(generarReferencia("Imagen", i, j, "G", contadorBytes++, tamanoPagina));
                referencias.add(generarReferencia("Imagen", i, j, "B", contadorBytes++, tamanoPagina));
            }
        }

        // Generar las referencias del mensaje escondido (empezando en el byte 17)
        for (int k = 0; k < longitudMensaje; k++) {
            referencias.add(generarReferencia("Mensaje", 0, k, "W", contadorBytes++, tamanoPagina));
        }

        // Escribir las referencias en el archivo
        for (String referencia : referencias) {
            writer.write(referencia + "\n");
        }

        writer.close();
        System.out.println("Archivo de referencias generado: " + archivoReferencias.getAbsolutePath());
    }

    // Método para generar la referencia en el formato especificado
    private String generarReferencia(String tipo, int fila, int columna, String canal, int byteActual, int tamanoPagina) {
        int paginaVirtual = byteActual / tamanoPagina;
        int desplazamiento = byteActual % tamanoPagina;
        return tipo + "[" + fila + "][" + columna + "]," + paginaVirtual + "," + desplazamiento + "," + canal;
    }

    // Getters si se necesitan más adelante
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