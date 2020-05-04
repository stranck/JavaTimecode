package ovh.stranck.javaTimecode.ltc;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

public class LTCGenerator implements Runnable {
	private volatile boolean autoIncrement = true;
    private volatile LTCPacket packet;
	
    private SourceDataLine dataLine;
    private Thread runThread;
	private Mixer mixer;
	private int sampleRate;
    
	public LTCGenerator(Mixer output, int sampleRate) throws LineUnavailableException{
		this.mixer = output;
		this.sampleRate = sampleRate;
		packet = new LTCPacket();
		
		AudioFormat format = new AudioFormat(sampleRate, 8, 1, true, true);
        SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        dataLine = (SourceDataLine) mixer.getLine(info);
        dataLine.open();
        dataLine.start();
	}

	public void run() {
		byte[] content;
		long ms = System.currentTimeMillis() + (long)packet.getFrameWindow(), currentMs;
		while(!Thread.interrupted()){
			content = packet.asAudioSample(sampleRate);
			dataLine.write(content, 0, content.length);
			
			currentMs = System.currentTimeMillis();
			if(autoIncrement && currentMs >= ms){
				System.out.println(packet/* + " " + packet.asBitsString()*/);
				packet.nextFrame();
				ms = currentMs + (long)packet.getFrameWindow();
			}
		}
		System.out.println("Exit");
	}
	
	public void start(){
        runThread = new Thread(this);
        runThread.start();
	}
	public void stop(){
		runThread.interrupt();
	}
	public void destroy(){
		stop();
		dataLine.stop();
        dataLine.close();
        mixer.close();
	}
	
	public LTCGenerator setPacket(LTCPacket packet){
		this.packet = packet;
		return this;
	}
	public LTCPacket getPacket(){
		return packet;
	}
	public LTCGenerator setAutoIncrement(boolean autoIncrement){
		this.autoIncrement = autoIncrement;
		return this;
	}
	public boolean isAutoIncrementing(){
		return autoIncrement;
	}
	
}
