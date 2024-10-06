public class ProductorCPU extends Worker {
    public ProductorCPU(Warehouse almacen, int tiempoProduccion, double salarioPorHora) {
        super(almacen, tiempoProduccion, salarioPorHora);
    }

    @Override
    protected void producir() {
        try {
            almacen.almacenarCPU();
            System.out.println("Productor de CPU ha producido una CPU. Salario por hora: " + salarioPorHora);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
