public class ProductorCPU extends Worker {

    public ProductorCPU(Warehouse almacen, int tiempoProduccion) {
        super(almacen, tiempoProduccion);
    }

    @Override
    public void producir() {
        try {
            almacen.almacenarCPU();  // Añade una CPU al almacén
            System.out.println("CPU producida.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
