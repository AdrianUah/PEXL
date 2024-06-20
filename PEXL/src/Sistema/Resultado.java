/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sistema;

import java.time.LocalTime;

/**
 *
 * @author Adrian
 */
public class Resultado {
    
private final String matricula;
    private final LocalTime horaFinalizacion;
    private final String tramo;
    private final long tiempoTotal;

    public Resultado(String matricula, LocalTime horaFinalizacion, int tramo, long tiempoTotal) {
        this.matricula = matricula;
        this.horaFinalizacion = horaFinalizacion;
        this.tramo = Integer.toString(tramo);
        this.tiempoTotal = tiempoTotal;
    }

    public String getMatricula() {
        return matricula;
    }

    public LocalTime getHoraFinalizacion() {
        return horaFinalizacion;
    }

    public String getTramo() {
        return tramo;
    }

    public long getTiempoTotal() {
        return tiempoTotal;
    }

    @Override
    public String toString() {
        return "Resultado{" +
                "matricula='" + matricula + '\'' +
                ", horaFinalizacion=" + horaFinalizacion +
                ", tramo='" + tramo + '\'' +
                ", tiempoTotal=" + tiempoTotal +
                '}';
    }
}
