package ovh.stranck.javaTimecode;

/**
 * This enum represents all possible framerates allowed by SMPTE.
 * 
 * @author <a href="https://github.com/mrexplode">MrExplode</a> and <a href="https://www.stranck.ovh">Stranck</a>
 *
 */
public enum Framerates {
    
    /**
     * 24 frames per second
     */
    FRAMERATE_24(24f),
    
    /**
     * 25 frames per second
     */
    FRAMERATE_25(25f),
    
    /**
     * 29.97 frames per second
     */
    FRAMERATE_DROPFRAME(29.97f),
    
    /**
     * 30 frames per second
     */
    FRAMERATE_30(30f);
    
    private float fps;
    Framerates(float fps) {
        this.fps = fps;
    }
    public float getFps(){
    	return fps;
    }
	public int getIntegerFramerate(){
		return (int) Math.ceil(fps);
	}
}

