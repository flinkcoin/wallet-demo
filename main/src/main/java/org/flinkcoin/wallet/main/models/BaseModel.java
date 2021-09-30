package org.flinkcoin.wallet.main.models;

/*-
 * #%L
 * Wallet - Main
 * %%
 * Copyright (C) 2021 Flink Foundation
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
