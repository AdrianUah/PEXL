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
public class Camion extends Thread{
    
    private AtomicInteger pause;
    private String id;
    private Rally rally;
    private Rally rally2;
    private Rally rally3;
    private Rally rally4;
    private int idRally, capacidadGasolina, gasolinaCamion;
    private boolean direccion=true;
    
    public Camion(AtomicInteger pause, String id, Rally rally,Rally rally2,Rally rally3,Rally rally4,int idRally){
        this.pause=pause;
        this.id=id;
        this.rally=rally;
        this.rally2=rally2;
        this.rally3=rally3;
        this.rally4=rally4;
        this.idRally=idRally;
    }

    public String getid() {
        return id;
    }

    public void setCapacidadGasolina(int capacidadGasolina) {
        this.capacidadGasolina = capacidadGasolina;
    }

    
    public int getIdRally() {
        return idRally;
    }

    public String getInfo() {
        return "("+gasolinaCamion+"/"+capacidadGasolina+"L)";
    }
    
    public boolean isDireccion() {
        return direccion;
    }

    public int getCapacidadGasolina() {
        return capacidadGasolina;
    }

    public void setGasolinaCamion(int gasolinaCamion) {
        this.gasolinaCamion = gasolinaCamion;
    }

    public int getGasolinaCamion() {
        return gasolinaCamion;
    }

    
    
    
    
    @Override
    public void run(){
        try {
            while(true){
                while(pause.get()!=0){}
                rally.ciudad(this);
                direccion=true; 
                while(pause.get()!=0){}                      
                rally.ciudadParking(this);
                while(pause.get()!=0){}
                rally.parkingCamion(this);
                while(pause.get()!=0){}
                rally.liberarParking(isDireccion(),getid());
                
                if(idRally<=2){
                    rally.parkingPuente(isDireccion(),getid(),getInfo());
                    while(pause.get()!=0){}
                    rally.ocuparPuente(isDireccion(),getid(),getInfo());
                    while(pause.get()!=0){}
                    rally.liberarPuente(isDireccion(),getid(),getInfo());
                    while(pause.get()!=0){}
                    rally.puenteGasolinera(isDireccion(),getid(),getInfo());
                    while(pause.get()!=0){}
                    rally.repostarCamion(this);
                    while(pause.get()!=0){}
                    rally.liberarGasolineraCamion(this);
                    direccion=false;
                    while(pause.get()!=0){}
                    rally.puenteGasolinera(isDireccion(),getid(),getInfo());
                    while(pause.get()!=0){}
                    rally.ocuparPuente(isDireccion(),getid(),getInfo());
                    while(pause.get()!=0){}
                    rally.liberarPuente(isDireccion(),getid(),getInfo());
                    while(pause.get()!=0){}
                    rally.parkingPuente(isDireccion(),getid(),getInfo());
                    
                    

                }else {
                    rally3.parkingPuente(isDireccion(),getid(),getInfo());
                    while(pause.get()!=0){}
                    rally3.ocuparPuente(isDireccion(),getid(),getInfo());
                    while(pause.get()!=0){}
                    rally3.liberarPuente(isDireccion(),getid(),getInfo());
                    while(pause.get()!=0){}
                    rally3.puenteGasolinera(isDireccion(),getid(),getInfo());
                    while(pause.get()!=0){}
                    rally3.repostarCamion(this);
                    while(pause.get()!=0){}
                    rally3.liberarGasolineraCamion(this);
                    direccion=false;
                    while(pause.get()!=0){}
                    rally3.puenteGasolinera(isDireccion(),getid(),getInfo());
                    while(pause.get()!=0){}
                    rally3.ocuparPuente(isDireccion(),getid(),getInfo());
                    while(pause.get()!=0){}
                    rally3.liberarPuente(isDireccion(),getid(),getInfo());
                    while(pause.get()!=0){}
                    rally3.parkingPuente(isDireccion(),getid(),getInfo());
                    while(pause.get()!=0){}
                    rally3.liberarParking( isDireccion(),getid());
                }
                rally.parkingCamion(this);
                rally.ciudadParking(this);                        
            }
            
        }catch (InterruptedException ex) {
            Logger.getLogger(Camion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
            
        
        //rally.cruzarPuente(this);
        
        
            
        
        //rally.cruzarPuente(this);
    }
}
