public class ProductorPlacaBase extends Worker {
    public ProductorPlacaBase(Warehouse almacen, int tiempoProduccion, double salarioPorHora) {
        super(almacen, tiempoProduccion, salarioPorHora);
    }

    @Override
    protected void producir() {
        try {
            almacen.almacenarPlacaBase();
            System.out.println("Productor de Placa Base ha producido una placa base. Salario total: " + salarioTotal);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
