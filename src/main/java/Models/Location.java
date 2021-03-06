/**
 * Author: jatenderjossan
 * Created on: Dec. 25, 2021
 *
 * The Location.java class represents a location which a user can create
 *      in order to save the location of an event
 */

package Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private String building;
    // room of the building
    private String room;

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

    public boolean checkNotEmpty(String string)
    {
        return !Objects.equals(string, "") && string != null;
    }

    public String mergeTwoStrings(String string1, String string2)
    {
        if(checkNotEmpty(string1) && checkNotEmpty(string2))
        {
            return string1 + " " + string2;
        }
        else if (checkNotEmpty(string1) && !checkNotEmpty(string2))
        {
            return string1;
        }
        return string2;
    }

    @Override
    public String toString()
    {
        String[] temp = {
                mergeTwoStrings(this.getStreet(), this.getStreetNumber()),
                mergeTwoStrings(this.getCity(), this.getZip()),
                (checkNotEmpty(this.getCountry())) ? this.getCountry() : null,
                (checkNotEmpty(this.getBuilding())) ? "building " + this.getBuilding() : null,
                (checkNotEmpty(this.getRoom())) ? "room " + this.getRoom() : null};

        List<String> result = new ArrayList<String>();
        for(String string : temp) {
            if(checkNotEmpty(string)) {
                result.add(string);
            }
        }

        return String.join(", ", result);
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

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setRoom(String room) {
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

    public String getBuilding() {
        return building;
    }

    public String getRoom() {
        return room;
    }
}
