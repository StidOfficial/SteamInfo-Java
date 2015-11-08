
public class Main {

	public static void main(String[] args) {
		try {
			Steam SteamInfo = new Steam();
			System.out.println("Steam Directory : " + SteamInfo.SteamDirectory);
			for (String string : SteamInfo.SteamAppDirectory) {
				System.out.println("Steam Library Directory : " + string);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
