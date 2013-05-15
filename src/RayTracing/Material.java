package RayTracing;

/**
 * The Material object
 */
public class Material 
{
	/** The reflection color*/
	private Color _reflectionColor;
	/** The diffuse color*/
	private Color _diffuseColor;
	/** The specular color*/
	private Color _specularColor;
	/** The phong coefficient*/
	private double _phongCoeff;
	/** The transparency level */
	private double _transperancy;
	/** The incidence parameter*/
	private double _incidence;
	
	/**
	 * Creates a new material object
	 * @param diffuse The diffuse color
	 * @param specular The specular color
	 * @param reflection The reflection color
	 * @param phong The phong coefficient
	 * @param transperancy The transparency level
	 * @param incidence The incidence parameter
	 */
	public Material(Color diffuse, 
			Color specular, Color reflection, double phong, double transperancy, double incidence)
	{
		_reflectionColor = reflection;
		_diffuseColor = diffuse;
		_specularColor = specular;
		_phongCoeff = phong;
		_transperancy = transperancy;
		_incidence = incidence;
	}
	
	/**
	 * 
	 * @return The reflection color
	 */
	public Color getReflection()
	{
		return _reflectionColor;
	}
	
	/**
	 * 
	 * @return The diffuse color
	 */
	public Color getDiffuse()
	{
		return _diffuseColor;
	}
	
	/**
	 * 
	 * @return The specular color
	 */
	public Color getSpecular()
	{
		return _specularColor;
	}
	
	/**
	 * 
	 * @return The phong coefficient
	 */
	public double getPhongCoeff()
	{
		return _phongCoeff;
	}
	
	/**
	 * 
	 * @return The transparency level
	 */
	public double getTransperancy()
	{
		return _transperancy;
	}
	
	/**
	 * 
	 * @return The incidence parameter
	 */
	public double getIncidence()
	{
		return _incidence;
	}
	
}
