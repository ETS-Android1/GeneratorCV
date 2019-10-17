package pl.remindapp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbilityModel {
    String ability;

    @Override
    public String toString() {
        return ability;
    }
}
