package RayTracing;

public class Sphere 
{
	public Sphere(double x, double y, double z, double radius, int materialIndex)
	{
		_centerX = x;
		_centerY = y;
		_centerZ = z;
		_radius = radius;
		_materialIndex = materialIndex;
	}
	
	private double _centerX;
	private double _centerY;
	private double _centerZ; 
	private double _radius;
	private int _materialIndex;
	
	public double getCenterX()
	{
		return _centerX;
	}
	
	public double getCenterY()
	{
		return _centerY;
	}
	
	public double getCenterZ()
	{
		return _centerZ;
	}
	
	public int getMaterialIndex()
	{
		return _materialIndex;
	}
	
	public double getRadius()
	{
		return _radius;
	}
}
