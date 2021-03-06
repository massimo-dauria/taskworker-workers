package drm.taskworker.workers.geo;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import drm.taskworker.Worker;
import drm.taskworker.tasks.ParameterFoundException;
import drm.taskworker.tasks.Task;

public abstract class AbstractGeoWorker extends Worker {

	public AbstractGeoWorker(String name) {
		super(name);
	}

	public void attachImage(Task t, BufferedImage img, Region reg){
		t.addParam("img", new SerialisableImageContainer(img));
		t.addParam("region", reg);
	}
	
	public void attachBinaryImage(Task t,String name, BufferedImage img) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(img, "png", baos);
		t.addParam(name, baos.toByteArray());
	}

	public BufferedImage detachImage(Task t) throws ParameterFoundException{
		return ((SerialisableImageContainer)t.getParam("img")).getImage();
	}
	
	public Region getRegion(Task task) throws ParameterFoundException {
		try{
			Object in = task.getParam("region"); 
			if(in instanceof Region)
				return (Region)in;
			if(in instanceof String)
				return Region.parse((String)in);
			throw new IllegalArgumentException("region argument could not be parsed: " +in);
		}catch(ParameterFoundException e){
			return Region.parse((String) task.getParam("arg0"));
		}
		
	}
}
