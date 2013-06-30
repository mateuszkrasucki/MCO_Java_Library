package mco_java_library_tests;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import methods.Electre.Criterium;
import methods.Electre.Criterium.Direction;
import methods.Electre.Data;
import methods.Electre.Electre1;
import methods.Electre.Electre4;
import methods.Electre.ElectreIs;

public class Electre_test {

	
	public static void test() {
		
		String altFile = "alternatives.csv";
		String criFile = "criteria.csv";
		BufferedReader br = null;
		String line = "";
		
		Data data = new Data();
		try {
			br = new BufferedReader(new FileReader(altFile));
			int i=0;
			while ((line = br.readLine()) != null) {
				String[] alternative = line.split(";");
				data.alternatives_.add(new Double[alternative.length]);
				for (int j=0; j<alternative.length; j++) {
					data.alternatives_.get(i)[j] = Double.valueOf(alternative[j]);
				}
				i++;
			}
			
			br = new BufferedReader(new FileReader(criFile));
			i=0;
			while ((line = br.readLine()) != null) {
				String[] criterium = line.split(";");
				data.criteria_.add(new Criterium());
				for (int j=0; j<criterium.length; j++) {
					data.criteria_.get(i).name = criterium[0];
					if (criterium[1]=="max")
						data.criteria_.get(i).direction = Direction.MAX;
					else
						data.criteria_.get(i).direction = Direction.MIN;
					data.criteria_.get(i).treshold = Double.valueOf(criterium[2]);
				}
				i++;
			}
			
			Electre1 el1 = new Electre1(data, 0.6, 0.2);
			el1.calculate();
    		Integer[] el1rank = el1.getRanking();
    		Integer[] el1rankpoints = el1.getRankingPoints_();
			for (int ii=0; ii<el1rank.length; ii++) 
				System.out.println(el1rank[ii]+" = " + el1rankpoints[ii]);

			ElectreIs elis = new ElectreIs(data, 0.1);
			elis.calculate();
    		Integer[] rankis = elis.getRanking();
    		Integer[] rankpoints = elis.getRankingPoints_();
			for (int ii=0; ii<rankis.length; ii++) 
				System.out.println(rankis[ii]+" = " + rankpoints[ii]);

			Electre4 eliv = new Electre4(data, 0.6, 0.2, 0.3);
			eliv.calculate();
    		Integer[] elivrank = eliv.getRanking();
    		Integer[] elivrankpoints = eliv.getRankingPoints_();
			for (int ii=0; ii<elivrank.length; ii++) 
				System.out.println(elivrank[ii]+" = " + elivrankpoints[ii]);
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
