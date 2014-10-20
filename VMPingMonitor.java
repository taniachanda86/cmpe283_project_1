package cmpe283_project_1;

import java.util.List;
import java.util.Set;


public class VMPingMonitor implements Runnable{
    public boolean heartbeatChecking (MyVM myVM) throws Exception {
        String cmd = "";
        if (System.getProperty("os.name").startsWith("Windows")) {
            // For Windows
            cmd = "ping -n 1 " + myVM.getVMIP();
        }else {
            // For Linux and OSX
            cmd = "ping -c 1 " + myVM.getVMIP();
        }
        Process process = Runtime.getRuntime().exec(cmd);
        process.waitFor();

        return process.exitValue() == 0;
    }
    
    @Override
    public void run() {
        try{
            // check all vms
            Set<String> vmNames = AvailabilityManager.VM_POOL.keySet();
            
            for (String vmName : vmNames) {
                
                if (heartbeatChecking(AvailabilityManager.VM_POOL.get(vmName))) {
                    AvailabilityManager.VM_POOL.get(vmName).setLive(true);
                }else if (AvailabilityManager.VM_POOL.get(vmName).isPoweredOn()){
                    
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
