package RayTracing;

/**
 * A Plane object
 */
public class Plane extends Surface
{
	/** The normal of the plane*/
	private Vector3D _normal;
	/** The offset of the plane*/
	private double _offset;
	
	/**
	 * Creates a new plane
	 * @param normal The normal of the plane
	 * @param offset The offset of the plane
	 * @param materialIndex The index of the plane material
	 */
	public Plane(Vector3D normal, double offset, int materialIndex)
	{
		super(materialIndex);
		_normal = normal;
		_offset = offset;
	}
	
	/**
	 * 
	 * @return The normal of the plane 
	 */
	public Vector3D getNormal()
	{
		return _normal;
	}
	
	/**
	 * 
	 * @return the offset of the plane
	 */
	public double getOffset()
	{
		return _offset;
	}

	/*
	 * (non-Javadoc)
	 * @see RayTracing.Surface#checkIntersection(RayTracing.Ray)
	 */
	@Override
	public Intersection checkIntersection(Ray ray) 
	{	
		double t = (-ray.getP0().dotProduct(_normal) + _offset) / 
					(ray.getV().dotProduct(_normal));
		
		if (t < EPSILON)
		{
			return null;
		}
		
		Vector3D intersectionPoint = ray.getP0().add(ray.getV().multByScalar(t));
		
		return new Intersection(t, intersectionPoint, _normal, this, ray);
	}
}
