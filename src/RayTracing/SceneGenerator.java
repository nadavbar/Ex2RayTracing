package RayTracing;

import java.util.ArrayList;
import java.util.Comparator;

public class SceneGenerator 
{
	private int _height;
	private int _width;
	private Camera _camera;
	private Settings _settings;
	private ArrayList<Material> _materials;
	private ArrayList<Surface> _surface;
	private ArrayList<Light> _lights;
	
	public SceneGenerator(Camera camera, Settings settings, ArrayList<Surface> surfaces, ArrayList<Material> materials,
						  ArrayList<Light> lights, int width, int height)
	{
		_height = height;;
		_width = width;
		_camera = camera;
		_settings = settings;
		_materials = materials;
		_surface = surfaces;
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
	
	class IntersectionComperator implements Comparator<Intersection>
	{
		/*
		 * (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Intersection o1, Intersection o2) 
		{
			return Double.compare(o1.getT(), o2.getT());
		}
	}
}

