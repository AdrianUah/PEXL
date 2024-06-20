/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sistema;

import Log.Log;
import java.time.LocalTime;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Rally {
    //ATRIBUTOS
    private static final org.apache.log4j.Logger LOG = Log.getLogger(Main.class);
    Random random=new Random();
    AtomicInteger pause;
    private ConcurrentHashMap<String,String> hmParking= new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,String> hmParkingPuente= new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,String> hmPuenteParking= new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,String> hmPuenteGasolinera= new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,String> hmGasolineraPuente= new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,String> hmPuenteIda= new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,String> hmPuenteVuelta= new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,Integer> hmGasolinera= new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,String> hmColaTramo= new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,String> hmSector= new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,String> hmCiudadParking= new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,String> hmParkingCiudad= new ConcurrentHashMap<>();
    // atributos puentes
    private static final int MAX_VEHICULOS = 10;

    private final Semaphore semaforo = new Semaphore(MAX_VEHICULOS);
    private final CyclicBarrier barrera = new CyclicBarrier(MAX_VEHICULOS, this::cambiarDireccion);
    private final Object lock = new Object();
    private boolean direccionActual = true; // true para una dirección, false para la otra
    private boolean direccionPreferente = true;
    //
    
    //atributos gasolinera
    private static final int NUM_SURTIDORES = 5;

    private int litrosDisponibles=500;
    private final Semaphore surtidores = new Semaphore(NUM_SURTIDORES, true);
    private final Lock lock2 = new ReentrantLock();
    private final Condition condition = lock2.newCondition();
    //atributos tramo
    private String clima="soleado";
    private static final int MAX_COCHES = 4;
    private final Semaphore semaforo2 = new Semaphore(MAX_COCHES, true);
    private final Queue<Coche> cola = new ConcurrentLinkedQueue<>();
    private final Map<String, Resultado> resultados = new ConcurrentHashMap<>();
    private final AtomicInteger cochesEnSectorTres = new AtomicInteger(0);
        
    //METODOS

    public Rally(AtomicInteger pause) {
        this.pause=pause;
    }
    
    
    public void llenarParking(Coche coche){
        try {
            if (coche.isNuevo()){
                int capacidadGasolina, gasolinaCoche;
                String tipoRuedas="";
                capacidadGasolina=random.nextInt(55)+75;
                gasolinaCoche=random.nextInt(25)+25;
                switch(random.nextInt(3)){
                    case 0 : tipoRuedas="soleado";
                    break;
                    case 1 : tipoRuedas="lluvia";
                    break;
                    case 2: tipoRuedas="nieve";
                    break;
                }
                coche.setCapacidadGasolina(capacidadGasolina);
                coche.setGasolinaCoche(gasolinaCoche);
                coche.setTiporuedas(tipoRuedas);
                logMsg(coche.getid()+coche.getInfo()+" es creado en el parking");
                hmParking.put(coche.getid(),coche.getInfo());
                coche.setNuevo(false);
            }
            else {        
                    hmParking.put(coche.getid(),coche.getInfo());
                    hmPuenteParking.remove(coche.getid());
                    logMsg(coche.getid()+coche.getInfo()+" esta descansando en el parking");
                    Thread.sleep((random.nextInt(6)+5)*1000);  
            }                     
            Thread.sleep((random.nextInt(3)+3)*1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Rally.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void liberarParking(boolean direccion,String id){
        if(direccion){
            hmParking.remove(id);
        }else{
            hmPuenteParking.remove(id);
        }
        
    }
    
    public void parkingCamion(Camion camion){
        try {
            logMsg(camion.getid()+camion.getInfo()+" esta descansando en el parking");
            if (camion.isDireccion()){
                hmCiudadParking.remove(camion.getid());
            }else{
                hmPuenteParking.remove(camion.getid());
            }
            hmParking.put(camion.getid(), camion.getInfo());
            
            Thread.sleep((random.nextInt(3)+3)*1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Rally.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void parkingPuente(boolean direccion, String id, String info){
        try {
            if (direccion){               
                hmParkingPuente.put(id, info);
                hmParking.remove(id);
            }
            else {
                hmPuenteParking.put(id, info);
                hmPuenteVuelta.remove(id);
            }        
            Thread.sleep((random.nextInt(3)+3)*1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Rally.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void puenteGasolinera(boolean direccion, String id, String info){
        try {
            if (direccion){
                hmPuenteIda.remove(id);
                hmPuenteGasolinera.put(id, info);
            }
            else{
                hmGasolinera.remove(id);
                hmGasolineraPuente.put(id, info);
                
            }
            
            Thread.sleep((random.nextInt(3)+3)*1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Rally.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void ciudadParking(Camion camion){
        try {
            if(camion.isDireccion()){
                hmCiudadParking.put(camion.getid(), camion.getInfo());
            }else{
                hmParking.remove(camion.getid());
                hmParkingCiudad.put(camion.getid(), camion.getInfo());
            }
            Thread.sleep((random.nextInt(6)+5)*1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Rally.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void ocuparPuente(boolean direccion, String id, String info){
        try{
            
            synchronized (lock) {
                while (direccion != direccionActual) {
                    lock.wait();
                }
            }

            semaforo.acquire();
            System.out.println(id+ " esta esperando para cruzar en direccion " + direccion);        
           
            barrera.await();
            
            if (direccion){
                hmParkingPuente.remove(id);
                hmPuenteIda.put(id, info);   
            }
            else {
                hmPuenteVuelta.put(id, info);
                hmGasolineraPuente.remove(id);
            }
            logMsg(id+info+" esta cruzando el puente en direccion "+direccion);
            System.out.println(id + " esta cruzando el puente en direccion " + direccion);
            Thread.sleep(5000);
        }catch (Exception ex) {
            Logger.getLogger(Rally.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }
    public void liberarPuente(boolean direccion, String id, String info){
        try{
                       
            System.out.println(id + " ha cruzado el puente en direccion " + direccion);

            semaforo.release();

            synchronized (lock) {
                if (semaforo.availablePermits() == MAX_VEHICULOS) {
                    direccionActual = !direccionActual;
                    lock.notifyAll();
                }
            }
            
        }catch (Exception ex) {
            Logger.getLogger(Rally.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }
    private void cambiarDireccion() {
        synchronized (lock) {
            if (semaforo.availablePermits() == MAX_VEHICULOS) {
                direccionActual = !direccionPreferente;
                direccionPreferente = !direccionPreferente;
                lock.notifyAll();
            }
        }
    }
    //metodos gasolinera
    public void repostarCoche(Coche coche) throws InterruptedException {
        surtidores.acquire();
        
        try {
            lock2.lock();
            while (litrosDisponibles <= 0) {
                condition.await();
            }
            
            int litrosRepostados = Math.min(coche.getCapacidadGasolina()-coche.getGasolinaCoche(), litrosDisponibles);
            litrosRepostados=random.nextInt(litrosRepostados)+1;
            litrosDisponibles -= litrosRepostados;
            
            System.out.println(coche.getid() + " está repostando " + litrosRepostados + " litros.");
            logMsg(coche.getid() + coche.getInfo()+" está repostando " + litrosRepostados + " litros.");
            hmPuenteGasolinera.remove(coche.getid());
            hmGasolinera.put(coche.getid(), litrosRepostados);
            //lock2.unlock();
            coche.setGasolinaCoche(litrosRepostados);
            Thread.sleep((random.nextInt(3)+3)*1000);
            
            /*//lock2.lock();
            System.out.println("Coche " + coche.getid() + " ha terminado de repostar.");
            condition.signalAll();
            //coche.setGasolinaCoche(litrosRepostados);
        } finally {
            lock2.unlock();
            surtidores.release();*/
        }catch(InterruptedException ex){
            Logger.getLogger(Rally.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void liberarGasolinera(Coche coche){
        System.out.println(coche.getid() + " ha terminado de repostar.");
        condition.signalAll();
        lock2.unlock();
        surtidores.release();
        hmGasolinera.remove(coche.getid());
    }

    public void repostarCamion(Camion camion) throws InterruptedException {
        surtidores.acquire();

        try {
            hmPuenteGasolinera.remove(camion.getid());
            System.out.println("Camión " + camion.getid() + " está llenando la gasolinera con " + camion.getGasolinaCamion() + " litros.");     
            lock2.lock();
            logMsg(camion.getid() + " está llenando la gasolinera con " + camion.getGasolinaCamion() + " litros.");
            Thread.sleep((random.nextInt(6)+5)*1000);
        }catch(InterruptedException ex){
            Logger.getLogger(Rally.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void liberarGasolineraCamion(Camion camion){
        litrosDisponibles += camion.getGasolinaCamion();
        System.out.println("Camión " + camion.getid() + " ha terminado de llenar la gasolinera.");
        condition.signalAll();
        lock2.unlock();
        surtidores.release();
    }
    //metodos tramo
    public void encolarTramo(Coche coche) throws InterruptedException{
        cola.add(coche);
        hmColaTramo.put(coche.getid(), coche.getInfo());
        logMsg(coche.getid() + coche.getInfo()+" está en la cola del tramo " + coche.getIdRally());
    }
    public void entrarTramo(Coche coche) throws InterruptedException {
        /*cola.add(coche);
        hmColaTramo.put(coche.getid(), coche.getInfo());
        logMsg("Coche " + coche.getid() + coche.getInfo()+" está en la cola del tramo " + coche.getIdRally());*/
        semaforo2.acquire();
        hmColaTramo.remove(coche.getid());
        
        try {
            System.out.println(coche.getid() + " está siendo verificado.");
            Thread.sleep(random.nextInt(1000, 3001));
            
            long tiempoTotal = 0;
            for (int sector = 1; sector <= 4; sector++) {
                while(pause.get()!=0){};
                
                hmSector.remove(coche.getid());
                hmSector.put(coche.getid(), String.valueOf(sector));
                logMsg(coche.getid() + coche.getInfo()+" accede al sector " + sector +" del tramo "+coche.getIdRally()+" (clima:"+clima+")");
                
                int tiempoSector = random.nextInt(4000, 10001);
                if (!coche.getTipoRuedas().equals(clima)) {
                    tiempoSector *= 3;
                }
                tiempoTotal += tiempoSector;
                System.out.println(coche.getid() + " está en el sector " + sector + " del tramo ");

                if (sector == 3) {
                    cochesEnSectorTres.incrementAndGet();
                }

                Thread.sleep(tiempoSector);

                if (sector == 3) {
                    cochesEnSectorTres.decrementAndGet();
                }
            }
            
            while(pause.get()!=0){};
            LocalTime horaFinalizacion = LocalTime.now();
            Resultado resultado = new Resultado(coche.getid(), horaFinalizacion, coche.getIdRally(), tiempoTotal);
            resultados.put(coche.getid(), resultado);
            System.out.println(coche.getid() + " ha completado el tramo " + coche.getIdRally() + " en " + tiempoTotal + " ms.");
        } finally {
            semaforo2.release();
            cola.poll();
            hmSector.remove(coche.getid());
        }
    }
    
    public void ciudad(Camion camion){
        try {
            camion.setCapacidadGasolina(1000);
            camion.setGasolinaCamion(random.nextInt(501)+500);
            System.out.println(camion.getid() + camion.getInfo()+" ha llenado su tanque en la ciudad");
            logMsg(camion.getid() + camion.getInfo()+" ha llenado su tanque en la ciudad");
            hmParkingCiudad.remove(camion.getid());
            
            Thread.sleep((random.nextInt(3)+3)*1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Rally.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Map<String, Resultado> getResultados() {
        return resultados;
    }
    
    public synchronized void logMsg(String msg){
        LOG.info(msg);
    }

}
