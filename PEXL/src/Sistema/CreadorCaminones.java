/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sistema;

import static java.lang.Math.random;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrian
 */
public class CreadorCaminones extends Thread{
    ExecutorService pool=Executors.newFixedThreadPool(1000);
    Rally rally;
    Rally rally2;
    Rally rally3;
    Rally rally4;
    AtomicInteger pause;
    
    Random random= new Random();
    
    public CreadorCaminones(AtomicInteger pause, Rally rally, Rally rally2, Rally rally3, Rally rally4){
        this.pause=pause;
        this.rally=rally;
        this.rally2=rally2;
        this.rally3=rally3;
        this.rally4=rally4;
    }
    
    public void run(){
        int i=0;
        while(true){
            try {
                while(pause.get()!=0){};
                i++;
                String id= "CC-" + String.format("%04d", i);
                int idRally;
                if(i%2==0){
                    idRally=1;
                }else{
                    idRally=3;
                }               
                pool.execute(new Camion(pause,id,rally,rally2,rally3,rally4,idRally));
                Thread.sleep((new Random().nextInt(3)+2)*1000);
            
            } catch (InterruptedException ex) {
                Logger.getLogger(CreadorCaminones.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
