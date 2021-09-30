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

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = AppUserModel.TABLE_NAME)
public class AppUserModel extends BaseModel {

    public static final String TABLE_NAME = "app_user";

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "app_user_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "app_user_id_seq", sequenceName = "app_user_id_seq", allocationSize = 1)
    private Integer id;

    /**
     * Also can be email.
     */
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "username", nullable = false)
    private String username;

    //password should not be serialized!
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "password_last_changed_at")
    private ZonedDateTime passwordLastChangedAt;

    @Column(name = "last_login_at")
    private ZonedDateTime lastLoginAt;

    @Column(name = "locked_at")
    private ZonedDateTime lockedAt;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "failed_logins", nullable = false)
    private int failedLogins;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Deprecated
    public AppUserModel() {
        this.deleted = false;
        this.failedLogins = 0;
    }

    public AppUserModel(String fullName, String username, String password, String phoneNumber) {
        this();
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public ZonedDateTime getPasswordLastChangedAt() {
        return passwordLastChangedAt;
    }

    public ZonedDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public ZonedDateTime getLockedAt() {
        return lockedAt;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getFailedLogins() {
        return failedLogins;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordLastChangedAt(ZonedDateTime passwordLastChangedAt) {
        this.passwordLastChangedAt = passwordLastChangedAt;
    }

    public void setLastLoginAt(ZonedDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public void setFailedLogins(int failedLogins) {
        this.failedLogins = failedLogins;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLockedAt(ZonedDateTime lockedAt) {
        this.lockedAt = lockedAt;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
