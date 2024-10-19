import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Simulador{

    private int totalAccesos;
    private int maxMarcos;
    private LinkedList<Integer> paginasCargadas;
    private int fallasPagina;
    private String archivoAccesos;
    private int accesosExitosos;
    private boolean[] referenciaBit;

    public Simulador(int maxMarcos, String archivoAccesos) {
        this.maxMarcos = maxMarcos;
        this.paginasCargadas = new LinkedList<>();
        this.fallasPagina = 0;
        this.accesosExitosos = 0;
        this.archivoAccesos = archivoAccesos;
        this.referenciaBit = new boolean[maxMarcos];
        this.totalAccesos = 0;
    }

    public void ejecutarSimulacion() {
        long inicioSimulacion = System.nanoTime();

        Thread procesadorAccesos = new Thread(() -> {
            try (BufferedReader br = new BufferedReader(new FileReader(archivoAccesos))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    if (linea.contains(",")) {
                        procesarAcceso(linea);
                    }
                    Thread.sleep(1);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread actualizadorBits = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                resetearBitsReferencia();
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        procesadorAccesos.start();
        actualizadorBits.start();

        try {
            procesadorAccesos.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        actualizadorBits.interrupt();

        long finSimulacion = System.nanoTime();
        long tiempoEjecucion = (finSimulacion - inicioSimulacion) / 1_000_000_000;
        long minutos = tiempoEjecucion / 60;
        long segundos = tiempoEjecucion % 60;

        System.out.println("Marcos Asignados: " + maxMarcos);
        System.out.println("Total de accesos: " + totalAccesos);
        System.out.println("Éxitos: " + accesosExitosos);
        System.out.println("Fallas: " + fallasPagina);
        System.out.println("Tiempo de ejecución: " + minutos + " minutos y " + segundos + " segundos.");
    }

    private synchronized void procesarAcceso(String acceso) {
        String[] datos = acceso.split(",");
        if (datos.length < 2) return;

        try {
            int pagina = Integer.parseInt(datos[1].trim());

            if (paginasCargadas.contains(pagina)) {
                accesosExitosos++;
                referenciaBit[paginasCargadas.indexOf(pagina)] = true;
            } else {
                fallasPagina++;
                if (paginasCargadas.size() >= maxMarcos) {
                    reemplazarPagina(pagina);
                } else {
                    paginasCargadas.addLast(pagina);
                }
            }
            totalAccesos++;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private synchronized void reemplazarPagina(int nuevaPagina) {
        for (int i = 0; i < paginasCargadas.size(); i++) {
            if (!referenciaBit[i]) {
                System.out.println("Reemplazando página: " + paginasCargadas.get(i) + " por: " + nuevaPagina);
                paginasCargadas.set(i, nuevaPagina);
                referenciaBit[i] = true;
                return;
            }
        }
        System.out.println("Reemplazando la primera página: " + paginasCargadas.getFirst() + " por: " + nuevaPagina);
        paginasCargadas.removeFirst();
        paginasCargadas.addLast(nuevaPagina);
    }

    private synchronized void resetearBitsReferencia() {
        for (int i = 0; i < paginasCargadas.size(); i++) {
            referenciaBit[i] = false;
        }
    }

    public static void main(String[] args) {
        Simulador simulador = new Simulador(3, "referencias.txt");
        simulador.ejecutarSimulacion();
    }
}
