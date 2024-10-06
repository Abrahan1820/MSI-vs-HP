public class ProductorRAM extends Worker {
    public ProductorRAM(Warehouse almacen, int tiempoProduccion, double salarioPorHora) {
        super(almacen, tiempoProduccion, salarioPorHora);
    }

    @Override
    protected void producir() {
        try {
            almacen.almacenarRAM();
            System.out.println("Productor de RAM ha producido una RAM. Salario por hora: " + salarioPorHora);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
