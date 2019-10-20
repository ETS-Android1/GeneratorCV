package pl.remindapp.cvObjects;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address implements Serializable {
    private String street;
    private Integer houseNumber;
    private Integer flatNumber;
    private Character houseLetter;
    private String postCode;
    private String city;

    @Override
    public String toString() {
        String result = new String();
        result += "ul." + street + " " + houseNumber;
        if(houseLetter != null)
            result += houseLetter;
        if(flatNumber >= 0)
            result += "/" + flatNumber;
        result +="\n" + postCode + " " + city;
        return  result;
    }
}
