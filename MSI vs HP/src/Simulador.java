public class Simulador {
    public static void main(String[] args) {
        Warehouse almacen = new Warehouse();

        // Crear un productor de CPUs
        Worker productorCPU = new ProductorCPU(almacen, 500);  // Producción cada 2 segundos
        Worker productorRAM = new ProductorRAM(almacen, 2000); //Produccion cada 2 segundos

        // Iniciar la producción
        productorCPU.start();  // Iniciar el hilo del productor
        productorRAM.start(); //Inicial el hilo del productor
    }
}
