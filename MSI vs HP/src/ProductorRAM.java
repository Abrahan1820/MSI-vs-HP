public class ProductorRAM extends Worker {

    public ProductorRAM(Warehouse almacen, int tiempoProduccion) {
        super(almacen, tiempoProduccion);
    }

    @Override
    public void producir() {
        try {
            almacen.almacenarRAM();  // Añade una RAM al almacén
            System.out.println("RAM producida.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
