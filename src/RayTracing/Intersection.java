package RayTracing;

/**
 * An intersection between a surface and a ray
 */
public class Intersection 
{
	/** The value of the parameter in which the ray hits the object*/
	private double _t;
	/** The point in which the objects intersect */
	private Vector3D _intersectionPoint;
	/** The normal vector in the intersection point */
	private Vector3D _normalAtIntersectionPoint;
	/** The surface which the ray intersects with*/
	private Surface _surface;
	/** The ray which the surface intersects with*/
	private Ray _ray;
	
	/**
	 * Creates a new intersection object
	 * @param t The value fo the parameter in which the ray hits the object
	 * @param intersectionPoint The intersection point of the ray and the surface
	 * @param normal The normal at the interseciton point
	 * @param surface The surface which the ray intersects with
	 * @param ray the ray which the surface intersects with
	 */
	public Intersection(double t, Vector3D intersectionPoint, Vector3D normal, Surface surface, Ray ray)
	{
		_t = t;
		_intersectionPoint = intersectionPoint;
		_normalAtIntersectionPoint = normal.normalize();
		_surface = surface;
		_ray = ray;
	}
	
	/**
	 * 
	 * @return The value of the parameter in the intersection point
	 */
	public double getT()
	{
		return _t;
	}
	
	/**
	 * 
	 * @return The intersection point
	 */
	public Vector3D getIntersectionPoint()
	{
		return _intersectionPoint;
	}
	
	/**
	 * 
	 * @return The normal at the intersectin point
	 */
	public Vector3D getNormal()
	{
		return _normalAtIntersectionPoint;
	}
	
	/**
	 * 
	 * @return The surface which the ray intersects with
	 */
	public Surface getSurface()
	{
		return _surface;
	}
	
	/**
	 * 
	 * @return The ray which the surface intersects with
	 */
	public Ray getRay()
	{
		return _ray;
	}

}
