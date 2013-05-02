package RayTracing;

public class Camera 
{
	private Vector3D _position;
	private Vector3D _lookAt;
	private Vector3D _upVector;
	private double _screenDistance;
	private double _screenWidth;
	
	private Vector3D _normal;
	private Vector3D _vz;
	private Vector3D _vy;
	private Vector3D _vx;
	
	public Camera(Vector3D position, Vector3D lookAt, Vector3D upVector,
				  double screenDistance, double screenWidth)
	{
		_position = position;
		_lookAt = lookAt;
		_upVector = upVector;
		_screenDistance = screenDistance;
		_screenWidth = screenWidth;
		calculateCoordinateSystem();
	}
	
	private void calculateCoordinateSystem()
	{
		System.out.println("camera: " + _position.toString());
		_normal = _lookAt.sub(_position).normalize();
		_vz = _normal.normalize();
		System.out.println("vz: " + _vz.toString());
		_vx = _vz.crossProduct(_upVector);
		System.out.println("vx: " + _vx.toString());
		_vy = _vx.crossProduct(_vz);
		System.out.println("vy: " + _vy.toString());
	}
	
	public Vector3D getPosition()
	{
		return _position;
	}
	
	public Vector3D getLookAt()
	{
		return _lookAt;
	}
	
	public Vector3D getUpVector()
	{
		return _upVector;
	}
	
	public double getScreenDistance()
	{
		return _screenDistance;
	}
	
	public double getScreenWidth()
	{
		return _screenWidth;
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
