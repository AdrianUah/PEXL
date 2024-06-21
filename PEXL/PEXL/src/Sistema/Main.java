
package Sistema;

import Interfaces.Interfaz1;
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
        Interfaz1 interfaz1=new Interfaz1(pause,rally,rally2,rally3,rally4);
        Thread threadinterfaz1=new Thread(interfaz1);
             
        cs.start();
        cc.start();
        threadinterfaz1.start();
    }
}
