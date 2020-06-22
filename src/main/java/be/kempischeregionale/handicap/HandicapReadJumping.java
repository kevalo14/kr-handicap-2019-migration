package be.kempischeregionale.handicap;

import be.kempischeregionale.handicap.models.Handicap;
import be.kempischeregionale.handicap.models.Proef;
import be.kempischeregionale.handicap.models.Punten;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@SpringBootApplication
public class HandicapReadJumping {

	private static Logger LOG = LoggerFactory.getLogger(HandicapReadJumping.class);

	private static String huidige_jumping = "5038";
	private static String vorige_jumping = "5037";

	private static final String DIRECTORY_INPUT = "C:\\handicap\\src\\main\\resources\\files\\input\\";
    private static final String DIRECTORY_OUTPUT = "C:\\handicap\\src\\main\\resources\\files\\output\\";

	public static Map<String, String> ruiters = new HashMap<>();
	public static Map<String, String> paarden = new HashMap<>();
	public static Map<String, Handicap> nieuweHandicap2019_combinatie_handicao = new HashMap<>();
	public static Map<String, Handicap> puntenHuidigeJumping = new HashMap<>();

	public static Map<String, Proef> proeven = new HashMap<>();

	public static Map<String, Integer> paardVanHetJaar = new HashMap<>();

	public static void main(String[] args) throws Exception {
//		SpringApplication.run(HandicapApplication.class, args);

		proeven.put("5001_3", new Proef("5001", "3", "A"));
		proeven.put("5001_4", new Proef("5001", "4", "A"));
		proeven.put("5001_5", new Proef("5001", "5", "1"));
		proeven.put("5001_6", new Proef("5001", "6", "2"));
		proeven.put("5001_7", new Proef("5001", "7", "1"));
		proeven.put("5001_8", new Proef("5001", "8", "2"));
		proeven.put("5041_7", new Proef("5001", "7", "A"));
		proeven.put("5041_10", new Proef("5001", "10", "A"));
		proeven.put("5041_11", new Proef("5001", "11", "B"));
		proeven.put("5041_12", new Proef("5001", "12", "A"));
		proeven.put("5041_13", new Proef("5001", "13", "B"));

		readHandicap2018();
		readRuiters();
		readPaarden();
		readPaardVanHetJaar();

		readJumping(huidige_jumping);
	}

	private static void readRuiters() throws Exception {
		String fileProef = DIRECTORY_INPUT +  "ruiters.csv";

		Reader in = new FileReader(fileProef);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(';').parse(in);

		for (CSVRecord record : records) {
			String id = record.get(0);
			String naam = record.get(1) + " " + record.get(2);

			ruiters.put(id, naam);
		}
	}

	private static void readPaarden() throws Exception {
		String fileProef = DIRECTORY_INPUT + "paarden.csv";

		Reader in = new FileReader(fileProef);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(';').parse(in);

		for (CSVRecord record : records) {
			String id = record.get(0);
			String naam = record.get(1);

			paarden.put(id, naam);
		}
	}

	private static void readHandicap2018() throws Exception {
		String fileProef = DIRECTORY_OUTPUT + "new_export_" + vorige_jumping + ".csv";

		Reader in = new FileReader(fileProef);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').parse(in);

		for (CSVRecord record : records) {
			if (StringUtils.isEmpty( record.get(0).trim()) || StringUtils.isEmpty( record.get(1).trim())) {
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

		LOG.info("handicap : " + nieuweHandicap2019_combinatie_handicao.size());
	}

	private static void readJumping(String jumping) throws Exception {
		String fileProef = DIRECTORY_INPUT + "jumpings\\" + jumping + ".csv";

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

		LOG.info("i : " + i);

		printPunten(jumpingPunten);
		exportPunten(jumping, jumpingPunten);
		exportPaardVanHetJaar();
	}

	private static void readPaardVanHetJaar() throws Exception {
		String exportPaard = DIRECTORY_OUTPUT + "paardvanhetjaar.csv";

		Reader in = new FileReader(exportPaard);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').parse(in);

		for (CSVRecord record : records) {
			String ruiter = record.get(0);
			String paard = record.get(1);
			int punten = Integer.parseInt(record.get(2));
			paardVanHetJaar.put(ruiter + "_" + paard, punten);
		}

		LOG.info("paardvanhetjaar : " + paardVanHetJaar.size());
	}

	private static void exportPaardVanHetJaar() throws Exception {
		List<Handicap> paardVanHetJaarToExport = new ArrayList<>();

		for (Map.Entry<String, Integer> pvhj : paardVanHetJaar.entrySet()) {
			String[] ruiterPaard = pvhj.getKey().split("_");
			Handicap handicap = new Handicap(ruiterPaard[0], ruiterPaard[1], pvhj.getValue(), "");
			paardVanHetJaarToExport.add(handicap);
		}

		String exportPaard = DIRECTORY_OUTPUT + "paardvanhetjaar.csv";
		FileWriter out = new FileWriter(exportPaard);
		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT)) {
			printer.printRecords(paardVanHetJaarToExport);
		}
	}

	private static void exportPunten(String jumping, Map<String, List<Punten>> jumpingPunten) throws Exception {
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

		String exportHandicap = DIRECTORY_OUTPUT + "new_export_" + jumping + ".csv";
		FileWriter out = new FileWriter(exportHandicap);
		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT)) {
			printer.printRecords(exportHandicapNaWedstrijd.values());
		}
	}


	private static void printPunten(Map<String, List<Punten>> jumpingPunten) {
		for (Map.Entry<String, List<Punten>> mapEntry : jumpingPunten.entrySet()) {
		//	LOG.info("START " + mapEntry.getKey());

			for (Punten punten : mapEntry.getValue()) {
				LOG.info(mapEntry.getKey() + punten.toString());
			}

		//	LOG.info("STOP " + mapEntry.getKey());
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

