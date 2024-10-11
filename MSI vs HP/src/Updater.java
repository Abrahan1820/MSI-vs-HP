public class Updater extends Thread {
    protected MainFrame frame;
    protected Warehouse almacenHP;
    protected Warehouse almacenMSI;
    protected int costosHP;
    protected int costosMSI;
    protected int gananciasHP;
    protected int gananciasMSI;
    protected Director estadoDirectorMSI;
    protected ProjectManager estadoPMMSI;
    protected String strestadoPMMSI;
    protected Director estadoDirectorHP;
    protected ProjectManager estadoPMHP;
    protected String strestadoPMHP;
    protected Ensamblador ensamHP;
    protected Ensamblador ensamMSI;
    
    public Updater(MainFrame frame, Warehouse almacenHP, Warehouse almacenMSI, int costosHP, int costosMSI, int gananciasHP, int gananciasMSI, ProjectManager pMHP, ProjectManager pMMSI, Director dirHP, Director dirMSI, Ensamblador ensambladorHP, Ensamblador ensambladorMSI){
        this.frame = frame;
        this.almacenHP = almacenHP;
        this.almacenMSI = almacenMSI;
        this.costosHP = costosHP;        
        this.costosMSI = costosMSI;        
        this.gananciasHP = gananciasHP;        
        this.gananciasMSI = gananciasMSI;        
        this.estadoPMHP = pMHP;
        this.estadoPMMSI = pMMSI;
        this.estadoDirectorHP = dirHP;
        this.estadoDirectorMSI = dirMSI;
        this.ensamHP = ensambladorHP;
        this.ensamMSI = ensambladorMSI;
    }

    protected void actualizarMainFrame() {
        while (true) {
            if (this.estadoPMHP.getVerAnime()) {
                this.strestadoPMHP = "Viendo Anime";
            } else {
                this.strestadoPMHP = "Trabajando";
            }
            if (this.estadoPMMSI.getVerAnime()) {
                this.strestadoPMMSI = "Viendo Anime";
            } else {
                this.strestadoPMMSI = "Trabajando";
            }
            int regPCHP = this.ensamHP.getComputadorasRegEnsambladas();
            int premPCHP = this.ensamHP.getComputadorasPremEnsambladas();
            int regPCMSI = this.ensamMSI.getComputadorasRegEnsambladas();
            int premPCMSI = this.ensamMSI.getComputadorasPremEnsambladas();
            
            frame.actualizarData(almacenHP, almacenMSI, costosHP, costosMSI, gananciasHP, gananciasMSI, estadoDirectorMSI.getEstado(), strestadoPMMSI, estadoDirectorHP.getEstado(), strestadoPMHP, regPCHP, premPCHP, regPCMSI, premPCMSI);
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