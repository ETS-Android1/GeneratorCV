package pl.remindapp.cvObjects;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class Person implements Serializable {
    private String name;
    private String surname;
    private String emailAddress;
    private String shortInfo;
    private LocalDate dateOfBirth;
    private int phoneNumber;
    private Address address;
    private List<String> interest;
    private List<String> skills;
    private List<LifeEvent> experience;
    private List<LifeEvent>  courses;
    private List<LifeEvent>  education;
    private String imageFile;
    private int rotationAngle;
}
