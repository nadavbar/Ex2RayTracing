package RayTracing;

public class Plain 
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
}
