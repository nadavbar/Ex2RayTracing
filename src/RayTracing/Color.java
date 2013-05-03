package RayTracing;

public class Color 
{
	public Color(double red, double green, double blue)
	{
		_red = normalize(red);
		_green = normalize(green);
		_blue = normalize(blue);
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
	
	public void add(Color other)
	{
		_red = normalize(_red + other._red);
		_blue = normalize(_blue + other._blue);
		_green = normalize(_green + other._green);
	}
	
	private double normalize(double value)
	{
		return Math.max(0d, Math.min(1.0, value));
	}
	
	private double _red;
	private double _green;
	private double _blue;
}
