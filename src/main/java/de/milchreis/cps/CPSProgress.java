package de.milchreis.cps;

import java.io.File;

public interface CPSProgress {
	public void onScanStep(File file);
	public void onSortStep(File file1, File file2);
}