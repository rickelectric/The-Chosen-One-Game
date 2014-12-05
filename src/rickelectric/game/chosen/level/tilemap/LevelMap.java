package rickelectric.game.chosen.level.tilemap;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import rickelectric.game.chosen.AssetManager;
import rickelectric.game.chosen.Globals;

import com.google.gson.Gson;

public class LevelMap {
	private Tile[][][] tiles;
	private Map mapData;

	private HashMap<Integer, ArrayList<Line2D>> terrain;
	private Integer terrainStart;

	public LevelMap(String mapName) {
		Gson gson = new Gson();

		String json = "";
		try {
			json = getMap("maps/" + mapName + ".json");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println(json);
		this.mapData = gson.fromJson(json, Map.class);

		terrainStart = null;
		terrain = new HashMap<Integer, ArrayList<Line2D>>();
		int tLayers = 0;
		for (int lid = 0; lid < mapData.layers.length; lid++) {
			MapLayer l = mapData.layers[lid];
			if (l.type.equals("objectgroup")) {
				tLayers++;
				if (terrainStart == null)
					terrainStart = lid;
				ArrayList<Line2D> layerTerraiin = new ArrayList<Line2D>();
				for (MapObject o : l.objects) {
					for (int p = 1; p < o.polyline.length; p++) {
						layerTerraiin.add(new Line2D.Double(o.x
								+ o.polyline[p - 1].x, o.y
								+ o.polyline[p - 1].y, o.x + o.polyline[p].x,
								o.y + o.polyline[p].y));
					}
				}
				terrain.put(lid, layerTerraiin);
			}
		}

		tiles = new Tile[mapData.layers.length - tLayers][mapData.width][mapData.height];
		buildTilesArray();

		Globals.MAP = this;
		Globals.WORLD_HEIGHT = mapData.height * mapData.tileheight;
		Globals.WORLD_WIDTH = mapData.width * mapData.tilewidth;
	}

	/**
	 * build tile array from map data
	 */
	private void buildTilesArray() {
		int drawX = 0;
		int drawY = 0;
		int tileX = 0;
		int tileY = 0;

		// iterate through our map layers
		for (int i = 0; i < mapData.layers.length; i++) {
			if (mapData.layers[i].type.equals("objectgroup"))
				continue;
			// reset drawX and drawY for each layer
			drawX = 0;
			drawY = 0;
			tileX = 0;
			tileY = 0;

			// iterate through our layer data
			for (int j = 0; j < mapData.layers[i].data.length; j++) {
				// if there is no tiles at this location
				if (mapData.layers[i].data[j] == 0 && i == 0) {
					tiles[i][tileX][tileY] = null;
				}

				// if we have a tile
				if (mapData.layers[i].data[j] != 0) {

					int tileID = mapData.layers[i].data[j];
					int tilesetID = mapData.tilesets.length - 1;
					for (int v = 0; v < mapData.tilesets.length - 1; v++) {
						if (tileID >= mapData.tilesets[v].firstgid
								&& tileID < mapData.tilesets[v + 1].firstgid) {
							tilesetID = v;
							break;
						}
					}

					int ordinalID = tileID
							- (mapData.tilesets[tilesetID].firstgid - 1);

					// System.out.println("TileID: " + tileID + " @Ordinal: "
					// + ordinalID + ", Tileset: "
					// + mapData.tilesets[tilesetID].image);

					int sourceX = ordinalID
							* mapData.tilesets[tilesetID].tilewidth;
					int sourceY = 0;

					// calculate source coordinates
					while (sourceX > mapData.tilesets[tilesetID].imagewidth) {
						sourceX -= mapData.tilesets[tilesetID].imagewidth;
						sourceY += mapData.tilesets[tilesetID].tileheight;
					}
					sourceX -= mapData.tilesets[tilesetID].tilewidth;

					// create tile
					tiles[i][tileX][tileY] = new Tile(
							mapData.tilesets[tilesetID].image, tileID, i,
							drawX, drawY,
							mapData.tilesets[tilesetID].tilewidth,
							mapData.tilesets[tilesetID].tileheight, sourceX,
							sourceY);
				}

				// increment drawX
				drawX += mapData.tilewidth;

				tileX++;

				if (drawX >= (mapData.tilewidth * mapData.width)) {
					drawX = 0;
					drawY += mapData.tileheight;
					tileX = 0;
					tileY++;
				}
			}
		}
	}

	public void update() {
		for (int k = 0; k < getTileLayerCount(); k++) {
			if (mapData.layers[k].visible)
				for (int i = 0; i < mapData.width; i++) {
					for (int j = 0; j < mapData.height; j++) {
						Tile t = tiles[k][i][j];
						if (t != null)
							t.update();
					}
				}
		}
	}

	/**
	 * Draw tiles
	 * 
	 * @param g2d
	 */
	public void draw(Graphics2D g2d) {
		for (int k = 0; k < getTileLayerCount(); k++) {
			if (mapData.layers[k].visible) {
				if (mapData.layers[k].type.equals("tilelayer")) {
					for (int i = 0; i < mapData.width; i++) {
						for (int j = 0; j < mapData.height; j++) {
							Tile t = tiles[k][i][j];

							if (t != null)
								t.draw(g2d);
						}
					}
				} else if (mapData.layers[k].type.equals("objectgroup")) {
					g2d.setStroke(new BasicStroke(4));
					ArrayList<Line2D> layerTerrain = terrain.get(k);
					for (Line2D l : layerTerrain) {
						g2d.draw(l);
					}
				}
			}
		}
	}

	/**
	 * Read map file
	 * 
	 * @param filename
	 * @return
	 */
	private String getMap(String file) throws IOException {
		return AssetManager.getInstance().readMapFile(file);
	}

	/**
	 * Returns a tile if present at our x,y array position
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Tile getTileAt(int layer, int x, int y) {
		if (x > 0 && y > 0 && x < mapData.width && y < mapData.height
				&& layer > 0 && layer < mapData.layers.length)
			return tiles[layer][x][y];
		return null;
	}

	public Map getMapData() {
		return mapData;
	}

	public ArrayList<Line2D> getTerrainLayer(int layerID) {
		return terrain.get(layerID);
	}

	public int getTerrainStart() {
		return terrainStart;
	}

	public int getTileLayerCount() {
		return tiles.length;
	}

	public Point getXYOf(int tileX, int tileY) {
		return new Point(tileX * mapData.tilewidth, tileY * mapData.tileheight);
	}

}
