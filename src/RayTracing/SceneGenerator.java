package RayTracing;

import java.util.ArrayList;

public class SceneGenerator 
{
	private int _height;
	private int _width;
	private Camera _camera;
	private Settings _settings;
	private ArrayList<Material> _materials;
	private ArrayList<Sphere> _spheres;
	private ArrayList<Plain> _plains;
	private ArrayList<Light> _lights;
	
	public SceneGenerator(Camera camera, Settings settings, ArrayList<Material> materials,
						  ArrayList<Sphere> spheres, ArrayList<Plain> plains, ArrayList<Light> lights, 
						  int width, int height)
	{
		_height = height;;
		_width = width;
		_camera = camera;
		_settings = settings;
		_materials = materials;
		_spheres = spheres;
		_plains = plains;
		_lights = lights;
	}
	
	public Color[][] renderScene()
	{
		Color[][] imageData = new Color[_height][_width];
		Vector3D p0 = _camera.getCoordinateSytstemP0();
		
		for (int i=0; i<_height; i++)
		{
			Vector3D p = p0;
			for (int j=0; j<_width; j++)
			{
				Ray ray = new Ray(_camera.getPosition(), p);
				ArrayList<Intersection> intersections = getIntersections(ray);
				Color color = getColor(intersections);
				imageData[i][j] = color;
				p = p.add(_camera.getVx());
			}
			// TODO: should we normalize py???
			p0 = p0.add(_camera.getVy());
		}
		
		return imageData;
	}
	
	public ArrayList<Intersection> getIntersections(Ray ray)
	{
		return null;
	}
	
	public Color getColor(ArrayList<Intersection> intersections)
	{
		return null;
	}
}
