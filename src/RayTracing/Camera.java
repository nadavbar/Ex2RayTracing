package RayTracing;

public class Camera 
{
	private Vector3D _position;
	private Vector3D _lookAt;
	private Vector3D _upVector;
	private double _screenDistance;
	private double _screenWidth;
	
	public Camera(Vector3D position, Vector3D lookAt, Vector3D upVector,
				  double screenDistance, double screenWidth)
	{
		_position = position;
		_lookAt = lookAt;
		_upVector = upVector;
		_screenDistance = screenDistance;
		_screenWidth = screenWidth;
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
	
}
