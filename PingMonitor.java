package cmpe283_project_1;

import java.util.List;
import java.util.Set;

import com.vmware.vim25.mo.VirtualMachine;

public class PingMonitor implements Runnable{
    private List<String> list;
    
    public boolean heartbeatChecking (MyVM myVM) {
        
        return false;
    }
    
    @Override
    public void run() {
        // while loop here
        try{
            Set<String> vmNames = AvailabilityManager.VM_POOL.keySet();
            
            for (String vmName : vmNames) {
                if (heartbeatChecking(AvailabilityManager.VM_POOL.get(vmName))) {
                    AvailabilityManager.VM_POOL.get(vmName).setLive(true);
                }else {
                    
                    AvailabilityManager.VM_POOL.get(vmName).deathCountIncrement();
                    if (AvailabilityManager.VM_POOL.get(vmName).getDeathCount() > 6) {
                        AvailabilityManager.VM_POOL.get(vmName).setLive(false);
                    }
                }
            }
            
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

}
