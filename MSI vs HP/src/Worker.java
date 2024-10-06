public abstract class Worker extends Thread {
    protected Warehouse almacen;
    protected int tiempoProduccion;

    public Worker(Warehouse almacen, int tiempoProduccion) {
        this.almacen = almacen;
        this.tiempoProduccion = tiempoProduccion;
    }

    // Este método es el que cada tipo de trabajador implementará
    public abstract void producir();

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(tiempoProduccion);  // Simular tiempo de producción
                producir();  // Llamar al método producir que será implementado en la subclase
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
