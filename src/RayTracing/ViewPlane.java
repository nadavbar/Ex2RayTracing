package RayTracing;

/**
 * Represents a view plane. Calculates the coordinate system according to the plane
 */
public class ViewPlane 
{
	/** The normal to the plane*/
	private Vector3D _normal;
	/** The up vector */
	private Vector3D _upVector;
	/** The x-axis vector for the place*/
	private Vector3D _vx;
	/** The z-axis vector for the plane*/
	private Vector3D _vz;
	/** The y axis vector of the plane*/
	private Vector3D _vy;
	
	/**
	 * Creates a new ViewPlane object
	 * @param normal The normal of the plane
	 * @param upVector The up vector
	 */
	public ViewPlane(Vector3D normal, Vector3D upVector)
	{
		_normal = normal;
		_upVector = upVector;
		_vz = _normal.normalize();
		_vx = _upVector.crossProduct(_vz);
		_vy = _vx.crossProduct(_vz);
	}
	
	/**
	 * 
	 * @return The normal of the plane
	 */
	public Vector3D getNormal()
	{
		return _normal;
	}
	
	/**
	 * 
	 * @return The up vector for the plane
	 */
	public Vector3D getUpVector()
	{
		return _upVector;
	}
	
	/**
	 * 
	 * @return The z-axis component
	 */
	public Vector3D getVz()
	{
		return _vz;
	}
	
	/**
	 * 
	 * @return The x-axis component
	 */
	public Vector3D getVx()
	{
		return _vx;
	}
	
	/**
	 * 
	 * @return The y-axis component
	 */
	public Vector3D getVy()
	{
		return _vy;
	}

}
