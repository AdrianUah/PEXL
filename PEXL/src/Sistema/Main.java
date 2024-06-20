
package Sistema;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Adrian
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        AtomicInteger pause=new AtomicInteger(0);
        Rally rally=new Rally(pause);
        Rally rally2=new Rally(pause);
        Rally rally3=new Rally(pause);
        Rally rally4=new Rally(pause);
        CreadorCoches cc = new CreadorCoches(pause,rally,rally2,rally3,rally4);
        CreadorCaminones cs = new CreadorCaminones(pause,rally,rally2,rally3,rally4);
        //Pantalla pantalla=new Pantalla(aeropuertomadrid,aeropuertobarcelona);
        //Thread threadPantalla= new Thread(pantalla);
        //threadPantalla.start();
        
        cs.start();
        cc.start();
        
    }
}
