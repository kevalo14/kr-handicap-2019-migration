package be.kempischeregionale.handicap.models;

public class Handicap {

    private String ruiter;
    private String paard;
    private int punten;
    private String proef;

    public Handicap(String ruiter, String paard, int punten, String proef) {
        this.ruiter = ruiter;
        this.paard = paard;
        this.punten = punten;
        this.proef = proef;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(ruiter).append(';');
        sb.append(paard).append(';');
        sb.append(punten).append(';');
        sb.append(proef).append(';');
        return sb.toString();
    }

    public String getKey() {
        return ruiter + "_" + paard;
    }

    public String getRuiter() {
        return ruiter;
    }

    public void setRuiter(String ruiter) {
        this.ruiter = ruiter;
    }

    public String getPaard() {
        return paard;
    }

    public void setPaard(String paard) {
        this.paard = paard;
    }

    public int getPunten() {
        return punten;
    }

    public void setPunten(int punten) {
        this.punten = punten;
    }

    public String getProef() {
        return proef;
    }

    public void setProef(String proef) {
        this.proef = proef;
    }
}
