package RayTracing;

public class Intersection 
{
	
	private double _t;
	private Vector3D _intersectionPoint;
	private Vector3D _normalAtIntersectionPoint;
	private Surface _surface;
	
	public Intersection(double t, Vector3D intersectionPoint, Vector3D normal, Surface surface)
	{
		_t = t;
		_intersectionPoint = intersectionPoint;
		_normalAtIntersectionPoint = normal;
		_surface = surface;
	}
	
	public double getT()
	{
		return _t;
	}
	
	public Vector3D getIntersectionPoint()
	{
		return _intersectionPoint;
	}
	
	public Vector3D getNormal()
	{
		return _normalAtIntersectionPoint;
	}
	
	public Surface getSurface()
	{
		return _surface;
	}

}
