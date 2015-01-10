package rickelectric.game.chosen.sounds;

import java.util.HashMap;
import java.util.Set;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class SoundManager {
	private static SoundManager thisInst = null;

	private HashMap<String, Clip> sounds;

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
		sounds = new HashMap<String, Clip>();
		loadSounds();
	}

	/**
	 * Build our hash table
	 */
	private void loadSounds() {
		sounds.put("level1", getSound("waveLight.wav"));
		sounds.put("level2", getSound("space.wav"));
		sounds.put("theme", getSound("theme.wav"));
		sounds.put("title", getSound("title.wav"));
		sounds.put("lightning", getSound("lightning.wav"));
		sounds.put("vortex", getSound("vortex.wav"));
		sounds.put("entralink", getSound("entralink.wav"));
		sounds.put("select", getSound("select.wav"));
	}

	/**
	 * Play or loop a sound
	 * 
	 * @param name
	 * @param loop
	 */
	public void playSound(String name, boolean loop) {
		Clip ac = sounds.get(name);
		ac.setFramePosition(0);
		if (ac != null) {
			if (loop)
				ac.loop(Clip.LOOP_CONTINUOUSLY);
			else
				ac.start();
		}
	}
	
	public void resumeSound(String name, boolean loop) {
		Clip ac = sounds.get(name);
		if (ac != null) {
			if (loop)
				ac.loop(Clip.LOOP_CONTINUOUSLY);
			else
				ac.start();
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
	
	public void pauseSound(String name) {
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
	private Clip getSound(String ref) {
		try {
			AudioInputStream audioStream = AudioSystem
					.getAudioInputStream(getClass().getResource(ref));
			Clip clip;
			AudioFormat format = audioStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(audioStream);
			return clip;
		} catch (Exception e) {
			System.out.println("PROBLEM LOADING SOUND: " + e.getMessage());
			e.printStackTrace();
			return null;
		}

		// AudioClip ac = null;
		// try {
		// ac = Applet.newAudioClip(getClass().getResource(ref));
		// } catch (Exception e) {
		// System.out.println("PROBLEM LOADING SOUND: " + e.getMessage());
		// e.printStackTrace();
		// }

		// return ac;
	}

	public void suspend() {
		stopAll();
	}

}
