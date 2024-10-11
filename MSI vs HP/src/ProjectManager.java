public class ProjectManager extends Worker {
    private int diasRestantes; // Contador de días restantes
    private static final int HORAS_DE_TRABAJO = 24; // Total de horas de trabajo por día
    private static final double SALARIO_POR_HORA = 40.0; // Salario por hora del PM
    private double salarioTotal; // Salario total acumulado del PM
    private boolean viendoAnime; // Indicador para saber si el PM está viendo anime

    public ProjectManager(int diasRestantes) {
        super(null, 0, SALARIO_POR_HORA); // No se necesita un almacén para el PM
        this.diasRestantes = diasRestantes;
        this.salarioTotal = 0.0; // Inicialmente el salario total es 0
        this.viendoAnime = false; // Inicialmente el PM no está viendo anime
    }

    @Override
    protected void producir() {
        for (int i = 0; i < HORAS_DE_TRABAJO; i++) {
            if (i < 16) {
                // Primeras 16 horas: alternando entre ver anime y trabajar
                for (int j = 0; j < 2; j++) { // 2 intervalos de 30 minutos
                    verAnime();
                    trabajar();
                    // Incrementa el salario por cada hora trabajada
                    salarioTotal += SALARIO_POR_HORA / 2; // Cada media hora se gana la mitad de la tarifa por hora
                }
            } else {
                // Últimas 8 horas: actualizar el contador de días
                cambiarContador();
                salarioTotal += SALARIO_POR_HORA; // Incrementa el salario por hora completa trabajada
            }
        }
    }
    
    public boolean getVerAnime(){
        return this.viendoAnime;
    }

    // Método para simular que el PM está viendo anime
    private void verAnime() {
        viendoAnime = true; // El PM está viendo anime
        System.out.println("Project Manager viendo anime...");
        try {
            Thread.sleep(TimeConfig.convertirMinutosASegundos(30)); // Espera 30 minutos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        viendoAnime = false; // Deja de ver anime al finalizar el intervalo
    }

    // Método para simular que el PM está trabajando
    private void trabajar() {
        System.out.println("Project Manager revisando el avance del proyecto...");
        try {
            Thread.sleep(TimeConfig.convertirMinutosASegundos(30)); // Espera 30 minutos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar el contador de días restantes
    private void cambiarContador() {
        System.out.println("Project Manager actualizando el contador de días restantes...");
        if (diasRestantes > 0) {
            diasRestantes--;
        }
        System.out.println("Días restantes para la entrega: " + diasRestantes);
        try {
            Thread.sleep(TimeConfig.convertirHorasASegundos(1)); // Espera 1 hora
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Método para que el Director sepa si el PM está viendo anime
    public boolean estaViendoAnime() {
        return viendoAnime;
    }

    // Método para descontar salario del PM, evitando que el salario total sea negativo
    public void descontarSalario(int cantidad) {
        if (salarioTotal >= cantidad) {
            salarioTotal -= cantidad;
            System.out.println("El salario del PM ha sido reducido a: $" + salarioTotal);
        } else {
            System.out.println("El salario no puede ser reducido más allá de 0.");
            salarioTotal = 0; // Evita que el salario se vuelva negativo
        }
    }

    // Método para obtener los días restantes
    public int getDiasRestantes() {
        return diasRestantes;
    }

    // Método para obtener el salario total acumulado
    public double getSalarioTotal() {
        return salarioTotal;
    }
}
