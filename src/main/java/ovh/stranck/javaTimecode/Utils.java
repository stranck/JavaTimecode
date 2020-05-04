package ovh.stranck.javaTimecode;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

public class Utils {
	public static boolean like(String original, String stringToMatch){
		return original.matches(stringToMatch.replace("*", ".*"));
	}
	public static boolean likeIgnoreCase(String original, String stringToMatch){
		return like(original.toUpperCase(), stringToMatch.toUpperCase());
	}
	
	public static int repeatBytes(byte data[], int index, byte value, int repeatBytes){
		for(int i = 0; i < repeatBytes; i++)
			data[index++] = value;
		return index;
	}

	public static int bcdSingle(boolean[] b, int index, int value) {
		while (value > 0 && index < b.length) {
			b[index++] = value % 2 == 1;
			value /= 2;
		}
		return index;
	}
	public static boolean[] bcd(int value, int numBits) {
		boolean[] result = new boolean[numBits];
		bcdSingle(result, 0, value % 10);
		bcdSingle(result, 4, value / 10);
		return result;
	}
	
	public static void printBitData(boolean[] data){
		for(int i = 0; i < data.length; i++)
			System.out.print((data[i] ? '1' : '0') + (i % 4 == 3 ? " " : ""));
		System.out.println();
	}

	public static int addAllBits(boolean[] dst, int index, boolean[] src) {
		for (int i = 0; i < src.length; i++)
			dst[index++] = src[i];
		return index;
	}

	public static boolean[] convertBitString(String s) {
		boolean[] ret = new boolean[s.length()];
		for (int i = 0; i < s.length(); i++)
			ret[i] = s.charAt(i) == '1';
		return ret;
	}
	
	public static boolean getBitParity(boolean data[], boolean searchForBit, boolean odd){
		int i = 0;
		for(boolean v : data)
			if(v == searchForBit)
				i++;
		return i % 2 == (odd ? 1 : 0);
	}
	
	public static Mixer getMixer(String name){
		Mixer mixer = null;
		Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
		if(name != null && mixerInfos != null)
			for(int i = 0; i < mixerInfos.length; i++){
				Mixer.Info m = mixerInfos[i];
				if(likeIgnoreCase(m.getName(), name)){
					mixer = AudioSystem.getMixer(mixerInfos[i]);
					break;
				}
			}
		return mixer;
	}
}
