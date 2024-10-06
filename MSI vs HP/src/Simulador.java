public class Simulador {

    public static void main(String[] args) {
        Warehouse almacen = new Warehouse(20, 55);  // Almacén con capacidad máxima de 20 CPUs y 55 RAMs

        // Crear dos productores
        ProductorCPU productorCPU = new ProductorCPU(almacen, 1000, 26.0); // 20 dólares por hora
        ProductorRAM productorRAM = new ProductorRAM(almacen, 1000, 40.0); // 22 dólares por hora

        // Iniciar los hilos de los productores
        productorCPU.start();
        productorRAM.start();

        // Simular por un periodo de tiempo
        try {
            Thread.sleep(10000); // Simular por 10 segundos (ajustar según sea necesario)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Imprimir el salario total acumulado de los productores
        System.out.println("Salario total Productor CPU1: " + productorCPU.getSalarioTotal());
        System.out.println("Salario total Productor RAM1: " + productorRAM.getSalarioTotal());
    }
}
