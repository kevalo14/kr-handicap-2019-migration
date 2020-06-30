package be.kempischeregionale.handicap;

import be.kempischeregionale.handicap.models.Handicap;
import be.kempischeregionale.handicap.models.Proef;
import be.kempischeregionale.handicap.models.Punten;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

//@SpringBootApplication
public class HandicapReadJumping {

	private static Logger LOG = LoggerFactory.getLogger(HandicapReadJumping.class);

	private static final String DIRECTORY_INPUT = "C:\\handicapkr\\";
    private static final String DIRECTORY_OUTPUT = "C:\\handicapkr\\output\\";
	private static final String DIRECTORY_BACKUP = "C:\\handicapkr\\backup\\";

	public static Map<String, String> ruiters = new HashMap<>();
	public static Map<String, String> paarden = new HashMap<>();
	public static Map<String, Handicap> nieuweHandicap2019_combinatie_handicao = new HashMap<>();
	public static Map<String, Handicap> puntenHuidigeJumping = new HashMap<>();

	public static Map<String, Proef> proeven = new HashMap<>();

	public static Map<String, Integer> paardVanHetJaar = new HashMap<>();

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			LOG.error("Geef een jumpingnummer op !");
			return;
		}

		String jumpingNummer = args[0];

		if (StringUtils.isEmpty(jumpingNummer)) {
			LOG.error("Geef een jumpingnummer op !");
			return;
		}

		cleanupFolder();

		readProeven();
		readRuiters();
		readPaarden();

		readPreviousJumping();

		readPaardVanHetJaar();

		boolean success = readJumping(jumpingNummer);

		if (success) {
			LOG.info("Bestanden succesvol ge-exporteerd !");
		} else {
			LOG.info("Jumping niet correct ingelezen en verwerkt !");
		}

	}

	private static void cleanupFolder() {
		LOG.info("Opkuisen output direcctory ...");
		File dir =new File(DIRECTORY_OUTPUT);
		for (File file: dir.listFiles()) {
			if (!file.isDirectory()) {
				file.delete();
			}
		}

	}

	private static String getFileVersionNumber() {
		DateTimeFormatter timeColonFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		return timeColonFormatter.format(LocalDateTime.now());
	}

	private static void readProeven() throws Exception {
		LOG.info("Inlezen van proeven ...");

        String fileProef = DIRECTORY_INPUT + "proeven.xlsx";

        File excelFile = new File(fileProef);
        FileInputStream fis = new FileInputStream(excelFile);

        // we create an XSSF Workbook object for our XLSX Excel File
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
		// we get first sheet
		XSSFSheet sheet = workbook.getSheetAt(0);

		// we iterate on rows
		Iterator<Row> rowIt = sheet.iterator();
		Row row = rowIt.next();

		while(rowIt.hasNext()) {
			row = rowIt.next();

			String jumping = getCellValue(row.getCell(0));
			String proef = getCellValue(row.getCell(1));
			String type = getCellValue(row.getCell(2));
			boolean isPony = getCellValue(row.getCell(3)).equalsIgnoreCase("pony");

			proeven.put(jumping + "_" + proef, new Proef(jumping, proef, type, isPony));
		}

		workbook.close();
		fis.close();
	}

	private static String getCellValue(Cell cell) {
		String cellValue;
		if ( CellType.NUMERIC == cell.getCellType()) {
			cellValue = String.valueOf((int)cell.getNumericCellValue());
		} else if ( CellType.STRING == cell.getCellType()) {
			cellValue = cell.getStringCellValue();
		} else {
			throw new IllegalArgumentException("Incorrect input while reading proeven");
		}
		return cellValue;
	}

	private static void readRuiters() throws Exception {
		LOG.info("Inlezen van ruiters ...");

		String fileProef = DIRECTORY_INPUT +  "ruiters.csv";

		Reader in = new FileReader(fileProef);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').parse(in);

		for (CSVRecord record : records) {
			String id = record.get(0);
			String naam = record.get(1) + " " + record.get(2);

			ruiters.put(id, naam);
		}
	}

	private static void readPaarden() throws Exception {
		LOG.info("Inlezen van paarden ...");

		String fileProef = DIRECTORY_INPUT + "paarden.csv";

		Reader in = new FileReader(fileProef);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').parse(in);
//		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(';').parse(in);

		for (CSVRecord record : records) {
			String id = record.get(0);
			String naam = record.get(1);

			paarden.put(id, naam);
		}
	}

	private static String getLatestJumpingFile() {
		File diretory = new File(DIRECTORY_BACKUP);

		FileFilter fileFilter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getAbsoluteFile().getName().startsWith("export");
			}
		};
		File[] filteredFiles = diretory.listFiles(fileFilter);
		Arrays.sort(filteredFiles);
		return filteredFiles[filteredFiles.length - 1].getName();
	}

	private static String getLatesPaardvanhetjaarFile() {
		File diretory = new File(DIRECTORY_BACKUP);

		FileFilter fileFilter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getAbsoluteFile().getName().startsWith("paardvanhetjaar");
			}
		};
		File[] filteredFiles = diretory.listFiles(fileFilter);
		Arrays.sort(filteredFiles);
		return filteredFiles[filteredFiles.length - 1].getName();
	}

	private static void readPreviousJumping() throws Exception {
		LOG.info("Inlezen huidige handicap stand ...");

		String filename = getLatestJumpingFile();
		String fileProef = DIRECTORY_BACKUP + filename;

		Reader in = new FileReader(fileProef);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').parse(in);

		for (CSVRecord record : records) {
			if (StringUtils.isEmpty( record.get(0).trim()) || StringUtils.isEmpty( record.get(1).trim())
				|| "rider".equalsIgnoreCase(record.get(0))) {
				LOG.info("SKIP");
				continue;
			}

			String ruiter = record.get(0);
			String paard = record.get(1);
			int punten = Integer.parseInt(record.get(2));
			String proef = record.get(3);

			Handicap handicap = new Handicap(ruiter, paard, punten, proef);

			nieuweHandicap2019_combinatie_handicao.put(ruiter + "_" + paard, handicap);
		}

	//	LOG.info("handicap : " + nieuweHandicap2019_combinatie_handicao.size());
	}

	private static boolean readJumping(String jumping) throws Exception {
		String fileProef = DIRECTORY_INPUT + "jumpings/" + jumping + ".csv";

		File proefFile = new File(fileProef);
		if (!proefFile.exists()) {
			LOG.error("Jumping " + jumping + " kan niet gevonden worden onder " + DIRECTORY_INPUT + "jumpings");
			return false;
		}

		Reader in = new FileReader(fileProef);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(';').parse(in);

		int i=0;

		Map<String, List<Punten>> jumpingPunten = new HashMap<>();

		for (CSVRecord record : records) {
			String proef = record.get(5);
			String ruiter = record.get(6);
			String paard = record.get(8);
			int position = Integer.parseInt(record.get(10));
			boolean prizeMoney = !StringUtils.isEmpty(record.get(11));

			i++;
		//	LOG.info("proef <" + proef + "> - ruiter <" + ruiter + "> - paard <" + paard + "> - position <" + position + "> - prizeMoney <" + prizeMoney + " >");

			if (prizeMoney) {

				String combinatie = ruiter + "_" + paard;
				// Get huidige punten

				Handicap handicap = nieuweHandicap2019_combinatie_handicao.get(combinatie);
				int punten = 0;
				if (handicap != null) {
					punten = handicap.getPunten();
				}

				List<Punten> jumpingPuntenVoorCombinatie = jumpingPunten.get(combinatie);
				if (!CollectionUtils.isEmpty(jumpingPuntenVoorCombinatie)) {
					for (Punten jumpingPuntVoorCombinatie : jumpingPuntenVoorCombinatie) {
						punten = punten + jumpingPuntVoorCombinatie.getPunten() + jumpingPuntVoorCombinatie.getExtraPunten();
					}
				} else {
					jumpingPunten.put(combinatie, new ArrayList<>());
				}

				// get proeven uit db

				Proef proefConfig = proeven.get(jumping + "_" +  proef);
				if (proefConfig == null) {
					LOG.error("Proef " + proef + " not found");
				}

				Punten recordProefPunten = new Punten(jumping + "_" + proef);
				jumpingPunten.get(combinatie).add(recordProefPunten);

				switch (proefConfig.getType()) {
					case "A" : calculatePunten(0, punten, position, jumping + "_" + proef, recordProefPunten);
						break;
					case "B": calculatePunten(1201, punten, position, jumping + "_" + proef, recordProefPunten);
						break;
					case "1": calculatePunten(2401, punten, position, jumping + "_" + proef, recordProefPunten);
						break;
					case "2": calculatePunten(3601, punten, position, jumping + "_" + proef, recordProefPunten);
						break;
					case "JA": calculateJuniorsPunten(0, punten, position, jumping + "_" + proef, recordProefPunten);
						break;
					case "JB": calculateJuniorsPunten(1201, punten, position, jumping + "_" + proef, recordProefPunten);
						break;
				}

				// Paard van het jaar
				if (!proefConfig.isPony()) {
					Integer paardPunten = paardVanHetJaar.get(combinatie);
					if (paardPunten == null) {
						paardPunten = 0;
					}

					switch (position) {
						case 1 : paardPunten += 6; break;
						case 2 : paardPunten += 4; break;
						case 3 : paardPunten += 3; break;
						case 4 : paardPunten += 2; break;
						default : paardPunten += 1; break;
					}

					paardVanHetJaar.put(combinatie, paardPunten);
				}
			}

		}

	//	LOG.info("i : " + i);

		printPunten(jumping, jumpingPunten);
		exportPunten(jumping, jumpingPunten);
		exportPaardVanHetJaar(jumping);

		return true;
	}

	private static void readPaardVanHetJaar() throws Exception {
		String exportPaard = DIRECTORY_BACKUP + getLatesPaardvanhetjaarFile();

		Reader in = new FileReader(exportPaard);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').parse(in);

		for (CSVRecord record : records) {
			String ruiter = record.get(0);
			String paard = record.get(1);
			int punten = Integer.parseInt(record.get(2));
			paardVanHetJaar.put(ruiter + "_" + paard, punten);
		}

	//	LOG.info("paardvanhetjaar : " + paardVanHetJaar.size());
	}

	private static void exportPaardVanHetJaar(String jumping) throws Exception {
		LOG.info("Exporteer paard/pony van het jaar..");

		List<Handicap> paardVanHetJaarToExport = new ArrayList<>();

		for (Map.Entry<String, Integer> pvhj : paardVanHetJaar.entrySet()) {
			String[] ruiterPaard = pvhj.getKey().split("_");
			Handicap handicap = new Handicap(ruiterPaard[0], ruiterPaard[1], pvhj.getValue(), "");
			paardVanHetJaarToExport.add(handicap);
		}

		String exportPaard = DIRECTORY_OUTPUT + "paardvanhetjaar-" + jumping + "-" + getFileVersionNumber() + ".csv";
		FileWriter out = new FileWriter(exportPaard);
		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT)) {
			printer.printRecords(paardVanHetJaarToExport);
		}

		String exportPaardBackuo = DIRECTORY_BACKUP + "paardvanhetjaar-" + jumping + "-" + getFileVersionNumber() + ".csv";
		FileWriter ou2t = new FileWriter(exportPaardBackuo);
		try (CSVPrinter printer2 = new CSVPrinter(ou2t, CSVFormat.DEFAULT)) {
			printer2.printRecords(paardVanHetJaarToExport);
		}
	}

	private static void exportPunten(String jumping, Map<String, List<Punten>> jumpingPunten) throws Exception {
		LOG.info("Exporteer handicap..");

		Map<String, Handicap> exportHandicapNaWedstrijd = new HashMap<>();

		for (Map.Entry<String, List<Punten>> mapEntry : jumpingPunten.entrySet()) {
			//LOG.info("START " + mapEntry.getKey());

			Handicap handicap = nieuweHandicap2019_combinatie_handicao.get(mapEntry.getKey());
			int punten = 0;
			if (handicap != null) {
				punten = handicap.getPunten();
			}

			List<Punten> jumpingPuntenVoorCombinatie = jumpingPunten.get(mapEntry.getKey());
			if (!CollectionUtils.isEmpty(jumpingPuntenVoorCombinatie)) {
				for (Punten jumpingPuntVoorCombinatie : jumpingPuntenVoorCombinatie) {
					punten = punten + jumpingPuntVoorCombinatie.getPunten() + jumpingPuntVoorCombinatie.getExtraPunten();
				}
			}

			String ruiter = mapEntry.getKey().split("_")[0];
			String paard = mapEntry.getKey().split("_")[1];

			String reeks = "";
			if (punten < 1201) {
				reeks = "A";
			} else if (punten < 2401) {
				reeks = "B";
			} else if (punten < 3601) {
				reeks = "1";
			} else {
				reeks = "2";
			}

			if (StringUtils.isEmpty(reeks)) {
				LOG.error("KAN NIET !!!!!!!!!!!");
			}

			Handicap handicapNaWedstrijd = new Handicap(ruiter, paard, punten, reeks);
			exportHandicapNaWedstrijd.put(mapEntry.getKey(), handicapNaWedstrijd);

			//LOG.info("STOP " + mapEntry.getKey());

		}

		// Niet meegedaan aan wedstrijd
		for (Map.Entry<String, Handicap> map2Entry : nieuweHandicap2019_combinatie_handicao.entrySet()) {

			if (!exportHandicapNaWedstrijd.containsKey(map2Entry.getKey())) {
				exportHandicapNaWedstrijd.put(map2Entry.getKey(), map2Entry.getValue());
			}

		}

		String exportHandicap = DIRECTORY_OUTPUT + "export-" + jumping + "-" + getFileVersionNumber() + ".csv";
		FileWriter out = new FileWriter(exportHandicap);

		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT)) {
			printer.print("rider;horse;points;serie");
			printer.println();
			printer.printRecords(exportHandicapNaWedstrijd.values());
		}

		String exportHandicapBackuo = DIRECTORY_BACKUP + "export-" + jumping + "-" + getFileVersionNumber() + ".csv";
		FileWriter out2 = new FileWriter(exportHandicapBackuo);
		try (CSVPrinter printe2r = new CSVPrinter(out2, CSVFormat.DEFAULT)) {
			printe2r.printRecords(exportHandicapNaWedstrijd.values());
		}
	}

	private static void printPunten(String jumping, Map<String, List<Punten>> jumpingPunten) throws Exception {
		LOG.info("Wegschrijvan van punten..");

		List<String> summaryList = new ArrayList<>();
		for (Map.Entry<String, List<Punten>> mapEntry : jumpingPunten.entrySet()) {
		//	LOG.info("START " + mapEntry.getKey());

			for (Punten punten : mapEntry.getValue()) {
				summaryList.add(mapEntry.getKey() + punten.toString());
//				LOG.info(mapEntry.getKey() + punten.toString());
			}

		//	LOG.info("STOP " + mapEntry.getKey());
		}

/*
		String exportHandicap = DIRECTORY_OUTPUT + "summary-" + jumping + "-" + getFileVersionNumber() + ".txt";
		FileWriter out = new FileWriter(exportHandicap);
		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT)) {
			printer.printRecords(summaryList);
		}
*/

		String exportHandicapBackup = DIRECTORY_BACKUP + "summary-" + jumping + "-" + getFileVersionNumber() + ".txt";
		FileWriter out2 = new FileWriter(exportHandicapBackup);
		try (CSVPrinter printer2 = new CSVPrinter(out2, CSVFormat.DEFAULT)) {
			printer2.printRecords(summaryList);
		}

	}

	private static void calculatePunten(int minimumPunten, int huidigePunten, int position, String jumpingProef, Punten punten) {
		switch (position) {
			case 1:
				punten.setPunten(200);
				punten.setPaardPonyPunten(6);

				if (huidigePunten < minimumPunten) {
					punten.setExtraPunten(minimumPunten - (huidigePunten));
					punten.setReasonExtraPunten("Prijs in " + jumpingProef + " (" + huidigePunten + ")");
				}

				break;
			case 2:
				punten.setPunten(150);
				punten.setPaardPonyPunten(4);
				if (huidigePunten < minimumPunten) {
					punten.setExtraPunten(minimumPunten - (huidigePunten));
					punten.setReasonExtraPunten("Prijs in " + jumpingProef + " (" + huidigePunten + ")");
				}
				break;
			case 3:
				punten.setPunten(100);
				punten.setPaardPonyPunten(3);
				if (huidigePunten < minimumPunten) {
					punten.setExtraPunten(minimumPunten - (huidigePunten));
					punten.setReasonExtraPunten("Prijs in " + jumpingProef + " (" + huidigePunten + ")");
				}
				break;
			case 4:
				punten.setPunten(75);
				punten.setPaardPonyPunten(2);
				if (huidigePunten < minimumPunten) {
					punten.setExtraPunten(minimumPunten - (huidigePunten));
					punten.setReasonExtraPunten("Prijs in " + jumpingProef + " (" + huidigePunten + ")");
				}
				break;
			default:
				punten.setPunten(50);
				punten.setPaardPonyPunten(1);
				if (huidigePunten < minimumPunten) {
					punten.setExtraPunten(minimumPunten - (huidigePunten));
					punten.setReasonExtraPunten("Prijs in " + jumpingProef + " (" + huidigePunten + ")");
				}
				break;

		}

	}

	private static void calculateJuniorsPunten(int minimumPunten, int huidigePunten, int position, String jumpingProef, Punten punten) {
		switch (position) {
			case 1:
				punten.setPunten(100);
				punten.setPaardPonyPunten(6);

				if (huidigePunten < minimumPunten) {
					punten.setExtraPunten(minimumPunten - (huidigePunten));
					punten.setReasonExtraPunten("Prijs in " + jumpingProef + " (" + huidigePunten + ")");
				}

				break;
			case 2:
				punten.setPunten(75);
				punten.setPaardPonyPunten(4);
				if (huidigePunten < minimumPunten) {
					punten.setExtraPunten(minimumPunten - (huidigePunten));
					punten.setReasonExtraPunten("Prijs in " + jumpingProef + " (" + huidigePunten + ")");
				}
				break;
			default:
				punten.setPunten(50);
				punten.setPaardPonyPunten(1);
				if (huidigePunten < minimumPunten) {
					punten.setExtraPunten(minimumPunten - (huidigePunten));
					punten.setReasonExtraPunten("Prijs in " + jumpingProef + " (" + huidigePunten + ")");
				}
				break;

		}

	}

}

