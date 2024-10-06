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

    // Método que ejecutará cada productor
    @Override
    public void run() {
        while (true) {
            try {
                // Simular una hora trabajada
                Thread.sleep(1000); // Simular una hora (1 segundo real)

                // Incrementar el contador de horas trabajadas
                horasTrabajadas++;

                // Acumular salario por cada hora trabajada
                acumularSalario();
                System.out.println("Salario por hora: " + salarioPorHora + ", Salario total: " + salarioTotal + " Horas trabajadas: " + horasTrabajadas);

                // Verificar si es hora de producir (según el tiempo de producción definido)
                if (horasTrabajadas >= tiempoProduccion / 1000) { // Dividir entre 1000 para convertir milisegundos a horas simuladas
                    producir();
                    horasTrabajadas = 0; // Reiniciar el contador de horas trabajadas
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Método abstracto que implementarán las subclases ProductorCPU y ProductorRAM
    protected abstract void producir();
}
