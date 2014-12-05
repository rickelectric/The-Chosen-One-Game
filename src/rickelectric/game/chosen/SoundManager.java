package rickelectric.game.chosen;
import java.applet.Applet;
import java.applet.AudioClip;
import java.util.HashMap;
import java.util.Set;

public class SoundManager {
	private static SoundManager thisInst = null;

	private HashMap<String, AudioClip> sounds;

	public static synchronized SoundManager getInstance() {
		if (thisInst == null) {
			thisInst = new SoundManager();
		}
		return thisInst;
	}

	/**
	 * Constructor
	 */
	private SoundManager() {
		sounds = new HashMap<String, AudioClip>();
		loadSounds();
	}

	/**
	 * Build our hash table
	 */
	private void loadSounds() {
		sounds.put("xfiles", getSound("/sounds/xfiles.wav"));
	}

	/**
	 * Play or loop a sound
	 * 
	 * @param name
	 * @param loop
	 */
	public void playSound(String name, boolean loop) {
		AudioClip ac = sounds.get(name);
		if(ac!=null){
			if (loop)
				ac.loop();
			else
				ac.play();
		}
	}

	/**
	 * Stops a specified sound
	 * 
	 * @param name
	 */
	public void stopSound(String name) {
		sounds.get(name).stop();
	}

	/**
	 * Stops all sounds
	 */
	public void stopAll() {
		Set<String> e = sounds.keySet();
		for (String s : e) {
			sounds.get(s).stop();
		}
	}

	/**
	 * Dumps hash table contents
	 */
	public void dispose() {
		sounds.clear();
		sounds = null;
	}

	/***
	 * Loads audio clip from file
	 * 
	 * @param ref
	 * @return
	 */
	private AudioClip getSound(String ref) {
		AudioClip ac = null;
		try {
			ac = Applet.newAudioClip(getClass().getResource(ref));
		} catch (Exception e) {
			System.out.println("PROBLEM LOADING SOUND: " + e.getMessage());
			e.printStackTrace();
		}

		return ac;
	}

}
