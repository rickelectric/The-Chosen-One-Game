package rickelectric.game.chosen.level.tilemap;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import rickelectric.game.chosen.ActionManager;
import rickelectric.game.chosen.AssetManager;
import rickelectric.game.chosen.Globals;
import rickelectric.game.chosen.entities.Entity;

public class Tile extends Entity {

	private Image tileset;
	private int sourceX;
	private int sourceY;
	private int tileTypeID;
	private int layerID;

	public Tile(String tilesetName, int tileTypeID, int layerID, int x, int y,
			int width, int height, int sourceX, int sourceY) {
		super(x, y, width, height);
		this.layerID = layerID;
		this.sourceX = sourceX;
		this.sourceY = sourceY;
		this.tileTypeID = tileTypeID;
		tileset = AssetManager.getInstance().getTileset(tilesetName);
	}

	public void draw(Graphics2D g2d) {
		g2d.setStroke(new BasicStroke(5));
		g2d.setColor(layerID == 1 ? Color.green : layerID == 2 ? Color.blue
				: layerID == 3 ? Color.magenta : layerID == 4 ? Color.orange
						: Color.red);

		g2d.setComposite(AlphaComposite.SrcOver.derive((float) Globals.MAP
				.getMapData().layers[layerID].opacity));
		g2d.drawImage(tileset, (int) x, (int) y, (int) (x + width),
				(int) (y + height), (int) sourceX, (int) sourceY,
				(int) (sourceX + width), (int) (sourceY + height), null);

		/*g2d.drawRect(boundingRect.x, boundingRect.y, boundingRect.width,
				boundingRect.height);
				*/
	}

	@Override
	public void update() {
		ActionManager.getInstance().activateAction(this);
	}

	public int getTileTypeID() {
		return tileTypeID;
	}

}
