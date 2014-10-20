package cmpe283_project_1;

import java.net.URL;

import com.vmware.vim25.VirtualMachineMovePriority;
import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.mo.ComputeResource;
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
    //private static ServiceInstance si;
    private VirtualMachine vm;
    private MyVHost myVHost;
    private int deathCount = 0;
    private boolean live = false;


    /**
     * Constructor for objects of class MyVM
     */
    public MyVM(VirtualMachine vm,MyVHost myVHost) {
        // initialise instance variables
        try {
            // your code here
            this.vm = vm;
            this.myVHost = myVHost;
            
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }
    
    
    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }
    
    public void deathCountIncrement() {
        deathCount++;
    }
    
    public void deathCountReset() {
        deathCount = 0;
    }
    
    public int getDeathCount() {
        return deathCount;
    }

    public String getVMIP() {
        return vm.getGuest().getIpAddress();
    }
    
    public boolean isPoweredOn() {
        VirtualMachineRuntimeInfo vmri = (VirtualMachineRuntimeInfo) vm.getRuntime();
        return vmri.getPowerState() == VirtualMachinePowerState.poweredOn;
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
            if (live) { 
                Task task = vm.createSnapshot_Task(vm.getName() + "_snapshot", "current", false, false);
                if (task.waitForMe()==Task.SUCCESS) {
                  System.out.println("Snapshot was created.");
                }
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
            Thread thread = new Thread(this);
            thread.start();
            
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    // migrate to a new host
    public void migrateToAnotherHost(HostSystem newHost) {
        try {
            ComputeResource cr = (ComputeResource) newHost.getParent();
            Task task = vm.migrateVM_Task(cr.getResourcePool(), newHost,
                    VirtualMachineMovePriority.highPriority,
                    VirtualMachinePowerState.poweredOff);
            System.out.println("Try to migrate " + vm.getName() + " to "
                    + newHost.getName());
            if (task.waitForTask() == task.SUCCESS) {
                System.out.println("\n Migrate virtual machine: "
                        + vm.getName() + " successfully!");
            } else {
                System.out.println("\n Migrate vm failed!");
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    // called by revertToCurrentSnapshot
    @Override
    public void run() {
        try {
            Task task = vm.revertToCurrentSnapshot_Task(myVHost.getHostSystem());
            if (task.waitForMe()==Task.SUCCESS) {
                System.out.println("Revert susccessful.");
            }else if (!myVHost.isLive()) {
                // revert failed, check for vhosts
                // look for available vhosts
                new Thread(new HostPingMonitor()).run(); // execute this in the same thread
                
                
            }else {
                System.out.println("Revert to snapshot failed, but ping to vhost works");
            }
            
        } catch(Exception e) {
            System.out.println(e);
        }    
    }

}
