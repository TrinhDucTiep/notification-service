package org.example.repositories;

import org.example.models.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Integer> {
}
