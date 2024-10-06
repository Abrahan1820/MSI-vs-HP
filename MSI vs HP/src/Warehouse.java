public class Warehouse {
    private int capacidadMaximaCPU;
    private int capacidadMaximaRAM;
    private int cantidadCPU;
    private int cantidadRAM;

    public Warehouse(int capacidadMaximaCPU, int capacidadMaximaRAM) {
        this.capacidadMaximaCPU = capacidadMaximaCPU;
        this.capacidadMaximaRAM = capacidadMaximaRAM;
        this.cantidadCPU = 0;
        this.cantidadRAM = 0;
    }

    public synchronized void almacenarCPU() throws InterruptedException {
        while (cantidadCPU >= capacidadMaximaCPU) {
            System.out.println("Almacén lleno de CPUs. Esperando espacio...");
            wait(); // Espera hasta que haya espacio disponible
        }
        cantidadCPU++;
        System.out.println("CPU almacenada. Total CPUs: " + cantidadCPU);
        notifyAll(); // Notifica que se ha añadido una CPU
    }

    public synchronized void almacenarRAM() throws InterruptedException {
        while (cantidadRAM >= capacidadMaximaRAM) {
            System.out.println("Almacén lleno de RAMs. Esperando espacio...");
            wait(); // Espera hasta que haya espacio disponible
        }
        cantidadRAM++;
        System.out.println("RAM almacenada. Total RAMs: " + cantidadRAM);
        notifyAll(); // Notifica que se ha añadido una RAM
    }

    public synchronized void retirarProductos(int cantidadCPU, int cantidadRAM) throws InterruptedException {
        while (this.cantidadCPU < cantidadCPU || this.cantidadRAM < cantidadRAM) {
            System.out.println("Esperando suficientes CPUs y RAMs para el ensamblador...");
            wait(); // Espera hasta que haya suficientes productos
        }
        this.cantidadCPU -= cantidadCPU;
        this.cantidadRAM -= cantidadRAM;
        System.out.println("Se han retirado " + cantidadCPU + " CPUs y " + cantidadRAM + " RAMs del almacén.");
        notifyAll(); // Notifica que se han retirado productos
    }
}
