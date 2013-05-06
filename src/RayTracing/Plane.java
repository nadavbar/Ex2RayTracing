package RayTracing;

public class Plane extends Surface
{
	private Vector3D _normal;
	private double _offset;
	
	public Plane(Vector3D normal, double offset, int materialIndex)
	{
		super(materialIndex);
		_normal = normal;
		_offset = offset;
	}
	
	public Vector3D getNormal()
	{
		return _normal;
	}
	
	public double getOffset()
	{
		return _offset;
	}

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
