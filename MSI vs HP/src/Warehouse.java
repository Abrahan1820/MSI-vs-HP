import java.util.concurrent.Semaphore;

public class Warehouse {
    private int cpus;
    private int rams;
    private final Semaphore mutex;

    public Warehouse() {
        this.cpus = 0;
        this.rams = 0;
        this.mutex = new Semaphore(1);  // Para evitar que dos hilos accedan al mismo tiempo
    }

    // Método para almacenar CPUs
    public void almacenarCPU() throws InterruptedException {
        mutex.acquire();  // Bloquear acceso
        cpus++;
        System.out.println("CPUs en almacén: " + cpus);
        mutex.release();  // Liberar acceso
    }
    
    //Metodo para almacenar RAMs
    public void almacenarRAM() throws InterruptedException {
        mutex.acquire(); //Bloquear acceso
        rams++;
        System.out.println("RAMs en almacen: " + rams);
        mutex.release(); //Liberar acceso
    }
}
