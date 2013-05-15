package RayTracing;

/**
 * A Light object.
 */
public class Light 
{
	/** The light color*/
	private Color _color;
	/** The light position */
	private Vector3D _position;
	/** The specular parameter of the light */
	private double _specular;
	/** The shadow intensity of the light */
	private double _shadow;
	/** The light radius*/
	private double _lightRadius;
	
	/**
	 * Creates a new light object
	 * @param position The light position
	 * @param color The light color
	 * @param specular The specular parameter of the light
	 * @param shadow The shadow intensity of the light
	 * @param lightRadius The light radius
	 */
	public Light(Vector3D position, Color color, double specular, double shadow,
				 double lightRadius)
	{
		_color = color;
		_position = position;
		_specular = specular;
		_shadow = shadow;
		_lightRadius = lightRadius;
	}
	
	/**
	 * 
	 * @return The light color
	 */
	public Color getColor()
	{
		return _color;
	}
	
	/**
	 * 
	 * @return the light position
	 */
	public Vector3D getPosition()
	{
		return _position;
	}
	
	/**
	 * 
	 * @return The specular parameter of the light
	 */
	public double getSpecular()
	{
		return _specular;
	}
	
	/**
	 * 
	 * @return The shadow intensity of the light
	 */
	public double getShadow()
	{
		return _shadow;
	}
	
	/**
	 * 
	 * @return The light radius
	 */
	public double getLightRadius()
	{
		return _lightRadius;
	}
}
