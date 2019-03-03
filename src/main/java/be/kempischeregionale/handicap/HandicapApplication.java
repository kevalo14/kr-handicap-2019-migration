package be.kempischeregionale.handicap;

import be.kempischeregionale.handicap.models.Handicap;
import be.kempischeregionale.handicap.models.Proef;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@SpringBootApplication
public class HandicapApplication {

	private static Logger LOG = LoggerFactory.getLogger(HandicapApplication.class);

	private static final String DIRECTORY_INPUT = "C:\\handicap\\src\\main\\resources\\files\\input\\";
    private static final String DIRECTORY_OUTPUT = "C:\\handicap\\src\\main\\resources\\files\\output\\";

	public static Map<String, List<Handicap>> puntenBehaald2018 = new HashMap<>();
	public static Map<String, Proef> proeven = new HashMap<>();
	public static Map<String, String> handicap2018_combinatie_reeks = new HashMap<>();
	public static Map<String, Integer> handicap2018_combinatie_punten = new HashMap<>();
	public static Map<String, String> ruiters = new HashMap<>();
	public static Map<String, String> paarden = new HashMap<>();
	public static List<Handicap> nieuweHandicap2019 = new ArrayList<>();
	public static Map<String, Handicap> nieuweHandicap2019_combinatie_handicao = new HashMap<>();

	public static void main(String[] args) throws Exception {
//		SpringApplication.run(HandicapApplication.class, args);

		readOldHandicap();
		readRuiters();
		readPaarden();

		readProeven();
		LOG.info("Aantal proeven : {}", proeven.size());

		readHandicap();
		LOG.info("Aantal combinaties : {}", puntenBehaald2018.size());

		exportHandicap();
	}

