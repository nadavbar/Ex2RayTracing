package RayTracing;

public class Plain extends Surface
{
	private Vector3D _normal;
	private double _offset;
	private int _materialIndex;
	
	public Plain(Vector3D normal, double offset, int materialIndex)
	{
		_normal = normal;
		_offset = offset;
		_materialIndex = materialIndex;
	}
	
	public Vector3D getNormal()
	{
		return _normal;
	}
	
	public double getOffset()
	{
		return _offset;
	}
	
	public int getMaterialIndex()
	{
		return _materialIndex;
	}

	@Override
	public Intersection checkIntersection(Ray ray) 
	{
		double t = (ray.getP().scalarProduct(_normal) + _offset) / 
					(ray.getV().scalarProduct(_normal));
		
		if (t < EPSILON)
		{
			return null;
		}
		
		Vector3D intersectionPoint = ray.getP0().add(ray.getV().multByScalar(t));
		
		return new Intersection(t, intersectionPoint, _normal);
	}
}
