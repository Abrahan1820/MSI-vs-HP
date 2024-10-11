public class Updater extends Thread {
    protected MainFrame frame;
    protected Warehouse almacenHP;
    protected Warehouse almacenMSI;
    protected int costosHP;
    protected int costosMSI;
    protected int gananciasHP;
    protected int gananciasMSI;
    
    public Updater(MainFrame frame, Warehouse almacenHP, Warehouse almacenMSI, int costosHP, int costosMSI, int gananciasHP, int gananciasMSI){
        this.frame = frame;
        this.almacenHP = almacenHP;
        this.almacenMSI = almacenMSI;
        this.costosHP = costosHP;        
        this.costosMSI = costosMSI;        
        this.gananciasHP = gananciasHP;        
        this.gananciasMSI = gananciasMSI;        
    }

    protected void actualizarMainFrame() {
        while (true) {
        frame.actualizarData(almacenHP, almacenMSI, costosHP, costosMSI, gananciasHP, gananciasMSI);
        }
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(TimeConfig.convertirHorasASegundos(1)); // Simular una hora (1 segundo real)
                actualizarMainFrame();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}