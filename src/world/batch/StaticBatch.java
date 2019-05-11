package world.batch;

import utils.Utilities;
import world.behavior.StaticModel;
import world.models.StaticVertex;

import static  org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;
import java.util.List;

public class StaticBatch {
	
	//List of the model contained in the batch
	private List<StaticModel> models;
	
	//attributes of the batch
	public long capacity;
	public int limit;
	
	//OpenGl elements
	private int VBO;
	private int VAO;
	
	public StaticBatch(long capacity, int limit) {
		
		//List containing the models
		models = new ArrayList<StaticModel>();
		
		//Batch attributes
		this.capacity = capacity;
		this.limit = limit;
		
		//generating new Vertex array buffer
		VAO = glGenVertexArrays();
		glBindVertexArray(VAO);
		
		//generarting new vertex buffer object
		VBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, capacity*Utilities.FLOATSIZE, GL_DYNAMIC_DRAW);
		
		//Setting the vertex Array attribute pointers
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 10*Utilities.FLOATSIZE, 0);	
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 10*Utilities.FLOATSIZE, 3*Utilities.FLOATSIZE);
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 10*Utilities.FLOATSIZE, 6*Utilities.FLOATSIZE);
		glEnableVertexAttribArray(3);
		glVertexAttribPointer(3, 1, GL_FLOAT, false, 10*Utilities.FLOATSIZE, 9*Utilities.FLOATSIZE);
		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	//Function that update one model in the buffer 
	public void update(int indice) {
		StaticModel model = models.get(indice);
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferSubData(GL_ARRAY_BUFFER, model.offset, model.getBuffer());
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	//Function that detect the first empty space between two models. Then, it shift the model to fill the hole
	public void shift() {	
		
		if(models.size() == 1) {
			//If the only model needs to be shifted
			if(models.get(0).offset > 0) {	
				models.get(0).offset = 0;
				this.update(0);
				//Update the limit
				limit = models.get(0).size;
			}
			
		}else {
			//Empty space in the buffer
			long diff = 0;
			
			for(int i = 0; i < models.size(); i++) {
				//If the offset of the first element isn't 0 then set the model offset to 0
				if(i == 0) {
					if(models.get(0).offset > 0) {	
						models.get(0).offset = 0;
						this.update(0);
					}
				}else {
					//If the previous model size + offset is less than the current model offset then calculate the difference and set the new offset 
					if((models.get(i-1).offset + (models.get(i-1).size*StaticVertex.SIZE*Utilities.FLOATSIZE)) < models.get(i).offset) {
						//Calculation of the hole size in bytes
						diff =  models.get(i).offset - (models.get(i-1).offset + (models.get(i-1).size*StaticVertex.SIZE*Utilities.FLOATSIZE));
						models.get(i).offset -= diff;
						this.update(i);	
						if(i == models.size()-1) { limit = (int)models.get(i).offset/(StaticVertex.SIZE*Utilities.FLOATSIZE) + models.get(i).size;}
					}
				}
			}
		}
	}
	
	public void resize(long newCapacity) {
		
	}
	
	//Function that add a model to the batch
	public void add(StaticModel model) {
		//Adding the new Element to the buffer
		models.add(model);
		if((limit + model.size)*StaticVertex.SIZE*Utilities.FLOATSIZE <= capacity) {
			//The model offset in the batch is now set to the end of the buffer
			model.offset = ((long)limit)*StaticVertex.SIZE*Utilities.FLOATSIZE;
			limit += model.size;
			glBindBuffer(GL_ARRAY_BUFFER, VBO);
			glBufferSubData(GL_ARRAY_BUFFER, model.offset, model.getBuffer());
			glBindBuffer(GL_ARRAY_BUFFER, 0);

			//System.out.println(limit);
		}
		else {System.err.println("Not Enough Space");}
	}
	
	//Function that remove the model given in parameter from the batch
	public void remove(StaticModel model) {
		models.remove(model);
		shift();
		//System.out.println(limit);
	}
	
	public void setLimit(int limit ) {this.limit= limit;}
	public int getLimit() {return this.limit;}
	public void setCapacity(long capacity ) {this.capacity = capacity;}
	public long getCapacity() {return this.capacity;}
	
	public void render(){
		glBindVertexArray(VAO);
		glDrawArrays(GL_TRIANGLES, 0, limit);
		
		glBindVertexArray(0);
	}
	
	public void destroy() {
		glDeleteBuffers(VBO);
		glDeleteVertexArrays(VAO);
	}
	
}
