package pl.remindapp.cvObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LifeEvent implements Serializable,Comparable<LifeEvent> {
    private LocalDate begin;
    private LocalDate end;
    private String title;
    private String description;


    @Override
    public int compareTo(LifeEvent o) {
        if(begin != null ) {
            if (o.begin != null) {
                return -begin.compareTo(o.begin);
            }
            return -1;
        }
        else{
            if(o.begin != null)
                return 1;
            else
                return 0;
        }

    }
}
