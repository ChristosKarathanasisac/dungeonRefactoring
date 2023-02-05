package org.mafagafogigante.dungeon.game;

public class WorldGeneratorData {
	private World world;
	private RiverGenerator riverGenerator;
	private DungeonDistributor dungeonDistributor;
	private DungeonCreator dungeonCreator = new DungeonCreator(dungeonDistributor);
	private int chunkSide;

	public WorldGeneratorData(DungeonDistributor dungeonDistributor) {
		this.dungeonDistributor = dungeonDistributor;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public RiverGenerator getRiverGenerator() {
		return riverGenerator;
	}

	public void setRiverGenerator(RiverGenerator riverGenerator) {
		this.riverGenerator = riverGenerator;
	}

	public DungeonDistributor getDungeonDistributor() {
		return dungeonDistributor;
	}

	public void setDungeonDistributor(DungeonDistributor dungeonDistributor) {
		this.dungeonDistributor = dungeonDistributor;
	}

	public DungeonCreator getDungeonCreator() {
		return dungeonCreator;
	}

	public void setDungeonCreator(DungeonCreator dungeonCreator) {
		this.dungeonCreator = dungeonCreator;
	}

	public int getChunkSide() {
		return chunkSide;
	}

	public void setChunkSide(int chunkSide) {
		this.chunkSide = chunkSide;
	}
}