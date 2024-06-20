/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sistema;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrian
 */
public class Coche extends Thread{
    
    private AtomicInteger pause;
    private String id, tipoRuedas;
    private Rally rally;
    private Rally rally2;
    private Rally rally3;
    private Rally rally4;
    private int idRally, capacidadGasolina, gasolinaCoche;
    private boolean direccion=true;
    
    public Coche(AtomicInteger pause, String id, Rally rally,Rally rally2,Rally rally3,Rally rally4,int idRally){
        this.pause=pause;
        this.id=id;
        this.rally=rally;
        this.rally2=rally2;
        this.rally3=rally3;
        this.rally4=rally4;
        this.idRally=idRally;
    }

    public String getTipoRuedas() {
        return tipoRuedas;
    }

    public String getid() {
        return id;
    }

    public void setCapacidadGasolina(int capacidadGasolina) {
        this.capacidadGasolina = capacidadGasolina;
    }

    public void setGasolinaCoche(int gasolinaCoche) {
        this.gasolinaCoche = gasolinaCoche;
    }

    public void setTiporuedas(String tipoRuedas) {
        this.tipoRuedas = tipoRuedas;
    }

    public int getIdRally() {
        return idRally;
    }

    public String getInfo() {
        return "("+gasolinaCoche+"/"+capacidadGasolina+"L)("+tipoRuedas+")";
    }
    
    public boolean isDireccion() {
        return direccion;
    }

    public int getCapacidadGasolina() {
        return capacidadGasolina;
    }

    public int getGasolinaCoche() {
        return gasolinaCoche;
    }

    
    
    
    
    @Override
    public void run(){
        try {
            while(true){
                while(pause.get()!=0){};
                rally.llenarParking(this);
                direccion=true;
                while(pause.get()!=0){};
                rally.parkingPuente(this);
                while(pause.get()!=0){};
                if(idRally<=2){
                    rally.ocuparPuente(this);
                    while(pause.get()!=0){};
                    rally.liberarPuente(this);
                    while(pause.get()!=0){};
                    rally.gasolineraPuente(this);
                    if (gasolinaCoche < capacidadGasolina * 0.5) {
                            rally.repostarCoche(this);
                            while(pause.get()!=0){};
                    }
                    switch (idRally){
                        case 1:
                            rally.entrarTramo(this);
                            System.out.println("Resultados del tramo:");
                            rally.getResultados().values().forEach(System.out::println);
                        break;
                        case 2:
                            rally2.entrarTramo(this);
                            System.out.println("Resultados del tramo:");
                            rally2.getResultados().values().forEach(System.out::println);
                        break;
                    }
                    direccion=false;
                    rally.gasolineraPuente(this);
                    rally.ocuparPuente(this);
                    rally.liberarPuente(this);
                    rally.parkingPuente(this);

                }else {
                    rally3.ocuparPuente(this);
                    while(pause.get()!=0){};
                    rally3.liberarPuente(this);
                    while(pause.get()!=0){};
                    if (gasolinaCoche < capacidadGasolina * 0.5) {
                            rally3.repostarCoche(this);
                            while(pause.get()!=0){};
                    }
                    switch (idRally){
                        case 3:
                            rally3.entrarTramo(this);
                            System.out.println("Resultados del tramo:");
                            rally3.getResultados().values().forEach(System.out::println);
                        break;
                        case 4:
                            rally4.entrarTramo(this);
                            System.out.println("Resultados del tramo:");
                            rally4.getResultados().values().forEach(System.out::println);
                        break;
                    }
                    direccion=false;
                    rally3.gasolineraPuente(this);
                    rally3.ocuparPuente(this);
                    rally3.liberarPuente(this);
                    rally3.parkingPuente(this);

                }
                int i;
                do {
                    i=new Random().nextInt(4)+1 ;
                } while (i == idRally);
                idRally=i;  
            }
            
        }catch (InterruptedException ex) {
            Logger.getLogger(Coche.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
            
        
        //rally.cruzarPuente(this);
    }
}
