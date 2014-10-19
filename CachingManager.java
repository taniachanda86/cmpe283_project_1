package cmpe283_project_1;

import com.vmware.vim25.mo.VirtualMachine;

public class CachingManager implements Runnable{

    private void cachingVMImage(VirtualMachine vm) {
        
    }
    
    @Override
    public void run() {
        // while loop here
        while (true) {
            try {
                
                cachingVMImage(null);
                
                
                
                Thread.sleep(1000 * 60 * 10); // 10 minutes
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
