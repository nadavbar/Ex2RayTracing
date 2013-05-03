package RayTracing;

public class ViewPlane 
{
	private Vector3D _normal;
	private Vector3D _upVector;
	private Vector3D _vx;
	private Vector3D _vz;
	private Vector3D _vy;
	
	public ViewPlane(Vector3D normal, Vector3D upVector)
	{
		_normal = normal;
		_upVector = upVector;
		_vz = _normal.normalize();
		_vx = _upVector.crossProduct(_vz);
		_vy = _vx.crossProduct(_vz);
	}
	
	public Vector3D getNormal()
	{
		return _normal;
	}
	
	public Vector3D getUpVector()
	{
		return _upVector;
	}
	
	public Vector3D getVz()
	{
		return _vz;
	}
	
	public Vector3D getVx()
	{
		return _vx;
	}
	
	public Vector3D getVy()
	{
		return _vy;
	}

}
