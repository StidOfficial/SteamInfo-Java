import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Steam {
	
	public static int OS_UNIX		= 0;
	public static int OS_WINDOWS	= 1;
	
	public int getOperatingSystem;
	private final String OSName = System.getProperty("os.name").toLowerCase();
	
	public String SteamDirectory;
	
	private List<String> SteamAppArray = new ArrayList<String>();
	public String[] SteamAppDirectory;
	
	public Steam() {
		System.out.println("[Steam] Initialize...");
		
		switch(OSName.substring(0, 3)) {
			case "win":
				this.getOperatingSystem = OS_WINDOWS;
				String Directory = this.getRegeditKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\Wow6432Node\\Valve\\Steam", "InstallPath");
				SteamDirectory = (new File(Directory).exists()) ? Directory : null;
				
				System.out.println("[Steam] Steam directory find !");
				
				break;
			case "nix":
				this.getOperatingSystem = OS_UNIX;
				break;
			default:
				System.err.println("Operating System is not supported for Steam !");
				break;
		}
		
		String ConfigFilePath = SteamDirectory + File.separator + "config" + File.separator + "config.vdf";
		if(new File(ConfigFilePath).exists()) {
			System.out.println("[Steam] Steam configuration file find !");
			try {
				@SuppressWarnings("resource")
				BufferedReader BufferRead = new BufferedReader(new FileReader(ConfigFilePath));
				
				String ReadLine;
				while((ReadLine = BufferRead.readLine()) != null) {
					if(ReadLine.trim().startsWith("\"BaseInstallFolder_")) {
						this.addSteamAppDirectory(ReadLine.trim().split("\t")[2].replaceAll("\\\\+", "\\\\").replaceAll("\"", ""));
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void addSteamAppDirectory(String DirectoryPath) {
		SteamAppArray.add(DirectoryPath);
		SteamAppDirectory = new String[SteamAppArray.size()];
		SteamAppArray.toArray(SteamAppDirectory);
		
	}
	
	private String getRegeditKey(String RegeditKey, String Key) {
		String ReturnKey = null;
		try {
			Process CmdProcess = Runtime.getRuntime().exec("reg query " + RegeditKey + " /v " + Key);
			BufferedReader CmdReader = new BufferedReader(new InputStreamReader(CmdProcess.getInputStream()));
			
			String CmdReadLine;
			while((CmdReadLine = CmdReader.readLine().trim()) != null) {
				if(CmdReadLine.startsWith(Key)) {
					ReturnKey = CmdReadLine.split("\\s\\s\\s\\s")[2];
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ReturnKey;
	}
}
