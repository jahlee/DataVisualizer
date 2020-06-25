/**
 * Helper class to better organize coordinate points
 */
public class Coordinate {
    private float x;
    private float y;

    public Coordinate(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return (int) this.x;
    }

    public int getY() { return (int) this.y; }
}