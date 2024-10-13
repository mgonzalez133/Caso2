import java.util.Scanner;

public class Consola {

    private static Referencias referencias;
    private static Datos datos;
    public static void main(String[] args) {

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
                    System.out.println("Has elegido la Opción 1:...");
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

}
