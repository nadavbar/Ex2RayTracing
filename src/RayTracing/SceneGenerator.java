package RayTracing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class SceneGenerator 
{
	private static final double EPSILON = 0.000000001;
	private int _height;
	private int _width;
	private Camera _camera;
	private Settings _settings;
	private ArrayList<Material> _materials;
	private ArrayList<Surface> _surfaces;
	private ArrayList<Light> _lights;
	private double _density;
	private Random _random;
	
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
		_density = _camera.getScreenWidth() / _width;
		_random = new Random();
	}
	
	public Color[][] renderScene()
	{
		Color[][] imageData = new Color[_height][_width];
		
		ViewPlane cameraViewPlane = new ViewPlane(_camera.getNormal(), _camera.getUpVector());
		Vector3D initial = _camera.getPosition().add(cameraViewPlane.getVz().multByScalar(_camera.getScreenDistance()));
		double width = _camera.getScreenWidth();
		double height = _height * _density;
		Vector3D p0 = initial.sub(cameraViewPlane.getVx().multByScalar(width/2)).sub(cameraViewPlane.getVy().multByScalar(height/2));
		Vector3D xStep = cameraViewPlane.getVx().multByScalar(_density);
		Vector3D yStep = cameraViewPlane.getVy().multByScalar(_density);
		for (int i=0; i<_height; i++)
		{
			Vector3D p = p0;
			for (int j=0; j<_width; j++)
			{
				Ray ray = new Ray(_camera.getPosition(), p);
				ArrayList<Intersection> intersections = getIntersectionsSorted(ray);
				Color color = getColor(intersections, 0);
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
	private Color getColor(ArrayList<Intersection> intersections, int generation)
	{
		if (intersections.size() == 0)
		{
			return _settings.getBackColor();
		}
		
		Intersection first = intersections.get(0);
		Material material = getMaterialForSurface(first.getSurface());
		
		Color color = new Color(0d, 0d, 0d);
		
		for (Light lgt : _lights)
		{
			Color lgtColor = getColorFromLight(first, lgt);
			lgtColor.multipy(1 - material.getTransperancy());
			color.add(lgtColor);
		}
		
		ArrayList<Intersection> nextIntersections = new ArrayList<Intersection>(intersections);
		nextIntersections.remove(0);
		Color transparancyColor = new Color(getColor(nextIntersections, generation));
		transparancyColor.multipy(material.getTransperancy());
		color.add(transparancyColor);
		
		//reflection handling
		if (generation < _settings.getRecursionLevel())
		{
			//R = V-2(V dotproduct N)N
			Vector3D V= first.getRay().getV();
			Vector3D N = first.getNormal();
			Vector3D reflectionAngle = V.sub(N.multByScalar(2*(V.dotProduct(N))));
			Ray reflectionRay = new Ray(first.getIntersectionPoint(), first.getIntersectionPoint(), reflectionAngle);
			ArrayList<Intersection> reflectionIntersections = getIntersectionsSorted(reflectionRay);
			
			Color reflectionColor = getColor(reflectionIntersections, ++generation);
			//it's better that this recursion level has its own copy of the object:
			reflectionColor = new Color(reflectionColor);  			
			reflectionColor.multipy(material.getReflection());
			color.add(reflectionColor);
					
		}
		
		// TODO: handle transperancy, lighting
		return color;
	}
	
	private Color getColorFromLight(Intersection intersection, Light light)
	{
		Vector3D normal = intersection.getNormal().normalize();
		// TODO: check if the ray is hidden from another object! use P0 as the position + epsilon
		// Should we also do hard shadows? or only soft shadows?
		Vector3D l = light.getPosition().sub(intersection.getIntersectionPoint()).normalize();
		double cosTheta = normal.dotProduct(l);
		
		if (cosTheta < EPSILON)
		{
			return new Color(0,0,0);
		}
		
		Material material = getMaterialForSurface(intersection.getSurface());
		
		// Calculate diffuse:
		double ired = material.getDiffuse().getRed() * light.getColor().getRed() * cosTheta;
		double igreen = material.getDiffuse().getGreen() * light.getColor().getGreen() * cosTheta;
		double iblue = material.getDiffuse().getBlue() * light.getColor().getBlue() * cosTheta;
		
		// calculate specular:
		Vector3D v = intersection.getRay().getV().multByScalar(-1);
		Vector3D r = (normal.multByScalar (2 * l.dotProduct(normal)).sub(l)).normalize();
		double cosPhi = r.dotProduct(v);
		
		if (cosPhi > EPSILON) 
		{
			double cosPhiPowered = Math.pow(cosPhi,material.getPhongCoeff());
			ired += material.getSpecular().getRed() * light.getSpecular() * cosPhiPowered;
			igreen += material.getSpecular().getGreen() * light.getSpecular() * cosPhiPowered;
			iblue += material.getSpecular().getBlue() * light.getSpecular() * cosPhiPowered;
		}
		
		// shadows:
		double shadow = calculateSoftShadow(l, intersection, light);
		ired *= shadow;
		igreen *= shadow;
		iblue *= shadow;
		
		return new Color(ired, igreen, iblue);
	}
	
	public double calculateSoftShadow(Vector3D lightVector, Intersection intersection, Light lgt)
	{
		// if n=1, treat this as hard shadows
		if (_settings.getShadowRays() == 1)
		{
			return 1.0 -
					(lgt.getShadow() * 
					(1.0 - rayHits(lgt.getPosition(), intersection.getIntersectionPoint(), intersection.getSurface())));
		}
		else 
		{
			return 1.0;
		}
		/*
		ViewPlane lightPlane = new ViewPlane(lightVector, _camera.getUpVector());
		Vector3D vx = lightPlane.getVx();
		Vector3D vy = lightPlane.getVy();
		
		Vector3D startPoint = lgt.getPosition().sub(vx.multByScalar(lgt.getLightRadius()/2)).sub(vy.multByScalar(lgt.getLightRadius()));
		
		// TODO: multiply in density?
		double stepSize = (lgt.getLightRadius() / _settings.getShadowRays()) * _density;
		Vector3D xStep = vx.multByScalar(stepSize);
		Vector3D yStep = vy.multByScalar(stepSize);
		Vector3D yPosition = startPoint;
		for (int i=0; i< _settings.getShadowRays(); i++)
		{
			Vector3D xPosition = yPosition;
			for (int j=0; j< _settings.getShadowRays(); j++)
			{
				double xRand = _random.nextDouble();
				double yRand = _random.nextDouble();
				
				Vector3D point = xPosition.add(xPosition.multByScalar(xRand)).add(yPosition.multByScalar(yRand));
				
				// TODO: check if the light hits the object from this point 
			}
			yPosition = yPosition.add(yStep);
		}
		// TODO: should we multiply the light vector by -1?
		// TODO: if the number of rays is only 1, then we should check from the center.
		// not from the start of the axis
		return 0;*/
	}
	
	private double rayHits(Vector3D origin, Vector3D point, Surface surface)
	{
		Ray ray = new Ray(origin, point);
		
		ArrayList<Intersection> intersections = getIntersectionsSorted(ray);
		
		// This shouldn't happen, since we shoot a ray to the ball..
		if (intersections.get(0).getSurface() == surface)
		{
			return 1.0;
		}
		
		return 0.0;
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

