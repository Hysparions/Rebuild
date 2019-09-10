package engine.opengl.models.stanford;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joml.Vector4i;

import engine.utils.Utilities;

public class PolyModel {

	// Name of the model
	private String name;
	// fileName directory
	private String directory;
	// List of the vertices of the mesh.
	private List<PolyVertex> vertices;
	// Number of floats of a polyModel
	public final static int VERTEX_BYTE_SIZE = 6*Utilities.FLOATSIZE+4;
	
	public PolyModel(String name, String directory) {
		this.name = name;
		this.directory = directory;
		this.vertices = new LinkedList<>();
		this.load();
	}

	public PolyModel(String name, String directory, Vector4i... filters) {
		this.name = name;
		this.directory = directory;
		this.vertices = new LinkedList<>();
		this.load(filters);
	}

	private void load(Vector4i... filters) {
		String fileName = "/models/"+directory+"/"+name+".ply";
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)));
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Can't find the .ply file: " + fileName);
			System.exit(0);
		}
		try {
			processMesh(reader, filters);
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof IOException) {
				System.err.println("Error with .ply format for: " + fileName);
			}else if(e instanceof NumberFormatException){
				System.err.println("Failed to convert String to number in file: " + fileName);
			}
			System.exit(0);
		}
	}

	/**
	 * Process the mesh using the reader of the Stanford file
	 * @param reader of the stanford file
	 * @param filters to add alpha value to vertices with a specified color
	 * @throws IOException when reader fails to read
	 * @throws NumberFormatException when reader failed to convert String to data
	 */
	private void processMesh(BufferedReader reader, Vector4i... filters) throws IOException, NumberFormatException {
		
		int vertexAmount;
		
		String line = reader.readLine();
		
		// Finding vertex amount
		Pattern pattern = Pattern.compile("element vertex");
		Matcher matcher = pattern.matcher(line);
		while(!matcher.find()) {
			line = reader.readLine();
			matcher = pattern.matcher(line);
		}
		String result[] = line.split(" ");
		vertexAmount = Integer.parseInt(result[result.length-1]);
		// Loading data
		pattern = Pattern.compile("end_header");
		matcher = pattern.matcher(line);
		while(!matcher.find()) {
			line = reader.readLine();
			matcher = pattern.matcher(line);
		}
		
		for(int i = 0; i<vertexAmount; i++) {

			// Reads the new Line
			line = reader.readLine();
			PolyVertex vertex = new PolyVertex();
			
			result = line.split(" ");
			
			// Adding the position component
			vertex.position.x = Float.parseFloat(result[0]);
			vertex.position.y = Float.parseFloat(result[1]);
			vertex.position.z = Float.parseFloat(result[2]);

			// Adding the normal component
			vertex.normal.x = Float.parseFloat(result[3]);
			vertex.normal.y = Float.parseFloat(result[4]);
			vertex.normal.z = Float.parseFloat(result[5]);

			// Adding the color component
			vertex.color.r(Integer.parseInt(result[6]));
			vertex.color.g(Integer.parseInt(result[7]));
			vertex.color.b(Integer.parseInt(result[8]));
			vertex.color.a(0);
			for(int n = 0; n<filters.length; n++){
				if(vertex.color.r() == filters[n].x-128 && vertex.color.g() == filters[n].y-128 && vertex.color.b() == filters[n].z-128) {
					vertex.color.a(filters[n].w);
					break;
				}
			}
			// Adding this whole new vertex to the list
			vertices.add(vertex);
		}
		
		
	}

	/** @return Model Name */
	public String getName() {
		return name;
	}

	/** @return vertices amount */
	public int size() {
		return vertices.size();
	}

	/** @return vertex list */
	public List<PolyVertex> getVertices() {
		return vertices;
	}

}


