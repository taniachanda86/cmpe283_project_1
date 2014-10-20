package cmpe283_project_1;

import com.vmware.vim25.mo.HostSystem;

public class MyVHost {
    private HostSystem hostSystem;

    public MyVHost(HostSystem hostSystem) {
        this.hostSystem = hostSystem;
    }
    
    public HostSystem getHostSystem() {
        return hostSystem;
    }

    public void setHostSystem(HostSystem hostSystem) {
        this.hostSystem = hostSystem;
    }
    
    public String getHostIP() {
        return hostSystem.getName();
    }
    
    // To determine if one host is live or not, only ping it once
    // But for VM, it takes 6 contiguous pings to say one VM is failed
    public boolean isLive() {
        Process process = null;
        try {
            String cmd = "";
            if (System.getProperty("os.name").startsWith("Windows")) {
                // For Windows
                cmd = "ping -n 1 " + hostSystem.getName();
            }else {
                // For Linux and OSX
                cmd = "ping -c 1 " + hostSystem.getName();
            }
            process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
        
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        return process.exitValue() == 0;
    }
    
}
