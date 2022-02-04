/**
 * Author: jatenderjossan
 * Created on: Dec. 25, 2021
 *
 * The Location.java class represents a location which a user can create
 *      in order to save the location of an event
 */

package Models;

public class Location {

    // location ID
    private int locationID;
    // street of a location
    private String street;
    // house Number of a location
    private String houseNumber;
    // zip of a location
    private String zip;
    // city of a location
    private String city;
    // country of a location
    private String country;
    // if there are multiple building with different letters e.g. building b (Gebäude b)
    private int building;
    // room of the building
    private int room;

    /**
     * Constructor for creating a location.
     *
     * @param street - street of a location
     * @param houseNumber - house Number of a location
     * @param zip - zip of a location
     * @param city - city of a location
     * @param country - country of a location
     * @param building - if there are multiple building with different letters e.g. building b (Gebäude b)
     * @param room -  room of the building
     */
    public Location(String street, String houseNumber, String zip, String city, String country, String building, String room) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.zip = zip;
        this.city = city;
        this.country = country;
        this.building = building;
        this.room = room;
    }

    /**
     * Constructor for fetching a location from the database
     *
     * @param locationID - ID of the location
     * @param street - street of a location
     * @param houseNumber - house Number of a location
     * @param zip - zip of a location
     * @param city - city of a location
     * @param country - country of a location
     * @param building - if there are multiple building with different letters e.g. building b (Gebäude b)
     * @param room -  room of the building
     */
    public Location(int locationID, String street, String houseNumber, String zip, String city, String country, String building, String room) {
        this.locationID = locationID;
        this.street = street;
        this.houseNumber = houseNumber;
        this.zip = zip;
        this.city = city;
        this.country = country;
        this.building = building;
        this.room = room;
    }

    //###############################################
    //                   Setter
    //###############################################

    public void setLocationID (int locationID) {
        this.locationID = locationID;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setStreetNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setBuilding(int building) {
        this.building = building;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    //###############################################
    //                   Getter
    //###############################################

    public int getLocationID() {
        return locationID;
    }

    public String getStreet() {
        return street;
    }

    public String getStreetNumber() {
        return houseNumber;
    }

    public String getZip() {
        return zip;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public int getBuilding() {
        return building;
    }

    public int getRoom() {
        return room;
    }
}
