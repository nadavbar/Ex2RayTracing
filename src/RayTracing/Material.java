package RayTracing;

public class Material 
{
	private Color _reflectionColor;
	private Color _diffuseColor;
	private Color _specularColor;
	private double _phongCoeff;
	private double _transperancy;
	private double _incidence;
	
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
	
	public Color getReflection()
	{
		return _reflectionColor;
	}
	
	public Color getDiffuse()
	{
		return _diffuseColor;
	}
	
	public Color getSpecular()
	{
		return _specularColor;
	}
	
	public double getPhongCoeff()
	{
		return _phongCoeff;
	}
	
	public double getTransperancy()
	{
		return _transperancy;
	}
	
	public double getIncidence()
	{
		return _incidence;
	}
	
}
