import java.util.concurrent.Semaphore;

public class Warehouse {
    private int capacidadMaximaCPU;
    private int capacidadMaximaRAM;
    private int capacidadMaximaPlacaBase;
    private int capacidadMaximaFuenteAlimentacion;
    private int cantidadCPU;
    private int cantidadRAM;
    private int cantidadPlacaBase;
    private int cantidadFuenteAlimentacion;

    // Semáforos para cada componente
    private Semaphore semaforoCPU;
    private Semaphore semaforoRAM;
    private Semaphore semaforoPlacaBase;
    private Semaphore semaforoFuenteAlimentacion;

    public Warehouse(int capacidadMaximaCPU, int capacidadMaximaRAM, int capacidadMaximaPlacaBase, int capacidadMaximaFuenteAlimentacion) {
        this.capacidadMaximaCPU = capacidadMaximaCPU;
        this.capacidadMaximaRAM = capacidadMaximaRAM;
        this.capacidadMaximaPlacaBase = capacidadMaximaPlacaBase;
        this.capacidadMaximaFuenteAlimentacion = capacidadMaximaFuenteAlimentacion;
        this.cantidadCPU = 0;
        this.cantidadRAM = 0;
        this.cantidadPlacaBase = 0;
        this.cantidadFuenteAlimentacion = 0;

        // Inicializar los semáforos
        this.semaforoCPU = new Semaphore(capacidadMaximaCPU);
        this.semaforoRAM = new Semaphore(capacidadMaximaRAM);
        this.semaforoPlacaBase = new Semaphore(capacidadMaximaPlacaBase);
        this.semaforoFuenteAlimentacion = new Semaphore(capacidadMaximaFuenteAlimentacion);
    }

    public void almacenarCPU() throws InterruptedException {
        semaforoCPU.acquire();
        synchronized (this) {
            cantidadCPU++;
            System.out.println("CPU almacenada. Total CPUs: " + cantidadCPU);
        }
    }

    public void almacenarRAM() throws InterruptedException {
        semaforoRAM.acquire();
        synchronized (this) {
            cantidadRAM++;
            System.out.println("RAM almacenada. Total RAMs: " + cantidadRAM);
        }
    }

    public void almacenarPlacaBase() throws InterruptedException {
        semaforoPlacaBase.acquire();
        synchronized (this) {
            cantidadPlacaBase++;
            System.out.println("Placa base almacenada. Total placas base: " + cantidadPlacaBase);
        }
    }

    public void almacenarFuenteAlimentacion() throws InterruptedException {
        semaforoFuenteAlimentacion.acquire();
        synchronized (this) {
            cantidadFuenteAlimentacion++;
            System.out.println("Fuente de alimentación almacenada. Total fuentes de alimentación: " + cantidadFuenteAlimentacion);
        }
    }

    // Método actualizado para retirar productos sin que quede en negativo
    public synchronized boolean retirarProductos(int cantidadCPU, int cantidadRAM, int cantidadPlacaBase, int cantidadFuenteAlimentacion) throws InterruptedException {
        // Verificar que haya suficiente stock de todos los productos
        if (this.cantidadCPU >= cantidadCPU && this.cantidadRAM >= cantidadRAM &&
            this.cantidadPlacaBase >= cantidadPlacaBase && this.cantidadFuenteAlimentacion >= cantidadFuenteAlimentacion) {

            // Adquirir permisos de los semáforos para retirar los productos
            semaforoCPU.release(cantidadCPU);
            semaforoRAM.release(cantidadRAM);
            semaforoPlacaBase.release(cantidadPlacaBase);
            semaforoFuenteAlimentacion.release(cantidadFuenteAlimentacion);

            // Actualizar las cantidades de los productos
            this.cantidadCPU -= cantidadCPU;
            this.cantidadRAM -= cantidadRAM;
            this.cantidadPlacaBase -= cantidadPlacaBase;
            this.cantidadFuenteAlimentacion -= cantidadFuenteAlimentacion;

            System.out.println("Se han retirado " + cantidadCPU + " CPUs, " + cantidadRAM + " RAMs, " + cantidadPlacaBase + " placas base y " + cantidadFuenteAlimentacion + " fuentes de alimentación del almacén.");
            return true;
        } else {
            // Si no hay suficiente stock, no se realiza la operación
            System.out.println("No hay suficientes recursos en el almacén para ensamblar la computadora.");
            return false;
        }
    }
    
    public boolean haySuficientesProductos(int cpus, int rams, int placasBase, int fuentes) {
    return this.cantidadCPU >= cpus && this.cantidadRAM >= rams && this.cantidadPlacaBase >= placasBase && this.cantidadFuenteAlimentacion >= fuentes;
}


    public void almacenarComputadoraTerminada(String tipoComputadora) {
        // No hay límite para almacenar computadoras
        System.out.println("Se ha almacenado una computadora terminada: " + tipoComputadora);
    }

    // Método para mostrar el estado actual del almacén
    public synchronized void mostrarRecursos() {
        System.out.println("===== Estado actual del almacén =====");
        System.out.println("CPUs: " + cantidadCPU + " / " + capacidadMaximaCPU);
        System.out.println("RAMs: " + cantidadRAM + " / " + capacidadMaximaRAM);
        System.out.println("Placas Base: " + cantidadPlacaBase + " / " + capacidadMaximaPlacaBase);
        System.out.println("Fuentes de Alimentación: " + cantidadFuenteAlimentacion + " / " + capacidadMaximaFuenteAlimentacion);
        System.out.println("====================================");
    }
}
