package mainDefault;

import imageReadFunctions.ImageDisplay;
import imageReadFunctions.ImageReading;
import imageReadFunctions.RegionGrowth;

import javax.swing.JFrame;

import prettyDrawings.HistogramAction;



public class Main {
	
	static void invokeFrame(int which, int x, int y, ImageDisplay Image){
		if (which == 1){
			JFrame urFrame = new JFrame("Obraz Orginalny");
			urFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			urFrame.setSize(x,y);
			urFrame.setContentPane(Image);
			urFrame.setVisible(true);
		}
		else if (which == 2){
			JFrame isoFrame = new JFrame("Obraz Zmieniony");
			isoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			isoFrame.setSize(x,y);
			isoFrame.setLocation(x, 0);
			isoFrame.setContentPane(Image);
			isoFrame.setVisible(true);
		}
	}
	
	public static void main (String[] args){
		
	// Basic variables.
	String fileToRead = null;
	String fileToSave = null;
	int value = 0;
	int value2 = 0;
	int choice = 0;
	String innerchoice = null;
	
	Boolean firstFlowControl = true;
	Boolean secondFlowControl = true;
	Boolean adjustTrigger = true;
	
	int frameX = 800;
	int frameY = 600;
	
	/*
	
	JOptionPane.showMessageDialog(null, "Podstawowy Program Przetwarzania Obrazu i DŸwiêku. \n\n Autorzy:\n Paula Pszczo³a\n £ukasz Ko³odziejczyk.");
	fileToRead = JOptionPane.showInputDialog("Wybierz obraz do wczytania.");
	//fileToRead = "Lena.png";
	
	ImageReading ReadImage = new ImageReading(fileToRead);
	HistogramAction hist = new HistogramAction();
	hist.showTheThing(ReadImage);
	ImageDisplay PrimeImage = new ImageDisplay(ReadImage, false);
	
	invokeFrame(1, frameX, frameY, PrimeImage);
	
	JOptionPane.showMessageDialog(null, "Wygenerowano Histogramy!\nHistogramy dostepne s¹ z poziomu eksploratora Windows.");
	
	while (firstFlowControl == true){
		choice = Integer.parseInt(JOptionPane.showInputDialog("Wybierz opcje przetwarzania obrazu:\n"
											+ " (1)> Zmiana Jasnoœci.\n"
											+ " (2)> Zmiana Kontrastu.\n"
											+ " (3)> Inwersja.\n"
											+ " (4)> Filtr Œrednioarytmetyczny.\n"
											+ " (5)> Filtr Medianowy.\n"
											+ " (6)> Wykrywanie Krawêdzi (Splot).\n"
											+ " (7)> Wykrywanie Krawêdzi (Rosenfeld).\n"
											+ " (8)> Transformacja Raleigha.\n"));
		secondFlowControl = true;
		while(secondFlowControl == true){
			if (choice == 1){
				innerchoice = JOptionPane.showInputDialog("Rozjaœniæ / Przyciemniæ?\n(+/-)");
				if (innerchoice.equals("+")) adjustTrigger = true;
				else adjustTrigger = false;
				value = Integer.parseInt(JOptionPane.showInputDialog("Podaj wartoœæ:"));
				
				ReadImage.brightnessAdjust(adjustTrigger, value);
				ImageDisplay AlterImage = new ImageDisplay(ReadImage, true);
				invokeFrame(2, frameX, frameY, AlterImage);
				ReadImage.calcPSNR();
			}
			else if (choice == 2){
				value = Integer.parseInt(JOptionPane.showInputDialog("Podaj wartoœæ:"));
				
				ReadImage.contrastAdjust(value);
				ImageDisplay AlterImage = new ImageDisplay(ReadImage, true);
				invokeFrame(2, frameX, frameY, AlterImage);
				ReadImage.calcPSNR();
			}
			else if (choice == 3){				
				ReadImage.invertAdjust();
				ImageDisplay AlterImage = new ImageDisplay(ReadImage, true);
				invokeFrame(2, frameX, frameY, AlterImage);
				ReadImage.calcPSNR();
			}
			else if (choice == 4){
				ReadImage.meanFilter();
				ImageDisplay AlterImage = new ImageDisplay(ReadImage, true);
				invokeFrame(2, frameX, frameY, AlterImage);
				ReadImage.calcPSNR();
			}
			else if (choice == 5){
				ReadImage.medianFilter();
				ImageDisplay AlterImage = new ImageDisplay(ReadImage, true);
				invokeFrame(2, frameX, frameY, AlterImage);
				ReadImage.calcPSNR();
			}
			else if (choice == 6){
				value = Integer.parseInt(JOptionPane.showInputDialog("Podaj rodzaj filtru:\n"
																+" (1)>Po³udniowy.\n"
																+" (2)>Po³udniowo-Zachodni.\n"
																+" (3)>Zachodni.\n"
																+" (4)>Pó³nocno-Zachodni.\n"));
				
				ReadImage.foregroundFilter(value);
				ImageDisplay AlterImage = new ImageDisplay(ReadImage, true);
				invokeFrame(2, frameX, frameY, AlterImage);
				ReadImage.calcPSNR();
			}
			else if (choice == 7){
				value = Integer.parseInt(JOptionPane.showInputDialog("Podaj wartoœæ 'R'"));
				
				ReadImage.Rosenfeld(value);
				ImageDisplay AlterImage = new ImageDisplay(ReadImage, true);
				invokeFrame(2, frameX, frameY, AlterImage);
				ReadImage.calcPSNR();
			}
			else if (choice == 8){
				value = Integer.parseInt(JOptionPane.showInputDialog("Podaj wartoœæ minimaln¹."));
				value2 = Integer.parseInt(JOptionPane.showInputDialog("Podaj wartoœæ alpha."));
				
				ReadImage.transformRaleigh(value, value2, hist);
				ImageDisplay AlterImage = new ImageDisplay(ReadImage, true);
				invokeFrame(2, frameX, frameY, AlterImage);
				ReadImage.calcPSNR();
			}			
			innerchoice = JOptionPane.showInputDialog("Powtórzyæ z innymi ustawieniami?\n(y/n)");
			System.out.println(innerchoice);
			if (innerchoice.equals("y")) ;
			else break;
		}
		innerchoice = JOptionPane.showInputDialog("Zapisaæ plik z przetworzonym obrazem?\n(y/n)");
		if (innerchoice.equals("y")){
			fileToSave = JOptionPane.showInputDialog("Podaj nazwe pliku. \n"
					+ "									[PAMIÊTAJ O ROZSZERZENIU PO KROPCE!! .png!!]");
			ReadImage.saveImage(fileToSave);
		}
		
		innerchoice = JOptionPane.showInputDialog("Powtórzyæ z innymi ustawieniami?\n(y/n)");
		if (innerchoice.equals("y")) firstFlowControl = true;
		else firstFlowControl = false;
	}
	
	*/
	
	
	//============
	// Debug:
	// Load the image and hook it to the image display controller.
	//fileToRead = JOptionPane.showInputDialog("Choose the image to read.");
	//fileToRead = "CheerSmiles.png"; // Short version for debug purposes.
	fileToRead = "Lena.png";
	ImageReading ReadImage = new ImageReading(fileToRead);
	//FourierTransformation fftTrans = new FourierTransformation(fileToRead);
	RegionGrowth regGrow = new RegionGrowth(fileToRead);
	
	regGrow.visualize(12, 300, 250);

	//fftTrans.FFTstandard();
	//fftTrans.exec_FLIP();
	//fftTrans.filterBandblock(12,120);
	//fftTrans.filterDisplacement(100, 1);
	//fftTrans.exec_FLIP();
	//fftTrans.FFTinverse();
	
	HistogramAction histAction = new HistogramAction();
	histAction.showTheThing(ReadImage);
	
		
	ImageDisplay PrimeImage = new ImageDisplay(ReadImage, false);
		

	// Alter the image and hook THAT up to the image display controller.
	//ReadImage.brightnessAdjust(true, 200);
	//ReadImage.contrastAdjust(100);
	//ReadImage.invertAdjust();
	//ReadImage.invertAdjust();
	//ReadImage.meanFilter();
	//ReadImage.meanFilter();
	//ReadImage.medianFilter();
	//ReadImage.foregroundFilter(1);
	//ReadImage.Rosenfeld(2);
	//ReadImage.transformRaleigh(0, 1, histAction);
	//ReadImage.calcPSNR();
	//ImageDisplay AlterImage = new ImageDisplay(ReadImage, true);
	
	//fftTrans.visualize("REAL");
	//fftTrans.visualize("FOUR");
	ImageDisplay AlterImage = new ImageDisplay(regGrow, true);
	
	
	
	// Save the altered image
	//fileToSave = JOptionPane.showInputDialog("Choose the name for the saved image. \n"
	//		+ "									[REMEMBER TO INCLUDE THE FILE EXTENSION!!]");
	//ReadImage.saveImage(fileToSave);

	
	// Frames that make the world spin round.
	// First frame invoke.
	JFrame urFrame = new JFrame("Obraz Orginalny");
	urFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	urFrame.setSize(frameX,frameY);
	urFrame.setContentPane(PrimeImage);
	urFrame.setVisible(true);
	
	// Second frame invoke.
	JFrame isoFrame = new JFrame("Obraz Zmieniony");
	isoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	isoFrame.setSize(frameX,frameY);
	isoFrame.setLocation(frameX, 0);
	isoFrame.setContentPane(AlterImage);
	isoFrame.setVisible(true);
	
	
	}
}