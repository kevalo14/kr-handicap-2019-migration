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

		proeven.put("5002_4", new Proef("5002", "4", "A"));
		proeven.put("5002_5", new Proef("5002", "5", "B"));
		proeven.put("5002_11", new Proef("5002", "11", "1"));
		proeven.put("5002_12", new Proef("5002", "12", "2"));
		proeven.put("5002_13", new Proef("5002", "13", "B"));
		proeven.put("5002_14", new Proef("5002", "14", "1"));
		proeven.put("5002_15", new Proef("5002", "15", "2"));

		proeven.put("5003_1", new Proef("5003", "1", "1"));
		proeven.put("5003_2", new Proef("5003", "2", "2"));
		proeven.put("5003_4", new Proef("5003", "4", "A"));
		proeven.put("5003_5", new Proef("5003", "5", "A"));
		proeven.put("5003_6", new Proef("5003", "6", "A"));
		proeven.put("5003_15", new Proef("5003", "15", "JA"));
		proeven.put("5003_16", new Proef("5003", "16", "JB"));
		proeven.put("5003_17", new Proef("5003", "17", "A"));
		proeven.put("5003_18", new Proef("5003", "18", "B"));
		proeven.put("5003_19", new Proef("5003", "19", "1"));
		proeven.put("5003_20", new Proef("5003", "20", "2"));

		proeven.put("5004_15", new Proef("5004", "15", "A"));
		proeven.put("5004_16", new Proef("5004", "16", "B"));
		proeven.put("5004_17", new Proef("5004", "17", "A"));
		proeven.put("5004_18", new Proef("5004", "18", "B"));
		proeven.put("5004_19", new Proef("5004", "19", "1"));
		proeven.put("5004_20", new Proef("5004", "20", "2"));

		proeven.put("5005_5", new Proef("5005", "5", "1"));
		proeven.put("5005_8", new Proef("5005", "8", "1"));
		proeven.put("5005_9", new Proef("5005", "9", "A"));
		proeven.put("5005_10", new Proef("5005", "10", "B"));
		proeven.put("5005_11", new Proef("5005", "11", "A"));
		proeven.put("5005_12", new Proef("5005", "12", "B"));
		proeven.put("5005_13", new Proef("5005", "13", "2"));
		proeven.put("5005_16", new Proef("5005", "16", "2"));
		proeven.put("5005_19", new Proef("5005", "19", "A"));
		proeven.put("5005_20", new Proef("5005", "20", "1"));
		proeven.put("5005_21", new Proef("5005", "21", "B"));

		proeven.put("5006_3", new Proef("5006", "3", "A"));
		proeven.put("5006_4", new Proef("5006", "4", "B"));
		proeven.put("5006_5", new Proef("5006", "5", "A"));
		proeven.put("5006_6", new Proef("5006", "6", "B"));
		proeven.put("5006_7", new Proef("5006", "7", "A"));
		proeven.put("5006_8", new Proef("5006", "8", "B"));
		proeven.put("5006_14", new Proef("5006", "14", "1"));
		proeven.put("5006_15", new Proef("5006", "15", "2"));
		proeven.put("5006_17", new Proef("5006", "17", "A"));
		proeven.put("5006_18", new Proef("5006", "18", "A"));
		proeven.put("5006_19", new Proef("5006", "19", "2"));
		proeven.put("5006_21", new Proef("5006", "21", "1"));

		proeven.put("5007_1", new Proef("5007", "1", "A"));
		proeven.put("5007_2", new Proef("5007", "2", "B"));
		proeven.put("5007_4", new Proef("5007", "4", "A"));
		proeven.put("5007_5", new Proef("5007", "5", "A"));
		proeven.put("5007_6", new Proef("5007", "6", "1"));
		proeven.put("5007_9", new Proef("5007", "9", "1"));
		proeven.put("5007_10", new Proef("5007", "10", "2"));
		proeven.put("5007_11", new Proef("5007", "11", "A"));
		proeven.put("5007_12", new Proef("5007", "12", "B"));
		proeven.put("5007_13", new Proef("5007", "13", "B"));
		proeven.put("5007_14", new Proef("5007", "14", "1"));
		proeven.put("5007_15", new Proef("5007", "15", "2"));
		proeven.put("5007_16", new Proef("5007", "16", "A"));
		proeven.put("5007_17", new Proef("5007", "17", "1"));

		proeven.put("5008_3", new Proef("5008", "3", "A"));
		proeven.put("5008_5", new Proef("5008", "5", "1"));
		proeven.put("5008_6", new Proef("5008", "6", "2"));
		proeven.put("5008_8", new Proef("5008", "8", "A"));
		proeven.put("5008_9", new Proef("5008", "9", "A"));
		proeven.put("5008_10", new Proef("5008", "10", "B"));
		proeven.put("5008_11", new Proef("5008", "11", "1"));
		proeven.put("5008_12", new Proef("5008", "12", "2"));

		proeven.put("5009_9", new Proef("5009", "9", "A"));
		proeven.put("5009_10", new Proef("5009", "10", "JB"));
		proeven.put("5009_11", new Proef("5009", "11", "JA"));
		proeven.put("5009_12", new Proef("5009", "12", "JB"));

		proeven.put("5010_9", new Proef("5010", "9", "A"));
		proeven.put("5010_10", new Proef("5010", "10", "B"));
		proeven.put("5010_11", new Proef("5010", "11", "A"));
		proeven.put("5010_12", new Proef("5010", "12", "A"));
		proeven.put("5010_13", new Proef("5010", "13", "1"));
		proeven.put("5010_14", new Proef("5010", "14", "2"));
		proeven.put("5010_16", new Proef("5010", "16", "2"));
		proeven.put("5010_17", new Proef("5010", "17", "B"));

		proeven.put("5012_8", new Proef("5012", "8", "A"));
		proeven.put("5012_9", new Proef("5012", "9", "JB"));
		proeven.put("5012_10", new Proef("5012", "10", "JA"));
		proeven.put("5012_11", new Proef("5012", "11", "JB"));

		proeven.put("5013_1", new Proef("5013", "1", "A"));
		proeven.put("5013_4", new Proef("5013", "4", "A"));
		proeven.put("5013_10", new Proef("5013", "10", "B", true));
		proeven.put("5013_11", new Proef("5013", "11", "1", true));
		proeven.put("5013_12", new Proef("5013", "12", "2", true));
		proeven.put("5013_13", new Proef("5013", "13", "B"));
		proeven.put("5013_14", new Proef("5013", "14", "1"));
		proeven.put("5013_15", new Proef("5013", "15", "2"));
		proeven.put("5013_16", new Proef("5013", "16", "B"));
		proeven.put("5013_17", new Proef("5013", "17", "1"));
		proeven.put("5013_18", new Proef("5013", "18", "2"));
		proeven.put("5013_20", new Proef("5013", "20", "A"));
		proeven.put("5013_21", new Proef("5013", "21", "A"));

		proeven.put("5043_1", new Proef("5043", "1", "A"));
		proeven.put("5043_2", new Proef("5043", "2", "A"));
		proeven.put("5043_3", new Proef("5043", "3", "A"));
		proeven.put("5043_4", new Proef("5043", "4", "A"));
		proeven.put("5043_5", new Proef("5043", "5", "B"));
		proeven.put("5043_8", new Proef("5043", "8", "B"));
		proeven.put("5043_9", new Proef("5043", "9", "1"));
		proeven.put("5043_10", new Proef("5043", "10", "2"));
		proeven.put("5043_11", new Proef("5043", "11", "A"));

		proeven.put("5014_3", new Proef("5014", "3", "A"));
		proeven.put("5014_4", new Proef("5014", "4", "B"));
		proeven.put("5014_5", new Proef("5014", "5", "JA"));
		proeven.put("5014_6", new Proef("5014", "6", "A"));
		proeven.put("5014_7", new Proef("5014", "7", "JA"));
		proeven.put("5014_16", new Proef("5014", "16", "JA"));
		proeven.put("5014_17", new Proef("5014", "17", "A"));
		proeven.put("5014_19", new Proef("5014", "19", "A"));
		proeven.put("5014_20", new Proef("5014", "20", "A"));
		proeven.put("5014_21", new Proef("5014", "21", "1"));
		proeven.put("5014_22", new Proef("5014", "22", "2"));
		proeven.put("5014_23", new Proef("5014", "23", "1"));
		proeven.put("5014_24", new Proef("5014", "24", "2"));

		proeven.put("5015_9", new Proef("5015", "9", "B"));
		proeven.put("5015_10", new Proef("5015", "10", "A"));
		proeven.put("5015_11", new Proef("5015", "11", "B"));
		proeven.put("5015_12", new Proef("5015", "12", "1"));
		proeven.put("5015_13", new Proef("5015", "13", "A"));
		proeven.put("5015_14", new Proef("5015", "14", "B"));
		proeven.put("5015_15", new Proef("5015", "15", "2"));
		proeven.put("5015_16", new Proef("5015", "16", "1"));
		proeven.put("5015_17", new Proef("5015", "17", "A"));
		proeven.put("5015_18", new Proef("5015", "18", "2"));
		proeven.put("5015_19", new Proef("5015", "19", "1"));

		proeven.put("5016_9", new Proef("5016", "9", "A"));
		proeven.put("5016_10", new Proef("5016", "10", "B"));
		proeven.put("5016_12", new Proef("5016", "12", "A"));
		proeven.put("5016_13", new Proef("5016", "13", "B"));
		proeven.put("5016_14", new Proef("5016", "14", "2"));
		proeven.put("5016_15", new Proef("5016", "15", "1"));
		proeven.put("5016_16", new Proef("5016", "16", "2"));

		proeven.put("5018_8", new Proef("5018", "8", "A"));
		proeven.put("5018_9", new Proef("5018", "9", "B"));
		proeven.put("5018_10", new Proef("5018", "10", "A"));
		proeven.put("5018_11", new Proef("5018", "11", "B"));
		proeven.put("5018_12", new Proef("5018", "12", "1"));
		proeven.put("5018_13", new Proef("5018", "13", "2"));
		proeven.put("5018_14", new Proef("5018", "14", "A"));
		proeven.put("5018_16", new Proef("5018", "16", "A"));

		proeven.put("5019_3", new Proef("5019", "3", "A"));
		proeven.put("5019_4", new Proef("5019", "4", "B"));
		proeven.put("5019_6", new Proef("5019", "6", "A"));
		proeven.put("5019_7", new Proef("5019", "7", "B"));
		proeven.put("5019_9", new Proef("5019", "9", "A"));
		proeven.put("5019_10", new Proef("5019", "10", "A"));
		proeven.put("5019_11", new Proef("5019", "11", "1"));
		proeven.put("5019_12", new Proef("5019", "12", "2"));
		proeven.put("5019_13", new Proef("5019", "13", "1"));
		proeven.put("5019_14", new Proef("5019", "14", "B"));

		proeven.put("5020_6", new Proef("5020", "6", "A"));
		proeven.put("5020_9", new Proef("5020", "9", "A"));
		proeven.put("5020_10", new Proef("5020", "10", "JB"));
		proeven.put("5020_11", new Proef("5020", "11", "JA"));
		proeven.put("5020_12", new Proef("5020", "12", "JB"));

		proeven.put("5021_3", new Proef("5021", "3", "A"));
		proeven.put("5021_4", new Proef("5021", "4", "A"));
		proeven.put("5021_5", new Proef("5021", "5", "A"));
		proeven.put("5021_12", new Proef("5021", "12", "A"));
		proeven.put("5021_13", new Proef("5021", "13", "A"));
		proeven.put("5021_14", new Proef("5021", "14", "A"));
		proeven.put("5021_15", new Proef("5021", "15", "A"));
		proeven.put("5021_16", new Proef("5021", "16", "A"));
		proeven.put("5021_17", new Proef("5021", "17", "A"));
		proeven.put("5021_18", new Proef("5021", "18", "A"));
		proeven.put("5021_19", new Proef("5021", "19", "A"));
		proeven.put("5021_20", new Proef("5021", "20", "A"));
		proeven.put("5021_21", new Proef("5021", "21", "A"));
		proeven.put("5021_22", new Proef("5021", "22", "A"));

		proeven.put("5022_3", new Proef("5022", "3", "1"));
		proeven.put("5022_4", new Proef("5022", "4", "A"));
		proeven.put("5022_6", new Proef("5022", "6", "1"));
		proeven.put("5022_7", new Proef("5022", "7", "A"));
		proeven.put("5022_8", new Proef("5022", "8", "B"));
		proeven.put("5022_10", new Proef("5022", "10", "A"));
		proeven.put("5022_11", new Proef("5022", "11", "B"));
		proeven.put("5022_12", new Proef("5022", "12", "2"));
		proeven.put("5022_13", new Proef("5022", "13", "A"));
		proeven.put("5022_14", new Proef("5022", "14", "1"));
		proeven.put("5022_15", new Proef("5022", "15", "B"));

		proeven.put("5023_6", new Proef("5023", "6", "B", true));
		proeven.put("5023_7", new Proef("5023", "7", "1", true));
		proeven.put("5023_8", new Proef("5023", "8", "2", true));
		proeven.put("5023_11", new Proef("5023", "11", "JA"));
		proeven.put("5023_12", new Proef("5023", "12", "JB"));
		proeven.put("5023_13", new Proef("5023", "13", "A"));
		proeven.put("5023_14", new Proef("5023", "14", "JB"));

		proeven.put("5025_8", new Proef("5025", "8", "A"));
		proeven.put("5025_9", new Proef("5025", "9", "B"));
		proeven.put("5025_10", new Proef("5025", "10", "A"));
		proeven.put("5025_11", new Proef("5025", "11", "B"));
		proeven.put("5025_15", new Proef("5025", "15", "A"));
		proeven.put("5025_17", new Proef("5025", "17", "A"));
		proeven.put("5025_18", new Proef("5025", "18", "A"));
		proeven.put("5025_19", new Proef("5025", "19", "1"));
		proeven.put("5025_20", new Proef("5025", "20", "2"));
		proeven.put("5025_21", new Proef("5025", "21", "B"));
		proeven.put("5025_22", new Proef("5025", "22", "1"));
		proeven.put("5025_23", new Proef("5025", "23", "2"));
		proeven.put("5025_24", new Proef("5025", "24", "A"));

		proeven.put("5026_11", new Proef("5026", "11", "1"));
		proeven.put("5026_12", new Proef("5026", "12", "JA"));
		proeven.put("5026_13", new Proef("5026", "13", "JB"));
		proeven.put("5026_14", new Proef("5026", "14", "JA"));
		proeven.put("5026_15", new Proef("5026", "15", "JB"));
		proeven.put("5026_16", new Proef("5026", "16", "2"));
		proeven.put("5026_17", new Proef("5026", "17", "A"));
		proeven.put("5026_18", new Proef("5026", "18", "B"));
		proeven.put("5026_19", new Proef("5026", "19", "A"));
		proeven.put("5026_20", new Proef("5026", "20", "B"));
		proeven.put("5026_21", new Proef("5026", "21", "1"));
		proeven.put("5026_22", new Proef("5026", "22", "JA"));
		proeven.put("5026_23", new Proef("5026", "23", "JB"));

		proeven.put("5027_1", new Proef("5027", "1", "B"));
		proeven.put("5027_2", new Proef("5027", "2", "1"));
		proeven.put("5027_3", new Proef("5027", "3", "2"));
		proeven.put("5027_7", new Proef("5027", "7", "A"));
		proeven.put("5027_8", new Proef("5027", "8", "B"));
		proeven.put("5027_10", new Proef("5027", "10", "A"));
		proeven.put("5027_11", new Proef("5027", "11", "B"));
		proeven.put("5027_12", new Proef("5027", "12", "1"));
		proeven.put("5027_14", new Proef("5027", "14", "2"));

		proeven.put("5028_1", new Proef("5028", "1", "A"));
		proeven.put("5028_2", new Proef("5028", "2", "B"));
		proeven.put("5028_5", new Proef("5028", "5", "A"));
		proeven.put("5028_6", new Proef("5028", "6", "B"));
		proeven.put("5028_12", new Proef("5028", "12", "B"));
		proeven.put("5028_13", new Proef("5028", "13", "1"));
		proeven.put("5028_14", new Proef("5028", "14", "2"));

		proeven.put("5029_3", new Proef("5029", "3", "A"));
		proeven.put("5029_6", new Proef("5029", "6", "2"));
		proeven.put("5029_7", new Proef("5029", "7", "A"));
		proeven.put("5029_10", new Proef("5029", "10", "A"));
		proeven.put("5029_11", new Proef("5029", "11", "B"));
		proeven.put("5029_12", new Proef("5029", "12", "1"));
		proeven.put("5029_14", new Proef("5029", "14", "2"));

		proeven.put("5030_4", new Proef("5030", "4", "A"));
		proeven.put("5030_5", new Proef("5030", "5", "B"));
		proeven.put("5030_6", new Proef("5030", "6", "1"));
		proeven.put("5030_7", new Proef("5030", "7", "2"));
		proeven.put("5030_10", new Proef("5030", "10", "A"));
		proeven.put("5030_11", new Proef("5030", "11", "B"));
		proeven.put("5030_17", new Proef("5030", "17", "B"));
		proeven.put("5030_18", new Proef("5030", "18", "1"));
		proeven.put("5030_19", new Proef("5030", "19", "2"));
		proeven.put("5030_20", new Proef("5030", "20", "A"));
		proeven.put("5030_21", new Proef("5030", "21", "B"));
		proeven.put("5030_22", new Proef("5030", "22", "1"));
		proeven.put("5030_23", new Proef("5030", "23", "2"));

		proeven.put("5031_4", new Proef("5031", "4", "A"));
		proeven.put("5031_5", new Proef("5031", "5", "B"));
		proeven.put("5031_6", new Proef("5031", "6", "1"));
		proeven.put("5031_8", new Proef("5031", "8", "A"));
		proeven.put("5031_9", new Proef("5031", "9", "B"));
		proeven.put("5031_12", new Proef("5031", "12", "A"));
		proeven.put("5031_13", new Proef("5031", "13", "B"));
		proeven.put("5031_14", new Proef("5031", "14", "1"));
		proeven.put("5031_15", new Proef("5031", "15", "B"));

		proeven.put("5032_8", new Proef("5032", "8", "A"));
		proeven.put("5032_9", new Proef("5032", "9", "B"));
		proeven.put("5032_10", new Proef("5032", "10", "A"));
		proeven.put("5032_11", new Proef("5032", "11", "B"));

		proeven.put("5033_1", new Proef("5033", "1", "A"));
		proeven.put("5033_2", new Proef("5033", "2", "B"));
		proeven.put("5033_3", new Proef("5033", "3", "1"));
		proeven.put("5033_5", new Proef("5033", "5", "A"));
		proeven.put("5033_6", new Proef("5033", "6", "2"));
		proeven.put("5033_7", new Proef("5033", "7", "1"));
		proeven.put("5033_8", new Proef("5033", "8", "2"));
		proeven.put("5033_10", new Proef("5033", "10", "1"));
		proeven.put("5033_12", new Proef("5033", "12", "B", true));
		proeven.put("5033_13", new Proef("5033", "13", "1", true));
		proeven.put("5033_14", new Proef("5033", "14", "2", true));
		proeven.put("5033_16", new Proef("5033", "16", "2"));

		proeven.put("5044_5", new Proef("5044", "5", "B", true));
		proeven.put("5044_6", new Proef("5044", "6", "1", true));
		proeven.put("5044_7", new Proef("5044", "7", "2", true));
		proeven.put("5044_8", new Proef("5044", "8", "B", true));
		proeven.put("5044_9", new Proef("5044", "9", "1", true));
		proeven.put("5044_10", new Proef("5044", "10", "2", true));

		proeven.put("5034_2", new Proef("5034", "2", "2"));
		proeven.put("5034_4", new Proef("5034", "4", "2"));
		proeven.put("5034_5", new Proef("5034", "5", "A"));
		proeven.put("5034_6", new Proef("5034", "6", "B"));
		proeven.put("5034_7", new Proef("5034", "7", "1"));
		proeven.put("5034_8", new Proef("5034", "8", "A"));

		proeven.put("5035_9", new Proef("5035", "9", "A"));
		proeven.put("5035_10", new Proef("5035", "10", "B"));
		proeven.put("5035_11", new Proef("5035", "11", "A"));
		proeven.put("5035_12", new Proef("5035", "12", "B"));

		proeven.put("5036_2", new Proef("5036", "2", "A"));
		proeven.put("5036_3", new Proef("5036", "3", "B"));
		proeven.put("5036_4", new Proef("5036", "4", "1"));
		proeven.put("5036_5", new Proef("5036", "5", "2"));
		proeven.put("5036_7", new Proef("5036", "7", "1"));
		proeven.put("5036_8", new Proef("5036", "8", "B"));
		proeven.put("5036_9", new Proef("5036", "9", "2"));
		proeven.put("5036_10", new Proef("5036", "10", "B"));
		proeven.put("5036_13", new Proef("5036", "13", "2"));
		proeven.put("5036_14", new Proef("5036", "14", "B"));
		proeven.put("5036_15", new Proef("5036", "15", "A"));

		proeven.put("5037_4", new Proef("5037", "4", "A"));

		proeven.put("5038_4", new Proef("5038", "4", "2"));

		readHandicap2018();
		readRuiters();
		readPaarden();
		readPaardVanHetJaar();

		readJumping(huidige_jumping);

