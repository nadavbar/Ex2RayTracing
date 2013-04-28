package RayTracing;

public class Settings 
{
	private Color _background;
	private int _shadowRays;
	private int _recursionLevel;
	
	public Settings(Color backColor, int shadowRays, int recursionLevel)
	{
		_background = backColor;
		_shadowRays = shadowRays;
		_recursionLevel = recursionLevel;
	}
	
	public Color getBackColor()
	{
		return _background;
	}
	
	public int getShadowRays()
	{
		return _shadowRays;
	}
	
	public int getRecursionLevel()
	{
		return _recursionLevel;
	}
}
