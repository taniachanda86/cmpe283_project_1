package cmpe283_project_1;

import java.net.URL;

import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

import CONFIG.*;

/**
 * Write a description of class MyVM here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class MyVM implements Runnable{
    // instance variables
    private String vmName;
    private ServiceInstance si;
    private VirtualMachine vm;
    private HostSystem hostSystem;

    private String vHostIP;
    private String url = "130.65.132.106";
    private String userName = "administrator";
    private String password = "12!@qwQW";
    
    /**
     * Constructor for objects of class MyVM
     */
    public MyVM(VirtualMachine vm,String vmName,HostSystem hostSystem, String vHostIP) {
        // initialise instance variables
        try {
            // your code here
            this.vm = vm;
            this.vmName = vmName;
            this.vHostIP = vHostIP;
            this.hostSystem = hostSystem;
            
            si = new ServiceInstance(new URL(url), userName, password, true);
            
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    /**
     * Power On the Virtual Machine
     */
    public void powerOn() {
        try {
            // your code here
            VirtualMachineRuntimeInfo vmri = (VirtualMachineRuntimeInfo) vm.getRuntime();
            if (vmri.getPowerState() == VirtualMachinePowerState.poweredOff) {
                Task task = vm.powerOnVM_Task(null);
                task.waitForMe();
                System.out.println("vm:" + vm.getName() + " powered off.");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Power Off the Virtual Machine
     */
    public void powerOff() {
        try {
            // your code here
            VirtualMachineRuntimeInfo vmri = (VirtualMachineRuntimeInfo) vm.getRuntime();
            if (vmri.getPowerState() == VirtualMachinePowerState.poweredOn) {
                Task task = vm.powerOffVM_Task();
                task.waitForMe();
                System.out.println("vm:" + vm.getName() + " powered off.");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * take a snapshot for VM
     */

    public void takeSnapshot() {
        try {
            // your code here
            Task task = vm.createSnapshot_Task(vmName + "_snapshot", "current", false, false);
            if (task.waitForMe()==Task.SUCCESS) {
              System.out.println("Snapshot was created.");
            }
            
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * revert to current snapshot
     */

    public void revertToCurrentSnapshot() {
        try {
            // your code here
            Thread thread = new Thread(this);
            thread.start();
            
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public void run() {
        try {
            // or null?
            Task task = vm.revertToCurrentSnapshot_Task(hostSystem);
            if (task.waitForMe()==Task.SUCCESS) {
                System.out.println("Revert susccessful.");
            }
            
        } catch(Exception e) {
            System.out.println(e);
        }    
    }

}
