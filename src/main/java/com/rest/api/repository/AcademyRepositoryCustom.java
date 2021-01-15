package com.rest.api.repository;

import com.rest.api.domain.Academy;
import java.util.List;

public interface AcademyRepositoryCustom  {
    List<Academy> findByName(String name);
}