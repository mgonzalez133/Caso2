import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Consola {

    private static Referencias referencias;
    private static Datos datos;
    public static void main(String[] args) throws IOException {
        
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            
            System.out.println("========= MENÚ =========");
            System.out.println("1. Opción 1: Geración de las referencias");
            System.out.println("2. Opción 2: Calcular datos buscados");
            System.out.println("3. Salir");
            System.out.print("Elige una opción: ");
            
            opcion = scanner.nextInt();
            switch (opcion) {
                case 1:
                    System.out.println("Porfavor Indique el tamaño de página");
                    int tamanoPaginas = scanner.nextInt();
                    System.out.println("Porfavor Indique el nombre del archivo que guarda la imagen con el mensaje");
                    String ruta = br.readLine();
                   

                    break;
                case 2:
                    System.out.println("Has elegido la Opción 2: ....");
                    break;
                
                    default:
                    System.out.println("Opción inválida. Por favor, intenta de nuevo.");
            }
            System.out.println(); 
        } while (opcion != 3); 

        scanner.close();
    }

    Referencias generacionReferencias = new Referencias(0, null);

}
