package org.fhi360.lamis.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fhi360.lamis.utility.Scrambler;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaseManagerDTO2 {
    private Long casemanagerId;
    private String surname;
    private String otherNames;
    private String currentStatus;
    private String address;
    private String dateBirth;
    private String gender;
    private String hospitalNum;
    private String name;
    private String fullName;
    private Long patientId;
    Scrambler scrambler = new Scrambler();
    @JsonIgnore
    public Scrambler getScrambler() {
        return scrambler;
    }

    public String getAddress() {
        return scrambler.unscrambleCharacters(address);
    }

    public String getName() {
        return scrambler.unscrambleCharacters(otherNames + " " + surname);
    }

}
