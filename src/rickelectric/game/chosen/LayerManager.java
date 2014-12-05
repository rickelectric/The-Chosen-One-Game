package rickelectric.game.chosen;
public class LayerManager {

	public static LayerManager thisInstance;

	public static synchronized LayerManager getInstance() {
		if (thisInstance == null)
			thisInstance = new LayerManager();
		return thisInstance;
	}

	public void setVisible(int layerID, boolean visible) {

	}

	public void setTangible(int layerID, boolean tangible) {

	}

}
