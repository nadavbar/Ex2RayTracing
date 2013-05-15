package RayTracing;

/**
 * This class represents a ray, with a start position and a direction
 */
public class Ray 
{
	/** The initial point of the ray*/
	private Vector3D _p0;
	/** The target of the ray */
	private Vector3D _p;
	/** The direction of the ray*/
	private Vector3D _v;
	
	/**
	 * Creates a new ray
	 * @param p0 The initial poing
	 * @param p The target point
	 */
	public Ray(Vector3D p0, Vector3D p)
	{
		setP0(p0);
		setP(p);
	}
	
	/**
	 * Creates a new ray (used for copying)
	 * @param p0 The initial point
	 * @param p The target point
	 * @param v The direction
	 */
	public Ray(Vector3D p0, Vector3D p, Vector3D v) 
	{
		_p0 = p0;
		_p = p;
		_v = v;		
	}
	
	/**
	 * Creates a new ray
	 * @param p0 The initial poing
	 * @param p The target point
	 * @param epsilon The offset in which the start position will be set
	 */
	public Ray(Vector3D p0, Vector3D p, double epsilon)
	{
		setP0(p0);
		setP(p);
		p0 = p0.add(_v.multByScalar(epsilon));
	}

	/**
	 * @return the initial point
	 */
	public Vector3D getP0() 
	{
		return _p0;
	}

	/**
	 * @param p0 the initial point to set
	 */
	public void setP0(Vector3D p0) 
	{
		_p0 = p0;
		
		if (_p != null)
		{
			_v = _p.sub(_p0).normalize();
		}
	}

	/**
	 * @return the target of the ray
	 */
	public Vector3D getP() 
	{
		return _p;
	}
	
	/**
	 * 
	 * @return The direction
	 */
	public Vector3D getV()
	{
		return _v;
	}

	/**
	 * @param p the target to set
	 */
	public void setP(Vector3D p) 
	{
		_p = p;
		
		if (_p != null)
		{
			_v = _p.sub(_p0).normalize();
		}
	}
}
