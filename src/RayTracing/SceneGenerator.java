package RayTracing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * The main class which generates the scene image
 */
public class SceneGenerator 
{
	/** An epsilon value used for the computatoin of soft shadows*/
	private static final double EPSILON = 0.00001;
	/** The scene height in pixels */
	private int _height;
	/** The scene width in pixels */
	private int _width;
	/** The camera object */
	private Camera _camera;
	/** The scene settings */
	private Settings _settings;
	/** The list of materials used in the scene */
	private ArrayList<Material> _materials;
	/** The list of surfaces used in the scene */
	private ArrayList<Surface> _surfaces;
	/** The list of lights used in the scene */
	private ArrayList<Light> _lights;
	/** The pixel-to-point ratio*/
	private double _pixelPointRatio;
	/** The ransom object used for generating soft shadows **/
	private Random _random;
	/** The total number of shadow rays for each light*/
	private double _numberOfShadowRays;	
	
	/**
	 * Creates a new scene generator object
	 * @param camera The camera
	 * @param settings The scene settings
	 * @param materials The list of materials for the scene
	 * @param surfaces The list of surfaces for the scene
	 * @param lights The list of lights for the scene
	 * @param width The scene width, in pixels
	 * @param height The scene height, in pixels
	 */
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
		_pixelPointRatio = _camera.getScreenWidth() / (double)_width;
		_random = new Random();
		_numberOfShadowRays = _settings.getShadowRays()*_settings.getShadowRays();
	}
	
	/**
	 * The main loop. Renders the scene and returns a multidimensional array color objects containing the image
	 * @return The image data
	 */
	public Color[][] renderScene()
	{
		Color[][] imageData = new Color[_height][_width];
		
		// calculate the camera view plane and start poing: 
		ViewPlane cameraViewPlane = new ViewPlane(_camera.getNormal(), _camera.getUpVector());
		Vector3D initial = _camera.getPosition().add(cameraViewPlane.getVz().multByScalar(_camera.getScreenDistance()));
		double width = _camera.getScreenWidth();
		double height = (double)_height * _pixelPointRatio;
		Vector3D p0 = initial.sub(cameraViewPlane.getVx().multByScalar(width/2)).sub(cameraViewPlane.getVy().multByScalar(height/2));
		Vector3D xStep = cameraViewPlane.getVx().multByScalar(_pixelPointRatio);
		Vector3D yStep = cameraViewPlane.getVy().multByScalar(_pixelPointRatio);
		
		// The main loop: 
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
	
	/**
	 * Gets the list of intersections for the given ray, sorted by the distane from the ray origin
	 * @param ray The ray
	 * @return The list of intersections for the given ray, sorted by the distane from the ray origin
	 */
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
	
	/**
	 * Calculates the color of the point, given the list of intersections
	 * @param intersections Sorted list of intersections
	 * @param recursionLevel The current recusrion level
	 * @return The color of the point
	 */
	private Color getColor(ArrayList<Intersection> intersections, int recursionLevel)
	{
		if (intersections.size() == 0)
		{
			return _settings.getBackColor();
		}
		
		Intersection first = intersections.get(0);
		Material material = getMaterialForSurface(first.getSurface());
		
		Color color = new Color(0d, 0d, 0d);
		
		// Add color from each light
		for (Light lgt : _lights)
		{
			Color lgtColor = getColorFromLight(first, lgt);
			lgtColor = lgtColor.multiply(1 - material.getTransperancy());
			
			color = color.add(lgtColor);
		}
		
		//add color from transparency
		color = color.add(getTransparencyColorWithIncidence(intersections, recursionLevel, material));
		
		//add color from reflections
		if (recursionLevel < _settings.getRecursionLevel())
		{
			color = color.add(getReflectionsColorWithIncidence(first, recursionLevel, material));
		}
		
		return color;
	}
	
	/**
	 * Gets the color that the given light contributes to the point
	 * @param intersection The intersection
	 * @param light The light
	 * @return The color that the light contiributes to the intersection
	 */
	private Color getColorFromLight(Intersection intersection, Light light)
	{
		Vector3D normal = intersection.getNormal().normalize();
		Vector3D l = light.getPosition().sub(intersection.getIntersectionPoint()).normalize();
		double cosTheta = normal.dotProduct(l);
		
		// if the light does not hit the surface in a visible angle
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
		
		if (cosPhi > 0) 
		{			
			double cosPhiPowered = Math.pow(cosPhi,material.getPhongCoeff());
			ired += material.getSpecular().getRed() * light.getSpecular() *  cosPhiPowered * light.getColor().getRed();
			igreen += material.getSpecular().getGreen() * light.getSpecular() * cosPhiPowered * light.getColor().getGreen();
			iblue += material.getSpecular().getBlue() * light.getSpecular() * cosPhiPowered * light.getColor().getBlue();
		}
		
		// shadows:
		double shadow = calculateSoftShadow(l.multByScalar(-1), intersection, light);
		ired *= shadow;
		igreen *= shadow;
		iblue *= shadow;
		
		return new Color(ired, igreen, iblue);
	}
	
	/**
	 * Calculates soft shadows on the intersection point
	 * @param lightVector The light vector to use as a normal for the plane
	 * @param intersection The intersection that the shadow will be calculated for
	 * @param lgt The light to compute the shadow for
	 * @return The amount of shadow for the given point
	 */
	public double calculateSoftShadow(Vector3D lightVector, Intersection intersection, Light lgt)
	{
		// if n=1, treat this as hard shadows
		if (_settings.getShadowRays() == 1)
		{
			return 1.0 -
					(lgt.getShadow() * 
					(1.0 - lightRayHits(intersection.getIntersectionPoint(), lgt.getPosition(), intersection.getSurface())));
		}
		
		// Create the "area light" by creating a plane and running in the boundaries of a square
		
		ViewPlane lightPlane = new ViewPlane(lightVector,_camera.getUpVector());
		
		Vector3D vx = lightPlane.getVx();
		Vector3D vy = lightPlane.getVy();
		
		Vector3D startPoint = lgt.getPosition().sub(vx.multByScalar(lgt.getLightRadius()/2.0)).sub(vy.multByScalar(lgt.getLightRadius()/2.0));
		
		double stepSize = (lgt.getLightRadius() / (double)_settings.getShadowRays());
		Vector3D xStep = vx.multByScalar(stepSize);
		Vector3D yStep = vy.multByScalar(stepSize);
		Vector3D yPosition = startPoint;
		double numberOfHits = 0d;
		
		// run inside the square:
		
		for (int i=0; i< _settings.getShadowRays(); i++)
		{
			Vector3D xPosition = yPosition;
			for (int j=0; j< _settings.getShadowRays(); j++)
			{
				// use random values to find the point of light
				double xRand = _random.nextDouble();
				double yRand = _random.nextDouble();
					
				Vector3D point = xPosition.add(xStep.multByScalar(xRand)).add(yStep.multByScalar(yRand));
				
				numberOfHits += lightRayHits(intersection.getIntersectionPoint(),point, intersection.getSurface());
				xPosition = xPosition.add(xStep);
			}
			yPosition = yPosition.add(yStep);
		}
		
		double nonHitPrecentage = 1 - ((double)numberOfHits / ((double)_numberOfShadowRays));
		
		return 1.0 - (nonHitPrecentage * lgt.getShadow());
	}
	
	/**
	 * Checks whether the light ray hits the surface or it have other surfaces in it's way
	 * @param surfacePoint The surface position
	 * @param lightPoint The light position
	 * @param surface The surface
	 * @return 1.0 if the light hits the surface directly, 0 if it's hidden 
	 * a value between 0-1.0 if the surface is hidden by transperant objects
	 */
	private double lightRayHits(Vector3D surfacePoint, Vector3D lightPoint, Surface surface)
	{
		Ray ray = new Ray(lightPoint, surfacePoint);
		
		ArrayList<Intersection> intersections = getIntersectionsSorted(ray);
		
		// In case some floating point errors missed the intersection
		if (intersections.size() == 0)
		{
			return 1.0;
		}
		
		Intersection first = intersections.get(0);
		double distanceFromOther = first.getIntersectionPoint().sub(lightPoint).size();
		double distanceFromSurface = surfacePoint.sub(lightPoint).size();
		
		// If we hit the same object or we are closer
		if ((distanceFromOther > distanceFromSurface) ||
			(Math.abs(distanceFromOther - distanceFromSurface) < EPSILON))
		{
			return 1.0;
		}
		else // we have something in the way
		{ 
			//There are objects between the origin and the (light) point
			double totalTransperancy = 1.0;
			for (Intersection intersection: intersections)
			{
				if (intersection.getSurface() == surface)
				{
					break;
				}
				
				Material material = getMaterialForSurface(intersection.getSurface());
				totalTransperancy *= material.getTransperancy();
			}
			return totalTransperancy;
		}
	}
	
	/**
	 * Gets the transparency color of the object (including incidence)
	 * @param intersections The intersections with the ray
	 * @param recursionLevel the current level of recursion
	 * @param material The current material to get the transparency for
	 * @return The transparency color for the given material
	 */
	private Color getTransparencyColorWithIncidence(ArrayList<Intersection> intersections, int recursionLevel, 
													Material material)
	{
		ArrayList<Intersection> nextIntersections = new ArrayList<Intersection>(intersections);
		Intersection currentIntersection = nextIntersections.remove(0);
		Color transparencyColor = new Color(getColor(nextIntersections, recursionLevel));
		transparencyColor = transparencyColor.multiply(material.getTransperancy());
		
		//bonus feature
		//new_transparency = (1 - MI) * transparency + MI * IV * transparency
		double materialIncidence = material.getIncidence();
		double incidenceValue = -1 * currentIntersection.getRay().getV().dotProduct(currentIntersection.getNormal());
		
		Color newTransparency = transparencyColor.multiply(1-materialIncidence).add(transparencyColor.multiply(materialIncidence * incidenceValue));
		
		return newTransparency;
		
	}
	
	/**
	 * Gets the reflection color for the given intersection (including the incidence)
	 * @param intersection The intersection to get the reflection for
	 * @param recursionLevel The current recursion level
	 * @param material The material to get the reflection for
	 * @return The reflection color for the given intersection
	 */
	private Color getReflectionsColorWithIncidence(Intersection intersection, int recursionLevel, Material material)
	{
		//R = V-2(V dotproduct N)N
		Vector3D V = intersection.getRay().getV();
		Vector3D N = intersection.getNormal();
		Vector3D R = (V.sub(N.multByScalar(2*(V.dotProduct(N))))).normalize();
		Ray reflectionRay = new Ray(intersection.getIntersectionPoint().add(R.multByScalar(EPSILON)), intersection.getIntersectionPoint().add(R), R);
		ArrayList<Intersection> reflectionIntersections = getIntersectionsSorted(reflectionRay);
		
		Color reflectionColor = null;
		Color materialReflectionColor = material.getReflection();
		if (materialReflectionColor.getBlue() == 0 && materialReflectionColor.getGreen() == 0
			&& materialReflectionColor.getRed() == 0)
		{
			reflectionColor = new Color(0, 0, 0);
		}
		else
		{
			reflectionColor = getColor(reflectionIntersections, recursionLevel + 1);
			reflectionColor = reflectionColor.multiply(material.getReflection());
		}
		
		//bonus feature
		//new_reflection = (1 - MI) * reflection_color + MI * (1 - IV) * reflecion_color
		double materialIncidence = material.getIncidence();
		double incidenceValue = -1 * intersection.getRay().getV().dotProduct(intersection.getNormal());
		
		Color newReflection = reflectionColor.multiply(1-materialIncidence).add(reflectionColor.multiply(materialIncidence*(1 - incidenceValue)));
		
		return newReflection;
	}
	
	/**
	 * Gets the material for the given surface
	 * @param surface the surface to get the material for
	 * @return The material for the given surface
	 */
	public Material getMaterialForSurface(Surface surface)
	{
		// we assume that everything is valid and the material exists
		int materialIndex = surface.getMaterialIndex();
		return _materials.get(materialIndex-1);		
	}
	
	/**
	 * Compares two intersections by their distance parameter (t)
	 */
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

