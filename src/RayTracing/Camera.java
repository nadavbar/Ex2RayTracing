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
		//_normal = _position.sub(_lookAt);
		System.out.println("camera: " + _position.toString());
		_normal = _lookAt.sub(_position).normalize();
		_vz = _normal.normalize();
		System.out.println("vz: " + _vz.toString());
		_vx = _upVector.crossProduct(_normal).normalize();
		System.out.println("vx: " + _vx.toString());
		// TODO: normalize vy?
		_vy = _vz.crossProduct(_vx).normalize();
		System.out.println("vy: " + _vy.toString());
		/*Vector3D p = _position.add(_vz.multByScalar(_screenDistance));
		System.out.println("p: " + p.toString());
		_coordinateSystemP0 = p.sub(_vx.multByScalar(_screenWidth/2)).sub(_vy.multByScalar(_screenWidth/2));
		System.out.println("p0: " + _coordinateSystemP0.toString());*/
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
