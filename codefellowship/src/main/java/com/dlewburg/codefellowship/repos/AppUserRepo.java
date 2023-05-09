package com.dlewburg.codefellowship.repos;

import com.dlewburg.codefellowship.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepo extends JpaRepository<AppUser, Long> {
    public AppUser findByUsername(String username);
}
