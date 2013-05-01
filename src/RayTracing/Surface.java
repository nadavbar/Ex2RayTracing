package RayTracing;

public abstract class Surface
{
	protected static final double EPSILON = 0.000000001; 
	
	public abstract Intersection checkIntersection(Ray ray);
	
	private int _materialIndex;
	
	public Surface(int materialIndex)
	{
		_materialIndex = materialIndex;
	}
	
	public int getMaterialIndex()
	{
		return _materialIndex;
	}
}
