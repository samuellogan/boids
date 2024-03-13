/**
 * A 2D vector class for representing and manipulating x,y coordinates in a 2D
 * space. This class provides basic vector operations such as addition,
 * subtraction, multiplication, division, and normalization.
 * 
 * @author Samuel Logan <contact@samuellogan.dev>
 */
public class Vector {
    // The x component of the vector
    public float x;
    // The y component of the vector
    public float y;

    /**
     * Constructs a new vector given x and y coordinates.
     *
     * @param x The x coordinate of the vector.
     * @param y The y coordinate of the vector.
     */
    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Adds a vector to this vector, modifying this vector's components.
     *
     * @param v The vector to be added to this vector.
     */
    public void add(Vector v) {
        this.x += v.x;
        this.y += v.y;
    }

    /**
     * Subtracts a vector from this vector, modifying this vector's components.
     *
     * @param v The vector to be subtracted from this vector.
     */
    public void subtract(Vector v) {
        this.x -= v.x;
        this.y -= v.y;
    }

    /**
     * Multiplies this vector by a scalar, modifying this vector's components.
     *
     * @param n The scalar value to multiply the vector by.
     */
    public void multiply(float n) {
        this.x *= n;
        this.y *= n;
    }

    /**
     * Divides this vector by a scalar, modifying this vector's components.
     * Does nothing if the scalar is 0, to avoid division by zero.
     *
     * @param n The scalar value to divide the vector by.
     */
    public void divide(float n) {
        if (n != 0) {
            this.x /= n;
            this.y /= n;
        }
    }

    /**
     * Calculates the magnitude (length) of this vector.
     *
     * @return The magnitude of the vector.
     */
    public float magnitude() {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Normalizes this vector, converting it to a unit vector pointing in the same
     * direction.
     * Does nothing if the vector is of zero length.
     */
    public void normalize() {
        float m = magnitude();
        if (m != 0) {
            divide(m);
        }
    }

    /**
     * Limits the magnitude of this vector to a maximum value.
     * If the current magnitude is greater than the maximum, scales the vector down
     * to that maximum.
     *
     * @param max The maximum magnitude for the vector.
     */
    public void limit(float max) {
        if (magnitude() > max) {
            normalize();
            multiply(max);
        }
    }

    /**
     * Creates a random 2D unit vector with a random direction.
     *
     * @return A new Vector object representing a 2D unit vector with a random
     *         direction.
     */
    public static Vector random2D() {
        float angle = (float) (Math.random() * Math.PI * 2);
        return new Vector((float) Math.cos(angle), (float) Math.sin(angle));
    }
}
