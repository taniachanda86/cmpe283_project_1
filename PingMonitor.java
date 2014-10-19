package cmpe283_project_1;

import com.vmware.vim25.mo.VirtualMachine;

public class PingMonitor implements Runnable{

    public boolean heartbeatChecking (VirtualMachine vm) {
        
        return false;
    }
    
    @Override
    public void run() {
        // while loop here
        try{
            while (true) {
                Thread.sleep(2000);
                
                heartbeatChecking(null);
                
                
                
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

}
