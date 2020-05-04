package ovh.stranck.javaTimecode.ltc;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

import ovh.stranck.javaTimecode.TimecodePlayer;
import ovh.stranck.javaTimecode.Wait;

public class LTCGenerator implements Runnable {
    private volatile float bufferCapacity = 0.5f;
	private volatile TimecodePlayer tcPlayer;
    private volatile LTCPacket packet;
	
    private SourceDataLine dataLine;
    private Thread runThread;
	private Mixer mixer;
	private int sampleRate;
    
	public LTCGenerator(Mixer output, int sampleRate) throws LineUnavailableException{
		this.mixer = output;
		this.sampleRate = sampleRate;
		setPacket(new LTCPacket());
		
		AudioFormat format = new AudioFormat(sampleRate, 8, 1, true, true);
        SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        dataLine = (SourceDataLine) mixer.getLine(info);
        dataLine.open();
        dataLine.start();
	}

	public void run() {
		byte[] content;
		tcPlayer.updateStartPoint();
		while(!Thread.interrupted()){
			if(dataLine.getBufferSize() * bufferCapacity < dataLine.available()){
				tcPlayer.updateTimecodeTime();
				content = packet.asAudioSample(sampleRate);
				dataLine.write(content, 0, content.length);
				System.out.println(packet);
			}
			Wait.wait(1);
		}
		dataLine.drain();
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
	
	public LTCGenerator setBufferDimension(float dimension){
		bufferCapacity = dimension;
		return this;
	}
	public float getCustomBufferDimension(){
		return bufferCapacity;
	}
	public int getRealBufferDimension(){
		return dataLine.getBufferSize();
	}
	public LTCGenerator setPacket(LTCPacket packet){
		this.packet = packet;
		tcPlayer = packet.getTimecodePlayer();
		return this;
	}
	public LTCPacket getPacket(){
		return packet;
	}
	public TimecodePlayer getTimecodePlayer(){
		return tcPlayer;
	}
}
