/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sistema;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrian
 */
public class CreadorCoches extends Thread{
    ExecutorService pool=Executors.newFixedThreadPool(8000);
    Rally rally;
    Rally rally2;
    Rally rally3;
    Rally rally4;
    AtomicInteger pause;
    
    Random random= new Random();
    
    public CreadorCoches(AtomicInteger pause, Rally rally, Rally rally2, Rally rally3, Rally rally4){
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
                String id=String.format("%04d-%s", i, generateRandomLetters(3));
                int idRally;
                if(i%2==0){
                    idRally=random.nextInt(2)+1;
                }else{
                    idRally=random.nextInt(2)+3;
                }               
                pool.execute(new Coche(pause,id,rally,rally2,rally3,rally4,idRally));
                Thread.sleep((new Random().nextInt(3)+1)*1000);
            
            } catch (InterruptedException ex) {
                Logger.getLogger(CreadorCoches.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public String generateRandomLetters(int length)
    {
        String letters = "BCDFGHJKLMNPQRSTVWXYZ";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(letters.charAt(random.nextInt(letters.length())));
        }
        return sb.toString();
    }
}
