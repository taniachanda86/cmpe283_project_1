package cmpe283_project_1;

import java.util.Set;

import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.VirtualMachine;

public class HostPingMonitor implements Runnable{
    /*
    public boolean heartbeatChecking (MyVHost myVHost) throws Exception {
        String cmd = "";
        if (System.getProperty("os.name").startsWith("Windows")) {
            // For Windows
            cmd = "ping -n 1 " + myVHost.getHostIP();
        }else {
            // For Linux and OSX
            cmd = "ping -c 1 " + myVHost.getHostIP();
        }
        Process process = Runtime.getRuntime().exec(cmd);
        process.waitFor();

        return process.exitValue() == 0;
    }
    */
    
    @Override
    public void run() {
        try{
            // check all vhosts
            Set<String> vhostNames = AvailabilityManager.VHOST_POOL.keySet();
            boolean noHost = true;
            
            for (String vhostName : vhostNames) {
                
                if (AvailabilityManager.VHOST_POOL.get(vhostName).isLive()) {                    
                    // migrate
                    for (VirtualMachine vm : AvailabilityManager.VHOST_POOL.get(vhostName).getHostSystem().getVms()) {
                        MyVM myVM = AvailabilityManager.VM_POOL.get(vm.getName());
                        myVM.migrateToAnotherHost(AvailabilityManager.VHOST_POOL.get(vhostName).getHostSystem());
                        // add to vm map
                        AvailabilityManager.VM_POOL.put(vm.getName(),myVM);
                        myVM.takeSnapshot();
                        noHost = false;
                    }
                    
                }
            }
            
            if (noHost) {
                // add new host
                
            }
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
}
