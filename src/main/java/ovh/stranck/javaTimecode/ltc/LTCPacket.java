package ovh.stranck.javaTimecode.ltc;

import ovh.stranck.javaTimecode.Framerates;
import ovh.stranck.javaTimecode.Timecode;
import ovh.stranck.javaTimecode.Utils;

public class LTCPacket extends Timecode {
	private static final boolean[] SYNC_WORD = Utils.convertBitString("0011111111111101");
	
	private final boolean[][] userbits = new boolean[8][];
	private boolean bgf0, bgf2;
	private boolean reversed;
	private boolean df; //TODO: REMOVE	
	private boolean sync;
	private boolean cf;
	private byte volume;
	
	public LTCPacket(){
		this(0, 0, 0, 0, Framerates.FRAMERATE_30);
	}
	public LTCPacket(int hour, int min, int sec, int frame, Framerates framerate) {
		this(hour, min, sec, frame, framerate, (byte) 90, false, false, false, false, false);
	}
	public LTCPacket(int hour, int min, int sec, int frame, Framerates framerate, byte volume,
			boolean colorFlag, boolean sync, boolean bgf0, boolean bgf2, boolean reversed) {
		super(hour, min, sec, frame, framerate);
		this.sync = sync;
		cf = colorFlag;
		this.bgf0 = bgf0;
		this.bgf2 = bgf2;
		this.reversed = reversed;
		this.volume = volume;
		for(int i = 0; i < userbits.length; i++)
			userbits[i] = new boolean[4];
	}
	
	private int buildBlock(boolean[] data, int index, int value, boolean longValue, boolean flag1, boolean flag2,
			boolean[] userBits1, boolean[] userBits2) {
		int optionalBit = longValue ? 1 : 0;
		boolean[] bcd = Utils.bcd(value, 6 + optionalBit);
		//First 4 bits of value
		for(int i = 0; i < 4; i++)
			data[index++] = bcd[i];
		//user bits
		index = Utils.addAllBits(data, index, userBits1);
		//Second 2/3 bits of value
		for(int i = 0; i < 2 + optionalBit; i++)
			data[index++] = bcd[4 + i];
		//First flag
		data[index++] = flag1;
		//If we're working with just 6 bits, add the second flag
		if(!longValue)
			data[index++] = flag2;
		//User bits again
		index = Utils.addAllBits(data, index, userBits2);
		return index;
	}
	public boolean[] asBooleanArray(){
		boolean data[] = new boolean[80];
		int index = 0;
		index = buildBlock(data, index, frames, false, df, cf, userbits[0], userbits[1]);
		index = buildBlock(data, index, secs, true, false, false, userbits[2], userbits[3]);
		index = buildBlock(data, index, mins, true, false, false, userbits[4], userbits[5]);
		index = buildBlock(data, index, hours, false, sync, false, userbits[6], userbits[7]);
		Utils.addAllBits(data, index, SYNC_WORD);
		data[framerate == Framerates.FRAMERATE_25 ? 59 : 27] = Utils.getBitParity(data, false, true);
		data[framerate == Framerates.FRAMERATE_25 ? 27 : 43] = bgf0;
		data[framerate == Framerates.FRAMERATE_25 ? 43 : 59] = bgf2;
		//if(framerate == Framerate.FRAMERATE_DROPFRAME) data[10] = 
		return data;
	}
	public byte[] asAudioSample(int sampleRate){
		int repeat = (int) (sampleRate / (160 * framerate.getFps()));
		return manchesterEncode(asBooleanArray(), repeat);
	}
	@Override
	public byte[] asByteArray() {
		return manchesterEncode(asBooleanArray(), 1);
	}
	@Override
	public String asBitsString(){
		boolean[] data = asBooleanArray();
		StringBuilder sb = new StringBuilder(data.length * 8);
		for(boolean b : data)
			sb.append(b ? '1' : '0');

		return sb.toString();
	}
	
	private byte[] manchesterEncode(boolean value[], int repeatBytes) {
		byte[] result = new byte[value.length * 2 * repeatBytes];
		boolean v = false;
		int index = 0;
		int add = reversed ? -1 : 1;
		for (int i = reversed ? value.length - 1 : 0; (!reversed && i < value.length) || (reversed && i >= 0); i += add) {
			if (value[i]) {
				v = !v;
				index = Utils.repeatBytes(result, index, (byte) (v ? volume : -volume), repeatBytes);
				v = !v;
				index = Utils.repeatBytes(result, index, (byte) (v ? volume : -volume), repeatBytes);
			} else {
				v = !v;
				index = Utils.repeatBytes(result, index, (byte) (v ? volume : -volume), repeatBytes * 2);
			}
		}
		return result;
	}
	
	public boolean[] getUserBits(int block){
		return userbits[block];
	}
	public LTCPacket setUserbits(String bits, int block){
		userbits[block] = Utils.convertBitString(bits);
		return this;
	}
	public LTCPacket setUserbits(boolean[] bits, int block){
		userbits[block] = bits;
		return this;
	}
	public boolean isBgf0() {
		return bgf0;
	}
	public LTCPacket setBgf0(boolean bgf0) {
		this.bgf0 = bgf0;
		return this;
	}
	public boolean isBgf2() {
		return bgf2;
	}
	public LTCPacket setBgf2(boolean bgf2) {
		this.bgf2 = bgf2;
		return this;
	}
	public boolean isDf() {
		return df;
	}
	public LTCPacket setDf(boolean df) {
		this.df = df;
		return this;
	}
	public boolean isSync() {
		return sync;
	}
	public LTCPacket setSync(boolean sync) {
		this.sync = sync;
		return this;
	}
	public LTCPacket setVolume(byte volume){
		this.volume = volume;
		return this;
	}
	public LTCPacket setPercentVolume(int volume){
		this.volume = (byte) (Byte.MAX_VALUE * volume / 100);
		return this;
	}
	public byte getVolume(){
		return volume;
	}
}