	private static void exportHandicap() throws Exception {
		int reeksA = 0;
		int reeksB = 0;
		int reeks1 = 0;
		int reeks2 = 0;

		for (Map.Entry<String, List<Handicap>> combinatie : puntenBehaald2018.entrySet()) {
			String key = combinatie.getKey();
			String[] splitted = StringUtils.split(key, "_");
			List<Handicap> handicapList = combinatie.getValue();

			int punten = 0;
			for (Handicap handicap : handicapList) {
				punten += handicap.getPunten();
			}

			String reeks;
			if (punten == 0) {
				reeks = "A";
				reeksA++;
			} else {
				String oudeHandicap = handicap2018_combinatie_reeks.get(key);

				if (oudeHandicap == null) {
					LOG.error("oudeHandicap NOT FOUND");
				}

				if (punten <= 1200) {
					if (oudeHandicap.equalsIgnoreCase("A")) {
						reeksA++;

						reeks = "A";
					} else {
						reeksB++;
						reeks = "B";
						punten = 1201;
					}
				} else if (punten <= 2400) {
					reeks = "B";
					reeksB++;
				} else if (punten <= 3600) {
					reeks = "1";
					reeks1++;
				} else {
					reeks = "2";
					reeks2++;
				}
			}

			Handicap nieuweHandicap = new Handicap(splitted[0], splitted[1], punten, reeks);
			nieuweHandicap2019.add(nieuweHandicap);
			nieuweHandicap2019_combinatie_handicao.put(key, nieuweHandicap);

			//LOG.info("Ruiter {} - Paard {} - Punten {} - Reeks {}", splitted[0], splitted[1], punten, reeks);
		}

		for (Map.Entry<String, String> handicap2018EntrySet : handicap2018_combinatie_reeks.entrySet()) {
			if (!puntenBehaald2018.containsKey(handicap2018EntrySet.getKey())) {
				String[] splitted = StringUtils.split(handicap2018EntrySet.getKey(), "_");

				String handicapValue = handicap2018EntrySet.getValue();
				if (!"A".equalsIgnoreCase(handicapValue)) {
					handicapValue = "B";
				}

				Handicap nieuweHandicap = new Handicap(splitted[0], splitted[1], 0, handicapValue);
				nieuweHandicap2019.add(nieuweHandicap);
			}
		}

		LOG.info("A {} - B {} - 1 {} - 2 {}", reeksA, reeksB, reeks1, reeks2);


		String exportHandicap = DIRECTORY_OUTPUT + "new_export.csv";
		FileWriter out = new FileWriter(exportHandicap);
		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT)) {
				printer.printRecords(nieuweHandicap2019);
		}

		String exportOverzicht = DIRECTORY_OUTPUT + "new_overzicht_punten.csv";
		out = new FileWriter(exportOverzicht);
		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT)) {
			for (Map.Entry<String, List<Handicap>> punten : puntenBehaald2018.entrySet()) {
				printer.printRecords(punten.getValue());
			}
		}

		String exportSite = DIRECTORY_OUTPUT + "new_handicap_site.csv";
		out = new FileWriter(exportSite);
		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT)) {
			for (Map.Entry<String, Integer> combinatiePunten : handicap2018_combinatie_punten.entrySet()) {
				String[] split = StringUtils.split(combinatiePunten.getKey(), "_");

				String ruiter = ruiters.get(split[0]);
				String paard = paarden.get(split[1]);

				int punten;
				String reeks;
				Handicap nieuweReeks = nieuweHandicap2019_combinatie_handicao.get(combinatiePunten.getKey());
				if (nieuweReeks == null) {
					punten = combinatiePunten.getValue();
					reeks = "A";
				} else {
					punten = nieuweReeks.getPunten();
					reeks = nieuweReeks.getProef();
				}

				StringBuilder sb = new StringBuilder();
				sb.append(split[0]).append(';');
				sb.append(ruiter != null ? ruiter : "").append(';');
				sb.append(split[1]).append(';');
				sb.append(paard != null ? paard : paard).append(';');
				sb.append(punten).append(';');
				sb.append(reeks).append(';');

				printer.printRecord(sb);
			}
		}
	}

	private static void readHandicap() throws Exception {
		String fileHandicap = DIRECTORY_INPUT + "handicap_punten.csv";

		Reader in = new FileReader(fileHandicap);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);

		for (CSVRecord record : records) {
			String ruiterCSV = record.get(0);
			String paardCSV = record.get(1);
			int puntenCSV = Integer.parseInt(record.get(2));
			String proefCsv = record.get(3);

			Handicap handicap = new Handicap(ruiterCSV, paardCSV, puntenCSV, proefCsv);

			List<Handicap> punten = puntenBehaald2018.get(handicap.getKey());
			if (punten == null) {
				punten = new ArrayList<>();
				puntenBehaald2018.put(handicap.getKey(), punten);
			}

			if (proefCsv.contains("manueel") || proefCsv.contains("prijs")) {
			//	LOG.info("Skip");
			} else {
				// check if points should be doubled

				Proef proef = proeven.get(handicap.getProef());
				if (proef == null) {
					LOG.error("PROEF <{}> NOT FOUND", handicap.getProef());
				}

				if (proef.getType().startsWith("J")) {
					Integer nieuwePunten;
					switch (handicap.getPunten()) {
						case 200 : nieuwePunten = 100; break;
						case 150 : nieuwePunten = 100; break;
						default : nieuwePunten = 50; break;
					}

					if (nieuwePunten == null) {
						LOG.error("PROEF <{}> NOT FOUND", handicap.getProef());
					}

			//		LOG.info("Change points from {} to {}", handicap.getPunten(), nieuwePunten);
					handicap.setPunten(nieuwePunten);
				}

				punten.add(handicap);
			}

		}


	}

	private static void readProeven() throws Exception {
		String fileProef = DIRECTORY_INPUT + "proef.csv";

		Reader in = new FileReader(fileProef);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);

		for (CSVRecord record : records) {
			String wedstrijdCsv = record.get(1);
			String proefCsv = record.get(3);
			String typeCsv = record.get(6);

			Proef proef = new Proef(wedstrijdCsv, proefCsv, typeCsv);
			proeven.put(proef.getKey(), proef);
		}
	}

	private static void readRuiters() throws Exception {
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
		String fileProef = DIRECTORY_INPUT + "paarden.csv";

		Reader in = new FileReader(fileProef);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').parse(in);

		for (CSVRecord record : records) {
			String id = record.get(0);
			String naam = record.get(1);

			paarden.put(id, naam);
		}
	}

	private static void readOldHandicap() throws Exception {
		String fileProef = DIRECTORY_INPUT + "export.csv";

		Reader in = new FileReader(fileProef);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').parse(in);

		for (CSVRecord record : records) {
			if (StringUtils.isEmpty( record.get(0).trim()) || StringUtils.isEmpty( record.get(1).trim())) {
				LOG.info("SKIP");
				continue;
			}

			String combinatie = record.get(0) + "_" + record.get(1);
			String handicap = record.get(3);

			handicap2018_combinatie_reeks.put(combinatie, handicap);
			handicap2018_combinatie_punten.put(combinatie, Integer.parseInt(record.get(2)));
		}
	}

}

