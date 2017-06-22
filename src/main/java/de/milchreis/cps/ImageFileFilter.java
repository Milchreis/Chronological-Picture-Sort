package de.milchreis.cps;

import java.io.File;

import org.apache.commons.io.filefilter.IOFileFilter;

public class ImageFileFilter implements IOFileFilter  {

	public static final String[] SUPPORTED_VIEW_FORMATS = {"jpg", "jpeg"};
	
	@Override
	public boolean accept(File pathname) {
		return 
				pathname.getName().toLowerCase().endsWith(SUPPORTED_VIEW_FORMATS[0]) ||
				pathname.getName().toLowerCase().endsWith(SUPPORTED_VIEW_FORMATS[1]);
	}

	@Override
	public boolean accept(File dir, String name) {
		return accept(new File(dir, name));
	}
	
}
