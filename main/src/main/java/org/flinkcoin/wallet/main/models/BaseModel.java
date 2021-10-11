package org.flinkcoin.wallet.main.models;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseModel implements Serializable {

    public abstract Integer getId();

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BaseModel)) {
            return false;
        }
        return Objects.equals(getId(), ((BaseModel) object).getId());
    }

    // emmanuel: I depend on this in UnitFirmwareService where I have a HashSet<UnitTms>
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(getId());
        return hash;
    }
}
