import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Consola {

    
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
            System.out.println("3. Salir");
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

                    switch (seleccion) {
                        case 1:
                            nombreArchivo = "caso2-parrots_mod.bmp";
                            break;
                        case 2:
                            nombreArchivo = "caso2-parrots.bmp";
                            break;
                        default:
                            System.out.println("Selección no válida. Debe elegir 1 o 2.");
                            continue; // Vuelve al inicio del bucle
                    }

                    Imagen imagen = new Imagen(ruta + nombreArchivo);
                    referencias = new Referencias(imagen, tamanoPaginas);
                    referencias.crearArchivoReferencias();

                    break;
                case 2:
<<<<<<< HEAD
                    System.out.println("Has elegido la Opción 2: Calcular datos buscados.");
=======
                    System.out.println("Indique el número de marcos de página. ");
                    int marcosPagina = scanner.nextInt();
                    System.out.println("Porfavor Indique el nombre del archivo que guarda las referencias");
                    String rutaRef = br.readLine();
                    ActualizacionEstado dat = new ActualizacionEstado();
                    dat.calcularDatos(marcosPagina, rutaRef);
>>>>>>> 786b290babed24af610c82ad3231853c3603164e
                    break;
                case 3:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, intenta de nuevo.");
            }
            System.out.println(); 
        } while (opcion != 3); 

        scanner.close(); 
    }
}