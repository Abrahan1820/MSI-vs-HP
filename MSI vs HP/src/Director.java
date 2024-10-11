public class Director extends Worker {
    private int diasLaborados;
    private ProjectManager pm; // El PM que el Director supervisará
    private static final int HORAS_DE_TRABAJO = 24; // Horas de trabajo diarias del Director
    private static final double SALARIO_POR_HORA = 60.0; // Salario por hora del Director
    private static final int DESCUENTO_POR_FALTA = 100; // Descuento por ver anime
    private String estado;
    
    public Director(ProjectManager pm, Warehouse almacen) {
        super(almacen, 0, SALARIO_POR_HORA);
        this.pm = pm;
        this.diasLaborados = 0;
        this.estado = "";
    }

    @Override
    protected void producir() {
        try {
            while (true) {
                System.out.println("Director trabajando. Día: " + diasLaborados);
                for (int i = 0; i < HORAS_DE_TRABAJO; i++) {
                    if (i < 12) {
                        monitorearAlmacen(); // Monitorea el almacén durante las primeras 12 horas
                    } else {
                        gestionarOperaciones(); // Gestiona operaciones durante las últimas 12 horas
                    }
                    
                    // El Director elige una hora aleatoria para supervisar al PM
                    int horaParaSupervisar = (int) (Math.random() * HORAS_DE_TRABAJO);
                    if (i == horaParaSupervisar) {
                        supervisarPM(); // Supervisar al PM durante esa hora
                    }
                }
                diasLaborados++;
                Thread.sleep(TimeConfig.convertirHorasASegundos(1)); // Simular paso de un día laboral
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public String getEstado(){
        return this.estado;
    }

    // Método para monitorear el almacén
    private void monitorearAlmacen() {
        System.out.println("Director monitoreando el almacén...");  
        this.estado = "Monitoreando Almacen";
        almacen.mostrarRecursos();
        try {
            Thread.sleep(TimeConfig.convertirHorasASegundos(5)); // Simular monitoreo durante 5 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Método para gestionar las operaciones del día
    private void gestionarOperaciones() {
        System.out.println("Director gestionando operaciones...");
        this.estado = "Gestionando Operaciones";
        try {
            Thread.sleep(TimeConfig.convertirHorasASegundos(5)); // Simular gestión durante 5 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Método para supervisar al PM y aplicar sanciones si está viendo anime
    private void supervisarPM() {
        System.out.println("Director supervisando al Project Manager...");
        this.estado = "Supervisando al PM";
        if (pm.estaViendoAnime()) {
            System.out.println("El Director ha encontrado al PM viendo anime. Aplicando sanción...");
            aplicarSancion();
        } else {
            System.out.println("El PM está trabajando correctamente.");
        }
    }

    // Método para aplicar la sanción al PM
    private void aplicarSancion() {
        System.out.println("Aplicando descuento de $" + DESCUENTO_POR_FALTA + " al salario del PM.");
        pm.descontarSalario(DESCUENTO_POR_FALTA); // Descontar del salario del PM
    }

    // Método para reportar los días trabajados
    public int getDiasLaborados() {
        return diasLaborados;
    }
}
