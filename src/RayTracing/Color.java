package RayTracing;

public class Color 
{
	public Color(double red, double green, double blue)
	{
		_red = normalize(red);
		_green = normalize(green);
		_blue = normalize(blue);
	}
	
	public Color(Color other)
	{
		_red = other._red;
		_blue = other._blue;
		_green = other._green;		
	}
	
	public double getRed()
	{
		return _red;
	}
	
	public double getGreen()
	{
		return _green;
	}
	
	public double getBlue()
	{
		return _blue;
	}
	
	public Color add(Color other)
	{
		return new Color(normalize(_red + other._red), normalize(_green + other._green), normalize(_blue + other._blue));
	}
	
	public Color multiply(Color other)
	{
		return new Color(normalize(_red * other._red), normalize(_green * other._green), normalize(_blue * other._blue));		
	}
	
	public Color multiply(double coefficient)
	{
		return new Color(normalize(_red * coefficient), normalize(_green * coefficient), normalize(_blue * coefficient));		
	}
	
	private double normalize(double value)
	{
		return Math.max(0d, Math.min(1.0, value));
	}
	
	public String toString()
	{
		return String.format("(%.2f,%.2f,%.2f)", _red,_blue,_green); 
	} 
	
	private double _red;
	private double _green;
	private double _blue;
}
