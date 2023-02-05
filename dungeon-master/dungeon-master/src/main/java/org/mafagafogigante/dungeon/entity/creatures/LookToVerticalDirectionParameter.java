package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.game.Point;
import org.mafagafogigante.dungeon.game.World;

public class LookToVerticalDirectionParameter {
	public DungeonString dungeonString;
	public World world;
	public Point up;
	public String adverb;

	public LookToVerticalDirectionParameter(DungeonString dungeonString, World world, Point up, String adverb) {
		this.dungeonString = dungeonString;
		this.world = world;
		this.up = up;
		this.adverb = adverb;
	}
}