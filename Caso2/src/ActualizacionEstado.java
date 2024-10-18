import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class ActualizacionEstado {

    private int numMarcos;
    private LinkedList<Integer> marcos;
    private int numeroFallasPagina;
    private int numeroHits;
    private String archivoReferencias;
    private boolean[] bitR; // Bit de referencia para cada página
    private int totalReferencias; // Contador para total de referencias procesadas

    public ActualizacionEstado(int numMarcos, String archivoReferencias) {
        this.numMarcos = numMarcos;
        this.marcos = new LinkedList<>();
        this.numeroFallasPagina = 0;
        this.numeroHits = 0;
        this.archivoReferencias = archivoReferencias;
        this.bitR = new boolean[numMarcos]; // Inicializamos el arreglo de bits de referencia
        this.totalReferencias = 0; // Inicializamos el contador de referencias
    }

    public void simular() {
        long inicioTiempo = System.nanoTime(); // Iniciar tiempo de ejecución

        Thread actualizadorPaginas = new Thread(() -> {
            try (BufferedReader br = new BufferedReader(new FileReader(archivoReferencias))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    if (linea.contains(",")) {
                        procesarReferencia(linea);
                    }
                    Thread.sleep(1); // Simula los milisegundos entre pulsos de reloj
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread actualizadorBitR = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                actualizarBitR();
                try {
                    Thread.sleep(2); // Simula los 2 milisegundos entre pulsos de reloj
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Salimos del ciclo cuando el hilo es interrumpido
                }
            }
        });

        // Iniciamos los dos threads concurrentemente
        actualizadorPaginas.start();
        actualizadorBitR.start();

        // Esperamos a que el thread de actualización de páginas termine
        try {
            actualizadorPaginas.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Detener el thread de bits R después de terminar las referencias
        actualizadorBitR.interrupt();

        long finTiempo = System.nanoTime(); // Fin del tiempo de ejecución

        // Calcular el tiempo de ejecución en minutos y segundos
        long tiempoEjecucionNano = finTiempo - inicioTiempo;
        long tiempoEjecucionSegundos = tiempoEjecucionNano / 1_000_000_000;
        long minutos = tiempoEjecucionSegundos / 60;
        long segundos = tiempoEjecucionSegundos % 60;

        // Imprimir los resultados de la simulación y el tiempo de ejecución
        System.out.println("Marcos Asignados: " + numMarcos);
        System.out.println("Total de referencias: " + totalReferencias);
        System.out.println("Hits: " + numeroHits);
        System.out.println("Fallas: " + numeroFallasPagina);
        System.out.println("Tiempo de ejecución: " + minutos + " minutos y " + segundos + " segundos.");
    }

    private synchronized void procesarReferencia(String referencia) {
        String[] partes = referencia.split(",");
        if (partes.length < 2) {
            return;
        }

        try {
            int pagina = Integer.parseInt(partes[1].trim());

            if (marcos.contains(pagina)) {
                numeroHits++;
                // Actualizamos el bit R de la página
                int index = marcos.indexOf(pagina);
                bitR[index] = true; // Indicamos que la página fue referenciada
            } else {
                numeroFallasPagina++;
                if (marcos.size() >= numMarcos) {
                    // Aplicamos NRU para elegir la página a reemplazar
                    reemplazarPagina(pagina);
                } else {
                    marcos.addLast(pagina); // Añade la nueva página si hay espacio disponible
                }
            }
            totalReferencias++; // Incrementar el contador de referencias
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // Método para reemplazar la página utilizando la política NRU
    private synchronized void reemplazarPagina(int nuevaPagina) {
        // Buscamos una página con bitR = false (no referenciada recientemente)
        for (int i = 0; i < marcos.size(); i++) {
            if (!bitR[i]) {
                // Reemplazamos la página con bitR = false
                System.out.println("Reemplazando página: " + marcos.get(i) + " con página: " + nuevaPagina);
                marcos.set(i, nuevaPagina); // Reemplazamos la página
                bitR[i] = true; // Marcamos la nueva página como referenciada
                return;
            }
        }
        // Si no encontramos ninguna página no referenciada, reemplazamos la primera
        System.out.println("Reemplazando la primera página: " + marcos.getFirst() + " con página: " + nuevaPagina);
        marcos.removeFirst();
        marcos.addLast(nuevaPagina);
    }

    // Reseteamos el bit de referencia cada 2 milisegundos
    private synchronized void actualizarBitR() {
        for (int i = 0; i < marcos.size(); i++) {
            bitR[i] = false; // Reseteamos el bit R cada 2 milisegundos
        }
    }

    public static void main(String[] args) {
        ActualizacionEstado simulacion = new ActualizacionEstado(3, "referencias.txt");
        simulacion.simular();
    }
}

