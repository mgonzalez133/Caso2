import java.io.File;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileWriter;

public class Referencias {

    private Imagen imagen;
    private int tamanoPagina;

<<<<<<< HEAD
    public Referencias(Imagen imagen, int tamanoPagina) {
        this.imagen = imagen;
        this.tamanoPagina = tamanoPagina;
    }

    public void crearArchivoReferencias() {
        int tamanoMensaje = imagen.leerLongitud();
        int totalReferencias = calcularTotalReferencias(tamanoMensaje);
        int totalPaginas = calcularTotalPaginas(tamanoMensaje);
=======
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
>>>>>>> 786b290babed24af610c82ad3231853c3603164e

        if (!asegurarDirectorio("salida")) {
            System.out.println("Error creando directorio de salida.");
            return;
        }

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter("salida/referencias.txt"))) {
            escribirInformacionArchivo(escritor, totalReferencias, totalPaginas);
            escribirReferencias(escritor, totalReferencias, tamanoMensaje);
            System.out.println("Archivo generado correctamente con " + totalReferencias + " referencias.");
        } catch (IOException e) {
            System.out.println("Error escribiendo en archivo: " + e.getMessage());
        }
    }

<<<<<<< HEAD
    private int calcularTotalReferencias(int tamanoMensaje) {
        return tamanoMensaje * 17 + 16;
    }

    private int calcularTotalPaginas(int tamanoMensaje) {
        int tamanoTotalDatos = (imagen.getAncho() * imagen.getAlto() * 3) + tamanoMensaje;

        return tamanoTotalDatos / tamanoPagina;
    }

    private boolean asegurarDirectorio(String directorio) {
        File carpeta = new File(directorio);
        if (!carpeta.exists()) {
            return carpeta.mkdir();
        }
        return true;
    }

    private void escribirInformacionArchivo(BufferedWriter escritor, int totalReferencias, int totalPaginas)
            throws IOException {
        escritor.write("P: " + tamanoPagina);
        escritor.newLine();
        escritor.write("NF: " + imagen.getAlto());
        escritor.newLine();
        escritor.write("NC: " + imagen.getAncho());
        escritor.newLine();
        escritor.write("NR: " + totalReferencias);
        escritor.newLine();
        escritor.write("Total de páginas: " + totalPaginas);
        escritor.newLine();
    }

    private void escribirReferencias(BufferedWriter escritor, int totalReferencias, int tamanoMensaje)
            throws IOException {
        int desplazamiento = 0;
        int paginaVirtual = 0;
        int fila = 0, columna = 0;
        String[] canales = { "R", "G", "B" };
        int canalIndice = 0;

        int anchoImagen = imagen.getAncho();
        int altoImagen = imagen.getAlto();

        int tamanoImagen = anchoImagen * altoImagen * 3;

        int bytesImagen = tamanoImagen;

        int paginaInicialMensaje = bytesImagen / tamanoPagina;
        int desplazamientoMensaje = bytesImagen % tamanoPagina;

        for (int i = 0; i < totalReferencias; i++) {
            if (i < 16) {
                escritor.write(
                        formatearReferenciaImagen(fila, columna, canales[canalIndice], paginaVirtual, desplazamiento));
            } else if (i % 2 == 0) {
                escritor.write(formatearReferenciaMensaje(i / 2, paginaInicialMensaje, desplazamientoMensaje));
                desplazamientoMensaje++;

                if (desplazamientoMensaje == tamanoPagina) {
                    desplazamientoMensaje = 0;
                    paginaInicialMensaje++;
                }
            } else {
                escritor.write(
                        formatearReferenciaImagen(fila, columna, canales[canalIndice], paginaVirtual, desplazamiento));
                desplazamiento++;
                canalIndice = (canalIndice + 1) % 3;

                if (desplazamiento == tamanoPagina) {
                    desplazamiento = 0;
                    paginaVirtual++;
                }

                if (canalIndice == 0) {
                    columna++;
                    if (columna == anchoImagen) {
                        columna = 0;
                        fila++;
                    }
                }
=======
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
>>>>>>> 786b290babed24af610c82ad3231853c3603164e
            }

<<<<<<< HEAD
            escritor.newLine();
=======
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
>>>>>>> 786b290babed24af610c82ad3231853c3603164e
        }
    
        // Devolver un valor por defecto para tipos desconocidos
        return "Error: Tipo desconocido - " + tipo;
    }
<<<<<<< HEAD

    private String formatearReferenciaImagen(int fila, int columna, String canal, int pagina, int offset) {
        return "Imagen[" + fila + "][" + columna + "]." + canal + "," + offset + "," + pagina + ",R";
=======
    
    // Getters si se necesitan más adelante
    public int getTamanoPagina() {
        return tamanoPagina;
>>>>>>> 786b290babed24af610c82ad3231853c3603164e
    }

    private String formatearReferenciaMensaje(int byteMensaje, int pagina, int offset) {
        return "Mensaje[" + byteMensaje + "]," + offset + "," + pagina + ",W";
    }
<<<<<<< HEAD
}
=======

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

>>>>>>> 786b290babed24af610c82ad3231853c3603164e
