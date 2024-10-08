public abstract class Worker extends Thread {
    protected Warehouse almacen;
    protected int tiempoProduccion; // Milisegundos para producir
    protected double salarioPorHora;
    protected double salarioTotal;  // Salario acumulado
    protected int horasTrabajadas;   // Contador de horas trabajadas

    public Worker(Warehouse almacen, int tiempoProduccion, double salarioPorHora) {
        this.almacen = almacen;
        this.tiempoProduccion = tiempoProduccion;
        this.salarioPorHora = salarioPorHora;
        this.salarioTotal = 0.0;  // Inicializar el salario acumulado
        this.horasTrabajadas = 0; // Inicializar el contador de horas trabajadas
    }

    // Método para acumular salario cada vez que pasa una hora
    public void acumularSalario() {
        this.salarioTotal += salarioPorHora;
    }

    // Método para obtener el salario total
    public double getSalarioTotal() {
        return salarioTotal;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000); // Simular una hora (1 segundo real)
                horasTrabajadas++;
                acumularSalario();

                if (horasTrabajadas >= tiempoProduccion / 1000) { // Verificar si es hora de producir
                    producir();
                    horasTrabajadas = 0;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract void producir();
}
