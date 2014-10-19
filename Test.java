package cmpe283_project_1;

import java.net.URL;

import com.vmware.vim25.HostVMotionCompatibility;
import com.vmware.vim25.TaskInfo;
import com.vmware.vim25.VirtualMachineMovePriority;
import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.mo.ComputeResource;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

public class Test {

	public static boolean pingByIP(String ip) throws Exception {
		String cmd = "";
		if (System.getProperty("os.name").startsWith("Windows")) {
			// For Windows
			cmd = "ping -n 1 " + ip;
		}else {
			// For Linux and OSX
			cmd = "ping -c 1 " + ip;
		}
		Process process = Runtime.getRuntime().exec(cmd);
		process.waitFor();

		return process.exitValue() == 0;
	}
	
	public static void migrateFromDeadHost() throws Exception{
		String vmname = "T06-VM02-Ubuntu1";
		String newHostName = "130.65.132.184"; // vHost name

		ServiceInstance si = new ServiceInstance(new URL(
		        "https://130.65.132.106/sdk"), "administrator", "12!@qwQW",
		        true);
		Folder rootFolder = si.getRootFolder();
		VirtualMachine vm = (VirtualMachine) new InventoryNavigator(rootFolder)
		        .searchManagedEntity("VirtualMachine", vmname);

		HostSystem newHost = (HostSystem) new InventoryNavigator(rootFolder)
		        .searchManagedEntity("HostSystem", newHostName);

		ComputeResource cr = (ComputeResource) newHost.getParent();

		String[] checks = new String[] { "cpu", "software" };
		HostVMotionCompatibility[] vmcs = si.queryVMotionCompatibility(vm,
		        new HostSystem[] { newHost }, checks);

		String[] comps = vmcs[0].getCompatibility();
		if (checks.length != comps.length) {
			System.out.println("CPU/software NOT compatible. Exit.");
			si.getServerConnection().logout();
			return;
		}

		Task task = vm.migrateVM_Task(cr.getResourcePool(), newHost,
		        VirtualMachineMovePriority.highPriority,
		        VirtualMachinePowerState.poweredOff);

		String status = task.waitForMe();

		if (status == Task.SUCCESS) {
			System.out.println("VMotioned!");
		} else {
			System.out.println("VMotion failed!");
			TaskInfo info = task.getTaskInfo();
			System.out.println(info.getError().getFault());
		}
		si.getServerConnection().logout();

	}
		

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String host = "130.65.133.198";
		String host1 = "130.65.133.224";
		
		try {
			migrateFromDeadHost();
	        // System.out.println(pingByIP(host1));
	        
        } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }

	}

}
