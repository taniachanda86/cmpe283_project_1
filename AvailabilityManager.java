package cmpe283_project_1;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import static java.util.concurrent.TimeUnit.*;

/**
 * Program entry point
 * 
 * @author shibaili
 *
 */
public class AvailabilityManager {
    // Global variable, contains all VM info
    // can be accessed by Availability.VMPOOL
    public static Map<String,MyVM> VM_POOL = new Hashtable<String,MyVM>();
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    
    
    public static void main(String[] args) {
        PingMonitor p = new PingMonitor();
        CachingManager m = new CachingManager();
        scheduler.scheduleAtFixedRate(p, 0, 10, SECONDS);
        scheduler.scheduleAtFixedRate(m, 0, 10, MINUTES);
        
 
    }

}
