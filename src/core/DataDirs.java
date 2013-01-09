package core;

/**
 * DataDirs.java
 * <p>
 * Keeps track of constantly used directories
 * @author nhydock
 *
 */
public enum DataDirs {
	
	/**
	 * Head data directory
	 */
	DataDir("data/"),
	/**
	 * location of images used by the game
	 */
	ImageDir(DataDir.path+"images/"),
	/**
	 * location of audio files
	 */
	AudioDir(DataDir.path+"audio/"),
	/**
	 * location of background music files
	 */
	BGMDir(AudioDir.path+"bgm/"),
	/**
	 * location of sound fx files
	 */
	SFXDir(AudioDir.path+"fx/"),
	/**
	 * location of all fonts files usable by the game
	 */
	FontDir(DataDir.path+"fonts/");
	
	/**
	 * Path to data
	 */
	public final String path;
	
	DataDirs(String p){
		path = p;
	}
}
