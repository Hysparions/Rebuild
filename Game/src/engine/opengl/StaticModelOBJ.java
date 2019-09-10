package engine.opengl;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

public class StaticModelOBJ {

	private String filePath;
	private List<StaticMesh> meshes;
	private Vector3f position;
	private Matrix4f model;
	private Shader shader;
	private Vector3f rotationAxe;
	private float angle;

	public StaticModelOBJ(String directory, Shader s) {
		filePath = directory;
		meshes = new ArrayList<StaticMesh>();
		shader = s;
		position = new Vector3f(0.0f, 0.0f, 0.0f);
		model = new Matrix4f();
		rotationAxe = new Vector3f(0.0f, 1.0f, 0.0f);
		angle = 0.0f;
		this.load();
	}

	private void load() {
		try {
			// Importing Scene data
			AIScene scene = Assimp.aiImportFile("res/objects/" + filePath + "/" + filePath + ".obj",
					Assimp.aiProcess_Triangulate | Assimp.aiProcess_JoinIdenticalVertices);
			if ((scene == null) || ((scene.mFlags() & Assimp.AI_SCENE_FLAGS_INCOMPLETE) != 0)
					|| (scene.mRootNode() == null)) {
				throw new Exception("ERROR::ASSIMP:: " + Assimp.aiGetErrorString());
			}

			// Processing Meshes
			int MeshCount = scene.mNumMeshes();
			PointerBuffer aiMaterials = scene.mMaterials();
			PointerBuffer aiMeshes = scene.mMeshes();
			for (int i = 0; i < MeshCount; i++) {
				AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
				meshes.add(processMesh(aiMesh, aiMaterials));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private StaticMesh processMesh(AIMesh aiMesh, PointerBuffer aiMaterials) {

		// Generating a Static Mesh
		StaticMesh mesh = new StaticMesh();
		/// Creating the vertices Buffer
		FloatBuffer vertices = BufferUtils.createFloatBuffer(aiMesh.mNumVertices() * 8);
		// Creating buffers for each Assimp Data ( Vertex Position, Normal direction,
		AIVector3D.Buffer vertex = aiMesh.mVertices();
		AIVector3D.Buffer normal = aiMesh.mNormals();
		AIVector3D.Buffer texPos = null;
		boolean t = false;
		if (aiMesh.mTextureCoords(0) != null) {
			t = true;
			texPos = aiMesh.mTextureCoords(0);
		}

		for (long i = 0; i < aiMesh.mNumVertices(); i++) {
			AIVector3D aiVertex = vertex.get();
			vertices.put(aiVertex.x());
			vertices.put(aiVertex.y());
			vertices.put(aiVertex.z());
			AIVector3D aiNormal = normal.get();
			vertices.put(aiNormal.x());
			vertices.put(aiNormal.y());
			vertices.put(aiNormal.z());
			if (t) {
				AIVector3D aiTexPos = texPos.get();
				vertices.put(aiTexPos.x());
				vertices.put(aiTexPos.y());
			}
		}
		((Buffer)vertices).flip();
		/// Creating the indices Buffer . As we store triangle there are 3 indices per
		/// Faces
		IntBuffer indices = BufferUtils.createIntBuffer(aiMesh.mNumFaces() * 3);
		// Creating buffer to store Assimp indices Data
		AIFace.Buffer faces = aiMesh.mFaces();
		for (int i = 0; i < aiMesh.mNumFaces(); i++) {
			AIFace face = faces.get();
			IntBuffer indicesBuffer = face.mIndices();
			for (int j = 0; j < face.mNumIndices(); j++) {
				indices.put(indicesBuffer.get(j));
			}
		}
		((Buffer)indices).flip();

		List<Texture> meshTextures = new ArrayList<>();

		if (aiMesh.mMaterialIndex() >= 0) {
			AIMaterial material = AIMaterial.create(aiMaterials.get(aiMesh.mMaterialIndex()));

			for (int i = 0; i < Assimp.aiGetMaterialTextureCount(material, Assimp.aiTextureType_DIFFUSE); i++) {
				AIString path = AIString.calloc();
				Assimp.aiGetMaterialTexture(material, Assimp.aiTextureType_DIFFUSE, i, path, (IntBuffer) null, null,
						null, null, null, null);
				Texture texture = Texture.createTexture("res/objects/" + filePath + "/" + path.dataString());
				texture.setType("diffuse");
				meshTextures.add(texture);
			}

		}

		mesh.Generate(vertices, indices, meshTextures);

		return mesh;
	}

	public void draw() {
		for (int i = 0; i < meshes.size(); i++) {
			model.identity();
			model.translate(position.x, position.y, position.z);

			model.rotate((float) Math.toRadians(angle), rotationAxe);
			shader.setMat4Uni("model", model);
			meshes.get(i).draw(shader);
		}
	}

	public void destroy() {
		for (int i = 0; i < meshes.size(); i++) {
			meshes.get(i).destroy();
		}
	}

	public void move(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}

	public void rotate(float x, float y, float z, float ang) {
		angle = ang;
		rotationAxe.x = x;
		rotationAxe.y = y;
		rotationAxe.z = z;
	}

}
