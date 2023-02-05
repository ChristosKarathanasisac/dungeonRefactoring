package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.io.Version;

import java.io.Serializable;

/**
 * A point in a tridimensional matrix.
 */
public class Point implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final int x;
  private final int y;
  private final int z;

  /**
   * Constructs a Point from three integers representing x, y, and z, respectively.
   */
  public Point(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   * Constructs a Point from another Point and a Direction that is equivalent to the specified Point moved towards the
   * provided Direction.
   */
  public Point(Point originalPoint, Direction shift) {
    this.x = originalPoint.getX() + shift.getOffset().getX();
    this.y = originalPoint.getY() + shift.getOffset().getY();
    this.z = originalPoint.getZ() + shift.getOffset().getZ();
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getZ() {
    return z;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    Point point = (Point) object;
    return getX() == point.getX() && getY() == point.getY() && getZ() == point.getZ();
  }

  @Override
  public int hashCode() {
    int result = getX();
    result = 31 * result + getY();
    result = 31 * result + getZ();
    return result;
  }

  @Override
  public String toString() {
    return String.format("{%d, %d, %d}", getX(), getY(), getZ());
  }

public void expand(WorldGenerator worldGenerator) {
    worldGenerator.data.getRiverGenerator().expand(this, worldGenerator.data.getChunkSide());
    Point currentPoint;
    LocationPreset currentLocationPreset = null;
    int remainingLocationsOfCurrentPreset = 0;
    int pX = getX();
    int pY = getY();
    // Get the closest smaller chunkSide multiple of x and y.
    // For instance, if chunkSide == 5, x == -2 and y == 1, then it makes xStart == -5 and yStart == 0.
    int xStart = pX < 0 ? worldGenerator.data.getChunkSide() * (((pX + 1) / worldGenerator.data.getChunkSide()) - 1) : worldGenerator.data.getChunkSide() * (pX / worldGenerator.data.getChunkSide());
    int yStart = pY < 0 ? worldGenerator.data.getChunkSide() * (((pY + 1) / worldGenerator.data.getChunkSide()) - 1) : worldGenerator.data.getChunkSide() * (pY / worldGenerator.data.getChunkSide());
    for (int x = xStart; x < xStart + worldGenerator.data.getChunkSide(); x++) {
      for (int y = yStart; y < yStart + worldGenerator.data.getChunkSide(); y++) {
        currentPoint = new Point(x, y, 0);
        if (!worldGenerator.data.getWorld().alreadyHasLocationAt(currentPoint)) {
          if (worldGenerator.data.getRiverGenerator().isRiver(currentPoint)) {
            worldGenerator.data.getWorld().addLocation(worldGenerator.createRandomRiverLocation(currentPoint), currentPoint);
          } else if (worldGenerator.data.getRiverGenerator().isBridge(currentPoint)) {
            worldGenerator.data.getWorld().addLocation(worldGenerator.createRandomBridgeLocation(currentPoint), currentPoint);
          } else if (worldGenerator.data.getRiverGenerator().isRiverside(currentPoint)) {
            worldGenerator.data.getWorld().addLocation(worldGenerator.createRiversideLocation(currentPoint), currentPoint);
          } else if (worldGenerator.data.getDungeonDistributor().rollForDungeon(currentPoint)) {
            worldGenerator.data.getDungeonCreator().createDungeon(worldGenerator.data.getWorld(), currentPoint);
          } else {
            if (currentLocationPreset == null || remainingLocationsOfCurrentPreset == 0) {
              currentLocationPreset = WorldGenerator.getRandomLandLocationPreset();
              remainingLocationsOfCurrentPreset = currentLocationPreset.getBlobSize();
            }
            worldGenerator.data.getWorld().addLocation(new Location(currentLocationPreset, worldGenerator.data.getWorld(), currentPoint), currentPoint);
            remainingLocationsOfCurrentPreset--;
          }
        }
      }
    }
  }
}
