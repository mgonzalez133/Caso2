
public class ActualizacionEstado extends Thread{

    private int marcosPagina;
    private String nombreArchivoReferencias;
    

    
    public void calcularDatos(int marcosPagina, String nombreArchivoReferencias) {
        this.marcosPagina = marcosPagina;
        this.nombreArchivoReferencias = nombreArchivoReferencias;
        
    }

    int bitReferencia ;
    int bitModificación;
    int hint;
    int misses;


    @Override
    public void run(){

    }
    
}
