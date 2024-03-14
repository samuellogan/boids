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
     * @return This Vector object after addition.
     */
    public Vector add(Vector v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    /**
     * Subtracts a vector from this vector, modifying this vector's components.
     *
     * @param v The vector to be subtracted from this vector.
     * @return This Vector object after subtraction.
     */
    public Vector subtract(Vector v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    /**
     * Multiplies this vector by a scalar, modifying this vector's components.
     *
     * @param n The scalar value to multiply the vector by.
     * @return This Vector object after multiplication.
     */
    public Vector multiply(float n) {
        this.x *= n;
        this.y *= n;
        return this;
    }

    /**
     * Divides this vector by a scalar, modifying this vector's components.
     * Does nothing if the scalar is 0, to avoid division by zero.
     *
     * @param n The scalar value to divide the vector by.
     * @return This Vector object after division.
     */
    public Vector divide(float n) {
        if (n != 0) {
            this.x /= n;
            this.y /= n;
        }

        return this;
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
     * direction. Returns itself for chaining.
     * Does nothing and returns itself if the vector is of zero length.
     *
     * @return This Vector object after normalization.
     */
    public Vector normalize() {
        float m = magnitude();
        if (m != 0) {
            divide(m);
        }
        return this; // Return the current Vector object to allow for method chaining.
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
     * Limits the magnitude of this vector to a range between a minimum and maximum
     * value. If the current magnitude is less than the minimum, scales the vector
     * up
     * to that minimum. If the current magnitude is greater than the maximum, scales
     * the vector down to that maximum.
     *
     * @param min The minimum magnitude for the vector.
     * @param max The maximum magnitude for the vector.
     */
    public void limit(float min, float max) {
        float m = magnitude();
        if (m < min) {
            normalize();
            multiply(min);
        } else if (m > max) {
            normalize();
            multiply(max);
        }
    }

    /**
     * Calculates the distance between two vectors.
     *
     * @param v1 The first vector.
     * @param v2 The second vector.
     * @return The distance between the two vectors.
     */
    public static float dist(Vector v1, Vector v2) {
        float dx = v2.x - v1.x;
        float dy = v2.y - v1.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Subtracts the second vector from the first vector and returns the result as a
     * new Vector.
     *
     * @param v1 The vector to subtract from.
     * @param v2 The vector to be subtracted.
     * @return A new Vector that is the result of the subtraction v1 - v2.
     */
    public static Vector sub(Vector v1, Vector v2) {
        return new Vector(v1.x - v2.x, v1.y - v2.y);
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

    /**
     * Calculates the angle between two vectors.
     *
     * @param v1 The first vector.
     * @param v2 The second vector.
     * @return The angle in radians between the two vectors.
     */
    public static float angleBetween(Vector v1, Vector v2) {
        float dotProduct = v1.x * v2.x + v1.y * v2.y;
        float magnitudeV1 = v1.magnitude();
        float magnitudeV2 = v2.magnitude();
        float cosineAngle = dotProduct / (magnitudeV1 * magnitudeV2);
        // Ensure the cosine value is within -1 and 1 to avoid NaN from acos()
        cosineAngle = Math.max(-1, Math.min(1, cosineAngle));
        return (float) Math.acos(cosineAngle);
    }

    /**
     * Calculates the dot product of this vector with another vector.
     * 
     * @param v The other vector to calculate the dot product with.
     * @return The dot product of this vector and the other vector.
     */
    public float dot(Vector v) {
        return this.x * v.x + this.y * v.y;
    }

    /**
     * Adds a dot product method to calculate the dot product of two vectors.
     * This is a static method that can be called without an instance of Vector.
     *
     * @param v1 The first vector.
     * @param v2 The second vector.
     * @return The dot product of the two vectors.
     */
    public static float dot(Vector v1, Vector v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }
}
