public class Ensamblador extends Worker {
    private String compania; // HP o MSI
    private int computadorasEnsambladas; // Lleva la cuenta de cuántas computadoras estándar ha ensamblado
    private static final int TIEMPO_ENSAMBLAR = 48 * 1000; // 48 horas para ensamblar una computadora
    private static final double SALARIO_POR_HORA = 50.0;

    public Ensamblador(Warehouse almacen, String compania) {
        super(almacen, TIEMPO_ENSAMBLAR, SALARIO_POR_HORA);
        this.compania = compania;
        this.computadorasEnsambladas = 0;
    }

    @Override
    protected void producir() {
        try {
            if (compania.equalsIgnoreCase("HP")) {
                ensamblarHP();
            } else if (compania.equalsIgnoreCase("MSI")) {
                ensamblarMSI();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void ensamblarHP() throws InterruptedException {
        // HP: Cada 3 computadoras, la tercera es con tarjeta gráfica
        if (computadorasEnsambladas % 3 == 2) {
            ensamblarComputadoraConTarjetaGraficaHP();
        } else {
            ensamblarComputadoraEstandarHP();
        }
    }

    private void ensamblarMSI() throws InterruptedException {
        // MSI: Cada 7 computadoras, la séptima es con tarjeta gráfica
        if (computadorasEnsambladas % 7 == 6) {
            ensamblarComputadoraConTarjetaGraficaMSI();
        } else {
            ensamblarComputadoraEstandarMSI();
        }
    }

    private void ensamblarComputadoraEstandarHP() throws InterruptedException {
        // Requerimientos de HP para una computadora estándar
        almacen.retirarProductos(1, 2, 1, 4); // 1 CPU, 2 RAMs, 1 Placa Base, 4 Fuentes de Alimentación
        computadorasEnsambladas++;
        System.out.println("Computadora estándar HP ensamblada. Ganancia: $90K");
    }

    private void ensamblarComputadoraConTarjetaGraficaHP() throws InterruptedException {
        // Computadora con tarjeta gráfica de HP después de 2 estándar
        almacen.retirarProductos(1, 2, 1, 4); // Requiere 1 CPU, 2 RAMs, 1 Placa Base, 4 Fuentes de Alimentación
        System.out.println("Computadora con tarjeta gráfica HP ensamblada. Ganancia: $140K");
        computadorasEnsambladas++;
    }

    private void ensamblarComputadoraEstandarMSI() throws InterruptedException {
        // Requerimientos de MSI para una computadora estándar
        almacen.retirarProductos(3, 4, 2, 6); // 3 CPUs, 4 RAMs, 2 Placas Base, 6 Fuentes de Alimentación
        computadorasEnsambladas++;
        System.out.println("Computadora estándar MSI ensamblada. Ganancia: $180K");
    }

    private void ensamblarComputadoraConTarjetaGraficaMSI() throws InterruptedException {
        // Computadora con tarjeta gráfica de MSI después de 6 estándar
        almacen.retirarProductos(3, 4, 2, 6); // Requiere 3 CPUs, 4 RAMs, 2 Placas Base, 6 Fuentes de Alimentación
        System.out.println("Computadora con tarjeta gráfica MSI ensamblada. Ganancia: $250K");
        computadorasEnsambladas++;
    }
}
