package mainDefault;

import imageReadFunctions.FourierTransformation;
import imageReadFunctions.ImageDisplay;
import imageReadFunctions.ImageReading;
import imageReadFunctions.RegionGrowth;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import prettyDrawings.HistogramAction;



public class Main {
	
	static void invokeFrame(int which, int x, int y, ImageDisplay Image, int posY){
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
			isoFrame.setLocation(x, posY);
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
	double fourValue1 = 0;
	double fourValue2 = 0;
	String innerchoice = null;
	
	Boolean firstFlowControl = true;
	Boolean secondFlowControl = true;
	Boolean adjustTrigger = true;
	
	int frameX = 800;
	int frameY = 600;
	
	/*
	
	JOptionPane.showMessageDialog(null, "Podstawowy Program Przetwarzania Obrazu i DŸwiêku. \n\n Autorzy:\n Paula Pszczo³a\n £ukasz Ko³odziejczyk.");
	//fileToRead = JOptionPane.showInputDialog("Wybierz obraz do wczytania.");
	fileToRead = "Lena.png";
	
	ImageReading ReadImage = new ImageReading(fileToRead);
	FourierTransformation Fourier = new FourierTransformation(fileToRead);
	RegionGrowth Reg = new RegionGrowth(fileToRead);
	HistogramAction hist = new HistogramAction();
	hist.showTheThing(ReadImage);
	ImageDisplay PrimeImage = new ImageDisplay(ReadImage, false);
	
	invokeFrame(1, frameX, frameY, PrimeImage, 0);
	
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
											+ " (8)> Transformacja Raleigha.\n"
											+ " ============================\n"
											+ " (9)> Fourier: Filtr Highpass.\n"
											+ "(10)> Fourier: Filtr Lowpass.\n"
											+ "(11)> Fourier: Filtr Bandpass.\n"
											+ "(12)> Fourier: Filtr Bandblock.\n"
											+ "(13)> Fourier: Filtr EdgeDetect\n"
											+ " ============================\n"
											+ "(14)> Segmentacja: Region Growth.\n"));
		secondFlowControl = true;
		while(secondFlowControl == true){
			if (choice == 1){
				innerchoice = JOptionPane.showInputDialog("Rozjaœniæ / Przyciemniæ?\n(+/-)");
				if (innerchoice.equals("+")) adjustTrigger = true;
				else adjustTrigger = false;
				value = Integer.parseInt(JOptionPane.showInputDialog("Podaj wartoœæ:"));
				
				ReadImage.brightnessAdjust(adjustTrigger, value);
				ImageDisplay AlterImage = new ImageDisplay(ReadImage, true);
				invokeFrame(2, frameX, frameY, AlterImage, 0);
				ReadImage.calcPSNR();
			}
			else if (choice == 2){
				value = Integer.parseInt(JOptionPane.showInputDialog("Podaj wartoœæ:"));
				
				ReadImage.contrastAdjust(value);
				ImageDisplay AlterImage = new ImageDisplay(ReadImage, true);
				invokeFrame(2, frameX, frameY, AlterImage, 0);
				ReadImage.calcPSNR();
			}
			else if (choice == 3){				
				ReadImage.invertAdjust();
				ImageDisplay AlterImage = new ImageDisplay(ReadImage, true);
				invokeFrame(2, frameX, frameY, AlterImage, 0);
				ReadImage.calcPSNR();
			}
			else if (choice == 4){
				ReadImage.meanFilter();
				ImageDisplay AlterImage = new ImageDisplay(ReadImage, true);
				invokeFrame(2, frameX, frameY, AlterImage, 0);
				ReadImage.calcPSNR();
			}
			else if (choice == 5){
				ReadImage.medianFilter();
				ImageDisplay AlterImage = new ImageDisplay(ReadImage, true);
				invokeFrame(2, frameX, frameY, AlterImage, 0);
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
				invokeFrame(2, frameX, frameY, AlterImage, 0);
				ReadImage.calcPSNR();
			}
			else if (choice == 7){
				value = Integer.parseInt(JOptionPane.showInputDialog("Podaj wartoœæ 'R'"));
				
				ReadImage.Rosenfeld(value);
				ImageDisplay AlterImage = new ImageDisplay(ReadImage, true);
				invokeFrame(2, frameX, frameY, AlterImage, 0);
				ReadImage.calcPSNR();
			}
			else if (choice == 8){
				value = Integer.parseInt(JOptionPane.showInputDialog("Podaj wartoœæ minimaln¹."));
				value2 = Integer.parseInt(JOptionPane.showInputDialog("Podaj wartoœæ alpha."));
				
				ReadImage.transformRaleigh(value, value2, hist);
				ImageDisplay AlterImage = new ImageDisplay(ReadImage, true);
				invokeFrame(2, frameX, frameY, AlterImage, 0);
				ReadImage.calcPSNR();
			}
			else if (choice == 9){
				Fourier.FFTstandard();
				Fourier.exec_FLIP();
				Fourier.visualize("FOUR");
				ImageDisplay AlterImage = new ImageDisplay(Fourier, true);
				invokeFrame(2, frameX, frameY, AlterImage, 0);
				Fourier.visualize("PHASE");
				AlterImage = new ImageDisplay(Fourier, true);
				invokeFrame(2, frameX, frameY, AlterImage, frameY);
				
				fourValue1 = Double.parseDouble(JOptionPane.showInputDialog("Podaj promieñ maski filtru."));
				Fourier.filterHighpass(fourValue1);
				
				Fourier.visualize("FOUR");
				AlterImage = new ImageDisplay(Fourier, true);
				invokeFrame(2, frameX, frameY, AlterImage, frameY);
				
				Fourier.exec_FLIP();
				Fourier.FFTinverse();
				
				Fourier.visualize("REAL");
				AlterImage = new ImageDisplay(Fourier, true);
				invokeFrame(2, frameX, frameY, AlterImage, 0);
			}
			else if (choice == 10){
				Fourier.FFTstandard();
				Fourier.exec_FLIP();
				Fourier.visualize("FOUR");
				ImageDisplay AlterImage = new ImageDisplay(Fourier, true);
				invokeFrame(2, frameX, frameY, AlterImage, 0);
				Fourier.visualize("PHASE");
				AlterImage = new ImageDisplay(Fourier, true);
				invokeFrame(2, frameX, frameY, AlterImage, frameY);
				
				fourValue1 = Double.parseDouble(JOptionPane.showInputDialog("Podaj promieñ maski filtru."));
				Fourier.filterLowpass(fourValue1);
				
				Fourier.visualize("FOUR");
				AlterImage = new ImageDisplay(Fourier, true);
				invokeFrame(2, frameX, frameY, AlterImage, frameY);
				
				Fourier.exec_FLIP();
				Fourier.FFTinverse();
				
				Fourier.visualize("REAL");
				AlterImage = new ImageDisplay(Fourier, true);
				invokeFrame(2, frameX, frameY, AlterImage, 0);
			}
			else if (choice == 11){
				Fourier.FFTstandard();
				Fourier.exec_FLIP();
				Fourier.visualize("FOUR");
				ImageDisplay AlterImage = new ImageDisplay(Fourier, true);
				invokeFrame(2, frameX, frameY, AlterImage, 0);
				Fourier.visualize("PHASE");
				AlterImage = new ImageDisplay(Fourier, true);
				invokeFrame(2, frameX, frameY, AlterImage, frameY);
				
				fourValue1 = Double.parseDouble(JOptionPane.showInputDialog("Podaj promieñ pocz¹tkowy maski filtru."));
				fourValue2 = Double.parseDouble(JOptionPane.showInputDialog("Podaj promieñ koñcowy maski filtru."));
				Fourier.filterBandpass(fourValue1, fourValue2);
				
				Fourier.visualize("FOUR");
				AlterImage = new ImageDisplay(Fourier, true);
				invokeFrame(2, frameX, frameY, AlterImage, frameY);
				
				Fourier.exec_FLIP();
				Fourier.FFTinverse();
				
				Fourier.visualize("REAL");
				AlterImage = new ImageDisplay(Fourier, true);
				invokeFrame(2, frameX, frameY, AlterImage, 0);	
			}
			else if (choice == 12){
				Fourier.FFTstandard();
				Fourier.exec_FLIP();
				Fourier.visualize("FOUR");
				ImageDisplay AlterImage = new ImageDisplay(Fourier, true);
				invokeFrame(2, frameX, frameY, AlterImage, 0);
				Fourier.visualize("PHASE");
				AlterImage = new ImageDisplay(Fourier, true);
				invokeFrame(2, frameX, frameY, AlterImage, frameY);
				
				fourValue1 = Double.parseDouble(JOptionPane.showInputDialog("Podaj promieñ pocz¹tkowy maski filtru."));
				fourValue2 = Double.parseDouble(JOptionPane.showInputDialog("Podaj promieñ koñcowy maski filtru."));
				Fourier.filterBandblock(fourValue1, fourValue2);
				
				Fourier.visualize("FOUR");
				AlterImage = new ImageDisplay(Fourier, true);
				invokeFrame(2, frameX, frameY, AlterImage, frameY);
				
				Fourier.exec_FLIP();
				Fourier.FFTinverse();
				
				Fourier.visualize("REAL");
				AlterImage = new ImageDisplay(Fourier, true);
				invokeFrame(2, frameX, frameY, AlterImage, 0);
			}
			else if (choice == 13){
				Fourier.FFTstandard();
				Fourier.exec_FLIP();
				Fourier.visualize("FOUR");
				ImageDisplay AlterImage = new ImageDisplay(Fourier, true);
				invokeFrame(2, frameX, frameY, AlterImage, 0);
				Fourier.visualize("PHASE");
				AlterImage = new ImageDisplay(Fourier, true);
				invokeFrame(2, frameX, frameY, AlterImage, frameY);
				
				fourValue1 = Double.parseDouble(JOptionPane.showInputDialog("Podaj promieñ pocz¹tkowy maski filtru."));
				fourValue2 = Double.parseDouble(JOptionPane.showInputDialog("Podaj promieñ koñcowy maski filtru."));
				Fourier.filterEdge(fourValue1, fourValue2);
				
				Fourier.visualize("FOUR");
				AlterImage = new ImageDisplay(Fourier, true);
				invokeFrame(2, frameX, frameY, AlterImage, frameY);
				
				Fourier.exec_FLIP();
				Fourier.FFTinverse();
				
				Fourier.visualize("REAL");
				AlterImage = new ImageDisplay(Fourier, true);
				invokeFrame(2, frameX, frameY, AlterImage, 0);
			}
			else if (choice == 14){
				value = Integer.parseInt(JOptionPane.showInputDialog("Podaj koordynat X piksela startowego."));
				value2 = Integer.parseInt(JOptionPane.showInputDialog("Podaj koordynat Y piksela startowego."));
				fourValue1 = Double.parseDouble(JOptionPane.showInputDialog("Podaj zakres (czu³oœæ) granicy koloru piksela.\n"
																			+ "   Najlepsze wyniki dla granicy 9.8 - 12.5"));
				Reg.visualize(fourValue1, value, value2);
				ImageDisplay AlterImage = new ImageDisplay(Reg, true);
				invokeFrame(2, frameX, frameY, AlterImage, 0);
			}
			innerchoice = JOptionPane.showInputDialog("Powtórzyæ z innymi ustawieniami?\n(y/n)");
			System.out.println(innerchoice);
			if (innerchoice.equals("y")) ;
			else break;
		}
		innerchoice = JOptionPane.showInputDialog("Zapisaæ plik z przetworzonym obrazem?\n(y/n)");
		if (innerchoice.equals("y")){
			if (choice <=8){
				fileToSave = JOptionPane.showInputDialog("Podaj nazwe pliku. \n"
						+ "									[PAMIÊTAJ O ROZSZERZENIU PO KROPCE!! .png!!]");
				ReadImage.saveImage(fileToSave);
			}
			if (choice>8 && choice<14){
				fileToSave = JOptionPane.showInputDialog("Podaj nazwe pliku. \n"
						+ "									[PAMIÊTAJ O ROZSZERZENIU PO KROPCE!! .png!!]");
				Fourier.saveImage(fileToSave);
			
			}
			if (choice == 14){
				fileToSave = JOptionPane.showInputDialog("Podaj nazwe pliku. \n"
						+ "									[PAMIÊTAJ O ROZSZERZENIU PO KROPCE!! .png!!]");
				Reg.saveImage(fileToSave);
			}
		}
		
		innerchoice = JOptionPane.showInputDialog("Powtórzyæ z innymi ustawieniami?\n(y/n)");
		if (innerchoice.equals("y")) firstFlowControl = true;
		else {
			firstFlowControl = false;
			break;
		}
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
	
	regGrow.visualize(8, 100, 250);

	//fftTrans.FFTstandard();
	//fftTrans.exec_FLIP();
	//fftTrans.filterBandblock(12,120);
	//fftTrans.filterDisplacement(256, 256);
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