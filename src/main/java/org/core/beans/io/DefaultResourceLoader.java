package org.core.beans.io;

import java.io.File;
import java.net.URL;

public class DefaultResourceLoader implements ResourceLoader {

	public Resource getResource(String location) {
		FileResource fileResource=new FileResource();
        URL url= this.getClass().getClassLoader().getResource(location);
        File file= new File(url.getFile());
        fileResource.setFile(file);
        fileResource.setPath(url.getFile());
		return fileResource;
	}

}