//		exportPaardVanHetJaar();

//		readProeven();
//		LOG.info("Aantal proeven : {}", proeven.size());


//		exportHandicap();
	}

//	private static void exportHandicap() throws Exception {
//		int reeksA = 0;
//		int reeksB = 0;
//		int reeks1 = 0;
//		int reeks2 = 0;
//
//		for (Map.Entry<String, List<Handicap>> combinatie : puntenBehaald2018.entrySet()) {
//			String key = combinatie.getKey();
//			String[] splitted = StringUtils.split(key, "_");
//			List<Handicap> handicapList = combinatie.getValue();
//
//			int punten = 0;
//			for (Handicap handicap : handicapList) {
//				punten += handicap.getPunten();
//			}
//
//			String reeks;
//			if (punten == 0) {
//				reeks = "A";
//				reeksA++;
//			} else {
//				String oudeHandicap = handicap2018_combinatie_reeks.get(key);
//
//				if (oudeHandicap == null) {
//					LOG.error("oudeHandicap NOT FOUND");
//				}
//
//				if (punten <= 1200) {
//					if (oudeHandicap.equalsIgnoreCase("A")) {
//						reeksA++;
//
//						reeks = "A";
//					} else {
//						reeksB++;
//						reeks = "B";
//						punten = 1201;
//					}
//				} else if (punten <= 2400) {
//					reeks = "B";
//					reeksB++;
//				} else if (punten <= 3600) {
//					reeks = "1";
//					reeks1++;
//				} else {
//					reeks = "2";
//					reeks2++;
//				}
//			}
//
//			Handicap nieuweHandicap = new Handicap(splitted[0], splitted[1], punten, reeks);
//			nieuweHandicap2019.add(nieuweHandicap);
//			nieuweHandicap2019_combinatie_handicao.put(key, nieuweHandicap);
//
//			//LOG.info("Ruiter {} - Paard {} - Punten {} - Reeks {}", splitted[0], splitted[1], punten, reeks);
//		}
//
//		for (Map.Entry<String, String> handicap2018EntrySet : handicap2018_combinatie_reeks.entrySet()) {
//			if (!puntenBehaald2018.containsKey(handicap2018EntrySet.getKey())) {
//				String[] splitted = StringUtils.split(handicap2018EntrySet.getKey(), "_");
//
//				String handicapValue = handicap2018EntrySet.getValue();
//				if (!"A".equalsIgnoreCase(handicapValue)) {
//					handicapValue = "B";
//				}
//
//				Handicap nieuweHandicap = new Handicap(splitted[0], splitted[1], 0, handicapValue);
//				nieuweHandicap2019.add(nieuweHandicap);
//			}
//		}
//
//		LOG.info("A {} - B {} - 1 {} - 2 {}", reeksA, reeksB, reeks1, reeks2);
//
//
//		String exportHandicap = DIRECTORY_OUTPUT + "new_export.csv";
//		FileWriter out = new FileWriter(exportHandicap);
//		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT)) {
//				printer.printRecords(nieuweHandicap2019);
//		}
//
//		String exportOverzicht = DIRECTORY_OUTPUT + "new_overzicht_punten.csv";
//		out = new FileWriter(exportOverzicht);
//		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT)) {
//			for (Map.Entry<String, List<Handicap>> punten : puntenBehaald2018.entrySet()) {
//				printer.printRecords(punten.getValue());
//			}
//		}
//
//		String exportSite = DIRECTORY_OUTPUT + "new_handicap_site.csv";
//		out = new FileWriter(exportSite);
//		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT)) {
//			for (Map.Entry<String, Integer> combinatiePunten : handicap2018_combinatie_punten.entrySet()) {
//				String[] split = StringUtils.split(combinatiePunten.getKey(), "_");
//
//				String ruiter = ruiters.get(split[0]);
//				String paard = paarden.get(split[1]);
//
//				int punten;
//				String reeks;
//				Handicap nieuweReeks = nieuweHandicap2019_combinatie_handicao.get(combinatiePunten.getKey());
//				if (nieuweReeks == null) {
//					punten = combinatiePunten.getValue();
//					reeks = "A";
//				} else {
//					punten = nieuweReeks.getPunten();
//					reeks = nieuweReeks.getProef();
//				}
//
//				StringBuilder sb = new StringBuilder();
//				sb.append(split[0]).append(';');
//				sb.append(ruiter != null ? ruiter : "").append(';');
//				sb.append(split[1]).append(';');
//				sb.append(paard != null ? paard : paard).append(';');
//				sb.append(punten).append(';');
//				sb.append(reeks).append(';');
//
//				printer.printRecord(sb);
//			}
//		}
//	}


//
//	private static void readProeven() throws Exception {
//		String fileProef = DIRECTORY_INPUT + "proef.csv";
//
//		Reader in = new FileReader(fileProef);
//		Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
//
//		for (CSVRecord record : records) {
//			String wedstrijdCsv = record.get(1);
//			String proefCsv = record.get(3);
//			String typeCsv = record.get(6);
//
//			Proef proef = new Proef(wedstrijdCsv, proefCsv, typeCsv);
//			proeven.put(proef.getKey(), proef);
//		}
//	}

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

