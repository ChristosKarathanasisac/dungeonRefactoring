package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.game.LocationPreset.Type;
import org.mafagafogigante.dungeon.io.Version;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * The world generator. This class should be instantiated by a World object.
 */
class WorldGenerator implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private static final int DEFAULT_CHUNK_SIDE = 5;

  WorldGeneratorData data = new WorldGeneratorData(new DungeonDistributor());

WorldGenerator(World world) {
    this.data.setWorld(world);
    this.data.setRiverGenerator(new RiverGenerator());
    this.data.setChunkSide(WorldGenerator.DEFAULT_CHUNK_SIDE);
  }

  /**
   * Retrieves a random LocationPreset whose type is "Land".
   *
   * @return a LocationPreset
   */
  static LocationPreset getRandomLandLocationPreset() {
    LocationPresetStore locationPresetStore = LocationPresetStore.getDefaultLocationPresetStore();
    return Random.select(locationPresetStore.getLocationPresetsByType(Type.LAND));
  }

  Location createRandomRiverLocation(@NotNull final Point point) {
    LocationPresetStore locationPresetStore = LocationPresetStore.getDefaultLocationPresetStore();
    return new Location(Random.select(locationPresetStore.getLocationPresetsByType(Type.RIVER)), data.getWorld(), point);
  }

  Location createRandomBridgeLocation(@NotNull final Point point) {
    LocationPresetStore locationPresetStore = LocationPresetStore.getDefaultLocationPresetStore();
    return new Location(Random.select(locationPresetStore.getLocationPresetsByType(Type.BRIDGE)), data.getWorld(), point);
  }

  Location createRiversideLocation(@NotNull final Point point) {
    LocationPresetStore locationPresetStore = LocationPresetStore.getDefaultLocationPresetStore();
    return new Location(Random.select(locationPresetStore.getLocationPresetsByType(Type.RIVERSIDE)), data.getWorld(), point);
  }

}
