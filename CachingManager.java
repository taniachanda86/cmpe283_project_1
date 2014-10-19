package cmpe283_project_1;

import java.net.URL;

import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.VirtualMachineSummary;
import com.vmware.vim25.mo.Datacenter;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

public class CachingManager implements Runnable{

    private void cachingVMImage(VirtualMachine vm) {
        
    }
    
    @Override
    public void run() {
        try {
            ServiceInstance si = new ServiceInstance(new URL("https://130.65.132.106/sdk"), "administrator", "12!@qwQW", true);
            Folder rootFolder = si.getRootFolder();
            String vHostName = null;
            ManagedEntity[] hosts = new InventoryNavigator(rootFolder).searchManagedEntities("HostSystem");
            if(hosts==null || hosts.length==0) {
                System.out.println("Did not find vHost");   
                return;
            }
            
            for (HostSystem hostSystem : (HostSystem[])hosts) {
                
                for (VirtualMachine vm : hostSystem.getVms()) {
                    MyVM myVM = new MyVM(vm, hostSystem);
                    AvailabilityManager.VM_POOL.put(vm.getName(),myVM);
                    
                }
            }
            

        } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
    }

}
