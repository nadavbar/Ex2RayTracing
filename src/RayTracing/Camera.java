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
	private Vector3D _coordinateSystemP0;
	
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
		_normal = _position.sub(_lookAt);
		_vz = _normal.normalize();
		_vx = _normal.crossProduct(_upVector).normalize();
		// TODO: normalize vy?
		_vy = _vz.crossProduct(_vx);
		Vector3D p = _position.add(_vz.multByScalar(_screenDistance));
		_coordinateSystemP0 = p.sub(_vx.sub(_vy).multByScalar(_screenWidth));
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
	

	public Vector3D getCoordinateSytstemP0()
	{
		return _coordinateSystemP0;
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
