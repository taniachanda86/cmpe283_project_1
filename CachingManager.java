package cmpe283_project_1;

import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class CachingManager implements Runnable{
    
    @Override
    public void run() {
        try {
            ServiceInstance si = new ServiceInstance(new URL("https://130.65.132.106/sdk"), "administrator", "12!@qwQW", true);
            Folder rootFolder = si.getRootFolder();
            
            ManagedEntity[] hosts = new InventoryNavigator(rootFolder).searchManagedEntities("HostSystem");
            if(hosts==null || hosts.length==0) {
                System.out.println("Zero host!!!");   
                return;
            }
            
            AvailabilityManager.VM_POOL = new Hashtable<String,MyVM>();
            AvailabilityManager.VHOST_POOL = new Hashtable<String,MyVHost>();
            
            for (ManagedEntity hostSystem : hosts) {
                MyVHost myVHost = new MyVHost((HostSystem)hostSystem);
                
                // add to vhost map
                AvailabilityManager.VHOST_POOL.put(hostSystem.getName(),myVHost);
                for (VirtualMachine vm : ((HostSystem)hostSystem).getVms()) {
                    MyVM myVM = new MyVM(vm, myVHost);
                    
                    // add to vm map
                    AvailabilityManager.VM_POOL.put(vm.getName(),myVM);
                    myVM.takeSnapshot();
                }
            }

        }catch (Exception e) {
                e.printStackTrace();
        }
    }

}
