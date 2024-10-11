/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author guill
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        setLocationRelativeTo(null);
        
        // Almacenes con limites de 20 CPUs, 55 RAMs, 25 PLACAS BASE y 35 FUENTEs DE ALIMENTACION
        Warehouse almacenHP = new Warehouse(20,55,25,35);
        Warehouse almacenMSI = new Warehouse(20,55,25,35);
        
         // Crear productores
        crearProductoresYEnsamblador(almacenHP, 1, 1, 1, 1, 1, 1); //HP representa el almacen, debe ser HP o MSI 
        crearProductoresYEnsamblador(almacenMSI, 1, 1, 1, 1, 1, 1); //HP representa el almacen, debe ser HP o MSI 
        //y cada uno de los numeros representa la cantidad de trabajadores en cada area.
        
        // Iniciar los hilos de los PM y Directores. Por ahora, hay 1 por cada area de cada almacen
        ProjectManager PMHP = new ProjectManager(10);
        Director directorHP = new Director(PMHP, almacenHP);
        ProjectManager PMMSI = new ProjectManager(10);
        Director directorMSI = new Director(PMMSI, almacenMSI);
        
        PMHP.start();
        directorHP.start();
        PMMSI.start();
        directorMSI.start();
        
        //Updater thread. Completar con los acumulados
        int costosHP = 0;
        int costosMSI = 0;
        int gananciasHP = 0;
        int gananciasMSI = 0;
                
        Updater actualizador = new Updater(this, almacenHP, almacenMSI, costosHP, costosMSI, gananciasHP, gananciasMSI, PMHP, PMMSI, directorHP, directorMSI);
        actualizador.start();
        
        // Simular por un periodo de tiempo
        try {
            Thread.sleep(TimeConfig.convertirHorasASegundos(10)); // Simular por X segundos (ajustar según sea necesario)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Mostrar los recursos actuales en el almacén
        System.out.println("------- RECURSOS HP -------");
        almacenHP.mostrarRecursos();
        System.out.println("------- RECURSOS MSI -------");
        almacenMSI.mostrarRecursos();

    }
    
    //Metodo para crear productores y ensambladores
    public void crearProductoresYEnsamblador(Warehouse almacen, int cantidadProductoresCPU, int cantidadProductoresRAM, 
                                          int cantidadProductoresPlacaBase, int cantidadProductoresFuenteAlimentacion, 
                                          int cantidadProductoresGPU, int cantidadEnsamblador) {
    
    // Variables
    final int maxProductores = 15; // Máximo de productores y ensambladores
    int tiempoProduccionCPU = 72 * (TimeConfig.convertirHorasASegundos(1)); // Tiempo de producción para CPU
    int tiempoProduccionRAM = 12 * (TimeConfig.convertirHorasASegundos(1)); // Tiempo de producción para RAM
    int tiempoProduccionPlacaBase = 72 * (TimeConfig.convertirHorasASegundos(1)); // Tiempo de producción para Placa Base
    int tiempoProduccionFuenteAlimentacion = 8 * (TimeConfig.convertirHorasASegundos(1)); // Tiempo de producción para Fuente de Alimentación
    int tiempoProduccionGPU = 72 * (TimeConfig.convertirHorasASegundos(1)); // Tiempo de producción para GPU
    double salarioPorHoraCPU = 26.0; // Salario por hora para CPU
    double salarioPorHoraRAM = 40.0; // Salario por hora para RAM
    double salarioPorHoraPlacaBase = 20.0; // Salario por hora para Placa Base
    double salarioPorHoraFuenteAlimentacion = 16.0; // Salario por hora para Fuente de Alimentación
    double salarioPorHoraGPU = 34.0; // Salario por hora para GPU

    // Calcular el total de productores
    int totalProductores = cantidadProductoresCPU + cantidadProductoresRAM + 
                           cantidadProductoresPlacaBase + cantidadProductoresFuenteAlimentacion +
                           cantidadProductoresGPU + cantidadEnsamblador;

    // Verificar condiciones
    if (cantidadProductoresCPU < 1 || cantidadProductoresRAM < 1 ||
        cantidadProductoresPlacaBase < 1 || cantidadProductoresFuenteAlimentacion < 1 ||
        cantidadProductoresGPU < 1 || cantidadEnsamblador < 1) {
        System.out.println("Error: Cada cantidad de productores y ensambladores debe ser al menos 1.");
        System.exit(1);
    }

    if (totalProductores > maxProductores) {
        System.out.println("Error: La suma de todos los productores y ensambladores no puede ser mayor a " + maxProductores + ".");
        System.exit(1);
    }

    // Crear productores
    for (int i = 0; i < cantidadProductoresCPU; i++) {
        ProductorCPU productorCPU = new ProductorCPU(almacen, tiempoProduccionCPU, salarioPorHoraCPU);
        productorCPU.start();
    }

    for (int i = 0; i < cantidadProductoresRAM; i++) {
        ProductorRAM productorRAM = new ProductorRAM(almacen, tiempoProduccionRAM, salarioPorHoraRAM);
        productorRAM.start();
    }

    for (int i = 0; i < cantidadProductoresPlacaBase; i++) {
        ProductorPlacaBase productorPlacaBase = new ProductorPlacaBase(almacen, tiempoProduccionPlacaBase, salarioPorHoraPlacaBase);
        productorPlacaBase.start();
    }

    for (int i = 0; i < cantidadProductoresFuenteAlimentacion; i++) {
        ProductorFuenteAlimentacion productorFuenteAlimentacion = new ProductorFuenteAlimentacion(almacen, tiempoProduccionFuenteAlimentacion, salarioPorHoraFuenteAlimentacion);
        productorFuenteAlimentacion.start();
    }

    for (int i = 0; i < cantidadProductoresGPU; i++) {
        ProductorGPU productorGPU = new ProductorGPU(almacen, tiempoProduccionGPU, salarioPorHoraGPU);
        productorGPU.start();
    }

    // Crear ensambladores
    for (int i = 0; i < cantidadEnsamblador; i++) {
        Ensamblador ensamblador = new Ensamblador(almacen, "HP");
        ensamblador.start();
    }
}
    
    // Metodo con el cual se actualiza toda la data del jFrame
    public void actualizarData(Warehouse almacenHP, Warehouse almacenMSI, int totalCostoAcumuladoHP, int totalCostoAcumuladoMSI, int gananciasHP, int gananciasMSI, String estadoDirectorMSI, String estadoPMMSI, String estadoDirectorHP, String estadoPMHP){
        //HP
        
        tieneRAMHP.setText(almacenHP.getRAM());
        tieneCPUHP.setText(almacenHP.getCPU());
        tienePlacaHP.setText(almacenHP.getPBase());
        tienePSupplyHP.setText(almacenHP.getPSupply());
        tieneGPUHP.setText(almacenHP.getGPU());
        maxGPUHP.setText(almacenHP.getMAXGPU());
        maxCPUHP.setText("/" + almacenHP.getMAXCPU());
        maxPSupplyHP.setText("/" + almacenHP.getMAXPSupply());
        maxRAMHP.setText("/" + almacenHP.getMAXRAM());
        maxPlacaHP.setText("/" + almacenHP.getMAXPBase());
        this.estadoDirectorHP.setText(estadoDirectorHP);
        this.estadoPMHP.setText(estadoPMHP);
        //MSI
        tieneRAMMSI.setText(almacenMSI.getRAM());
        tieneCPUMSI.setText(almacenMSI.getCPU());
        tienePlacaMSI.setText(almacenMSI.getPBase());
        tienePSupplyMSI.setText(almacenMSI.getPSupply());
        tieneGPUMSI.setText(almacenMSI.getGPU());
        maxGPUMSI.setText("/" + almacenMSI.getMAXGPU());
        maxCPUMSI.setText("/" + almacenMSI.getMAXCPU());
        maxPSupplyMSI.setText("/" + almacenMSI.getMAXPSupply());
        maxRAMMSI.setText("/" + almacenMSI.getMAXRAM());
        maxPlacaMSI.setText("/" + almacenMSI.getMAXPBase());
        this.estadoDirectorMSI.setText(estadoDirectorMSI);
        this.estadoPMMSI.setText(estadoPMMSI);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        companyPanelMSI = new javax.swing.JPanel();
        logoMSI = new javax.swing.JLabel();
        panelEnsambladorMSI = new javax.swing.JPanel();
        ENSAMBLADORMSI = new javax.swing.JLabel();
        jProgressBar3 = new javax.swing.JProgressBar();
        panelGPUMSI = new javax.swing.JPanel();
        GPUMSI = new javax.swing.JLabel();
        panelContadorGPUMSI = new javax.swing.JPanel();
        maxGPUMSI = new javax.swing.JLabel();
        tieneGPUMSI = new javax.swing.JLabel();
        panelRAMMSI = new javax.swing.JPanel();
        RAMMSI = new javax.swing.JLabel();
        panelContadorRAMMSI = new javax.swing.JPanel();
        tieneRAMMSI = new javax.swing.JLabel();
        maxRAMMSI = new javax.swing.JLabel();
        panelPSupplyMSI = new javax.swing.JPanel();
        AlimentacionMSI = new javax.swing.JLabel();
        panelContadoPSupplyHP1 = new javax.swing.JPanel();
        tienePSupplyMSI = new javax.swing.JLabel();
        maxPSupplyMSI = new javax.swing.JLabel();
        panelPBaseMSI = new javax.swing.JPanel();
        PlacaMSI = new javax.swing.JLabel();
        panelContadorPlacaMSI = new javax.swing.JPanel();
        tienePlacaMSI = new javax.swing.JLabel();
        maxPlacaMSI = new javax.swing.JLabel();
        panelCPUMSI = new javax.swing.JPanel();
        CPUMSI = new javax.swing.JLabel();
        panelContadorCPUMSI = new javax.swing.JPanel();
        maxCPUMSI = new javax.swing.JLabel();
        tieneCPUMSI = new javax.swing.JLabel();
        labelEstadoDirectorMSI = new javax.swing.JLabel();
        estadoDirectorMSI = new javax.swing.JLabel();
        labelEstadoPMMSI = new javax.swing.JLabel();
        estadoPMMSI = new javax.swing.JLabel();
        companyPanelHP = new javax.swing.JPanel();
        logoHP = new javax.swing.JLabel();
        panelEnsambladorHP = new javax.swing.JPanel();
        ENSAMBLADORHP = new javax.swing.JLabel();
        jProgressBar4 = new javax.swing.JProgressBar();
        panelGPUHP = new javax.swing.JPanel();
        GPUHP = new javax.swing.JLabel();
        panelContadorGPUHP = new javax.swing.JPanel();
        maxGPUHP = new javax.swing.JLabel();
        tieneGPUHP = new javax.swing.JLabel();
        panelRAMHP = new javax.swing.JPanel();
        RAMHP = new javax.swing.JLabel();
        panelContadorRAMHP = new javax.swing.JPanel();
        tieneRAMHP = new javax.swing.JLabel();
        maxRAMHP = new javax.swing.JLabel();
        panelPSupplyHP = new javax.swing.JPanel();
        AlimentacionHP = new javax.swing.JLabel();
        panelContadoPSupplyHP = new javax.swing.JPanel();
        tienePSupplyHP = new javax.swing.JLabel();
        maxPSupplyHP = new javax.swing.JLabel();
        panelPBaseHP = new javax.swing.JPanel();
        PlacaHP = new javax.swing.JLabel();
        panelContadorPlacaHP = new javax.swing.JPanel();
        tienePlacaHP = new javax.swing.JLabel();
        maxPlacaHP = new javax.swing.JLabel();
        panelCPUHP = new javax.swing.JPanel();
        CPUHP = new javax.swing.JLabel();
        panelContadorCPUHP = new javax.swing.JPanel();
        maxCPUHP = new javax.swing.JLabel();
        tieneCPUHP = new javax.swing.JLabel();
        labelDia = new javax.swing.JLabel();
        labelEstadoDirectorHP = new javax.swing.JLabel();
        labelEstadoPMHP = new javax.swing.JLabel();
        estadoDirectorHP = new javax.swing.JLabel();
        estadoPMHP = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("HP VS MSI");
        setLocation(new java.awt.Point(0, 0));
        setPreferredSize(new java.awt.Dimension(928, 600));
        setResizable(false);

        companyPanelMSI.setBackground(new java.awt.Color(102, 102, 102));
        companyPanelMSI.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        companyPanelMSI.setForeground(new java.awt.Color(102, 102, 102));
        companyPanelMSI.setPreferredSize(new java.awt.Dimension(451, 500));

        logoMSI.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        logoMSI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MSI LOGO RESIZED.png"))); // NOI18N

        panelEnsambladorMSI.setBackground(new java.awt.Color(102, 102, 102));
        panelEnsambladorMSI.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        panelEnsambladorMSI.setForeground(new java.awt.Color(102, 102, 102));
        panelEnsambladorMSI.setPreferredSize(new java.awt.Dimension(221, 94));

        ENSAMBLADORMSI.setFont(new java.awt.Font("Myanmar Text", 1, 14)); // NOI18N
        ENSAMBLADORMSI.setText("Ensamblador");

        javax.swing.GroupLayout panelEnsambladorMSILayout = new javax.swing.GroupLayout(panelEnsambladorMSI);
        panelEnsambladorMSI.setLayout(panelEnsambladorMSILayout);
        panelEnsambladorMSILayout.setHorizontalGroup(
            panelEnsambladorMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEnsambladorMSILayout.createSequentialGroup()
                .addGroup(panelEnsambladorMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEnsambladorMSILayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(ENSAMBLADORMSI, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelEnsambladorMSILayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jProgressBar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        panelEnsambladorMSILayout.setVerticalGroup(
            panelEnsambladorMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEnsambladorMSILayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ENSAMBLADORMSI)
                .addGap(18, 18, 18)
                .addComponent(jProgressBar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        panelGPUMSI.setBackground(new java.awt.Color(102, 102, 102));
        panelGPUMSI.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        panelGPUMSI.setForeground(new java.awt.Color(102, 102, 102));
        panelGPUMSI.setPreferredSize(new java.awt.Dimension(221, 94));

        GPUMSI.setFont(new java.awt.Font("Myanmar Text", 1, 14)); // NOI18N
        GPUMSI.setText("GPU");

        panelContadorGPUMSI.setPreferredSize(new java.awt.Dimension(55, 30));

        maxGPUMSI.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        maxGPUMSI.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        maxGPUMSI.setText("/ 1");
        maxGPUMSI.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        maxGPUMSI.setPreferredSize(new java.awt.Dimension(30, 30));

        tieneGPUMSI.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        tieneGPUMSI.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        tieneGPUMSI.setText("CPUHP");
        tieneGPUMSI.setMaximumSize(new java.awt.Dimension(25, 30));
        tieneGPUMSI.setMinimumSize(new java.awt.Dimension(25, 30));
        tieneGPUMSI.setPreferredSize(new java.awt.Dimension(25, 30));

        javax.swing.GroupLayout panelContadorGPUMSILayout = new javax.swing.GroupLayout(panelContadorGPUMSI);
        panelContadorGPUMSI.setLayout(panelContadorGPUMSILayout);
        panelContadorGPUMSILayout.setHorizontalGroup(
            panelContadorGPUMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContadorGPUMSILayout.createSequentialGroup()
                .addComponent(tieneGPUMSI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maxGPUMSI, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelContadorGPUMSILayout.setVerticalGroup(
            panelContadorGPUMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(maxGPUMSI, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelContadorGPUMSILayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(tieneGPUMSI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout panelGPUMSILayout = new javax.swing.GroupLayout(panelGPUMSI);
        panelGPUMSI.setLayout(panelGPUMSILayout);
        panelGPUMSILayout.setHorizontalGroup(
            panelGPUMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGPUMSILayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(GPUMSI, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelGPUMSILayout.createSequentialGroup()
                .addContainerGap(156, Short.MAX_VALUE)
                .addComponent(panelContadorGPUMSI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelGPUMSILayout.setVerticalGroup(
            panelGPUMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelGPUMSILayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(GPUMSI)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(panelContadorGPUMSI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelRAMMSI.setBackground(new java.awt.Color(102, 102, 102));
        panelRAMMSI.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        panelRAMMSI.setForeground(new java.awt.Color(102, 102, 102));
        panelRAMMSI.setPreferredSize(new java.awt.Dimension(221, 94));

        RAMMSI.setFont(new java.awt.Font("Myanmar Text", 1, 14)); // NOI18N
        RAMMSI.setText("RAM");

        panelContadorRAMMSI.setPreferredSize(new java.awt.Dimension(55, 30));

        tieneRAMMSI.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        tieneRAMMSI.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        tieneRAMMSI.setText("RAMHP");
        tieneRAMMSI.setAlignmentY(0.0F);
        tieneRAMMSI.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tieneRAMMSI.setMaximumSize(new java.awt.Dimension(25, 30));
        tieneRAMMSI.setMinimumSize(new java.awt.Dimension(25, 30));
        tieneRAMMSI.setPreferredSize(new java.awt.Dimension(25, 30));

        maxRAMMSI.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        maxRAMMSI.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        maxRAMMSI.setText("/ 1");
        maxRAMMSI.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        maxRAMMSI.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout panelContadorRAMMSILayout = new javax.swing.GroupLayout(panelContadorRAMMSI);
        panelContadorRAMMSI.setLayout(panelContadorRAMMSILayout);
        panelContadorRAMMSILayout.setHorizontalGroup(
            panelContadorRAMMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelContadorRAMMSILayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(tieneRAMMSI, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maxRAMMSI, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelContadorRAMMSILayout.setVerticalGroup(
            panelContadorRAMMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(maxRAMMSI, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tieneRAMMSI, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelRAMMSILayout = new javax.swing.GroupLayout(panelRAMMSI);
        panelRAMMSI.setLayout(panelRAMMSILayout);
        panelRAMMSILayout.setHorizontalGroup(
            panelRAMMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRAMMSILayout.createSequentialGroup()
                .addContainerGap(156, Short.MAX_VALUE)
                .addComponent(panelContadorRAMMSI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panelRAMMSILayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RAMMSI, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelRAMMSILayout.setVerticalGroup(
            panelRAMMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRAMMSILayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RAMMSI)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(panelContadorRAMMSI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelPSupplyMSI.setBackground(new java.awt.Color(102, 102, 102));
        panelPSupplyMSI.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        panelPSupplyMSI.setForeground(new java.awt.Color(102, 102, 102));
        panelPSupplyMSI.setPreferredSize(new java.awt.Dimension(221, 94));

        AlimentacionMSI.setFont(new java.awt.Font("Myanmar Text", 1, 14)); // NOI18N
        AlimentacionMSI.setText("PSupply");

        panelContadoPSupplyHP1.setPreferredSize(new java.awt.Dimension(55, 30));

        tienePSupplyMSI.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        tienePSupplyMSI.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        tienePSupplyMSI.setText("SUPPLYHP");
        tienePSupplyMSI.setAlignmentY(0.0F);
        tienePSupplyMSI.setMaximumSize(new java.awt.Dimension(25, 30));
        tienePSupplyMSI.setMinimumSize(new java.awt.Dimension(25, 30));
        tienePSupplyMSI.setPreferredSize(new java.awt.Dimension(25, 30));

        maxPSupplyMSI.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        maxPSupplyMSI.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        maxPSupplyMSI.setText("/ 1");
        maxPSupplyMSI.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        maxPSupplyMSI.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout panelContadoPSupplyHP1Layout = new javax.swing.GroupLayout(panelContadoPSupplyHP1);
        panelContadoPSupplyHP1.setLayout(panelContadoPSupplyHP1Layout);
        panelContadoPSupplyHP1Layout.setHorizontalGroup(
            panelContadoPSupplyHP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelContadoPSupplyHP1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(tienePSupplyMSI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maxPSupplyMSI, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelContadoPSupplyHP1Layout.setVerticalGroup(
            panelContadoPSupplyHP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContadoPSupplyHP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(maxPSupplyMSI, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tienePSupplyMSI, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout panelPSupplyMSILayout = new javax.swing.GroupLayout(panelPSupplyMSI);
        panelPSupplyMSI.setLayout(panelPSupplyMSILayout);
        panelPSupplyMSILayout.setHorizontalGroup(
            panelPSupplyMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPSupplyMSILayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelContadoPSupplyHP1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panelPSupplyMSILayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(AlimentacionMSI, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(152, Short.MAX_VALUE))
        );
        panelPSupplyMSILayout.setVerticalGroup(
            panelPSupplyMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPSupplyMSILayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(AlimentacionMSI)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(panelContadoPSupplyHP1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelPBaseMSI.setBackground(new java.awt.Color(102, 102, 102));
        panelPBaseMSI.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        panelPBaseMSI.setForeground(new java.awt.Color(102, 102, 102));
        panelPBaseMSI.setPreferredSize(new java.awt.Dimension(221, 94));

        PlacaMSI.setFont(new java.awt.Font("Myanmar Text", 1, 14)); // NOI18N
        PlacaMSI.setText("Placa Base");

        panelContadorPlacaMSI.setPreferredSize(new java.awt.Dimension(55, 30));

        tienePlacaMSI.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        tienePlacaMSI.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        tienePlacaMSI.setText("PLACAHP");
        tienePlacaMSI.setAlignmentY(0.0F);
        tienePlacaMSI.setMaximumSize(new java.awt.Dimension(25, 30));
        tienePlacaMSI.setMinimumSize(new java.awt.Dimension(25, 30));
        tienePlacaMSI.setPreferredSize(new java.awt.Dimension(25, 30));

        maxPlacaMSI.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        maxPlacaMSI.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        maxPlacaMSI.setText("/ 1");
        maxPlacaMSI.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        maxPlacaMSI.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout panelContadorPlacaMSILayout = new javax.swing.GroupLayout(panelContadorPlacaMSI);
        panelContadorPlacaMSI.setLayout(panelContadorPlacaMSILayout);
        panelContadorPlacaMSILayout.setHorizontalGroup(
            panelContadorPlacaMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelContadorPlacaMSILayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(tienePlacaMSI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maxPlacaMSI, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelContadorPlacaMSILayout.setVerticalGroup(
            panelContadorPlacaMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContadorPlacaMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(maxPlacaMSI, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tienePlacaMSI, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout panelPBaseMSILayout = new javax.swing.GroupLayout(panelPBaseMSI);
        panelPBaseMSI.setLayout(panelPBaseMSILayout);
        panelPBaseMSILayout.setHorizontalGroup(
            panelPBaseMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPBaseMSILayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPBaseMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPBaseMSILayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(panelContadorPlacaMSI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelPBaseMSILayout.createSequentialGroup()
                        .addComponent(PlacaMSI, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 111, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelPBaseMSILayout.setVerticalGroup(
            panelPBaseMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPBaseMSILayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PlacaMSI)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(panelContadorPlacaMSI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelCPUMSI.setBackground(new java.awt.Color(102, 102, 102));
        panelCPUMSI.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        panelCPUMSI.setForeground(new java.awt.Color(102, 102, 102));
        panelCPUMSI.setPreferredSize(new java.awt.Dimension(221, 94));

        CPUMSI.setFont(new java.awt.Font("Myanmar Text", 1, 14)); // NOI18N
        CPUMSI.setText("CPU");

        panelContadorCPUMSI.setPreferredSize(new java.awt.Dimension(55, 30));

        maxCPUMSI.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        maxCPUMSI.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        maxCPUMSI.setText("/ 1");
        maxCPUMSI.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        maxCPUMSI.setPreferredSize(new java.awt.Dimension(30, 30));

        tieneCPUMSI.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        tieneCPUMSI.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        tieneCPUMSI.setText("CPUHP");
        tieneCPUMSI.setAlignmentY(0.0F);
        tieneCPUMSI.setMaximumSize(new java.awt.Dimension(25, 30));
        tieneCPUMSI.setMinimumSize(new java.awt.Dimension(25, 30));
        tieneCPUMSI.setPreferredSize(new java.awt.Dimension(25, 30));

        javax.swing.GroupLayout panelContadorCPUMSILayout = new javax.swing.GroupLayout(panelContadorCPUMSI);
        panelContadorCPUMSI.setLayout(panelContadorCPUMSILayout);
        panelContadorCPUMSILayout.setHorizontalGroup(
            panelContadorCPUMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelContadorCPUMSILayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(tieneCPUMSI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maxCPUMSI, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelContadorCPUMSILayout.setVerticalGroup(
            panelContadorCPUMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContadorCPUMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(maxCPUMSI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(tieneCPUMSI, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout panelCPUMSILayout = new javax.swing.GroupLayout(panelCPUMSI);
        panelCPUMSI.setLayout(panelCPUMSILayout);
        panelCPUMSILayout.setHorizontalGroup(
            panelCPUMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCPUMSILayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CPUMSI, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCPUMSILayout.createSequentialGroup()
                .addContainerGap(156, Short.MAX_VALUE)
                .addComponent(panelContadorCPUMSI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelCPUMSILayout.setVerticalGroup(
            panelCPUMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCPUMSILayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CPUMSI)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(panelContadorCPUMSI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        labelEstadoDirectorMSI.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        labelEstadoDirectorMSI.setText("El Director está:");

        estadoDirectorMSI.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        estadoDirectorMSI.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        estadoDirectorMSI.setText("El Director está:");

        labelEstadoPMMSI.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        labelEstadoPMMSI.setText("El PM está:");

        estadoPMMSI.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        estadoPMMSI.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        estadoPMMSI.setText("El PM está:");

        javax.swing.GroupLayout companyPanelMSILayout = new javax.swing.GroupLayout(companyPanelMSI);
        companyPanelMSI.setLayout(companyPanelMSILayout);
        companyPanelMSILayout.setHorizontalGroup(
            companyPanelMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(companyPanelMSILayout.createSequentialGroup()
                .addGroup(companyPanelMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, companyPanelMSILayout.createSequentialGroup()
                        .addComponent(panelGPUMSI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelEnsambladorMSI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, companyPanelMSILayout.createSequentialGroup()
                        .addComponent(panelRAMMSI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelPSupplyMSI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, companyPanelMSILayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panelCPUMSI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(companyPanelMSILayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(companyPanelMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelEstadoPMMSI, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(companyPanelMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, companyPanelMSILayout.createSequentialGroup()
                            .addComponent(labelEstadoDirectorMSI, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(17, 17, 17))
                        .addGroup(companyPanelMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(estadoDirectorMSI, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(estadoPMMSI, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(companyPanelMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelPBaseMSI, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, companyPanelMSILayout.createSequentialGroup()
                        .addComponent(logoMSI, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        companyPanelMSILayout.setVerticalGroup(
            companyPanelMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(companyPanelMSILayout.createSequentialGroup()
                .addGroup(companyPanelMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(companyPanelMSILayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(logoMSI)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addComponent(panelPBaseMSI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(companyPanelMSILayout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(labelEstadoDirectorMSI)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(estadoDirectorMSI)
                        .addGap(18, 18, 18)
                        .addComponent(labelEstadoPMMSI)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(estadoPMMSI)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(panelCPUMSI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(companyPanelMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelPSupplyMSI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelRAMMSI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(companyPanelMSILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelEnsambladorMSI, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelGPUMSI, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        companyPanelHP.setBackground(new java.awt.Color(102, 102, 102));
        companyPanelHP.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        companyPanelHP.setForeground(new java.awt.Color(102, 102, 102));
        companyPanelHP.setPreferredSize(new java.awt.Dimension(451, 500));

        logoHP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/HP LOGO RESIZED.png"))); // NOI18N
        logoHP.setToolTipText("");

        panelEnsambladorHP.setBackground(new java.awt.Color(102, 102, 102));
        panelEnsambladorHP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        panelEnsambladorHP.setForeground(new java.awt.Color(102, 102, 102));
        panelEnsambladorHP.setPreferredSize(new java.awt.Dimension(221, 94));

        ENSAMBLADORHP.setFont(new java.awt.Font("Myanmar Text", 1, 14)); // NOI18N
        ENSAMBLADORHP.setText("Ensamblador");

        jProgressBar4.setValue(50);

        javax.swing.GroupLayout panelEnsambladorHPLayout = new javax.swing.GroupLayout(panelEnsambladorHP);
        panelEnsambladorHP.setLayout(panelEnsambladorHPLayout);
        panelEnsambladorHPLayout.setHorizontalGroup(
            panelEnsambladorHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEnsambladorHPLayout.createSequentialGroup()
                .addGroup(panelEnsambladorHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEnsambladorHPLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(ENSAMBLADORHP, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelEnsambladorHPLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jProgressBar4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        panelEnsambladorHPLayout.setVerticalGroup(
            panelEnsambladorHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEnsambladorHPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ENSAMBLADORHP)
                .addGap(18, 18, 18)
                .addComponent(jProgressBar4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        panelGPUHP.setBackground(new java.awt.Color(102, 102, 102));
        panelGPUHP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        panelGPUHP.setForeground(new java.awt.Color(102, 102, 102));
        panelGPUHP.setPreferredSize(new java.awt.Dimension(221, 94));

        GPUHP.setFont(new java.awt.Font("Myanmar Text", 1, 14)); // NOI18N
        GPUHP.setText("GPU");

        panelContadorGPUHP.setPreferredSize(new java.awt.Dimension(55, 30));

        maxGPUHP.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        maxGPUHP.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        maxGPUHP.setText("/ 1");
        maxGPUHP.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        maxGPUHP.setPreferredSize(new java.awt.Dimension(30, 30));

        tieneGPUHP.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        tieneGPUHP.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        tieneGPUHP.setText("CPUHP");
        tieneGPUHP.setMaximumSize(new java.awt.Dimension(25, 30));
        tieneGPUHP.setMinimumSize(new java.awt.Dimension(25, 30));
        tieneGPUHP.setPreferredSize(new java.awt.Dimension(25, 30));

        javax.swing.GroupLayout panelContadorGPUHPLayout = new javax.swing.GroupLayout(panelContadorGPUHP);
        panelContadorGPUHP.setLayout(panelContadorGPUHPLayout);
        panelContadorGPUHPLayout.setHorizontalGroup(
            panelContadorGPUHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContadorGPUHPLayout.createSequentialGroup()
                .addComponent(tieneGPUHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maxGPUHP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelContadorGPUHPLayout.setVerticalGroup(
            panelContadorGPUHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(maxGPUHP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelContadorGPUHPLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(tieneGPUHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout panelGPUHPLayout = new javax.swing.GroupLayout(panelGPUHP);
        panelGPUHP.setLayout(panelGPUHPLayout);
        panelGPUHPLayout.setHorizontalGroup(
            panelGPUHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelGPUHPLayout.createSequentialGroup()
                .addContainerGap(156, Short.MAX_VALUE)
                .addComponent(panelContadorGPUHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panelGPUHPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(GPUHP, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelGPUHPLayout.setVerticalGroup(
            panelGPUHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelGPUHPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(GPUHP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(panelContadorGPUHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelRAMHP.setBackground(new java.awt.Color(102, 102, 102));
        panelRAMHP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        panelRAMHP.setForeground(new java.awt.Color(102, 102, 102));
        panelRAMHP.setPreferredSize(new java.awt.Dimension(221, 94));

        RAMHP.setFont(new java.awt.Font("Myanmar Text", 1, 14)); // NOI18N
        RAMHP.setText("RAM");

        panelContadorRAMHP.setPreferredSize(new java.awt.Dimension(55, 30));

        tieneRAMHP.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        tieneRAMHP.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        tieneRAMHP.setText("RAMHP");
        tieneRAMHP.setAlignmentY(0.0F);
        tieneRAMHP.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tieneRAMHP.setMaximumSize(new java.awt.Dimension(25, 30));
        tieneRAMHP.setMinimumSize(new java.awt.Dimension(25, 30));
        tieneRAMHP.setPreferredSize(new java.awt.Dimension(25, 30));

        maxRAMHP.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        maxRAMHP.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        maxRAMHP.setText("/ 1");
        maxRAMHP.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        maxRAMHP.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout panelContadorRAMHPLayout = new javax.swing.GroupLayout(panelContadorRAMHP);
        panelContadorRAMHP.setLayout(panelContadorRAMHPLayout);
        panelContadorRAMHPLayout.setHorizontalGroup(
            panelContadorRAMHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelContadorRAMHPLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(tieneRAMHP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maxRAMHP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelContadorRAMHPLayout.setVerticalGroup(
            panelContadorRAMHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tieneRAMHP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(maxRAMHP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelRAMHPLayout = new javax.swing.GroupLayout(panelRAMHP);
        panelRAMHP.setLayout(panelRAMHPLayout);
        panelRAMHPLayout.setHorizontalGroup(
            panelRAMHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRAMHPLayout.createSequentialGroup()
                .addContainerGap(156, Short.MAX_VALUE)
                .addComponent(panelContadorRAMHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panelRAMHPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RAMHP, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelRAMHPLayout.setVerticalGroup(
            panelRAMHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRAMHPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RAMHP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(panelContadorRAMHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelPSupplyHP.setBackground(new java.awt.Color(102, 102, 102));
        panelPSupplyHP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        panelPSupplyHP.setForeground(new java.awt.Color(102, 102, 102));
        panelPSupplyHP.setPreferredSize(new java.awt.Dimension(221, 94));

        AlimentacionHP.setFont(new java.awt.Font("Myanmar Text", 1, 14)); // NOI18N
        AlimentacionHP.setText("PSupply");

        panelContadoPSupplyHP.setPreferredSize(new java.awt.Dimension(55, 30));

        tienePSupplyHP.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        tienePSupplyHP.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        tienePSupplyHP.setText("SUPPLYHP");
        tienePSupplyHP.setAlignmentY(0.0F);
        tienePSupplyHP.setMaximumSize(new java.awt.Dimension(25, 30));
        tienePSupplyHP.setMinimumSize(new java.awt.Dimension(25, 30));
        tienePSupplyHP.setPreferredSize(new java.awt.Dimension(25, 30));

        maxPSupplyHP.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        maxPSupplyHP.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        maxPSupplyHP.setText("/ 1");
        maxPSupplyHP.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        maxPSupplyHP.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout panelContadoPSupplyHPLayout = new javax.swing.GroupLayout(panelContadoPSupplyHP);
        panelContadoPSupplyHP.setLayout(panelContadoPSupplyHPLayout);
        panelContadoPSupplyHPLayout.setHorizontalGroup(
            panelContadoPSupplyHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelContadoPSupplyHPLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(tienePSupplyHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maxPSupplyHP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelContadoPSupplyHPLayout.setVerticalGroup(
            panelContadoPSupplyHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContadoPSupplyHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(maxPSupplyHP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tienePSupplyHP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout panelPSupplyHPLayout = new javax.swing.GroupLayout(panelPSupplyHP);
        panelPSupplyHP.setLayout(panelPSupplyHPLayout);
        panelPSupplyHPLayout.setHorizontalGroup(
            panelPSupplyHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPSupplyHPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelContadoPSupplyHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panelPSupplyHPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(AlimentacionHP, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(152, Short.MAX_VALUE))
        );
        panelPSupplyHPLayout.setVerticalGroup(
            panelPSupplyHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPSupplyHPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(AlimentacionHP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(panelContadoPSupplyHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelPBaseHP.setBackground(new java.awt.Color(102, 102, 102));
        panelPBaseHP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        panelPBaseHP.setForeground(new java.awt.Color(102, 102, 102));
        panelPBaseHP.setPreferredSize(new java.awt.Dimension(221, 94));

        PlacaHP.setFont(new java.awt.Font("Myanmar Text", 1, 14)); // NOI18N
        PlacaHP.setText("Placa Base");

        panelContadorPlacaHP.setPreferredSize(new java.awt.Dimension(55, 30));

        tienePlacaHP.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        tienePlacaHP.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        tienePlacaHP.setText("PLACAHP");
        tienePlacaHP.setAlignmentY(0.0F);
        tienePlacaHP.setMaximumSize(new java.awt.Dimension(25, 30));
        tienePlacaHP.setMinimumSize(new java.awt.Dimension(25, 30));
        tienePlacaHP.setPreferredSize(new java.awt.Dimension(25, 30));

        maxPlacaHP.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        maxPlacaHP.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        maxPlacaHP.setText("/ 1");
        maxPlacaHP.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        maxPlacaHP.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout panelContadorPlacaHPLayout = new javax.swing.GroupLayout(panelContadorPlacaHP);
        panelContadorPlacaHP.setLayout(panelContadorPlacaHPLayout);
        panelContadorPlacaHPLayout.setHorizontalGroup(
            panelContadorPlacaHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelContadorPlacaHPLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(tienePlacaHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maxPlacaHP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelContadorPlacaHPLayout.setVerticalGroup(
            panelContadorPlacaHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContadorPlacaHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(maxPlacaHP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tienePlacaHP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout panelPBaseHPLayout = new javax.swing.GroupLayout(panelPBaseHP);
        panelPBaseHP.setLayout(panelPBaseHPLayout);
        panelPBaseHPLayout.setHorizontalGroup(
            panelPBaseHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPBaseHPLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPBaseHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPBaseHPLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(panelContadorPlacaHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelPBaseHPLayout.createSequentialGroup()
                        .addComponent(PlacaHP, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 111, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelPBaseHPLayout.setVerticalGroup(
            panelPBaseHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPBaseHPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PlacaHP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(panelContadorPlacaHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelCPUHP.setBackground(new java.awt.Color(102, 102, 102));
        panelCPUHP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        panelCPUHP.setForeground(new java.awt.Color(102, 102, 102));
        panelCPUHP.setPreferredSize(new java.awt.Dimension(221, 94));

        CPUHP.setFont(new java.awt.Font("Myanmar Text", 1, 14)); // NOI18N
        CPUHP.setText("CPU");

        panelContadorCPUHP.setPreferredSize(new java.awt.Dimension(55, 30));

        maxCPUHP.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        maxCPUHP.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        maxCPUHP.setText("/ 1");
        maxCPUHP.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        maxCPUHP.setPreferredSize(new java.awt.Dimension(30, 30));

        tieneCPUHP.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        tieneCPUHP.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        tieneCPUHP.setText("CPUHP");
        tieneCPUHP.setAlignmentY(0.0F);
        tieneCPUHP.setMaximumSize(new java.awt.Dimension(25, 30));
        tieneCPUHP.setMinimumSize(new java.awt.Dimension(25, 30));
        tieneCPUHP.setPreferredSize(new java.awt.Dimension(25, 30));

        javax.swing.GroupLayout panelContadorCPUHPLayout = new javax.swing.GroupLayout(panelContadorCPUHP);
        panelContadorCPUHP.setLayout(panelContadorCPUHPLayout);
        panelContadorCPUHPLayout.setHorizontalGroup(
            panelContadorCPUHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelContadorCPUHPLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(tieneCPUHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maxCPUHP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelContadorCPUHPLayout.setVerticalGroup(
            panelContadorCPUHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContadorCPUHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(maxCPUHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(tieneCPUHP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout panelCPUHPLayout = new javax.swing.GroupLayout(panelCPUHP);
        panelCPUHP.setLayout(panelCPUHPLayout);
        panelCPUHPLayout.setHorizontalGroup(
            panelCPUHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCPUHPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CPUHP, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCPUHPLayout.createSequentialGroup()
                .addContainerGap(156, Short.MAX_VALUE)
                .addComponent(panelContadorCPUHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelCPUHPLayout.setVerticalGroup(
            panelCPUHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCPUHPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CPUHP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(panelContadorCPUHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        labelDia.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        labelDia.setText("Dia:");

        labelEstadoDirectorHP.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        labelEstadoDirectorHP.setText("El Director está:");

        labelEstadoPMHP.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        labelEstadoPMHP.setText("El PM está:");

        estadoDirectorHP.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        estadoDirectorHP.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        estadoDirectorHP.setText("El Director está:");

        estadoPMHP.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        estadoPMHP.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        estadoPMHP.setText("El PM está:");

        javax.swing.GroupLayout companyPanelHPLayout = new javax.swing.GroupLayout(companyPanelHP);
        companyPanelHP.setLayout(companyPanelHPLayout);
        companyPanelHPLayout.setHorizontalGroup(
            companyPanelHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(companyPanelHPLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(companyPanelHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(companyPanelHPLayout.createSequentialGroup()
                        .addGroup(companyPanelHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(companyPanelHPLayout.createSequentialGroup()
                                .addGroup(companyPanelHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panelCPUHP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panelPBaseHP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addComponent(labelEstadoPMHP, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(companyPanelHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, companyPanelHPLayout.createSequentialGroup()
                                    .addComponent(panelGPUHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(panelEnsambladorHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, companyPanelHPLayout.createSequentialGroup()
                                    .addComponent(panelRAMHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(panelPSupplyHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())
                    .addGroup(companyPanelHPLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(logoHP, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(companyPanelHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, companyPanelHPLayout.createSequentialGroup()
                                .addComponent(labelDia, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(141, 141, 141))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, companyPanelHPLayout.createSequentialGroup()
                                .addComponent(labelEstadoDirectorHP, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35))
                            .addGroup(companyPanelHPLayout.createSequentialGroup()
                                .addGroup(companyPanelHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(estadoDirectorHP, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                                    .addComponent(estadoPMHP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())))))
        );
        companyPanelHPLayout.setVerticalGroup(
            companyPanelHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(companyPanelHPLayout.createSequentialGroup()
                .addGroup(companyPanelHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(companyPanelHPLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(logoHP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelPBaseHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelCPUHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(companyPanelHPLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(labelDia)
                        .addGap(18, 18, 18)
                        .addComponent(labelEstadoDirectorHP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(estadoDirectorHP)
                        .addGap(18, 18, 18)
                        .addComponent(labelEstadoPMHP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(estadoPMHP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(companyPanelHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelPSupplyHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelRAMHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(companyPanelHPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelEnsambladorHP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelGPUHP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setPreferredSize(new java.awt.Dimension(920, 30));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 902, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(companyPanelHP, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(companyPanelMSI, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 902, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(companyPanelHP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
                    .addComponent(companyPanelMSI, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AlimentacionHP;
    private javax.swing.JLabel AlimentacionMSI;
    private javax.swing.JLabel CPUHP;
    private javax.swing.JLabel CPUMSI;
    private javax.swing.JLabel ENSAMBLADORHP;
    private javax.swing.JLabel ENSAMBLADORMSI;
    private javax.swing.JLabel GPUHP;
    private javax.swing.JLabel GPUMSI;
    private javax.swing.JLabel PlacaHP;
    private javax.swing.JLabel PlacaMSI;
    private javax.swing.JLabel RAMHP;
    private javax.swing.JLabel RAMMSI;
    private javax.swing.JPanel companyPanelHP;
    private javax.swing.JPanel companyPanelMSI;
    private javax.swing.JLabel estadoDirectorHP;
    private javax.swing.JLabel estadoDirectorMSI;
    private javax.swing.JLabel estadoPMHP;
    private javax.swing.JLabel estadoPMMSI;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar3;
    private javax.swing.JProgressBar jProgressBar4;
    private javax.swing.JLabel labelDia;
    private javax.swing.JLabel labelEstadoDirectorHP;
    private javax.swing.JLabel labelEstadoDirectorMSI;
    private javax.swing.JLabel labelEstadoPMHP;
    private javax.swing.JLabel labelEstadoPMMSI;
    private javax.swing.JLabel logoHP;
    private javax.swing.JLabel logoMSI;
    private javax.swing.JLabel maxCPUHP;
    private javax.swing.JLabel maxCPUMSI;
    private javax.swing.JLabel maxGPUHP;
    private javax.swing.JLabel maxGPUMSI;
    private javax.swing.JLabel maxPSupplyHP;
    private javax.swing.JLabel maxPSupplyMSI;
    private javax.swing.JLabel maxPlacaHP;
    private javax.swing.JLabel maxPlacaMSI;
    private javax.swing.JLabel maxRAMHP;
    private javax.swing.JLabel maxRAMMSI;
    private javax.swing.JPanel panelCPUHP;
    private javax.swing.JPanel panelCPUMSI;
    private javax.swing.JPanel panelContadoPSupplyHP;
    private javax.swing.JPanel panelContadoPSupplyHP1;
    private javax.swing.JPanel panelContadorCPUHP;
    private javax.swing.JPanel panelContadorCPUMSI;
    private javax.swing.JPanel panelContadorGPUHP;
    private javax.swing.JPanel panelContadorGPUMSI;
    private javax.swing.JPanel panelContadorPlacaHP;
    private javax.swing.JPanel panelContadorPlacaMSI;
    private javax.swing.JPanel panelContadorRAMHP;
    private javax.swing.JPanel panelContadorRAMMSI;
    private javax.swing.JPanel panelEnsambladorHP;
    private javax.swing.JPanel panelEnsambladorMSI;
    private javax.swing.JPanel panelGPUHP;
    private javax.swing.JPanel panelGPUMSI;
    private javax.swing.JPanel panelPBaseHP;
    private javax.swing.JPanel panelPBaseMSI;
    private javax.swing.JPanel panelPSupplyHP;
    private javax.swing.JPanel panelPSupplyMSI;
    private javax.swing.JPanel panelRAMHP;
    private javax.swing.JPanel panelRAMMSI;
    private javax.swing.JLabel tieneCPUHP;
    private javax.swing.JLabel tieneCPUMSI;
    private javax.swing.JLabel tieneGPUHP;
    private javax.swing.JLabel tieneGPUMSI;
    private javax.swing.JLabel tienePSupplyHP;
    private javax.swing.JLabel tienePSupplyMSI;
    private javax.swing.JLabel tienePlacaHP;
    private javax.swing.JLabel tienePlacaMSI;
    private javax.swing.JLabel tieneRAMHP;
    private javax.swing.JLabel tieneRAMMSI;
    // End of variables declaration//GEN-END:variables
}
