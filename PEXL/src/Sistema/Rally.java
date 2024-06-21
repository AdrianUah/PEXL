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
import java.util.concurrent.Semaphore;
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
    private ConcurrentHashMap<Integer,String> hmGasolinera= new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,String> hmColaTramo= new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,Integer> hmSector= new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,String> hmCiudadParking= new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,String> hmParkingCiudad= new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,String> hmTramoPuente= new ConcurrentHashMap<>();
    // atributos puentes
    private int maxVehiculos = 10;
    private final Semaphore semaforo1 = new Semaphore(maxVehiculos);
    private final CyclicBarrier barrera = new CyclicBarrier(maxVehiculos, this::cambiarDireccion);
    private final Object lock1 = new Object();
    private boolean direccionActual = true;
    //
    
    //atributos gasolinera 
    private final Semaphore surtidores = new Semaphore(5, true);
    private final Lock lock2 = new ReentrantLock();
    private final Condition condition = lock2.newCondition();
    private boolean[] arraySurtidores={false,false,false,false,false};
    private Integer[] arraySurtidoresCombustible={500,500,500,500,500};
    private int nsurtidoreslibres=5;
    //
    
    //atributos tramo
    private String clima="soleado";
    private int maxCoches = 4;
    private final Semaphore semaforo2 = new Semaphore(maxCoches, true);
    private final Queue<Coche> cola = new ConcurrentLinkedQueue<>();
    private final Map<String, Resultado> resultados = new ConcurrentHashMap<>();
    private final AtomicInteger cochesEnSectorTres = new AtomicInteger(0);
        
    //METODOS

    public Integer[] getArraySurtidoresCombustible() {
        return arraySurtidoresCombustible;
    }

    public Rally(AtomicInteger pause) {
        this.pause=pause;
    }

    public ConcurrentHashMap<String, String> getHmParking() {
        return hmParking;
    }

    public ConcurrentHashMap<String, String> getHmParkingPuente() {
        return hmParkingPuente;
    }

    public ConcurrentHashMap<String, String> getHmPuenteParking() {
        return hmPuenteParking;
    }

    public ConcurrentHashMap<String, String> getHmPuenteGasolinera() {
        return hmPuenteGasolinera;
    }

    public ConcurrentHashMap<String, String> getHmGasolineraPuente() {
        return hmGasolineraPuente;
    }

    public ConcurrentHashMap<String, String> getHmPuenteIda() {
        return hmPuenteIda;
    }

    public ConcurrentHashMap<String, String> getHmPuenteVuelta() {
        return hmPuenteVuelta;
    }

    public ConcurrentHashMap<Integer, String> getHmGasolinera() {
        return hmGasolinera;
    }

    public ConcurrentHashMap<String, String> getHmColaTramo() {
        return hmColaTramo;
    }

    public ConcurrentHashMap<String, Integer> getHmSector() {
        return hmSector;
    }

    public ConcurrentHashMap<String, String> getHmCiudadParking() {
        return hmCiudadParking;
    }

    public ConcurrentHashMap<String, String> getHmParkingCiudad() {
        return hmParkingCiudad;
    }

    public ConcurrentHashMap<String, String> getHmTramoPuente() {
        return hmTramoPuente;
    }
    
    
    
    public void llenarParking(Coche coche){
        try {
            if (coche.isNuevo()){
                int capacidadGasolina, gasolinaCoche;
                String tipoRuedas="";
                capacidadGasolina=random.nextInt(55)+75;
                gasolinaCoche=random.nextInt(25)+25;
                switch(random.nextInt(3)){
                    case 0 -> tipoRuedas="soleado";
                    case 1 -> tipoRuedas="lluvia";
                    case 2 -> tipoRuedas="nieve";
                    default -> {
                    }
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
            }        
            Thread.sleep((random.nextInt(3)+3)*1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Rally.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void puenteGasolinera(boolean direccion, String id, String info){
        try {
            if (direccion){
                hmPuenteGasolinera.put(id, info);
            }
            else{  
                hmGasolineraPuente.put(id, info); 
            }           
            Thread.sleep((random.nextInt(3)+3)*1000);
            while(pause.get()!=0){}
            
            
        } catch (InterruptedException ex) {
            Logger.getLogger(Rally.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void tramoPuente(Coche coche){
        try {
            hmTramoPuente.put(coche.getid(), coche.getInfo());                 
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
    
    public void cruzarPuente(boolean direccion, String id, String info) throws InterruptedException, BrokenBarrierException {
        synchronized (lock1) {
            while (direccion != direccionActual) {
                lock1.wait();
            }
        }   
        semaforo1.acquire();
        while(pause.get()!=0){}
        barrera.await();
        while(pause.get()!=0){}
        if (direccion){
            hmParkingPuente.remove(id);
            hmPuenteIda.put(id, info);   
        }else{
            hmPuenteVuelta.put(id, info);
            hmGasolineraPuente.remove(id);
            hmTramoPuente.remove(id);
        }
        logMsg(id+info+" esta cruzando el puente en direccion "+direccion);
        Thread.sleep(5000);
        while(pause.get()!=0){}
        semaforo1.release();

        synchronized (lock1) {
            if (semaforo1.availablePermits() == maxVehiculos) {
                direccionActual = !direccionActual;
                lock1.notifyAll();
            }
        }
        
        if (direccion){
            hmPuenteIda.remove(id);
        }else{
            hmPuenteVuelta.remove(id);
        } 
    }
    
    private void cambiarDireccion() {
        synchronized (lock1) {
            if (semaforo1.availablePermits() == maxVehiculos) {
                direccionActual = !direccionActual;
                lock1.notifyAll();
            }
        }
    }
    //metodos gasolinera
    public void repostarCoche(Coche coche) throws InterruptedException {
        
        surtidores.acquire();
        while(pause.get()!=0){}
        
        try {
            lock2.lock();
            while(pause.get()!=0){}
            hmPuenteGasolinera.remove(coche.getid());
            nsurtidoreslibres=nsurtidoreslibres-1;
            int i=0;
            while(arraySurtidores[i]==true){
                i++;
            }
            arraySurtidores[i]=true;        
            int litrosRepostados = Math.min(coche.getCapacidadGasolina()-coche.getGasolinaCoche(), arraySurtidoresCombustible[i]);
            litrosRepostados=random.nextInt(litrosRepostados)+1;          
            logMsg(coche.getid() + coche.getInfo()+" está repostando " + litrosRepostados + " litros en el surtidor "+i+1);          
            hmGasolinera.put(i, coche.getid());          
            lock2.unlock();     
            Thread.sleep((random.nextInt(3)+3)*1000);
            while(pause.get()!=0){}
            lock2.lock();
            while(pause.get()!=0){}
            arraySurtidoresCombustible[i] -= litrosRepostados;
            coche.setGasolinaCoche(coche.getGasolinaCoche()+litrosRepostados);
            arraySurtidores[i]=false;
            nsurtidoreslibres++;
            hmGasolinera.remove(i);
            condition.signalAll();
        }finally{
            lock2.unlock();
            surtidores.release();
        }
    }
    

    public void repostarCamion(Camion camion) throws InterruptedException {
        surtidores.acquire();
        while(pause.get()!=0){}
        
        try {
            nsurtidoreslibres=nsurtidoreslibres-1;
            int i=0;
            while(arraySurtidores[i]==true){
                i++;
            }
            arraySurtidores[i]=true;     
            hmPuenteGasolinera.remove(camion.getid());
            hmGasolinera.put(i,camion.getid());
            logMsg(camion.getid() + " está llenando la gasolinera con " + camion.getGasolinaCamion() + " litros.");
            Thread.sleep((random.nextInt(6)+5)*1000);
            while(pause.get()!=0){}
            lock2.lock();
            arraySurtidoresCombustible[i] += camion.getGasolinaCamion();
            camion.setGasolinaCamion(0);
            arraySurtidores[i]=false;
            nsurtidoreslibres++;
            condition.signalAll();
            lock2.unlock();
            hmGasolinera.remove(i);
        }finally{
            surtidores.release();
        }
    }
    //metodos tramo
    public void entrarTramo(Coche coche) throws InterruptedException {
        cola.add(coche);
        hmPuenteGasolinera.remove(coche.getid());
        hmColaTramo.put(coche.getid(), coche.getInfo());
        logMsg(coche.getid() + coche.getInfo()+" está en la cola del tramo " + coche.getIdRally());
        
        semaforo2.acquire();
        while(pause.get()!=0){}
        
        hmColaTramo.remove(coche.getid());
        try {
            Thread.sleep(random.nextInt(1000, 3001));
            while(pause.get()!=0){}
            long tiempoTotal = 0;
            String infoCoche=coche.getid()+coche.getInfo();
            for (int sector = 1; sector <= 4; sector++) {
                while(pause.get()!=0){}
                
                hmSector.remove(infoCoche);
                hmSector.put(infoCoche, sector);
                logMsg(coche.getid() + coche.getInfo()+" accede al sector " + sector +" del tramo "+coche.getIdRally()+" (clima:"+clima+")");
                
                int tiempoSector = random.nextInt(4000, 10001);
                if (!coche.getTipoRuedas().equals(clima)) {
                    tiempoSector *= 3;
                }
                tiempoTotal += tiempoSector;
                if (sector == 3) {
                    cochesEnSectorTres.incrementAndGet();
                }
                Thread.sleep(tiempoSector);
                while(pause.get()!=0){}
                if (sector == 3) {
                    cochesEnSectorTres.decrementAndGet();
                }
            }
            hmSector.remove(infoCoche);
            while(pause.get()!=0){}
            LocalTime horaFinalizacion = LocalTime.now();
            Resultado resultado = new Resultado(coche.getid(), horaFinalizacion, coche.getIdRally(), tiempoTotal);
            resultados.put(coche.getid(), resultado);
        } finally {
            semaforo2.release();
            cola.poll();
        }
    }
    
    public void ciudad(Camion camion){
        try {
            camion.setCapacidadGasolina(1000);
            camion.setGasolinaCamion(random.nextInt(501)+500);
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
    
    public String recorrerHashMap(ConcurrentHashMap<String, String> hm) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : hm.entrySet()) {
            stringBuilder.append(entry.getKey()).append(entry.getValue()).append(", ");
        }
        return stringBuilder.toString();
    }
    
    public String recorrerHashMapSector(ConcurrentHashMap<String, Integer> hm,int n) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Integer> entry : hm.entrySet()) {
            if (entry.getValue().equals(n)) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(entry.getKey());
            }
        }
        return stringBuilder.toString();
    }

}
