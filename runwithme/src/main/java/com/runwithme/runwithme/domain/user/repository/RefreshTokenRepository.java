package com.runwithme.runwithme.domain.user.repository;

import com.runwithme.runwithme.domain.user.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
