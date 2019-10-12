package pl.remindapp.cvObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LifeEvent implements Serializable {
    private LocalDate begin;
    private LocalDate end;
    private String title;
    private String description;
}
