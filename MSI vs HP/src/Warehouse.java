public class Warehouse {
    private int capacidadMaximaCPU;
    private int capacidadMaximaRAM;
    private int capacidadMaximaPlacaBase;
    private int capacidadMaximaFuenteAlimentacion;
    private int cantidadCPU;
    private int cantidadRAM;
    private int cantidadPlacaBase;
    private int cantidadFuenteAlimentacion;

    public Warehouse(int capacidadMaximaCPU, int capacidadMaximaRAM, int capacidadMaximaPlacaBase, int capacidadMaximaFuenteAlimentacion) {
        this.capacidadMaximaCPU = capacidadMaximaCPU;
        this.capacidadMaximaRAM = capacidadMaximaRAM;
        this.capacidadMaximaPlacaBase = capacidadMaximaPlacaBase;
        this.capacidadMaximaFuenteAlimentacion = capacidadMaximaFuenteAlimentacion;
        this.cantidadCPU = 0;
        this.cantidadRAM = 0;
        this.cantidadPlacaBase = 0;
        this.cantidadFuenteAlimentacion = 0;
    }

    public synchronized void almacenarCPU() throws InterruptedException {
        while (cantidadCPU >= capacidadMaximaCPU) {
            System.out.println("Almacén lleno de CPUs. Esperando espacio...");
            wait();
        }
        cantidadCPU++;
        System.out.println("CPU almacenada. Total CPUs: " + cantidadCPU);
        notifyAll();
    }

    public synchronized void almacenarRAM() throws InterruptedException {
        while (cantidadRAM >= capacidadMaximaRAM) {
            System.out.println("Almacén lleno de RAMs. Esperando espacio...");
            wait();
        }
        cantidadRAM++;
        System.out.println("RAM almacenada. Total RAMs: " + cantidadRAM);
        notifyAll();
    }

    public synchronized void almacenarPlacaBase() throws InterruptedException {
        while (cantidadPlacaBase >= capacidadMaximaPlacaBase) {
            System.out.println("Almacén lleno de placas base. Esperando espacio...");
            wait();
        }
        cantidadPlacaBase++;
        System.out.println("Placa base almacenada. Total placas base: " + cantidadPlacaBase);
        notifyAll();
    }

    public synchronized void almacenarFuenteAlimentacion() throws InterruptedException {
        while (cantidadFuenteAlimentacion >= capacidadMaximaFuenteAlimentacion) {
            System.out.println("Almacén lleno de fuentes de alimentación. Esperando espacio...");
            wait();
        }
        cantidadFuenteAlimentacion++;
        System.out.println("Fuente de alimentación almacenada. Total fuentes de alimentación: " + cantidadFuenteAlimentacion);
        notifyAll();
    }

    public synchronized void retirarProductos(int cantidadCPU, int cantidadRAM, int cantidadPlacaBase, int cantidadFuenteAlimentacion) throws InterruptedException {
        while (this.cantidadCPU < cantidadCPU || this.cantidadRAM < cantidadRAM || this.cantidadPlacaBase < cantidadPlacaBase || this.cantidadFuenteAlimentacion < cantidadFuenteAlimentacion) {
            System.out.println("Esperando suficientes CPUs, RAMs, placas base y fuentes de alimentación para el ensamblador...");
            wait();
        }
        this.cantidadCPU -= cantidadCPU;
        this.cantidadRAM -= cantidadRAM;
        this.cantidadPlacaBase -= cantidadPlacaBase;
        this.cantidadFuenteAlimentacion -= cantidadFuenteAlimentacion;
        System.out.println("Se han retirado " + cantidadCPU + " CPUs, " + cantidadRAM + " RAMs, " + cantidadPlacaBase + " placas base y " + cantidadFuenteAlimentacion + " fuentes de alimentación del almacén.");
        notifyAll();
    }
}
