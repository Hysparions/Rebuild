package world.models;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;


public class PolyModel{
	
	//Name of the model
	private String name;
	//fileName directory
	private String directory;
	//List of the vertices of the mesh.
	private List<StaticVertex> vertices;
	
	public PolyModel(String name, String directory) {
		this.name = name;
		this.directory = directory;
		this.vertices = new ArrayList<>();
		this.load();
	}
	
	private void load() {
		try {
			//Importing Scene data
			AIScene scene = Assimp.aiImportFile("res/models/" + directory +"/"+name+".ply", Assimp.aiProcess_Triangulate);
			if ((scene == null) || ((scene.mFlags() & Assimp.AI_SCENE_FLAGS_INCOMPLETE) != 0 ) || (scene.mRootNode() == null)) {throw new Exception( "ERROR::ASSIMP:: " + Assimp.aiGetErrorString() ); }
	
			//Processing Meshes
			PointerBuffer aiMeshes = scene.mMeshes();
		    AIMesh aiMesh = AIMesh.create(aiMeshes.get(0));
		    processMesh(aiMesh);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void processMesh(AIMesh aiMesh) {
		//Creating buffers for each Assimp Data  ( Vertex Position,  Normal direction, 
		AIVector3D.Buffer vertexBuffer = aiMesh.mVertices();	
		AIVector3D.Buffer normalBuffer = aiMesh.mNormals();
		AIColor4D.Buffer colorBuffer = aiMesh.mColors(0);
		for(long i = 0; i < aiMesh.mNumVertices(); i++) {
			StaticVertex vertex = new StaticVertex();
			
			//Adding the position component
			AIVector3D aiVertex = vertexBuffer.get();
			vertex.position.x = aiVertex.x();
			vertex.position.y = aiVertex.y();
			vertex.position.z = aiVertex.z();
			
			//Adding the normal component
	        AIVector3D aiNormal = normalBuffer.get();
	        vertex.normal.x = aiNormal.x();
	        vertex.normal.y = aiNormal.y();
	        vertex.normal.z = aiNormal.z();
	        
	        //Adding the color component
	        AIColor4D aiColor = colorBuffer.get();
	        vertex.color.x = aiColor.r();
	        vertex.color.y = aiColor.g();
	        vertex.color.z = aiColor.b();
	        
	        if(vertex.color.x > 0.25f && vertex.color.x < 0.27f && vertex.color.y>0.35f && vertex.color.y<0.36f && vertex.color.z >0.14f && vertex.color.z <0.15f) {
		        vertex.flexibility = 1.0f;
	        }
	        else{vertex.flexibility =0.0f;}
	        //Adding this whole new vertex to the list
	        vertices.add(vertex);
		}
		
		/*for(int i = 0 ; i < vertices.size(); i++) {
			vertices.get(i).display();
		}*/
		//System.out.println(vertices.size());
	}
	
	//GETTERS
	public String getName() { return name; }
	public int getSize() {return vertices.size(); }
	public List<StaticVertex> getVertices(){return vertices;}
	
}
