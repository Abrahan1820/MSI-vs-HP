public class TimeConfig {
    private static int duracionDiaEnSegundos = 10000; // Valor por defecto: 24 horas en segundos

    // Método para establecer la duración del día
    public static void setDuracionDia(int segundos) {
        duracionDiaEnSegundos = segundos;
    }

    // Método para obtener la duración del día
    public static int getDuracionDia() {
        return duracionDiaEnSegundos;
    }

    // Método para convertir horas a segundos
    public static int convertirHorasASegundos(int horas) {
        return (horas * duracionDiaEnSegundos) / 24; // Proporcional a la duración del día
    }

    // Método para convertir minutos a segundos
    public static int convertirMinutosASegundos(int minutos) {
        return (minutos * duracionDiaEnSegundos) / (24 * 60); // Proporcional a la duración del día
    }
}