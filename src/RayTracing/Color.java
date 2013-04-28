package RayTracing;

public class Color 
{
	public Color(double red, double green, double blue)
	{
		_red = red;
		_green = green;
		_blue = blue;
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
	
	private double _red;
	private double _green;
	private double _blue;
}
