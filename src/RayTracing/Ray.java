package RayTracing;

public class Ray 
{
	private Vector3D _p0;
	private Vector3D _p;
	private Vector3D _v;
	
	public Ray(Vector3D p0, Vector3D p)
	{
		setP0(p0);
		setP(p);
	}

	/**
	 * @return the p0
	 */
	public Vector3D getP0() 
	{
		return _p0;
	}

	/**
	 * @param p0 the p0 to set
	 */
	public void setP0(Vector3D p0) 
	{
		_p0 = p0;
		
		if (_p != null)
		{
			_v = _p.sub(_p0);
		}
	}

	/**
	 * @return the p
	 */
	public Vector3D getP() 
	{
		return _p;
	}
	
	public Vector3D getV()
	{
		return _v;
	}

	/**
	 * @param _p the _p to set
	 */
	public void setP(Vector3D p) 
	{
		_p = p;
		
		if (_p != null)
		{
			_v = _p.sub(_p0);
		}
	}
}
