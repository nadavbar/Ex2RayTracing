package RayTracing;

public class Intersection 
{
	
	private double _t;
	private Vector3D _intersectionPoint;
	private Vector3D _normalAtIntersectionPoint;
	
	public Intersection(double t, Vector3D intersectionPoint, Vector3D normal)
	{
		_t = t;
		_intersectionPoint = intersectionPoint;
		_normalAtIntersectionPoint = normal;
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
	

}
