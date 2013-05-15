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
	private double _numberOfShadowRays;
	private double [][][] _randArray;
	
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
		_numberOfShadowRays = _settings.getShadowRays()*_settings.getShadowRays();
		_randArray = new double[_settings.getShadowRays()][_settings.getShadowRays()][2];
		for (int i=0; i < _settings.getShadowRays(); i++)
		{
			for (int j=0; j < _settings.getShadowRays(); j++)
			{
				_randArray[i][j][0] = _random.nextDouble();
				_randArray[i][j][1] = _random.nextDouble();
			}
		}
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
			lgtColor = lgtColor.multiply(1 - material.getTransperancy());
			
			color = color.add(lgtColor);
		}
		
		//add color from transparency
		color = color.add(getTransparencyColor(intersections, generation, material));
		
		//add color from reflections
		if (generation < _settings.getRecursionLevel())
		{
			color = color.add(getReflectionsColor(first, generation, material));
		}
		
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
			ired += material.getSpecular().getRed() * light.getSpecular() *  cosPhiPowered;
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
					(1.0 - rayHits(intersection.getIntersectionPoint(), lgt.getPosition(), intersection.getSurface())));
		}
		
		ViewPlane lightPlane = new ViewPlane(lightVector, _camera.getUpVector());
		Vector3D vx = lightPlane.getVx();
		Vector3D vy = lightPlane.getVy();
		
		Vector3D startPoint = lgt.getPosition().sub(vx.multByScalar(lgt.getLightRadius()*2)).sub(vy.multByScalar(lgt.getLightRadius()*2));
		
		// TODO: multiply in density?
		double stepSize = (lgt.getLightRadius()*4 / _settings.getShadowRays());
		Vector3D xStep = vx.multByScalar(stepSize);
		Vector3D yStep = vy.multByScalar(stepSize);
		Vector3D yPosition = startPoint;
		double numberOfHits = 0d;
		
		for (int i=0; i< _settings.getShadowRays(); i++)
		{
			Vector3D xPosition = yPosition;
			for (int j=0; j< _settings.getShadowRays(); j++)
			{
				double xRand = _random.nextDouble();
				double yRand = _random.nextDouble();
				
				Vector3D point = xPosition.add(xStep.multByScalar(xRand)).add(yStep.multByScalar(yRand));
				
				// TODO: check if the light hits the object from this point
				numberOfHits += rayHits(intersection.getIntersectionPoint(),point, intersection.getSurface());
				xPosition = xPosition.add(xStep);
			}
			yPosition = yPosition.add(yStep);
		}
		
		double nonHitPrecentage = 1 - (numberOfHits / (_numberOfShadowRays));
		
		return 1.0 - (nonHitPrecentage * lgt.getShadow());
		// TODO: should we multiply the light vector by -1?
		// TODO: if the number of rays is only 1, then we should check from the center.
		// not from the start of the axis
	}
	
	private double rayHits(Vector3D origin, Vector3D point, Surface surface)
	{
		Ray ray = new Ray(origin, point, EPSILON);
		
		ArrayList<Intersection> intersections = getIntersectionsSorted(ray);
		
		if (intersections.size() == 0 /*|| intersections.get(0).getSurface() == surface*/)
		{
			return 1.0;
		}
		
		Intersection first = intersections.get(0);
		double distanceFromOther = first.getIntersectionPoint().sub(point).size();
		double distanceFromSurface = origin.sub(point).size();
		
		if (distanceFromOther > distanceFromSurface)
		{
			return 1.0;
		}
		else
		{ //There are objects between the origin and the (light) point
			double totalTransperancy = 1.0;
			for (Intersection intersection: intersections)
			{
				Material material = getMaterialForSurface(intersection.getSurface());
				totalTransperancy *= material.getTransperancy();
			}
			return totalTransperancy;
		}
	}
	
	private Color getTransparencyColor(ArrayList<Intersection> intersections, int generation, Material material)
	{
		ArrayList<Intersection> nextIntersections = new ArrayList<Intersection>(intersections);
		Intersection currentIntersection = nextIntersections.remove(0);
		Color transparencyColor = new Color(getColor(nextIntersections, generation));
		transparencyColor = transparencyColor.multiply(material.getTransperancy());
		
		//bonus feature
		//new_transparency = (1 - MI) * transparency + MI * IV * transparency
		double materialIncidence = material.getIncidence();
		double incidenceValue = -1 * currentIntersection.getRay().getV().dotProduct(currentIntersection.getNormal());
		
		Color newTransparency = transparencyColor.multiply(1-materialIncidence).add(transparencyColor.multiply(materialIncidence * incidenceValue));
		
		return newTransparency;
		
	}
	
	private Color getReflectionsColor(Intersection intersection, int generation, Material material)
	{
		//R = V-2(V dotproduct N)N
		Vector3D V= intersection.getRay().getV();
		Vector3D N = intersection.getNormal();
		Vector3D reflectionAngle = V.sub(N.multByScalar(2*(V.dotProduct(N))));
		Ray reflectionRay = new Ray(intersection.getIntersectionPoint(), intersection.getIntersectionPoint(), reflectionAngle);
		ArrayList<Intersection> reflectionIntersections = getIntersectionsSorted(reflectionRay);
		
		//it's better that this recursion level has its own copy of the object
		Color reflectionColor = new Color(getColor(reflectionIntersections, ++generation));							
		reflectionColor = reflectionColor.multiply(material.getReflection());
		
		//bonus feature
		//new_reflection = (1 - MI) * reflection_color + MI * (1 - IV) * reflecion_color
		double materialIncidence = material.getIncidence();
		double incidenceValue = -1 * intersection.getRay().getV().dotProduct(intersection.getNormal());
		
		Color newReflection = reflectionColor.multiply(1-materialIncidence).add(reflectionColor.multiply(materialIncidence*(1 - incidenceValue)));
		
		return newReflection;
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

