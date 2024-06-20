
package Sistema;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Adrian
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        AtomicInteger pause=new AtomicInteger(0);
        Rally rally=new Rally();
        Rally rally2=new Rally();
        Rally rally3=new Rally();
        Rally rally4=new Rally();
        CreadorCoches cc = new CreadorCoches(pause,rally,rally2,rally3,rally4);
        //Pantalla pantalla=new Pantalla(aeropuertomadrid,aeropuertobarcelona);
        //Thread threadPantalla= new Thread(pantalla);
        //threadPantalla.start();
        cc.start();
        
    }
}
