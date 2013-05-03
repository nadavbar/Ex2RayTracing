package RayTracing;

public class Light 
{
	private Color _color;
	private Vector3D _position;
	private double _specular;
	private double _shadow;
	private double _lightRadius;
	
	public Light(Vector3D position, Color color, double specular, double shadow,
				 double lightRadius)
	{
		_color = color;
		_position = position;
		_specular = specular;
		_shadow = shadow;
		_lightRadius = lightRadius;
	}
	
	public Color getColor()
	{
		return _color;
	}
	
	public Vector3D getPosition()
	{
		return _position;
	}
	
	public double getSpecular()
	{
		return _specular;
	}
	
	public double getShadow()
	{
		return _shadow;
	}
	
	public double getLightRadius()
	{
		return _lightRadius;
	}
}
