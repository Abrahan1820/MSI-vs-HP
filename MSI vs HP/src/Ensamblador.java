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
        while (true) { // Reintenta indefinidamente hasta que pueda ensamblar
             System.out.println("Ensamblador revisando que haya recursos suficientes..."); 
            if (compania.equalsIgnoreCase("HP")) {
                if (almacen.haySuficientesProductos(1, 2, 1, 4)) {
                    ensamblarHP();
                    break; // Sal del bucle una vez que se ensambla correctamente
                } else {
                    System.out.println("Esperando recursos para ensamblar HP...");
                }
            } else if (compania.equalsIgnoreCase("MSI")) {
                if (almacen.haySuficientesProductos(3, 4, 2, 6)) {
                    ensamblarMSI();
                    break; // Sal del bucle una vez que se ensambla correctamente
                } else {
                    System.out.println("Esperando recursos para ensamblar MSI...");
                }
            }
            Thread.sleep(1000); // Espera 1 segundos antes de volver a intentar
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
        if (almacen.haySuficientesProductos(1, 2, 1, 4)) {
            almacen.retirarProductos(1, 2, 1, 4); // 1 CPU, 2 RAMs, 1 Placa Base, 4 Fuentes de Alimentación
            computadorasEnsambladas++;
            System.out.println("Computadora estándar HP ensamblada. Ganancia: $90K");
        } else {
            System.out.println("No hay suficientes recursos para ensamblar una computadora estándar HP.");
        }
    }

    private void ensamblarComputadoraConTarjetaGraficaHP() throws InterruptedException {
        // Computadora con tarjeta gráfica de HP después de 2 estándar
        if (almacen.haySuficientesProductos(1, 2, 1, 4)) {
            almacen.retirarProductos(1, 2, 1, 4); // Requiere 1 CPU, 2 RAMs, 1 Placa Base, 4 Fuentes de Alimentación
            System.out.println("Computadora con tarjeta gráfica HP ensamblada. Ganancia: $140K");
            computadorasEnsambladas++;
        } else {
            System.out.println("No hay suficientes recursos para ensamblar una computadora con tarjeta gráfica HP.");
        }
    }

    private void ensamblarComputadoraEstandarMSI() throws InterruptedException {
        // Requerimientos de MSI para una computadora estándar
        if (almacen.haySuficientesProductos(3, 4, 2, 6)) {
            almacen.retirarProductos(3, 4, 2, 6); // 3 CPUs, 4 RAMs, 2 Placas Base, 6 Fuentes de Alimentación
            computadorasEnsambladas++;
            System.out.println("Computadora estándar MSI ensamblada. Ganancia: $180K");
        } else {
            System.out.println("No hay suficientes recursos para ensamblar una computadora estándar MSI.");
        }
    }

    private void ensamblarComputadoraConTarjetaGraficaMSI() throws InterruptedException {
        // Computadora con tarjeta gráfica de MSI después de 6 estándar
        if (almacen.haySuficientesProductos(3, 4, 2, 6)) {
            almacen.retirarProductos(3, 4, 2, 6); // Requiere 3 CPUs, 4 RAMs, 2 Placas Base, 6 Fuentes de Alimentación
            System.out.println("Computadora con tarjeta gráfica MSI ensamblada. Ganancia: $250K");
            computadorasEnsambladas++;
        } else {
            System.out.println("No hay suficientes recursos para ensamblar una computadora con tarjeta gráfica MSI.");
        }
    }
}
