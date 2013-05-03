package RayTracing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SceneGenerator 
{
	private int _height;
	private int _width;
	private Camera _camera;
	private Settings _settings;
	private ArrayList<Material> _materials;
	private ArrayList<Surface> _surfaces;
	private ArrayList<Light> _lights;
	
	public SceneGenerator(Camera camera, Settings settings, ArrayList<Material> materials, ArrayList<Surface> surfaces,
						  ArrayList<Light> lights, int width, int height)
	{
		_height = height;;
		_width = width;
		_camera = camera;
		_settings = settings;
		_materials = materials;
		_surfaces = surfaces;
		_lights = lights;
	}
	
	public Color[][] renderScene()
	{
		Color[][] imageData = new Color[_height][_width];
		
		Vector3D initial = _camera.getPosition().add(_camera.getVz().multByScalar(_camera.getScreenDistance()));
		double width = _camera.getScreenWidth();
		double density = _camera.getScreenWidth() / _width;
		double height = _height * (_camera.getScreenWidth() / _width);
		System.out.println("initial: " + initial.toString());
		Vector3D p0 = initial.sub(_camera.getVx().multByScalar(width/2)).sub(_camera.getVy().multByScalar(height/2));
		System.out.println("p0: " + p0.toString());
		Vector3D xStep = _camera.getVx().multByScalar(density);
		Vector3D yStep = _camera.getVy().multByScalar(density);
		for (int i=0; i<_height; i++)
		{
			Vector3D p = p0;
			for (int j=0; j<_width; j++)
			{
				Ray ray = new Ray(_camera.getPosition(), p);
				ArrayList<Intersection> intersections = getIntersectionsSorted(ray);
				Color color = getColor(intersections);
				imageData[i][j] = color;
				p = p.add(xStep);
			}
			p0 = p0.add(yStep);
		}
		
		return imageData;
	}
	
	public ArrayList<Intersection> getIntersectionsSorted(Ray ray)
	{
		ArrayList<Intersection> intersections = new ArrayList<Intersection>();
		
		for (Surface surface : _surfaces)
		{
			Intersection intersection = surface.checkIntersection(ray);
			if (intersection != null)
			{
				intersections.add(intersection);
			}
		}
		
		Collections.sort(intersections, new IntersectionComperator());
		return intersections;
	}
	
	// Assume the intersections are sorted
	private Color getColor(ArrayList<Intersection> intersections)
	{
		if (intersections.size() == 0)
		{
			return _settings.getBackColor();
		}
		
		Intersection first = intersections.get(0);
		
		Color color = new Color(0d, 0d, 0d);
		
		if (first.getSurface().getClass() == Sphere.class)
		{
			int a = 10;
		}
		
		for (Light lgt : _lights)
		{
			Color diffuse = getColorFromLight(first, lgt);
			color.add(diffuse);
		}
		
		// TODO: handle transperancy, lighting
		return color;
	}
	
	private Color getColorFromLight(Intersection intersection, Light light)
	{
		Vector3D normal = intersection.getNormal();
		// TODO: check if the ray is hidden from another object! use P0 as the position + epsilon
		// Should we also do hard shadows? or only soft shadows?
		Vector3D l = light.getPosition().sub(intersection.getIntersectionPoint()).normalize();
		double cosTheta = normal.scalarProduct(l);
		
		Material material = getMaterialForSurface(intersection.getSurface());
		
		// Calculate diffuse:
		double ired = material.getDiffuse().getRed() * light.getColor().getRed() * cosTheta;
		double igreen = material.getDiffuse().getGreen() * light.getColor().getGreen() * cosTheta;
		double iblue = material.getDiffuse().getBlue() * light.getColor().getBlue() * cosTheta;
		
		// calculate specular:
		Vector3D v = _camera.getPosition().sub(intersection.getIntersectionPoint()).normalize();
		Vector3D h = l.add(v).normalize();
		double cosPhiPowered = Math.pow(h.scalarProduct(normal),material.getPhongCoeff());
		
		ired += material.getSpecular().getRed() * light.getSpecular() * cosPhiPowered;
		igreen += material.getSpecular().getGreen() * light.getSpecular() * cosPhiPowered;
		iblue += material.getSpecular().getBlue() * light.getSpecular() * cosPhiPowered;
		
		return new Color(ired, igreen, iblue);
	}
	
	public Material getMaterialForSurface(Surface surface)
	{
		// we assume that everything is valid and the material exists
		int materialIndex = surface.getMaterialIndex();
		return _materials.get(materialIndex-1);		
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

