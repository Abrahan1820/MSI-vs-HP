public class Simulador {
    public static void main(String[] args) {
        Warehouse almacen = new Warehouse(20, 55, 15, 35);  // Almacén con capacidad máxima de 20 CPUs, 55 RAMs, 15 placas base, y 35 fuentes de alimentación

        // Crear productores
        ProductorCPU productorCPU = new ProductorCPU(almacen, 72 * 1000, 26.0); // Produce 1 cada 72 horas, salario por hora: 26.0
        ProductorRAM productorRAM = new ProductorRAM(almacen, 12 * 1000, 40.0); // Produce 1 cada 12 horas, salario por hora: 40.0
        ProductorPlacaBase productorPlacaBase = new ProductorPlacaBase(almacen, 72 * 1000, 20.0); // Produce 1 cada 72 horas, salario por hora: 20.0
        ProductorFuenteAlimentacion productorFuenteAlimentacion = new ProductorFuenteAlimentacion(almacen, (24 / 3) * 1000, 16.0); // Produce 1 cada 8 horas (3 por día), salario por hora: 16.0
        ProductorGPU productorGPU = new ProductorGPU(almacen, 72 * 1000, 34.0); // Produce 1 cada 3 díaS, salario por hora: 34.0

        // Crear ensambladores (HP y MSI)
        Ensamblador ensambladorHP = new Ensamblador(almacen, "HP");
       

        // Iniciar los hilos de los productores y ensambladores
        productorCPU.start();
        productorRAM.start();
        productorPlacaBase.start();
        productorFuenteAlimentacion.start();
        productorGPU.start();
        ensambladorHP.start();

        // Simular por un periodo de tiempo
        try {
            Thread.sleep(60000); // Simular por 10 segundos (ajustar según sea necesario)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Mostrar los recursos actuales en el almacén
        almacen.mostrarRecursos();

        // Imprimir el salario total acumulado de los productores y ensambladores
        System.out.println("Salario total Productor CPU: " + productorCPU.getSalarioTotal());
        System.out.println("Salario total Productor RAM: " + productorRAM.getSalarioTotal());
        System.out.println("Salario total Productor Placa Base: " + productorPlacaBase.getSalarioTotal());
        System.out.println("Salario total Productor Fuente Alimentación: " + productorFuenteAlimentacion.getSalarioTotal());
        System.out.println("Salario total Ensamblador HP: " + ensambladorHP.getSalarioTotal());
    }
}
