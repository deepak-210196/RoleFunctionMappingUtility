package com.generatescript;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class RoleFunctionMappingUtility {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(".\\RoleFunctionMap.csv"))) {
			String line;
			boolean append = true;
			String filename = ".\\RoleFunctionMap.txt";
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename, append));
			int index = 0;
			while ((line = br.readLine()) != null) {
				if(index == 0) {
					index++;
					continue;
				}
				String[] values = line.split(",");
				String roleFinIdStr = "Select fin_id from dfuser.tbls_roles where name = '" + values[0] + "'";
				String functionFinIdStr = "Select fin_id from dfuser.tbls_functions where display_name = '" + values[1] + "'";
				String createRole = (values[2] != null && !"".equals(values[2]) && values[2].equals("Y")) ? "C" : "X";
				String readRole = (values[3] != null && !"".equals(values[3]) && values[3].equals("Y")) ? "R" : "X";
				String updateRole = (values[4] != null && !"".equals(values[4]) && values[4].equals("Y")) ? "U" : "X";
				String deleteRole = (values[5] != null && !"".equals(values[5]) && values[5].equals("Y")) ? "D" : "X";
				String roleStr = createRole + readRole + updateRole + deleteRole + "X";
				
				String deleteQueryStr = "delete from dfuser.tbls_roles_functions_int where ";
				deleteQueryStr = deleteQueryStr + "fin_id = concat((" + roleFinIdStr + "),'_',(" + functionFinIdStr + "));";
				writer.write(deleteQueryStr);
				writer.newLine();
				
				String insertQueryStr = "insert into dfuser.tbls_roles_functions_int values (";
				insertQueryStr = insertQueryStr + "concat((" + roleFinIdStr + "),'_',(" + functionFinIdStr + ")),";
				insertQueryStr = insertQueryStr + "(" + roleFinIdStr + "),";
				insertQueryStr = insertQueryStr + "(" + functionFinIdStr + "),";
				insertQueryStr = insertQueryStr + "'" + roleStr + "',";
				insertQueryStr = insertQueryStr
						+ "'N','N',current_date,'System',current_date,'System','System',current_date,current_date,1,'COMMITTED',-1);";

				writer.write(insertQueryStr);
				writer.newLine();
				writer.newLine();
			}
			writer.close();
		}
	}

}