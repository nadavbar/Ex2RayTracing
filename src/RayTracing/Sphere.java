package RayTracing;

public class Sphere extends Surface
{
	public Sphere(double x, double y, double z, double radius, int materialIndex)
	{
		_center = new Vector3D(x, y, z);
		_radius = radius;
		_radiusSquared  = _radius * _radius;
		_materialIndex = materialIndex;
	}
	
	Vector3D _center;
	private double _radius;
	private double _radiusSquared;
	private int _materialIndex;
	
	public Vector3D getCenter()
	{
		return _center;
	}
	
	public int getMaterialIndex()
	{
		return _materialIndex;
	}
	
	public double getRadius()
	{
		return _radius;
	}

	@Override
	public Intersection checkIntersection(Ray ray) 
	{
		Vector3D L = _center.sub(ray.getP0());
		double tca = L.scalarProduct(ray.getV());
		
		if (tca < 0)
			return null;
		
		double dsquared = L.scalarProduct(L) - tca * tca;
		
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
		
		// TODO: if the camera is inside the sphere?
		
		return new Intersection(t, intersectionPoint, normal);
	}
}
