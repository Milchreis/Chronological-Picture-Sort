package de.milchreis;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import de.milchreis.cps.CPS;

public class SimpleGui {

	public SimpleGui() {

		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		chooser.setDialogTitle("Choose a directory to reorder your images by creation date");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File selected = chooser.getSelectedFile();

			int response = JOptionPane.showConfirmDialog(null,
					"Would you really rename the containing files by creation date (EXIF)?\n"+
					"(" + selected.getAbsolutePath() + ")", 
					"Confirm",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if(response == JOptionPane.YES_OPTION) {
				
				try {
					CPS.sort(selected);
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, 
							e.getLocalizedMessage(), 
							"Error", 
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

}
