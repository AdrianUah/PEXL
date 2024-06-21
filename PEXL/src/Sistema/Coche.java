/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sistema;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
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
    private boolean nuevo=true;
    private boolean direccion=true;
    private String info="";
    
    public Coche(AtomicInteger pause, String id, Rally rally,Rally rally2,Rally rally3,Rally rally4,int idRally){
        this.pause=pause;
        this.id=id;
        this.rally=rally;
        this.rally2=rally2;
        this.rally3=rally3;
        this.rally4=rally4;
        this.idRally=idRally;
    }

    public boolean isNuevo() {
        return nuevo;
    }

    public void setNuevo(boolean nuevo) {
        this.nuevo = nuevo;
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
        info = "("+gasolinaCoche+"/"+capacidadGasolina+"L)("+tipoRuedas+")";
        return info;
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
                while(pause.get()!=0){}
                direccion=true;               
                rally.llenarParking(this);
                while(pause.get()!=0){}
                rally.liberarParking(isDireccion(),getid());
                
                if(idRally<=2){
                    while(pause.get()!=0){}
                    rally.parkingPuente(isDireccion(),getid(),getInfo());
                    while(pause.get()!=0){}
                    rally.cruzarPuente(isDireccion(),getid(),getInfo());                  
                    while(pause.get()!=0){}
                    rally.puenteGasolinera(isDireccion(),getid(),getInfo());
                    
                    if (gasolinaCoche < capacidadGasolina * 0.5) {
                        while(pause.get()!=0){}
                        rally.repostarCoche(this);                                             
                    }                   
                    switch (idRally){
                        case 1 -> {                           
                            while(pause.get()!=0){}
                            rally.entrarTramo(this);                                                  
                        }
                        case 2 -> {
                            while(pause.get()!=0){}
                            rally2.entrarTramo(this);
                        }
                    }
                    while(pause.get()!=0){}
                    direccion=false;
                    rally.tramoPuente(this);
                    while(pause.get()!=0){}
                    rally.cruzarPuente(isDireccion(),getid(),getInfo());
                    while(pause.get()!=0){}
                    rally.parkingPuente(isDireccion(),getid(),getInfo());
                    while(pause.get()!=0){}
                    rally.liberarParking(isDireccion(), getid());
                }else {
                    while(pause.get()!=0){}
                    rally3.parkingPuente(isDireccion(),getid(),getInfo());
                    while(pause.get()!=0){}
                    rally3.cruzarPuente(isDireccion(),getid(),getInfo());
                    while(pause.get()!=0){}
                    rally3.puenteGasolinera(isDireccion(),getid(),getInfo());                   
                    if (gasolinaCoche < capacidadGasolina * 0.5) {
                        while(pause.get()!=0){}
                        rally3.repostarCoche(this);
                        while(pause.get()!=0){}                                      
                    }                   
                    switch (idRally){
                        case 3 -> {
                            while(pause.get()!=0){}
                            rally3.entrarTramo(this);
                        }
                        case 4 -> {
                            while(pause.get()!=0){}
                            rally4.entrarTramo(this);
                        }
                    }
                    direccion=false;
                    while(pause.get()!=0){}
                    rally3.tramoPuente(this);
                    while(pause.get()!=0){}
                    rally3.cruzarPuente(isDireccion(), getid(), getInfo());
                    while(pause.get()!=0){}
                    rally3.parkingPuente(isDireccion(),getid(),getInfo());
                    while(pause.get()!=0){}
                    rally3.liberarParking(isDireccion(), getid());
                }               
                while(pause.get()!=0){}
                int r;
                do {
                    r=new Random().nextInt(4)+1 ;
                } while (r == idRally);
                idRally=r;  
            }
            
        }catch (InterruptedException | BrokenBarrierException ex) {
            Logger.getLogger(Coche.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
