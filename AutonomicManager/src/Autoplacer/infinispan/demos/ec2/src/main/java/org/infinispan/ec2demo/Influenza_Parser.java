/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009 Red Hat Inc. and/or its affiliates and other
 * contributors as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.infinispan.ec2demo;

import org.infinispan.util.SysPropertyActions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * @author noconnor@redhat.com
 * 
 */
public class Influenza_Parser {

	public List<Influenza_N_P_CR_Element> parseFile(String fileName) {
		return this.processFile(fileName, null);
	}

	public List<Influenza_N_P_CR_Element> processFile(String fileName, ProteinCache cacheImpl) {
		List<Influenza_N_P_CR_Element> outList =  new ArrayList<Influenza_N_P_CR_Element>();
		System.out.println("Processing Influenza file " + fileName);
		try {
			Scanner scanner = new Scanner(new File(fileName.trim()));
			scanner.useDelimiter(SysPropertyActions.getProperty("line.separator"));
			while (scanner.hasNext()) {

				Influenza_N_P_CR_Element x = parseLine(scanner.next());
				outList.add(x);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Processed " + outList.size() + " records from file");
		return outList;
	}

	private static Influenza_N_P_CR_Element parseLine(String line) {
		Influenza_N_P_CR_Element currRec = new Influenza_N_P_CR_Element();

		Scanner lineScanner = new Scanner(line);
		lineScanner.useDelimiter("\t");
		while (lineScanner.hasNext()) {
			try {
				currRec.setGanNucleoid(lineScanner.next());
				try {
					while (true) {
						String protein_GAN = lineScanner.next();
						String protein_CR = lineScanner.next();
						currRec.setProtein_Data(protein_GAN, protein_CR);
					}
				} catch (NoSuchElementException ex) {
					// ignore exception
				}
			} catch (Exception ex) {
				System.out.println("Exception while processing line " + line);
			}
		}
		return currRec;
	}
}
