package cmpe283_project_1;

import com.vmware.vim25.mo.HostSystem;

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
	
	public static void createSnapshot() {
		
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String host = "130.65.133.198";
		String host1 = "130.65.133.224";
		
		try {
			
	        System.out.println(pingByIP(host1));
	        
        } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }

	}

}
