package be.kempischeregionale.handicap.models;

public class Proef {

    private String wedstrijd;
    private String proef;
    private String type;

    public Proef(String wedstrijd, String proef, String type) {
        this.wedstrijd = wedstrijd;
        this.proef = proef;
        this.type = type;
    }

    public String getKey() {
        return wedstrijd + "_" + proef;
    }

    public String getWedstrijd() {
        return wedstrijd;
    }

    public void setWedstrijd(String wedstrijd) {
        this.wedstrijd = wedstrijd;
    }

    public String getProef() {
        return proef;
    }

    public void setProef(String proef) {
        this.proef = proef;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
