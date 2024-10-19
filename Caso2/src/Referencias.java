import java.io.File;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileWriter;

public class Referencias {

    private Imagen imagen;
    private int tamanoPagina;

    public Referencias(Imagen imagen, int tamanoPagina) {
        this.imagen = imagen;
        this.tamanoPagina = tamanoPagina;
    }

    public void crearArchivoReferencias() {
        int tamanoMensaje = imagen.leerLongitud();
        int totalReferencias = calcularTotalReferencias(tamanoMensaje);
        int totalPaginas = calcularTotalPaginas(tamanoMensaje);

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

    private void escribirReferencias(BufferedWriter escritor, int totalReferencias, int tamanoMensaje) throws IOException {
        int desplazamiento = 0;
        int paginaVirtual = 0;
        int fila = 0, columna = 0;
        String[] canales = { "R", "G", "B" };
        int canalIndice = 0;
        int indiceMensaje = 0; // Índice separado para controlar las referencias de mensaje
    
        // Calcula los datos de la imagen: ancho y alto
        int anchoImagen = imagen.getAncho();
        int altoImagen = imagen.getAlto();
    
        int tamanoImagen = anchoImagen * altoImagen * 3;
    
        int bytesImagen = tamanoImagen;
    
        int paginaInicialMensaje = bytesImagen / tamanoPagina;
        int desplazamientoMensaje = bytesImagen % tamanoPagina;
    
        for (int i = 0; i < totalReferencias; i++) {
            if (i < 16) {  // Referencias de la imagen
                escritor.write(formatearReferenciaImagen(fila, columna, canales[canalIndice], paginaVirtual, desplazamiento));
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
            } else {  
                if (i % 2 == 0) {  // Referencias del mensaje
                    escritor.write(formatearReferenciaMensaje(indiceMensaje, paginaInicialMensaje, desplazamientoMensaje));
                    indiceMensaje++; // Aumenta el índice del mensaje correctamente
                    desplazamientoMensaje++;
    
                    if (desplazamientoMensaje == tamanoPagina) {
                        desplazamientoMensaje = 0;
                        paginaInicialMensaje++;
                    }
                } else {  // Referencias de la imagen
                    escritor.write(formatearReferenciaImagen(fila, columna, canales[canalIndice], paginaVirtual, desplazamiento));
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
                }
            }
    
            escritor.newLine();
        }
    }
    
   


    private String formatearReferenciaImagen(int fila, int columna, String canal, int pagina, int offset) {
        return "Imagen[" + fila + "][" + columna + "]." + canal + "," + pagina + "," + offset + ",R";
    }

    private String formatearReferenciaMensaje(int byteMensaje, int pagina, int offset) {
        return "Mensaje[" + byteMensaje + "]," + pagina + "," + offset + ",W";
    }
}
