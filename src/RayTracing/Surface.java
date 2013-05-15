package RayTracing;

public abstract class Surface
{
	/** An epsilon value used for intersection calculation */
	protected static final double EPSILON = 0.000000001;
	
	/** The index of the material in the materials array*/
	private int _materialIndex;
	
	/**
	 * Constructor used for setting this class fields
	 * @param materialIndex The material index of this surface
	 */
	public Surface(int materialIndex)
	{
		_materialIndex = materialIndex;
	}
	
	/**
	 * Checks if the given ray intersects with the surface
	 * @param ray The ray to checl the intersection with
	 * @return An object describing the intersection
	 */
	public abstract Intersection checkIntersection(Ray ray);
	
	/**
	 * 
	 * @return The material index of this surface
	 */
	public int getMaterialIndex()
	{
		return _materialIndex;
	}
}
