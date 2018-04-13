package Database;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
import java.util.Vector;
import java.util.stream.Stream;

public class SVMPredictor {
	private Vector<SupportVector> supportVectors;
	private double b;
	private static final int FACTOR_LENGTH =15;
	public SVMPredictor(String path) {
		supportVectors = new Vector<SupportVector>();
		try {
			BufferedReader file = new BufferedReader(new FileReader(path));
			load(file);
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public double predict(double ... x)
	{
		double sum = 0;
		for(SupportVector v :supportVectors)
		{
			double vectorSum= 0;
			for(int i =0;i<v.vectors.length;i++)
				vectorSum += v.vectors[i]*x[i];
			sum+=Math.pow(vectorSum,v.pow)*v.factor;
		}
		return sum + b;
	}
	
	public void load(BufferedReader file) throws IOException
	{
		for(String line = file.readLine(); line != null; line = file.readLine())
		{
			double factor= getFactor(line);
			if(line.length() > 15)
			{
				double[] vectors = getVectors(line);
				supportVectors.add(new SupportVector(vectors, factor, 2));	
			}
			else
				b= factor;
		}
	}


	private static double getFactor(String line) {
		String factorPart = line.substring(8, FACTOR_LENGTH);
		double factor = Double.parseDouble(factorPart);
		return (line.contains("+")? factor: -factor);
	}
	private static double[] getVectors(String line) {
		
		String[] vectorsPart = line.substring(line.indexOf('<')+1, line.indexOf(" >")).split(" ");
		double[] vectors = new double[vectorsPart.length];
		for(int i = 0; i< vectors.length;i ++)
			vectors[i] = Double.parseDouble(vectorsPart[i]);
		return vectors;
	}
		
	private class SupportVector{
		public double[] vectors;
		public double factor;
		public int pow;
		
		public SupportVector(double[] vectors,double factor,int pow) {
			this.vectors = vectors;
			this.factor = factor;
			this.pow = pow;
		}
	}
}
