package fr.vlock.app.ui.map;

public class StationElement {

    private String titre, etat, desc;
    private double latitude, longitude;
    private int id;

    public StationElement(String titre, String etat, String desc, double latitude, double longitude, int id) {
        this.titre = titre;
        this.etat = etat;
        this.desc = desc;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
