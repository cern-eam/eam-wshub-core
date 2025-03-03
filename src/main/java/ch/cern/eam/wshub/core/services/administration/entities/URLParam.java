package ch.cern.eam.wshub.core.services.administration.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class URLParam {
    private String paramName;
    private String paramValue;
    private boolean system;
    private boolean useFieldValue;
}
