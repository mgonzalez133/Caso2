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

    // Método principal para generar referencias
    public void generarReferencias(int tamanoPagina, String nombreArchivoImagen) {
        this.tamanoPagina = tamanoPagina;
        this.nombreArchivoImagen = nombreArchivoImagen;
        try {
            // Abrir la imagen para obtener las dimensiones y la longitud del mensaje escondido
            Imagen imagen = new Imagen(nombreArchivoImagen);
            this.filasMatriz = imagen.getAlto();
            this.columnasMatriz = imagen.getAncho();
            this.longitudMensaje = imagen.leerLongitud(); // Longitud del mensaje escondido (en bits)

            // Generar y guardar el archivo de referencias
            generarArchivoReferencias(imagen);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para generar las referencias y escribirlas en un archivo
    private void generarArchivoReferencias(Imagen imagen) throws IOException {
        List<String> referencias = new ArrayList<>();
        int numBytesPorFila = columnasMatriz * 3; // Cada fila tiene columnas * 3 bytes (RGB)
        numReferencias = filasMatriz * columnasMatriz * 3 + longitudMensaje; // Total de referencias
        numPaginasVirtuales = (numReferencias + tamanoPagina - 1) / tamanoPagina; // Redondeo hacia arriba

        // Crear archivo de salida
        File carpetaArchivos = new File(System.getProperty("user.dir") + File.separator + "archivos");
        if (!carpetaArchivos.exists()) carpetaArchivos.mkdir();
        File archivoReferencias = new File(carpetaArchivos, "referencias_" + tamanoPagina + ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(archivoReferencias));

        // Escribir los datos generales en el archivo
        writer.write("TP: " + tamanoPagina + "\n");
        writer.write("NF: " + filasMatriz + "\n");
        writer.write("NC: " + columnasMatriz + "\n");
        writer.write("NR: " + numReferencias + "\n");
        writer.write("NP: " + numPaginasVirtuales + "\n");
        writer.write("\n");

        // Generar las referencias de la imagen (byte a byte)
        int contadorBytes = 0;
        for (int i = 0; i < filasMatriz; i++) {
            for (int j = 0; j < columnasMatriz; j++) {
                // Generar referencias de los componentes R, G, B de cada pixel
                referencias.add(generarReferencia("Imagen", i, j, "R", contadorBytes++, tamanoPagina, "R"));
                referencias.add(generarReferencia("Imagen", i, j, "G", contadorBytes++, tamanoPagina, "R"));
                referencias.add(generarReferencia("Imagen", i, j, "B", contadorBytes++, tamanoPagina, "R"));
            }
        }

        // Generar las referencias del mensaje escondido (a partir del byte 17)
        for (int k = 0; k < longitudMensaje; k++) {
            referencias.add(generarReferencia("Mensaje", 0, k, "", contadorBytes++, tamanoPagina, "W"));
        }

        // Escribir todas las referencias en el archivo
        for (String referencia : referencias) {
            writer.write(referencia + "\n");
        }

        writer.close();
        System.out.println("Archivo de referencias generado: " + archivoReferencias.getAbsolutePath());
    }

    // Método para generar una referencia en el formato especificado
    private String generarReferencia(String tipo, int fila, int columna, String color, int byteActual, int tamanoPagina, String canal) {
        if (tipo == null) {
            return "Error: El tipo es nulo.";
        }
    
        int paginaVirtual = byteActual / tamanoPagina;
        int desplazamiento = byteActual % tamanoPagina;
    
        // Condición para "Mensaje"
        if (tipo.equals("Mensaje")) {
            return tipo + "[" + columna + "]," + paginaVirtual + "," + desplazamiento + "," + canal;
        }
        
        // Condición para "Imagen"
        if (tipo.equals("Imagen")) {
            return tipo + "[" + fila + "][" + columna + "]." + color + "," + paginaVirtual + "," + desplazamiento + "," + canal;
        }
    
        // Devolver un valor por defecto para tipos desconocidos
        return "Error: Tipo desconocido - " + tipo;
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

