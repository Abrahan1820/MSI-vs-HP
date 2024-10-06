public abstract class Worker extends Thread {
    protected Warehouse almacen;
    protected int tiempoProduccion; // Milisegundos
    protected double salarioPorHora;
    protected double salarioTotal;  // Salario acumulado

    public Worker(Warehouse almacen, int tiempoProduccion, double salarioPorHora) {
        this.almacen = almacen;
        this.tiempoProduccion = tiempoProduccion;
        this.salarioPorHora = salarioPorHora;
        this.salarioTotal = 0.0;  // Inicializar el salario acumulado
    }

    // Método para acumular salario cada vez que pasa una hora
    public void acumularSalario() {
        this.salarioTotal += salarioPorHora;
    }

    // Método para obtener el salario total
    public double getSalarioTotal() {
        return salarioTotal;
    }

    // Método que ejecutará cada productor
    @Override
    public void run() {
        while (true) {
            try {
                // Simular tiempo de producción
                Thread.sleep(tiempoProduccion);

                // Intentar producir (subclases implementarán este método)
                producir();

                // Acumular salario por cada hora
                acumularSalario();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Método abstracto que implementarán las subclases ProductorCPU y ProductorRAM
    protected abstract void producir();
}
