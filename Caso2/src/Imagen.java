import java.io.*;

public class Imagen {
    byte[] header = new byte[54];
    byte[][][] imagen;
    int alto, ancho; // en pixeles
    int padding;
    String nombre;

    /** 
     * Método para crear una matriz imagen a partir de un archivo.
     * @param input: nombre del archivo. El formato debe ser BMP de 24 bits de bit depth
     * @pos la matriz imagen tiene los valores correspondientes a la imagen almacenada en el archivo.
     */
    public Imagen(String input) {
        nombre = new String(input);
        try {
            FileInputStream fis = new FileInputStream(nombre);
            fis.read(header);

            // Extraer el ancho y alto de la imagen desde la cabecera
            // Almacenados en little endian
            ancho = ((header[21] & 0xFF) << 24) | ((header[20] & 0xFF) << 16) |
                    ((header[19] & 0xFF) << 8) | (header[18] & 0xFF);
            alto = ((header[25] & 0xFF) << 24) | ((header[24] & 0xFF) << 16) |
                   ((header[23] & 0xFF) << 8) | (header[22] & 0xFF);

            System.out.println("Ancho: " + ancho + " px, Alto: " + alto + " px");

            imagen = new byte[alto][ancho][3];
            int rowSizeSinPadding = ancho * 3;
            // El tamaño de la fila debe ser múltiplo de 4 bytes
            padding = (4 - (rowSizeSinPadding % 4)) % 4;

            // Leer y modificar los datos de los píxeles (en formato RGB, pero almacenados en orden BGR)
            byte[] pixel = new byte[3];
            for (int i = 0; i < alto; i++) {
                for (int j = 0; j < ancho; j++) {
                    fis.read(pixel);
                    imagen[i][j][0] = pixel[0];
                    imagen[i][j][1] = pixel[1];
                    imagen[i][j][2] = pixel[2];
                }
                fis.skip(padding);
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 
     * Método para esconder un valor en una matriz imagen.
     * @param contador: contador de bytes escritos en la matriz
     * @param valor: valor que se quiere esconder
     * @param numbits: longitud (en bits) del valor
     * @pre la matriz imagen debe haber sido inicializada con una imagen
     * @pos los bits recibidos como parámetro (en valor) están escondidos en la imagen.
     */
    private void escribirBits(int contador, int valor, int numbits) {
        int bytesPorFila = ancho * 3; // ancho de la imagen en bytes
        int mascara;
        for (int i = 0; i < numbits; i++) {
            int fila = (8 * contador + i) / bytesPorFila;
            int col = ((8 * contador + i) % bytesPorFila) / 3;
            int color = ((8 * contador + i) % bytesPorFila) % 3;

            mascara = valor >> i;
            mascara = mascara & 1;
            imagen[fila][col][color] = (byte)((imagen[fila][col][color] & 0xFE) | mascara);
        }
    }

    /** 
     * Método para esconder un mensaje en una matriz imagen.
     * @param mensaje: Mensaje a esconder
     * @param longitud: longitud del mensaje
     * @pre la matriz imagen debe haber sido inicializada con una imagen
     * @pre la longitud del mensaje en bits debe ser menor que el numero de pixels de la imagen * 3
     * @pos la longitud del mensaje y el mensaje completo están escondidos en la imagen
     */
    public void esconder(char[] mensaje, int longitud) {
        int contador = 0;
        byte elByte;
        escribirBits(contador, longitud, 16); // La longitud del mensaje se esconderá en los primeros 16 bytes
        contador = 2; // El primer byte del mensaje se almacena después de la longitud (a partir del byte 17)

        for (int i = 0; i < longitud; i++) {
            elByte = (byte) mensaje[i];
            escribirBits(contador, elByte, 8);
            contador++;
            if (i % 1000 == 0) {
                System.out.println("Van " + i + " caracteres de " + longitud);
            }
        }
    }

    /** 
     * Método para escribir una imagen a un archivo en formato BMP
     * @param output: nombre del archivo donde se almacenará la imagen
     * @pre la matriz imagen debe haber sido inicializada con una imagen
     * @pos se creó el archivo en formato bmp con la información de la matriz imagen
     */
    public void escribirImagen(String output) {
        byte pad = 0;
        try {
            FileOutputStream fos = new FileOutputStream(output);
            fos.write(header);
            byte[] pixel = new byte[3];

            for (int i = 0; i < alto; i++) {
                for (int j = 0; j < ancho; j++) {
                    pixel[0] = imagen[i][j][0];
                    pixel[1] = imagen[i][j][1];
                    pixel[2] = imagen[i][j][2];
                    fos.write(pixel);
                }
                for (int k = 0; k < padding; k++) {
                    fos.write(pad);
                }
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 
     * Método para recuperar la longitud del mensaje escondido en una imagen
     * @pre la matriz imagen debe haber sido inicializada con una imagen
     * @return La longitud del mensaje que se esconde en la imagen
     */
    public int leerLongitud() {
        int longitud = 0;

        // Usamos 16 bits para almacenar la longitud del mensaje
        for (int i = 0; i < 16; i++) {
            int col = (i % (ancho * 3)) / 3;
            longitud = longitud | (imagen[0][col][((i % (ancho * 3)) % 3)] & 1) << i;
        }
        return longitud;
    }

    /** 
     * Método para recuperar un mensaje escondido en una imagen
     * @param cadena: vector de char, con espacio ya asignado
     * @param longitud: tamaño del mensaje escondido
     * @pre la matriz imagen debe haber sido inicializada con una imagen
     * @pos cadena contiene el mensaje escondido en la imagen
     */
    public void recuperar(char[] cadena, int longitud) {
        int bytesFila = ancho * 3;
        for (int posCaracter = 0; posCaracter < longitud; posCaracter++) {
            cadena[posCaracter] = 0;
            for (int i = 0; i < 8; i++) {
                int numBytes = 16 + (posCaracter * 8) + i;
                int fila = numBytes / bytesFila;
                int col = (numBytes % bytesFila) / 3;
                cadena[posCaracter] = (char) (cadena[posCaracter] |
                        (imagen[fila][col][((numBytes % bytesFila) % 3)] & 1) << i);
            }
        }
    }

    public static void main(String[] args) {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        try {
            // Ejemplo para esconder un mensaje
            System.out.println("Nombre del archivo con la imagen a procesar: ");
            String ruta = br.readLine();
            Imagen imagen = new Imagen(ruta);

            System.out.println("Nombre del archivo con el mensaje a esconder: ");
            String ruta2 = br.readLine();

            int longitud = leerArchivoTexto(ruta2);
            char[] mensaje = new char[longitud]; // Aquí deberías cargar el mensaje
            imagen.esconder(mensaje, longitud);
            imagen.escribirImagen("salida_" + ruta);

            // Ejemplo para recuperar un mensaje
            System.out.println("Nombre del archivo con el mensaje escondido: ");
            ruta = br.readLine();

            System.out.println("Nombre del archivo para almacenar el mensaje recuperado: ");
            String salida = br.readLine();

            imagen = new Imagen(ruta);
            longitud = imagen.leerLongitud();
            char[] recuperado = new char[longitud];
            imagen.recuperar(recuperado, longitud);
            escribirArchivoTexto(salida, recuperado);

            // Escribe el mensaje recuperado en el archivo de salida
            // Aquí podrías escribir los caracteres recuperados a un archivo
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int leerArchivoTexto(String rutaArchivo) {
    StringBuilder mensaje = new StringBuilder();
    
    try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            mensaje.append(linea).append("\n");
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    
    // Convertir el StringBuilder a un array de caracteres
    char[] mensajeArray = mensaje.toString().toCharArray();
    
    // Devolver la longitud del mensaje
    return mensajeArray.length;
}

    public static void escribirArchivoTexto(String rutaArchivo, char[] mensaje) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (char c : mensaje) {
                bw.write(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}