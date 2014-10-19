package cmpe283_project_1;

import java.util.HashMap;
import java.util.Map;

/**
 * Program entry point
 * 
 * @author shibaili
 *
 */
public class AvailabilityManager {
    // Global variable, contains all VM info
    // can be accessed by Availability.VMPOOL
    public static Map<String,MyVM> VM_POOL = new HashMap<String,MyVM>();

    public static void main(String[] args) {
        
        Thread pingMonitorThread = new Thread(new PingMonitor());
        Thread cachingManagerThread = new Thread(new CachingManager());
        
        pingMonitorThread.start();
        cachingManagerThread.start();
    }

}
