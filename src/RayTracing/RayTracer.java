package RayTracing;

import java.awt.Transparency;
import java.awt.color.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 *  Main class for ray tracing exercise.
 */
public class RayTracer {
	// this is a comment
	public int _imageWidth;
	public int _imageHeight;
	
	SceneGenerator _sceneGenerator;
	
	public RayTracer()
	{
		
	}

	/**
	 * Runs the ray tracer. Takes scene file, output image file and image size as input.
	 */
	public static void main(String[] args) {

		try {

			RayTracer tracer = new RayTracer();

                        // Default values:
			tracer._imageWidth = 500;
			tracer._imageHeight = 500;

			if (args.length < 2)
				throw new RayTracerException("Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");

			String sceneFileName = args[0];
			String outputFileName = args[1];

			if (args.length > 3)
			{
				tracer._imageWidth = Integer.parseInt(args[2]);
				tracer._imageHeight = Integer.parseInt(args[3]);
			}


			// Parse scene file:
			tracer.parseScene(sceneFileName);

			// Render scene:
			tracer.renderScene(outputFileName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (RayTracerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	/**
	 * Parses the scene file and creates the scene. Change this function so it generates the required objects.
	 */
	public void parseScene(String sceneFileName) throws IOException, RayTracerException
	{
		Camera camera = null;
		Settings settings = null;
		ArrayList<Material> materials = new ArrayList<Material>();
		ArrayList<Surface> surfaces = new ArrayList<Surface>();
		ArrayList<Light> lights = new ArrayList<Light>();
		
		FileReader fr = new FileReader(sceneFileName);

		BufferedReader r = new BufferedReader(fr);
		
		try 
		{
			String line = null;
			int lineNum = 0;
			System.out.println("Started parsing scene file " + sceneFileName);
	
			while ((line = r.readLine()) != null)
			{
				line = line.trim();
				++lineNum;
	
				if (line.isEmpty() || (line.charAt(0) == '#'))
				{  // This line in the scene file is a comment
					continue;
				}
				else
				{
					String code = line.substring(0, 3).toLowerCase();
					// Split according to white space characters:
					String[] params = line.substring(3).trim().toLowerCase().split("\\s+");
	
					if (code.equals("cam"))
					{
						camera = new Camera(vectorFromParams(params, 0),
								vectorFromParams(params, 3),
								vectorFromParams(params, 6),
								parseDouble(params, 9), parseDouble(params, 10));
						System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));
					}
					else if (code.equals("set"))
					{
						settings = new Settings(colorFromParams(params, 0), 
								Integer.parseInt(params[3]), Integer.parseInt(params[4]));
						System.out.println(String.format("Parsed general settings (line %d)", lineNum));
					}
					else if (code.equals("mtl"))
					{
						Material material = new Material(colorFromParams(params, 0),
								colorFromParams(params, 3),
								colorFromParams(params, 6),
								parseDouble(params, 9),
								parseDouble(params, 10),
								parseDouble(params, 11));
						materials.add(material);
						System.out.println(String.format("Parsed material (line %d)", lineNum));
					}
					else if (code.equals("sph"))
					{
						Sphere sph = new Sphere(parseDouble(params, 0),
								parseDouble(params, 1),
								parseDouble(params, 2),
								parseDouble(params, 3),
								Integer.parseInt(params[4]));
						
						surfaces.add(sph);
	
						System.out.println(String.format("Parsed sphere (line %d)", lineNum));
					}
					else if (code.equals("pln"))
					{
						Plane pln = new Plane(vectorFromParams(params, 0), 
								parseDouble(params, 3),
								Integer.parseInt(params[4]));
						surfaces.add(pln);
						System.out.println(String.format("Parsed plane (line %d)", lineNum));
					}
					else if (code.equals("lgt"))
					{
						Light lgt = new Light(vectorFromParams(params, 0), 
								colorFromParams(params, 3),
								parseDouble(params, 6),
								parseDouble(params, 7),
								parseDouble(params, 8));
						lights.add(lgt);
						System.out.println(String.format("Parsed light (line %d)", lineNum));
					}
					else
					{
						System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
					}
				}
			}
			
			if (!validateScene(camera, settings, materials, surfaces, lights))
			{
				System.out.println("Given scene isn't valid, exiting");
			}
			
			_sceneGenerator = new SceneGenerator(camera, settings, materials, surfaces, lights,
												_imageWidth, _imageHeight);
		}
		finally
		{
			r.close();
		}
		
		System.out.println("Finished parsing scene file " + sceneFileName);

	}
	
	private boolean validateScene(Camera camera, Settings settings, 
								  ArrayList<Material> materials, ArrayList<Surface> surfaces,
								  ArrayList<Light> lights)
	{
		if (camera == null || settings == null)
		{
			return false;
		}
		
		for (Surface surface : surfaces)
		{
			if (surface.getMaterialIndex() <= 0 ||
				surface.getMaterialIndex() > materials.size())
			{
				return false;
			}
		}
		
		return true;
	}
	
	private Color colorFromParams(String[] params, int index)
	{
		return new Color(parseDouble(params ,index),
				parseDouble(params, index+1),
				parseDouble(params, index+2));
	}
	
	private Vector3D vectorFromParams(String[] params, int index)
	{
		return new Vector3D(parseDouble(params, index),
				parseDouble(params, index+1),
				parseDouble(params, index+2));
	}
	
	private double parseDouble(String[] param, int index)
	{
		if (param.length <= index )
		{
			return 0;
		}
		
		if (param[index] == null)
		{
			return 0;
		}
		
		return Double.parseDouble(param[index]);
	}

	/**
	 * Renders the loaded scene and saves it to the specified file location.
	 */
	public void renderScene(String outputFileName)
	{
		long startTime = System.currentTimeMillis();

		// Create a byte array to hold the pixel data:
		byte[] rgbData = new byte[this._imageWidth * this._imageHeight * 3];


                // Put your ray tracing code here!
                //
                // Write pixel color values in RGB format to rgbData:
                // Pixel [x, y] red component is in rgbData[(y * this.imageWidth + x) * 3]
                //            green component is in rgbData[(y * this.imageWidth + x) * 3 + 1]
                //             blue component is in rgbData[(y * this.imageWidth + x) * 3 + 2]
                //
                // Each of the red, green and blue components should be a byte, i.e. 0-255
		Color[][] rendered = _sceneGenerator.renderScene();
		
		for (int i=0; i<_imageHeight; i++)
		{
			for (int j=0; j<_imageWidth; j++)
			{
				Color color = rendered[i][j];
				rgbData[(i*_imageWidth + j)*3] = (byte) (color.getRed() * 255);
				rgbData[(i*_imageWidth + j)*3 + 1] = (byte) (color.getGreen() * 255);
				rgbData[(i*_imageWidth + j)*3 + 2] = (byte) (color.getBlue() * 255);
			}
		}

		long endTime = System.currentTimeMillis();
		Long renderTime = endTime - startTime;

                // The time is measured for your own conveniece, rendering speed will not affect your score
                // unless it is exceptionally slow (more than a couple of minutes)
		System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");

                // This is already implemented, and should work without adding any code.
		saveImage(this._imageWidth, rgbData, outputFileName);

		System.out.println("Saved file " + outputFileName);

	}




	//////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT //////////////////////////////////////////

	/*
	 * Saves RGB data as an image in png format to the specified location.
	 */
	public static void saveImage(int width, byte[] rgbData, String fileName)
	{
		try {

			BufferedImage image = bytes2RGB(width, rgbData);
			ImageIO.write(image, "png", new File(fileName));

		} catch (IOException e) {
			System.out.println("ERROR SAVING FILE: " + e.getMessage());
		}

	}

	/*
	 * Producing a BufferedImage that can be saved as png from a byte array of RGB values.
	 */
	public static BufferedImage bytes2RGB(int width, byte[] buffer) {
	    int height = buffer.length / width / 3;
	    ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
	    ColorModel cm = new ComponentColorModel(cs, false, false,
	            Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
	    SampleModel sm = cm.createCompatibleSampleModel(width, height);
	    DataBufferByte db = new DataBufferByte(buffer, width * height);
	    WritableRaster raster = Raster.createWritableRaster(sm, db, null);
	    BufferedImage result = new BufferedImage(cm, raster, false, null);

	    return result;
	}

	public static class RayTracerException extends Exception 
	{
		private static final long serialVersionUID = -2109299985872926772L;

		public RayTracerException(String msg) {  super(msg); }
	}


}
