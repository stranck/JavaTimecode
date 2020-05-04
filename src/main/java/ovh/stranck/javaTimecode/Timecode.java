package ovh.stranck.javaTimecode;

public abstract class Timecode {
	protected int hours;
	protected int mins;
	protected int secs;
	protected int frames;
	protected Framerates framerate;
	
	public abstract byte[] asByteArray();
	public abstract String asBitsString();
	
	public Timecode(int hour, int min, int sec, int frame, Framerates framerate){
		this.framerate = framerate;
		setTime(hour, min, sec, frame);
	}
	
	protected int getIntegerFramerate(){
		return (int) Math.ceil(framerate.getFps());
	}
	
	protected final void sanityzeTime(){
		int fps = getIntegerFramerate();
		secs += frames / fps;
		frames %= fps;
		if(frames < 0) {
			frames = fps - frames;
			secs--;
		}
		mins += secs / 60;
		secs %= 60;
		if(secs < 0){
			secs = 60 - secs;
			mins--;
		}
		hours += mins / 60;
		mins %= 60;
		if(mins < 0){
			mins = 60 - mins;
			hours--;
		}
		hours %= 24;
		if(hours < 0){
			hours = 24 - hours;
		}
	}
	
	public int getTotalFrames(){
		int n = hours;
		n = 60 * n + mins;
		n = 60 * n + secs;
		n = getIntegerFramerate() * n + frames;
		return n;
	}
	public Timecode setTime(int totalFrames){
		hours = mins = secs = 0;
		frames = totalFrames;
		sanityzeTime();
		return this;
	}
	public Timecode setTime(int hours, int mins, int secs, int frames){
		this.hours = hours;
		this.mins = mins;
		this.secs = secs;
		this.frames = frames;
		sanityzeTime();
		return this;
	}
	
	public int getHours() {
		return hours;
	}
	public Timecode setHours(int hours) {
		this.hours = hours;
		sanityzeTime();
		return this;
	}
	public Timecode nextHour(){
		hours++;
		sanityzeTime();
		return this;
	}
	public Timecode previousHour(){
		hours--;
		sanityzeTime();
		return this;
	}
	public int getMins() {
		return mins;
	}
	public Timecode setMins(int mins) {
		this.mins = mins;
		sanityzeTime();
		return this;
	}
	public Timecode nextMin(){
		mins++;
		sanityzeTime();
		return this;
	}
	public Timecode previousMin(){
		mins--;
		sanityzeTime();
		return this;
	}
	public int getSecs() {
		return secs;
	}
	public Timecode setSecs(int secs) {
		this.secs = secs;
		sanityzeTime();
		return this;
	}
	public Timecode nextSec(){
		secs++;
		sanityzeTime();
		return this;
	}
	public Timecode previousSec(){
		secs--;
		sanityzeTime();
		return this;
	}
	public int getFrames() {
		return frames;
	}
	public Timecode setFrames(int frames) {
		this.frames = frames;
		sanityzeTime();
		return this;
	}
	public Timecode nextFrame(){
		frames++;
		sanityzeTime();
		return this;
	}
	public Timecode previousFrame(){
		frames--;
		sanityzeTime();
		return this;
	}
	public Framerates getFramerate() {
		return framerate;
	}
	public Timecode setFramerate(Framerates framerate) {
		int totalFrames = getTotalFrames();
		this.framerate = framerate;
		setTime(totalFrames);
		return this;
	}
	public float getFrameWindow(){
		return 1000 / framerate.getFps();
	}
	@Override
	public String toString(){
		return String.format("%02d:%02d:%02d:%02d", hours, mins, secs, frames);
	}
}
