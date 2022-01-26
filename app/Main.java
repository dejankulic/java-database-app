package app;

import main.model.AppCore;
import main.view.MainFrame;

/**
 * @author StefanP
 * */
public class Main {
	public static void main(String[] args) {
		AppCore appcore= new AppCore();
		MainFrame mf= MainFrame.getInstance();
		mf.setAppCore(appcore);
		
		mf.getCore().loadResource();
		
		
		mf.setVisible(true);
	}
}
