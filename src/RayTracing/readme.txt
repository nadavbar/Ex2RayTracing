Ex2  - RayTracing

Submitted by:
Nadav Bar, 03458193, nadavbar@gmail.com
Alon Slotky, 037958832, alonslotky@gmail.com

Short explanation:

The main ray casting loop and rendering logic resides in the SceneGenerator class (The renderScene method). 

Soft shadowing was done by creating the area light using a plane which was calculated using the light vector as the normal and the
camera's up vector as the vector that was used to calculate the 2 prependicular vectors to the normal.

The Incidence bonus feature was also implemented.

The surface class is the base abstract class for all surfaces in the program, 
and all surfaces should extend this class.

List of files sumbitted:

Camera.java - The camera object. 
Color.java - A color with R, G, B components
Intersection.java - Represents an intersection between a surface and a ray
Light.java - A Light object.
Material.java - A Material object
Plane.java - Represents a plane
Ray.java -  This class represents a ray, with a start position and a direction
RayTracer.java - The main program class, parses the input file and outputs the image
SceneGenerator.java - The main class which contains the ray casting main loop and generates the scene image
Settings.java - Holds the global settings of the scene
Sphere.java - A sphere object
Surface.java - Represents an abstract surface. All supported surfaces should extend this class.
Vector3D.java - Representation of 3D vector.
ViewPlane.java - Represents a view plane. Calculates the coordinate system according to the plane.