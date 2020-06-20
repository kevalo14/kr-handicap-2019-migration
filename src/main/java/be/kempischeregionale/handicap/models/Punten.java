package be.kempischeregionale.handicap.models;

import java.util.HashMap;
import java.util.Map;

public class Punten {

    private String reeks;
    private int punten;
    private int extraPunten;
    private String reasonExtraPunten;

    private int paardPonyPunten;
    private String jumping;
    private String proef;

    public String getJumping() {
        return jumping;
    }

    public void setJumping(String jumping) {
        this.jumping = jumping;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
     //   sb.append("_").append(jumping);
        sb.append("_").append(proef);
        sb.append("_").append(punten);
        sb.append("_").append(extraPunten);
        sb.append("_").append(reasonExtraPunten != null ? reasonExtraPunten : "");
        sb.append('_');
        return sb.toString();
    }

    public Punten(String proef) {
        this.proef = proef;
    }

    public String getReeks() {
        return reeks;
    }

    public void setReeks(String reeks) {
        this.reeks = reeks;
    }

    public int getPunten() {
        return punten;
    }

    public void setPunten(int punten) {
        this.punten = punten;
    }

    public int getExtraPunten() {
        return extraPunten;
    }

    public void setExtraPunten(int extraPunten) {
        this.extraPunten = extraPunten;
    }

    public String getReasonExtraPunten() {
        return reasonExtraPunten;
    }

    public void setReasonExtraPunten(String reasonExtraPunten) {
        this.reasonExtraPunten = reasonExtraPunten;
    }

    public int getPaardPonyPunten() {
        return paardPonyPunten;
    }

    public void setPaardPonyPunten(int paardPonyPunten) {
        this.paardPonyPunten = paardPonyPunten;
    }

    public String getProef() {
        return proef;
    }

    public void setProef(String proef) {
        this.proef = proef;
    }
}
