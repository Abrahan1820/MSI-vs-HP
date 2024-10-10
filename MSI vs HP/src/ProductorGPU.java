public class ProductorGPU extends Worker {
    public ProductorGPU(Warehouse almacen, int tiempoProduccion, double salarioPorHora) {
        super(almacen, tiempoProduccion, salarioPorHora);
    }

    @Override
    protected void producir() {
        try {
            almacen.almacenarGPU();
            System.out.println("Productor de GPU ha producido una GPU. Salario total: " + salarioTotal);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
