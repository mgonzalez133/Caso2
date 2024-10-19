import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Consola {

    private static Referencias referencias;
    private static Simulador simulador;

    public static void main(String[] args) throws IOException {

        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n");
            System.out.println("========= MENÚ =========");
            System.out.println("1. Generación de las referencias");
            System.out.println("2. Calcular datos buscados");
            System.out.println("3. Esconder un mensaje ");
            System.out.println("4. Recuperar un mensaje");
            System.out.println("5. Salir");
            System.out.print("Elige una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    System.out.println("Por favor, indique el tamaño de página:");
                    int tamanoPaginas = scanner.nextInt();
                    System.out.println("Por favor, seleccione la imagen:");
                    System.out.println("1. caso2-parrots_mod.bmp");
                    System.out.println("2. caso2-parrots.bmp");
                    int seleccion = scanner.nextInt();
                    System.out.println(System.getProperty("user.dir") + "/Archivos/");
                    String ruta = System.getProperty("user.dir") + "/Archivos/";
                    String nombreArchivo = "";
                    String nombreArchivoRef = "";

                    switch (seleccion) {
                        case 1:
                            nombreArchivo = "caso2-parrots_mod.bmp";
                            break;
                        case 2:
                            nombreArchivo = "caso2-parrots.bmp";
                            break;
                        default:
                            System.out.println("Selección no válida. Debe elegir 1 o 2.");
                            continue; 
                    }

                    Imagen imagen = new Imagen(ruta + nombreArchivo);
                    referencias = new Referencias(imagen, tamanoPaginas);
                    referencias.crearArchivoReferencias();

                    break;
                case 2:
                    System.out.println("Has elegido la Opción 2: Calcular datos buscados.");
                    nombreArchivoRef = "referencias.txt";
                    System.out.println("Indique el número de marcos de página");
                    int marcospag = scanner.nextInt();
                    String rutaref = System.getProperty("user.dir") + "/Salida/";
                    nombreArchivoRef = "referencias.txt";
                    simulador = new Simulador(marcospag, rutaref + nombreArchivoRef);
                    simulador.ejecutarSimulacion();

                    break;

                case 3:
                    System.out.println("Seleccione una de las siguientes imagenes:");
                    System.out.println("1. caso2-parrots_mod.bmp");
                    System.out.println("2. caso2-parrots.bmp");
                    System.out.println("3. Otro");

                    int opcionImagenEsconder = scanner.nextInt();
                    scanner.nextLine(); 

                    // Determinar la ruta base
                    String rutaBaseImagenEsconder = System.getProperty("user.dir") + "/archivos/";
                    String archivoImagenEsconder = "";

                    switch (opcionImagenEsconder) {
                        case 1:
                            archivoImagenEsconder = "caso2-parrots_mod.bmp";
                            break;
                        case 2:
                            archivoImagenEsconder = "caso2-parrots.bmp";
                            break;
                        case 3:
                            System.out.println(
                                    "Ingrese el nombre del archivo:");
                            archivoImagenEsconder = scanner.nextLine();
                            break;
                        default:
                            System.out.println("Seleccion no valida.");
                            break;
                    }

                    imagen = new Imagen(rutaBaseImagenEsconder + archivoImagenEsconder);

                    System.out.println("Seleccione uno de los siguientes archivos de texto archivo de texto con el mensaje para esconder:");
                    System.out.println("1. caso2-mensaje_dollshousep1.txt");
                    System.out.println("2. 100.txt");
                    System.out.println("3. 1000.txt");
                    System.out.println("4. 2000.txt");
                    System.out.println("5. 4000.txt");
                    System.out.println("6. 8000.txt");
                    System.out.println("7. Otro");

                    int opcionTextoEsconder = scanner.nextInt();
                    scanner.nextLine(); 

                    String archivoTextoEsconder = "";

                    switch (opcionTextoEsconder) {
                        case 1:
                            archivoTextoEsconder = "caso2-mensaje_dollshousep1.txt";
                            break;
                        case 2:
                            archivoTextoEsconder = "100.txt";
                            break;
                        case 3:
                            archivoTextoEsconder = "1000.txt";
                            break;
                        case 4:
                            archivoTextoEsconder = "2000.txt";
                            break;
                        case 5:
                            archivoTextoEsconder = "4000.txt";
                            break;
                        case 6:
                            archivoTextoEsconder = "8000.txt";
                            break;
                        case 7:
                            System.out.println(
                                    "Ingrese el nombre del archivo de texto:");
                            archivoTextoEsconder = scanner.nextLine();
                            break;
                        default:
                            System.out.println("Selección no válida.");
                            break;
                    }

                    StringBuilder contenidoTextoEsconder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(
                            new FileReader(rutaBaseImagenEsconder + archivoTextoEsconder))) {
                        String linea;
                        while ((linea = reader.readLine()) != null) {
                            contenidoTextoEsconder.append(linea).append("\n");
                        }
                    } catch (Exception e) {
                        System.out.println("Error leyendo el archivo de texto: " + e.getMessage());
                        break;
                    }

                    char[] mensajeEsconder = contenidoTextoEsconder.toString().toCharArray();
                    imagen.esconder(mensajeEsconder, mensajeEsconder.length);

                    String archivoSalidaEsconder = "salida_con_mensaje.bmp";
                    imagen.escribirImagen(rutaBaseImagenEsconder + archivoSalidaEsconder);
                    System.out.println("Mensaje escondido exitosamente en " + archivoSalidaEsconder);
                    break;

                case 4:
                    System.out.println("Seleccione el archivo BMP con el mensaje escondido:");
                    System.out.println("1. salida_con_mensaje.bmp");
                    System.out.println("2. Otro (especifique el nombre del archivo)");

                    int opcionImagenRecuperar = scanner.nextInt();
                    scanner.nextLine(); 

                    String rutaBaseImagenRecuperar = System.getProperty("user.dir") + "/Archivos/";
                    String archivoImagenRecuperar = "";

                    switch (opcionImagenRecuperar) {
                        case 1:
                            archivoImagenRecuperar = "salida_con_mensaje.bmp";
                            break;
                        case 2:
                            System.out.println(
                                    "Ingrese el nombre del archivo BMP (incluyendo la extensión, ej: salida.bmp):");
                            archivoImagenRecuperar = scanner.nextLine();
                            break;
                        default:
                            System.out.println("Selección no válida. Debe elegir entre 1 o 2.");
                            break;
                    }

                    imagen = new Imagen(rutaBaseImagenRecuperar + archivoImagenRecuperar);

                    int longitudMensajeRecuperar = imagen.leerLongitud();
                    char[] mensajeRecuperado = new char[longitudMensajeRecuperar];

                    imagen.recuperar(mensajeRecuperado, longitudMensajeRecuperar);
                    System.out.println("Mensaje recuperado: " + new String(mensajeRecuperado));
                    break;
                case 5:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, intenta de nuevo.");
            }

            System.out.println();
        } while (opcion != 5);

        scanner.close();
    }
}