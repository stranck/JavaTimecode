package ovh.stranck.javaTimecode;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

import ovh.stranck.javaTimecode.ltc.LTCGenerator;

public class App {
	public static void main(String[] args) throws LineUnavailableException {
		Mixer m = Utils.getMixer("*PnP*");
		System.out.println(m.getMixerInfo());
		LTCGenerator ltc = new LTCGenerator(m, 48000);
		TimecodePlayer tp = ltc.getTimecodePlayer();
		Timecode tc = ltc.getPacket();
		System.out.println("starting");
		ltc.start();
		//Wait.wait(1000);
		//tc.setHours(1);
		//Wait.wait(5000);
		tp.setSpeed(1);
		Wait.wait(5000);
		tp.setSpeed(2);
		Wait.wait(5000);
		tp.setSpeed(1);
		Wait.wait(5000);
		//System.out.println("stopping");
		//ltc.stop();
		//Wait.wait(10000);
		/*System.out.println("starting");
		ltc.start();
		Wait.wait(10000);
		System.out.println("stopping");
		ltc.stop();
		Wait.wait(10000);*/
	}
}
