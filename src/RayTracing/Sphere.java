package RayTracing;

/**
 * A sphere object
 */
public class Sphere extends Surface
{
	/** The center of the sphere */
	Vector3D _center;
	/** The radius of the sphere */
	private double _radius;
	/** The squared radius of the sphere. Saved here as an optimization*/
	private double _radiusSquared;
	
	/**
	 * Creates a new sphere
	 * @param x The x coordinate of the sphere center
	 * @param y The y coordinate of the sphere center
	 * @param z The z coordinate of the sphere center
	 * @param radius The radius of the sphere
	 * @param materialIndex The material index of the surface
	 */
	public Sphere(double x, double y, double z, double radius, int materialIndex)
	{
		super(materialIndex);
		_center = new Vector3D(x, y, z);
		_radius = radius;
		_radiusSquared  = _radius * _radius;
	}
	
	/**
	 * 
	 * @return The center of the sphere
	 */
	public Vector3D getCenter()
	{
		return _center;
	}
	
	/**
	 * 
	 * @return The radius of the sphere
	 */
	public double getRadius()
	{
		return _radius;
	}

	/*
	 * (non-Javadoc)
	 * @see RayTracing.Surface#checkIntersection(RayTracing.Ray)
	 */
	@Override
	public Intersection checkIntersection(Ray ray) 
	{
		Vector3D L = _center.sub(ray.getP0());
		double tca = L.dotProduct(ray.getV());
		
		if (tca < 0)
			return null;
		
		double dsquared = L.dotProduct(L) - tca * tca;
		
		if (dsquared > _radiusSquared)
			return null;
		
		double thc = Math.sqrt(_radiusSquared - dsquared);
		double t1 = tca - thc;
		double t2 = tca + thc;
		
		// find the appropriate intersection point
		double t;
		if (t1 < EPSILON && t2 < EPSILON)
		{
			return null;
		}
		else if (t1 >= EPSILON && t2 >= EPSILON)
		{
			t = Math.min(t1,t2);
		}
		else
		{
			t = Math.max(t1, t2);
		}
		
		if (t < EPSILON)
			return null;
		
		Vector3D intersectionPoint = ray.getP0().add(ray.getV().multByScalar(t));
		Vector3D normal = intersectionPoint.sub(_center).normalize();
		
		return new Intersection(t, intersectionPoint, normal, this, ray);
	}
}
