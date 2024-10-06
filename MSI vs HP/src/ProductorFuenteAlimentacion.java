public class ProductorFuenteAlimentacion extends Worker {
    public ProductorFuenteAlimentacion(Warehouse almacen, int tiempoProduccion, double salarioPorHora) {
        super(almacen, tiempoProduccion, salarioPorHora);
    }

    @Override
    protected void producir() {
        try {
            almacen.almacenarFuenteAlimentacion();
            System.out.println("Productor de Fuente de Alimentación ha producido una fuente de alimentación. Salario total: " + salarioTotal);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
