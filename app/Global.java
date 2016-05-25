import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import models.ImageEntry;
import play.*;

@SuppressWarnings("deprecation")
public class Global extends GlobalSettings {

  @Override
  public void onStart(Application app) {
    Logger.info("Application has started");
    
    for(int i = 1; i <= 3; i++) {  	
        try {
        	BufferedImage img = null;
            img = ImageIO.read(new File("conf/resources/img_samples/"+Integer.toString(i)+".png"));
            ImageEntry entry = new ImageEntry(img);
            entry.insert();
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
    
    
  }  

  @Override
  public void onStop(Application app) {
    Logger.info("Application shutdown...");
  }  
}
