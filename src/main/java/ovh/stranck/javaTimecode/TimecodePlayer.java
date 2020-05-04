package ovh.stranck.javaTimecode;

public class TimecodePlayer {
	private Timecode t;
	private boolean playing = true;
	private double speed;
	private int offset;
	private int zFrame;
	private long zMs;
	
	public TimecodePlayer(Timecode t){
		this(t, 0);
	}
	public TimecodePlayer(Timecode t, double speed){
		this.t = t;
		setSpeed(speed);
	}
	
	public synchronized TimecodePlayer updateTimecodeTime(){
		if(playing){
			//System.out.println((System.currentTimeMillis() - zMs) + " ");
			t.setTimeWithoutUpdatingStartPoint((int) ((System.currentTimeMillis() - zMs)
				* speed * t.getFramerate().getIntegerFramerate() / 1000 + zFrame + offset));
		}
		return this;
	}
	
	public synchronized TimecodePlayer updateStartPoint(){
		zFrame = t.getTotalFrames();
		zMs = System.currentTimeMillis();
		return this;
	}
	
	public synchronized TimecodePlayer play(){
		updateStartPoint();
		playing = true;
		return this;
	}
	public synchronized TimecodePlayer pause(){
		playing = false;
		return this;
	}
	
	public synchronized TimecodePlayer setSpeed(double speed){
		this.speed = speed;
		updateStartPoint();
		return this;
	}
	public double getSpeed(){
		return speed;
	}
	public TimecodePlayer setOffset(int offset){
		this.offset = offset;
		return this;
	}
	public int getOffset(){
		return offset;
	}
	/*public synchronized TimecodePlayer setTimecode(Timecode t){
		this.t = t;
		return this;
	}*/
	public Timecode getTimecode(){
		return t;
	}
}
